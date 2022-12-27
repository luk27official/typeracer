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

public class Game extends JFrame {
    private JPanel gamePanel;
    private JButton startButton;
    private JTextField inputTextField;
    private JLabel wpmLabel;
    private JLabel accuracyLabel;
    private JLabel upperLabel;
    private JTextPane textPane;
    private JLabel timerLabel;
    private JButton backToMenuButton;

    private int writtenChars;
    private int secondsElapsed;
    private double accuracy;
    private ArrayList<String> wordsRemaining;
    private ArrayList<String> wordsCompleted;
    private boolean playing;
    private Timer timer;
    private Statistics statistics;

    private void startGame() {
        String text = getRandomText();
        String[] words = text.split(" ");

        wordsRemaining = new ArrayList<>(Arrays.asList(words));
        wordsCompleted = new ArrayList<>();
        accuracy = 0;
        writtenChars = 0;
        secondsElapsed = 0;

        textPane.setText(text);
        textPane.setForeground(Color.BLACK);

        inputTextField.setText("");

        statistics = new Statistics();

        timer = new Timer(1000, e -> handleGameTimer());
        timer.start();

        playing = true;
    }

    private String getRandomText() {
        try (InputStream in = getClass().getResourceAsStream(Constants.TEXTS_FILE)) {
            if(in == null) {
                throw new IOException("Resource not found.");
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            JSONObject obj = new JSONObject(new JSONTokener(reader));
            String[] texts = obj.getJSONArray(Constants.JSON_TEXTS_KEY).toList().toArray(new String[0]);

            return texts[(int) (Math.random() * texts.length)];
        }
        catch (IOException e) {
            System.err.println("Error while reading JSON file: " + e.getMessage());
            return "An error occurred. But don't worry, you can still play the game with this great sample text. Have fun and make sure to fix the problem (provide a valid JSON file with texts)!";
        }
    }

    private void handleGameTimer() {
        secondsElapsed++;

        int hours = secondsElapsed / 3600;
        int minutes = (secondsElapsed % 3600) / 60;
        int seconds = secondsElapsed % 60;

        if(hours < 1) {
            timerLabel.setText(String.format("Time: %02d:%02d", minutes, seconds));
        }
        else {
            timerLabel.setText(String.format("Time: %02d:%02d:%02d", hours, minutes, seconds));
        }
    }

    private void changeTextColor(Color firstColor, Color secondColor) {
        try {
            textPane.setText("");
            StyledDocument doc = textPane.getStyledDocument();
            Style style = textPane.addStyle(null, null);
            StyleConstants.setForeground(style, firstColor);
            doc.insertString(doc.getLength(), String.join(" ", wordsCompleted), style);

            if(wordsCompleted.size() > 0) {
                doc.insertString(doc.getLength(), " ", style);
            }

            Style style2 = textPane.addStyle(null, null);
            StyleConstants.setForeground(style2, secondColor);
            doc.insertString(doc.getLength(),  String.join(" ", wordsRemaining), style2);
        }
        catch (BadLocationException e) {
            this.textPane.setText("An error occurred while changing text color. Please restart the game.");
            System.err.println("An error occurred while changing text color in the text pane.");
        }
    }

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

    private double getCompletedWordsCharactersCount() {
        double count = 0;
        for(String word : wordsCompleted) {
            count += word.length();
        }
        return count;
    }

    private double calculateWPM() {
        if(secondsElapsed == 0) {
            return 0;
        }

        double wpm = (getCompletedWordsCharactersCount() / 5) / (secondsElapsed / 60.0);
        // 5 is the average word length, standardized
        // Proceedings of the IEEE Toronto International Conference–Science and Technology for Humanity (TIC-STH '09). IEEE, Washington, D.C., US, pp. 100-105.
        return wpm;
    }

    private void gameOver() {
        double wpm = calculateWPM();

        textPane.setText(String.format("You have finished the game in %d seconds with %.1f WPM and %.2f%% accuracy.", secondsElapsed, wpm, accuracy*100));
        textPane.setForeground(Color.BLACK);
        playing = false;
        timer.stop();

        statistics.saveStatistics(wpm, accuracy);
    }

    private void checkWord() {
        if(!playing) {
            return;
        }

        if(wordsRemaining.size() == 0) {
            gameOver();
            return;
        }

        String input = inputTextField.getText();

        input = input.trim();

        if(input.length() != 0) {
            writtenChars++;
        }

        String currentWord = wordsRemaining.get(0);

        if(input.equals(currentWord)) {
            inputTextField.setForeground(Color.BLACK);
            inputTextField.setText("");

            changeTextColor(Color.GREEN, Color.BLACK);

            wordsRemaining.remove(0);
            wordsCompleted.add(currentWord);

            currentWord = "";
        }
        else if(currentWord.startsWith(input)) {
            inputTextField.setForeground(Color.BLACK);

            changeTextColor(Color.GREEN, Color.BLACK);
        }
        else {
            inputTextField.setForeground(Color.RED);

            changeTextColor(Color.GREEN, Color.RED);
        }

        updateStats(currentWord, input);
    }

    private void updateStats(String currentWord, String input) {
        wpmLabel.setText(String.format("WPM: %.1f", calculateWPM()));

        // accuracy between [0, 1]
        accuracy = (getCompletedWordsCharactersCount() + getCommonCharactersInTwoStrings(currentWord, input).length) / (double) writtenChars;
        if(accuracy <= 1) {
            accuracyLabel.setText(String.format("Accuracy: %.2f%%", accuracy * 100));
        }
    }

    private void handleReadyTimer(int countdown, String readyMessage) {
        secondsElapsed++;
        if(secondsElapsed < countdown) {
            textPane.setText(String.format(readyMessage, countdown - secondsElapsed));
        }
        else {
            textPane.setText("");
            timer.stop();
            startGame();
        }
    }

    private void handleStartBtn() {
        if(playing) {
            return;
        }

        secondsElapsed = 0;
        int countdown = 3;

        String readyMessage = "Get ready in %d...";
        textPane.setText(String.format(readyMessage, countdown));

        timer = new Timer(1000, e1 -> handleReadyTimer(countdown, readyMessage));
        timer.start();
    }

    private void handleBackToMenuBtn() {
        this.dispose();
        MainMenu menu = new MainMenu();
        menu.createUIComponents();
    }

    public void createUIComponents() {
        this.setContentPane(this.gamePanel);
        this.setTitle(Constants.GAME_TITLE);
        this.setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.textPane.setFont(new Font(Constants.DEFAULT_FONT, Font.PLAIN, 20));
        this.inputTextField.setFont(new Font(Constants.DEFAULT_FONT, Font.PLAIN, 15));

        startButton.addActionListener(e -> handleStartBtn());
        backToMenuButton.addActionListener(e -> handleBackToMenuBtn());

        inputTextField.getDocument().addDocumentListener(new DocumentListener() {
            Runnable checkWord = () -> checkWord();

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
