
//in_typeID, in_name, in_materials, in_flatpack, in_level, in_xpmod
var furnitureArray = [
	[0, 'Oak Table', [[0, 3]], {"flatpackable":true,"IDBID":2980,"cost":2}, 32, 0],
	[1, 'Wood Table', [[1, 3], [2, 3]], {"flatpackable":true,"IDBID":2112,"cost":2}, 12, 0]
];

var $pohFurniture = {
	$0 : {
		name : 'Demolish Furniture',
		materials : [ [] ],
		flatpack : {
			flatpackable : false
		},
		level : 0,
		xpmod : 0,
		image : ''
	},
	$1 : {
		name : 'Oak Table',
		materials : [ [0, 3] ],
		flatpack : {
			flatpackable : true,
			IDBID : 2980,
			cost : 2
		},
		level : 90,
		xpmod : 0,
		image : 'kitchenTable/oak_table'
	},
	$2 : {
		name : 'Wood Table',
		materials : [ [1, 3], [2, 3] ],
		flatpack : {
			flatpackable : true,
			IDBID : 2112,
			cost : 2
		},
		level : 1,
		xpmod : 0,
		image : 'kitchenTable/wood_table'
	}
};