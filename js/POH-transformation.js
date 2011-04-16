/*-THE ENTIRE HOUSE */

/*-TRANSLATION */

/*Adds the triggers to move the house */
function addMoveHouseTriggers(){
	$('#moveHouseList li a').overlay({
		onBeforeLoad : function(e){
			//work out what triggered the event
			$trigger = this.getTrigger();
			//fetch the values to pass to the function
			var xDirection = convertStringToBoolean($trigger.attr('xDirection'));
			var positiveDirection = convertStringToBoolean($trigger.attr('positiveDirection'));
			//make the text in the error message correct
			$('#moveHouseCompassDirection').html($trigger.attr('compass'));
			return moveHouse(xDirection, positiveDirection);
		}
	});
}

/*Moves the house 1 square in the specified direction
xDirection - true = move house from left to right, false = move house up and down
positiveDirection - true = move house toward bottom / right, false = move house towards top / left
Should only be called through a trigger related to the #demolishOnMove overlay
Returns true if a confirmation dialogue should be shown, false otherwise */
function moveHouse(xDirection, positiveDirection){
	var yChange = xDirection ? positiveDirection ? -1 : 1 : 0;
	var xChange = xDirection ? 0 : positiveDirection ? -1 : 1;
	
	//if there's something in the way
	if(hasRoomThatWouldBeDeletedByMove(xChange, yChange)){
		//if you haven't chosen to stop the warning message
		if(!$house.noConfirmation.demolishOnMove){
			//store the x and y changes encase the user wants to move the house anyway
			$savedVariables.xChange = xChange;
			$savedVariables.yChange = yChange;
			//show the confirmation dialogue
			return true;
		} //if remembered the 'yes' setting, move the house
		else if ($house.noConfSettings.demolishOnMove){
			moveHouseConfirmed(xChange, yChange);
		}
		return false;
	} else {
		//move the house
		moveHouseConfirmed(xChange, yChange);
		return false;
	}
}

/*Actually moves the house once it's been confirmed that it's possible and the user doesn't mind if rooms will be demolished by doing so */
function moveHouseConfirmed(xChange, yChange){
	var tempHouse = createEmptyHouseArray();
	
	//loop through the rooms and put things where they should be in a temporary array
	for(var i = 0; i < 3; i++){
		for(var j = 0; j < 9; j++){
			for(var k = 0; k < 9; k++){
				if((j + xChange) >= 0 && (k + yChange) >= 0 && (j + xChange) < 9 && (k + yChange) < 9){
					tempHouse[i][j][k] = houseArray[i][j + xChange][k + yChange];
				}
			}
		}
	}
	
	//calculate the Sharing Code for this trans house and load it	
	loadHouseFromSharingCode(calculateSharingCode(tempHouse));
}

/*Checks whether a room would be deleted if the house was moved in the specified manner
Returns true if it would, false otherwise */
function hasRoomThatWouldBeDeletedByMove(x, y){
	var obstruction = false; //innocent until proven guilty
	if(x === 1){
		obstruction = hasRoomAlongEdge(0, false);
	} else if(x === -1){
		obstruction = hasRoomAlongEdge(8, false);
	}
	if(y === 1){
		//only bother checking if there's been nothing found in the specified x-direction
		if(!obstruction){
			obstruction = hasRoomAlongEdge(0, true);
		}
	} else if(y === -1){
		if(!obstruction){
			obstruction = hasRoomAlongEdge(8, true);
		}
	}
	
	return obstruction;
}

/*Checks to see if there's a room built along the specified edge of the house
sideNumber = (edge === 'left' || edge === 'top') ? 0 : 8; //the number of the side
x = (edge === 'top' || edge === 'bottom') ? true : false; //whether you're checking a horizontal row
Returns true if there is, otherwise false */
function hasRoomAlongEdge(sideNumber, x){
	//loop through the floors
	for(var i = 0; i < 3; i++){
		//and one row / column on each floor
		for(var j = 0; j < 9; j++){
			if(x){
				if(houseArray[i][j][sideNumber].labelText !== ' '){
					return true;
				}
			} else {
				if(houseArray[i][sideNumber][j].labelText !== ' '){
					return true;
				}
			}
		}
	}
	return false;
}

/*-ROTATIONS AND REFLECTIONS */

/*=
[a, c]
[b, d]  is a 2x2 matrix that the parameters (a, b, c, d) represent

[j]
[k] is the 2x1 matrix that the 2x2 matrix is post-multiplied by

Because (0, 0) is in the top-left corner, anything other than a direct flip in the x or y axis will be in the opposite direction to what you'd expect
eg. anti-clockwise becomes clockwise and a reflection in y = x becomes a reflection in y = -x

'trans' means 'transformed', but takes up less space on the screen
*/

/*Transforms (moves) the house by the specified 2x2 matrix */
function transformHouseByMatrix(a, b, c, d){
	//console.time("transform");
	//create a temporary array so that you aren't overwriting anything
	var tempHouse = createEmptyHouseArray();
	var transCoords;
	
	//loop through the rooms and put things where they should be in a temporary array
	for(var i = 0; i < 3; i++){
		for(var j = 0; j < 9; j++){
			for(var k = 0; k < 9; k++){
				transCoords = transformRoomByMatrix(j, k, a, b, c, d);
				tempHouse[i][transCoords[0]][transCoords[1]] = houseArray[i][j][k];
				if(houseArray[i][j][k].labelText !== ' '){
					/*tempHouse[i][transCoords[0]][transCoords[1]].doorLayout = 
						transformDoorOrientationByMatrix(houseArray[i][j][k].doorLayout, a, b, c, d);*/
					tempHouse[i][transCoords[0]][transCoords[1]].doorLayout = 
						transformDoorOrientation(houseArray[i][j][k].doorLayout, a, b, c, d);
				}
			}
		}
	}
	
	//calculate the Sharing Code for this trans house and load it	
	loadHouseFromSharingCode(calculateSharingCode(tempHouse));
	//console.timeEnd("transform");
}

/*-TRANSFORMING THE DOOR LAYOUT */

/*Changes the door layout as the matrix specifies
It would be nicer if it wasn't static, but at least it works */
function transformDoorOrientation(doorLayout, a, b, c, d){
	var transDoorLayout = new Array(4);
	if(a === 1 && b === 0 && c === 0 && d === -1){
		//reflection in x-axis
		transDoorLayout[0] = doorLayout[2];
		transDoorLayout[2] = doorLayout[0];
		transDoorLayout[1] = doorLayout[1];
		transDoorLayout[3] = doorLayout[3];
	} else if(a === -1 && b === 0 && c === 0 && d === 1){
		//reflection in y-axis
		transDoorLayout[1] = doorLayout[3];
		transDoorLayout[3] = doorLayout[1];
		transDoorLayout[0] = doorLayout[0];
		transDoorLayout[2] = doorLayout[2];
	} else if(a === 0 && b === -1 && c === -1 && d === 0){
		//reflection in y = x
		transDoorLayout[0] = doorLayout[1];
		transDoorLayout[1] = doorLayout[0];
		transDoorLayout[2] = doorLayout[3];
		transDoorLayout[3] = doorLayout[2];
	} else if(a === 0 && b === 1 && c === 1 && d === 0){
		//reflection in y = -x
		transDoorLayout[0] = doorLayout[3];
		transDoorLayout[3] = doorLayout[0];
		transDoorLayout[2] = doorLayout[1];
		transDoorLayout[1] = doorLayout[2];
	} else if(a === 0 && b === 1 && c === -1 && d === 0){
		//rotation clockwise 90 degrees
		transDoorLayout[0] = doorLayout[3];
		transDoorLayout[1] = doorLayout[0];
		transDoorLayout[2] = doorLayout[1];
		transDoorLayout[3] = doorLayout[2];
	} else if(a === 0 && b === -1 && c === 1 && d === 0){
		//rotation anti-clockwise 90 degrees
		transDoorLayout[0] = doorLayout[1];
		transDoorLayout[1] = doorLayout[2];
		transDoorLayout[2] = doorLayout[3];
		transDoorLayout[3] = doorLayout[0];
	} else if(a === -1 && b === 0 && c === 0 && d === -1){
		//rotation 180 degrees
		transDoorLayout[0] = doorLayout[2];
		transDoorLayout[1] = doorLayout[3];
		transDoorLayout[2] = doorLayout[0];
		transDoorLayout[3] = doorLayout[1];
	}/* else if(a ===  && b ===  && c ===  && d === ){
		//
		transDoorLayout[] = doorLayout[];
		transDoorLayout[] = doorLayout[];
		transDoorLayout[] = doorLayout[];
		transDoorLayout[] = doorLayout[];
	}*/
	
	return transDoorLayout;
}

/*-TRANSFORMING THE LOCATION OF A ROOM */

/*Calculates the room location when trans by the specified 2x2 matrix
Returns the new [j, k] coordinates*/
function transformRoomByMatrix(j, k, a, b, c, d){
	//zero the coordinates of the center of the house
	j -= 4;
	k -= 4;
	
	//calculate the trans coordinates
	var transCoords = transformPointByMatrix(j, k, a, b, c, d);
	
	//return an array with the moved coordinates in, moved back to the middle of the house
	return [transCoords[0] + 4, transCoords[1] + 4];
}

/*Calculates the point location when trans by the specified 2x2 matrix
Returns the new [x, y] coordinates*/
function transformPointByMatrix(j, k, a, b, c, d){
	return [(j * a) + (k * c), (j * b) + (k * d)];
}

/*-ROTATING INDIVIDUAL ROOMS */

/*Rotates the selected room - true = Clockwise, false = Anti-Clockwise
The currently selected room is located since the Rotate buttons are only visible when you have a room selected */
function rotateRoom(clockwise, i, j, k){
	var $room = houseArray[$house.csr[0]][$house.csr[1]][$house.csr[2]];
	var temp;
	if(clockwise){
		temp = $room.doorLayout[3];
		$room.doorLayout[3] = $room.doorLayout[2];
		$room.doorLayout[2] = $room.doorLayout[1];
		$room.doorLayout[1] = $room.doorLayout[0];
		$room.doorLayout[0] = temp;
	} else {
		temp = $room.doorLayout[0];
		$room.doorLayout[0] = $room.doorLayout[1];
		$room.doorLayout[1] = $room.doorLayout[2];
		$room.doorLayout[2] = $room.doorLayout[3];
		$room.doorLayout[3] = temp;
	}
	//update the room image to show that it's been rotated
	changeRoomOverviewImage($house.csr[0], $house.csr[1], $house.csr[2]);
	
	//and update the Sharing Code
	calculateSharingCode(houseArray);
}


/*-DOESN'T WORK AS WANTED, SO LEFT TO ROT FOR THE MOMENT  - SEE transformDoorOrientation() INSTEAD */

/*Calculates the new door layout when the house is rotated by the specified 2x2 matrix
Each door location is represented by one of the corners of a unit square
This creates a 2x4 matrix (doorMatrix) which represents the four doors
Returns the transformed door layout
*/
function transformDoorOrientationByMatrix(doorLayout, a, b, c, d){
	var doorMatrix = getDoorMatrix();
	var transDoorMatrix = getDoorMatrix();
	var transDoorLayout = new Array(4);
	
	//loop through the doors
	for(var i = 0; i < 4; i++){
		/*console.log(transformPointByMatrix(doorMatrix[i][0], doorMatrix[i][1], -a, -b, -c, -d) + " " +
			doorMatrix[i]);
		console.log(getDoorLayoutPosition(
			transformPointByMatrix(doorMatrix[i][0], doorMatrix[i][1], -a, -b, -c, -d)) + " " +
				getDoorLayoutPosition(doorMatrix[i]));
		transDoorLayout[getDoorLayoutPosition(
			transformPointByMatrix(doorMatrix[i][0], doorMatrix[i][1], -a, -b, -c, -d))] = doorLayout[i];*/
		//set the location 
		transDoorMatrix[i] = transformPointByMatrix(doorMatrix[i][0], doorMatrix[i][1], -a, -b, -c, -d);
	}
	
	transDoorMatrix = moveIntoFirstQuadrant(transDoorMatrix);
	
	//loop through the doors again
	for(var i = 0; i < 4; i++){
		//console.log(i + " " + getDoorLayoutPosition(transDoorMatrix[i]));
		transDoorLayout[getDoorLayoutPosition(transDoorMatrix[i])] = doorLayout[i];
	}
	
	//console.log("mat " + transDoorMatrix + " " + doorMatrix);

	//console.log(doorLayout + " " + transDoorLayout);
	
	return transDoorLayout;
}

/*Detects which quadrant the unit square is in and move it to the first quadrant (makes them positive) */
function moveIntoFirstQuadrant(matrix){
	//var initialQuadrant;
	//console.log(matrix);
	//the point (1, 1) always ends up furthest from the axes, so can be used to detect which quadrant it'd in
	if(matrix[0][0] >= 0 && matrix[0][1] >= 0){
		//initialQuadrant = 1;
		//console.log("first");
	} else if (matrix[0][0] < 0 && matrix[0][1] >= 0){
		//initialQuadrant = 2;
		matrix = translateIntoFirstQuadrant(matrix, true, false);
		//console.log("second");
	} else if(matrix[0][0] < 0 && matrix[0][1] < 0){
		//initialQuadrant = 3;
		matrix = translateIntoFirstQuadrant(matrix, true, true);
		//console.log("third");
	} else {
		//initialQuadrant = 4;
		matrix = translateIntoFirstQuadrant(matrix, false, true);
		//console.log("fourth");
	}
	//console.log(matrix);
	
	return matrix;
}

/*Moves the unit square via translation into the first quadrant
moveX and moveY are boolean values which say whether the x and y coordinates should be increased */
function translateIntoFirstQuadrant(matrix, moveX, moveY){
	for(var i = 0; i < 4; i++){
		if(moveX){
			matrix[i][0]++;
		}
		if(moveY){
			matrix[i][1]++;
		}
	}
	return matrix;
}

/*Returns a number from 0 to 3 inclusive which is the index of doorLayout that a cordinate in doorMatrix represents */
function getDoorLayoutPosition(coords){
	var doorMatrix = getDoorMatrix();
	//loop through the possibilities and see if they're the same
	for(var i = 0; i < 4; i++){
		if(checkArrayContentsAreTheSame(coords, doorMatrix[i])){
			return i;
		}
	}
	//if not found, return an error
	return -1;
}

/*Returns a 4x2 matrix with a series of coordinates (the corners of a unit square) which represent the position of each door
	North = (1, 1)
	East = (1, 0)
	South = (0, 0)
	West = (0, 1) */
function getDoorMatrix(){
	return [[1, 1],[1, 0],[0, 0],[0, 1]];
}