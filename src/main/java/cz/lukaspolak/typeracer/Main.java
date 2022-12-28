package cz.lukaspolak.typeracer;

/**
 * Main class of the application. Contains main method which creates the main menu.
 * The main menu is then responsible for creating the game and statistics forms.
 */
public class Main {

    /**
     * A default empty constructor.
     */
    public Main() {
    }
    /**
     * Main method of the application. Creates the main menu.
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        MainMenu mainMenu = new MainMenu();
        mainMenu.createUIComponents();
    }
}
