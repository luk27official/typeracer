package cz.lukaspolak.typeracer;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
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

    private int writtenChars;
    private int secondsElapsed;
    private double accuracy;
    private ArrayList<String> wordsRemaining;
    private ArrayList<String> wordsCompleted;
    private boolean playing;
    private Timer timer;

    private String getRandomText() {
        // TODO: add more texts
        return "This is a sample text.";
    }

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

        // start the timer
        timer = new Timer(1000, e -> {
            secondsElapsed++;

            int hours = secondsElapsed / 3600;
            int minutes = (secondsElapsed % 3600) / 60;
            int seconds = secondsElapsed % 60;

            if(hours < 1) {
                timerLabel.setText(String.format("Time: %02d:%02d", minutes, seconds));
            } else {
                timerLabel.setText(String.format("Time: %02d:%02d:%02d", hours, minutes, seconds));
            }
        });

        timer.start();

        playing = true;
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
            System.out.println("An error occurred while inserting text into the text pane.");
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

    private void checkWord() {
        if(!playing) {
            return;
        }

        if(wordsRemaining.size() == 0) {
            textPane.setText(String.format("You have finished the game in %d seconds with %.1f WPM and %.2f%% accuracy.", secondsElapsed, calculateWPM(), accuracy*100));
            textPane.setForeground(Color.BLACK);
            playing = false;
            timer.stop();
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

        accuracy = (getCompletedWordsCharactersCount() + getCommonCharactersInTwoStrings(currentWord, input).length) / (double) writtenChars;
        if(accuracy <= 1) {
            accuracyLabel.setText(String.format("Accuracy: %.2f%%", accuracy * 100));
        }
    }

    public void createUIComponents() {
        this.setContentPane(this.gamePanel);
        this.setTitle("TypeRacer Game");
        this.setSize(500, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        this.textPane.setFont(new Font("Arial", Font.PLAIN, 20));
        this.inputTextField.setFont(new Font("Arial", Font.PLAIN, 15));

        startButton.addActionListener(e -> {
            secondsElapsed = 0;
            int countdown = 3;
            textPane.setText(String.format("Get ready in %d...", countdown));

            timer = new Timer(1000, e1 -> {
                secondsElapsed++;
                if(secondsElapsed < countdown) {
                    textPane.setText(String.format("Get ready in %d...", countdown - secondsElapsed));
                }
                else {
                    textPane.setText("");
                    timer.stop();
                    startGame();
                }
            });

            timer.start();
        });

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
    }

}
