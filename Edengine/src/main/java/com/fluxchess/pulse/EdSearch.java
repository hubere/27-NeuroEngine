/*
 * Copyright (C) 2013-2014 Phokham Nonava
 *
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.fluxchess.pulse;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.fluxchess.jcpi.commands.IProtocol;

/**
 * This class implements our search in a separate thread to keep the main thread
 * available for more commands.
 */
final class EdSearch extends Search {

	public EdSearch(IProtocol protocol, Board board) {
		super(protocol, board);
	}


	public void run() {
		super.run();
		
		//
		// do a boad evaluation
		//
		board.makeMove(rootMoves.entries[0].move);
		int m = evaluation.materialScore(board);
		double t = evaluation.mobilityScore(board);
		int ks = evaluation.kingSafety(board);
		double dk = evaluation.compactness(board);
		double dm = evaluation.elevation(board);
		NumberFormat formatter = new DecimalFormat("#0.00");
		System.out.println("m          =" + m);
		System.out.println("t          =" + formatter.format(t));
		System.out.println("safety     =" + ks);
		System.out.println("compactness=" + formatter.format(dk));
		System.out.println("elevation  =" + formatter.format(dm));

		board.undoMove(rootMoves.entries[0].move);
	}


	protected void searchRoot(int depth, int alpha, int beta) {
		long start = System.currentTimeMillis();
		long m_timeConsumed = 0;
		evaluation.m_methodCalled = 0;
		evaluation.m_timeConsumedMaterial = 0;
		evaluation.m_timeConsumedMobility = 0;

		super.searchRoot(depth, alpha, beta);

		m_timeConsumed += System.currentTimeMillis() - start;
		System.out.println("m_timeConsumed          =" + m_timeConsumed + "ms");
		System.out.println("m_methodCalled          =" + evaluation.m_methodCalled + "times");
		System.out.println("m_timeConsumedMaterial  =" + evaluation.m_timeConsumedMaterial + "ms");
		System.out.println("m_timeConsumedMobility  =" + evaluation.m_timeConsumedMobility + "ms");

	}

}
