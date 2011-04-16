//the aToolTip plugin has been changed to make it work as wanted
//the maphilight plugin has been modified in 'mouseover = function(e)' so that it works with alwaysOn set to true. Defaults also changed.
//TODO fix flicker on refresh

//http://stackoverflow.com/questions/883033/memento-in-javascript - memento undo implementation
//http://en.wikipedia.org/wiki/Memento_pattern - wiki desc

var $house = {
	debugMode : true, //whether firebug-less debug statements should be outputted
	fullAccessMode : false, //whether things requiring access 
	sharingCodeVersion : 0, //the latest version ID of the Sharing Code to allow for changes to it's format to be made
	builtRooms : 0, //the number of rooms that have been built
	maxRooms : 20, //the maximum number of rooms that can be built based on the specified Construction level
	maxDimensions : 3, //the maximum square dimenion of the house in terms of the number of rooms in a given direction based on the specified Construction level
	totalRoomCost : 0, //the total cost of all the rooms
	totalFurniCost : 0, //the total cost of all the furniture
	totalFurniXp : 0, //the total experience that building all the specified furniture will provide
	visibleOverviewFloor : 1, //the floor that is being viewed in Overview Mode
	overviewMode : true, //whether you're viewing the Overview Mode (true) or the Picture Mode (false)
	overviewOnly : false, //if set to true, means that the furni build mode is not visible
	csr : [-1, -1, -1], //the coordinates (i, j, k) of the Currently Selected Room
	initialised : false, //whether the house has fully initialised
	loading : false, //loading a layout from a Sharing Code
	transforming : false, //transforming or translating the house from the Transform menu
	hasCostumeRoom : false, //the house has a costume room built (can only have one)
	hasMenagerie : false, //the house has a menagerie built (can only have one)
	noConfSettings : { //settings to remember the choices for various options
		demolishOnMove : { //the warning about rooms being demolished when moving a house too far in any direction
			value : false, //false = 'no', true = 'yes'
			description : 'Demolish any rooms in the way when moving the house', //describes what it does
			ignoreWarnings : false //whether you should ignore warnings and error messages about this
		}/*,
		levelLimit : { //level limits when building a room or furniture
			value : false,
			description : 'Build rooms and furniture even if you do not have the levels to do so',
			ignoreWarnings : false
		},
		maxRoomLimit : { //the maxRooms limit that the house has
			value : false,
			description : 'Ignore the limit on the maximum number of rooms that can be built',
			ignoreWarnings : false
		}*/
	}
};

var POHPlanner = function( overviewOnly ){
	var poh = this;
	
	$house.overviewOnly = overviewOnly;
	
	this.sc = new POHSharingCode();
	
	this.trans = new POHTransformation();
	
	this.init = new POHInitialisation();
	
	this.htmlgen = new POHHTMLGeneration();
	
	this.rp = new POHRightPanel();
	
	this.rc = new POHRealityCheck();
	
	this.pm = new POHPictureMode();

	this.$stats = { //the user's stats
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
	this.houseArray = poh.init.createEmptyHouseArray(); //the array that makes up the house - created in initHouseArray()
	//var roomsArray; //an array of all the different types of room, set in POH-rooms.js
	this.$savedVariables = { //somewhere to store temporary variables when the code goes off to do something else
		//tooltipTitle : '', //a variable to make the aToolTips work as wanted
		//tooltipID : '', //another variable to make the aToolTips work as wanted
	};
	
	$( document ).ready( function() {
		//console.log('pow');
		poh.initialise();
	});
	
	this.initialise = function(){
		//$('.map').maphilight();
		//$('area').aToolTip({inSpeed: 400, outSpeed: 100});
		
		//console.time("initialise planner");
		/*$.fn.maphilight.defaults = {
			fill: true,
			fillColor: 'ffffff',
			fillOpacity: 0.3,
			stroke: true,
			strokeColor: 'ff0000',
			strokeOpacity: 1,
			strokeWidth: 1,
			fade: false,
			alwaysOn: true,
			neverOn: false,
			groupBy: false,
			wrapClass: true
		};*/
		
		//stop the context menu appearing when double-clicking in Opera
		//http://operawatch.com/news/2007/07/opera-tip-how-to-disable-the-context-menu-when-double-clicking-on-text.html
		if( $('.opera').length > 0 ){
			$('#POHPlanner').dblclick(function(){
				return false;
			});
		}
		
		$.tools.overlay.addEffect('furniBuildFrame', 
			function(pos, onLoad) {
				//console.log('trigger', this.getTrigger());
				poh.pm.displayFurnitureBuildingFrame(this.getTrigger().data());
				
				var frameVisible = $('#furniBuildingFrame:visible').length > 0 ? true : false;
				//console.log(frameVisible, onLoad, this.getOverlay().html());
				
				var conf = this.getConf(),
					 w = $(window);				 
					
				if (!conf.fixed)  {
					pos.top += w.scrollTop();
					pos.left += w.scrollLeft();
				} 
					
				pos.position = conf.fixed ? 'fixed' : 'absolute';
				//console.log('open', this.getTrigger());
				
				if(frameVisible){
					this.getOverlay().css(pos).fadeIn(0, onLoad);
				} else {
					this.getOverlay().css(pos).fadeIn(conf.speed, onLoad);
				}
				
			}, function(onClose) {
				//var furniBuildTrigger = this.getTrigger() === this.getConf()
				//console.log('close', this.getTrigger(), onClose);
				//this.getOverlay().fadeOut(this.getConf().closeSpeed, onClose); 
				this.getOverlay().fadeOut(1, onClose); 
			}		
		);
		
		if($house.fullAccessMode){
			$.fn.qtip.styles.realm = {
				width: {
					min : 50,
					max : 350
				},
				"font-size": "11px",
				"color" : "#000",
				"background" : "#C59F70",
				title : {
					"color" : "#4F2F0C",
					"background" : "#AC875A",
					"border-bottom" : "1px solid #7F5F27",
					"font-size" : "11px"
				},
				border: {
					width : 1,
					radius : 0,
					color : "#7F5F27"
				}
			};
		}

		//generate the core divs that everything will go in
		poh.init.coreDivs();
		
		//add things to the top bar
		poh.init.topBar();
		
		//creating the table so you can make more rooms
		poh.init.roomBuildingTable();

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
		poh.init.infoPanel();

		//creating the initial house
		poh.init.house();
		
		//give tooltips to the bottom two floors, but not the top since you can't build there by default and they can be given tooltips as needed
		$('#' + poh.determineIDOfFloor(0) + ' div .room').aToolTip();
		$('#' + poh.determineIDOfFloor(1) + ' div .room').aToolTip();
		//$('#' + poh.determineIDOfFloor(2) + ' div').aToolTip();

		//adds the various events to buttons in the modal dialogues
		//initDialogueButtonEvents();
		poh.init.dialogues();
		
		$('#userStatsTable .userStatsInput').blur(function(){
			var value = isNaN( parseInt($(this).val()) ) ? 1 : parseInt($(this).val())
			poh.setStat($(this).attr("name"), value);
		});
		
		$('#rcConLevelInput').blur(function(){
			var value = isNaN( parseInt($(this).val()) ) ? 1 : parseInt($(this).val())
			poh.setStat($(this).attr("name"), value);
			poh.rc.calculate();
		}).keyup(function(){
			var value = parseInt($(this).val());
			if(!isNaN( value )){
				if(value !== poh.$stats.construction){
					poh.setStat($(this).attr("name"), value);
					poh.rc.calculate();
				}
			}
		});
		
		//only allow numeric input
		$('.userStatsInput, .rcStatsInput').keydown(function(e){
			var key = e.charCode || e.keyCode || 0;
            return isValidNumericInputKey( key ) || ( key >= 37 && key <= 40 );
		});
		
		$('#extra .faceboxInner').after(poh.htmlgen.faceboxCloseButton());
		
		//make clicking on rooms in Overview mode open up the table they link to in a facebox
		$('#houseOverview .faceboxLink').overlay();
		
		//make clicking on the 'Stats' button open up the stats panel
		$('#openCharStatsButton').overlay();
		
		//show the Reality Check Pane when the menu button is clicked
		$('#rcMenuLink').overlay({
			onBeforeLoad: function(e){
				poh.rc.calculate();
			}
		});
		
		poh.init.tooltipData();

		//the default rooms need to be added after everything has been facebox'd otherwise the 'zebra' will not work in
		//the Room Building Frame when first choosing to build a room upstairs above them
		poh.init.defaultRooms();

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

		//delete a selected room by pressing the 'delete' key
		$(document).keydown(function(e) {
			//console.log(e.keyCode);
			if (e.keyCode == '46') {
				if( $house.csr[0] !== -1 && $house.csr[1] !== -1 && $house.csr[2] !== -1
						&& $house.overviewMode === true){
					poh.demolishRoom0();
				}
			}
		});
		
		/*if (Modernizr.canvas) {
		   debug('supports canvas!');
		} else {
			debug('does not support canvas!');
		}*/
		
		$house.initialised = true;
		
		//console.timeEnd("initialise planner");

		//var t = document.getElementById('POHPlanner').innerHTML;
		//console.log(t.length);
	};
	
	/*Sets the specified stat to the specified level */
	this.setStat = function(skill, level){
		//console.time('set stat');
		if(level < 1){ //can't have negative levels
			level = 1;
		} else if (level > 120){ //or ones above 120
			level = 120;
			
			if(skill !== 'dungeoneering'){ //or above 99 if not a load of dung
				level = 99;
			}
		}

		if(skill === "construction"){
			poh.setMaxRooms(level);
			poh.setMaxDimensions(level);
			$('#rcConLevelInput').val(level);
			$('#usi' + skill).val(level);
		}
		
		poh.$stats[skill] = level;
		//console.timeEnd('set stat');
	};
	
	/*Sets the maximum number of rooms that you can have built based on your Construction level and updates the relevant visible information */
	this.setMaxRooms = function(level){
		$house.maxRooms = poh.calculateMaxRooms(level);
		poh.rp.updateMaxNumberOfRooms();
	};
	
	/*Returns the maximum number of rooms you can build with the specified Construction level */
	this.calculateMaxRooms = function(level){
		var max = level < 38 ? 20 : level >= 99 ? 32 : level > 95 ? 31 : Math.floor(20 + ((level - 32) / 6));
		return max;
	};
	
	/*Sets the maximum number of rooms that you can have built based on your Construction level and updates the relevant visible information */
	this.setMaxDimensions = function(level){
		$house.maxDimensions = poh.calculateMaxDimensions(level);
	};
	
	/*Returns the maximum dimension of the house with the specified Construction level */
	this.calculateMaxDimensions = function(level){
		var limit = level < 15 ? 3 : level >= 75 ? 8 : Math.floor(3 + (level / 15));
		return limit;
	};
	
	/*Returns true if the array contents are the same, otherwise false */
	this.checkArrayContentsAreTheSame = function(arrayOne, arrayTwo){
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
	};
	
	/*Returns true if there's a string input of 'true' otherwise returns false */
	this.convertStringToBoolean = function(input){
		return input === 'true' ? true : false;
	};
	
	/*Returns the string with a capitalised first letter */
	this.capitaliseString = function(str){
		return str.substr(0,1).toUpperCase() + str.substr(1,str.length);
	};

	/*Sets the ID (location) of the currently selected room in Overview mode*/
	this.setSelectedRoom = function(i, j, k, building){
		//console.log(i, j, k);
		//if you're trying to select the room that's already selected
		if(!building && (i !== -1 && j !== -1 && k !== -1) &&
				i === $house.csr[0] && j === $house.csr[1] && k === $house.csr[2]
				&& poh.houseArray[i][j][k].labelText !== ' '){
			poh.pm.switchToPictureMode(i, j, k);
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
			roomID = poh.calculateRoomID(i, j, k);
			$(roomID).addClass('selectedRoom');
			//console.log('selected: ' + $house.csr[0] + " " + $house.csr[1] + " " + $house.csr[2]); 
			
			//if there's a room built there
			if(poh.houseArray[$house.csr[0]][$house.csr[1]][$house.csr[2]].labelText !== ' '){
				poh.rp.updateRoomStatsContent();
				$('#infoPanelRoomStats').show();
				poh.hideVisibleFrames();
			} else { //otherwise hide the panel
				$('#infoPanelRoomStats').hide();
			}
		}
	};

	/*Hides any visible frames such as the Room Building or Stats frames */
	this.hideVisibleFrames = function(){
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
	};

	/*Deselects the current room by setting it to the default [-1, -1, -1] */
	this.deselectAllRooms = function(){
		poh.setSelectedRoom(-1, -1, -1);
	};

	/*Returns the #roomID of the room located in position houseArray[i][j][k] */
	this.calculateRoomID = function(i, j, k){
		return "#room" + i + j + k;
	};

	/*Determines the class for the door layout background image for a room in Overview Mode by looking at the door locations and room type
	Returns a string containing the class name for that background */
	this.determineRoomOverviewImage = function(i, j, k){
		var imageURL = 'ROI'; //a default (Room Overview Image) so that it's hopefully easier to make sense of the stylesheet
		
		//indicate a dungeon or upstairs room
		if(i === 0 && poh.houseArray[i][j][k].labelText === ' '
			|| poh.houseArray[i][j][k].labelText.indexOf("Dungeon") != -1
				|| poh.houseArray[i][j][k].labelText === 'Treasure Room'){
			imageURL += "d";
		} else if(i == 2 && poh.houseArray[1][j][k].labelText !== ' '
					&& poh.houseArray[i][j][k].labelText === ' '){
			imageURL += "u";
		}
		
		//compass directions of the doors
		if(poh.houseArray[i][j][k].doorLayout[0] == true){
			imageURL += "n";
		}
		if(poh.houseArray[i][j][k].doorLayout[1] == true){
			imageURL += "e";
		}
		if(poh.houseArray[i][j][k].doorLayout[2] == true){
			imageURL += "s";
		}
		if(poh.houseArray[i][j][k].doorLayout[3] == true){
			imageURL += "w";
		}

		//special types
		if(poh.houseArray[i][j][k].labelText === ' '){
			imageURL += "empty";
		} else if(poh.houseArray[i][j][k].labelText.indexOf('Garden') != -1){
			imageURL += "garden";
		} else if(poh.houseArray[i][j][k].labelText.indexOf('Dungeon') != -1){
			if(poh.houseArray[i][j][k].labelText === 'Dungeon Junction'){
				imageURL += "j";
			} else if(poh.houseArray[i][j][k].labelText === 'Dungeon Stairs'){
				imageURL += "s";
			}
		} else if(poh.houseArray[i][j][k].labelText === 'Treasure Room'){
			imageURL += "t";
		}
		//it should have at least one door if you're going to be able to enter, so something has probably gone wrong
		if(imageURL === 'ROI'){
			imageURL += "probableError";
		}

		return imageURL;
	};

	/*Returns the #ID of the floor based on the number (0, 1, 2) */
	this.determineIDOfFloor = function(i){
		if(i === 0){
			return "dungeon";
		} else if(i === 1){
			return "groundFloor";
		} else {
			return "upstairs";
		}
	};

	/*Returns the text that is used as the tooltip for rooms in Overview mode*/
	this.determineRoomOverviewTooltipText = function(i, j, k){
		if (i === 2 && poh.houseArray[i][j][k].doorImageURL === 'ROIempty'){
			return "";
		} else if (poh.houseArray[i][j][k].labelText === ' '){
			return "Click to build a room";
		} else {
			var str = poh.houseArray[i][j][k].labelText;
			if( !$house.overviewOnly ){
				str += " - Click to enter furniture build mode";
			}
			return str;
		}
	};

	/*Removes the class of the previous room type when building a new one */
	this.removeExistingRoomOverviewImage = function(i, j, k){
		//console.log(i + " " + houseArray[i][j][k].doorImageURL);
		$(poh.calculateRoomID(i, j, k)).removeClass(poh.houseArray[i][j][k].doorImageURL);
	};

	/*Sets the background image for a room in Overview Mode */
	this.setRoomOverviewImage = function(i, j, k){
		poh.houseArray[i][j][k].doorImageURL = poh.determineRoomOverviewImage(i, j, k);
		$(poh.calculateRoomID(i, j, k)).addClass(poh.houseArray[i][j][k].doorImageURL);
	};

	/*Updates the Room Overview Image when rotating a room so that only one thing needs calling */
	this.changeRoomOverviewImage = function(i, j, k){
		//console.log(i + " s " + houseArray[i][j][k].doorImageURL);
		poh.removeExistingRoomOverviewImage(i, j, k);
		poh.setRoomOverviewImage(i, j, k);
	};

	/*Builds a room*/
	this.buildRoom = function(i, j, k, typeID){
		/*if($house.initialised && !$house.loading){
			hideVisibleFrames();
		}*/
		//console.time("build room");
		var roomID = poh.calculateRoomID(i, j, k);
		
		//if building a floating room - could occur down to a rearranged Sharing Code
		if(i === 2 && poh.houseArray[2][j][k].labelText === ' ' &&
				poh.houseArray[1][j][k].labelText === ' '){
			//console.log('pow');
			poh.updateBlankRoomHTML(roomID, i, j, k, true);
		}

		poh.removeExistingRoomOverviewImage(i, j, k);
		
		//build the room
		poh.houseArray[i][j][k] = new pohroom(roomsArray[typeID][0],roomsArray[typeID][1],roomsArray[typeID][2],
			roomsArray[typeID][3],roomsArray[typeID][4],roomsArray[typeID][5],roomsArray[typeID][6],
			roomsArray[typeID][7],roomsArray[typeID][8]);

		//visibly show the change by switching the background image
		poh.setRoomOverviewImage(i, j, k);
		//and changing the tooltip
		$(roomID).data('tooltipText', poh.determineRoomOverviewTooltipText(i, j, k));
		//$(roomID).data('atooltip', poh.determineRoomOverviewTooltipText(i, j, k));
		//and updating the room above
		poh.updateRoomAbove(i, j, k);
		//then switching the text
		$(roomID  + ' a').html(poh.houseArray[i][j][k].labelText)
		
		//console.log('hc',i,j,k,$(roomID  + ' a').hasClass('faceboxLink'));
		//if building a blank 'room'
		if(poh.houseArray[i][j][k].labelText === ' '){
			
		} else {//update the House Stats
			poh.rp.updateHouseStatsContent(true, i, j, k);
			//and remove the facebox link, replacing it with a link to Picture Mode
			$(roomID  + ' a').removeClass("faceboxLink").attr("href", '#'/*roomID + "PictureMode"*/).unbind('click');
		}
		if($house.initialised && !$house.loading && typeID !== 0){
			poh.setSelectedRoom(i, j, k, true);
			poh.sc.calculateSharingCode(poh.houseArray);
		}
		//console.timeEnd("build room");
	};

	/*Multiple functions are based on having different numbers of parameters so that it can be used in different situations
	The digit on the end denotes the number of parameters and if it isn't there, problems with infinite recursion occur */
	/*Demolishes a room, the parameters being the location of the room to demolish */
	this.demolishRoom3 = function(i, j, k){
		
		poh.rp.updateFurniValuesOnRoomDemolition( i, j, k );
		
		var roomID = poh.calculateRoomID(i, j, k);
		//var roomAboveID = poh.calculateRoomID(i + 1, j, k);
		
		poh.rp.updateHouseStatsContent(false, i, j, k);

		//build an empty 'room' in it's place
		poh.buildRoom(i, j, k, 0);
		
		//if demolishing the selected room, update the sharing code
		if( i === $house.csr[0] && j === $house.csr[1] && k === $house.csr[2] ){
			poh.sc.calculateSharingCode(poh.houseArray);
		}
		
		poh.deselectAllRooms();

		//$(roomID + ' a').addClass("faceboxLink").attr("href", "#roomBuildingFrame");
		//$(roomID + ' .faceboxLink').overlay();
		//make it posible to build a new room in it's place - need to fully replace the innerHTML otherwise the overlay won't work
		poh.updateBlankRoomHTML(roomID, i, j, k, false);
		
		poh.updateRoomAbove(i, j, k);
	};

	/*Demolishes the selected room */
	this.demolishRoom0 = function(){
		poh.demolishRoom3($house.csr[0], $house.csr[1], $house.csr[2]);
	};
	
	/*Check to see if there's a room above the selected ground floor room */
	this.hasRoomAbove = function(i, j, k){
		return poh.houseArray[i + 1][j][k].labelText === ' ' ? false : true;
	};

	/*Updates the image of the room above and whether you're able to build anything there, so you can't build a room in mid air Upstairs */
	this.updateRoomAbove = function(i, j, k){
		//if you're building a room on the Ground Floor and there's nothing above
		if(i === 1 && !( poh.houseArray[i][j][k].labelText === ' '
							&& poh.houseArray[i + 1][j][k].labelText !== ' ' )
					&& poh.houseArray[i][j][k].labelText.indexOf('Garden') === -1 ){
			//console.log('updating above');
			poh.changeRoomOverviewImage(i + 1, j, k);

			var aboveID = poh.calculateRoomID(i + 1, j, k);
			
			//if you're building something and there's no floater above, make the room above clickable
			if(poh.houseArray[i][j][k].labelText !== ' ' && poh.houseArray[i + 1][j][k].labelText === ' '){
				//create a link and such
				poh.updateBlankRoomHTML(aboveID, i + 1, j, k, false)
			} else if (poh.houseArray[i][j][k].labelText === ' ' && poh.houseArray[i + 1][j][k].labelText === ' '){
				//remove the ability to do anything useful
				$(aboveID).html(' ').unbind();
			}
		}
	};

	/*Replaces the HTML in a room div in Overview mode, then makes it so clicking on it will bring up the room building frame
	It almost certainly isn't the best solution, but if the whole innerHTML isn't replaced, there are problems with dead clicks after demolishing rooms */
	this.updateBlankRoomHTML = function(roomID, i, j, k, forceFloat){
		if(!forceFloat && i === 2 && poh.houseArray[i - 1][j][k].labelText === ' '){
			$(roomID).html(' ');
		} else {
			//var content = ;
			//console.log('blank room html');
			$(roomID).html('<span><a href="#roomBuildingFrame"\
				onclick="poh.setSelectedRoom('+i+','+j+','+k+');return false;"\
					class="overviewLink faceboxLink" rel="#roomBuildingFrame">' +
						poh.houseArray[i][j][k].labelText + '</a></span>');
			//document.getElementById('room'+i+j+k).innerHTML = content;
			//add the tooltip
			$(roomID).data('tooltipText', poh.determineRoomOverviewTooltipText(i, j, k)).aToolTip();
			
			//add the overview link
			$(roomID + ' .faceboxLink').overlay();
		}
	};

	/*Adds commas to numbers >= 1000 to make them easier to read
	Doesn't work for negative numbers, although shouldn't need to, so it doesn't matter
	About 15% faster (using console.time() with Firebug) than anything Google will find */
	this.addCommasToLargeNumbers = function(number){
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
	};
};

/*Returns true if all elements of an array are 'false', otherwise returns false */
function checkArrayContentsAllFalse(array){
	for(var i = 0; i < array.length; i++){
		if(array[i] === true){
			return false;
		}
	}
	return true;
}

/*Checks a key input to only allow numbers to be inputted into level boxes */
function isValidNumericInputKey( key ) {
    return ( key >= 48 && key <= 57 ) || ( key >= 96 && key <= 105 ) || key == 8 || key == 9 || key == 46;
}

function initQTips( parent ) {
    $( "[title],a[rel]", $( parent ) ).each( function() {
        var content = {
            text: false
        };
        
        if( typeof $( this ).data( "qtip" ) == "object" && $( this ).data( "qtip" ) !== null  ) {
            return true;
        }

        if( $( this ).is( "a[rel]" ) ) {
            content = {
                url : $( this ).attr( "rel" ),
                text : "<img src='assets/templates/SalsRealm/images/ajax-loader.gif' \
							width='16' height='11' alt='Loading...' />",
                title : {
                    text : $( this ).text()
                }
            };
        }

        $( this ).qtip( {
            content : content,
            style : "realm",
            position : {
                target : "mouse",
                corner : {
                    target : "topRight",
                    tooltip : "bottomLeft"
                },
                adjust : {
                    mouse : true,
                    screen : true
                }
            },
            show : {
                solo : true
            },
            hide : {
                when : {
                    event : "mouseout"
                }
            },
            api : {
                onContentUpdate : function() {
                    if( this.elements.tooltip.find( ".title" ).length ) {
                        this.updateTitle( this.elements.tooltip.find( ".title" ).html() );
                    }
                }
            }
        } );
    } );
}

/*-DATA CLASSES */
/*Room*/
function pohroom(in_typeID, in_labelText, in_level, in_cost, in_doorLayout, in_imgURL, in_buildIcon, in_hotspots, in_coords){
	this.typeID = in_typeID; //an ID relating to the type of the room
	this.labelText = in_labelText; //the type of room eg. Garden, Parlour
	this.level = in_level; //the level required to build the room
	this.cost = in_cost; //the cost of the room in gp
	this.doorLayout = in_doorLayout; //the door layout of the room, starting in the north and heading clockwise
	this.hasFourDoors = (checkArrayContentsAllFalse(this.doorLayout)) ? true : false; //whether it has 4 doors or not
	this.doorImgURL = ''; //URL of the image of the door layout to be shown in Overview Mode
	this.imgURL = in_imgURL; //the URL of room to be shown in Picture Mode
	this.buildIcon = in_buildIcon; //an icon of the room to be shown in the room building frame
	this.hotspots = in_hotspots; //the hotspots that are available in the room
	this.furniture = new Array(in_hotspots.length); //somewhere to store the contents of each hotspot
	/*var i = 0;
	while(i < this.hotspots.length){
		this.hotspots[i] = new pohhotspot(hotspotsArray[in_hotspots][0],hotspotsArray[in_hotspots][1],hotspotsArray[in_hotspots][2]);
		i++;
	}*/
	this.coords = in_coords; //the coordinates of each hotspot on the Picture Mode image
}

/*Materials */
function pohmaterial(in_typeID, in_name, in_idbID, in_GEcost, in_shopCost, in_image, in_xp){
	this.typeID = in_typeID; //an ID relating to the type of the material
	this.name = in_name; //the name of the material
	this.idbID = in_idbID; //the ID in the IDB
	this.GEcost = in_GEcost; //the cost on the GE
	this.shopCost = in_shopCost; //the cost from a shop, -1 if not available from a shop
	this.image = 'materialPics/' + in_image + '.gif'; //the item image
	this.GEID = in_GEID; //the ID on the GEDB
	this.xp = in_xp; //an array showing the experience provided by the material.  The first value is construction, others are additional skills with the skill ID provided
}

/*Furniture */
function pohfurniture(in_typeID, in_name, in_materials, in_flatpack, in_level, in_xpmod){
	this.typeID = in_typeID; //an ID relating to the type of the furniture
	this.name = in_name; //the name of the furniture
	this.materials = in_materials; //an array containing the material typeIDs and the number of each type of material [[type, no], [type, no], [etc...]]
	this.flatpack = in_flatpack; //a JSON object with a range of data about flatpacks - flatpackable, IDBID, cost
	this.level = in_level; //an array showing the level requirements needed to build the furniture.  The first value is construction, others are additional skills with the skill ID provided
	this.xpmod = in_xpmod; //sometimes bits of furniture, particularly with nails, don't give the amount of xp you'd expect from the materials. An array as above showing modified xp.
}

/*Hotspots */
function pohhotspot(in_typeID, in_name, in_furniture){
	this.typeID = in_typeID; //an ID relating to the type of the hotspot
	this.name = in_name; //the name of the hotspot
	this.furniture = in_furniture; //an array of furniture typeIDs that can be built here
	this.active = -1; //the typeID of the furniture that is currently built in this position
}

/*Provides something siimiliar to console.log where Firebug isn't available*/
function debug(text){
	if($house.debugMode){
		$('#debug').append(text + '<br />');
	}
}