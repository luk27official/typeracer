package cz.lukaspolak.typeracer;

import org.json.JSONObject;
import org.json.JSONTokener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * This class contains the main game logic.
 * It is responsible for the game state and UI.
 */
public class Game extends JFrame {
    /**
     * The main game panel.
     */
    private JPanel gamePanel;
    /**
     * Button to start the game.
     */
    private JButton startButton;
    /**
     * The text area where the user types.
     */
    private JTextField inputTextField;
    /**
     * WPM label.
     */
    private JLabel wpmLabel;
    /**
     * Typing accuracy label.
     */
    private JLabel accuracyLabel;
    /**
     * Upper game label.
     */
    private JLabel upperLabel;
    /**
     * The text area where the text is displayed.
     */
    private JTextPane textPane;
    /**
     * Timer for the game.
     */
    private JLabel timerLabel;
    /**
     * Button to get back to the main menu.
     */
    private JButton backToMenuButton;

    /**
     * Counter which holds the total number of characters typed.
     */
    private int writtenChars;
    /**
     * Field helping the timer to count the elapsed seconds.
     */
    private int secondsElapsed;
    /**
     * Typing accuracy.
     */
    private double accuracy;
    /**
     * A list containing remaining words to be typed.
     */
    private ArrayList<String> wordsRemaining;
    /**
     * A list containing already typed words.
     */
    private ArrayList<String> wordsCompleted;
    /**
     * A boolean indicating whether the game is running.
     */
    private boolean playing;
    /**
     * A timer to control the game events. Multiple timers are used during the game.
     */
    private Timer timer;
    /**
     * An instance of the Statistics class to manage the statistics.
     */
    private final Statistics statistics;

    /**
     * A boolean indicating whether a space is required as the next character.
     */
    private boolean waitingForSpace;

    /**
     * A constructor for the Game class.
     */
    public Game() {
        super();
        this.statistics = new Statistics();
    }

    /**
     * This method is called when the game is started.
     * It initializes the game and sets up all the fields.
     */
    private void startGame() {
        String text = getRandomText();
        String[] words = text.split(" ");

        wordsRemaining = new ArrayList<>(Arrays.asList(words));
        wordsCompleted = new ArrayList<>();
        accuracy = 0;
        writtenChars = 0;
        secondsElapsed = 0;
        waitingForSpace = false;

        textPane.setText(text);
        textPane.setForeground(Color.BLACK);

        inputTextField.setText("");

        timer = new Timer(1000, e -> handleGameTimer());
        timer.start();

        playing = true;
    }

    /**
     * This method opens a file with texts to be typed.
     * Then it randomly selects one of the texts and returns it.
     * @return a random text to be typed
     */
    private String getRandomText() {
        try (InputStream in = getClass().getResourceAsStream(Constants.TEXTS_FILE)) {
            if(in == null) {
                throw new IOException(Constants.NOT_FOUND_ERROR);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            JSONObject obj = new JSONObject(new JSONTokener(reader));
            String[] texts = obj.getJSONArray(Constants.JSON_TEXTS_KEY).toList().toArray(new String[0]);

            return texts[(int) (Math.random() * texts.length)];
        }
        catch (IOException e) {
            System.err.printf((Constants.JSON_ERROR) + "%n", e.getMessage());
            return "An error occurred. But don't worry, you can still play the game with this great sample text. Have fun and make sure to fix the problem (provide a valid JSON file with texts)!";
        }
    }

    /**
     * This method is called when the game timer ticks.
     * It updates the timer label during the game.
     */
    private void handleGameTimer() {
        secondsElapsed++;

        int hours = secondsElapsed / 3600;
        int minutes = (secondsElapsed % 3600) / 60;
        int seconds = secondsElapsed % 60;

        if(hours < 1) {
            timerLabel.setText(String.format(Constants.TIME_FORMAT_MINUTES_SECONDS, minutes, seconds));
        }
        else {
            timerLabel.setText(String.format(Constants.TIME_FORMAT_HOURS_MINUTES_SECONDS, hours, minutes, seconds));
        }
    }

    /**
     * This method is called when the user types a character.
     * It updates the game text area to be colored property.
     *
     * @param color color of the words which have not been typed
     */
    private void changeTextColor(Color color) {
        try {
            textPane.setText("");
            StyledDocument doc = textPane.getStyledDocument();
            Style style = textPane.addStyle(null, null);
            StyleConstants.setForeground(style, Color.GREEN);
            doc.insertString(doc.getLength(), String.join(" ", wordsCompleted), style);

            if(wordsCompleted.size() > 0) {
                doc.insertString(doc.getLength(), " ", style);
            }

            Style style2 = textPane.addStyle(null, null);
            StyleConstants.setForeground(style2, color);
            doc.insertString(doc.getLength(),  String.join(" ", wordsRemaining), style2);
        }
        catch (BadLocationException e) {
            this.textPane.setText(Constants.TEXT_FORMAT_ERROR);
            System.err.println(Constants.TEXT_FORMAT_ERROR);
        }
    }

    /**
     * This method is responsible for getting common characters in two given strings.
     * @param s1 first string
     * @param s2 second string
     * @return array of common characters
     */
    private Character[] getCommonCharactersInTwoStrings(String s1, String s2) {
        HashSet<Character> h1 = new HashSet<>(), h2 = new HashSet<>();
        for(int i = 0; i < s1.length(); i++)
        {
            h1.add(s1.charAt(i));
        }

        for(int i = 0; i < s2.length(); i++)
        {
            h2.add(s2.charAt(i));
        }

        h1.retainAll(h2);
        return h1.toArray(new Character[0]);
    }

    /**
     * This method counts the total number of characters in the completed words.
     * @return total number of characters in the completed words
     */
    private double getCompletedWordsCharactersCount() {
        double count = 0;
        for(String word : wordsCompleted) {
            count += word.length();
        }
        return count;
    }

    /**
     * This method calculates the WPM metric.
     * @return WPM metric
     */
    private double calculateWPM() {
        if(secondsElapsed == 0) {
            return 0;
        }

        // the calculation above is based on the following formula:
        // ((written characters + spaces) / 5) / (seconds / 60)
        // 5 is the average word length, standardized
        // Proceedings of the IEEE Toronto International Conference–Science and Technology for Humanity (TIC-STH '09). IEEE, Washington, D.C., US, pp. 100-105.
        return ((getCompletedWordsCharactersCount() + wordsCompleted.size()) / 5) / (secondsElapsed / 60.0);
    }

    /**
     * This method is called when the user typed the entire text.
     * It stops the game and shows the statistics.
     */
    private void gameOver() {
        double wpm = calculateWPM();

        textPane.setText(String.format(Constants.GAME_FINISHED, secondsElapsed, wpm, accuracy*100));
        textPane.setForeground(Color.BLACK);
        playing = false;
        timer.stop();

        statistics.saveStatistics(wpm, accuracy);
    }

    /**
     * This method is called when the user presses a key.
     * It gets the current input and based on the input it updates the game.
     */
    private void checkWord() {
        if(!playing) {
            return;
        }

        if(wordsRemaining.size() == 0) {
            gameOver();
            return;
        }

        String input = inputTextField.getText();
        String currentWord = wordsRemaining.get(0);

        //input = input.trim();

        if(input.length() != 0) {
            writtenChars++;
        }

        if(input.length() > 0 && waitingForSpace) {
            if(input.charAt(0) != ' ') {
                inputTextField.setForeground(Color.RED);
                changeTextColor(Color.RED);
            }
            else {
                inputTextField.setText("");
                waitingForSpace = false;
            }
            updateStats(currentWord, input);
            return;
        }

        if(input.equals(currentWord)) {
            inputTextField.setForeground(Color.BLACK);
            inputTextField.setText("");

            changeTextColor(Color.BLACK);

            wordsRemaining.remove(0);
            wordsCompleted.add(currentWord);

            waitingForSpace = true;
            currentWord = "";
        }
        else if(currentWord.startsWith(input)) {
            inputTextField.setForeground(Color.BLACK);

            changeTextColor(Color.BLACK);
        }
        else {
            inputTextField.setForeground(Color.RED);

            changeTextColor(Color.RED);
        }

        updateStats(currentWord, input);
    }

    /**
     * This method updates the current game statistics (in the GUI).
     * @param currentWord current word
     * @param input current input
     */
    private void updateStats(String currentWord, String input) {
        wpmLabel.setText(String.format(Constants.WPM_INFO, calculateWPM()));

        // accuracy between [0, 1]
        accuracy = (getCompletedWordsCharactersCount() + wordsCompleted.size() + getCommonCharactersInTwoStrings(currentWord, input).length) / (double) writtenChars;
        if(accuracy > 1) { //may happen at the end of the game, because of the non-existent space at the end of the last word
            accuracy = 1;
        }
        accuracyLabel.setText(String.format(Constants.ACCURACY_INFO, accuracy * 100));
    }

    /**
     * This method is invoked by the timer before the game starts.
     */
    private void handleReadyTimer() {
        secondsElapsed++;
        if(secondsElapsed < Constants.START_COUNTDOWN) {
            textPane.setText(String.format(Constants.READY_MESSAGE, Constants.START_COUNTDOWN - secondsElapsed));
        }
        else {
            textPane.setText("");
            timer.stop();
            startGame();
        }
    }

    /**
     * This method handles the click on the start button.
     * If not in game, it starts a timer which counts down before the game starts.
     */
    private void handleStartBtn() {
        if(playing) {
            return;
        }

        secondsElapsed = 0;

        textPane.setText(String.format(Constants.READY_MESSAGE, Constants.START_COUNTDOWN));

        timer = new Timer(1000, e1 -> handleReadyTimer());
        timer.start();
    }

    /**
     * This method handles the click on the "Back to menu" button.
     */
    private void handleBackToMenuBtn() {
        this.dispose();
        MainMenu menu = new MainMenu();
        menu.createUIComponents();
    }

    /**
     * This method creates the game form, sets up all the event handlers and other window properties.
     * Then the window is shown to the user.
     */
    public void createUIComponents() {
        this.setContentPane(this.gamePanel);
        this.setTitle(Constants.GAME_TITLE);
        this.setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.textPane.setFont(new Font(Constants.DEFAULT_FONT, Font.PLAIN, Constants.GAME_FONT_SIZE));
        this.inputTextField.setFont(new Font(Constants.DEFAULT_FONT, Font.PLAIN, Constants.TEXTINPUT_FONT_SIZE));

        startButton.addActionListener(e -> handleStartBtn());
        backToMenuButton.addActionListener(e -> handleBackToMenuBtn());

        inputTextField.getDocument().addDocumentListener(new DocumentListener() {
            final Runnable checkWord = () -> checkWord();

            public void changedUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(checkWord);
            }
            public void removeUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(checkWord);
            }
            public void insertUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(checkWord);
            }
        });

        this.setVisible(true);
    }
}
