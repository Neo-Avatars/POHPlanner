//the 'reveal' function of the facebox has been modified to allow the tablesorter to work when in the facebox
//the aToolTip plugin has been changed to make it work as wanted
//removed ':visible' from column 12460 in tablesorter.min.js so that the zebra will work on all tables from the start

var $house = {
	builtRooms : 0, //the number of rooms that have been built
	maxRooms : 20, //the maximum number of rooms that can be built based on the specified Construction level
	totalRoomCost : 0, //the total cost of all the rooms
	totalFurniCost : 0, //the total cost of all the furniture
	totalFurniXp : 0, //the total experience that building all the specified furniture will provide
	visibleOverviewFloor : 1, //the floor that is being viewed in Overview Mode
	csr : [-1, -1, -1], //the coordinates (i, j, k) of the Currently Selected Room
	initialised : false, //whether the house has fully initialised
	loading : false, //loading a layout from a Sharing Code
	hasCostumeRoom : false, //the house has a costume room built (can only have one)
	hasMenagerie : false, //the house has a menagerie built (can only have one)
	noConfirmation : { //whether you should ignore...
		demolishOnMove : false, //the warning about rooms being demolished when moving a house too far in any direction
		levelLimit : false, //level limits when building a room or furniture
		maxRoomLimit : false //the maxRooms limit that the house has
	},
	noConfSettings : { //settings to remember the choice when the 'Remember this choice' box is ticked
		demolishOnMove : {
			value : false, //false = 'no', true = 'yes'
			description : 'Demolish any rooms in the way when moving the house'
		}
	}
};

var $stats = { //the user's stats
	attack : 1,
	defence : 1,
	strength : 1,
	constitution : 10,
	ranged : 1,
	prayer : 1,
	magic : 1,
	cooking : 1,
	woodcutting : 1,
	fletching : 1,
	fishing : 1,
	firemaking : 1,
	crafting : 1,
	smithing : 1,
	mining : 1,
	herblore : 1,
	agility : 1,
	thieving : 1,
	slayer : 1,
	farming : 1,
	runecrafting : 1,
	hunter : 1,
	construction : 1,
	summoning : 1
};

/*Sets the specified stat to the specified level */
function setStat(skill, level){
	if(level < 1){//can't have negative levels
		level = 1;
	} else if (level > 99){//or ones above 99
		level = 99;
	}
	//calculate the maximum number of rooms if you're setting a new Construction level
	if(skill === "construction"){
		setMaxRooms(level);
	}
	$stats[skill] = level;
}

/*Sets the maximum number of rooms that you can have built based on your Construction level and updates the relevant visible information */
function setMaxRooms(level){
	$house.maxRooms = calculateMaxRooms(level);
	updateMaxNumberOfRooms();
}

/*Returns the maximum number of rooms you can build with the specified Construction level */
function calculateMaxRooms(level){
	var max = level < 38 ? 20 : level >= 99 ? 32 : level > 95 ? 31 : Math.floor(20 + ((level - 32) / 6));
	return max;
}

var houseArray; //the array that makes up the house - created in initHouseArray()
//var roomsArray; //an array of all the different types of room, set in POH-rooms.js
var $savedVariables = { //somewhere to store temporary variables when the code goes off to do something else
	//tooltipTitle : '', //a variable to make the aToolTips work as wanted
	//tooltipID : '', //another variable to make the aToolTips work as wanted
};

/*-DATA CLASSES */
/*Room*/
function room(in_type, in_labelText, in_level, in_cost, in_doorLayout, in_imgURL, in_buildIcon){
	this.type = in_type;
	this.labelText = in_labelText;
	this.level = in_level;
	this.cost = in_cost;
	this.doorLayout = in_doorLayout;
	this.hasFourDoors = (checkArrayContentsAllFalse(this.doorLayout)) ? true : false;
	this.doorImgURL = '';
	this.imgURL = in_imgURL;
	this.buildIcon = in_buildIcon;
}

/*Returns true if all elements of an array are 'false', otherwise returns false */
function checkArrayContentsAllFalse(array){
	for(var i = 0; i < array.length; i++){
		if(array[i] === true){
			return false;
		}
	}
	return true;
}

/*Returns true if the array contents are the same, otherwise false */
function checkArrayContentsAreTheSame(arrayOne, arrayTwo){
	if(arrayOne.length !== arrayTwo.length){
		return false;
	} else {
		for(var i = 0; i < arrayOne.length; i++){
			if(arrayOne[i] !== arrayTwo[i]){
				return false;
			}
		}
	}
	return true;
}

/*Returns true if there's a string input of 'true' otherwise returns false */
function convertStringToBoolean(input){
	return input === 'true' ? true : false;
}

/*Returns the string with a capitalised first letter */
function capitaliseString(str){
	return str.substr(0,1).toUpperCase() + str.substr(1,str.length);
}

/*Sets the ID (location) of the currently selected room in Overview mode*/
function setSelectedRoom(i, j, k){
	//remove the visible selection from the previous room
	var roomID = "#room" + $house.csr[0] + $house.csr[1] + $house.csr[2];
	$(roomID).removeClass('selectedRoom');
	
	//select the new room
	$house.csr[0] = i;
	$house.csr[1] = j;
	$house.csr[2] = k;

	//if unselecting all rooms (demolishing or switching floor)
	if(i == -1 || j == -1 || k == -1){
		//hide the room info panel
		$('#infoPanelRoomStats').hide();
	} else {
		//visibly select the new room
		roomID = calculateRoomID(i, j, k);
		$(roomID).addClass('selectedRoom');
		//console.log('selected: ' + $house.csr[0] + " " + $house.csr[1] + " " + $house.csr[2]); 
		
		//if there's a room built there
		if(houseArray[$house.csr[0]][$house.csr[1]][$house.csr[2]].labelText !== ' '){
			//update the stats about the selected room
			updateRoomStatsContent();
			//and show it
			$('#infoPanelRoomStats').show();
			//as well as hiding any frames that are shown
			hideVisibleFrames();
		} else { //otherwise hide the panel
			$('#infoPanelRoomStats').hide();
		}
	}
}

/*Hides any visible frames such as the Room Building or Stats frames */
function hideVisibleFrames(){
	if(!$house.loading){
		var $frames = $('.facebox');
		$frames.each(function(){
			if($(this).is(':visible')){
				$(this).fadeOut('fast');
			}
		});
	}
	//if($('#roomBuildingFrame').is(':visible')){
	//	$('#roomBuildingFrame').fadeOut('fast');
	//}
}

/*Deselects the current room by setting it to the default [-1, -1, -1] */
function deselectAllRooms(){
	setSelectedRoom(-1, -1, -1);
}

/*Returns the #roomID of the room located in position houseArray[i][j][k] */
function calculateRoomID(i, j, k){
	return "#room" + i + j + k;
}

/*Determines the class for the door layout background image for a room in Overview Mode by looking at the door locations and room type
Returns a string containing the class name for that background */
function determineRoomOverviewImage(i, j, k){
	var imageURL = 'ROI'; //a default (Room Overview Image) so that it's hopefully easier to make sense of the stylesheet
	
	//indicate a dungeon or upstairs room
	if(i === 0 && houseArray[i][j][k].labelText === ' '
		|| houseArray[i][j][k].labelText.indexOf("Dungeon") != -1
			|| houseArray[i][j][k].labelText === 'Treasure Room'){
		imageURL += "d";
	} else if(i == 2 && houseArray[1][j][k].labelText !== ' '
				&& houseArray[i][j][k].labelText === ' '){
		imageURL += "u";
	}
	
	//compass directions of the doors
	if(houseArray[i][j][k].doorLayout[0] == true){
		imageURL += "n";
	}
	if(houseArray[i][j][k].doorLayout[1] == true){
		imageURL += "e";
	}
	if(houseArray[i][j][k].doorLayout[2] == true){
		imageURL += "s";
	}
	if(houseArray[i][j][k].doorLayout[3] == true){
		imageURL += "w";
	}

	//special types
	if(houseArray[i][j][k].labelText === ' '){
		imageURL += "empty";
	} else if(houseArray[i][j][k].labelText.indexOf('Garden') != -1){
		imageURL += "garden";
	} else if(houseArray[i][j][k].labelText.indexOf('Dungeon') != -1){
		if(houseArray[i][j][k].labelText === 'Dungeon Junction'){
			imageURL += "j";
		} else if(houseArray[i][j][k].labelText === 'Dungeon Stairs'){
			imageURL += "s";
		}
	} else if(houseArray[i][j][k].labelText === 'Treasure Room'){
		imageURL += "t";
	}
	//it should have at least one door if you're going to be able to enter, so something has probably gone wrong
	if(imageURL === 'ROI'){
		imageURL += "probableError";
	}

	return imageURL;
}

/*Returns the #ID of the floor based on the number (0, 1, 2) */
function determineIDOfFloor(i){
	if(i === 0){
		return "dungeon";
	} else if(i === 1){
		return "groundFloor";
	} else {
		return "upstairs";
	}
}

/*Returns the text that is used as the tooltip for rooms in Overview mode*/
function determineRoomOverviewTooltipText(i, j, k){
	if (i === 2 && houseArray[i][j][k].doorImageURL === 'ROIempty'){
		return "";
	} else if (houseArray[i][j][k].labelText === ' '){
		return "Click to build a room";
	} else {
		return houseArray[i][j][k].labelText + " - Click to enter furniture build mode";
	}
}

/*Removes the class of the previous room type when building a new one */
function removeExistingRoomOverviewImage(i, j, k){
	//console.log(i + " " + houseArray[i][j][k].doorImageURL);
	$(calculateRoomID(i, j, k)).removeClass(houseArray[i][j][k].doorImageURL);
}

/*Sets the background image for a room in Overview Mode */
function setRoomOverviewImage(i, j, k){
	houseArray[i][j][k].doorImageURL = determineRoomOverviewImage(i, j, k);
	$(calculateRoomID(i, j, k)).addClass(houseArray[i][j][k].doorImageURL);
}

/*Updates the Room Overview Image when rotating a room so that only one thing needs calling */
function changeRoomOverviewImage(i, j, k){
	//console.log(i + " s " + houseArray[i][j][k].doorImageURL);
	removeExistingRoomOverviewImage(i, j, k);
	setRoomOverviewImage(i, j, k);
}

/*Builds a room*/
function buildRoom(i, j, k, typeID){
	/*if($house.initialised && !$house.loading){
		hideVisibleFrames();
	}*/
	//console.time("build room");
	var roomID = calculateRoomID(i, j, k);
	//removes existing type
	removeExistingRoomOverviewImage(i, j, k);
	
	//build the room
	houseArray[i][j][k] = new room(roomsArray[typeID][0],roomsArray[typeID][1],roomsArray[typeID][2],
		roomsArray[typeID][3],roomsArray[typeID][4],roomsArray[typeID][5],roomsArray[typeID][6]);

	//visibly show the change by switching the background image
	setRoomOverviewImage(i, j, k);
	//and changing the tooltip
	$(roomID).attr("title", determineRoomOverviewTooltipText(i, j, k));
	
	//and updating the room above
	updateRoomAbove(i, j, k);
	//console.log('hc',i,j,k,$(roomID  + ' a').hasClass('faceboxLink'));
	//then switching the text
	$(roomID  + ' a').html(houseArray[i][j][k].labelText)
	
	//console.log('hc',i,j,k,$(roomID  + ' a').hasClass('faceboxLink'));
	//if building a blank 'room'
	if(houseArray[i][j][k].labelText === ' '){
		
	} else {//update the House Stats
		updateHouseStatsContent(true, i, j, k);
		//and remove the facebox link, replacing it with a link to Picture Mode
		$(roomID  + ' a').removeClass("faceboxLink").attr("href", roomID + "PictureMode").unbind('click');
	}
	
	if($house.initialised && !$house.loading){
		//select the room
		setSelectedRoom(i, j, k);
		calculateSharingCode(houseArray);
	}
	//console.timeEnd("build room");
}

/*Multiple functions are based on having different numbers of parameters so that it can be used in different situations
The digit on the end denotes the number of parameters and if it isn't there, problems with infinite recursion occur */
/*Demolishes a room, the parameters being the location of the room to demolish */
function demolishRoom3(i, j, k){
	var roomID = calculateRoomID(i, j, k);
	updateHouseStatsContent(false, i, j, k);

	//build an empty 'room' in it's place
	buildRoom(i, j, k, 0);
	deselectAllRooms();

	//$(roomID + ' a').addClass("faceboxLink").attr("href", "#roomBuildingFrame");
	//$(roomID + ' .faceboxLink').overlay();
	//make it posible to build a new room in it's place - need to fully replace the innerHTML otherwise the overlay won't work
	updateBlankRoomHTML(roomID, i, j, k);
	
	//update the room above
	updateRoomAbove(i, j, k);
}

/*Demolishes the selected room */
function demolishRoom0(){
	demolishRoom3($house.csr[0], $house.csr[1], $house.csr[2]);
}

/*Updates the image of the room above and whether you're able to build anything there, so you can't build a room in mid air Upstairs */
function updateRoomAbove(i, j, k){
	//if you're building a room on the Ground Floor
	if(i === 1){
		//update the image of the room above
		changeRoomOverviewImage(i + 1, j, k);

		//work out the ID of the room above
		var aboveID = calculateRoomID(i + 1, j, k);
		
		//if you're building something, make the room above clickable
		if(houseArray[i][j][k].labelText !== ' '){
			//create a link and such
			updateBlankRoomHTML(aboveID, i + 1, j, k)
			/*$(aboveID).html('<span><a href="#roomBuildingFrame"\
				onclick="setSelectedRoom('+(i+1)+','+j+','+k+');return false;"\
					class="overviewLink faceboxLink" rel="#roomBuildingFrame">' +
						houseArray[i + 1][j][k].labelText + '</a></span>')
				//set the title and give it a tooltip
				.attr("title", determineRoomOverviewTooltipText(i + 1, j, k)).aToolTip();
			//make it so you can build a room here
			$(aboveID + ' .faceboxLink').overlay();*/
		} else {
			//remove the ability to do anything useful
			$(aboveID).html(" ").unbind();
		}
	}
}

/*Replaces the HTML in a room div in Overview mode, then makes it so clicking on it will bring up the room building frame
It almost certainly isn't the best solution, but if the whole innerHTML isn't replaced, there are problems with dead clicks after demolishing rooms */
function updateBlankRoomHTML(roomID, i, j, k){
	$(roomID).html('<span><a href="#roomBuildingFrame"\
		onclick="setSelectedRoom('+i+','+j+','+k+');return false;"\
			class="overviewLink faceboxLink" rel="#roomBuildingFrame">' +
				houseArray[i][j][k].labelText + '</a></span>');
	//add the overview link
	$(roomID + ' .faceboxLink').overlay();
}

/*Adds commas to numbers >= 1000 to make them easier to read
Doesn't work for negative numbers, although shouldn't need to, so it doesn't matter
About 15% faster (using console.time() with Firebug) than anything Google will find */
function addCommasToLargeNumbers(number){
	if(number >= 1000){
		var noStr = number + '';
		var decimalPointPosition = noStr.indexOf('.');
		var decimal = '';
		if(decimalPointPosition != -1){
			decimal = noStr.substr(decimalPointPosition, noStr.length - decimalPointPosition);
			noStr = noStr.substr(0, decimalPointPosition);
		}
		var numberOfCommas = Math.floor((noStr.length - 1) / 3);
		var output = noStr.substr(0, (noStr.length - (numberOfCommas * 3)));
		var lengthBeforeCommas = output.length;
		for(var i = 0; i < numberOfCommas; i++){
			output += ',' + noStr.substr(lengthBeforeCommas, 3);
			lengthBeforeCommas += 3;
		}
		return output + decimal;
	} else {
		return number;
	}
}

$( document ).ready( function() {
	//$('.map').maphilight();
	//$('area').aToolTip({inSpeed: 400, outSpeed: 100});

	console.time("document ready");

	//generate the core divs that everything will go in
	initCoreDivs();
	
	//add things to the top bar
	initTopBar();
	
	//make all sortable tables have the hawt zebra effect
	$.tablesorter.defaults.widgets = ['zebra'];
	
	//creating the table so you can make more rooms
	initRoomBuildingTable();
	$('#roomBuildingTable').tablesorter({
		//widgets : ['zebra'],
		sortList : [[2,0],[3,0],[1,0]]
	});
	
	//initialise the content for the Info Panel (the one on the right)
	initInfoPanel();

	//creating the initial house
	initHouse();
	
	//give tooltips to the bottom two floors, but not the top since you can't build there by default and they can be given tooltips as needed
	$('#' + determineIDOfFloor(0) + ' div').aToolTip();
	$('#' + determineIDOfFloor(1) + ' div').aToolTip();

	//adds the various events to buttons in the modal dialogues
	//initDialogueButtonEvents();
	initDialogues();
	
	$('#userStatsTable .userStatsInput').blur(function(){
		setStat($(this).attr("name"), parseInt($(this).val()));
	});
	
	$('#extra .faceboxInner').after(createFaceboxCloseButton());
	
	//make clicking on rooms in Overview mode open up the table they link to in a facebox
	$('#houseOverview .faceboxLink').overlay();
	
	//make clicking on the 'Stats' button open up the stats panel
	$('#openCharStatsButton').overlay();
	
	//the default rooms need to be added after everything has been facebox'd otherwise the 'zebra' will not work in
	//the Room Building Frame when first choosing to build a room upstairs above them
	buildDefaultRooms();
	
	$('#userStatsTable').tablesorter({
		sortList : [[1,0]]
	});
	
	//initMoveHouseTriggers();
	//console.log(addHouseMoveTriggers());
	
	//console.log('Planner width: ' + $('#POHPlanner').width());
	//console.log('width: ' + ($('#houseOverview').width() + $('#infoPanel').width()));
	//console.log('height: ' + $('#houseOverview').height());

	$house.initialised = true;
	
	console.timeEnd("document ready");

	//var t = document.getElementById('POHPlanner').innerHTML;
	//console.log(t.length);
});