//Akanksha Joshi - akankshj
package hw3;


import java.text.DecimalFormat;
import java.util.Random;


public class Hangman extends Game {
	static final int MIN_WORD_LENGTH = 5; // minimum length of puzzle word
	static final int MAX_WORD_LENGTH = 10; // maximum length of puzzle word
	static final int HANGMAN_TRIALS = 10; // max number of trials in a game
	static final int HANGMAN_GAME_TIME = 30; // max time in seconds for one round of game

	HangmanRound hangmanRound;
	WordNerdModel wordNerdModelObj;
	float finalScore=0.00f;

	/**
	 * setupRound() is a replacement of findPuzzleWord() in HW1. It returns a new
	 * HangmanRound instance with puzzleWord initialized randomly drawn from
	 * wordsFromFile. The puzzleWord must be a word between HANGMAN_MIN_WORD_LENGTH
	 * and HANGMAN_MAX_WORD_LEGTH. Other properties of Hangmanround are also
	 * initialized here.
	 */
	@Override
	HangmanRound setupRound() {
		// write your code here


		hangmanRound = new HangmanRound();
		wordNerdModelObj = new WordNerdModel();
		String choosenPuzzleWord = null;
		//choosing puzzle word according to the given conditions
		while (choosenPuzzleWord == null || choosenPuzzleWord.length() < MIN_WORD_LENGTH
				|| choosenPuzzleWord.length() > MAX_WORD_LENGTH) {
			int wordCount = WordNerdModel.wordsFromFile.length;
			Random rand = new Random();
			int randomWordIndex = rand.nextInt(wordCount - 1); // Obtain a number(index value) between [0 - total words
			// - 1].
			choosenPuzzleWord = WordNerdModel.wordsFromFile[randomWordIndex];
		}
		//Setting hangmanRound properties
		hangmanRound.setPuzzleWord(choosenPuzzleWord);
		hangmanRound.setClueWord(makeAClue(hangmanRound.getPuzzleWord()));
		hangmanRound.setHitCount(0);
		hangmanRound.setMissCount(0);
		return hangmanRound;
	}

	/**
	 * Returns a clue that has at least half the number of letters in puzzleWord
	 * replaced with dashes. The replacement should stop as soon as number of dashes
	 * equals or exceeds 50% of total word length. Note that repeating letters will
	 * need to be replaced together. For example, in 'apple', if replacing p, then
	 * both 'p's need to be replaced to make it a--le
	 */
	@Override
	String makeAClue(String puzzleWord) {
		// write your code here
		// generate a clue word by randomly swapping letters of the puzzle word
		String clueWord = puzzleWord;
		int halfPuzzleWordLength = (puzzleWord.length() / 2) + 1;
		while (countDashes(clueWord) < halfPuzzleWordLength) { // run the loop till the count of dashes is equal to 50%
			// of the puzzle word length
			Random rand = new Random();
			// Obtain a number between [0 - total words - 1].
			int randomWordIndex = rand.nextInt(puzzleWord.length() - 1);
			char[] clueWordCharArray = clueWord.toCharArray();
			if (clueWordCharArray[randomWordIndex] != '_') {
				char charToReplace = clueWordCharArray[randomWordIndex];

				String sLit = Character.toString(charToReplace);
				String replacedClueWord = clueWord.replaceAll(sLit, "_");
				clueWord = replacedClueWord;
			}

		}
		return clueWord;
	}

	/** countDashes() returns the number of dashes in a clue String */
	int countDashes(String word) {
		// write your code here
		//helper function to count the number of dashes in the clue word
		int noOfDashes = 0;
		char[] wordCharArray = word.toCharArray();
		for (char c : wordCharArray) {
			if (c == '_') {
				noOfDashes++;
			}
		}
		return noOfDashes;
	}

	/**
	 * getScoreString() returns a formatted String with calculated score to be
	 * displayed after each trial in Hangman. See the handout and the video clips
	 * for specific format of the string.
	 */
	@Override
	String getScoreString() {
		// write your code here
		//function to return a formatted string to be displyed on the UI
		int hitScore = hangmanRound.getHitCount();
		int missScore = hangmanRound.getMissCount();
		if (missScore == 0) { // checking for divide by zero exception
			return "Hit: "+Integer.toString(hitScore)+" Miss: "+missScore+" Score: "+Integer.toString(hitScore);
		} else {

			finalScore = ((float) hitScore / missScore);
			//formatting to display result upto 2 decimal places
			String formattedString = String.format("%.02f", finalScore);    
			String hitMissScore = "Hit: "+hitScore+" Miss: "+missScore+" Score: "; 
			return hitMissScore+formattedString;
		}
	}

	/**
	 * nextTry() takes next guess and updates hitCount, missCount, and clueWord in
	 * hangmanRound. Returns INDEX for one of the images defined in GameView
	 * (SMILEY_INDEX, THUMBS_UP_INDEX...etc. The key change from HW1 is that because
	 * the keyboardButtons will be disabled after the player clicks on them, there
	 * is no need to track the previous guesses made in userInputs
	 */
	@Override
	int nextTry(String guess) {
		// write your code here

		// Need to compute
		// 1. Status - hence images - thumbs up/down. Smily, Cry
		// 2. Compute the score and update it

		String puzzleWord = hangmanRound.getPuzzleWord();
		char[] pWordArray = puzzleWord.toCharArray();
		String clueWord = hangmanRound.getClueWord();
		char[] cWordArray = clueWord.toCharArray();

		boolean puzzleWordContainsGuess = puzzleWord.contains(guess);


		if (puzzleWordContainsGuess) { // We know that the input is not a part of clue word or already entered before
			// at this point. Check if its a right or wrong guess.
			hangmanRound.setHitCount(hangmanRound.getHitCount() + 1); // Update hitCount as required

			for (int i = 0; i < puzzleWord.length(); i++) { // check if entered letter is at the same position in the
				// clue word and the original word
				if (cWordArray[i] == '_' && pWordArray[i] == guess.charAt(0)) {
					cWordArray[i] = pWordArray[i];
				}
			}
			hangmanRound.setClueWord(new String(cWordArray));
			// Check if the entire word is done
			if (hangmanRound.getPuzzleWord().equalsIgnoreCase(hangmanRound.getClueWord())) {
				double timeSeconds = GameView.wordTimer.timeline.getCurrentTime().toSeconds();
				double roundedVal=Math.round(timeSeconds);
				int wholeTSec = (int)roundedVal;
				String tSec = String.valueOf(wholeTSec);
				
				System.out.println(tSec);
				DecimalFormat df = new DecimalFormat("0.000000");
				String scoreString = 0+","+hangmanRound.getPuzzleWord()+","+tSec+","+df.format(finalScore);
				wordNerdModelObj.writeScore(scoreString);					//write score in the scores.csv file
				return GameView.SMILEY_INDEX;
			} else {
				return GameView.THUMBS_UP_INDEX;
			}
		} else {
			hangmanRound.setMissCount(hangmanRound.getMissCount() + 1); // update miss count as the entered letter was
			// not a part of the clueword

			if (hangmanRound.getHitCount() + hangmanRound.getMissCount() >= HANGMAN_TRIALS) {
				double timeSeconds = GameView.wordTimer.timeline.getCurrentTime().toSeconds();
				double roundedVal=Math.round(timeSeconds);
				int wholeTSec = (int)roundedVal;
				String tSec = String.valueOf(wholeTSec);
				DecimalFormat df = new DecimalFormat("0.000000");
				String scoreString = 0+","+hangmanRound.getPuzzleWord()+","+tSec+","+df.format(finalScore);
				wordNerdModelObj.writeScore(scoreString);				//write score in the scores.csv file
				return GameView.SADLY_INDEX;
			} else {
				return GameView.THUMBS_DOWN_INDEX;
			}
		}


	}

}
