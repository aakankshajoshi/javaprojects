//Akanksha Joshi - akankshj
package hw3;


import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class SearchController extends WordNerdController {

	//this is a placeholder class. Will be completed in HW3
	SearchView searchView = new SearchView();
	ObservableList<Score> hmList= FXCollections.observableArrayList();
	ObservableList<Score> twList= FXCollections.observableArrayList();
	ObservableList<Score> displayList = FXCollections.observableArrayList();
	
	@Override
	void startController() {
		//to be implemented in HW3
		wordNerdModel.readScore();
		searchView.setupView();
		ObservableList<String> data = FXCollections.observableArrayList("All games", "Hangman", "Twister");
		searchView.gameComboBox.setValue("All games");
		searchView.gameComboBox.getItems().addAll(data);
		displayList.clear();
		displayList = FXCollections.observableArrayList(wordNerdModel.scoreList);
		populateIndividualGameScores(wordNerdModel.scoreList);
		searchView.searchTableView.setItems(displayList);
		setupBindings();

	}

	private void populateIndividualGameScores(ObservableList<Score> scoreList) {
		//Populate Hangman scores
		scoreList.stream().filter(score -> score.getGameId()==0).forEach(score -> hmList.add(score));
		
		//Populate Twister scores
		scoreList.stream().filter(score -> score.getGameId()==1).forEach(score -> twList.add(score));
	}

	@Override
	void setupBindings() {
		//bindings for the searchTextField and gameComboBox
		searchView.gameComboBox.setOnAction(new ComboBoxSelectorEventHandler());
		searchView.searchTextField.textProperty().addListener((obs, oldText, newText) -> listeners(obs, oldText, newText, hmList, twList));
	}

	public void listeners(ObservableValue<? extends String> obs, String oldText, String newText, ObservableList<Score> hmList, ObservableList<Score> twList) {
		ObservableList<Score> currentDataList = FXCollections.observableArrayList();
		// get selection from gameComboBox and populate currentDataList accordingly
		int selection = searchView.gameComboBox.getSelectionModel().getSelectedIndex();
		switch (selection) {
		case 1: currentDataList = hmList;
		break;
		case 2: currentDataList = twList;
		break;
		default: 
		currentDataList = wordNerdModel.scoreList;
		}
		ObservableList<Score> tempList = searchScoreList(newText, currentDataList);
		displayList.clear();
		displayList = FXCollections.observableArrayList(tempList);
		searchView.searchTableView.setItems(displayList);
	}

	//helper function to return a list with words having characters from search textfield
	public static ObservableList<Score> searchScoreList(String newText, ObservableList<Score> currentDataList) {
		ObservableList<Score> tempList= FXCollections.observableArrayList();
		char[] searchText = newText.toCharArray();
		boolean flag=true;
		for(Score score : currentDataList) {	
			for(int i=0;i<searchText.length;i++) {
				int contains = score.getPuzzleWord().indexOf(searchText[i]);
				if(contains>=0) {
					flag = true;
				}
				else {
					flag = false;
					break;
				}
			}

			if (flag) {
				tempList.add(score);
			}

		}
		return tempList;
	}
	
	//inner class for implementing the event handlers for combom box
	class ComboBoxSelectorEventHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent arg0) {
			int selectionFromGameComboBox = searchView.gameComboBox.getSelectionModel().getSelectedIndex();
			ObservableList<Score> filteredList= FXCollections.observableArrayList();
			
			if(selectionFromGameComboBox == 0) {
				wordNerdModel.readScore();
				filteredList = SearchController.searchScoreList(searchView.searchTextField.getText(), wordNerdModel.scoreList);

			}else if(selectionFromGameComboBox ==1) {
				ObservableList<Score> newList= FXCollections.observableArrayList();
				wordNerdModel.readScore();
				for(Score score : wordNerdModel.scoreList) {
					if(score.getGameId()== 0) {
						newList.add(score);
					}
				}
				filteredList = SearchController.searchScoreList(searchView.searchTextField.getText(), newList);
			}else if (selectionFromGameComboBox ==2) {
				ObservableList<Score> newList= FXCollections.observableArrayList();
				wordNerdModel.readScore();
				for(Score score : wordNerdModel.scoreList) {
					if(score.getGameId()== 1) {
						newList.add(score);
					}
				}
				
				filteredList = SearchController.searchScoreList(searchView.searchTextField.getText(), newList);
			}
			displayList.clear();
			displayList = FXCollections.observableArrayList(filteredList);
			searchView.searchTableView.setItems(displayList);
		}
		
	}
}
