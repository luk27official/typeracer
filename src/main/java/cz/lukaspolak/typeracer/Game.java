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

public class Game extends JFrame {
    private JPanel gamePanel;
    //private JTextArea readOnlyTextArea;
    private JButton startButton;
    private JTextField inputTextField;
    private JLabel wpmLabel;
    private JLabel accuracyLabel;
    private JLabel upperLabel;
    private JTextPane textPane;

    private int wpm;
    private double accuracy;
    private ArrayList<String> wordsRemaining;
    private ArrayList<String> wordsCompleted;
    private boolean playing;

    private String getRandomText() {
        // TODO: add more texts
        return "This is a sample text. It is used to test the game. It is not a real text. It is just a sample text.";
    }

    private void startGame() {
        String text = getRandomText();
        String[] words = text.split(" ");

        wordsRemaining = new ArrayList<>(Arrays.asList(words));
        wordsCompleted = new ArrayList<>();
        wpm = 0;
        accuracy = 0;

        textPane.setText(text);
        textPane.setForeground(Color.BLACK);

        inputTextField.setText("");

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

    private void checkWord() {
        if(!playing || wordsRemaining.isEmpty()) {
            return;
        }

        String input = inputTextField.getText();
        input = input.trim();
        String currentWord = wordsRemaining.get(0);

        if(input.equals(currentWord)) {
            inputTextField.setForeground(Color.BLACK);
            inputTextField.setText("");

            changeTextColor(Color.GREEN, Color.BLACK);

            wordsRemaining.remove(0);
            wordsCompleted.add(currentWord);
        }
        else if(currentWord.startsWith(input)) {
            inputTextField.setForeground(Color.BLACK);

            changeTextColor(Color.GREEN, Color.BLACK);

            // TODO: increase stats
        }
        else {
            inputTextField.setForeground(Color.RED);

            changeTextColor(Color.GREEN, Color.RED);
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
            // TODO: add a timer
            startGame();
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
