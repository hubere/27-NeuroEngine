package com.comed.edengine.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.comed.edengine.tools.fluxchess.pulse.EdBoard;
import com.fluxchess.jcpi.models.GenericBoard;
import com.fluxchess.jcpi.models.IllegalNotationException;
// import com.fluxchess.pulse.Board;

import chesspresso.game.Game;
import chesspresso.move.Move;
import chesspresso.pgn.PGNReader;
import chesspresso.position.Position;

/**
 * Read a database in PGN and an evaluation file. Merge them. Write a tensorflow
 * trainingsset.
 * 
 * @author Huber
 *
 */
public class Png2tf_categorialBoard {

	private static String filenameData = "src\\main\\python\\comed\\neuroengine\\edengine\\data\\stockfishEvaluations\\data.pgn";
	private static String filenameBewertung = "src\\main\\python\\comed\\neuroengine\\edengine\\data\\stockfishEvaluations\\stockfish.csv";
	private static String filenameTrainingsSet = "src\\main\\python\\comed\\neuroengine\\edengine\\data\\stockfishEvaluations\\kaggle_chesspositions_train.extFEN";
	private static String filenameTestSet = "src\\main\\python\\comed\\neuroengine\\edengine\\data\\stockfishEvaluations\\kaggle_chesspositions_test.extFEN";

	
	public static void main(String[] args) {

		System.out.println("Working Directory = " + System.getProperty("user.dir"));

		PrintStream trainingStream = null;
		PrintStream testStream = null;
		BufferedReader br = null;		
		File trainingFile = new File(filenameTrainingsSet);
		File testFile = new File(filenameTestSet);

		try {

			//
			// parse evaluations file into evaluations data structure
			//
			System.out.println("PGN2TF: reading " + filenameBewertung);
			Map<Integer, ArrayList<Integer>> evaluations = new HashMap<Integer, ArrayList<Integer>>();
			InputStream fis = new FileInputStream(filenameBewertung);
			InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
			br = new BufferedReader(isr);
			String line;
			Integer gameNo;
			Integer moveEval;
			while ((line = br.readLine()) != null) {
				String[] split = line.split(",");
				if (split.length != 2)
					continue;
				try {
					gameNo = Integer.parseInt(split[0]);
				} catch (Exception e) {
					continue;
				}
				String[] moveEvaluations = split[1].split(" ");
				ArrayList<Integer> moveEvaluationsList = new ArrayList<Integer>();
				for (String eval : moveEvaluations) {
					try {
						moveEval = Integer.parseInt(eval);
					} catch (NumberFormatException e) {
						moveEval = Integer.MIN_VALUE;
					}
					moveEvaluationsList.add(moveEval);
				}
				evaluations.put(gameNo, moveEvaluationsList);
			}
			// for (Map.Entry entry : evaluations.entrySet()) {
			// System.out.println(entry.getKey() + ", " + entry.getValue());
			// }

			//
			// read game data
			//
			System.out.println("PGN2TF: reading " + filenameData);
			PGNReader reader = new PGNReader(filenameData);

			//
			// create writer and write header
			//
			trainingStream = new PrintStream(trainingFile);
			trainingStream.println(evaluations.size() + ",68,extFEN and its evaluation");
			testStream = new PrintStream(testFile);
			testStream.println(evaluations.size() + ",68,extFEN and its evaluation");
			System.out.println("PGN2TF: wiriting to " + trainingFile.getAbsoluteFile());
			System.out.println("PGN2TF: wiriting to " + testFile.getAbsoluteFile());

			int positionIdx = 0;	
			int maxGames = 1000;
			int gameNr = 0;
			Game game;
			while (gameNr++ < maxGames) {
				try
				{
					game = reader.parseGame();	
				}catch (Exception e)	{
					System.err.println("Could not parse game ");
					continue;
				}
				
				game.gotoStart();
				
				String gameEvent = game.getEvent();
				ArrayList<Integer> moveEvaluationsList = evaluations.get(Integer.parseInt(gameEvent));
				System.out.println("Game: " + game.getEvent() + " NumOfMoves: " + game.getNumOfMoves() + " moveEvaluationsList: " + moveEvaluationsList);

				while (game.hasNextMove()) {
					Move move = game.getNextMove();
					int ply = game.getCurrentPly();
					int no = game.getCurrentMoveNumber() + 1;
					Position position = game.getPosition();
					GenericBoard genericBoard = new GenericBoard(position.getFEN());
					EdBoard board = new EdBoard(genericBoard);

					// System.out.println("CurrentMoveNumber " +
					// game.getCurrentMoveNumber());
					// System.out.println("move " + move);
					// System.out.println("move.getLAN " + move.getLAN());
					// System.out.println("move.getSAN " + move.getSAN());

					// if (move.isWhiteMove()) {
					// System.out.print(no + ". " + move + " ");
					// } else {
					// System.out.print(" - " + move + " ");
					// }
					// System.out.println();

					// System.out.println(board);
					// System.out.println(board.printExtendedFEN(","));
					// System.out.println(board.printBoard());
					// System.out.println(board.printBits(""));
					// System.out.println(moveEvaluationsList.get(ply) + " - " +
					// board + " - " + board.printBits(","));
					// System.out.println(board.printBits(",") +
					// moveEvaluationsList.get(ply));

					//
					// write each 10th position to test set, all others to
					// trainings set
					//
					if (ply >= moveEvaluationsList.size()) {
						System.err.println("ply (" + ply + ") is greater then numer of evaluations ("
								+ moveEvaluationsList.size() + ") for game " + game.getLongInfoString() + " NumOfPlies" + game.getNumOfPlies());
					} else {

						if (positionIdx++ % 10 == 0)
							testStream.println(board.printExtendedFEN(",") + moveEvaluationsList.get(ply));
						else
							trainingStream.println(board.printExtendedFEN(",")  + moveEvaluationsList.get(ply));
					}
					game.goForward();
				}
			}

		} catch (IOException | IllegalNotationException | IndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			System.out.println();
			System.out.println("----------");
			System.out.println("PGN2TF: wrote " + trainingFile.getAbsoluteFile());
			System.out.println("PGN2TF: wrote " + testFile.getAbsoluteFile());

			if (trainingStream != null) {
				trainingStream.flush();
				trainingStream.close();
				testStream.flush();
				testStream.close();
			}
			if (br != null)
			{
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
