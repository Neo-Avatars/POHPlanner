/*-THE GIANT DIVs THAT HOUSE EVERYTHING */

/*Creates the core DIVs that house everything */
function initCoreDivs(){
	var core = '<div id="topBar"></div>';
	core += generateBorders('overviewBorder', 'houseOverview');
	core += generateBorders('infoPanelBorder', 'infoPanel');
	$('#POHPlanner').html(core);
}

/*-THE BAR ACROSS THE TOP */

/*Adds content to the top bar */
function initTopBar(){
	$('#topBar').html(createTopBarDivs());
	$('#topBarSharingDiv').html(createTopBarSharingContent());
	$('#topBarMenu').html(createTopBarMenu());
	
	//highlight the Sharing Code when it's box gets focus
	$('#topBarSharingDiv input[type=text]').focus(function() {
		$(this).select();
	});
	
	//load the specified Sharing Code when the button is clicked
	$('#loadSharingCodeButton').click(function() {
		loadHouseFromSharingCode($('#sharingCodeInput').val());
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
	var code = "UDOB,UEBC"; //default
	loadHouseFromSharingCode(code);
	
	//buildRoom(1, 4, 3, 1);
	//buildRoom(1, 4, 4, 2);
	
	//set the Sharing Code for the world to see
	$('#sharingCodeInput').val(code);
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

	//create content for the House Stats section
	initHouseStatsContent();
	
	/*Detects when the drop-down list for the selected floor is changed and shows the newly slected floor */
	$("#floorSelectDropDown").change(function(){
		deselectAllRooms(); //so you can't demolish something you can't see and such
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
		['Number of Rooms:', $house.builtRooms + ' / ' + $house.maxRooms, ''],
		['Rooms Cost:', $house.totalRoomCost, 'gp'],
		['Furniture Cost:', $house.totalFurniCost, 'gp'],
		['Furniture Exp:', $house.totalFurniXp, 'xp'],
		['Total Cost:', ($house.totalRoomCost + $house.totalFurniCost), 'gp']
	];
	
	$('#infoPanelHouseStats').html(generateInfoPanelStatsTable(tableContent, 'houseStatsTable', 'House'));
}

/*Initialises the Room Stats Array content */
function initRoomStatsContent(){
	//say what goes in each cell in the table
	var tableContent = [
		['Required Level:', '', ''],
		['Room Cost:', 0, 'gp'],
		['Furniture Cost:', 0, 'gp'],
		['Furniture Exp:', 0, 'xp']
	];
	
	$('#infoPanelRoomStats').html(generateInfoPanelStatsTable(tableContent, 'roomStatsTable', 'Room'));	
	
	initRoomStatsButtons();
}

/*Initialises the #infoPanelRoomButtons content */
function initRoomStatsButtons(){
	$('#infoPanelRoomStats').append(createRoomStatsButtons());
}

/*-ROOM BUILDING TABLE */

/*Initialise the table that allows you to build furniture and whatnot */
function initRoomBuildingTable(){
	$('#roomBuildingFrame').html(createRoomBuildingTable());
}