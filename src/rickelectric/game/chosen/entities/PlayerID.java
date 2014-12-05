package rickelectric.game.chosen.entities;

public enum PlayerID {
	Man1, Man2, Man3, Ghost, Woman1, Woman2, Woman3, Woman;
	
	public String getPlayerName() {
		switch (this) {
		case Man1:
			return "Daxter: The Rebel Time-Lord of Eyorn";
		case Man2:
			return "Emmanuel: The Rebel Space-Lord of Zela";
		case Man3:
			return "Sayearn: The Goofy Sailor of Aldrydion";
		case Ghost:
			return "Veron: The Yepsyl Ghost of Nowhere"; 
		case Woman1:
			return "Timille: The Ice Princess of Kryzonaux";
		case Woman2:
			return "Melony: The Zela Tribrid of Veridiaa";
		case Woman3:
			return "Leanna: The Fire Queen of Chaldaron";
		default:
			return "Kha-ona: The Cyber Raider of Du'Virta";
		}
	}

	public int getNumFrames() {
		switch (this) {
		case Man1:
			return 11;
		case Man2:
			return 8;
		case Man3:
			return 18;
		case Ghost:
			return 10;
		case Woman1:
			return 8;
		case Woman2:
			return 16;
		case Woman3:
			return 12;
		default:
			return 16;
		}
	}

	public String getImageL() {
		switch (this) {
		case Man1:
			return "man-1-L";
		case Man2:
			return "man-2-L";
		case Man3:
			return "man-3-L";
		case Ghost:
			return "Ghost-L";
		case Woman1:
			return "woman-1-L";
		case Woman2:
			return "woman-2-L";
		case Woman3:
			return "woman-3-L";
		default:
			return "woman-4-L";
		}
	}

	public String getImageR() {
		switch (this) {
		case Man1:
			return "man-1-R";
		case Man2:
			return "man-2-R";
		case Man3:
			return "man-3-R";
		case Ghost:
			return "Ghost-R";
		case Woman1:
			return "woman-1-R";
		case Woman2:
			return "woman-2-R";
		case Woman3:
			return "woman-3-R";
		default:
			return "woman-4-R";
		}
	}

	public int reverseImageID() {
		switch (this) {
		case Man1:
			return 1;
		case Man2:
			return 2;
		case Man3:
			return 1;
		case Ghost:
			return 2;
		case Woman1:
			return 2;
		case Woman2:
			return 2;
		case Woman3:
			return 2;
		default:
			return 1;
		}
	}

	public int restOrdinalID() {
		switch (this) {
		case Man1:
			return 2;
		case Man2:
			return 4;
		case Man3:
			return 3;
		case Ghost:
			return 4;
		case Woman1:
			return 4;
		case Woman2:
			return 8;
		case Woman3:
			return 5;
		default:
			return 1;
		}
	}

	public int jumpOrdinalID() {
		switch (this) {
		case Man1:
			return 2;
		case Man2:
			return 5;
		case Man3:
			return 14;
		case Ghost:
			return 2;
		case Woman1:
			return 3;
		case Woman2:
			return 15;
		case Woman3:
			return 1;
		default:
			return 16;
		}
	}

}
