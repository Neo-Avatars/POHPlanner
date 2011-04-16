/*-CONFIRMATION DIALOGUES AND ERROR MESSAGES */
//http://stackoverflow.com/questions/358168/how-to-create-a-custom-confirm-pause-js-execution-until-user-clicks-button

function initDialogues(){
	initDialogueButtonEvents();
	initTriggers();
	initDialogueStoppingInputs();
}

/*Provides a way to stop the confirmation dialogues appearing again */
function initDialogueStoppingInputs(){
	$('#confirmationDialogues .modalConfirmationButtons').each(function(){
		$(this).after(createDialogueStoppingInputs($(this).attr("id")))
	});
}

/*Adds the various events to buttons in the modal dialogues */
function initDialogueButtonEvents(){
	//confirm house movement when room(s) would be demolished by doing so
	var demolishOnMoveButtons = $("#demolishOnMove button").click(function(e){
		//see if they want to continue with the move anyway
		var continueAnyway = demolishOnMoveButtons.index(this) === 0;
		
		//remember the setting if the box is checked
		if($('#demolishOnMoveButtonsCheck').is(':checked')){
			$house.noConfSettings.demolishOnMove.ignoreWarnings = true;
			$house.noConfSettings.demolishOnMove.value = continueAnyway ? true : false;
		}

		if(continueAnyway){
			moveHouseConfirmed($savedVariables.xChange, $savedVariables.yChange);
		}
	});
}

/*-HIDDEN STUFF THAT YOU CAN'T SEE RELATED TO DIALOGUES */	

/*Initialises triggers for buttons in various confirmation dialogues */
function initTriggers(){
	addMoveHouseTriggers();
	//addBuildRoomTriggers();
}

/*-THE GIANT DIVs THAT HOUSE EVERYTHING */

/*Creates the core DIVs that house everything */
function initCoreDivs(){
	var core = generateBorders('topBarBorder', 'topBar');
	core += generateBorders('overviewBorder', 'houseOverview');
	core += '<div id="pictureMode" class="hidden">';
	core += generateBorders('pmImageBorder', 'pmImage');
	core += generateBorders('pmInfoBorder', 'pmInfo');
	core += '</div>';
	core += generateBorders('infoPanelBorder', 'infoPanel');
	$('#POHPlanner').html(core);
}

/*-THE BAR ACROSS THE TOP */

/*Adds content to the top bar */
function initTopBar(){
	//$('#topBar').html(createTopBarDivs());
	//$('#topBarSharingDiv').html(createTopBarSharingContent());
	//$('#topBarMenu').html(createTopBarMenu());
	$('#topBar').html(createTopBar());
	$('#topBarSharingDiv input').aToolTip();
	
	$('#moveHouseNorth').data('movementData', { xDirection: true, positiveDirection: false, compass : 'north' });
	$('#moveHouseSouth').data('movementData', { xDirection: true, positiveDirection: true, compass : 'south' });
	$('#moveHouseWest').data('movementData', { xDirection: false, positiveDirection: false, compass : 'west' });
	$('#moveHouseEast').data('movementData', { xDirection: false, positiveDirection: true, compass : 'east' });
	
	//highlight the Sharing Code when it's box gets focus
	//in Chrome / Safari, you need to drag the mouse slightly when you click for it to highlight
	$('#sharingCodeInput').focus(function() {
		$(this).select();
	});
	
	//load the specified Sharing Code when the button is clicked or you hit enter when the Sharing Code input has focus
	$('#loadSharingCodeButton').click(function() {
		loadHouseFromSharingCode($('#sharingCodeInput').val());
	});
	$('#sharingCodeInput').keydown(function(e) {
		if(e.keyCode === 13){
			loadHouseFromSharingCode($('#sharingCodeInput').val());
		}
	});
}

/*-THE HOUSE ITSELF */

/*Creates a blank house array with empty rooms
Returns the blank array */
function createEmptyHouseArray(){
	var blankArray = new Array(3);

	for (var i = 0; i < 3; i++) {
		blankArray[i] = new Array(9);
		for (var j = 0; j < 9; j++) {
			blankArray[i][j] = new Array(9);
			for(var k = 0; k < 9; k++){
				blankArray[i][j][k] = new room(roomsArray[0][0],roomsArray[0][1],roomsArray[0][2],
					roomsArray[0][3],roomsArray[0][4],roomsArray[0][5],roomsArray[0][6]);
			}
		}
	}
	return blankArray;
}

/*Create a blank house array with empty rooms*/
function initHouseArray(){
	//console.time("empty array");
	houseArray = createEmptyHouseArray();
	//console.timeEnd("empty array");
}

/*Initialise the house, creating the HTML for it*/
function initHouse() {
	//console.time("create house array");
	initHouseArray();
	//console.timeEnd("create house array");	

	//console.time("building house");
	$('#houseOverview').html(createHouse());
	
	showSelectedOverviewFloor();
	//console.timeEnd("building house");
}

/*Builds the default rooms for the house */
function buildDefaultRooms(){
	var code;
	//try loading a previous house design from a cookie
	if($.cookie('pohPlannerSharingCode') !== null){
		code = $.cookie('pohPlannerSharingCode');
		loadHouseFromSharingCode(code);
	} else { //or fall back to a default value and create a cookie
		loadDefaultHouse();
	}
	//buildRoom(1, 4, 3, 1);
	//buildRoom(1, 4, 4, 2);
}

/*-RIGHT PANEL */

/*Initilises the content for the #infoPanel */
function initInfoPanel(){
	//create sub-sections to put content in
	$('#infoPanel').html(createInfoPanelCoreDivs());

	//create content for the floor select and stats setting section
	$('#infoPanelFloorAndCharStats').html(createInfoPanelFloorAndCharStatsContent());
	$('#floorAndCharStatsTable tr').aToolTip();
	$('#floorAndCharStatsTable input').aToolTip();
	
	//create a way to view your stats
	$('#userStatsFrame .faceboxInner').html(createStatsAndQuestStatusContent());

	//create content for the House Stats section
	initHouseStatsContent();
	
	//Detects when the drop-down list for the selected floor is changed and shows the newly slected floor 
	$("#floorSelectDropDown").change(function(){
		if($('#overviewBorder').is(':hidden')){
			switchToOverviewMode();
		}
		deselectAllRooms(); //so you can't demolish something you can't see
		hideSelectedOverviewFloor();
		var index = $("#floorSelectDropDown option").index($("#floorSelectDropDown option:selected"));
		$house.visibleOverviewFloor = index;
		showSelectedOverviewFloor();
	});
	
	//and the Room Stats
	initRoomStatsContent();
	//before hiding it
	$('#infoPanelRoomStats').hide();
}

/*-HOUSE AND ROOM STATS TABLES */

/*Initialises the House Stats Array content */
function initHouseStatsContent(){
	//initHouseStatsArray();
	
	//say what goes in each cell in the table
	var tableContent = [
		['Number of Rooms:', '<span id="statsRoomNumbers"><span id="statsBuiltRooms">' + $house.builtRooms + '</span> / <span id="statsMaxRooms">' + $house.maxRooms + '</span></span>', '',
			'The number of rooms that are currently built and the maximum you can build with your specified Construction level'],
		['Rooms Cost:', '<span id="statsTotalRoomCost">' + $house.totalRoomCost + '</span>', 'gp', 'The total cost of all the rooms that have been built'],
		['Furniture Cost:', '<span id="statsTotalFurniCost">' + $house.totalFurniCost + '</span>', 'gp', 'The total cost of all the furniture in the house'],
		['Furniture Exp:', '<span id="statsTotalFurniXp">' + $house.totalFurniXp + '</span>', 'xp', 'The total Construction experience gained by building all the furniture in the house'],
		['Total Cost:', '<span id="statsTotalHouseCost">' + ($house.totalRoomCost + $house.totalFurniCost) + '</span>', 'gp', 'The total cost of all the rooms and furniture combined']
	];
	
	$('#infoPanelHouseStats').html(generateInfoPanelStatsTable(tableContent, 'houseStatsTable', 'House'));
	//$('#houseStatsTable tr').aToolTip({inSpeed: 800});
}

/*Initialises the Room Stats Array content */
function initRoomStatsContent(){
	//say what goes in each cell in the table
	var tableContent = [
		['Required Level:', '<span id="statsRoomLevel"></span>', '', 'The Construction level required to build the selected room'],
		['Room Cost:', '<span id="statsRoomCost">' + 0 + '</span>', 'gp', 'The cost to build the seleccted room'],
		['Furniture Cost:', '<span id="statsRoomFurniCost">' + 0 + '</span>', 'gp', 'The cost of all the furniture built within the selected room'],
		['Furniture Exp:', '<span id="statsRoomFurniXp">' + 0 + '</span>', 'xp', 'The Construction experience gained by building all the furniture in the selected room']
	];
	
	$('#infoPanelRoomStats').html(generateInfoPanelStatsTable(tableContent, 'roomStatsTable', '<span id="statsRoomNameHeading">Room</span>'));	
	//$('#roomStatsTable tr').aToolTip({inSpeed: 800});
	
	initRoomStatsButtons();
}

/*Initialises the #infoPanelRoomButtons content */
function initRoomStatsButtons(){
	$('#infoPanelRoomStats').append(createRoomStatsButtons());
	$('#infoPanelRoomButtons input').aToolTip();
}

/*-ROOM BUILDING TABLE */

/*Initialise the table that allows you to build furniture and whatnot */
function initRoomBuildingTable(){
	$('#roomBuildingFrame .faceboxInner').html(createRoomBuildingTable());
}