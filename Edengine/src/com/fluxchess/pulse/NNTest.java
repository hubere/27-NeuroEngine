package com.fluxchess.pulse;

public class NNTest {

	public static void main(String[] args) {

		new NNTest().test3x3();

	}

	public void test3x3() {
		int size = 3;
		long[] squaresRook = generateSquares(size);
		long[] squaresBischop = generateSquares(size);

		for (int r = 0; r < size * size; r++) {
			for (int b = 0; b < size * size; b++) {
				if ((squaresRook[r] & squaresBischop[b]) != 0) {
					// rook and bishop on same square
					continue;
				}
				int m = evalMobility(squaresRook[r], squaresBischop[b]);
				// System.out.println("mobility(" +
				// square2String(squaresRook[r], size) + ","
				// + square2String(squaresBischop[b], size) + "): " + m);
				System.out.println(square2String(squaresRook[r], size, ",")
						+ square2String(squaresBischop[b], size, ",") + m / 10.0);
			}
		}
	}

	public void test2x2() {
		int size = 2;
		long[] squaresRook = generateSquares(size);
		long[] squaresBischop = generateSquares(size);

		for (int r = 0; r < size * size; r++) {
			for (int b = 0; b < size * size; b++) {
				if ((squaresRook[r] & squaresBischop[b]) != 0) {
					// rook and bishop on same square
					continue;
				}
				int m = evalMobility(squaresRook[r], squaresBischop[b]);
				// System.out.println("mobility(" +
				// square2String(squaresRook[r], size) + ","
				// + square2String(squaresBischop[b], size) + "): " + m);
				System.out.println(square2String(squaresRook[r], size, ",")
						+ square2String(squaresBischop[b], size, ",") + m / 10.0);
			}
		}
	}

	private int evalMobility(long r, long b) {

		long i = r & b;
		if (i > 0)
			return 0; // both on same square => ingore

		int eval = 1;
		int size = 3;
		// long rookMoves[] = { 1, 3, -1, -3 };
		// long bishopMoves[] = { 7, 2, -2, -7 };
		long rookMoves[] = { size, 3, -1, -3 };
		long bishopMoves[] = { 7, 2, -2, -7 };

		for (long rookMove : rookMoves) {
			if (((r + rookMove) & b) == 0L) {
				// free square
				eval++;
			}
		}
		for (long bischopMove : bishopMoves) {
			if ((b + bischopMove & r) == 0L) {
				// free square
				eval--;
			}
			continue;
		}

		return eval;

	}

	private long[] generateSquares(int size) {

		long squares[] = new long[size * size];
		long square = 1;
		for (int i = 0; i < size * size; i++) {
			squares[i] = square;
			System.out.println("square(" + i + "): " + square2String(square, size));
			square = square << 1;
		}
		return squares;
	}

	public static String square2String(long square, int width) {

		return String.format("%" + width * width + "s", Long.toBinaryString(square)).replace(' ', '0');
		// return String.format("%"+width+"s",
		// Long.toBinaryString(square)).replace(' ', '0');
		// .replace("0", "0,").replace("1", "1,");
	}

	public static String square2String(long square, int width, String delim) {

		return square2String(square, width).replace("0", "0" + delim).replace("1", "1" + delim);
		// return String.format("%"+width+"s",
		// Long.toBinaryString(square)).replace(' ', '0');
		// .replace("0", "0,").replace("1", "1,");
	}
}
