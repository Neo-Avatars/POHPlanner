var AOffset = 65;

//example code with 14 rooms
//CEKB,FDEE,FFKG,UCCD,UDKB,UEBC,VDBC,WEIF,YHEE,YICD,eDIF,fDIF,gEBC,iHCD
//with 20
//BEKB,EDEE,EFKG,TCCD,TDKB,TEBC,UCOB,UDBC,UEBC,VBIF,VCBC,VDKG,VEIF,WBOB,WDBC,XHEE,XICD,dDIF,eDIF,fEBC

/*Calculates the Sharing Code for the house
Requires a house array as a parameter so that it can be used to make rotating the house easier
Returns the Sharing Code */
function calculateSharingCode(houseArrayToUse){
	var code = '';
	
	//console.log("cs " + houseArrayToUse[1][4][5].doorLayout + " " + houseArrayToUse[1][4][3].doorLayout);
	for(var i = 0; i < 3; i++){
		for(var j = 0; j < 9; j++){
			for(var k = 0; k < 9; k++){
				if(houseArrayToUse[i][j][k].labelText !== ' '){
					if(code.length !== 0){
						code += ",";
					}
					code += calculateCharsFromRoomLocation(i, j, k);
					code += calculateCharFromDoorLayout(i, j, k, houseArrayToUse);
					code += houseArrayToUse[i][j][k].type;
				}
			}
		}
	}
	
	//console.log("in " + code);
	//set the value of the Sharing Code box to the code
	$('#sharingCodeInput').val(code);

	//and set a cookie so it'll be remembered for the next visit
	$.cookie('pohPlannerSharingCode', code);
	
	return code;
}

/*Buids a house based on the input Sharing Code */
function loadHouseFromSharingCode(code){
	//console.time("load house");
	$house.loading = true;
	hideVisibleFrames();
	//reset the house
	if($house.initialised){
		resetHouse();
	}
	
	//if there's nothing to load, there's no point continuing
	if(code !== ''){
		//split the code into the different rooms
		var splitCode = code.split(",");
		var n, roomType;
		
		//console.log(splitCode);
		for(var i = 0; i < splitCode.length; i++){
			//console.log(i + " " + splitCode[i]);

			//work out the location of the room
			var ijk = calculateRoomLocationFromChars(splitCode[i].substr(0, 2));
			
			//work out the type of room
			n = 0;
			roomType = splitCode[i].substr(3, 1);
			//console.log(i + " " + splitCode[i] + " " + roomType);
			while(roomsArray[n][0] !== roomType){
				//console.log(n + " " + roomsArray[n][0] + " j " + roomType);
				n++;
			}
			buildRoom(ijk[0], ijk[1], ijk[2], n);
			
			//set the door layout
			houseArray[ijk[0]][ijk[1]][ijk[2]].doorLayout = calculateDoorLayoutFromChar(splitCode[i].substr(2, 1));
			changeRoomOverviewImage(ijk[0], ijk[1], ijk[2]);
		}
		setSharingCodeInputBoxValue(code);
	}
	//call only at the end, not after each room is built
	updateNumberOfRooms();
	updateRoomsCost();
	updateTotalCost();
	
	$house.loading = false;
	//console.timeEnd("load house");
}

/*Loads the default house */
function loadDefaultHouse(){
	var code = "UDOB,UEBC"; //default
	$.cookie('pohPlannerSharingCode', code);
	loadHouseFromSharingCode(code);
	//make it so you're viewing the ground floor
	hideSelectedOverviewFloor();
	$house.visibleOverviewFloor = 1;
	$('#floorSelectDropDown').val('groundFloor');
	showSelectedOverviewFloor();
}

/*Sets the value of the Sharing Code input box */
function setSharingCodeInputBoxValue(code){
	$('#sharingCodeInput').val(code);
}

/*Resets the house by demolishing any built rooms */
function resetHouse(){
	//console.time("reset house");
	for(var i = 0; i < 3; i++){
		for(var j = 0; j < 9; j++){
			for(var k = 0; k < 9; k++){
				//if there's a room built there
				if(houseArray[i][j][k].labelText !== ' '){
					demolishRoom3(i, j, k); //demolish it
				}
			}
		}
	}
	//console.timeEnd("reset house");
}

/*Calculates two characters to represent the location (i, j, k) of the room
Returns the characters */
function calculateCharsFromRoomLocation(i, j, k){
	//combine the values of the i and j positions so they become one
	var ij = (16 * i) + j + AOffset;
	//work out the characters that have these values and return them
	return String.fromCharCode(ij) + String.fromCharCode(k + AOffset);
}

/*Calculates the room location (i, j, k) that the two characters represent
Returns an array of [i, j, k] */
function calculateRoomLocationFromChars(chars){
	var ij = chars.charCodeAt(0) - AOffset;
	var i, j, k;
	
	j = ij % 16;
	i = (ij - j) / 16;	
	k = chars.charCodeAt(1) - AOffset;
	
	//var location = [i, j, k];
	return [i, j, k];
}

/*Calculates a single character to represent the door layout of the room
Returns the character */
function calculateCharFromDoorLayout(i, j, k, houseArrayToUse){
	var number = calculateNumberFromDoorLayout(i, j, k, houseArrayToUse);
	number += AOffset;

	return String.fromCharCode(number);
}

/*Calculates a number to represent the door layout of the room so you need less characters in the Sharing Code
Returns a number between 0 and 15 inclusive which represents the door layout */
function calculateNumberFromDoorLayout(i, j, k, houseArrayToUse){
	var doorLayout = houseArrayToUse[i][j][k].doorLayout;
	var number = 0;
	
	if(doorLayout[0] === true){
		number++;
	}
	if(doorLayout[1] === true){
		number += 2;
	}
	if(doorLayout[2] === true){
		number += 4;
	}
	if(doorLayout[3] === true){
		number += 8;
	}
	
	return number;
}

/*Works out the door layout from a character which represents it
Returns the door layout */
function calculateDoorLayoutFromChar(chara){
	var value = chara.charCodeAt(0);
	value -= AOffset;
	
	return calculateDoorLayoutFromNumber(value);
}

/*Works out the door layout from a number between 0 and 15 which represents it
Returns the door layout */
function calculateDoorLayoutFromNumber(number){
	var doorLayout = [false, false, false, false];
	
	if(number > 7){
		doorLayout[3] = true;
		number -= 8;
	}
	if(number > 3){
		doorLayout[2] = true;
		number -= 4;
	}
	if(number > 1){
		doorLayout[1] = true;
		number -= 2;
	}
	if(number > 0){
		doorLayout[0] = true;
	}
	
	return doorLayout;
}