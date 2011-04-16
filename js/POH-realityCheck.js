
var POHRealityCheck = function(){
	
	var rc = this;
	
	this.calculate = function(){
		//console.time('reality check');
		var numberVisible = 0;
		numberVisible += rc.maxRooms();
		numberVisible += rc.dimensionLimit();
		numberVisible += rc.highLevelRooms();
		numberVisible += rc.floatingRooms();
		
		$('#rcConLevelInput').val(poh.$stats.construction);
		
		if(numberVisible === 0){
			$('#rcThereAreProblems').hide();
			$('#rcAllIsFine').show();
		} else {
			$('#rcThereAreProblems').show();
			$('#rcAllIsFine').hide();
		}
		//console.timeEnd('reality check');
	};
	
	this.maxRooms = function(){
		if($house.builtRooms > $house.maxRooms){
			$('#rcBuiltRooms').html($house.builtRooms);
			$('#rcMaxRooms').html($house.maxRooms);
			$('#realityCheckMaxRooms').show();
			return 1;
		} else {
			$('#realityCheckMaxRooms').hide();
			return 0;
		}
	};
	
	this.dimensionLimit = function(){
		var mdr = rc.determineMostDistantRooms();
		
		if( mdr[0] !== -1 && mdr[1] !== -1 && mdr[2] !== -1 && mdr[3] !== -1){
			var width = mdr[3] - mdr[1] + 1;
			var height = mdr[2] - mdr[0] + 1;
			
			if(width > $house.maxDimensions || height > $house.maxDimensions){
				$('#rcDimensionWidth').html(width);
				$('#rcDimensionHeight').html(height);
				$('#rcDimensionAllowed').html($house.maxDimensions + 'x' + $house.maxDimensions);
				$('#realityCheckDimensionLimit').show();
				return 1;
			} else {
				$('#realityCheckDimensionLimit').hide();
				return 0;
			}
		}
		return 0;
	};
	
	this.determineMostDistantRooms = function(){
		var mostDistantRooms = [ -1, -1, -1, -1 ]; //n, e, s, w
		
		for(var i = 0; i < 3; i++){
			for(var j = 0; j < 9; j++){
				for(var k = 0; k < 9; k++){
					if(poh.houseArray[i][j][k].labelText !== ' '){
						//north
						if(mostDistantRooms[0] === -1 || k < mostDistantRooms[0]){
							mostDistantRooms[0] = k;
						}
						//east
						if(mostDistantRooms[1] === -1 || j < mostDistantRooms[1]){
							mostDistantRooms[1] = j;
						}
						//south
						if(mostDistantRooms[2] === -1 || k > mostDistantRooms[2]){
							mostDistantRooms[2] = k;
						}
						//west
						if(mostDistantRooms[3] === -1 || j > mostDistantRooms[3]){
							mostDistantRooms[3] = j;
						}
					}
					
				}
			}
		}
		//console.log(mostDistantRooms);
		return mostDistantRooms;
	};
	
	this.floatingRooms = function(){
		var floaters = rc.determineNumberOfFloatingRooms();

		if(floaters > 0){
			$('#rcNoOfMisplacedDungeons').html(floaters);
			$('#realityCheckFloatingRooms').show();
			return 1;
		} else {
			$('#realityCheckFloatingRooms').hide();
			return 0;
		}
	};
	
	this.determineNumberOfFloatingRooms = function(){
		var floaters = 0;
		for(var i = 0; i < 9; i++){
			for(var j = 0; j < 9; j++){
				if(poh.houseArray[2][i][j].labelText !== ' ' && poh.houseArray[1][i][j].labelText === ' '){
					floaters++;
				}
			}
		}
		return floaters;
	};
	
	this.highLevelRooms = function(){
		var highLevels = rc.determineNumberOfHighLevelRooms();
		
		if(highLevels > 0){
			$('#rcNoOfHighLevelRooms').html(highLevels);
			if(highLevels > 1){
				$('#rcNoOfHighLevelRoomsPlural').show();
				$('#rcNoOfHighLevelRoomsIsAre').html('are');
			} else {
				$('#rcNoOfHighLevelRoomsPlural').hide();
				$('#rcNoOfHighLevelRoomsIsAre').html('is');
			}
			$('#realityCheckHighLevelRooms').show();
			return 1;
		} else {
			$('#realityCheckHighLevelRooms').hide();
			return 0;
		}
	};
	
	this.determineNumberOfHighLevelRooms = function(){
		var highLevels = 0;
		for(var i = 0; i < 3; i++){
			for(var j = 0; j < 9; j++){
				for(var k = 0; k < 9; k++){
					if(poh.houseArray[i][j][k].level > poh.$stats.construction){
						highLevels++;
					}					
				}
			}
		}
		return highLevels;
	};


};