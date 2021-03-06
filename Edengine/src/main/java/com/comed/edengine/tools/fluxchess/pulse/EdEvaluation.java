/*
 * Copyright (C) 2013-2014 Phokham Nonava
 *
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.comed.edengine.tools.fluxchess.pulse;

import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogDirectoryPersistence;
import org.encog.util.simple.EncogUtility;

import com.fluxchess.pulse.Bitboard;
import com.fluxchess.pulse.Board;
import com.fluxchess.pulse.Color;
import com.fluxchess.pulse.Evaluation;
import com.fluxchess.pulse.Pulse;
import com.fluxchess.pulse.Square;
import com.fluxchess.pulse.Value;

public class EdEvaluation extends Evaluation {

	static final int TEMPO = 1;

	static int materialWeight = 100;
	static int mobilityWeight = 80;
	private static final int MAX_WEIGHT = 100;

	public long m_timeConsumedMaterial = 0;
	public long m_timeConsumedMobility = 0;
	public long m_methodCalled = 0;

	static BasicNetwork network;
	static {
		network = (BasicNetwork) EncogDirectoryPersistence
				.loadObject(new java.io.File("c:/Users/Huber/EncogProjects/Mobility/mobility.eg"));
		MLDataSet trainingsSet = EncogUtility
				.loadEGB2Memory(new java.io.File("c:/Users/Huber/EncogProjects/Mobility/mobility.egb"));

		double calculateError = network.calculateError(trainingsSet);
		System.out.println("calculateError: " + calculateError);

	}

	/**
	 * Evaluates the board.
	 * 
	 * @param board
	 *            the board.
	 * @return the evaluation value in centipawns.
	 */
	public int evaluate(Board board) {
		assert board != null;

		m_methodCalled++;

		// Initialize
		int myColor = board.activeColor;
		int oppositeColor = Color.opposite(myColor);
		int value = 0;

		// Evaluate material
		long start = System.currentTimeMillis();
		int materialScore = (evaluateMaterial(myColor, board) - evaluateMaterial(oppositeColor, board)) * materialWeight
				/ MAX_WEIGHT;
		value += materialScore;
		m_timeConsumedMaterial += System.currentTimeMillis() - start;

		// Evaluate mobility
		start = System.currentTimeMillis();
		int mobilityScore = (evaluateMobility(myColor, board) - evaluateMobility(oppositeColor, board)) * mobilityWeight
				/ MAX_WEIGHT;
		value += mobilityScore;
		m_timeConsumedMobility += System.currentTimeMillis() - start;

		double[] input2 = new double[769];
		int index = 0;

		//
		// Print mobility
		//
		long[] squares = { board.pawns[Color.WHITE].squares, board.knights[Color.WHITE].squares,
				board.bishops[Color.WHITE].squares, board.rooks[Color.WHITE].squares, board.queens[Color.WHITE].squares,
				board.kings[Color.WHITE].squares, board.pawns[Color.BLACK].squares, board.knights[Color.BLACK].squares,
				board.bishops[Color.BLACK].squares, board.rooks[Color.BLACK].squares, board.queens[Color.BLACK].squares,
				board.kings[Color.BLACK].squares, };
		String s = "";
		for (long square : squares) {
			String boardPart = String.format("%64s", Long.toBinaryString(square)).replace(' ', '0').replace("0", "0,")
					.replace("1", "1,");
			Pulse.mobilityWriter.print(boardPart);
			s += boardPart;

			for (int i = 63; i >= 0; i--) {
				long mask = 1L << i;
				input2[index] = (square & mask) != 0 ? 1.0 : 0.0;
				index++;
			}
		}
		Pulse.mobilityWriter.println(mobilityScore / 100.0);
		// s += mobilityScore / 100.0;

		//
		// Ask NN
		//
		double[] input = new double[769];
		String[] split = s.split(",");
		for (int i = 0; i < split.length; i++) {
			input[i] = Double.parseDouble(split[i]);
		}

		MLData MLboard = new BasicMLData(input2);
		MLData compute = network.compute(MLboard);
		int data = (int) (compute.getData(0) * 100);

		System.out.println("Mobility: " + (mobilityScore / 100.0) + " NN: " + (data / 100.0) + " Error: "
				+ (mobilityScore - data) / 100.0);

		// Add Tempo
		value += TEMPO;

		// This is just a safe guard to protect against overflow in our
		// evaluation
		// function.
		if (value <= -Value.CHECKMATE_THRESHOLD || value >= Value.CHECKMATE_THRESHOLD) {
			assert false;
		}

		return value;
	}

	public int materialScore(Board board) {
		int myColor = board.activeColor;
		int oppositeColor = Color.opposite(myColor);
		int materialScore = board.material[myColor] - board.material[oppositeColor];
		return materialScore / 100;
	}

	public double mobilityScore(Board board) {
		int myColor = board.activeColor;
		int oppositeColor = Color.opposite(myColor);
		return ((double) evaluateMobility(myColor, board) / evaluateMobility(oppositeColor, board));
	}

	public int kingSafety(Board board) {
		// TODO Auto-generated method stub
		return 0;
	}

	public double compactness(Board board) {
		int myColor = board.activeColor;
		int oppositeColor = Color.opposite(myColor);
		int mySpace = space(board, myColor);
		int hisSpace = space(board, oppositeColor);
		return ((double) mySpace / hisSpace);
	}

	private int space(Board board, int myColor) {
		int minRank = 9;
		int maxRank = 0;
		int minFile = 9;
		int maxFile = 0;
		for (long squares = board.pawns[myColor].squares; squares != 0; squares &= squares - 1) {
			int square = Bitboard.next(squares);
			int file = Square.getFile(square);
			int rank = Square.getRank(square);
			minFile = Math.min(minFile, file);
			maxFile = Math.max(maxFile, file);
			minRank = Math.min(minRank, rank);
			maxRank = Math.max(maxRank, rank);
		}

		int squareKing = Bitboard.next(board.kings[myColor].squares);
		int file = Square.getFile(squareKing);
		int rank = Square.getRank(squareKing);
		minFile = Math.min(minFile, file);
		maxFile = Math.max(maxFile, file);
		minRank = Math.min(minRank, rank);
		maxRank = Math.max(maxRank, rank);
		return (maxFile - minFile + 1) * (maxRank - minRank + 1);
	}

	public double elevation(Board board) {
		// TODO Auto-generated method stub
		return 0;
	}
}
