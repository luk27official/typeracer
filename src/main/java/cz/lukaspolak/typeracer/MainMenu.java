package cz.lukaspolak.typeracer;

import javax.swing.*;

/**
 * This class is responsible for creating the main menu of the application.
 * It is also responsible for handling the events that occur in the main menu.
 */
public class MainMenu extends JFrame {
    /**
     * The main menu panel.
     */
    private JPanel mainPanel;
    /**
     * The button that starts the game.
     */
    private JButton gameBtn;
    /**
     * The button that shows the statistics.
     */
    private JButton statsBtn;
    /**
     * Top label of the main menu (has to be defined because of the GUI manager).
     */
    private JLabel mainMenuLabel;

    /**
     * A constructor for the MainMenu class.
     */
    public MainMenu() {
        super();
    }

    /**
     * This method creates the main menu, sets up all the event handlers and other window properties.
     * Then the window is shown to the user.
     */
    public void createUIComponents() {
        this.setContentPane(this.mainPanel);
        this.setTitle(Constants.GAME_TITLE);
        this.setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gameBtn.addActionListener(e -> handleGameBtn());
        statsBtn.addActionListener(e -> handleStatsBtn());

        this.setVisible(true);
    }

    /**
     * This method is called when the user clicks the "Show stats!" button.
     * It hides the main menu and creates a new StatsForm window.
     * @see StatsForm
     */
    private void handleStatsBtn() {
        StatsForm statsForm = new StatsForm();
        statsForm.createUIComponents();
        this.dispose();
    }

    /**
     * This method is called when the user clicks the "Play Game!" button.
     * It hides the main menu and creates a new Game window.
     * @see Game
     */
    private void handleGameBtn() {
        Game game = new Game();
        game.createUIComponents();
        this.dispose();
    }
}
