//the 'reveal' function of the facebox has been modified to allow the tablesorter to work when in the facebox
//the aToolTip plugin has been changed to make it work as wanted
//removed ':visible' from column 12460 in tablesorter.min.js so that the zebra will work on all tables from the start
var $house = {
	debugMode : true, //whether firebug-less debug statements should be outputted
	builtRooms : 0, //the number of rooms that have been built
	maxRooms : 20, //the maximum number of rooms that can be built based on the specified Construction level
	totalRoomCost : 0, //the total cost of all the rooms
	totalFurniCost : 0, //the total cost of all the furniture
	totalFurniXp : 0, //the total experience that building all the specified furniture will provide
	visibleOverviewFloor : 1, //the floor that is being viewed in Overview Mode
	overviewMode : true, //whether you're viewing the OVerview Mode (true) or the Picture Mode (false)
	csr : [-1, -1, -1], //the coordinates (i, j, k) of the Currently Selected Room
	initialised : false, //whether the house has fully initialised
	loading : false, //loading a layout from a Sharing Code
	hasCostumeRoom : false, //the house has a costume room built (can only have one)
	hasMenagerie : false, //the house has a menagerie built (can only have one)
	noConfSettings : { //settings to remember the choices for various options
		demolishOnMove : { //the warning about rooms being demolished when moving a house too far in any direction
			value : false, //false = 'no', true = 'yes'
			description : 'Demolish any rooms in the way when moving the house', //describes what it does
			ignoreWarnings : false //whether you should ignore warnings and error messages about this
		},
		levelLimit : { //level limits when building a room or furniture
			value : false,
			description : 'Build rooms and furniture even if you do not have the levels to do so',
			ignoreWarnings : false
		},
		maxRoomLimit : { //the maxRooms limit that the house has
			value : false,
			description : 'Ignore the limit on the maximum number of rooms that can be built',
			ignoreWarnings : false
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
	summoning : 1,
	dungeoneering : 1
};

/*Sets the specified stat to the specified level */
function setStat(skill, level){
	if(level < 1){ //can't have negative levels
		level = 1;
	} else if (level > 99){ //or ones above 99
		level = 99;
	}

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
function room(in_typeID, in_labelText, in_level, in_cost, in_doorLayout, in_imgURL, in_buildIcon){
	this.typeID = in_typeID; //an ID relating to the type of the room
	this.labelText = in_labelText; //the type of room eg. Garden, Parlour
	this.level = in_level; //the level required to build the room
	this.cost = in_cost; //the cost of the room in gp
	this.doorLayout = in_doorLayout; //the door layout of the room, starting in the north and heading clockwise
	this.hasFourDoors = (checkArrayContentsAllFalse(this.doorLayout)) ? true : false; //whether it has 4 doors or not
	this.doorImgURL = ''; //URL of the image of the door layout to be shown in Overview Mode
	this.imgURL = in_imgURL; //the URL of room to be shown in Picture Mode
	this.buildIcon = in_buildIcon; //an icon of the room to be shown in the room building frame
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

/*Switches the view from Picture Mode to Overview Mode */
function switchToOverviewMode(){
	if($('#overviewBorder').is(':hidden')){
		$house.overviewMode = true;
		$('#pictureMode').hide();
		$('#overviewBorder').show();
		toggleViewSwitchButtons();
		$('#infoPanelRoomButtons').show();
		//deselectAllRooms();
		//hide the buttons that allow you to demolish and rotate the room
		//$('#infoPanelRoomButtons').hide();
	}
}

/*Switches the view from Overview Mode to Picture Mode */
function switchToPictureMode(i, j, k){
	if($('#pictureMode').is(':hidden')){
		$house.overviewMode = false;
		$('#overviewBorder').hide();
		$('#pictureMode').show();
		toggleViewSwitchButtons();
		$('#pmImage').html('<img src="roomPics/' + houseArray[i][j][k].imgURL + '.jpg" width="512" height="314" />');
		
		//hide the buttons that allow you to demolish and rotate the room to avoid demolishing the room you're currently viewing
		$('#infoPanelRoomButtons').hide();
	}
}

/*Toggles the visibility of both the button that takes you back to Overview Mode and the dropdown that allows you to change the visible floor 
The one that is hidden will show and the one that is visible will be hidden */
function toggleViewSwitchButtons(){
	$('#facsTableR0').toggleClass('hidden');
	$('#facsTableR1').toggleClass('hidden');
}

/*Sets the ID (location) of the currently selected room in Overview mode*/
function setSelectedRoom(i, j, k, building){
	//console.log(i, j, k);
	//if you're trying to select the room that's already selected
	if(!building && (i !== -1 && j !== -1 && k !== -1) && i === $house.csr[0] && j === $house.csr[1] && k === $house.csr[2]){
		switchToPictureMode(i, j, k);
		return false;
	}
	//remove the visible selection from the previous room
	var roomID = "#room" + $house.csr[0] + $house.csr[1] + $house.csr[2];
	$(roomID).removeClass('selectedRoom');
	
	//select the new room
	$house.csr[0] = i;
	$house.csr[1] = j;
	$house.csr[2] = k;

	//if unselecting all rooms (demolishing or switching floor)
	if($house.overviewMode && (i == -1 || j == -1 || k == -1)){
		//hide the room info panel
		$('#infoPanelRoomStats').hide();
	} else {
		//visibly select the new room
		roomID = calculateRoomID(i, j, k);
		$(roomID).addClass('selectedRoom');
		//console.log('selected: ' + $house.csr[0] + " " + $house.csr[1] + " " + $house.csr[2]); 
		
		//if there's a room built there
		if(houseArray[$house.csr[0]][$house.csr[1]][$house.csr[2]].labelText !== ' '){
			updateRoomStatsContent();
			$('#infoPanelRoomStats').show();
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
	if($house.initialised && !$house.loading && typeID !== 0){
		setSelectedRoom(i, j, k, true);
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
		changeRoomOverviewImage(i + 1, j, k);

		var aboveID = calculateRoomID(i + 1, j, k);
		
		//if you're building something, make the room above clickable
		if(houseArray[i][j][k].labelText !== ' '){
			//create a link and such
			updateBlankRoomHTML(aboveID, i + 1, j, k)
		} else {
			//remove the ability to do anything useful
			$(aboveID).html(' ').unbind();
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

/*Provides something siimiliar to console.log where Firebug isn't available*/
function debug(text){
	if($house.debugMode){
		$('#debug').append(text + '<br />');
	}
}

$( document ).ready( function() {
	//$('.map').maphilight();
	//$('area').aToolTip({inSpeed: 400, outSpeed: 100});

	//console.time("document ready");

	//generate the core divs that everything will go in
	initCoreDivs();
	
	//add things to the top bar
	initTopBar();
	
	//make all sortable tables have the hawt zebra effect
	$.tablesorter.defaults.widgets = ['zebra'];
	
	//creating the table so you can make more rooms
	initRoomBuildingTable();
	/*$('#roomBuildingTable').tablesorter({
		//widgets : ['zebra'],
		sortList : [[2,0],[3,0],[1,0]]
	});*/
	$('#roomBuildingTable').dataTable({
		"aaSorting": [ [2,'asc'], [3,'asc'], [1,'asc'] ],
		iDisplayLength : 100,
		bLengthChange : false,
		"aoColumns": [
			{ "bSearchable": false },
			null,
			null,
			null
		]
	});
	
	//initialise the content for the Info Panel (the one on the right)
	initInfoPanel();

	//creating the initial house
	initHouse();
	
	//give tooltips to the bottom two floors, but not the top since you can't build there by default and they can be given tooltips as needed
	$('#' + determineIDOfFloor(0) + ' div').aToolTip();
	$('#' + determineIDOfFloor(1) + ' div').aToolTip();
	//$('#' + determineIDOfFloor(2) + ' div').aToolTip();

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

	/*$('#userStatsTable').tablesorter({
		sortList : [[1,0]]
	});*/
	$('#userStatsTable').dataTable({
		"aaSorting": [ [1,'asc'] ],
		iDisplayLength : 100,
		bLengthChange : false,
		"aoColumns": [
			{ "bSearchable": false },
			null,
			null
		]
	});
	
	//initMoveHouseTriggers();
	//console.log(addHouseMoveTriggers());
	
	//console.log('Planner width: ' + $('#POHPlanner').width());
	//console.log('width: ' + ($('#houseOverview').width() + $('#infoPanel').width()));
	//console.log('height: ' + $('#houseOverview').height());

	$house.initialised = true;
	
	/*$(document).keydown(function(e) {
		if (e.keyCode == '46') {
			demolishRoom0();
		}
	});*/
	
	//console.timeEnd("document ready");

	//var t = document.getElementById('POHPlanner').innerHTML;
	//console.log(t.length);
});