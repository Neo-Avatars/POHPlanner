/*-THE ENTIRE HOUSE */

/*-TRANSLATION */

/*Adds the triggers to move the house */
function addMoveHouseTriggers(){
	$('#moveHouseList li a').overlay({
		onBeforeLoad : function(e){
			//work out which direction the house will move in, visibly show this to the user, then actually perform the move
			$trigger = this.getTrigger();
			var xDirection = $trigger.data('movementData').xDirection;
			var positiveDirection = $trigger.data('movementData').positiveDirection;
			$('#moveHouseCompassDirection').html($trigger.data('movementData').compass);
			return moveHouse(xDirection, positiveDirection);
		},
		closeOnClick : false,
		expose: {
			color: '#333',
			loadSpeed: 200,
			opacity: 0.9
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
	
	if(hasRoomThatWouldBeDeletedByMove(xChange, yChange)){
		//if you haven't chosen to stop the warning message
		if(!$house.noConfSettings.demolishOnMove.ignoreWarnings){
			//store the x and y changes encase the user wants to move the house anyway
			$savedVariables.xChange = xChange;
			$savedVariables.yChange = yChange;
			//show the confirmation dialogue
			return true;
		} //if remembered the 'yes' setting, move the house
		else if ($house.noConfSettings.demolishOnMove.value){
			moveHouseConfirmed(xChange, yChange);
		}
		return false;
	} else {
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
	
	switchToOverviewMode();
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
					tempHouse[i][transCoords[0]][transCoords[1]].doorLayout = 
						transformDoorOrientation(houseArray[i][j][k].doorLayout, a, b, c, d);
				}
			}
		}
	}
	
	switchToOverviewMode();
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

	calculateSharingCode(houseArray);
}


/*-DOESN'T WORK AS WANTED, SO LEFT HERE TO COME BACK TO LATER  - SEE transformDoorOrientation() INSTEAD */

/*Code to use this:

var doors = [false, true, true, true];
	for(var i = -1; i < 2; i++){
		for(var j = -1; j < 2; j++){
			for(var k = -1; k < 2; k++){
				for(var l = -1; l < 2; l++){
					TrialtransformDoorOrientation(doors, i, j, k, l);
				}
			}
		}
	}
*/
/*function TrialtransformDoorOrientation(doorLayout, a, b, c, d){
	var transDoorLayout = new Array(4);
	var got = true;
	if(a === 1 && b === 0 && c === 0 && d === -1){
		//reflection in x-axis
		console.log('x-axis:');
		transDoorLayout[0] = doorLayout[2];
		transDoorLayout[2] = doorLayout[0];
		transDoorLayout[1] = doorLayout[1];
		transDoorLayout[3] = doorLayout[3];
	} else if(a === -1 && b === 0 && c === 0 && d === 1){
		//reflection in y-axis
		console.log('y-axis:');
		transDoorLayout[1] = doorLayout[3];
		transDoorLayout[3] = doorLayout[1];
		transDoorLayout[0] = doorLayout[0];
		transDoorLayout[2] = doorLayout[2];
	} else if(a === 0 && b === -1 && c === -1 && d === 0){
		//reflection in y = x
		console.log('y = x:');
		transDoorLayout[0] = doorLayout[1];
		transDoorLayout[1] = doorLayout[0];
		transDoorLayout[2] = doorLayout[3];
		transDoorLayout[3] = doorLayout[2];
	} else if(a === 0 && b === 1 && c === 1 && d === 0){
		//reflection in y = -x
		console.log('y = -x:');
		transDoorLayout[0] = doorLayout[3];
		transDoorLayout[3] = doorLayout[0];
		transDoorLayout[2] = doorLayout[1];
		transDoorLayout[1] = doorLayout[2];
	} else if(a === 0 && b === 1 && c === -1 && d === 0){
		//rotation clockwise 90 degrees
		console.log('CW 90:');
		transDoorLayout[0] = doorLayout[3];
		transDoorLayout[1] = doorLayout[0];
		transDoorLayout[2] = doorLayout[1];
		transDoorLayout[3] = doorLayout[2];
	} else if(a === 0 && b === -1 && c === 1 && d === 0){
		//rotation anti-clockwise 90 degrees
		console.log('CCW 90:');
		transDoorLayout[0] = doorLayout[1];
		transDoorLayout[1] = doorLayout[2];
		transDoorLayout[2] = doorLayout[3];
		transDoorLayout[3] = doorLayout[0];
	} else if(a === -1 && b === 0 && c === 0 && d === -1){
		//rotation 180 degrees
		console.log('180:');
		transDoorLayout[0] = doorLayout[2];
		transDoorLayout[1] = doorLayout[3];
		transDoorLayout[2] = doorLayout[0];
		transDoorLayout[3] = doorLayout[1];
	} else {
		got = false;
	}
	
	if(got){
		console.log(a, b, c, d);
		console.log(-a, -b, -c, -d);
		console.log(transDoorLayout[0],transDoorLayout[1],transDoorLayout[2],transDoorLayout[3]);
	
	
		//try and work out how to make it dynamic
		var transDoorLayoutCompare = new Array(4);
		var transDoorLayoutCompare2 = new Array(4);
		var j, k;
		var doorMatrix = [[0.5, 1],[1, 0.5],[0.5, 0],[0, 0.5]];
		for(var i = 0; i < 4; i++){
			transDoorLayoutCompare[i] = transformPointByMatrix(doorMatrix[i][0], doorMatrix[i][1], -a, -b, -c, -d);
			//console.log(transDoorLayoutCompare[i]);
			//console.log(doorMatrix[i][0], doorMatrix[i][1]);
		}
		moveIntoFirstQuadrant(transDoorLayoutCompare);
		
		for(var i = 0; i < 4; i++){
			//console.log(transDoorLayoutCompare[i]);
		}
		var n;
		for(var i = 0; i < 4; i++){
			n = -1;
			do {
				n++;
				//console.log('td', transDoorLayoutCompare[i], doorMatrix[n]);
			} while(!checkArrayContentsAreTheSame(transDoorLayoutCompare[i], doorMatrix[n]) && n < 3);
			transDoorLayoutCompare2[n] = doorLayout[i];
			//console.log(n);
		}
		console.log(transDoorLayoutCompare2[0],transDoorLayoutCompare2[1],
			transDoorLayoutCompare2[2],transDoorLayoutCompare2[3]);
		
		if(checkArrayContentsAreTheSame(transDoorLayout, transDoorLayoutCompare2)){
			console.log('pass');
		} else {
			console.log('fail');
		}
	
	}
}*/

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