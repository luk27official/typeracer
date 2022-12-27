package cz.lukaspolak.typeracer;

import javax.swing.*;

public class MainMenu extends JFrame {
    private JPanel mainPanel;
    private JButton gameBtn;
    private JButton statsBtn;
    private JLabel mainMenuLabel;

    public void createUIComponents() {
        this.setContentPane(this.mainPanel);
        this.setTitle(Constants.GAME_TITLE);
        this.setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        gameBtn.addActionListener(e -> handleGameBtn());
        statsBtn.addActionListener(e -> handleStatsBtn());
    }

    private void handleStatsBtn() {
        StatsForm statsForm = new StatsForm();
        statsForm.createUIComponents();
        this.dispose();
    }

    private void handleGameBtn() {
        Game game = new Game();
        game.createUIComponents();
        this.dispose();
    }
}
