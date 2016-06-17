package exemplos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import busca.BuscaCompetitiva;
import busca.Estado;
import busca.Heuristica;
import busca.MostraStatusConsole;

public class EstadoTicTacToe implements Estado, Heuristica {

	public int lastX;
	public int lastY;
	private boolean isOpponent = false;
	private final Boolean[][] board = new Boolean[3][3];

	public void opponent(final int x, final int y) {
		playAt(x, y, false);
	}

	@Override
	public int h() {
		return heuristic(true) - heuristic(false);
	}

	@Override
	public String getDescricao() {
		return null;
	}

	@Override
	public boolean ehMeta() {
		if (hasWon(true) || hasWon(false)) {
			return true;
		}
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				if (board[x][y] == null) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public int custo() {
		return 0;
	}

	@Override
	public List<Estado> sucessores() {
		List<Estado> sucessors = new ArrayList<>();
		if (hasWon(true) || hasWon(false)) {
			return sucessors;
		}
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				if (board[x][y] == null) {
					final EstadoTicTacToe sucessor = copy();
					sucessor.playAt(x, y, !isOpponent);
					sucessors.add(sucessor);
				}
			}
		}
		return sucessors;
	}

	public static void main(String[] args) throws Exception {
		final Scanner scanner = new Scanner(System.in);
		// match.playAt(0, 0, false);
		// match.playAt(2, 1, false);
		// match.playAt(2, 2, false);
		// match.playAt(0, 1, true);
		// match.playAt(1, 0, true);
		// match.playAt(2, 0, true);
		// match.print();
		// match.isOpponent = false;
		// match = (EstadoTicTacToe) search.busca(match).getEstado();
		EstadoTicTacToe match = new EstadoTicTacToe();
		match.print();
		do {
			System.out.print("Informe a linha: ");
			int x = scanner.nextInt() - 1;
			System.out.print("Informe a coluna: ");
			int y = scanner.nextInt() - 1;
			match.playAt(x, y, false);
			if (!match.ehMeta()) {
				match = (EstadoTicTacToe) new BuscaCompetitiva(new MostraStatusConsole(), 20).busca(match).getEstado();
			}
			match.print();
		} while (!match.ehMeta());
		scanner.close();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(board);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EstadoTicTacToe other = (EstadoTicTacToe) obj;
		if (!Arrays.deepEquals(board, other.board))
			return false;
		return true;
	}

	public void print() {
		System.out.println(toString());
	}

	@Override
	public String toString() {
		String str = "\r\n " + player(0, 0) + " | " + player(0, 1) + " | " + player(0, 2) + "\r\n" + " " + player(1, 0)
				+ " | " + player(1, 1) + " | " + player(1, 2) + "\r\n" + " " + player(2, 0) + " | " + player(2, 1)
				+ " | " + player(2, 2) + "\r\n";
		return str;
	}

	private void playAt(final int x, final int y, final boolean v) {
		lastX = x;
		lastY = y;
		isOpponent = v;
		board[x][y] = v;
	}

	private char player(final int x, final int y) {
		Boolean position = board[x][y];
		if (position == null) {
			return ' ';
		}
		return position ? 'X' : 'O';
	}

	private EstadoTicTacToe copy() {
		final EstadoTicTacToe clone = new EstadoTicTacToe();
		clone.board[0] = board[0].clone();
		clone.board[1] = board[1].clone();
		clone.board[2] = board[2].clone();
		return clone;
	}

	private boolean hasWon(final boolean player) {
		if (lineIs(0, player)) {
			return true;
		}
		if (lineIs(1, player)) {
			return true;
		}
		if (lineIs(2, player)) {
			return true;
		}
		if (columnIs(0, player)) {
			return true;
		}
		if (columnIs(1, player)) {
			return true;
		}
		if (columnIs(2, player)) {
			return true;
		}
		if (firstDiagonalIs(player)) {
			return true;
		}
		if (secondDiagonalIs(player)) {
			return true;
		}
		return false;
	}

	private int heuristic(final boolean player) {
		int h = 0;
		if (lineIs(0, player)) {
			h += 3;
		} else if (lineAlmostIs(0, player)) {
			h += 1;
		}
		if (lineIs(1, player)) {
			h += 3;
		} else if (lineAlmostIs(1, player)) {
			h += 1;
		}
		if (lineIs(2, player)) {
			h += 3;
		} else if (lineAlmostIs(2, player)) {
			h += 1;
		}
		if (columnIs(0, player)) {
			h += 3;
		} else if (columnAlmostIs(0, player)) {
			h += 1;
		}
		if (columnIs(1, player)) {
			h += 3;
		} else if (columnAlmostIs(1, player)) {
			h += 1;
		}
		if (columnIs(2, player)) {
			h += 3;
		} else if (columnAlmostIs(2, player)) {
			h += 1;
		}
		if (firstDiagonalIs(player)) {
			h += 3;
		} else if (firstDiagonalAlmostIs(player)) {
			h += 1;
		}
		if (secondDiagonalIs(player)) {
			h += 3;
		} else if (secondDiagonalAlmostIs(player)) {
			h += 1;
		}
		return h;
	}

	private boolean firstDiagonalIs(final boolean v) {
		return is(0, 0, v) && is(1, 1, v) && is(2, 2, v);
	}

	private boolean secondDiagonalIs(final boolean v) {
		return is(2, 0, v) && is(1, 1, v) && is(0, 2, v);
	}

	private boolean lineIs(final int line, final boolean v) {
		return is(line, 0, v) && is(line, 1, v) && is(line, 2, v);
	}

	private boolean columnIs(final int column, final boolean v) {
		return is(0, column, v) && is(1, column, v) && is(2, column, v);
	}

	private boolean lineAlmostIs(final int line, final boolean v) {
		int count = 0;
		if (is(line, 0, !v)) {
			return false;
		} else if (is(line, 0, v)) {
			count++;
		}
		if (is(line, 1, !v)) {
			return false;
		} else if (is(line, 1, v)) {
			count++;
		}
		if (is(line, 2, !v)) {
			return false;
		} else if (is(line, 2, v)) {
			count++;
		}
		return count > 1;
	}

	private boolean columnAlmostIs(final int column, final boolean v) {
		int count = 0;
		if (is(0, column, !v)) {
			return false;
		} else if (is(0, column, v)) {
			count++;
		}
		if (is(1, column, !v)) {
			return false;
		} else if (is(1, column, v)) {
			count++;
		}
		if (is(2, column, !v)) {
			return false;
		} else if (is(2, column, v)) {
			count++;
		}
		return count > 1;
	}

	private boolean firstDiagonalAlmostIs(final boolean v) {
		int count = 0;
		if (is(0, 0, !v)) {
			return false;
		} else if (is(0, 0, v)) {
			count++;
		}
		if (is(1, 1, !v)) {
			return false;
		} else if (is(1, 1, v)) {
			count++;
		}
		if (is(2, 2, !v)) {
			return false;
		} else if (is(2, 2, v)) {
			count++;
		}
		return count > 1;
	}

	private boolean secondDiagonalAlmostIs(final boolean v) {
		int count = 0;
		if (is(0, 2, !v)) {
			return false;
		} else if (is(0, 2, v)) {
			count++;
		}
		if (is(1, 1, !v)) {
			return false;
		} else if (is(1, 1, v)) {
			count++;
		}
		if (is(2, 0, !v)) {
			return false;
		} else if (is(2, 0, v)) {
			count++;
		}
		return count > 1;
	}

	private boolean is(final int x, final int y, final boolean v) {
		Boolean b = board[x][y];
		return b != null && b == v;
	}
}
