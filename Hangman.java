import java.util.Scanner;
import java.util.Random;
import java.util.HashSet;
import java.util.Set;

/**
 * A simple Hangman Game demonstrating basic OOP concepts:
 * - Encapsulation: State is managed within classes.
 * - Abstraction: Logic is separated from display and word selection.
 */

// Class responsible for word selection
class WordProvider {
    private static final String[] WORDS = {
            "JAVA", "PROGRAMMING", "COMPUTER", "ALGORITHM", "COMPILER",
            "OBJECT", "CLASS", "INHERITANCE", "POLYMORPHISM", "ENCAPSULATION"
    };

    public String getRandomWord() {
        Random random = new Random();
        return WORDS[random.nextInt(WORDS.length)];
    }
}

// Class representing the game state and logic
class HangmanGame {
    private String secretWord;
    private StringBuilder currentGuess;
    private int remainingAttempts;
    private Set<Character> guessedLetters;
    private static final int MAX_ATTEMPTS = 6;

    public HangmanGame(String word) {
        this.secretWord = word.toUpperCase();
        this.remainingAttempts = MAX_ATTEMPTS;
        this.guessedLetters = new HashSet<>();
        this.currentGuess = new StringBuilder();

        for (int i = 0; i < secretWord.length(); i++) {
            currentGuess.append("_");
        }
    }

    /**
     * Processes a guess.
     * 
     * @param letter The letter to guess.
     * @return 0 if already guessed, 1 if correct guess, 2 if wrong guess.
     */
    public int makeGuess(char letter) {
        letter = Character.toUpperCase(letter);
        if (guessedLetters.contains(letter)) {
            return 0; // Already guessed
        }

        guessedLetters.add(letter);
        boolean found = false;
        for (int i = 0; i < secretWord.length(); i++) {
            if (secretWord.charAt(i) == letter) {
                currentGuess.setCharAt(i, letter);
                found = true;
            }
        }

        if (!found) {
            remainingAttempts--;
            return 2; // Wrong guess
        }

        return 1; // Correct guess
    }

    public boolean isWon() {
        return currentGuess.toString().equals(secretWord);
    }

    public boolean isLost() {
        return remainingAttempts <= 0;
    }

    public String getCurrentDisplay() {
        StringBuilder display = new StringBuilder();
        for (int i = 0; i < currentGuess.length(); i++) {
            display.append(currentGuess.charAt(i)).append(" ");
        }
        return display.toString().trim();
    }

    public int getRemainingAttempts() {
        return remainingAttempts;
    }

    public String getSecretWord() {
        return secretWord;
    }
}

// Class responsible for visual display (Hangman ASCII art)
class GameRenderer {
    private static final String[] HANGMAN_PICS = {
            "  +---+\n  |   |\n      |\n      |\n      |\n      |\n=========",
            "  +---+\n  |   |\n  O   |\n      |\n      |\n      |\n=========",
            "  +---+\n  |   |\n  O   |\n  |   |\n      |\n      |\n=========",
            "  +---+\n  |   |\n  O   |\n /|   |\n      |\n      |\n=========",
            "  +---+\n  |   |\n  O   |\n /|\\  |\n      |\n      |\n=========",
            "  +---+\n  |   |\n  O   |\n /|\\  |\n /    |\n      |\n=========",
            "  +---+\n  |   |\n  O   |\n /|\\  |\n / \\  |\n      |\n========="
    };

    public void render(HangmanGame game) {
        int index = 6 - game.getRemainingAttempts();
        System.out.println(HANGMAN_PICS[index]);
        System.out.println("Word: " + game.getCurrentDisplay());
        System.out.println("Attempts left: " + game.getRemainingAttempts());
        System.out.println();
    }
}

// Main class to run the game
public class Hangman {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        WordProvider wordProvider = new WordProvider();
        GameRenderer renderer = new GameRenderer();

        System.out.println("Welcome to OOP Hangman!");
        String word = wordProvider.getRandomWord();
        HangmanGame game = new HangmanGame(word);

        while (!game.isWon() && !game.isLost()) {
            renderer.render(game);
            System.out.print("Enter your guess: ");
            String input = scanner.nextLine();

            if (input.isEmpty()) {
                continue;
            }

            char guess = input.charAt(0);
            if (!Character.isLetter(guess)) {
                System.out.println("Please enter a valid letter.");
                continue;
            }

            int result = game.makeGuess(guess);
            if (result == 0) {
                System.out.println("You already guessed that letter!");
            } else if (result == 1) {
                System.out.println("Good job! '" + guess + "' is in the word.");
            } else {
                System.out.println("Sorry, '" + guess + "' is not there.");
            }
        }

        renderer.render(game);
        if (game.isWon()) {
            System.out.println("CONGRATULATIONS! You guessed the word: " + game.getSecretWord());
        } else {
            System.out.println("GAME OVER! The word was: " + game.getSecretWord());
        }

        scanner.close();
    }
}
