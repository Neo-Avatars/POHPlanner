
var POHPictureMode = function(){

	var pm = this;
	
	/*Switches the view from Picture Mode to Overview Mode */
	this.switchToOverviewMode = function(){
		if($('#overviewBorder').is(':hidden')){
			$house.overviewMode = true;
			$('#pictureMode').hide();
			$('#overviewBorder').show();
			pm.toggleViewSwitchButtons();
			$('#infoPanelRoomButtons').show();
			
			//$('#pmImageMap').unbind();
			//deselectAllRooms();
			//hide the buttons that allow you to demolish and rotate the room
			//$('#infoPanelRoomButtons').hide();
		}
	};
	
	/*Switches the view from Overview Mode to Picture Mode */
	this.switchToPictureMode = function(i, j, k){
		if($('#pictureMode').is(':hidden') && !$house.overviewOnly){
			//console.time('switch to picture mode');
			$house.overviewMode = false;
			pm.toggleViewSwitchButtons();
			//$('#pmImageMap *').remove();
			//need to replace the html every time to appease IE and allow you to view more than one room before it becomes itself and refuses to do a thing
			if( ( $('#pmImage img').length < 1 ) || !Modernizr.canvas ){
				$('#pmImage').html('<img src="roomPics/' + poh.houseArray[i][j][k].imgURL + '.jpg" width="512" height="334" id="pmImageImage" usemap="#pmImageMap" />\
				<map id="pmImageMap" name="pmImageMap"></map>'); //roomPics/empty.jpg //roomPics/blank.png
			} else { //need to do it this way to make it work in Chrome (it has the canvas element, so will only do the above the first time)
				$('#pmImage div, #pmImage img').remove();
				//$('#pmImageImage').attr('src', 'roomPics/' + poh.houseArray[i][j][k].imgURL);
				$( document.createElement( "img" ) )
				.attr({
					src: 'roomPics/' + poh.houseArray[i][j][k].imgURL + '.jpg',
					width: 512,
					height: 334,
					id: 'pmImageImage',
					usemap: '#pmImageMap'
				})
				.appendTo( $('#pmImage') );
			}
		
			//$('#pmImageImage').attr('src', 'roomPics/' + poh.houseArray[i][j][k].imgURL + '.jpg');
			
			pm.generatePictureModeImageMap(i, j, k);
			$('#overviewBorder').hide();
			$('#pictureMode').show();
			//hide the buttons that allow you to demolish and rotate the room to avoid demolishing the room you're currently viewing
			$('#infoPanelRoomButtons').hide();
			//console.timeEnd('switch to picture mode');
		}
	};
	
	/*Returns the RGB value that a hotspot should be based on the furniture that is built there */
	this.getFillColorFromLevel = function( levelParent ){
		if(typeof levelParent === 'undefined'){
			return "FFFFFF";
		} else {
			var green = 255 - (2 * levelParent.level);
			return 'FF' + green.toString(16) + '00'
		}
	};
	
	/*Generates, inserts and displays the Picture Mode imagemap for the specified room */
	this.generatePictureModeImageMap = function(i, j, k){
		var coords = poh.houseArray[i][j][k].coords;
		//var map = '';
		$('#pmImageMap area').remove();
		var vowelRegex = /[aeiou]/i;
		for(var l = 0; l < coords.length; l++){
			for(var m = 0; m < coords[l].length; m++){
				
				//work out what is currently built here so it can be displayed in the tooltip
				var furniName = $pohFurniture['$' + $pohHotspots['$' + l].content[ poh.houseArray[i][j][k].furniture[l] ] ];
				if(typeof furniName !== 'undefined'){
					if( vowelRegex.test( furniName.name.charAt(0) ) ){
						var aAn = 'An ';
					} else {
						var aAn = 'A ';
					}
					var builtFurniType = ' - ' + aAn + furniName.name + ' is built here';
				} else {
					var builtFurniType = ' - nothing is built here';
				}
				//create the area and all the data associated with it
				$( document.createElement( "area" ) )
				.attr({
					shape: 'poly',
					coords: coords[l][m],
					href: '#',
					//title: $pohHotspots['$' + poh.houseArray[i][j][k].hotspots[l] ].name + builtFurniType,
					id: 'pmArea' + i + j + k + l + m,
					rel: '#furniBuildingFrame'//,
					//class: "{fillColor:'" + pm.getFillColorFromLevel( $pohFurniture['$' + $pohHotspots['$' + l].content[ poh.houseArray[i][j][k].furniture[l] ] ] ) + "'}"
					//,onclick: 'poh.pm.displayFurnitureBuildingFrame($pohHotspots.$'+poh.houseArray[i][j][k].hotspots[l]+'.content);return false;'
				})
				//the whole Planner will stop working in IE if this is grouped with all the other attributes. Guthix knows why.
				.attr( 'class', "{fillColor:'" + pm.getFillColorFromLevel( $pohFurniture['$' + $pohHotspots['$' + l].content[ poh.houseArray[i][j][k].furniture[l] ] ] ) + "'}" )
				.data({
					fbfContent: $pohHotspots['$' + poh.houseArray[i][j][k].hotspots[l] ].content,
					fbfHotspot: l,
					tooltipText: $pohHotspots['$' + poh.houseArray[i][j][k].hotspots[l] ].name + builtFurniType
				})
				//.click(function(){
				//	console.log($(this));
				//	poh.pm.displayFurnitureBuildingFrame($(this).data());
				//})
				.appendTo( $('#pmImageMap') )
				.overlay({
					effect : 'furniBuildFrame'
				});
			}
		}
		//console.log(coords, map);
		//$('#pmImageMap').html(map);
		//if( $('#pmImageMap area').length > 0){
			$('#pmImageImage').maphilight();
			$('#pmImageMap area').aToolTip();
		//}
		//console.log('done areas');
	};
	
	/*Returns true or false depending on whether there's already some furniture built in the specified hotspot in the CSR */
	this.hasBuiltFurniture = function( hotspotID ){
		var hotspotContent = poh.houseArray[$house.csr[0]][$house.csr[1]][$house.csr[2]].furniture[hotspotID];
		//console.log('hsc', hotspotContent);
		if( (typeof hotspotContent === 'undefined') || (hotspotContent === -1) ){
			return false;
		} else {
			return true;
		}
	};
	
	/*Adds a container to the FBF that allows any currently built furniture to be demolished */
	this.addFurniDemolitionOption = function( hotspotID, $furniBuildFrame ){
		var hotspotContent = poh.houseArray[$house.csr[0]][$house.csr[1]][$house.csr[2]].furniture[hotspotID];
		var $furniture = $pohFurniture['$' + $pohHotspots['$' + hotspotID].content[ hotspotContent ] ];
		//console.log('hsc', hotspotContent, $furniture);
		var demolishBox = '<div class="furniBuildContainer" id="furniDemolishContainer">\
		<table class="furniBuildTable" id="furniDemolishTable"><tr>\
		<td class="furniBuildImgCell" id="furniBuildImgCellDemolish">\
		<img src="furniIcons/' + $furniture.image +
				'.gif" width="30" height="34" class="furniBuildImg" align="left" /></td>\
		<td class="furniBuildDemolishTextCell">Demolish ' + $furniture.name + '</td>\
		<td class="furniBuildImgCell" id="furniBuildImgCellDemolish">\
		<img src="furniIcons/' + $furniture.image +
				'.gif" width="30" height="34" class="furniBuildImg" align="left" /></td>\
		</tr></table></div>';
		$furniBuildFrame.append(demolishBox);
	};
	
	/*Populates and displays the Furniture Building frame 
	Content is the array of furniture IDs found in $pohHotspots.$#.content */
	/*
		Image
		Name
		Level
		Cost
		Exp
		Flatpackable
		Materials - Name + Number
	*/
	this.displayFurnitureBuildingFrame = function( data ){
		var content = data.fbfContent;
		var hotspotID = data.fbfHotspot;
		//console.log(data);

		var $furniBuildFrame = $('#furniBuildingFrame .faceboxInner');
		$furniBuildFrame.html('');
		//console.log($furniBuildFrame);
		var hasBuiltFurni = pm.hasBuiltFurniture( hotspotID );
		if(hasBuiltFurni){
			pm.addFurniDemolitionOption( hotspotID, $furniBuildFrame );
			var fbfRightOffset = 1;
		} else {
			var fbfRightOffset = 0;
		}
		//var $currentFurni = 
		//loop through the bits of furniture that can be built in a spot and create a way to build each of them
		for(var l = 0; l < content.length; l++){
			$furniture = $pohFurniture[ '$' + content[l] ]; //name, level, flatpack, materials, xpmod
			$furniture.cost = pm.calculateFurniCost($furniture);
			$furniture.xp = pm.calculateFurniXp($furniture);
			
			var furniBox = '<div class="furniBuildContainer';
			
			if( l % 2 !== fbfRightOffset ){
				furniBox += ' furniBuildContainerRight';
				//console.log(l);
			}
			
			furniBox += '" id="furniBuildFurni' + l + '">\
			<table class="furniBuildTable';
			
			if( l % 2 !== fbfRightOffset){
				furniBox += ' furniBuildTableRight';
			}
			
			furniBox += '" id="furniBuildTable' + l + '"><tr>\
			<td class="furniBuildImgCell" id="furniBuildImgCell' + l + '">\
			<img src="furniIcons/' + $furniture.image +
				'.gif" width="30" height="34" class="furniBuildImg" />';
			
			//if( poh.$stats.construction < $furniture.level){
				//furniBox += '<img src="furniIcons/unavailable.gif" width="16" height="16" class="furniBuildImgUnavailable" />';
			//}
			
			furniBox += '</td>\
			<td class="furniBuildInfoCell">\
			<span class="furniBuildName">' + $furniture.name + '</span><br />\
			<span class="furniBuildLevel">Level: ' + $furniture.level + '</span><br />\
			<span class="furniBuildCost">Cost: ' + $furniture.cost + 'gp</span><br />\
			<span class="furniBuildXp">Experience: ' + $furniture.xp + '</span><br />\
			<span class="furniBuildFlatpack">Flatpackable: ';
			
			if($house.fullAccessMode){
				if($furniture.flatpack.flatpackable){
				furniBox += '<a href="index.php?id=99&viewitem=' + $furniture.flatpack.IDBID + '"\
					alt="Yes"\
					rel="index.php?id=77&action=getquickinfo&item_id='
						+ $furniture.flatpack.IDBID + '" class="item" target="_blank">';
				}
			}
			
			if($furniture.flatpack.flatpackable){
				furniBox += 'Yes';
			} else {
				furniBox += 'No';
			}
			
			if($house.fullAccessMode){
				furniBox += '</a>';
			}
			
			furniBox += '</span></td>\
			<td class="furniBuildMaterialsCell">\
			<span class="furniBuildMaterials">Materials</span><br />';
			
			//list the materials required to make it
			for(var m = 0; m < $furniture.materials.length; m++){
				furniBox += '<span class="furniBuildMatName">';
				
				if($house.fullAccessMode){
					furniBox += '<a href="index.php?id=99&viewitem=' + $pohMaterials['$'+$furniture.materials[m][0]].IDBID + '"\
						alt="' + $pohMaterials['$'+$furniture.materials[m][0]].name +'"\
						rel="index.php?id=77&action=getquickinfo&item_id='
							+ $pohMaterials['$'+$furniture.materials[m][0]].IDBID + '" class="item" target="_blank">';
				}
				
				furniBox += $pohMaterials['$'+$furniture.materials[m][0]].name
				
				if($house.fullAccessMode){
					furniBox += '</a>';
				}
				
				furniBox += '</span>: \
				<span class="furniBuildMatNumber">' + $furniture.materials[m][1] + '</span><br />';
			}
			
			furniBox += '</td></div></tr></table>\
			</div>';
			//console.log('fb:', furniBox);
			$furniBuildFrame.append(furniBox);
		}
		
		if($house.fullAccessMode){
			initQTips( $furniBuildFrame );
		}
		
		$('#furniBuildingFrame .furniBuildContainer:not(#furniDemolishContainer)').each(function(index){
			//if(hasBuiltFurni){							//the 'not' stops a double-bind on the 'demolish' container
				//index--;
			//}
			$(this).click(function(){
				pm.buildFurniture($house.csr[0], $house.csr[1], $house.csr[2], hotspotID, index);
				$(this).parent().parent().fadeOut('fast');
			});
		});
		
		$('#furniDemolishContainer').click(function(){
			pm.demolishFurniture($house.csr[0], $house.csr[1], $house.csr[2], hotspotID);
			$(this).parent().parent().fadeOut('fast');
		});
	};
	
	/*Builds a piece of furniture */
	this.buildFurniture = function(i, j, k, hotspotID, furniIndex){
		//console.log('building furni', hotspotID, furniIndex);
		//remove the values of the previous pieces that were built
		$oldFurniture = $pohFurniture['$' + $pohHotspots['$' + hotspotID].content[ poh.houseArray[i][j][k].furniture[hotspotID] ] ];
		if(typeof $oldFurniture !== 'undefined'){
			$house.totalFurniCost -= $oldFurniture.cost;
			$house.totalFurniXp -= $oldFurniture.xp;
		}
		
		//build the new one
		poh.houseArray[i][j][k].furniture[hotspotID] = furniIndex;
		
		if($house.overviewMode === false){
			pm.generatePictureModeImageMap(i, j, k);
		}
		//update with the new values
		if(furniIndex !== -1){
			$furniture = $pohFurniture['$' + $pohHotspots['$' + hotspotID].content[ poh.houseArray[i][j][k].furniture[hotspotID] ] ];
			//console.log($furniture);
			$house.totalFurniCost += $furniture.cost;
			$house.totalFurniXp += $furniture.xp;
		}
		
		poh.rp.updateFurnitureCost();
		poh.rp.updateFurnitureXp();
		poh.rp.updateTotalCost();
		poh.rp.updateRoomFurniCost( i, j, k );
		poh.rp.updateRoomFurniXp( i, j, k );
		
		if($house.initialised && !$house.loading){
			poh.sc.calculateSharingCode(poh.houseArray);
		}
		//pm.calculateRoomFurniCost( i, j, k );
		//pm.calculateRoomFurniXp( i, j, k );
	};
	
	/*Demolishes a piece of furniture */
	this.demolishFurniture = function(i, j, k, hotspotID){
		pm.buildFurniture(i, j, k, hotspotID, -1);
	};
	
	/*Calculates the cost of all the furniture built in a single room */
	this.calculateRoomFurniCost = function( i, j, k ){
		var $furniture;
		var furnitureCost = 0;
		for(var m = 0; m < poh.houseArray[i][j][k].furniture.length; m++){
			$furniture = $pohFurniture['$' + $pohHotspots['$' + m].content[ poh.houseArray[i][j][k].furniture[m] ] ];
			
			if(typeof $furniture !== 'undefined'){
				furnitureCost += pm.calculateFurniCost( $furniture );
			}
		}
		//console.log('room cost', furnitureCost);
		return furnitureCost;
	};
	
	/*Calculates the xp provided by all the furniture built in a single room */
	this.calculateRoomFurniXp = function( i, j, k ){
		var $furniture;
		var furnitureXp = 0;
		for(var m = 0; m < poh.houseArray[i][j][k].furniture.length; m++){
			$furniture = $pohFurniture['$' + $pohHotspots['$' + m].content[ poh.houseArray[i][j][k].furniture[m] ] ];
			
			if(typeof $furniture !== 'undefined'){
				furnitureXp += pm.calculateFurniXp( $furniture );
			}
		}
		//console.log('room xp', furnitureXp);
		return furnitureXp;
	};
	
	/*Calculates the cost of a piece of furniture based on the required materials */
	this.calculateFurniCost = function( $furniture ){
		materials = $furniture.materials;
		var cost = 0;
		for(var l = 0; l < materials.length; l++){
			cost += $pohMaterials['$' + materials[l][0]].GEcost * materials[l][1];
		}
		//console.log(cost);
		return cost;
	};
	
	/*Calculates the xp provided by a piece of furniture based on the required materials */
	this.calculateFurniXp = function( $furniture ){
		materials = $furniture.materials;
		var xp = 0;
		for(var l = 0; l < materials.length; l++){
			xp += $pohMaterials['$' + materials[l][0]].xp * materials[l][1];
		}
		xp += $furniture.xpmod;
		//console.log(xp);
		return xp;
	};
	
	/*Toggles the visibility of both the button that takes you back to Overview Mode and the dropdown that allows you to change the visible floor 
	The one that is hidden will show and the one that is visible will be hidden */
	this.toggleViewSwitchButtons = function(){
		$('#facsTableR0').toggleClass('hidden');
		$('#facsTableR1').toggleClass('hidden');
	};

};