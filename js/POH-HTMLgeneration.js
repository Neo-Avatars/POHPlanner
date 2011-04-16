/*-THE BAR ACROSS THE TOP */

/*Creates the DIVs for the sections within the Top Bar
Returns the constructed HTML */
function createTopBarDivs(){
	var divs = '<div id="topBarMenu" class="POHmenu"></div>\
	<div id="topBarSharingDiv"></div>';
	
	return divs;
}

/*Creates the DIVs for the sections within the Top Bar as well as the content that goes within them
Returns the constructed HTML */
function createTopBar(){
	var content = '<div id="topBarMenu" class="POHmenu">' + createTopBarMenu() + '</div>\
	<div id="topBarSharingDiv">' + createTopBarSharingContent() + '</div>';
	
	return content;
}

/*Creates the menu for the #topBarMenu
Returns the constructed HTML */
function createTopBarMenu(){
	var content = '<ul id="menuList">\
		<li><a href="#">File<!--[if IE 7]><!--></a><!--<![endif]-->\
		<!--[if lte IE 6]><table><tr><td><![endif]-->\
			<ul id="fileList">\
			<li><a href="#" onclick="loadDefaultHouse();return false;">Reset House</a></li>\
			</ul>\
		<!--[if lte IE 6]></td></tr></table></a><![endif]-->\
		</li>\
		<li><a href="#">Transform<!--[if IE 7]><!--></a><!--<![endif]-->\
		<!--[if lte IE 6]><table><tr><td><![endif]-->\
			<ul id="transformList">\
			<li><a href="#">Move House<!--[if IE 7]><!--></a><!--<![endif]-->\
			<!--[if lte IE 6]><table><tr><td><![endif]-->\
				<ul id="moveHouseList">\
					<li><a href="#" rel="#demolishOnMove" id="moveHouseNorth">Move House Up</a></li>\
					<li><a href="#" rel="#demolishOnMove" id="moveHouseSouth">Move House Down</a></li>\
					<li><a href="#" rel="#demolishOnMove" id="moveHouseWest">Move House Left</a></li>\
					<li><a href="#" rel="#demolishOnMove" id="moveHouseEast">Move House Right</a></li>\
				</ul>\
			<!--[if lte IE 6]></td></tr></table></a><![endif]-->\
			</li>\
			<li><a href="#">Rotate House<!--[if IE 7]><!--></a><!--<![endif]-->\
			<!--[if lte IE 6]><table><tr><td><![endif]-->\
				<ul id="rotateHouseList">\
					<li><a href="#" onclick="transformHouseByMatrix(0,1,-1,0);return false;">Rotate House 90&deg; Clockwise</a></li>\
					<li><a href="#" onclick="transformHouseByMatrix(0,-1,1,0);return false;">Rotate House 90&deg; Anti-Clockwise</a></li>\
					<li><a href="#" onclick="transformHouseByMatrix(-1,0,0,-1);return false;">Rotate House 180&deg;</a></li>\
				</ul>\
			<!--[if lte IE 6]></td></tr></table></a><![endif]-->\
			</li>\
			<li><a href="#">Flip House<!--[if IE 7]><!--></a><!--<![endif]-->\
			<!--[if lte IE 6]><table><tr><td><![endif]-->\
				<ul id="flipHouseList">\
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
	
	/*<ul id="moveHouseList">\
					<li><a href="#" onclick="moveHouse(true,false);return false;">Move House Up</a></li>\
					<li><a href="#" onclick="moveHouse(true,true);return false;">Move House Down</a></li>\
					<li><a href="#" onclick="moveHouse(false,false);return false;">Move House Left</a></li>\
					<li><a href="#" onclick="moveHouse(false,true);return false;">Move House Right</a></li>\
				</ul>\*/
	
	return content;
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
						class="overviewLink faceboxLink" rel="#roomBuildingFrame">' +
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
		<td><input type="submit" value="Stats" class="openCharStatsButton" rel="#userStatsFrame"\
			id="openCharStatsButton" title="View and edit your stats and completed quests" /></td>\
		<td><input type="submit" value="Fetch My Exp!" class="fetchCharStatsButton"\
			id="fetchCharStatsButton" title="Fetch your stats from the RuneScape Hiscores" /></td>\
	</tr>\
	</table>';
	
	return content;
}

/*-THE STATS AND QUEST STATUS DIV */

/*Returns the HTML for the Stats and Quest Status Div */
function createStatsAndQuestStatusContent(){
	var content = createUserStatsTable();
	
	return content;
}

/*Returns the HTML for the user stats table */
function createUserStatsTable(){
	var content = '<table id="userStatsTable" class="tablesorter"><thead><tr>';
	
	//create the headings
	var headings = [ "Icon", "Skill", "Level" ];
	for(var i = 0; i < headings.length; i++){
		content += '<th>' + headings[i] + '</th>';
	}
	content += '</tr></thead><tbody>';
	
	//and the rows themselves	
	//http://stackoverflow.com/questions/722668/traverse-all-the-nodes-of-a-json-object-tree-with-javascript
	$.each($stats, function(key,val){
		//value must be the first attribute of the input so that it sorts by value rather than skill name
		content += '<tr id="userStatsRow' + key + '" class="userStatsRow">\
			<td class="userStatsIconCell"><img src="skillIcons/' + key + '.gif" alt="' + key + '" width="18" height="18" /></td>\
			<td class="userStatsNameCell">' + capitaliseString(key) + '</td>\
			<td class="userStatsLevelCell">\
				<input value="' + val + '" class="userStatsInput" type="text" name="' + key + '"maxlength="2" />\
			</td>\
		</tr>';
	});
	
	content += '</tbody></table>';
	
	return content;
}

/*-HOUSE AND ROOM STATS TABLES */

/*Generates a table for the House or Room Stats.
Returns the HTML for the table*/
function generateInfoPanelStatsTable(tableContent, id, heading){
	var content = '<div><table id="' + id + '">\
	<tr><th colspan="2" class="statsTableHeading">' + heading + ' Stats\
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
	
	content += '</table></div>';

	//console.log(content);
	
	return content;
}

/*Creates the buttons to go in #infoPanelRoomButtons
Returns the HTML for the buttons and their containing table */
function createRoomStatsButtons(){
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
	var table = '';
	
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
			,$house.csr[2],'+i+');return false;">\
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

/*-CONFIRMATION DIALOGUES AND ERROR MESSAGES */

/*Returns the HTML for the checkbox and label that will allow you to stop the dialogue appearing again */
function createDialogueStoppingInputs(id){
	var content = '<p>\
	<input type="checkbox" id="' + id + 'Check" class="modalNoMoreBox" />\
	<label for="' + id + 'Check" id="' + id + 'Label" class="modalNoMoreLabel">\
		Remember choice and don\'t ask me this again</label>\
	</p>';
	
	return content;
}

/*Returns the HTML to append to the buttom of any 'facebox' so that there's something obvious to click on */
function createFaceboxCloseButton(){
	var content = '<div class="faceboxClose"><div class="close"></div></div>';
	
	return content;
}

/*-THE SETTINGS FOR THE ABOVE */

/*Returns the HTML for the House Settings div */
function createHouseSettingsContent(){
	var content = '<table id="houseSettingsTable"><thead><tr><th colspan="2">House Settings</th></tr></thead><tbody>'
	
	$.each($house.noConfSettings, function(key,val){
		content += '<tr id="houseSettingsRow'+key+'">\
			<td id="houseSettingsDescription'+key+'">'+val.description+'</td>\
			<td id="houseSettingsValue'+key+'">\
				<input type="checkbox" id="houseSettingsCheckbox'+key+'" />\
			</td>\
		</tr>';
	});
	
	content += '</tbody></table>';
	
	return content;
}