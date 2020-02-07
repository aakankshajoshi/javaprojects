//Akanksha Joshi - akankshj
package hw3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class Twister {

	static int SOLUTION_LIST_COUNT = 5;
	static int TWISTER_MAX_WORD_LENGTH = 7;
	static int TWISTER_MIN_WORD_LENGTH = 3;
	static int NEW_WORD_BUTTON_INDEX = 0;
	static int TWIST_BUTTON_INDEX = 1;
	static int CLEAR_BUTTON_INDEX = 2;
	static int SUBMIT_BUTTON_INDEX = 3;
	static int CLUE_BUTTON_SIZE = 75;
	//TO DO
	static int TWISTER_GAME_TIME = 120;
	static int MIN_SOLUTION_WORDCOUNT = 10;
	TwisterRound twisterRound;
	ArrayList<String> enteredGuess;								//list to hold the answers(right/wrong) submitted by the user
	int totalSolutionWords = 0;									//total count of solution words
	int totalSubmittedWords = 0;								//total count of submitted words uptil the nextTry
	TwisterRound setupRound() {
		String choosenPuzzleWord = null;
		List<String> solutionsWordList = new ArrayList<String>();
		enteredGuess = new ArrayList<String>();
		while (choosenPuzzleWord == null || choosenPuzzleWord.length() < TWISTER_MIN_WORD_LENGTH
				|| choosenPuzzleWord.length() > TWISTER_MAX_WORD_LENGTH
				|| solutionsWordList.size() < MIN_SOLUTION_WORDCOUNT) {
			int wordCount = WordNerdModel.wordsFromFile.length;
			Random rand = new Random();
			int randomWordIndex = rand.nextInt(wordCount - 1); // Obtain a number(index value) between [0 - total words
			// - 1].
			
			choosenPuzzleWord = WordNerdModel.wordsFromFile[randomWordIndex];
			
			// Find all possible solutions from chosen word
			solutionsWordList = findAllTwisterSolutions(choosenPuzzleWord);

			twisterRound = new TwisterRound();
			//setup parameters of twisterRound
			twisterRound.setPuzzleWord(choosenPuzzleWord);

			twisterRound.setClueWord(makeAClue(twisterRound.getPuzzleWord()));
			twisterRound.setSolutionWordsList(solutionsWordList);
			for (String singleSolution : solutionsWordList) {
				twisterRound.setSolutionListsByWordLength(singleSolution);
			}
		}

		return twisterRound;

	}

	private List<String> findAllTwisterSolutions(String puzzleWord) {
		//function to generate all possible solutions for the puzzle word
		// Generate map of given input words character frequencies
		Map<Character, Integer> puzzleWordFrequencies = generateCharacterFrequencies(puzzleWord);

		// Go throught all words to find possible solutions
		final List<String> initialSolutionsList = new ArrayList();
		Arrays.asList(WordNerdModel.wordsFromFile).stream().forEach(singleWord -> {
			// Get character frequency for current word
			Map<Character, Integer> currentWordFrequency = generateCharacterFrequencies(singleWord);
			boolean canBeGeneratedFromPuzzleWord = true; // By default assume that we can
			for (Character currentCharacter : currentWordFrequency.keySet()) {
				if (!puzzleWordFrequencies.containsKey(currentCharacter)
						|| puzzleWordFrequencies.get(currentCharacter) < currentWordFrequency.get(currentCharacter)) {
					canBeGeneratedFromPuzzleWord = false;
				}
			}

			if (canBeGeneratedFromPuzzleWord) {
				initialSolutionsList.add(singleWord);
			}
		});

		// Filter to remove solutions based on length restrictions
		final List<String> solutionsList = initialSolutionsList.stream()
				.filter(singleWord -> singleWord.length() >= TWISTER_MIN_WORD_LENGTH)
				.filter(singleWord -> singleWord.length() <= TWISTER_MAX_WORD_LENGTH)
				.collect(Collectors.toList());

		return solutionsList;

	}

	//function to generate character frequency map of current word
	private Map<Character, Integer> generateCharacterFrequencies(String word) {

		Map<Character, Integer> wordFrequencies = new HashMap<>();
		for (char ch : word.toCharArray()) {
			wordFrequencies.put(ch, wordFrequencies.getOrDefault(ch, 0) + 1);
		}
		return wordFrequencies;
	}

	//function to randomly shuffle the characters of the puzzle word to generate a clue word
	String makeAClue(String puzzleWord) {
		ArrayList<Character> puzzleLetters = new ArrayList<Character>();
		ArrayList<Character> clueLetters = new ArrayList<Character>();
		for (char singleChar : puzzleWord.toCharArray()) {
			puzzleLetters.add(singleChar);
		}
		Random random = new Random();
		while (!puzzleLetters.isEmpty()) {
			int randomCharacterIndex = random.nextInt(puzzleLetters.size()); // Obtain a random letter
			clueLetters.add(puzzleLetters.get(randomCharacterIndex));
			puzzleLetters.remove(randomCharacterIndex);
		}
		String clueWord = clueLetters.stream().map(singleChar -> singleChar.toString()).collect(Collectors.joining());
		return clueWord;
	}

	//function to update the value of total solutions for a puzzle word
	int solutionWordCount() {
		for(int i=0;i<twisterRound.solutionWordsList.size();i++) {
			totalSolutionWords++;
		}
		return totalSolutionWords;

	}

	//function to update the value of total submitted words until current point for a puzzle word
	int submittedWordCount() {
		for(int i=0;i<twisterRound.submittedListsByWordLength.size();i++) {
			for(int j=0;j<twisterRound.submittedListsByWordLength.get(i).size();j++) {
				totalSubmittedWords++;
			}
		}
		return totalSubmittedWords;
	}

	//function to check which smiley index to return depending on the answer submitted bu the user
	int nextTry(String guess) {

		int totalSolWords = solutionWordCount();
		int totalSubWords = submittedWordCount();
		if(guess.length()<TWISTER_MIN_WORD_LENGTH) {
			totalSolutionWords = 0;
			totalSubmittedWords = 0;
			return GameView.THUMBS_DOWN_INDEX;
		}
		if(guess.length()>=TWISTER_MIN_WORD_LENGTH && enteredGuess.contains(guess)) {

			totalSolutionWords = 0;
			totalSubmittedWords = 0;
			return GameView.REPEAT_INDEX;

		}
		if(guess.length()>=TWISTER_MIN_WORD_LENGTH && !enteredGuess.contains(guess) && twisterRound.solutionWordsList.contains(guess) && totalSolWords-totalSubWords>1 ) {

			enteredGuess.add(guess);
			twisterRound.submittedListsByWordLength.get(guess.length() - Twister.TWISTER_MIN_WORD_LENGTH).add(guess);
			twisterRound.submittedListsByWordLength.get(guess.length() - Twister.TWISTER_MIN_WORD_LENGTH).sort(String.CASE_INSENSITIVE_ORDER);
			totalSolutionWords = 0;
			totalSubmittedWords = 0;
			return GameView.THUMBS_UP_INDEX;


		}
		if(!twisterRound.solutionWordsList.contains(guess)) {
			totalSolutionWords = 0;
			totalSubmittedWords = 0;
			return GameView.THUMBS_DOWN_INDEX;
		} 
		else if(twisterRound.solutionWordsList.contains(guess) && totalSolWords-totalSubWords==1) {
			twisterRound.submittedListsByWordLength.get(guess.length() - Twister.TWISTER_MIN_WORD_LENGTH).add(guess);
			enteredGuess.add(guess);
			totalSolutionWords = 0;
			totalSubmittedWords = 0;
			return GameView.SMILEY_INDEX;
		}
		return 0;
	}


	//function to return formatted score string to the text message
	String getScoreString() {

		int totalWordsInSolution = twisterRound.solutionWordsList.size();
		int currentScore = 0;
		for(int i=0;i<twisterRound.submittedListsByWordLength.size();i++) {
			currentScore+=twisterRound.submittedListsByWordLength.get(i).size();
		}
		int currentToDisplay = totalWordsInSolution - currentScore;
		String display = "Twist to find "+currentToDisplay+" of "+totalWordsInSolution+" words";
		return display;

	}
}
