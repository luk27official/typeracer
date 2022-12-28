package cz.lukaspolak.typeracer;

import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Date;

public class StatsForm extends JFrame {

    private JPanel statsPanel;
    private JLabel upperLabel;
    private JTable scoresTable;
    private JButton backToMenuBtn;
    private JScrollPane scrollPane;

    private Statistics statistics;

    private String[] columns = new String[] {
        Constants.WPM_COLUMN_NAME,
        Constants.ACCURACY_COLUMN_NAME,
        Constants.DATETIME_COLUMN_NAME
    };

    private void updateTable(JSONObject[] scores) {

        //DefaultTableModel model = (DefaultTableModel)scoresTable.getModel();
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        model.setColumnIdentifiers(columns);

        for(int i = 0; i < scores.length; i++) {
            JSONObject score = scores[i];

            String[] row = new String[columns.length];

            // round to 1 decimal place
            row[Arrays.asList(columns).indexOf(Constants.WPM_COLUMN_NAME)] = String.format("%.1f", score.getDouble(Constants.JSON_WPM_KEY));

            // round to 0 decimal places
            row[Arrays.asList(columns).indexOf(Constants.ACCURACY_COLUMN_NAME)] = String.format("%.0f%%", score.getDouble(Constants.JSON_ACCURACY_KEY)*100);

            // convert timestamp to date and time
            Date date = new Date(score.getLong(Constants.JSON_DATETIME_KEY));
            row[Arrays.asList(columns).indexOf(Constants.DATETIME_COLUMN_NAME)] = date.toString();

            model.addRow(row);
        }

        this.scoresTable.setModel(model);
    }

    private void handleBackToMenuBtn() {
        MainMenu mainMenu = new MainMenu();
        mainMenu.createUIComponents();
        this.dispose();
    }

    private void handleColumnTableClick(int col) {
        JSONObject[] topScores = statistics.getTopScores(Constants.TOP_SCORES_COUNT, StatisticsCriteria.values()[col]);
        updateTable(topScores);
    }

    public void createUIComponents() {
        this.setContentPane(this.statsPanel);
        this.setTitle(Constants.GAME_TITLE);
        this.setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

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
    }
}
