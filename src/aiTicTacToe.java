import java.util.*;
public class aiTicTacToe {

	public int player; //1 for player 1 and 2 for player 2
	private positionTicTacToe myNextMove;

	private double positiveInfinity = Double.POSITIVE_INFINITY;
	private double negativeInfinity = Double.NEGATIVE_INFINITY;
	private List<List<positionTicTacToe>> winningLines = initializeWinningLines();


	//My 3D tictactoe AI algorithms (different lookahead depth for analyzing)
	public positionTicTacToe myAIAlgorithm(List<positionTicTacToe> board, int player)
	{
		// call minimax with alpha-beta pruning to get the best next move
		long startTime = System.nanoTime();
		minimax(board, 4, negativeInfinity, positiveInfinity, true);
		//myNextMove is set to the best we find in minimax
		long endTime = System.nanoTime();
		System.out.println("depth 4: " + (endTime - startTime) / 1000000);
		return myNextMove;
	}

	public positionTicTacToe myAIAlgorithm2(List<positionTicTacToe> board, int player)
	{
		// call minimax with alpha-beta pruning to get the best next move
		long startTime = System.nanoTime();
		minimax(board, 5, negativeInfinity, positiveInfinity, true);
		//myNextMove is set to the best we find in minimax
		long endTime = System.nanoTime();
		System.out.println("depth 5: " + (endTime - startTime) / 1000000);
		return myNextMove;
	}

	public positionTicTacToe myAIAlgorithm3(List<positionTicTacToe> board, int player)
	{
		// call minimax with alpha-beta pruning to get the best next move
		long startTime = System.nanoTime();
		minimax(board, 3, negativeInfinity, positiveInfinity, true);
		//myNextMove is set to the best we find in minimax
		long endTime = System.nanoTime();
		System.out.println("depth 3: " + (endTime - startTime) / 1000000);
		return myNextMove;
	}


	//minimax algorithm implemention
	private int minimax(List<positionTicTacToe> board, int depth, double alpha, double beta, boolean isMaximizer)
	{
		// Base Case: Max look-ahead depth or game ends, no child node.
		if(depth == 0 || isEnded(board) !=0) { //-1 for draw, 0 for not end, 1 and 2 for players
			return staticEvaluate(board, player);
		}
		//Recursive call
		if (isMaximizer) {
			//at the level of maximizer
			double maxScore = negativeInfinity;
			for(positionTicTacToe move : board) {

				// check for invalid move
				if(getStateOfPositionFromBoard(move, board) != 0) continue;

				// copy a board by deepCopyBoard (make a child node)
				List<positionTicTacToe> copyBoard = deepCopyBoard(board);
				if(player == 1) makeMove(move, 1, copyBoard);
				else makeMove(move, 2, copyBoard);

				// Recursive call on minimax for depth-1 and switch to minimizer level
				int score = minimax(copyBoard, depth-1, alpha, beta, false);
				if(score > maxScore) {
					myNextMove = new positionTicTacToe(move.x, move.y, move.z);
				}

				maxScore = Math.max(maxScore, score);

				//update alpha
				alpha = Math.max(alpha, score);
				if(beta <= alpha) {break;}

			}

			// return max value for maximizer
			return (int) maxScore;

		} else {
			//at the level of minimizer
			double minScore = positiveInfinity;
			for(positionTicTacToe move : board) {

				// check for invalid move
				if(getStateOfPositionFromBoard(move, board) != 0) continue;

				// copy a board by deepCopyBoard (make a child node)
				List<positionTicTacToe> copyBoard = deepCopyBoard(board);
				if(player == 1) makeMove(move, 2, copyBoard);
				else makeMove(move, 1, copyBoard);

				// Recursive call on minimax for depth-1 and switch to minimizer level
				int score = minimax(copyBoard, depth-1, alpha, beta, true);

				if(score < minScore) {
					myNextMove = new positionTicTacToe(move.x, move.y, move.z);
				}
				minScore = Math.min(minScore, score);

				//update beta
				beta = Math.min(beta, score);
				if(beta <= alpha) {break;}

			}

			// return min value for minimizer
			return (int) minScore;
		}
	}

	public int staticEvaluate(List<positionTicTacToe> targetBoard, int playerNum)
	{
		// for a targetBoard configuration, we go through all winning lines one by one and count the number of piece for each player.
		// the final output score is based on the count. To emphasize the importance of getting more piece in a winning line, we do some mathematical manipulation and output the difference between 2 players



		int score = 0;
		//counting the winning combinations theoretical state of the board
		for (List<positionTicTacToe> line: winningLines)
		{
			int playerMarks = 0;
			int otherPlayerMarks = 0;
			for(positionTicTacToe winningPosition: line)
			{
				int state = getStateOfPositionFromBoard(winningPosition, targetBoard);

				if(state == playerNum) playerMarks++;
				else if(state == 0);
				else otherPlayerMarks++;
			}

			score += Math.pow(10, playerMarks) - Math.pow(9.999999999, otherPlayerMarks);
		}
		return score;
	}

	//********************************************
	//all methods provided
	private static List<positionTicTacToe> deepCopyBoard(List<positionTicTacToe> board)
	{
		//deep copy of game boards
		List<positionTicTacToe> copiedBoard = new ArrayList<positionTicTacToe>();
		for(int i=0;i<board.size();i++)
		{
			copiedBoard.add(new positionTicTacToe(board.get(i).x,board.get(i).y,board.get(i).z,board.get(i).state));
		}
		return copiedBoard;
	}

	private boolean makeMove(positionTicTacToe position, int player, List<positionTicTacToe> targetBoard)
	{
		//make move on Tic-Tac-Toe board, given position and player
		//player 1 = 1, player 2 = 2

		//brute force (obviously not a wise way though)
		for(int i=0;i<targetBoard.size();i++)
		{
			if(targetBoard.get(i).x==position.x && targetBoard.get(i).y==position.y && targetBoard.get(i).z==position.z) //if this is the position
			{
				if(targetBoard.get(i).state==0)
				{
					targetBoard.get(i).state = player;
					return true;
				}
				else
				{
					System.out.println("Error: this is not a valid move.");
				}
			}

		}
		return false;
	}

	private int getStateOfPositionFromBoard(positionTicTacToe position, List<positionTicTacToe> board)
	{
		//return the state of the position on the board
		//-1 draw, 0 over, 1 and 2 players
		int index = position.x*16+position.y*4+position.z;
		return board.get(index).state;
	}

	private int isEnded(List<positionTicTacToe> board) {
		List<List<positionTicTacToe>> winningLines = initializeWinningLines();
		for(int i=0;i<winningLines.size();i++)
		//check whether any possible winningLine contains all markers of one player.
		{

			positionTicTacToe p0 = winningLines.get(i).get(0);
			positionTicTacToe p1 = winningLines.get(i).get(1);
			positionTicTacToe p2 = winningLines.get(i).get(2);
			positionTicTacToe p3 = winningLines.get(i).get(3);

			int state0 = getStateOfPositionFromBoard(p0,board);
			int state1 = getStateOfPositionFromBoard(p1,board);
			int state2 = getStateOfPositionFromBoard(p2,board);
			int state3 = getStateOfPositionFromBoard(p3,board);

			//if they have the same state (marked by same player) and they are not all marked.
			if(state0 == state1 && state1 == state2 && state2 == state3 && state0!=0)
			{
				return state0; //1 for player1 wins, 2 for player2 wins
			}
		}
		for(int i=0;i<board.size();i++)
		{
			if(board.get(i).state==0)
			{
				return 0; //not ended
			}
		}
		return -1; //draw
	}


	//this is the random algorithm provided
	public positionTicTacToe randomAIAlgorithm(List<positionTicTacToe> board, int player)
	{
		positionTicTacToe myNextMove = new positionTicTacToe(0,0,0);
		
		do
			{
				Random rand = new Random();
				int x = rand.nextInt(4);
				int y = rand.nextInt(4);
				int z = rand.nextInt(4);
				myNextMove = new positionTicTacToe(x,y,z);
			}while(getStateOfPositionFromBoard(myNextMove,board)!=0);
		return myNextMove;
			
		
	}
	private List<List<positionTicTacToe>> initializeWinningLines()
	{
		//create a list of winning line so that the game will "brute-force" check if a player satisfied any 	winning condition(s).
		List<List<positionTicTacToe>> winningLines = new ArrayList<List<positionTicTacToe>>();
		
		//48 straight winning lines
		//z axis winning lines
		for(int i = 0; i<4; i++)
			for(int j = 0; j<4;j++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(i,j,0,-1));
				oneWinCondtion.add(new positionTicTacToe(i,j,1,-1));
				oneWinCondtion.add(new positionTicTacToe(i,j,2,-1));
				oneWinCondtion.add(new positionTicTacToe(i,j,3,-1));
				winningLines.add(oneWinCondtion);
			}
		//y axis winning lines
		for(int i = 0; i<4; i++)
			for(int j = 0; j<4;j++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(i,0,j,-1));
				oneWinCondtion.add(new positionTicTacToe(i,1,j,-1));
				oneWinCondtion.add(new positionTicTacToe(i,2,j,-1));
				oneWinCondtion.add(new positionTicTacToe(i,3,j,-1));
				winningLines.add(oneWinCondtion);
			}
		//x axis winning lines
		for(int i = 0; i<4; i++)
			for(int j = 0; j<4;j++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,i,j,-1));
				oneWinCondtion.add(new positionTicTacToe(1,i,j,-1));
				oneWinCondtion.add(new positionTicTacToe(2,i,j,-1));
				oneWinCondtion.add(new positionTicTacToe(3,i,j,-1));
				winningLines.add(oneWinCondtion);
			}
		
		//12 main diagonal winning lines
		//xz plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,i,0,-1));
				oneWinCondtion.add(new positionTicTacToe(1,i,1,-1));
				oneWinCondtion.add(new positionTicTacToe(2,i,2,-1));
				oneWinCondtion.add(new positionTicTacToe(3,i,3,-1));
				winningLines.add(oneWinCondtion);
			}
		//yz plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(i,0,0,-1));
				oneWinCondtion.add(new positionTicTacToe(i,1,1,-1));
				oneWinCondtion.add(new positionTicTacToe(i,2,2,-1));
				oneWinCondtion.add(new positionTicTacToe(i,3,3,-1));
				winningLines.add(oneWinCondtion);
			}
		//xy plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,0,i,-1));
				oneWinCondtion.add(new positionTicTacToe(1,1,i,-1));
				oneWinCondtion.add(new positionTicTacToe(2,2,i,-1));
				oneWinCondtion.add(new positionTicTacToe(3,3,i,-1));
				winningLines.add(oneWinCondtion);
			}
		
		//12 anti diagonal winning lines
		//xz plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,i,3,-1));
				oneWinCondtion.add(new positionTicTacToe(1,i,2,-1));
				oneWinCondtion.add(new positionTicTacToe(2,i,1,-1));
				oneWinCondtion.add(new positionTicTacToe(3,i,0,-1));
				winningLines.add(oneWinCondtion);
			}
		//yz plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(i,0,3,-1));
				oneWinCondtion.add(new positionTicTacToe(i,1,2,-1));
				oneWinCondtion.add(new positionTicTacToe(i,2,1,-1));
				oneWinCondtion.add(new positionTicTacToe(i,3,0,-1));
				winningLines.add(oneWinCondtion);
			}
		//xy plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,3,i,-1));
				oneWinCondtion.add(new positionTicTacToe(1,2,i,-1));
				oneWinCondtion.add(new positionTicTacToe(2,1,i,-1));
				oneWinCondtion.add(new positionTicTacToe(3,0,i,-1));
				winningLines.add(oneWinCondtion);
			}
		
		//4 additional diagonal winning lines
		List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
		oneWinCondtion.add(new positionTicTacToe(0,0,0,-1));
		oneWinCondtion.add(new positionTicTacToe(1,1,1,-1));
		oneWinCondtion.add(new positionTicTacToe(2,2,2,-1));
		oneWinCondtion.add(new positionTicTacToe(3,3,3,-1));
		winningLines.add(oneWinCondtion);
		
		oneWinCondtion = new ArrayList<positionTicTacToe>();
		oneWinCondtion.add(new positionTicTacToe(0,0,3,-1));
		oneWinCondtion.add(new positionTicTacToe(1,1,2,-1));
		oneWinCondtion.add(new positionTicTacToe(2,2,1,-1));
		oneWinCondtion.add(new positionTicTacToe(3,3,0,-1));
		winningLines.add(oneWinCondtion);
		
		oneWinCondtion = new ArrayList<positionTicTacToe>();
		oneWinCondtion.add(new positionTicTacToe(3,0,0,-1));
		oneWinCondtion.add(new positionTicTacToe(2,1,1,-1));
		oneWinCondtion.add(new positionTicTacToe(1,2,2,-1));
		oneWinCondtion.add(new positionTicTacToe(0,3,3,-1));
		winningLines.add(oneWinCondtion);
		
		oneWinCondtion = new ArrayList<positionTicTacToe>();
		oneWinCondtion.add(new positionTicTacToe(0,3,0,-1));
		oneWinCondtion.add(new positionTicTacToe(1,2,1,-1));
		oneWinCondtion.add(new positionTicTacToe(2,1,2,-1));
		oneWinCondtion.add(new positionTicTacToe(3,0,3,-1));
		winningLines.add(oneWinCondtion);	
		
		return winningLines;
		
	}
	public aiTicTacToe(int setPlayer)
	{
		player = setPlayer;
	}
}
