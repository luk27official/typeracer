package cz.lukaspolak.typeracer;

import javax.swing.*;

public class MainMenu extends JFrame {
    private JPanel mainPanel;
    private JButton gameBtn;
    private JButton statsBtn;
    private JLabel mainMenuLabel;

    public void createUIComponents() {
        this.setContentPane(this.mainPanel);
        this.setTitle("TypeRacer Game");
        this.setSize(500, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        gameBtn.addActionListener(e -> {
            Game game = new Game();
            game.createUIComponents();
            dispose();
        });
    }
}
