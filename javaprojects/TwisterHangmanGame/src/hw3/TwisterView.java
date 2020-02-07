//Akanksha Joshi - akankshj
package hw3;

import java.util.Collections;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

public class TwisterView extends GameView {

	static final int PLAY_BUTTON_COUNT = 4; // New word, Twist, Clear, and Submit buttons
	Label[] wordLengthLabels; // shows letter count on the left of each solution list
	Label[] wordScoreLabels; // shows score for a letter count on the right of each solution list
	Button[] clueButtons; // clue buttons on the top
	Button[] answerButtons; // empty buttons below clue buttons
	Button[] playButtons; // buttons for New Word, Twise, Clear, and Submit
	ListView<String>[] solutionListViews; // lists that show he correct guesses made by the player



	TwisterView() {

		// initialize member variables
		wordLengthLabels = new Label[Twister.SOLUTION_LIST_COUNT];
		wordScoreLabels = new Label[Twister.SOLUTION_LIST_COUNT];
		playButtons = new Button[TwisterView.PLAY_BUTTON_COUNT];
		setupTopGrid();
		setupBottomGrid();
		setupSizesAlignmentsEtc();
	}

	@Override
	void setupTopGrid() {
		topGrid.add(clueGrid, 0, 0);
		topGrid.add(playButtonsGrid, 0, 2);


		// setup play buttons grid
		playButtons[0] = new Button("New Word");
		playButtons[1] = new Button("Twist");
		playButtons[2] = new Button("Clear");
		playButtons[3] = new Button("Submit");

		for (int col = 0; col < playButtons.length; col++) {
			playButtons[col].setPrefSize(120, 20);
			playButtons[col].setStyle("-fx-background-color: gray," + " linear-gradient(lightblue, gray), "
					+ " linear-gradient(lightblue 0%, white 49%, white 50%, lightblue 100%);"
					+ " -fx-background-insets: 0,1,2;");
			playButtons[col].setTextFill(Color.BLACK);
			playButtons[col].setFont(Font.font(15));

			playButtonsGrid.add(playButtons[col], col + 1, 0);
		}

		wordTimer = new WordTimer(Twister.TWISTER_GAME_TIME);
		playButtonsGrid.add(wordTimer.timerButton, 0, 0);
		playButtonsGrid.add(smileyButton, 5, 0);
	}

	@SuppressWarnings("unchecked")
	@Override
	void setupBottomGrid() {
		bottomGrid.getChildren().clear();
		bottomGrid.setAlignment(Pos.CENTER);
		bottomGrid.setVgap(5);
		bottomGrid.setMinSize(700, 300);
		// setup letter count buttons
		for (int i = 0; i < wordLengthLabels.length; i++) {
			wordLengthLabels[i] = new Label(String.format("%d", i + 3)); // starting with 3
			wordLengthLabels[i].setPrefSize(50, 50);

			wordLengthLabels[i].setStyle("-fx-font: 15px Tahoma;" + " -fx-background-color: lightgray;");
			wordLengthLabels[i].setTextFill(Color.BLACK);
			wordLengthLabels[i].setAlignment(Pos.CENTER);
			bottomGrid.add(wordLengthLabels[i], 0, i);
		}

		// setup solution lists
		solutionListViews = new ListView[Twister.SOLUTION_LIST_COUNT];
		for (int i = 0; i < solutionListViews.length; i++) {
			solutionListViews[i] = new ListView<>();
			solutionListViews[i].setOrientation(Orientation.HORIZONTAL);
			solutionListViews[i].setPrefSize(525, 50);
		}

		// setup word score buttons
		wordScoreLabels = new Label[Twister.SOLUTION_LIST_COUNT];
		for (int i = 0; i < wordScoreLabels.length; i++) {
			wordScoreLabels[i] = new Label(String.format("%d", i + 3)); // starting with 3
			wordScoreLabels[i].setPrefSize(50, 50);
			wordScoreLabels[i].setStyle("-fx-font: 15px Tahoma;" + " -fx-background-color: lightgray;");
			wordScoreLabels[i].setTextFill(Color.BLACK);
			wordScoreLabels[i].setAlignment(Pos.CENTER);
			bottomGrid.add(wordScoreLabels[i], 2, i);
		}
	}

	@Override
	void setupSizesAlignmentsEtc() {
		playButtonsGrid.setHgap(10);
		playButtonsGrid.setVgap(10);

		topGrid.setMinSize(WordNerd.GAME_SCENE_WIDTH, 200);
		topGrid.setAlignment(Pos.CENTER);
		topGrid.setHgap(10);
		topGrid.setVgap(10);

		bottomGrid.setAlignment(Pos.BASELINE_CENTER);
		clueGrid.setAlignment(Pos.CENTER);
	}

	/**
	 * refreshGameRoundView() clears up previous game round and refreshes all
	 * components with info in the new gameRound
	 */
	void refreshGameRoundView(GameRound gameRound) {
		clueGrid.getChildren().clear();
		if (gameRound instanceof TwisterRound) {
			TwisterRound twisterRound = (TwisterRound) gameRound;
			clueGrid.getChildren().clear(); // clear components from the previous game round
			bottomGrid.getChildren().clear();
			clueButtons = new Button[twisterRound.getClueWord().length()];
			answerButtons = new Button[twisterRound.getClueWord().length()];

			//set clue word characters to the clue buttons
			for (int i = 0; i < twisterRound.getClueWord().length(); i++) {
				clueButtons[i] = new Button();
				clueButtons[i].setShape(new Circle(35));
				clueButtons[i].setText(Character.toString(twisterRound.getClueWord().charAt(i)));
				clueButtons[i].setPrefSize(70, 70);
				clueGrid.add(clueButtons[i], i, 0);

			}
			//add answer button to the clue grid
			for (int i = 0; i < twisterRound.getClueWord().length(); i++) {
				answerButtons[i] = new Button();
				answerButtons[i].setShape(new Circle(35));
				answerButtons[i].setPrefSize(70, 70);
				clueGrid.add(answerButtons[i], i, 1);
			}
			topMessageText.setText("Twist to find "+twisterRound.solutionWordsList.size()+" words");

			//Setup current solutions as entered by user along with the score
			int viewCurrentIndex = 0;
			for (int i = 0; i <= twisterRound.getClueWord().length()-Twister.TWISTER_MIN_WORD_LENGTH; i++) {	
				//Re-calculate the labels

				
				Collections.sort(twisterRound.getSubmittedListsByWordLength(i));
				solutionListViews[i].setItems(twisterRound.getSubmittedListsByWordLength(i));

				wordScoreLabels[i].setText(
						String.format("%d/%d",
								twisterRound.getSubmittedListsByWordLength(i).size(),
								twisterRound.getSolutionListsByWordLength(i).size()));

				if(twisterRound.getSolutionListsByWordLength(i).size()!=0) {
					bottomGrid.add(wordLengthLabels[i], 0, viewCurrentIndex);
					bottomGrid.add(solutionListViews[i], 1, viewCurrentIndex);
					bottomGrid.add(wordScoreLabels[i], 2, viewCurrentIndex);
					viewCurrentIndex++;
				}
			}

		}
	}
}
