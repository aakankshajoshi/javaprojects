//Akanksha Joshi - akankshj
package hw3;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class TwisterController extends WordNerdController {

	TwisterView twisterView;
	Twister twister;
	

	@Override
	void startController() {
		// TODO Auto-generated method stub
		//start a new game and initialize the UI
		twister = new Twister();
		twisterView = new TwisterView();
		twister.twisterRound = twister.setupRound();
		twisterView.refreshGameRoundView(twister.twisterRound);
		setupBindings();
		VBox lowerPanel = new VBox();
		lowerPanel.getChildren().add(twisterView.bottomGrid);
		lowerPanel.getChildren().add(WordNerd.exitButton);
		lowerPanel.setAlignment(Pos.CENTER);

		WordNerd.root.setTop(twisterView.topMessageText);
		WordNerd.root.setCenter(twisterView.topGrid);
		WordNerd.root.setBottom(lowerPanel);
	}

	@Override
	void setupBindings() {
		// attaching handlers to play buttons, clue buttons, answer buttons, timer button
		twisterView.playButtons[0].setOnAction(new NewButtonHandler());
		twisterView.playButtons[1].setOnAction(new TwistButtonHandler());
		twisterView.playButtons[2].setOnAction(new ClearButtonHandler());
		twisterView.playButtons[3].setOnAction(new SubmitButtonHandler());
		twister.twisterRound.clueWordProperty().addListener((observable, oldValue, newValue)->{
			for (int i = 0; i < twister.twisterRound.getClueWord().length(); i++) {
				twisterView.clueButtons[i].setText(String.format("%s", newValue.charAt(i)));
			}
		});
		for (int i = 0; i < twister.twisterRound.getClueWord().length(); i++) {
			twisterView.clueButtons[i].setOnAction(new ClueButtonHandler());
			twisterView.answerButtons[i].setOnAction(new AnswerButtonHandler());
		}

		//to stop the game when the timer hits zero
		twisterView.wordTimer.timerButton.textProperty().addListener((observable, oldValue, newValue)->{
			if(newValue.equalsIgnoreCase("0")) {
				for(int i=1;i<twisterView.playButtons.length;i++) {
					twisterView.playButtons[i].setDisable(true);
				}

				for(int i=0;i<twisterView.clueButtons.length;i++) {
					twisterView.clueButtons[i].setDisable(true);
				}

				for(int i=0;i<twisterView.answerButtons.length;i++) {
					twisterView.answerButtons[i].setDisable(true);
				}
				String pWord = twister.twisterRound.getPuzzleWord();
				int totalWordsInSolution = twister.twisterRound.solutionWordsList.size();
				int cntOfCorrectWordsSubmitted = 0;
				for(int i=0;i<twister.twisterRound.submittedListsByWordLength.size();i++) {
					cntOfCorrectWordsSubmitted+=twister.twisterRound.submittedListsByWordLength.get(i).size();
				}
				float twisterScore =  ((float)cntOfCorrectWordsSubmitted/totalWordsInSolution);
				DecimalFormat df = new DecimalFormat("0.000000");
				double timeSeconds = GameView.wordTimer.timeline.getCurrentTime().toSeconds();
				double roundedVal=Math.round(timeSeconds);
				int wholeTSec = (int)roundedVal;
				String tSec = String.valueOf(wholeTSec);
				String score = "1"+","+pWord+","+tSec+","+df.format(twisterScore);
				wordNerdModel.writeScore(score);								//write to scores.csv file
				twisterView.smileyButton.setGraphic(twisterView.smileyImageViews[GameView.SADLY_INDEX]);
			}
		});

	}

	class TwistButtonHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent arg0) {
			// TODO Auto-generated method stub
			StringBuilder existingClueLetters = new StringBuilder();
			for(int i=0;i<twisterView.clueButtons.length;i++) {
				existingClueLetters.append(twisterView.clueButtons[i].getText());
			}
			String originalClueWord = twister.twisterRound.getClueWord();
			String currentClueWordTemp = existingClueLetters.toString();
			if(originalClueWord.length()>currentClueWordTemp.length()) {
				int originalClueWordLength = originalClueWord.length();
				int currentClueWordLength = currentClueWordTemp.length();
				for(int i=currentClueWordLength;i<originalClueWordLength;i++) {
					existingClueLetters.append(" ");
				}
			}
			String currentClueWord = existingClueLetters.toString();
			ArrayList<Character> clueLetters = new ArrayList<Character>();
			ArrayList<Character> tempLetters = new ArrayList<Character>();
			for (char singleChar : currentClueWord.toCharArray()) {
				tempLetters.add(singleChar);
			}
			//randomly shuffle the letters of the clue word and assign it back to the clue word
			Random random = new Random();
			while (!tempLetters.isEmpty()) {
				int randomCharacterIndex = random.nextInt(tempLetters.size()); // Obtain a random letter
				clueLetters.add(tempLetters.get(randomCharacterIndex));
				tempLetters.remove(randomCharacterIndex);
			}
			String clueWord = clueLetters.stream().map(singleChar -> singleChar.toString()).collect(Collectors.joining());
			StringBuilder dashAtEndConvert = new StringBuilder();
			int counterDash=0;
			for (int i = 0; i < clueWord.length(); i++) {
				if(clueWord.charAt(i)==' ') {
					counterDash++;
				}
				else {
					dashAtEndConvert.append(clueWord.charAt(i));
				}
			}
			for(int i=0;i<counterDash;i++) {
				dashAtEndConvert.append(" ");
			}
			String finalClueWord = dashAtEndConvert.toString();
			for (int i = 0; i < finalClueWord.length(); i++) {
				twisterView.clueButtons[i].setText(Character.toString(finalClueWord.charAt(i)));
			}


		}

	}

	class ClueButtonHandler implements EventHandler<ActionEvent>{


		@Override
		public void handle(ActionEvent arg0) {
			// TODO Auto-generated method stub
			Button b = (Button)arg0.getSource();
			String clickedClueLetter = b.getText();			//get character on the clicked clue button

			for(int i=0;i<twisterView.answerButtons.length;i++) {
				if(twisterView.answerButtons[i].getText().length() == 0) {
					twisterView.answerButtons[i].setText(clickedClueLetter);	//assign the clicked clue character to the next answer button
					break;
				}
			}

			b.setText("");									//set clue button to empty string
		}

	}

	class AnswerButtonHandler implements EventHandler<ActionEvent>{


		@Override
		public void handle(ActionEvent arg0) {
			// TODO Auto-generated method stub
			Button b = (Button)arg0.getSource();
			String clickedAnsLetter = b.getText();		//get character on the clicked answer button
			for(int i=0;i<twisterView.clueButtons.length;i++) {
				if(twisterView.clueButtons[i].getText().length() == 0 || twisterView.clueButtons[i].getText().equalsIgnoreCase(" ") ) {
					twisterView.clueButtons[i].setText(clickedAnsLetter);	//assign the clicked answer character to the next clue button
					break;
				}
			}
			b.setText("");			//set answer button to empty string
		}

	}

	class ClearButtonHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent arg0) {
			// TODO Auto-generated method stub
			String clueWord = twister.twisterRound.getClueWord();
			for(int i=0;i<twisterView.answerButtons.length;i++) {
				String ansButtonContent = twisterView.answerButtons[i].getText();
				if(ansButtonContent!=null) {
					twisterView.answerButtons[i].setText("");		//set all answer buttons having text to empty string
					twisterView.clueButtons[i].setText(Character.toString(clueWord.charAt(i))); // set the clue button with characters from clue word
				}
				else {
					break;
				}

			}

		}

	}

	class SubmitButtonHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent arg0) {
			// TODO Auto-generated method stub
			StringBuilder answerButtonContent = new StringBuilder();
			for(int i=0;i<twisterView.answerButtons.length;i++) {
				answerButtonContent.append(twisterView.answerButtons[i].getText());
			}
			int index = twister.nextTry(answerButtonContent.toString());		// call next try for checking if the entered answer is correct
			//if user losses disable the UI
			if(index == 0) {
				for(int i=1;i<twisterView.playButtons.length;i++) {
					twisterView.playButtons[i].setDisable(true);
				}

				for(int i=0;i<twisterView.clueButtons.length;i++) {
					twisterView.clueButtons[i].setDisable(true);
				}
				for(int i=0;i<twisterView.answerButtons.length;i++) {
					twisterView.answerButtons[i].setDisable(true);
				}
				String pWord = twister.twisterRound.getPuzzleWord();
				int totalWordsInSolution = twister.twisterRound.solutionWordsList.size();
				int cntOfCorrectWordsSubmitted = 0;
				for(int i=0;i<twister.twisterRound.submittedListsByWordLength.size();i++) {
					cntOfCorrectWordsSubmitted+=twister.twisterRound.submittedListsByWordLength.get(i).size();
				}
				float twisterScore =  ((float)cntOfCorrectWordsSubmitted/totalWordsInSolution);
				//int timeInSec=Integer.parseInt(GameView.wordTimer.timeline.getCurrentTime());
				double timeSeconds = GameView.wordTimer.timeline.getCurrentTime().toSeconds();
				double roundedVal=Math.round(timeSeconds);
				int wholeTSec = (int)roundedVal;
				String tSec = String.valueOf(wholeTSec);
				DecimalFormat df = new DecimalFormat("0.000000");
				String score = "1"+","+pWord+","+tSec+","+df.format(twisterScore);
				wordNerdModel.writeScore(score);							//write the score to the scores.csv file
				GameView.wordTimer.timeline.stop();
			}
			//if user guesses the correct word update the score text
			if(index == 1 || index == 0) {
				twisterView.topMessageText.setText(twister.getScoreString());
				int currentIndex = answerButtonContent.length()-twister.TWISTER_MIN_WORD_LENGTH;
				twisterView.wordScoreLabels[currentIndex].setText(
						String.format("%d/%d",
								twister.twisterRound.getSubmittedListsByWordLength(currentIndex).size(),
								twister.twisterRound.getSolutionListsByWordLength(currentIndex).size()));
				if(index==0) {
					String pWord = twister.twisterRound.getPuzzleWord();
					int totalWordsInSolution = twister.twisterRound.solutionWordsList.size();
					int cntOfCorrectWordsSubmitted = 0;
					for(int i=0;i<twister.twisterRound.submittedListsByWordLength.size();i++) {
						cntOfCorrectWordsSubmitted+=twister.twisterRound.submittedListsByWordLength.get(i).size();
					}
					float twisterScore =  ((float)cntOfCorrectWordsSubmitted/totalWordsInSolution);
					double timeSeconds = GameView.wordTimer.timeline.getCurrentTime().toSeconds();
					double roundedVal=Math.round(timeSeconds);
					int wholeTSec = (int)roundedVal;
					String tSec = String.valueOf(wholeTSec);
					DecimalFormat df = new DecimalFormat("0.000000");
					String score = "1"+","+pWord+","+tSec+","+df.format(twisterScore);
					wordNerdModel.writeScore(score);						//write the score to the scores.csv file
				}
			}
			twisterView.smileyButton.setGraphic(twisterView.smileyImageViews[index]);
			for(int i=0;i<twisterView.answerButtons.length;i++) {
				twisterView.answerButtons[i].setText("");
				//twisterView.clueButtons[i].setText(Character.toString(twister.twisterRound.getClueWord().charAt(i)));
			}
			
			String currentClueWord = twister.twisterRound.getClueWord();
			ArrayList<Character> clueLetters = new ArrayList<Character>();
			ArrayList<Character> tempLetters = new ArrayList<Character>();
			for (char singleChar : currentClueWord.toCharArray()) {
				tempLetters.add(singleChar);
			}
			//randomly shuffle the letters of the clue word and assign it back to the clue word
			Random random = new Random();
			while (!tempLetters.isEmpty()) {
				int randomCharacterIndex = random.nextInt(tempLetters.size()); // Obtain a random letter
				clueLetters.add(tempLetters.get(randomCharacterIndex));
				tempLetters.remove(randomCharacterIndex);
			}
			String clueWord = clueLetters.stream().map(singleChar -> singleChar.toString()).collect(Collectors.joining());
			for (int i = 0; i < clueWord.length(); i++) {
				twisterView.clueButtons[i].setText(Character.toString(clueWord.charAt(i)));
			}

		}

	}

	class NewButtonHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent arg0) {
			//start a new game and enable UI components from the previous game
			GameView.wordTimer.restart(Twister.TWISTER_GAME_TIME);
			for(int i=1;i<twisterView.playButtons.length;i++) {
				twisterView.playButtons[i].setDisable(false);
			}

			for(int i=1;i<twisterView.clueButtons.length;i++) {
				twisterView.clueButtons[i].setDisable(false);
			}
			for(int i=0;i<twisterView.answerButtons.length;i++) {
				twisterView.answerButtons[i].setDisable(false);
			}
			twisterView.smileyButton.setGraphic(twisterView.smileyImageViews[GameView.SMILEY_INDEX]);
			startController();
		}

	}
}
