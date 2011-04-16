/*-THE BAR ACROSS THE TOP */

/*Creates the DIVs for the sections within the Top Bar
Returns the constructed HTML */
function createTopBarDivs(){
	var divs = '<div id="topBarMenu" class="POHmenu"></div>\
	<div id="topBarSharingDiv"></div>';
	
	return divs;
}

/*Creates the content for the #topBarSharingDiv
Returns the constructed HTML */
function createTopBarSharingContent(){
	var content = '<span id="sharingCodeLabel">House Plan:</span> \
	<input type="text" name="sharingCodeInput" class="sharingCodeInput" id="sharingCodeInput"\
		title="The code for the house which can be used to share the house design with friends" />\
	<input type="submit" value="Load" class="loadSharingCodeButton"\
			id="loadSharingCodeButton" title="Load house design from code" />';
	
	return content;
}

/*Creates the menu for the #topBarMenu
Returns the constructed HTML */
function createTopBarMenu(){
	var content = '<ul>\
		<li><a href="#">File<!--[if IE 7]><!--></a><!--<![endif]-->\
		<!--[if lte IE 6]><table><tr><td><![endif]-->\
			<ul>\
			<li><a href="#" onclick="loadDefaultHouse();return false;">Reset House</a></li>\
			</ul>\
		<!--[if lte IE 6]></td></tr></table></a><![endif]-->\
		</li>\
		<li><a href="#">Transform<!--[if IE 7]><!--></a><!--<![endif]-->\
		<!--[if lte IE 6]><table><tr><td><![endif]-->\
			<ul>\
			<li><a href="#">Move House<!--[if IE 7]><!--></a><!--<![endif]-->\
			<!--[if lte IE 6]><table><tr><td><![endif]-->\
				<ul>\
					<li><a href="#" onclick="moveHouse(true,false);return false;">Move House Up</a></li>\
					<li><a href="#" onclick="moveHouse(true,true);return false;">Move House Down</a></li>\
					<li><a href="#" onclick="moveHouse(false,false);return false;">Move House Left</a></li>\
					<li><a href="#" onclick="moveHouse(false,true);return false;">Move House Right</a></li>\
				</ul>\
			<!--[if lte IE 6]></td></tr></table></a><![endif]-->\
			</li>\
			<li><a href="#">Rotate House<!--[if IE 7]><!--></a><!--<![endif]-->\
			<!--[if lte IE 6]><table><tr><td><![endif]-->\
				<ul>\
					<li><a href="#" onclick="transformHouseByMatrix(0,1,-1,0);return false;">Rotate House 90&deg; Clockwise</a></li>\
					<li><a href="#" onclick="transformHouseByMatrix(0,-1,1,0);return false;">Rotate House 90&deg; Anti-Clockwise</a></li>\
					<li><a href="#" onclick="transformHouseByMatrix(-1,0,0,-1);return false;">Rotate House 180&deg;</a></li>\
				</ul>\
			<!--[if lte IE 6]></td></tr></table></a><![endif]-->\
			</li>\
			<li><a href="#">Flip House<!--[if IE 7]><!--></a><!--<![endif]-->\
			<!--[if lte IE 6]><table><tr><td><![endif]-->\
				<ul>\
					<li><a href="#" onclick="transformHouseByMatrix(1,0,0,-1);return false;">Flip House Vertically</a></li>\
					<li><a href="#" onclick="transformHouseByMatrix(-1,0,0,1);return false;">Flip House Horizontally</a></li>\
					<li><a href="#" onclick="transformHouseByMatrix(0,-1,-1,0);return false;">Flip House Diagonally 1</a></li>\
					<li><a href="#" onclick="transformHouseByMatrix(0,1,1,0);return false;">Flip House Diagonally 2</a></li>\
				</ul>\
			<!--[if lte IE 6]></td></tr></table></a><![endif]-->\
			</li>\
			</ul>\
		<!--[if lte IE 6]></td></tr></table></a><![endif]-->\
		</li>\
	</ul>';
	
	return content;
}

/*-THE HOUSE ITSELF */

/*Creates all the DIVs and base HTML for the house
Returns the constructed HTML */
function createHouse(){
	var roomsStr = "";
	var floor = "";
	var inner;
	
	for(var i = 0; i < 3; i++){
		floor = determineIDOfFloor(i);
		roomsStr += '<div id="' + floor + '" class="overviewFloor">';
		
		for(var j = 0; j < 9; j++){
			roomsStr += '<div id="roomColumn' + i + j + '" class="roomColumn">';
			
			for(var k = 0; k < 9; k++){
				houseArray[i][j][k].doorImageURL = determineRoomOverviewImage(i, j, k);
				//don't link things on the top floor since you can't build in the air
				inner = i === 2 ? ' ' : '<span><a href="#roomBuildingFrame"\
					onclick="setSelectedRoom('+i+','+j+','+k+');return false;"\
						class="overviewLink faceboxLink">' +
							houseArray[i][j][k].labelText + '</a></span>';
				//console.log(houseArray[i][j][k].doorImageURL);
				roomsStr += '<div id="room' + i + j + k + '" class="room ' + houseArray[i][j][k].doorImageURL +
					'" title="' + determineRoomOverviewTooltipText(i, j, k) +'">' + inner + '</div>';
			}
			
			roomsStr += '</div>';
		}
		roomsStr += '</div>';
	}
	
	return roomsStr;
}

/*-THINGS THAT ARE EVERYWHERE */

/*Generates a set of borders to fit around content found in #innerID.
#outerID allows you to provide a background colour for this section.
Returns a string containing a series of divs */
function generateBorders(outerID, innerID){
	var borderClasses = [	'pohBorderTop',
		'pohBorderBottom',
		'pohBorderLeft',
		'pohBorderRight',
		'pohBorderCornerTR',
		'pohBorderCornerTL',
		'pohBorderCornerBR',
		'pohBorderCornerBL'];
	var borders = '';
	
	//create the outer div
	borders += '<div id="' + outerID + '">';
	//create the borders
	for(var i = 0; i < borderClasses.length; i++){
		borders += '<div class="' + borderClasses[i] + '">';
	}
	//create the inner div
	borders += '<div id="' + innerID + '">';
	//close divs
	for(var i = 0; i < (borderClasses.length) + 2; i++){
		borders += '</div>';
	}
	
	return borders;
}

/*-RIGHT PANEL */

/*Creates the different divs for various elements in the #infoPanel.
Returns the HTML for the divs */
function createInfoPanelCoreDivs(){
	var divs = '<div id="infoPanelFloorAndCharStats"></div>\
	<div id="infoPanelHouseStats"></div>\
	<div id="infoPanelRoomStats"></div>';
	
	return divs;
}

/*-tHE BIT AT THE TOP OF THE #infoPanel WITH FLOOR SELECTION, USERNAME, ETC */

/*Creates the drop-down menu to select which floor you're viewing.
The 'Ground Floor' is the default selection.
Returns the HTML for the dropdown menu */
function createInfoPanelFloorSelectDropDown(){
	var menu = '<select name="floorSelect" id="floorSelectDropDown" onchange="return false;">\
	<option value="dungeon">Dungeon</option>\
	<option value="groundFloor" selected="selected">Ground Floor</option>\
	<option value="upperFloor">Upper Floor</option>\
	</select>';
	
	return menu;
}

/*Creates the content for #infoPanelFloorAndCharStats.
Returns the HTML */
function createInfoPanelFloorAndCharStatsContent(){
	var content = '<table id="floorAndCharStatsTable">\
	<tr title="Choose which floor of the house to view" id="facsTableR1">\
		<td><span class="floorAndCharStatsHeading">View Floor: </span></td>\
		<td>' + createInfoPanelFloorSelectDropDown() + '</td>\
	</tr>\
	<tr title="Enter your RuneScape username" id="facsTableR2">\
		<td><span class="floorAndCharStatsHeading">Username: </span></td>\
		<td><input type="text" name="usernameInput" class="usernameInput" id="usernameInput"\
			maxlength="12" /></td>\
	</tr>\
	<tr>\
		<td><input type="submit" value="Stats" class="openCharStatsButton"\
			id="openCharStatsButton" title="View and edit your stats and completed quests" /></td>\
		<td><input type="submit" value="Fetch My Exp!" class="fetchCharStatsButton"\
			id="fetchCharStatsButton" title="Fetch your stats from the RuneScape Hiscores" /></td>\
	</tr>\
	</table>';
	
	return content;
}

/*-HOUSE AND ROOM STATS TABLES */

/*Generates a table for the House or Room Stats.
Returns the HTML for the table*/
function generateInfoPanelStatsTable(tableContent, id, heading){
	var content = '<table id="' + id + '">\
	<tr><th colspan="2" class="statsTableHeading">\
		<span class="statsTypeHeading">' + heading + '</span> Stats\
	</th></tr>';
	
	//loop through each of the pieces of info that wants displaying
	for(var i = 0; i < tableContent.length; i++){
		//title="' + tableContent[i][3] + '" id="' + id + i + '"
		content += '<tr>\
		<td class="statsHeading">' + tableContent[i][0] + '</td>\
		<td class="statsInfo"><span class="statsNumericalData">' + tableContent[i][1] + '</span>'
			+ '<span class="statsUnit">' + tableContent[i][2] + '</span></td>\
		</tr>';
	}
	
	content += '</table>';

	//console.log(content);
	
	return content;
}

/*Creates the buttons to go in #infoPanelRoomButtons
Returns the HTML for the buttons and their containing table */
function createRoomStatsButtons(){
	/*var buttons = '<div id="infoPanelRoomButtons">\
		<div id="demolishRoomDiv" class="overviewRoomButton" onclick="demolishRoom0();return false;">\
			Demolish <span id="demolishButtonRoomName">Room</span></div>\
		<div id="roomRotateButtons">\
			<div id="rotateCwDiv" class="overviewRoomButton" onclick="rotateRoom(true);return false;">\
				Rotate Clockwise</div>\
			<div id="rotateCcwDiv" class="overviewRoomButton" onclick="rotateRoom(false);return false;">\
				Rotate Anti-Clockwise</div>\
		</div>\
	</div>';*/
	
	var buttons = '<div id="infoPanelRoomButtons">\
		<input type="submit" value="Demolish Room" class="overviewRoomButton"\
			id="demolishRoomButton" title="Demolish the Room" onclick="demolishRoom0();return false;" />\
		<div id="roomRotateButtons">\
			<input type="submit" value="Rotate Clockwise" class="overviewRoomButton"\
				id="rotateCwButton" title="Rotate the Room Clockwise" onclick="rotateRoom(true);return false;" />\
			<input type="submit" value="Rotate Anti-Clockwise" class="overviewRoomButton"\
				id="rotateCcwButton" title="Rotate the Room Anti-Clockwise" onclick="rotateRoom(false);return false;" />\
		</div>\
	</div>';
	
	return buttons;
}

/*-ROOM BUILDING TABLE */

/*Create the table that allows you to build furniture and whatnot
Returns the HTML for the table */
function createRoomBuildingTable(){
	var table = "";
	
	//create the headings
	var headings = [ "Icon", "Type", "Level", "Cost" ];
	table += '<table id="roomBuildingTable" class="tablesorter"><thead><tr>';
	
	for(var i = 0; i < headings.length; i++){
		table += '<th>' + headings[i] + '</th>';
	}
	table += '</tr></thead><tbody>';
	//and the rows themselves
	for(var i = 0; i < roomsArray.length; i++){
		if(roomsArray[i][1] === ' '){
			//don't add empty rooms to the list
		}
		else {
		table += '<tr onclick="buildRoom($house.csr[0],$house.csr[1]\
			,$house.csr[2],'+i+');$(document).trigger(\'close.facebox\');return false;">\
			<td class="tablesorterRoomIcon"><img src="buildIcons/' +
				roomsArray[i][6] + '.gif" alt="' + roomsArray[i][6] + '" width="32" height="32" /></td>\
			<td>' + roomsArray[i][1] + '</td>\
			<td>' + roomsArray[i][2] + '</td>\
			<td>' + roomsArray[i][3] + '</td></tr>';
		}
	}
	
	table += '</tbody></table>';
	
	return table;
}