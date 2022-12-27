package cz.lukaspolak.typeracer;

import javax.swing.*;

public class Game extends JFrame {
    private JPanel gamePanel;

    public void createUIComponents() {
        this.setContentPane(this.gamePanel);
        this.setTitle("TypeRacer Game");
        this.setSize(500, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

}
