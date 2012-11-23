package japcheckers.game;

import japcheckers.game.Coord;
import java.util.ArrayList;

/**
 *
 * @author Александр
 */
public class Checker implements Cloneable {
	private static int counter = 0;

	private Coord crd;
	private int turn;
	private State state;
	private int ID;
	private Checker[] neighbors, neighbors_backUp;
	private int nghbrCnt, nghbrCnt_backUp;
	private boolean backUpStatus;

	public static enum State {
		CAPTURED (-1),
		ACTIVE (1),
		BORDER (2),
		INSIDE (3);

		private int _type;

		private State(int val) {
		   _type = val;
		}

		public int getInt() {
			return _type;
		}
	}

	public Checker (int x, int y, int turn) {
		crd = new Coord(x, y);
		this.turn = turn;
		this.state = State.ACTIVE;
		this.nghbrCnt = 0;
		ID = counter;
		counter++;
		neighbors = new Checker[8];
		neighbors_backUp = new Checker[8];
		backUpStatus = false;
	}

	private Checker () {
		ID = counter;
		counter++;
	}

	public int getID () {
		return ID;
	}

	public Coord getCrd () {
		return crd;
	}

	public int getTurn () {
		return turn;
	}

	public State getState () {
		return state;
	}

	public void setState (State state) {
		this.state = state;
	}

	public int getStateInt () {
		return state.getInt();
	}

	public void setCrd(Coord crd) {
		this.crd = crd;
	}

	public void bindNeighbor (int dx, int dy, Checker ch) {
		neighbors[Coord.direction(dx, dy)] = ch;
		nghbrCnt++;
	}

	public void unbindNeighbor (int dx, int dy) {
		neighbors[Coord.direction(dx, dy)] = null;
		nghbrCnt--;
	}

	public void unbindAllNeighbors () {
		for (int i = 0; i < neighbors.length; i++) {
			neighbors[i] = null;
		}
		nghbrCnt = 0;
	}

	public void unbindNeighbor (int idx) {
		if (neighbors[idx] != null) {
			neighbors[idx] = null;
			nghbrCnt--;
		}
	}

	public void unbindNeighbor (Checker ch) {
		if (ch == null) return;
		for (int i = 0; i < neighbors.length; i++) {
			if ((neighbors[i] != null) && (neighbors[i].getID() == ch.getID())) {
				neighbors[i] = null;
				nghbrCnt--;
				return;
			}
		}
	}

	public Checker[] getNeighbors () {
		return neighbors;
	}

	public ArrayList<Checker> getNeighborsList () {
		ArrayList<Checker> res = new ArrayList<>();
		for (Checker ch : neighbors) {
			if (ch != null) {
				res.add(ch);
			}
		}
		return res;
	}

	public int getNeighborsCnt () {
		return nghbrCnt;
	}

	public int getDirectionByNeighbor (Checker ch) {
		for (int i = 0; i < neighbors.length; i++) {
			if ((neighbors[i] != null) && (neighbors[i].getID() == ch.getID()))
				return i;
		}
		return -1;
	}

	public ArrayList<Checker> getRightNeighborsList ()  {
		ArrayList<Checker> res = new ArrayList<>();
		if (neighbors[7] != null) res.add(neighbors[7]);
		if (neighbors[0] != null) res.add(neighbors[0]);
		if (neighbors[1] != null) res.add(neighbors[1]);
		if (neighbors[2] != null) res.add(neighbors[2]);
		return res;
	}

	public ArrayList<Checker> getLeftNeighborsList ()  {
		ArrayList<Checker> res = new ArrayList<>();
		if (neighbors[6] != null) res.add(neighbors[6]);
		if (neighbors[5] != null) res.add(neighbors[5]);
		if (neighbors[4] != null) res.add(neighbors[4]);
		if (neighbors[3] != null) res.add(neighbors[3]);
		return res;
	}

	public void backUp () {
		System.arraycopy(neighbors, 0, neighbors_backUp, 0, neighbors.length);
		nghbrCnt_backUp = nghbrCnt;
		backUpStatus = true;
	}

	public void restore () {
		if (backUpStatus == true) {
			System.arraycopy(neighbors_backUp, 0, neighbors, 0, neighbors_backUp.length);
			nghbrCnt = nghbrCnt_backUp;
			backUpStatus = false;
		}
	}

	public boolean BeyondsToCircuit (ArrayList<Checker> circuit) {
		for (Checker ch : circuit) {
			int x = ch.getCrd().getX();
			int y = ch.getCrd().getY();
		}
		return false;
	}

}
