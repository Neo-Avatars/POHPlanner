/*-CONFIRMATION DIALOGUES AND ERROR MESSAGES */
//http://stackoverflow.com/questions/358168/how-to-create-a-custom-confirm-pause-js-execution-until-user-clicks-button

var POHInitialisation = function(){

	var init = this;
	
	this.dialogues = function(){
		init.realityCheck();
		init.dialogueButtonEvents();
		init.triggers();
		init.dialogueStoppingInputs();
	};

	/*Provides a way to stop the confirmation dialogues appearing again */
	this.dialogueStoppingInputs = function(){
		$('#confirmationDialogues .modalConfirmationButtons').each(function(){
			$(this).after(poh.htmlgen.dialogueStoppingInputs($(this).attr("id")))
		});
	};

	/*Adds the various events to buttons in the modal dialogues */
	this.dialogueButtonEvents = function(){
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
				poh.trans.moveHouseConfirmed(poh.$savedVariables.xChange, poh.$savedVariables.yChange);
			}
		});
	};
	
	/*Adds tooltip data to various elements so that it works fully in IE (the 'title' tip doesn't appear) and is valid markup */
	this.tooltipData = function(){
		$('#sharingCodeInput').data('tooltipText', 'The code for the house which can be used to share the house design with friends');
		$('#loadSharingCodeButton').data('tooltipText', 'Load house design from code');
		$('#facsTableR1').data('tooltipText', 'Choose which floor of the house to view');
		$('#returnToOverviewModeButton').data('tooltipText', 'Return to the House Overview mode');
		$('#facsTableR2').data('tooltipText', 'Enter your RuneScape username');
		$('#openCharStatsButton').data('tooltipText', 'View and edit your stats and completed quests');
		$('#fetchCharStatsButton').data('tooltipText', 'Fetch your stats from the RuneScape Hiscores');
		$('#demolishRoomButton').data('tooltipText', 'Demolish the Room');
		$('#rotateCwButton').data('tooltipText', 'Rotate the Room Clockwise');
		$('#rotateCcwButton').data('tooltipText', 'Rotate Anti-Clockwise');
	};

	/*-HIDDEN STUFF THAT YOU CAN'T SEE RELATED TO DIALOGUES */	

	/*Initialises triggers for buttons in various confirmation dialogues */
	this.triggers = function(){
		poh.trans.addMoveHouseTriggers();
		//addBuildRoomTriggers();
	};

	/*-THE GIANT DIVs THAT HOUSE EVERYTHING */

	/*Creates the core DIVs that house everything */
	this.coreDivs = function(){
		var core = poh.htmlgen.borders('topBarBorder', 'topBar');
		core += poh.htmlgen.borders('overviewBorder', 'houseOverview');
		core += '<div id="pictureMode" class="hidden">';
		core += poh.htmlgen.borders('pmImageBorder', 'pmImage');
		core += poh.htmlgen.borders('pmInfoBorder', 'pmInfo');
		core += '</div>';
		core += poh.htmlgen.borders('infoPanelBorder', 'infoPanel');
		$('#POHPlanner').html(core);
	};

	/*-THE BAR ACROSS THE TOP */

	/*Adds content to the top bar */
	this.topBar = function(){
		//$('#topBar').html(createTopBarDivs());
		//$('#topBarSharingDiv').html(createTopBarSharingContent());
		//$('#topBarMenu').html(createTopBarMenu());
		$('#topBar').html(poh.htmlgen.topBar());
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
			poh.sc.loadHouseFromSharingCode($('#sharingCodeInput').val());
		});
		$('#sharingCodeInput').keydown(function(e) {
			if(e.keyCode === 13){
				poh.sc.loadHouseFromSharingCode($('#sharingCodeInput').val());
			}
		});
	};

	/*-THE HOUSE ITSELF */

	/*Creates a blank house array with empty rooms
	Returns the blank array */
	this.createEmptyHouseArray = function(){
		var blankArray = new Array(3);
		//console.log('blank');
		for (var i = 0; i < 3; i++) {
			blankArray[i] = new Array(9);
			for (var j = 0; j < 9; j++) {
				blankArray[i][j] = new Array(9);
				for(var k = 0; k < 9; k++){
					blankArray[i][j][k] = new pohroom(roomsArray[0][0],roomsArray[0][1],roomsArray[0][2],
						roomsArray[0][3],roomsArray[0][4],roomsArray[0][5],roomsArray[0][6],
						roomsArray[0][7],roomsArray[0][8]);
				}
			}
		}
		return blankArray;
	};

	/*Create a blank house array with empty rooms*/
	this.houseArray = function(){
		//console.time("empty array");
		houseArray = poh.init.createEmptyHouseArray();
		//console.timeEnd("empty array");
	};

	/*Initialise the house, creating the HTML for it*/
	this.house = function() {
		//console.time("create house array");
		poh.init.houseArray();
		//console.timeEnd("create house array");	
		//console.log('about to build house');
		//console.time("building house");
		$('#houseOverview').html(poh.htmlgen.house());

		$('#houseOverview .room').each(function(){
			$(this).data('tooltipText', 'Click to build a room');
		});

		//console.log('made house');
		poh.rp.showSelectedOverviewFloor();
		//console.timeEnd("building house");
	};

	/*Builds the default rooms for the house */
	this.defaultRooms = function(){
		//try loading a previous house design from a cookie
		//console.log($.cookie('pohPlannerSharingCode'));
		if($.cookie('pohPlannerSharingCode') !== null){
			var code = $.cookie('pohPlannerSharingCode');
			poh.sc.loadHouseFromSharingCode(code);
		} else { //or fall back to a default value and create a cookie
			poh.sc.loadDefaultHouse();
		}
		//buildRoom(1, 4, 3, 1);
		//buildRoom(1, 4, 4, 2);
	};

	/*-RIGHT PANEL */

	/*Initilises the content for the #infoPanel */
	this.infoPanel = function(){
		//create sub-sections to put content in
		$('#infoPanel').html(poh.htmlgen.infoPanelCoreDivs());

		//create content for the floor select and stats setting section
		$('#infoPanelFloorAndCharStats').html(poh.htmlgen.infoPanelFloorAndCharStatsContent());
		$('#floorAndCharStatsTable tr').aToolTip();
		$('#floorAndCharStatsTable input').aToolTip();
		
		//create a way to view your stats
		$('#userStatsFrame .faceboxInner').html(poh.htmlgen.statsAndQuestStatusContent());
		
		//allow inputs > 99 for dungeoneering
		$('#userStatsTable input[name="dungeoneering"]').attr('maxlength', 3);

		//create content for the House Stats section
		poh.init.houseStatsContent();
		
		//Detects when the drop-down list for the selected floor is changed and shows the newly slected floor 
		$("#floorSelectDropDown").change(function(){
			if($('#overviewBorder').is(':hidden')){
				poh.pm.switchToOverviewMode();
			}
			poh.deselectAllRooms(); //so you can't demolish something you can't see
			poh.rp.hideSelectedOverviewFloor();
			var index = $("#floorSelectDropDown option").index($("#floorSelectDropDown option:selected"));
			$house.visibleOverviewFloor = index;
			poh.rp.showSelectedOverviewFloor();
		});
		
		//and the Room Stats
		poh.init.roomStatsContent();
		//before hiding it
		$('#infoPanelRoomStats').hide();
	};

	/*-HOUSE AND ROOM STATS TABLES */

	/*Initialises the House Stats Array content */
	this.houseStatsContent = function(){
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
		var id = 'houseStatsTable';
		$('#infoPanelHouseStats').html(poh.htmlgen.infoPanelStatsTable(tableContent, id, 'House'));
		if( $house.overviewOnly ){
			$('#' + id + ' tr:gt(2)').hide();
		}
		//$('#houseStatsTable tr').aToolTip({inSpeed: 800});
	};

	/*Initialises the Room Stats Array content */
	this.roomStatsContent = function(){
		//say what goes in each cell in the table
		var tableContent = [
			['Required Level:', '<span id="statsRoomLevel"></span>', '', 'The Construction level required to build the selected room'],
			['Room Cost:', '<span id="statsRoomCost">' + 0 + '</span>', 'gp', 'The cost to build the seleccted room'],
			['Furniture Cost:', '<span id="statsRoomFurniCost">' + 0 + '</span>', 'gp', 'The cost of all the furniture built within the selected room'],
			['Furniture Exp:', '<span id="statsRoomFurniXp">' + 0 + '</span>', 'xp', 'The Construction experience gained by building all the furniture in the selected room']
		];
		var id = 'roomStatsTable';
		$('#infoPanelRoomStats').html(poh.htmlgen.infoPanelStatsTable(tableContent, id, '<span id="statsRoomNameHeading">Room</span>'));	
		//$('#roomStatsTable tr').aToolTip({inSpeed: 800});
		if( $house.overviewOnly ){
			$('#' + id + ' tr:gt(2)').hide();
		}
		poh.init.roomStatsButtons();
	};

	/*Initialises the #infoPanelRoomButtons content */
	this.roomStatsButtons = function(){
		$('#infoPanelRoomStats').append(poh.htmlgen.roomStatsButtons());
		$('#infoPanelRoomButtons input').aToolTip();
	};

	/*-ROOM BUILDING TABLE */

	/*Initialise the table that allows you to build furniture and whatnot */
	this.roomBuildingTable = function(){
		$('#roomBuildingFrame .faceboxInner').html(poh.htmlgen.roomBuildingTable());
	};
	
	/*-REALITY CHECK */
	
	/*Initialise the reality check content */
	this.realityCheck = function(){
		var content = [
			[
				'MaxRooms',
				'Maximum Number of Rooms',
				'You have built <span id="rcBuiltRooms"></span> out of a maximum of <span id="rcMaxRooms"></span> rooms \
					that your Construction level allows for.'
			],
			[
				'DimensionLimit',
				'Dimension Limit',
				'The dimensions of your house are <span id="rcDimensionWidth"></span>x<span id="rcDimensionHeight"></span> rooms,\
					while your Construction level only allows for dimensions of <span id="rcDimensionAllowed"></span> rooms.'
			],
			[
				'HighLevelRooms',
				'Room Level Requirements',
				'There <span id="rcNoOfHighLevelRoomsIsAre"></span> <span id="rcNoOfHighLevelRooms"></span> room<span id="rcNoOfHighLevelRoomsPlural">s</span>\
					which your Construction level is not high enough to build.'
			],
			/*[
				'DisconnectedFloors',
				'Disconnected Floors',
				'There is no way to reach certain rooms due to a lack of stairs'
			],*/ //not worth using until furniture is enabled
			[
				'FloatingRooms',
				'Floating Rooms',
				'There are <span id="rcNoOfFloatingRooms"></span> rooms upstairs which have no room below them.\
					<div id="rcFloatingRoomLocations"></div>'
			]/*,
			[
				'MisplacedDungeons',
				'Misplaced Dungeon Rooms',
				'There are <span id="rcNoOfMisplacedDungeons"></span> dungeon rooms which have risen out of the dungeon:\
					<div id="rcMisplacedDungeonLocations"></div>'
			]*/ //should be limited before building so that this can't happen
		];
		
		$('#realityCheckFrame .faceboxInner').html(poh.htmlgen.realityCheck(content));
		//$('#realityCheckFrame').show();
	};

};