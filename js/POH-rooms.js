//function initRooms(){
//in_typeID, in_labelText, in_level, in_cost, in_doorLayout, in_imgURL, in_buildIcon, in_hotspots, in_coords
	var roomsArray = [
		['A', ' ', 0, 0, [false, false, false, false], 'empty', 'unavailable', [], [[]] ],
		['B', 'Parlour', 1, 1000, [false, true, true, true], 'Parlour', 'parlour', [], [[]] ],
		['C', 'Garden', 1, 1000, [true, true, true, true], 'Garden', 'garden', [], [[]] ],
		['D', 'Kitchen', 5, 5000, [false, true, true, false], 'Kitchen', 'kitchen', [0, 1, 2],
			[ 	['199,82,277,82,277,159,194,159,199,82'],
				['317,0,384,0,388,33,373,72,317,71,315,18,317,0'],
				['60,215,69,170,85,174,83,185,90,191,88,198,82,191,79,214,60,215', '98,6,115,10,110,43,92,41,98,6']
			] ],
		['E', 'Dining Room', 10, 5000, [true, false, true, true], 'DiningRoom', 'diningroom', [], [[]] ],
		['F', 'Workshop', 15, 10000, [false, true, false, true], 'Workshop2', 'workshop', [], [[]] ],
		['G', 'Bedroom', 20, 10000, [true, true, false, false], 'Bedroom', 'bedroom', [], [[]] ],
		['H', 'Skill Hall', 25, 15000, [true, true, true, true], 'SkillHall', 'skillhall', [], [[]] ],
		['I', 'Games Room', 30, 25000, [false, true, true, true], 'GamesRoom', 'gamesroom', [], [[]] ],
		['J', 'Combat Room', 32, 25000, [false, true, true, true], 'CombatRoom', 'combatroom', [], [[]] ],
		['K', 'Quest Hall', 35, 25000, [true, true, true, true], 'QuestHall', 'questhall', [], [[]] ],
		['L', 'Study', 40, 50000, [false, true, true, true], 'Study', 'study', [], [[]] ],
		['M', 'Costume Room', 42, 50000, [false, true, false, false], 'empty', 'costumeroom', [], [[]] ],
		['N', 'Chapel', 45, 50000, [true,true, false, false], 'Chapel', 'chapel', [], [[]] ],
		['O', 'Portal Chamber', 50, 100000, [false, true, false, false], 'PortalChamber', 'portalchamber', [], [[]] ],
		['P', 'Formal Garden', 55, 75000, [true, true, true, true], 'FormalGarden', 'formalgarden', [], [[]] ],
		['Q', 'Throne Room', 60, 150000, [false, false, true, false], 'ThroneRoom', 'throneroom', [], [[]] ],
		['R', 'Oubliette', 65, 150000, [true, true, true, true], 'Oubliette', 'oubliette', [], [[]] ],
		['S', 'Dungeon Corridor', 70, 7500, [true, false, true, false], 'DungeonCorridor', 'dungeon', [], [[]] ],
		['T', 'Dungeon Junction', 70, 7500, [true, true, true, true], 'DungeonJunction', 'dungeon', [], [[]] ],
		['U', 'Dungeon Stairs', 70, 7500, [true, true, true, true], 'DungeonStairs', 'dungeon', [], [[]] ],
		['U', 'Dungeon Pit', 70, 10000, [true, true, true, true], 'empty', 'dungeon', [], [[]] ],
		['V', 'Treasure Room', 75, 250000, [false, false, true, false], 'TreasureRoom', 'treasureroom', [], [[]] ],
		['W', ' ', 0, 0, [false, false, false, false], 'empty', 'unavailable', [], [[]] ]
	];
	/*console.log(roomsArray[0][0]);
	console.log(roomsArray[0][1]);
	console.log(roomsArray[0][2]);
	console.log(roomsArray[0][3]);
	console.log(roomsArray[0][4]);
	console.log(roomsArray[0][5]);*/

	//return roomsArray;
//}