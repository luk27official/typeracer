package cz.lukaspolak.typeracer;

import org.json.JSONObject;

import javax.swing.*;

public class StatsForm extends JFrame {

    private JPanel statsPanel;
    private JLabel upperLabel;
    private JTable scoresTable;
    private JButton backToMenuBtn;

    private Statistics statistics;

    private void updateTable(JSONObject[] scores) {
        // TODO: implement
    }

    private void handleBackToMenuBtn() {
        MainMenu mainMenu = new MainMenu();
        mainMenu.createUIComponents();
        this.dispose();
    }

    public void createUIComponents() {
        this.setContentPane(this.statsPanel);
        this.setTitle(Constants.GAME_TITLE);
        this.setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        backToMenuBtn.addActionListener(e -> handleBackToMenuBtn());

        statistics = new Statistics();
        JSONObject[] topScores = statistics.getTopScores(10, StatisticsCriteria.WPM);
        updateTable(topScores);
    }
}
