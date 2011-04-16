package pohplanner;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Furniture {

	private String type;
	private String furniURL;
	private Materials[] materials;
	private int[] materialNumbers;
	private JLabel imageLabel;
	private int level;
	private String ID;
	
	public Furniture ( Furniture furni ) {
		this.type = furni.type;
		this.furniURL = furni.furniURL;
		this.materials = furni.materials;
		this.materialNumbers = furni.materialNumbers;
		this.level = furni.level;
		this.ID = furni.ID;
		this.imageLabel = furni.imageLabel;
		
	}
	
	public Furniture ( String type ) {
		
		if(type.equals("None")){
			this.type = type;
			this.furniURL = "none/empty";
			this.materials = new Materials[0];
			this.materialNumbers = new int[0];
			this.level = 1;
			this.ID = "00";
		}
	}
	
	public Furniture ( String type, String furniURL, Materials[] materials,
			int[] materialNumbers, int level, String ID ) {
		this.type = type;
		this.furniURL = furniURL;
		this.materials = materials;
		this.materialNumbers = materialNumbers;
		this.level = level;
		this.ID = ID;
		
		createImageLabel();
		
		//the numbers aren't in order so that things with similar
		//types are correctly identified
		/*if(type.contains("None")||type.contains("00")){
			initNone();
		} else if(type.contains("Exit Portal")||type.contains("01")){
			initExitPortal();
		} else if(type.contains("Decorative Rock")||type.contains("02")){
			initDecorativeRock();
		} else if(type.contains("Pond")||type.contains("03")){
			initPond();
		} else if(type.contains("Imp Statue")||type.contains("04")){
			initImpStatue();
		} else if(type.contains("Dungeon Entrance")||type.contains("05")){
			initDungeonEntrance();
		} else if(type.contains("Nice Tree")||type.contains("07")){
			initNiceTree();
		} else if(type.contains("Oak Tree")||type.contains("08")){
			initOakTree();
		} else if(type.contains("Willow Tree")||type.contains("09")){
			initWillowTree();
		} else if(type.contains("Maple Tree")||type.contains("10")){
			initMapleTree();
		} else if(type.contains("Yew Tree")||type.contains("11")){
			initYewTree();
		} else if(type.contains("Magic Tree")||type.contains("12")){
			initMagicTree();
		} else if(type.contains("Tree")||type.contains("06")){
			initTree();
		} else if(type.contains("Dock Leaf")||type.contains("16")){
			initDockLeaf();
		} else if(type.contains("Thistle")||type.contains("17")){
			initThistle();
		} else if(type.contains("Reeds")||type.contains("18")){
			initReeds();
		} else if(type.contains("Small Fern")||type.contains("14")){
			initSmallFern();
		} else if(type.contains("Fern ")||type.contains("19")){
			initFern2();
		} else if(type.contains("Fern")||type.contains("15")){
			initFern();
		} else if(type.contains("Bush")||type.contains("20")){
			initBush();
		} else if(type.contains("Tall Plant")||type.contains("21")){
			initTallPlant();
		} else if(type.contains("Short Plant")||type.contains("22")){
			initShortPlant();
		} else if(type.contains("Large Leaf Bush")||type.contains("23")){
			initLargeLeafBush();
		} else if(type.contains("Huge Plant")||type.contains("24")){
			initHugePlant();
		} else if(type.contains("Plant")||type.contains("13")){
			initPlant();
		} else if(type.contains("Crude Wooden Chair")||type.contains("25")){
			initCrudeWoodenChair();
		} else if(type.contains("Wooden Chair")||type.contains("26")){
			initWoodenChair();
		} else if(type.contains("Rocking Chair")||type.contains("27")){
			initRockingChair();
		} else if(type.contains("Oak Chair")||type.contains("28")){
			initOakChair();
		} else if(type.contains("Oak Armchair")||type.contains("29")){
			initOakArmchair();
		} else if(type.contains("Teak Armchair")||type.contains("30")){
			initTeakArmchair();
		} else if(type.contains("Mahogany Armchair")||type.contains("31")){
			initMahoganyArmchair();
		} else if(type.contains("Brown Rug")||type.contains("32")){
			initBrownRug();
		} else if(type.contains("Opulent Rug")||type.contains("34")){
			initOpulentRug();
		} else if(type.contains("Rug")||type.contains("33")){
			initRug();
		} else if(type.contains("Clay Fireplace")||type.contains("35")){
			initClayFireplace();
		} else if(type.contains("Stone Fireplace")||type.contains("36")){
			initStoneFireplace();
		} else if(type.contains("Marble Fireplace")||type.contains("37")){
			initMarbleFireplace();
		} else if(type.contains("Torn Curtains")||type.contains("38")){
			initTornCurtains();
		} else if(type.contains("Opulent Curtains")||type.contains("40")){
			initOpulentCurtains();
		} else if(type.contains("Curtains")||type.contains("39")){
			initCurtains();
		} else if(type.contains("Wooden Bookcase")||type.contains("41")){
			initWoodenBookcase();
		} else if(type.contains("Oak Bookcase")||type.contains("42")){
			initOakBookcase();
		} else if(type.contains("Mahogany Bookcase")||type.contains("43")){
			initMahoganyBookcase();
		} else if(type.contains("Wood Table")||type.contains("44")){
			initWoodTable();
		} else if(type.contains("Oak Table")||type.contains("45")){
			initOakTable();
		} else if(type.contains("Teak Table")||type.contains("46")){
			initTeakTable();
		} else if(type.contains("Wooden Larder")||type.contains("47")){
			initWoodenLarder();
		} else if(type.contains("Oak Larder")||type.contains("48")){
			initOakLarder();
		} else if(type.contains("Teak Larder")||type.contains("49")){
			initTeakLarder();
		} else if(type.contains("Pump and Drain")||type.contains("50")){
			initPumpAndDrain();
		} else if(type.contains("Pump and Tub")||type.contains("51")){
			initPumpAndTub();
		} else if(type.contains("Sink")||type.contains("52")){
			initSink();
		} else if(type.contains("Wooden Shelves 1")||type.contains("53")){
			initWoodenShelves1();
		} else if(type.contains("Wooden Shelves 2")||type.contains("54")){
			initWoodenShelves2();
		} else if(type.contains("Wooden Shelves 3")||type.contains("55")){
			initWoodenShelves3();
		} else if(type.contains("Oak Shelves 1")||type.contains("56")){
			initOakShelves1();
		} else if(type.contains("Oak Shelves 2")||type.contains("57")){
			initOakShelves2();
		} else if(type.contains("Teak Shelves 1")||type.contains("58")){
			initTeakShelves1();
		} else if(type.contains("Teak Shelves 2")||type.contains("59")){
			initTeakShelves2();
		} else if(type.contains("Firepit with Hook")||type.contains("61")){
			initFirepitWithHook();
		} else if(type.contains("Firepit with Pot")||type.contains("62")){
			initFirepitWithPot();
		} else if(type.contains("Firepit")||type.contains("60")){
			initFirepit();
		} else if(type.contains("Small Oven")||type.contains("63")){
			initSmallOven();
		} else if(type.contains("Large Oven")||type.contains("64")){
			initLargeOven();
		} else if(type.contains("Steel Range")||type.contains("65")){
			initSteelRange();
		} else if(type.contains("Fancy Range")||type.contains("66")){
			initFancyRange();
		} else if(type.contains("Beer Barrel")||type.contains("67")){
			initBeerBarrel();
		} else if(type.contains("Cider Barrel")||type.contains("68")){
			initCiderBarrel();
		} else if(type.contains("Asgarnian Ale")||type.contains("69")){
			initAsgarnianAle();
		} else if(type.contains("Greenmans Ale")||type.contains("70")){
			initGreenmansAle();
		} else if(type.contains("Dragon Bitter")||type.contains("71")){
			initDragonBitter();
		} else if(type.contains("Chefs Delight")||type.contains("72")){
			initChefsDelight();
		} else if(type.contains("Cat Blanket")||type.contains("73")){
			initCatBlanket();
		} else if(type.contains("Cat Basket")||type.contains("74")){
			initCatBasket();
		} else if(type.contains("Cushioned Basket")||type.contains("75")){
			initCushionedBasket();
		}*/
		
		/* else if(type.contains("")||type.contains("7")){
			init();
		}*/
		
	}
	
	/*private void initNone(){
		this.type = "None";
		this.furniURL = "none/empty";
		this.materials = new Materials[0];
		this.materialNumbers = new int[0];
		this.level = 1;
		this.ID = "00";
	}
	private void initExitPortal(){
		this.type = "Exit Portal";
		this.furniURL = "centrepiece/exit_portal";
		this.materials = new Materials[]{
			new Materials("Iron Bar"),
		};
		this.materialNumbers = new int[]{ 10};
		this.level = 1;
		this.ID = "01";
	}
	private void initDecorativeRock(){
		this.type = "Decorative Rock";
		this.furniURL = "centrepiece/decorative_rock";
		this.materials = new Materials[]{
				new Materials("Limestone Brick"),
		};
		this.materialNumbers = new int[]{ 5};
		this.level = 5;
		this.ID = "02";
	}
	private void initPond(){
		this.type = "Pond";
		this.furniURL = "centrepiece/pond";
		this.materials = new Materials[]{
				new Materials("Soft Clay"),
		};
		this.materialNumbers = new int[]{ 10};
		this.level = 10;
		this.ID = "03";
	}
	private void initImpStatue(){
		this.type = "Imp Statue";
		this.furniURL = "centrepiece/imp_statue";
		this.materials = new Materials[]{
				new Materials("Limestone Brick"),
				new Materials("Soft Clay"),
		};
		this.materialNumbers = new int[]{ 5, 5};
		this.level = 15;
		this.ID = "04";
	}
	private void initDungeonEntrance(){
		this.type = "Dungeon Entrance";
		this.furniURL = "centrepiece/dungeon_entrance";
		this.materials = new Materials[]{
				new Materials("Marble Block"),
		};
		this.materialNumbers = new int[]{ 1};
		this.level = 70;
		this.ID = "05";
	}
	private void initTree(){
		this.type = "Tree";
		this.furniURL = "tree/tree";
		this.materials = new Materials[]{
				new Materials("Bagged Dead Tree"),
				new Materials("Watering Can"),
		};
		this.materialNumbers = new int[]{ 1, 1};
		this.level = 5;
		this.ID = "06";
	}
	private void initNiceTree(){
		this.type = "Nice Tree";
		this.furniURL = "tree/nice_tree";
		this.materials = new Materials[]{
				new Materials("Bagged Nice Tree"),
				new Materials("Watering Can"),
		};
		this.materialNumbers = new int[]{ 1, 1};
		this.level = 10;
		this.ID = "07";
	}
	private void initOakTree(){
		this.type = "Oak Tree";
		this.furniURL = "tree/oak_tree";
		this.materials = new Materials[]{
				new Materials("Bagged Oak Tree"),
				new Materials("Watering Can"),
		};
		this.materialNumbers = new int[]{ 1, 1};
		this.level = 15;
		this.ID = "08";
	}
	private void initWillowTree(){
		this.type = "Willow Tree";
		this.furniURL = "tree/willow_tree";
		this.materials = new Materials[]{
				new Materials("Bagged Willow Tree"),
				new Materials("Watering Can"),
		};
		this.materialNumbers = new int[]{ 1, 1};
		this.level = 30;
		this.ID = "09";
	}
	private void initMapleTree(){
		this.type = "Maple Tree";
		this.furniURL = "tree/maple_tree";
		this.materials = new Materials[]{
				new Materials("Bagged Maple Tree"),
				new Materials("Watering Can"),
		};
		this.materialNumbers = new int[]{ 1, 1};
		this.level = 45;
		this.ID = "10";
	}
	private void initYewTree(){
		this.type = "Yew Tree";
		this.furniURL = "tree/yew_tree";
		this.materials = new Materials[]{
				new Materials("Bagged Yew Tree"),
				new Materials("Watering Can"),
		};
		this.materialNumbers = new int[]{ 1, 1};
		this.level = 60;
		this.ID = "11";
	}
	private void initMagicTree(){
		this.type = "Magic Tree";
		this.furniURL = "tree/magic_tree";
		this.materials = new Materials[]{
				new Materials("Bagged Magic Tree"),
				new Materials("Watering Can"),
		};
		this.materialNumbers = new int[]{ 1, 1};
		this.level = 75;
		this.ID = "12";
	}
	private void initPlant(){
		this.type = "Plant";
		this.furniURL = "smallPlant1/plant";
		this.materials = new Materials[]{
				new Materials("Bagged Plant 1"),
				new Materials("Watering Can"),
		};
		this.materialNumbers = new int[]{ 1, 1};
		this.level = 31;
		this.ID = "13";
	}
	private void initSmallFern(){
		this.type = "Small Fern";
		this.furniURL = "smallPlant1/small_fern";
		this.materials = new Materials[]{
				new Materials("Bagged Plant 2"),
				new Materials("Watering Can"),
		};
		this.materialNumbers = new int[]{ 1, 1};
		this.level = 6;
		this.ID = "14";
	}
	private void initFern(){
		this.type = "Fern";
		this.furniURL = "smallPlant1/fern";
		this.materials = new Materials[]{
				new Materials("Bagged Plant 3"),
				new Materials("Watering Can"),
		};
		this.materialNumbers = new int[]{ 1, 1};
		this.level = 12;
		this.ID = "15";
	}
	private void initDockLeaf(){
		this.type = "Dock Leaf";
		this.furniURL = "smallPlant2/dock_leaf";
		this.materials = new Materials[]{
				new Materials("Bagged Plant 1"),
				new Materials("Watering Can"),
		};
		this.materialNumbers = new int[]{ 1, 1};
		this.level = 1;
		this.ID = "16";
	}
	private void initThistle(){
		this.type = "Thistle";
		this.furniURL = "smallPlant2/thistle";
		this.materials = new Materials[]{
				new Materials("Bagged Plant 2"),
				new Materials("Watering Can"),
		};
		this.materialNumbers = new int[]{ 1, 1};
		this.level = 6;
		this.ID = "17";
	}
	private void initReeds(){
		this.type = "Reeds";
		this.furniURL = "smallPlant2/reeds";
		this.materials = new Materials[]{
				new Materials("Bagged Plant 3"),
				new Materials("Watering Can"),
		};
		this.materialNumbers = new int[]{ 1, 1};
		this.level = 12;
		this.ID = "18";
	}
	private void initFern2(){
		this.type = "Fern";
		this.furniURL = "bigPlant1/fern";
		this.materials = new Materials[]{
				new Materials("Bagged Plant 1"),
				new Materials("Watering Can"),
		};
		this.materialNumbers = new int[]{ 1, 1};
		this.level = 1;
		this.ID = "19";
	}
	private void initBush(){
		this.type = "Bush";
		this.furniURL = "bigPlant1/bush";
		this.materials = new Materials[]{
				new Materials("Bagged Plant 2"),
				new Materials("Watering Can"),
		};
		this.materialNumbers = new int[]{ 1, 1};
		this.level = 6;
		this.ID = "20";
	}
	private void initTallPlant(){
		this.type = "Tall Plant";
		this.furniURL = "bigPlant1/tall_plant";
		this.materials = new Materials[]{
				new Materials("Bagged Plant 3"),
				new Materials("Watering Can"),
		};
		this.materialNumbers = new int[]{ 1, 1};
		this.level = 12;
		this.ID = "21";
	}
	private void initShortPlant(){
		this.type = "Short Plant";
		this.furniURL = "bigPlant2/short_plant";
		this.materials = new Materials[]{
				new Materials("Bagged Plant 1"),
				new Materials("Watering Can"),
		};
		this.materialNumbers = new int[]{ 1, 1};
		this.level = 1;
		this.ID = "22";
	}
	private void initLargeLeafBush(){
		this.type = "Large Leaf Bush";
		this.furniURL = "bigPlant2/large_leaf_bush";
		this.materials = new Materials[]{
				new Materials("Bagged Plant 2"),
				new Materials("Watering Can"),
		};
		this.materialNumbers = new int[]{ 1, 1};
		this.level = 6;
		this.ID = "23";
	}
	private void initHugePlant(){
		this.type = "Huge Plant";
		this.furniURL = "bigPlant2/huge_plant";
		this.materials = new Materials[]{
				new Materials("Bagged Plant 3"),
				new Materials("Watering Can"),
		};
		this.materialNumbers = new int[]{ 1, 1};
		this.level = 12;
		this.ID = "24";
	}
	private void initCrudeWoodenChair(){
		this.type = "Crude Wooden Chair";
		this.furniURL = "chairs/crude_wooden_chair";
		this.materials = new Materials[]{
				new Materials("Plank"),
				new Materials("Nails"),
		};
		this.materialNumbers = new int[]{ 2, 2};
		this.level = 1;
		this.ID = "25";
	}
	private void initWoodenChair(){
		this.type = "Wooden Chair";
		this.furniURL = "chairs/wooden_chair";
		this.materials = new Materials[]{
				new Materials("Plank"),
				new Materials("Nails"),
		};
		this.materialNumbers = new int[]{ 3, 3};
		this.level = 8;
		this.ID = "26";
	}
	private void initRockingChair(){
		this.type = "Rocking Chair";
		this.furniURL = "chairs/rocking_chair";
		this.materials = new Materials[]{
				new Materials("Plank"),
				new Materials("Nails"),
		};
		this.materialNumbers = new int[]{ 3, 3};
		this.level = 14;
		this.ID = "27";
	}
	private void initOakChair(){
		this.type = "Oak Chair";
		this.furniURL = "chairs/oak_chair";
		this.materials = new Materials[]{
				new Materials("Oak Plank"),
		};
		this.materialNumbers = new int[]{ 2};
		this.level = 19;
		this.ID = "28";
	}
	private void initOakArmchair(){
		this.type = "Oak Armchair";
		this.furniURL = "chairs/oak_armchair";
		this.materials = new Materials[]{
				new Materials("Oak Plank"),
		};
		this.materialNumbers = new int[]{ 3};
		this.level = 26;
		this.ID = "29";
	}
	private void initTeakArmchair(){
		this.type = "Teak Armchair";
		this.furniURL = "chairs/teak_armchair";
		this.materials = new Materials[]{
				new Materials("Teak Plank"),
		};
		this.materialNumbers = new int[]{ 2};
		this.level = 35;
		this.ID = "30";
	}
	private void initMahoganyArmchair(){
		this.type = "Mahogany Armchair";
		this.furniURL = "chairs/mahogany_armchair";
		this.materials = new Materials[]{
				new Materials("Mahogany Plank"),
		};
		this.materialNumbers = new int[]{ 2};
		this.level = 50;
		this.ID = "31";
	}
	private void initBrownRug(){
		this.type = "Brown Rug";
		this.furniURL = "rugs/brown_rug";
		this.materials = new Materials[]{
				new Materials("Cloth"),
		};
		this.materialNumbers = new int[]{ 2};
		this.level = 2;
		this.ID = "32";
	}
	private void initRug(){
		this.type = "Rug";
		this.furniURL = "rugs/rug";
		this.materials = new Materials[]{
				new Materials("Cloth"),
		};
		this.materialNumbers = new int[]{ 4};
		this.level = 13;
		this.ID = "33";
	}
	private void initOpulentRug(){
		this.type = "Opulent Rug";
		this.furniURL = "rugs/opulent_rug";
		this.materials = new Materials[]{
				new Materials("Cloth"),
				new Materials("Gold Leaf"),
		};
		this.materialNumbers = new int[]{ 4, 1};
		this.level = 65;
		this.ID = "34";
	}
	private void initClayFireplace(){
		this.type = "Clay Fireplace";
		this.furniURL = "fireplace/clay_fireplace";
		this.materials = new Materials[]{
				new Materials("Soft Clay"),
		};
		this.materialNumbers = new int[]{ 3};
		this.level = 3;
		this.ID = "35";
	}
	private void initStoneFireplace(){
		this.type = "Stone Fireplace";
		this.furniURL = "fireplace/stone_fireplace";
		this.materials = new Materials[]{
				new Materials("Limestone Brick"),
		};
		this.materialNumbers = new int[]{ 2};
		this.level = 33;
		this.ID = "36";
	}
	private void initMarbleFireplace(){
		this.type = "Marble Fireplace";
		this.furniURL = "fireplace/marble_fireplace";
		this.materials = new Materials[]{
				new Materials("Marble Block"),
		};
		this.materialNumbers = new int[]{ 1};
		this.level = 63;
		this.ID = "37";
	}
	private void initTornCurtains(){
		this.type = "Torn Curtains";
		this.furniURL = "curtains/torn_curtains";
		this.materials = new Materials[]{
				new Materials("Plank"),
				new Materials("Cloth"),
				new Materials("Nails"),
		};
		this.materialNumbers = new int[]{ 3, 3, 3};
		this.level = 2;
		this.ID = "38";
	}
	private void initCurtains(){
		this.type = "Curtains";
		this.furniURL = "curtains/curtains";
		this.materials = new Materials[]{
				new Materials("Oak Plank"),
				new Materials("Cloth"),
		};
		this.materialNumbers = new int[]{ 3, 3};
		this.level = 18;
		this.ID = "39";
	}
	private void initOpulentCurtains(){
		this.type = "Opulent Curtains";
		this.furniURL = "curtains/opulent_curtains";
		this.materials = new Materials[]{
				new Materials("Teak Plank"),
				new Materials("Cloth"),
		};
		this.materialNumbers = new int[]{ 3, 3};
		this.level = 40;
		this.ID = "40";
	}
	private void initWoodenBookcase(){
		this.type = "Wooden Bookcase";
		this.furniURL = "bookcases/wooden_bookcase";
		this.materials = new Materials[]{
				new Materials("Plank"),
				new Materials("Nails"),
		};
		this.materialNumbers = new int[]{ 4, 4};
		this.level = 4;
		this.ID = "41";
	}
	private void initOakBookcase(){
		this.type = "Oak Bookcase";
		this.furniURL = "bookcases/oak_bookcase";
		this.materials = new Materials[]{
				new Materials("Oak Plank"),
		};
		this.materialNumbers = new int[]{ 3};
		this.level = 29;
		this.ID = "42";
	}
	private void initMahoganyBookcase(){
		this.type = "Mahogany Bookcase";
		this.furniURL = "bookcases/mahogany_bookcase";
		this.materials = new Materials[]{
				new Materials("Mahogany Plank"),
		};
		this.materialNumbers = new int[]{ 3};
		this.level = 40;
		this.ID = "43";
	}
	private void initWoodTable(){
		this.type = "Wood Table";
		this.furniURL = "kitchenTable/wood_table";
		this.materials = new Materials[]{
				new Materials("Plank"),
				new Materials("Nails"),
		};
		this.materialNumbers = new int[]{ 3, 3};
		this.level = 12;
		this.ID = "44";
	}
	private void initOakTable(){
		this.type = "Oak Table";
		this.furniURL = "kitchenTable/oak_table";
		this.materials = new Materials[]{
				new Materials("Oak Plank"),
		};
		this.materialNumbers = new int[]{ 3};
		this.level = 32;
		this.ID = "45";
	}
	private void initTeakTable(){
		this.type = "Teak Table";
		this.furniURL = "kitchenTable/teak_table";
		this.materials = new Materials[]{
				new Materials("Teak Plank"),
		};
		this.materialNumbers = new int[]{ 3};
		this.level = 52;
		this.ID = "46";
	}
	private void initWoodenLarder(){
		this.type = "Wooden Larder";
		this.furniURL = "larder/wooden_larder";
		this.materials = new Materials[]{
				new Materials("Plank"),
				new Materials("Nails"),
		};
		this.materialNumbers = new int[]{ 8, 8};
		this.level = 9;
		this.ID = "47";
	}
	private void initOakLarder(){
		this.type = "Oak Larder";
		this.furniURL = "larder/oak_larder";
		this.materials = new Materials[]{
				new Materials("Oak Plank"),
		};
		this.materialNumbers = new int[]{ 8};
		this.level = 33;
		this.ID = "48";
	}
	private void initTeakLarder(){
		this.type = "Teak Larder";
		this.furniURL = "larder/teak_larder";
		this.materials = new Materials[]{
				new Materials("Teak Plank"),
				new Materials("Cloth"),
		};
		this.materialNumbers = new int[]{ 8, 2};
		this.level = 43;
		this.ID = "49";
	}
	private void initPumpAndDrain(){
		this.type = "Pump and Drain";
		this.furniURL = "sink/pump_and_drain";
		this.materials = new Materials[]{
				new Materials("Steel Bar"),
		};
		this.materialNumbers = new int[]{ 5};
		this.level = 7;
		this.ID = "50";
	}
	private void initPumpAndTub(){
		this.type = "Pump and Tub";
		this.furniURL = "sink/pump_and_tub";
		this.materials = new Materials[]{
				new Materials("Steel Bar"),
		};
		this.materialNumbers = new int[]{ 10};
		this.level = 27;
		this.ID = "51";
	}
	private void initSink(){
		this.type = "Sink";
		this.furniURL = "sink/sink";
		this.materials = new Materials[]{
				new Materials("Steel Bar"),
		};
		this.materialNumbers = new int[]{ 15};
		this.level = 47;
		this.ID = "52";
	}
	private void initWoodenShelves1(){
		this.type = "Wooden Shelves 1";
		this.furniURL = "shelves/wooden_shelves_1";
		this.materials = new Materials[]{
				new Materials("Plank"),
				new Materials("Nails"),
		};
		this.materialNumbers = new int[]{ 3, 3};
		this.level = 6;
		this.ID = "53";
	}
	private void initWoodenShelves2(){
		this.type = "Wooden Shelves 2";
		this.furniURL = "shelves/wooden_shelves_2";
		this.materials = new Materials[]{
				new Materials("Plank"),
				new Materials("Nails"),
				new Materials("Soft Clay"),
		};
		this.materialNumbers = new int[]{ 3, 3, 6};
		this.level = 12;
		this.ID = "54";
	}
	private void initWoodenShelves3(){
		this.type = "Wooden Shelves 3";
		this.furniURL = "shelves/wooden_shelves_3";
		this.materials = new Materials[]{
				new Materials("Plank"),
				new Materials("Nails"),
				new Materials("Soft Clay"),
		};
		this.materialNumbers = new int[]{ 3, 3, 6};
		this.level = 23;
		this.ID = "55";
	}
	private void initOakShelves1(){
		this.type = "Oak Shelves 1";
		this.furniURL = "shelves/oak_shelves_1";
		this.materials = new Materials[]{
				new Materials("Oak Plank"),
				new Materials("Soft Clay"),
		};
		this.materialNumbers = new int[]{ 3, 6};
		this.level = 34;
		this.ID = "56";
	}
	private void initOakShelves2(){
		this.type = "Oak Shelves 2";
		this.furniURL = "shelves/oak_shelves_2";
		this.materials = new Materials[]{
				new Materials("Oak Plank"),
				new Materials("Soft Clay"),
		};
		this.materialNumbers = new int[]{ 3, 6};
		this.level = 45;
		this.ID = "57";
	}
	private void initTeakShelves1(){
		this.type = "Teak Shelves 1";
		this.furniURL = "shelves/teak_shelves_1";
		this.materials = new Materials[]{
				new Materials("Teak Plank"),
				new Materials("Soft Clay"),
		};
		this.materialNumbers = new int[]{ 3, 6};
		this.level = 56;
		this.ID = "58";
	}
	private void initTeakShelves2(){
		this.type = "Teak Shelves 2";
		this.furniURL = "shelves/teak_shelves_2";
		this.materials = new Materials[]{
				new Materials("Teak Plank"),
				new Materials("Soft Clay"),
				new Materials("Gold Leaf"),
		};
		this.materialNumbers = new int[]{ 3, 6, 2};
		this.level = 67;
		this.ID = "59";
	}
	private void initFirepit(){
		this.type = "Firepit";
		this.furniURL = "stove/firepit";
		this.materials = new Materials[]{
				new Materials("Steel Bar"),
				new Materials("Soft Clay"),
		};
		this.materialNumbers = new int[]{ 1, 2};
		this.level = 5;
		this.ID = "60";
	}
	private void initFirepitWithHook(){
		this.type = "Firepit with Hook";
		this.furniURL = "stove/firepit_with_hook";
		this.materials = new Materials[]{
				new Materials("Steel Bar"),
				new Materials("Soft Clay"),
		};
		this.materialNumbers = new int[]{ 2, 2};
		this.level = 11;
		this.ID = "61";
	}
	private void initFirepitWithPot(){
		this.type = "Firepit with Pot";
		this.furniURL = "stove/firepit_with_pot";
		this.materials = new Materials[]{
				new Materials("Steel Bar"),
				new Materials("Soft Clay"),
		};
		this.materialNumbers = new int[]{ 3, 2};
		this.level = 17;
		this.ID = "62";
	}
	private void initSmallOven(){
		this.type = "Small Over";
		this.furniURL = "stove/small_oven";
		this.materials = new Materials[]{
				new Materials("Steel Bar"),
		};
		this.materialNumbers = new int[]{ 4};
		this.level = 24;
		this.ID = "63";
	}
	private void initLargeOven(){
		this.type = "Large Oven";
		this.furniURL = "stove/large_oven";
		this.materials = new Materials[]{
				new Materials("Steel Bar"),
		};
		this.materialNumbers = new int[]{ 5};
		this.level = 29;
		this.ID = "64";
	}
	private void initSteelRange(){
		this.type = "Steel Range";
		this.furniURL = "stove/steel_range";
		this.materials = new Materials[]{
				new Materials("Steel Bar"),
		};
		this.materialNumbers = new int[]{ 6};
		this.level = 34;
		this.ID = "65";
	}
	private void initFancyRange(){
		this.type = "Fancy Range";
		this.furniURL = "stove/fancy_range";
		this.materials = new Materials[]{
				new Materials("Steel Bar"),
		};
		this.materialNumbers = new int[]{ 8};
		this.level = 42;
		this.ID = "66";
	}
	private void initBeerBarrel(){
		this.type = "Beer Barrel";
		this.furniURL = "barrel/beer_barrel";
		this.materials = new Materials[]{
				new Materials("Plank"),
				new Materials("Nails"),
		};
		this.materialNumbers = new int[]{ 3, 3};
		this.level = 7;
		this.ID = "67";
	}
	private void initCiderBarrel(){
		this.type = "Cider Barrel";
		this.furniURL = "barrel/cider_barrel";
		this.materials = new Materials[]{
				new Materials("Plank"),
				new Materials("Nails"),
				new Materials("Cider"),
		};
		this.materialNumbers = new int[]{ 3, 3, 8};
		this.level = 12;
		this.ID = "68";
	}
	private void initAsgarnianAle(){
		this.type = "Asgarnian Ale";
		this.furniURL = "barrel/asgarnian_ale";
		this.materials = new Materials[]{
				new Materials("Oak Plank"),
				new Materials("Asgarnian Ale"),
		};
		this.materialNumbers = new int[]{ 3, 8};
		this.level = 18;
		this.ID = "69";
	}
	private void initGreenmansAle(){
		this.type = "Greenmans Ale";
		this.furniURL = "barrel/greenmans_ale";
		this.materials = new Materials[]{
				new Materials("Oak Plank"),
				new Materials("Greenmans Ale"),
		};
		this.materialNumbers = new int[]{ 3, 8};
		this.level = 26;
		this.ID = "70";
	}
	private void initDragonBitter(){
		this.type = "Dragon Bitter";
		this.furniURL = "barrel/dragon_bitter";
		this.materials = new Materials[]{
				new Materials("Oak Plank"),
				new Materials("Dragon Bitter"),
				new Materials("Steel Bar"),
		};
		this.materialNumbers = new int[]{ 3, 8, 2};
		this.level = 36;
		this.ID = "71";
	}
	private void initChefsDelight(){
		this.type = "Chefs Delight";
		this.furniURL = "barrel/chefs_delight";
		this.materials = new Materials[]{
				new Materials("Oak Plank"),
				new Materials("Chefs Delight"),
				new Materials("Steel Bar"),
		};
		this.materialNumbers = new int[]{ 3, 8, 2};
		this.level = 48;
		this.ID = "72";
	}
	private void initCatBlanket(){
		this.type = "Cat Blanket";
		this.furniURL = "catBasket/cat_blanket";
		this.materials = new Materials[]{
				new Materials("Cloth"),
		};
		this.materialNumbers = new int[]{ 1};
		this.level = 5;
		this.ID = "73";
	}
	private void initCatBasket(){
		this.type = "Cat Basket";
		this.furniURL = "catBasket/cat_basket";
		this.materials = new Materials[]{
				new Materials("Plank"),
				new Materials("Nails"),
		};
		this.materialNumbers = new int[]{ 2, 2};
		this.level = 19;
		this.ID = "74";
	}
	private void initCushionedBasket(){
		this.type = "Cushioned Basket";
		this.furniURL = "catBasket/cushioned_basket";
		this.materials = new Materials[]{
				new Materials("Plank"),
				new Materials("Nails"),
				new Materials("Wool"),
		};
		this.materialNumbers = new int[]{ 2, 2, 2};
		this.level = 33;
		this.ID = "75";
	}*/
	
	/*private void init(){
		this.type = "";
		this.furniURL = "catBasket/";
		this.materials = new Materials[]{
				new Materials(""),
				new Materials(""),
				new Materials(""),
		};
		this.materialNumbers = new int[]{ };
		this.level = ;
		this.ID = "";
	}*/

	
	private void createImageLabel(){
		this.imageLabel = new JLabel(new ImageIcon( "furniIcons/" + getFurniURL() +".gif" ));
	}
	
	public String getFurniType(){
		return type;
	}
	public void setFurniType(String newType){
		type = newType;
	}
	public String getFurniURL(){
		return furniURL;
	}
	public void setFurniURL(String newFurniURL){
		furniURL = newFurniURL;
	}
	public Materials[] getMaterials(){
		return materials;
	}
	public void setMaterials(Materials[] newMaterials){
		materials = newMaterials;
	}
	public int[] getMaterialNumbers(){
		return materialNumbers;
	}
	public void setmaterialNumbers(int[] newmaterialNumbers){
		materialNumbers = newmaterialNumbers;
	}
	public JLabel getImageLabel(){
		return imageLabel;
	}
	public void setImageLabel(JLabel newImageLabel){
		imageLabel = newImageLabel;
	}
	public int getLevel(){
		return level;
	}
	public void setLevel(int newLevel){
		level = newLevel;
	}
	public String getFurniID(){
		return ID;
	}
	public void setFurniID(String newFurniID){
		ID = newFurniID;
	}
}
