//Akanksha Joshi akankshj
package hw3;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

//java class to having property member variables of Score
public class Score {

	private IntegerProperty gameId=new SimpleIntegerProperty();
	private StringProperty puzzleWord = new SimpleStringProperty();
	private IntegerProperty timeStamp = new SimpleIntegerProperty(); 
	private FloatProperty score = new SimpleFloatProperty(); 
	
	public Score() {
	}
	
	//parameterised constructor
	public Score(int gameid,String puzzleWord,int timeStamp,float score) {
		this.gameId.set(gameid);
		this.puzzleWord.set(puzzleWord);
		this.timeStamp.set(timeStamp);
		this.score.set(score);
	}
	
	//getter setters
	public int getGameId() {
		return this.gameId.get();
	}
	
	public void setGameId(int gameid) {
		this.gameId.set(gameid);
	}
	
	public String getPuzzleWord() {
		return this.puzzleWord.get();
	}
	
	public void setPuzzleWord(String puzzleWord) {
		this.puzzleWord.set(puzzleWord);
	}
	
	public int getTimeStamp() {
		return this.timeStamp.get();
	}
	
	public void setTimeStamp(int timeStamp) {
		this.timeStamp.set(timeStamp);
	}
	
	public float getScore() {
		return this.score.get();
	}
	
	public void setScore(float score) {
		this.score.set(score);
		
	}
}
