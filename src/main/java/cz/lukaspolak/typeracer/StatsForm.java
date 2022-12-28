package cz.lukaspolak.typeracer;

import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Date;

/**
 * This class is responsible for displaying statistics of the player.
 * It is a JFrame that is displayed when the player clicks on the "Statistics" button in the main menu.
 */
public class StatsForm extends JFrame {

    /**
     * Main panel of the form.
     */
    private JPanel statsPanel;
    /**
     * The top label of the form.
     */
    private JLabel upperLabel;
    /**
     * The table that displays the statistics.
     */
    private JTable scoresTable;
    /**
     * The button that returns the player to the main menu.
     */
    private JButton backToMenuBtn;
    /**
     * JScrollPane that contains the table.
     */
    private JScrollPane scrollPane;

    /**
     * A field holding an instance of the Statistics class to access the player statistics.
     */
    private Statistics statistics;

    /**
     * An array of column names for the table.
     */
    private final String[] columns = new String[] {
        Constants.WPM_COLUMN_NAME,
        Constants.ACCURACY_COLUMN_NAME,
        Constants.DATETIME_COLUMN_NAME
    };

    /**
     * A constructor for the StatsForm class.
     */
    public StatsForm() {
        super();
    }
    /**
     * Method that is responsible for displaying the scores data in the table.
     */
    private void updateTable(JSONObject[] scores) {

        //DefaultTableModel model = (DefaultTableModel)scoresTable.getModel();
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        model.setColumnIdentifiers(columns);

        for (JSONObject score : scores) {
            String[] row = new String[columns.length];

            // round to 1 decimal place
            row[Arrays.asList(columns).indexOf(Constants.WPM_COLUMN_NAME)] = String.format("%.1f", score.getDouble(Constants.JSON_WPM_KEY));

            // round to 0 decimal places
            row[Arrays.asList(columns).indexOf(Constants.ACCURACY_COLUMN_NAME)] = String.format("%.0f%%", score.getDouble(Constants.JSON_ACCURACY_KEY) * 100);

            // convert timestamp to date and time
            Date date = new Date(score.getLong(Constants.JSON_DATETIME_KEY));
            row[Arrays.asList(columns).indexOf(Constants.DATETIME_COLUMN_NAME)] = date.toString();

            model.addRow(row);
        }

        this.scoresTable.setModel(model);
    }

    /**
     * Method that handles the click on the "Back to menu" button.
     */
    private void handleBackToMenuBtn() {
        MainMenu mainMenu = new MainMenu();
        mainMenu.createUIComponents();
        this.dispose();
    }

    /**
     * Method that handles the click on the table column (sorts the data by the given column metric).
     */
    private void handleColumnTableClick(int col) {
        JSONObject[] topScores = statistics.getTopScores(Constants.TOP_SCORES_COUNT, StatisticsCriteria.values()[col]);
        updateTable(topScores);
    }

    /**
     * Method that should be called when the form is created.
     * Creates the form, sets up the needed properties and sets the event listeners.
     * Also calls a method to load the statistics data from the file and updates the table.
     */
    public void createUIComponents() {
        this.setContentPane(this.statsPanel);
        this.setTitle(Constants.GAME_TITLE);
        this.setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        backToMenuBtn.addActionListener(e -> handleBackToMenuBtn());

        statistics = new Statistics();

        this.upperLabel.setText(String.format(Constants.TOP_SCORES_LABEL, Constants.TOP_SCORES_COUNT));

        scoresTable.setDefaultEditor(Object.class, null); //disable editing cells

        scoresTable.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleColumnTableClick(scoresTable.columnAtPoint(e.getPoint()));
            }
        });

        JSONObject[] topScores = statistics.getTopScores(Constants.TOP_SCORES_COUNT, StatisticsCriteria.WPM);
        updateTable(topScores);

        this.setVisible(true);
    }
}
