

From Kaggle (https://www.kaggle.com/c/finding-elo/data):

	A total of 50,000 games are provided in portable game notation (data.pgn) format. We have run the games through the 
	Stockfish chess engine (the world's strongest!) and provided the resulting scores in stockfish.csv. 

	stockfish.csv (evaluations of stockfish calculating 1 sec on each position) 
	data.png (games)

	
Created from stockfish.csv and data.png with Png2tf_categorialBoard.java in extended FEN format:

	kaggle_chesspositions_training.csv	
	kaggle_chesspositions_test.csv (each 10th position from data.png)
	
	
	
	
extended FEN format:

	My own adaption of FEN (https://de.wikipedia.org/wiki/Forsyth-Edwards-Notation). 
	Each empty square is represented by a dash ('-').
	There is no slash ('/') dividing the rows.
	There is no Halfmove clock and no  Fullmove number. These are not relevant for evaluation of the position (true?)
	
	example starting position: r,n,b,q,k,b,n,r,p,p,p,p,p,p,p,p,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,P,P,P,P,P,P,P,P,R,N,B,Q,K,B,N,R,w,KQkq,-,18	