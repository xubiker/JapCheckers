package japcheckers;

/**
 *
 * @author Александр
 */
public final class Coord {
	private int x, y;

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setCoords (int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Coord (int x, int y) {
		setCoords(x, y);
	}

	public Coord () {
		setCoords(0, 0);
	}

	public Coord (Coord crd) {
		this.x = crd.getX();
		this.y = crd.getY();
	}

	public static int direction (int x, int y) {
		if ((x ==  1) && (y ==  0))
			return 0;
		if ((x ==  1) && (y ==  1))
			return 1;
		if ((x ==  0) && (y ==  1))
			return 2;
		if ((x == -1) && (y ==  1))
			return 3;
		if ((x == -1) && (y ==  0))
			return 4;
		if ((x == -1) && (y == -1))
			return 5;
		if ((x ==  0) && (y == -1))
			return 6;
		if ((x ==  1) && (y == -1))
			return 7;
		return -1;
	}

	public static int nextDirection (int curDirection, int d) {
		if (d == 0) {
			return ((curDirection + 4) % 8);
		}
		return ((curDirection + d + 8) % 8);
	}

}
