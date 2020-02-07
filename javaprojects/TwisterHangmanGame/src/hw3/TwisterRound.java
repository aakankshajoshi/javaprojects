//Akanksha Joshi - akankshj
package hw3;

import java.util.List;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TwisterRound extends GameRound {

	ObservableList<String> solutionWordsList;
	ObservableList<ObservableList<String>> submittedListsByWordLength;
	ObservableList<ObservableList<String>> solutionsListsByWordLength;

	//initialize lists in the constructor
	public TwisterRound() {
		// TODO Auto-generated constructor stub
		solutionWordsList = FXCollections.observableArrayList();
		submittedListsByWordLength = FXCollections.observableArrayList();
		solutionsListsByWordLength = FXCollections.observableArrayList();
		for(int i= Twister.TWISTER_MIN_WORD_LENGTH;i<=Twister.TWISTER_MAX_WORD_LENGTH;i++) {
			submittedListsByWordLength.add(FXCollections.observableArrayList());
			solutionsListsByWordLength.add(FXCollections.observableArrayList());
		}
	}
	//below are the getter setter methods for accessing and updating the values of the member variable lists
	public void setSolutionWordsList(List<String> solutionWordsList) {
		this.solutionWordsList = FXCollections.observableArrayList(solutionWordsList);
	}

	public List<String> getSolutionWordsList(){
		return solutionWordsList;

	}

	public ObservableList<String> solutionWordsListProperty(){
		return solutionWordsList;

	}

	public void setSubmittedListsByWordLength(String word) {
		int wordLength = word.length();
		this.submittedListsByWordLength.get(wordLength-Twister.TWISTER_MIN_WORD_LENGTH).add(word);
	}

	public ObservableList<String> getSubmittedListsByWordLength(int letterCount){
		return submittedListsByWordLength.get(letterCount);

	}

	public ObservableList<ObservableList<String>> submittedListsByWordLengthProperty(){
		return submittedListsByWordLength;

	}

	public void setSolutionListsByWordLength(String word) {
		int wordLength = word.length();
		this.solutionsListsByWordLength.get(wordLength-Twister.TWISTER_MIN_WORD_LENGTH).add(word);
	}

	public ObservableList<String> getSolutionListsByWordLength(int letterCount){
		return solutionsListsByWordLength.get(letterCount);

	}

	public ObservableList<ObservableList<String>> getSolutionListsByWordLength(){	
		return solutionsListsByWordLength;		
	}

	public ObservableList<ObservableList<String>> SolutionsListsByWordLengthProperty(){
		return solutionsListsByWordLength;

	}

}
