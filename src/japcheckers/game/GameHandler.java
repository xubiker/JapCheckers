package japcheckers.game;

import japcheckers.JCException;
import japcheckers.Pair;
import japcheckers.accounts.User;
import japcheckers.events.JCEventProducer;
import japcheckers.events.JCListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Александр
 */
public class GameHandler {

	private int turn; // turn is associated with corresponding gamer
	private int gamers_cnt, row = 25, col = 25;
	private ArrayList<Checker> checkers;
	private GameFrame frame;
	private ArrayList<ArrayList<Checker>> matrix;
	private ArrayList<User> gamers;

	private JCEventProducer eventProducer;


	//**********************************************************************************************
	private void nextTurn() {
		turn = (turn + 1) % gamers_cnt;
	}

	//**********************************************************************************************
	private void _createFrame() {
		frame = new GameFrame(row, col);
		frame.setHandler(this);
		frame.setVisible(true);
	}

	//**********************************************************************************************
	public GameHandler() {
		eventProducer = new JCEventProducer();
	}

	//**********************************************************************************************
	public void addListener (JCListener lst) {
		eventProducer.addListener(lst);
	}

	//**********************************************************************************************
	public void finishGame () {
		frame.dispose();
		eventProducer.doWork("finish_game", gamers);
	}

	//**********************************************************************************************
	public void StartGame (ArrayList<User> gamers) throws JCException {
		if (gamers == null || gamers.isEmpty()) {
			throw (new JCException());
		}
		this.gamers = gamers;
		gamers_cnt = gamers.size();
		turn = 0;
		checkers = new ArrayList<>();
		matrix = new ArrayList<>();
		for (int i = 0; i <= col; i++) {
			ArrayList<Checker> _col = new ArrayList<>();
			for (int j = 0; j <= row; j++) {
				_col.add(null);
			}
			matrix.add(_col);
		}
		createFrame();
	}

	//**********************************************************************************************
	public void newChecker (int x, int y) {
		if (x > col || x < 0 || y > row || y < 0) {
			System.out.println("Invalid coord");
			return;
		}
		if (matrix.get(x).get(y) != null) {
			System.out.println("Invalid position");
			return;
		}
		Checker ch = new Checker(x, y, turn);
		checkers.add(ch);
		matrix.get(x).set(y, ch);
		bindNeighbors(ch);
		System.out.println(ch.getID() + " (" + ch.getCrd().getX()+ "," + ch.getCrd().getY() + ") has " + ch.getNeighborsCnt() + " nghbrs");
		// build circuit
		ArrayList<Checker> circuit;
		circuit = buildCircuit(ch);
		circuit = excludeChainEffect(circuit, ch);
		if (!circuit.isEmpty()) {
			boolean f = updateCheckersState(circuit);

			System.out.println("circuit: ");
			frame.printLog("circuit:\n");
			if (f) {
				for (int i = 0; i < circuit.size(); i++) {
					frame.getImageLabel().addLine(circuit.get(i), circuit.get((i + 1) % circuit.size()));
//					frame.printLog(circuit.get(i).getID() + "-");
					System.out.print(circuit.get(i).getID() + "-");
				}
//				frame.getImageLabel().addLine(circuit.get(circuit.size() - 1), circuit.get(0));
//				frame.printLog(circuit.get(circuit.size()-1).getID() + "\n________________\n");
				System.out.println(circuit.get(circuit.size()-1).getID() + "\n________________\n");
			}
		}
		frame.getImageLabel().addChecker(ch);
		frame.getImageLabel().repaint();
		updateGameScore();
		nextTurn();
	}

	//**********************************************************************************************
	private void bindNeighbors (Checker ch) {
		System.out.println("setting neighbors for " + ch.getID());
		int x = ch.getCrd().getX();
		int y = ch.getCrd().getY();

		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if ((i == 0) && (j == 0)) continue;
				if ((x + i > col) || (y - j > row) || (x + i < 0) || (y - j < 0)) continue;
				Checker nghbr = matrix.get(x + i).get(y - j);
				if ((nghbr != null) && (nghbr.getState() != Checker.State.CAPTURED) && (nghbr.getTurn() == ch.getTurn())) {
					ch.bindNeighbor(i, j, nghbr);
					nghbr.bindNeighbor(-i, -j, ch);
				}
			}
		}
	}

	//**********************************************************************************************
	private void unbindNeighbors (Checker ch) {
		System.out.println("unbinding neighbors for " + ch.getID());
		for (Checker ch0 : ch.getNeighborsList()) {
			ch0.unbindNeighbor(ch);
		}
		ch.unbindAllNeighbors();
	}

	//**********************************************************************************************
	private void buildPossibleVariety (Set<Checker> set, Checker ch, int turn) {
		ArrayList<Checker> nghbrs = ch.getNeighborsList();
		for (Checker ch0 : nghbrs) {
			if ((ch0.getTurn() == turn) && (ch0.getState() != Checker.State.CAPTURED) && (set.add(ch0) == true)) {
				buildPossibleVariety(set, ch0, turn);
			}
		}
	}

	//**********************************************************************************************
	private ArrayList<Checker> buildCircuit (Checker lastCh) {
		ArrayList<Checker> circuit = new ArrayList<>();
		// --------------------------------------choose checkers, that might be used in new circuit
		Set<Checker> possibleSet = new HashSet<>();
		buildPossibleVariety(possibleSet, lastCh, lastCh.getTurn());

		ArrayList<Checker> currCheckers = new ArrayList<>(possibleSet);
		for (Checker ch : currCheckers) {
			ch.backUp();
		}
		for (Checker ch : currCheckers) {
			System.out.println("POSSIBLE VARIETY " + ch.getID());
		}

		if (currCheckers.isEmpty()) {
			System.out.println("Empty");
			return circuit;
		}

		//------------------------------------------------------------------ exclude dead-end lines
		//------------------------------------------ AND DO NOT FORGET TO RESTORE BACKUPED CHECKERS
		boolean endFlag = true;
		while (endFlag) {
			endFlag = false;
			for (int i = 0; i < currCheckers.size(); i++) {
				Checker ch0 = currCheckers.get(i);
				if (ch0.getNeighborsCnt() == 1) {
					Checker nghbr = ch0.getNeighborsList().get(0);
					nghbr.unbindNeighbor(ch0);
					ch0.restore();
					currCheckers.remove(i);
					i--;
					endFlag = true;
				}
			}
		}
		//------------------------------------------------------ after all remove separate checkers
		for (int i = 0; i < currCheckers.size(); i++) {
			if (currCheckers.get(i).getNeighborsCnt() == 0) {
				currCheckers.get(i).restore();
				currCheckers.remove(i);
				i--;
			}
		}
		//----------------------------------------------------------------------- print all of them
		frame.printLog("after removing dead ends:\n");
		if (currCheckers.isEmpty()) {
			frame.printLog("empty\n");
		} else {
			for (Checker ch : currCheckers) {
				frame.printLog(ch.getID() + ", ");
			}
			frame.printLog("\n");
		}
		//----------------------------------------- check if the last checker wasn't droped out yet
		if (currCheckers.indexOf(lastCh) == -1) {
			System.out.println("Last checker doesn't relate to the circuit");
		} else {
			//------------------------------------------------------------- start building circuits
			int max_y = -1;
			Checker max_ch = null;
			for (Checker ch0 : currCheckers) {
				if (ch0.getCrd().getY() > max_y) {
					max_y = ch0.getCrd().getY();
					max_ch = ch0;
				}
			}
			if (max_ch == null) {
				throw new NullPointerException("there isn't any check with max coords");
			}

			frame.printLog("Start building circuits...\n");
			// set direction
			for (int q = 0; q < 2; q++) {
				int direction, init_direction = 1;
				circuit.clear();
				circuit.add(max_ch);
				frame.printLog(max_ch.getID() + " added\n");
				Checker prev = max_ch;
				Checker cur = null;
				if (q == 0) {
					if (max_ch.getRightNeighborsList().size() > 0) {
						cur = max_ch.getRightNeighborsList().get(0);
						init_direction = 1;
					} else {
						continue;
					}
				} else if (q == 1) {
					if (max_ch.getLeftNeighborsList().size() > 0) {
						cur = max_ch.getLeftNeighborsList().get(0);
						init_direction = -1;
					} else {
						continue;
					}
				}
				circuit.add(cur);
				frame.printLog(cur.getID() + " added\n");
				//----------------------------------------- build until last checker fits the first one
				System.out.println("LOOP " + q);
				while (cur.getID() != max_ch.getID()) {
					if (cur.getNeighborsCnt() == 2) {
						// regular point - just go further
						int idx = (cur.getNeighborsList().indexOf(prev) + 1) % 2;
						prev = cur;
						cur = cur.getNeighborsList().get(idx);
						circuit.add(cur);
						frame.printLog(cur.getID() + " added\n");
					} else {
						// node point - have to choose correct direction
						frame.printLog("node " + cur.getID() + "\n");
						direction = cur.getDirectionByNeighbor(prev);
						while (true) {
							direction = Coord.nextDirection(direction, init_direction);
							if (cur.getNeighbors()[direction] != null) {
								frame.printLog("direction " + direction + " choosed\n");
								prev = cur;
								cur = cur.getNeighbors()[direction];
								circuit.add(cur);
								frame.printLog(cur.getID() + " added\n");
								break;
							}
						}
					}
				}
				if (circuit.indexOf(lastCh) != -1) {
					break;
				}
			}
			circuit.remove(circuit.size()-1);
			frame.printLog("__________________________\n");
		}
		//------------------------------------------------------------------- restore left checkers
		for (Checker ch : currCheckers) {
			ch.restore();
		}
		//--------------------------------------------------------------- return all built circuits
		if (circuit.indexOf(lastCh) == -1) {
			circuit.clear();
		}
		return circuit;
	}

	//**********************************************************************************************
	public ArrayList<Checker> excludeChainEffect (ArrayList<Checker> circuit, Checker initCh) {
		ArrayList<Checker> res = new ArrayList<>();
		boolean[] tags = new boolean[circuit.size()];
		if (circuit.isEmpty()) return res;
		System.out.println("---------CHAIN_EFFECT---------");
		for (Checker ch : circuit)
			System.out.print(ch.getID() + "-");
		System.out.println();

		int idx = circuit.indexOf(initCh);
		System.out.println("InitIdx = " + idx);

		boolean flg;
		int pos = idx + 1;
		if (pos > circuit.size() - 1) {
			pos = 0;
		}
		while (pos != idx) {
			Checker ch0 = circuit.get(pos);
			if (tags[pos] == false) {
				flg = false;
				int pos2 = idx - 1;
				if (pos2 < 0) {
					pos2 = circuit.size() - 1;
				}
				while (pos2 != pos) {
					Checker ch1 = circuit.get(pos2);
					if (tags[pos2] == false) {
						if (ch1.getID() == ch0.getID()) {
							flg = true;
						}
						if (flg) {
							tags[pos2] = true;
						}
					}
					pos2--;
					if (pos2 < 0) {
						pos2 = circuit.size() - 1;
					}
				}
			}
			pos++;
			if (pos > circuit.size() - 1) {
				pos = 0;
			}
		}

		System.out.println("result: ");
		for (int i = 0; i < tags.length; i++) {
			if (tags[i] == false) {
				res.add(circuit.get(i));
				System.out.println(circuit.get(i).getID() + "-");
			}
		}

        int x1 = res.indexOf(initCh);
        int x2 = res.lastIndexOf(initCh);
        if ( x2 - x1 == 2 ) {
            res.remove(x1);
            res.remove(x1);
        } else if (x1 + res.size() - x2 == 2) {
            if ( x1 == 0 ) {
                res.remove(x2);
                res.remove(x2);
            }
            else if ( x1 > 0 ) {
                res.remove(0);
                res.remove(0);
            }
        }

		System.out.println();
		if (res.size() == 2) {
			res.clear();
		}
		return res;
	}

	//**********************************************************************************************
	public double getAngleBetweenVectors (int x1, int y1, int x2, int y2) {
		int vctrProd, scalProd, lh1, lh2;
		double a;
		vctrProd = x1 * y2 - x2 * y1;
		scalProd = x1 * x2 + y1 * y2;
		lh1 = x1 * x1 + y1 * y1;
		lh2 = x2 * x2 + y2 * y2;
		if ( (lh1 == 0) || (lh2 == 0)) {
			return 0;
		}
		a = Math.asin( vctrProd / Math.sqrt(lh1 * lh2) );
		if (scalProd >= 0) {
			return a;
		}
		if (vctrProd >= 0) {
			a = Math.PI - a;
		} else {
			a = -(Math.PI + a);
		}
		return a;
	}

	//**********************************************************************************************
	public int checkInside( ArrayList<Checker> circuit, Checker a) {
		// return value: 0 - outside; 1 - border; 2 - inside
		int lh = circuit.size();
	    double res = 0;
		for (int i = 1; i <= lh; i++ ) {
			res += getAngleBetweenVectors( circuit.get(i - 1).getCrd().getX() - a.getCrd().getX(),
										   circuit.get(i - 1).getCrd().getY() - a.getCrd().getY(),
										   circuit.get(i % lh).getCrd().getX() - a.getCrd().getX(),
										   circuit.get(i % lh).getCrd().getY() - a.getCrd().getY());
		}
		int r = (int)Math.round((Math.abs(res * 4 / Math.PI)));
        if (r == 0 ) {
			return 0;
		}
        if (r < 8) {
			return 1;
		}
        return 2;
	}

	//**********************************************************************************************
	public boolean updateCheckersState (ArrayList<Checker> circuit) {
		// returns true, if circuit has to be drawn
		boolean result = false;
		for (Checker ch : checkers) {
			if ((ch.getTurn() != turn) && (checkInside(circuit, ch) == 2) && (ch.getState() != Checker.State.CAPTURED)) {
				result = true;
				ch.setState(Checker.State.CAPTURED);
				unbindNeighbors(ch);
				gamers.get(turn).addCapturedEnemies();
				System.out.println("CAPTURED " + ch.getID());
			}
		}
		if (result) {
			for (Checker ch : checkers) {
				if ((ch.getTurn() == turn) && (checkInside(circuit, ch) == 2) && (ch.getState() != Checker.State.CAPTURED)) {
					ch.setState(Checker.State.INSIDE);
					unbindNeighbors(ch);
				}
			}
		}
		return result;
	}

	//**********************************************************************************************
	public void updateGameScore () {
		ArrayList<Pair<String, Integer>> gamers_score = new ArrayList<>();
		for (int i = 0; i < gamers_cnt; i++) {
			Pair<String, Integer> pair;
			pair = new Pair(gamers.get(i).getNick(), gamers.get(i).getCapturedEnemiesCnt());
			gamers_score.add(pair);
		}
		frame.updateScore(gamers_score, turn);
	}

	//**********************************************************************************************
	public Checker getCheckerById (int id) {
		if (id == -1) {
			return null;
		}
		return checkers.get(id);
	}

	//**********************************************************************************************
	public boolean coordIsEmpty (int x, int y) {
		return (matrix.get(x).get(y) == null);
	}

	//**********************************************************************************************
	public void printChecks () {
		System.out.println("All checks in the game:");
		for (Checker ch : checkers) {
			System.out.println(ch.getID());
		}
	}

	//**********************************************************************************************
	public void printMatrix () {
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				System.out.print(matrix.get(j).get(i) + " ");
			}
			System.out.print("\n");
		}
	}

	//**********************************************************************************************
	public void createFrame() {

		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
		 * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Windows".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(GameFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(GameFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(GameFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(GameFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				_createFrame();
			}
		});
	}
}