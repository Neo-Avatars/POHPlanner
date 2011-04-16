
var POHSharingCode = function(){
	var AOffset = 65;
	var aOffset = 97;
	var sc = this;
	
	this.defaultSharingCode = 'UDOB,UEPC';
	
	this.problemWithLoad = false;

	//example code with 14 rooms
	//CEKB,FDEE,FFKG,UCCD,UDKB,UEBC,VDBC,WEIF,YHEE,YICD,eDIF,fDIF,gEBC,iHCD
	//with 20
	//BEKB,EDEE,EFKG,TCCD,TDKB,TEBC,UCOB,UDBC,UEBC,VBIF,VCBC,VDKG,VEIF,WBOB,WDBC,XHEE,XICD,dDIF,eDIF,fEBC
	//floating room
	//UEBC,eDIF
	//with 23
	//BEKB,EDEE,EFKG,TBCD,TCCD,TDKB,TEBC,UCOB,UDBC,UEBC,VBIF,VCBC,VDKG,VEIF,WBOB,WDBC,XCIK,XHEE,XICD,dDIF,eDIF,fEBC
	//21, 5x8 and floaters
	//EDEE,EFKG,TBCD,TCCD,TDKB,TEBC,UCOB,UDBC,UEBC,VBIF,VCBC,VDKG,VEIF,WBOB,XCIK,XHEE,XICD,dDIF,eDIF,fEBC,gDIK
	//furni
	//UDOBbaa,UEBC,VDOBC,WDOBaB

	/*Calculates the Sharing Code for the house
	Requires a house array as a parameter so that it can be used to make rotating the house easier
	Returns the Sharing Code */
	this.calculateSharingCode = function(houseArrayToUse){
		var code = ''; //String.fromCharCode( AOffset + $house.sharingCodeVersion );
		//console.time('calculate sharing code');
		//console.log("cs " + houseArrayToUse[1][4][5].doorLayout + " " + houseArrayToUse[1][4][3].doorLayout);
		for(var i = 0; i < 3; i++){
			for(var j = 0; j < 9; j++){
				for(var k = 0; k < 9; k++){
					if(houseArrayToUse[i][j][k].labelText !== ' '){
						if(code.length !== 0){
							code += ",";
						}
						code += sc.calculateCharsFromRoomLocation(i, j, k);
						code += sc.calculateCharFromDoorLayout(i, j, k, houseArrayToUse);
						code += houseArrayToUse[i][j][k].typeID;
						code += sc.calculateCharsFromBuiltFurniture(i, j, k, houseArrayToUse);
					}
				}
			}
		}

		//set the value of the Sharing Code box to the code
		$('#sharingCodeInput').val(code);
		//console.log('load code', code);
		//and set a cookie so it'll be remembered for the next visit
		$.cookie('pohPlannerSharingCode', code);
		//console.log($.cookie('pohPlannerSharingCode'));
		//console.timeEnd('calculate sharing code');
		//console.log($('#POHPlanner').html().length);
		return code;
	};

	/*Buids a house based on the input Sharing Code */
	this.loadHouseFromSharingCode = function(code){
		//console.time("load house");
		$house.loading = true;
		poh.hideVisibleFrames();
		//reset the house
		if($house.initialised){
			sc.resetHouse();
		}
		
		//if there's nothing to load, there's no point continuing
		if(code !== ''){
			//split the code into the different rooms
			var splitCode = code.split(",");
			var n, roomType, doorLayout;
			
			//var codeVersion = splitCode[0];
			
			//console.log(splitCode);
			for(var i = 0; i < splitCode.length; i++){
			if(splitCode[i].length >= 4){ //4 is the minimum number of characters required for a room with no furniture
				//console.log(i + " " + splitCode[i]);
				//console.time('indy room');
				//work out the location of the room
				var ijk = sc.calculateRoomLocationFromChars(splitCode[i].substr(0, 2));
				
				if( sc.isValidRoomLocation( ijk ) ){
				//work out the type of room
				n = 0;
				roomType = splitCode[i].substr(3, 1);
				//console.log(i, splitCode[i], roomType/*, roomsArray[2][0]*/);
				while( (n < roomsArray.length) && (roomsArray[n][0] !== roomType) ){
					//console.log(n + " " + roomsArray[n][0] + " j " + roomType);
					n++;
				}
				if( (n < roomsArray.length) && (roomsArray[n][0] === roomType) ){
					poh.buildRoom(ijk[0], ijk[1], ijk[2], n);
					
					//set the door layout
					doorLayout = sc.calculateDoorLayoutFromChar(splitCode[i].substr(2, 1));
					poh.houseArray[ijk[0]][ijk[1]][ijk[2]].doorLayout = doorLayout;
					poh.changeRoomOverviewImage(ijk[0], ijk[1], ijk[2]);
					
					//build any specified furniture in the room
					if(splitCode[i].length > 4){
						sc.buildFurniFromSharingCode( splitCode[i], ijk[0], ijk[1], ijk[2] );
					}
				} else {
					sc.problemWithLoad = true;
				}
				//console.timeEnd('indy room');
				} else {
					sc.problemWithLoad = true;
				}
			} else {
				sc.problemWithLoad = true;
			}
			}
			sc.setSharingCodeInputBoxValue(code);
		}
		//call only at the end, not after each room is built
		poh.rp.updateNumberOfRooms();
		poh.rp.updateRoomsCost();
		poh.rp.updateTotalCost();
		
		if( sc.problemWithLoad ){
			//console.log('problem with load');
			sc.calculateSharingCode(poh.houseArray);
		} else {
			//console.log('loaded fine');
		}
		
		sc.problemWithLoad = false;
		$house.loading = false;
		//console.timeEnd("load house");
	};

	/*Loads the default house */
	this.loadDefaultHouse = function(){
		poh.pm.switchToOverviewMode(); //needs to be done before loading the new house otherwise there are problems with the selected room and it will cause errors
		var code = sc.defaultSharingCode;
		$.cookie('pohPlannerSharingCode', code);
		sc.loadHouseFromSharingCode(code);
		//make it so you're viewing the ground floor
		poh.rp.hideSelectedOverviewFloor();
		$house.visibleOverviewFloor = 1;
		$('#floorSelectDropDown').val('groundFloor');
		poh.rp.showSelectedOverviewFloor();
	};

	/*Sets the value of the Sharing Code input box */
	this.setSharingCodeInputBoxValue = function(code){
		$('#sharingCodeInput').val(code);
	};

	/*Resets the house by demolishing any built rooms */
	this.resetHouse = function(){
		//console.time("reset house");
		for(var i = 0; i < 3; i++){
			for(var j = 0; j < 9; j++){
				for(var k = 0; k < 9; k++){
					//if there's a room built there
					if(poh.houseArray[i][j][k].labelText !== ' '){
						poh.demolishRoom3(i, j, k); //demolish it
					}
				}
			}
		}
		if( !$house.transforming ){
			$house.totalFurniCost = 0;
			$house.totalFurniXp = 0;
			poh.rp.updateFurnitureCost();
			poh.rp.updateFurnitureXp();
		}
		//console.timeEnd("reset house");
	};
	
	/*Builds any furniture in the specified room that the Sharing Code says exists */
	this.buildFurniFromSharingCode = function( code, i, j, k ){
		var furniLength, maxFurni, processedFurni, blankFurni, furniChar, furniCharCode, hotspot, furniID, hsLength;
		furniLength = code.length - 4;
		maxFurni = poh.houseArray[i][j][k].furniture.length;
		processedFurni = hotspot = blankFurni = 0;
		//loop through the characters
		while( (hotspot < furniLength) && (hotspot < maxFurni) ){
			furniChar = code.substr(4 + processedFurni, 1);
			furniCharCode = furniChar.charCodeAt(0);
		
			if( furniCharCode < aOffset ){
				if( furniCharCode > AOffset ){
					//process empties (leave them as: typeof furniture === 'undefined')
					hotspot += furniCharCode - AOffset;
					blankFurni += furniCharCode - AOffset;
				}
			} else if( furniCharCode < ( aOffset + 30 ) ) {
				furniID = furniCharCode - aOffset;
				hsLength = $pohHotspots['$' + hotspot].content.length;
				if( furniID < hsLength ){
					poh.houseArray[i][j][k].furniture[hotspot] = furniID;
				} else {
					sc.problemWithLoad = true;
				}
				hotspot++;
			}
			processedFurni++;
		}
		//console.log(furniLength, maxFurni, processedFurni, hotspot, blankFurni);
		if( (furniLength > maxFurni) || (hotspot >= maxFurni) || ( maxFurni < (furniLength + blankFurni) ) ){
			sc.problemWithLoad = true;
		}
	};
	
	/*Works out how many empty furniture slots there are in a row after and including the start position passed as a parameter
	Returns the number of empty slots */
	this.calculateConsecutiveEmptyFurniSlots = function( furniture, start ){
		var position = start;
		var consecutive = 0;
		while( (position < furniture.length) && ((typeof furniture[position] === 'undefined') 
														|| (furniture[position] === -1)) ){
			consecutive++;
			position++;
		}
		return consecutive;
	};
	
	/*Calculates the characters to represent the furniture built in a room
	Returns the characters */
	this.calculateCharsFromBuiltFurniture = function(i, j, k, houseArrayToUse){
		var chars = '';
		var consecutiveEmpties;
		for(var l = 0; l < houseArrayToUse[i][j][k].furniture.length; l++){
			if( (typeof houseArrayToUse[i][j][k].furniture[l] === 'undefined') 
					|| (houseArrayToUse[i][j][k].furniture[l] === -1)){
				consecutiveEmpties = sc.calculateConsecutiveEmptyFurniSlots( houseArrayToUse[i][j][k].furniture, l );
				//console.log(consecutiveEmpties, '- empty');
				l += consecutiveEmpties - 1;
				chars += String.fromCharCode( AOffset + consecutiveEmpties - 1); //no furniture built here
			} else {
				chars += String.fromCharCode( aOffset + houseArrayToUse[i][j][k].furniture[l] );
			}
		}
		//don't show anything if there's no furniture
		if( consecutiveEmpties === houseArrayToUse[i][j][k].furniture.length ){
			chars = '';
		}
		return chars;
	};

	/*Calculates two characters to represent the location (i, j, k) of the room
	Returns the characters */
	this.calculateCharsFromRoomLocation = function(i, j, k){
		//combine the values of the i and j positions so they become one
		var ij = (16 * i) + j + AOffset;
		//work out the characters that have these values and return them
		return String.fromCharCode(ij) + String.fromCharCode(k + AOffset);
	};

	/*Calculates the room location (i, j, k) that the two characters represent
	Returns an array of [i, j, k] */
	this.calculateRoomLocationFromChars = function(chars){
		var ij = chars.charCodeAt(0) - AOffset;
		var i, j, k;
		
		j = ij % 16;
		i = (ij - j) / 16;	
		k = chars.charCodeAt(1) - AOffset;
		
		//var location = [i, j, k];
		return [i, j, k];
	};
	
	/*Determines whether a room location is valid or not
	ijk is the array returned by sc.calculateRoomLocationFromChars
	Returns true if it is, false otherwise */
	this.isValidRoomLocation = function( ijk ){
		if( (ijk[2] > 8) ||
				(ijk[1] > 8) ||
				(ijk[0] > 8) ||
				(ijk[0] < 0) ||
				(ijk[1] < 0) ||
				(ijk[2] < 0) ){
			return false;
		} else {
			return true;
		}
	};
	
	/*Determines whether a door layout is valid or not
	Returns the passed array if it is valid, otherwise the default layout for the room type built in houseArray[i][j][k] */
	this.validateRoomLocation = function( i, j, k, doorLayout ){
		
	};

	/*Calculates a single character to represent the door layout of the room
	Returns the character */
	this.calculateCharFromDoorLayout = function(i, j, k, houseArrayToUse){
		var number = sc.calculateNumberFromDoorLayout(i, j, k, houseArrayToUse);
		number += AOffset;

		return String.fromCharCode(number);
	};

	/*Calculates a number to represent the door layout of the room so you need less characters in the Sharing Code
	Returns a number between 0 and 15 inclusive which represents the door layout */
	this.calculateNumberFromDoorLayout = function(i, j, k, houseArrayToUse){
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
	};

	/*Works out the door layout from a character which represents it
	Returns the door layout */
	this.calculateDoorLayoutFromChar = function(chara){
		var value = chara.charCodeAt(0);
		value -= AOffset;
		
		return sc.calculateDoorLayoutFromNumber(value);
	};

	/*Works out the door layout from a number between 0 and 15 which represents it
	Returns the door layout */
	this.calculateDoorLayoutFromNumber = function(number){
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
	};
};

