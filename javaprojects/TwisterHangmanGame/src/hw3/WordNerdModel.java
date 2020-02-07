//Akanksha Joshi - akankshj
package hw3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class WordNerdModel {

	public static String currentWordFile;
	public static String[] wordsFromFile;
	public static String WORDS_FILE_NAME = "data/wordsFile.txt";		//for mac os
	public static String SCORE_FILE_NAME = "data/scores.csv";
	ObservableList<Score> scoreList;

	static void readWordsFile(String wordsFileName) {
		StringBuilder sb = new StringBuilder();
		try {
			@SuppressWarnings("resource")
			Scanner sc = new Scanner(new File(wordsFileName));
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				if (!line.matches("^[a-zA-Z]*") || line.isEmpty()) {
					throw new InvalidWordSourceException("Check word source format!");
				}
				sb.append(line + "\n");
			}
			wordsFromFile = sb.toString().split("\n");
			String base = ""; //current directory so that find relative path 
			String relativeFilePath = new File(base).toURI().relativize(new File(wordsFileName).toURI()).getPath();
			currentWordFile= relativeFilePath;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidWordSourceException e) {
			e.showAlert();
		}

	}
	
	void writeScore(String scoreString) {
		
		
	    BufferedWriter writer = null;
		try {
			FileWriter fw = new FileWriter(SCORE_FILE_NAME,true);
	    	//BufferedWriter writer give better performance
	    	writer = new BufferedWriter(fw);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    try {
	    	writer.newLine();
			writer.write(scoreString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
	    try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//method to read the scores from scores.csv
	void readScore() {
		BufferedReader csvReader = null;
		scoreList = FXCollections.observableArrayList();
		try {
			csvReader = new BufferedReader(new FileReader(SCORE_FILE_NAME));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String row = new String();
		try {
			while ((row = csvReader.readLine()) != null) {
			    Score newScore = new Score(Integer.parseInt(row.split(",")[0].trim()),row.split(",")[1].trim(),Integer.parseInt(row.split(",")[2].trim()),Float.parseFloat(row.split(",")[3].trim()));
			    scoreList.add(newScore);			//add the scores read to the scoreslist
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			csvReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
