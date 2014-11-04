package puzzle.sodoku;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;

/**
 * @author Jenny Gutierrez
 * Sodoku puzzle solver. 
 * Class uses the backtracking algorithm to find a solution for the provide unsolved puzzle.
 * It will check for validity of input (i.e letter, number of rows/columns) before solving the puzzle.
 * <p>
 * @param  file name with unsolved Sodoku puzzle
 * @output file (solved_sodoku.csv) containing solve Sodoku puzzle. Both input and output file can be found at same place.
 */

public class Sodoku {


	public static void main(String[] args){
		
		String inputFile = null;
		int gridSize = 9;
		int[][] grid = new int[gridSize][gridSize];
		
		if (args.length > 0) {
		        inputFile = args[0];
		}
		else{
			System.out.println("Please provide file name to solve Sodoku puzzle");
            System.exit(1);
		}

		Sodoku sodoku = new Sodoku();
        sodoku.solveSodoku(inputFile, sodoku.getSodokuCSV(inputFile));	
	}
	
	
  public Sodoku(){
  }

  void solveSodoku( String fileSodoku, int[][] grid){
		if(isInputValid(0,0,grid)){
			if (isSolved(0,0,grid)){
				System.out.println("Sodoku puzzle has been solve!");
				toFile(grid, getFilePath(fileSodoku));
			}
			else {
				System.out.println("Unable to solve sodoku problem");
			}
		}
		else{
			System.out.println("Invalid Input. Please check input puzzle in the file.");
		}		  
  }
  
  String getFilePath(String fileSodoku){
		File inFile = null;

		//get file path to place solved sodoku file
		inFile = new File(fileSodoku);
	    String absoluteFilePath = inFile.getAbsolutePath();
	    String filePath = absoluteFilePath.substring(0,absoluteFilePath.lastIndexOf(File.separator));

		return filePath;
  }

  boolean isSolved(int col, int row, int[][] grid) {
	  if (col == 9){
		  col = 0;
		  if(++row == 9){
			  //end of grid
			  return true;
		  }
	  }
	  
	  if ( grid[col][row] != 0){
		  //cell is already filled go to next but what happens if the value is invalid?
		  return isSolved(col+1,row, grid);
	  }
	  
	  //iterate from 1 to 9 to check for a valid number for that cell 
	  for (int value=1; value <=9; ++value ){
		  //check if value is valid and has not been taken
		  if(isValidNumber(col, row, value, grid)){
			  grid[col][row] = value;
			  if(isSolved(col+1, row, grid)){
				  return true;
			  }
		  }

	  }

	  grid[col][row] = 0;
	  return false;
  }//end of isSolved
  
  boolean isInputValid(int col, int row, int[][] grid) {
	  if (col == 9){
		  col = 0;
		  if(++row == 9){
			  //end of grid
			  return true;
		  }
	  }
	  
	  if ( grid[col][row] == 0){
		  //if the value in the cell is is 0 leave it to be solved later
		  return isInputValid(col+1,row, grid);
	  }
	  
  		// check is the value provide in the cell is valid
	  if(isValidNumber(col, row, grid[col][row], grid)){
		  if(isInputValid(col+1, row, grid)){
			  return true;
		  }
	  }

	  return false;
  }//end of isSolved
  

	void printSodoku(int[][] grid){
		System.out.println("Printing Sodoku");
		for(int row = 0; row < 9; row++){
			for(int col = 0; col < 9; col++){
				System.out.print(grid[col][row]);
				if(col < 8){
					System.out.print(",");
				}
			}
			System.out.println();
		}
	}

	void toFile(int[][] grid, String filePath){
		PrintWriter writer = null;
		try{
		    writer = new PrintWriter(filePath + "\\solved_sodoku.csv", "UTF-8");

			for(int j = 0; j < 9; j++){
				for(int i = 0; i < 9; i++){
					writer.print(grid[i][j]);
					if(i < 8){
						writer.print(",");
					}
				}
				writer.println();
			}
		}catch (IOException ex) {
			  // report
		} finally {
		   try {writer.close();} catch (Exception ex) {}
		}


	}
	
	int[][] getSodokuCSV(String fileName){
		String csvFile = fileName;
		BufferedReader br = null;
		String line = "";
		String csvSplitBy = ",";
		int[][] grid = new int[9][9];
		
		try {
		br = new BufferedReader(new FileReader(csvFile));

		int lineNumber = 0;
		while (( line = br.readLine()) != null){
			
			String[] sodokuLine = line.split(csvSplitBy);
			
			if (lineNumber >= 9 || sodokuLine.length > 9 ){
				System.out.println("ERROR: Wrong input. Incorrect number of columns or rows.");
				System.out.println("Line Number " + (lineNumber+1) +". Column Number "+sodokuLine.length);
				System.exit (1);
			}
			
			//covert content to integers
			for (int i = 0; i < sodokuLine.length; i++) {
			    try {
			    	if(sodokuLine[i].matches("\\d+")) {
			    	    grid[i][lineNumber] = Integer.parseInt(sodokuLine[i]);
			    	}
			    	else{
			    		System.out.println("ERROR: Wrong Sodoku input:" + sodokuLine[i] + " Only digits [0..9] allowed.");
                        System.out.println("Line Number " + (lineNumber+1) +". Column: "+sodokuLine.length);
			    		System.exit(1);
			    	}
			    } catch (NumberFormatException nfe) {};
			}

		
			lineNumber++;
		}
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}//end of while

	    return grid; 
	} //end of getSodokuCSV
	
	  boolean isValidNumber(int col, int row, int value, int[][] grid){
		  //check across the columns for that row
		  for( int i = 0; i < 9; i++){
			  if(grid[i][row] == value && i != col){
				  return false;
			  }
		  }
		  
		  //check across the rows for that column
		  for( int i = 0; i < 9; i++){
			  if(grid[col][i] == value && i != row){
				  return false;
			  }
		  }	  
		  
		  //check the 3/3 grid or other size if option is available
		  int offsetRow = (row/3)*3;
		  int offsetCol = (col/3)*3;
		  
		  for (int subGridRow = 0; subGridRow < 3; subGridRow++){
			  for (int subGridCol = 0; subGridCol < 3; subGridCol++){
				  if((row != offsetRow+subGridRow) && col != (offsetCol+subGridCol))
				      if (grid[offsetCol+subGridCol][offsetRow+subGridCol] == value){
					     return false;
				      }
			  }
			  return true;
		  }
		  
		  return true;
	  }//end of isValidNumber
}//end of class
