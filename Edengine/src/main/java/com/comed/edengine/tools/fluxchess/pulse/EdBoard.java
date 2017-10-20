package com.comed.edengine.tools.fluxchess.pulse;

import java.util.BitSet;

import com.fluxchess.jcpi.models.GenericBoard;
import com.fluxchess.pulse.Bitboard;
import com.fluxchess.pulse.Board;
import com.fluxchess.pulse.Color;

/**
 * This is Edis adaption of fluxchesses internal board.
 */
public class EdBoard  extends Board{


	public EdBoard(GenericBoard genericBoard) {
		super(genericBoard);
	}

	public String printBoard() {
		char[] charArray = new char[64];

		for (int i = 0; i < 64; i++) {
			long field = 1L << i;
			// System.out.println(String.format("%64s",
			// Long.toBinaryString(field)).replace(' ', '0').replaceAll("(.{8})", "$1\n"));

			if ((pawns[Color.WHITE].squares & field) > 0)
				charArray[i] = 'P';
			if ((knights[Color.WHITE].squares & field) > 0)
				charArray[i] = 'N';
			if ((bishops[Color.WHITE].squares & field) > 0)
				charArray[i] = 'B';
			if ((rooks[Color.WHITE].squares & field) > 0)
				charArray[i] = 'R';
			if ((queens[Color.WHITE].squares & field) > 0)
				charArray[i] = 'Q';
			if ((kings[Color.WHITE].squares & field) > 0)
				charArray[i] = 'K';

			if ((pawns[Color.BLACK].squares & field) > 0)
				charArray[i] = 'p';
			if ((knights[Color.BLACK].squares & field) > 0)
				charArray[i] = 'n';
			if ((bishops[Color.BLACK].squares & field) > 0)
				charArray[i] = 'b';
			if ((rooks[Color.BLACK].squares & field) > 0)
				charArray[i] = 'r';
			if ((queens[Color.BLACK].squares & field) > 0)
				charArray[i] = 'q';
			if ((kings[Color.BLACK].squares & field) > 0)
				charArray[i] = 'k';
		}

		return new String(charArray).replaceAll("(.{8})", "$1\n");
	}

	public String printBits(String delimiter) {
		String s = "";
		s += bitboard2binary(pawns[Color.WHITE]);
		s += bitboard2binary(knights[Color.WHITE]);
		s += bitboard2binary(bishops[Color.WHITE]);
		s += bitboard2binary(rooks[Color.WHITE]);
		s += bitboard2binary(queens[Color.WHITE]);
		s += bitboard2binary(kings[Color.WHITE]);
		s += bitboard2binary(pawns[Color.BLACK]);
		s += bitboard2binary(knights[Color.BLACK]);
		s += bitboard2binary(bishops[Color.BLACK]);
		s += bitboard2binary(rooks[Color.BLACK]);
		s += bitboard2binary(queens[Color.BLACK]);
		s += bitboard2binary(kings[Color.BLACK]);

		return s.replaceAll("(.{1})", "$1" + delimiter);
	}

	/**
	 * My own adaption of FEN
	 * (https://de.wikipedia.org/wiki/Forsyth-Edwards-Notation). Each empty square
	 * is represented by a dash ('-'). There is no slash ('/') dividing the rows.
	 * There is no Halfmove clock and no Fullmove number. These are not relevant for
	 * evaluation of the position (true?)
	 *
	 * example starting position:
	 * r,n,b,q,k,b,n,r,p,p,p,p,p,p,p,p,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,P,P,P,P,P,P,P,P,R,N,B,Q,K,B,N,R,w,KQkq,-,18
	 * 
	 * @param delimiter
	 * @return
	 */
	public String printExtendedFEN(String delimiter) {

		// see https://de.wikipedia.org/wiki/Forsyth-Edwards-Notation

		// get board in FEN
		String fen = this.toString();

		String split[] = fen.split(" ");
		if (split.length < 6) {
			System.err.println("Found FEN with less than 6 groups: " + this);
		}

		String pieces = split[0];
		String whoToMove = split[1];
		String right2Castle = split[2];
		String enPassant = split[3];
		// String numberOfMovesSinceLastPawnMoce = split[4]; <= These are not used 
		// String numberOfNextMove = split[5];

		// replace number of empty squares with dashes and remove row delimiter of FEN
		pieces = pieces.replaceAll("1", "-");
		pieces = pieces.replaceAll("2", "--");
		pieces = pieces.replaceAll("3", "---");
		pieces = pieces.replaceAll("4", "----");
		pieces = pieces.replaceAll("5", "-----");
		pieces = pieces.replaceAll("6", "------");
		pieces = pieces.replaceAll("7", "-------");
		pieces = pieces.replaceAll("8", "--------");
		pieces = pieces.replaceAll("/", "");
		StringBuilder sb = new StringBuilder(pieces);
		for (int idx = 1; idx < sb.length(); idx += 2)
			sb.insert(idx, delimiter);
		pieces = sb.toString();

		String extendedFEN = pieces + "," + whoToMove + "," + right2Castle + "," + enPassant + ","; 
		return extendedFEN;
	}

	public BitSet toBits() {
		long[] reps = new long[12];
		reps[0] = pawns[Color.WHITE].squares;
		reps[1] = knights[Color.WHITE].squares;
		reps[2] = bishops[Color.WHITE].squares;
		reps[3] = rooks[Color.WHITE].squares;
		reps[4] = queens[Color.WHITE].squares;
		reps[5] = kings[Color.WHITE].squares;
		reps[0] = pawns[Color.BLACK].squares;
		reps[1] = knights[Color.BLACK].squares;
		reps[2] = bishops[Color.BLACK].squares;
		reps[3] = rooks[Color.BLACK].squares;
		reps[4] = queens[Color.BLACK].squares;
		reps[5] = kings[Color.BLACK].squares;

		return BitSet.valueOf(reps);
	}

	public String bitboard2binary(Bitboard bitboard) {
		// return String.format("%64s", Long.toBinaryString(bitboard.squares)).replace('
		// ', '0').replaceAll("(.{8})", "$1\n");
		return String.format("%64s", Long.toBinaryString(bitboard.squares)).replace(' ', '0');
	}

}
