package cz.lukaspolak.typeracer;

/**
 * Class containing all constants used in the application.
 * To add new constant, just add new public static final field.
 */
public final class Constants {

    /**
     * A private constructor to prevent instantiation.
     */
    private Constants() {
    }

    /**
     * Game title.
     */
    public static final String GAME_TITLE = "TypeRacer Game";
    /**
     * Path to the file containing all statistics.
     */
    public static final String SCORES_FILE = "/scores.json";
    /**
     * Path to the file containing all words.
     */
    public static final String TEXTS_FILE = "/texts.json";

    /**
     * Key for the array of texts in the JSON file.
     */
    public static final String JSON_TEXTS_KEY = "texts";
    /**
     * Key for the array of scores in the JSON file.
     */
    public static final String JSON_SCORES_KEY = "scores";
    /**
     * Key for the WPM value in the JSON file.
     */
    public static final String JSON_WPM_KEY = "wpm";
    /**
     * Key for the accuracy value in the JSON file.
     */
    public static final String JSON_ACCURACY_KEY = "accuracy";
    /**
     * Key for the date and time value in the JSON file.
     */
    public static final String JSON_DATETIME_KEY = "datetime";

    /**
     * Default scores JSON indentation.
     */
    public static final int JSON_INDENT = 4;

    /**
     * Window width.
     */
    public static final int WINDOW_WIDTH = 600;
    /**
     * Window height.
     */
    public static final int WINDOW_HEIGHT = 500;
    /**
     * Default font family.
     */
    public static final String DEFAULT_FONT = "Arial";
    /**
     * Default game font size.
     */
    public static final int GAME_FONT_SIZE = 20;
    /**
     * Default text input font size.
     */
    public static final int TEXTINPUT_FONT_SIZE = 15;

    /**
     * Ready message to be displayed when the game is ready to start.
     */
    public static final String READY_MESSAGE = "Get ready in %d...";

    /**
     * Number of top scores to be displayed.
     */
    public static final int TOP_SCORES_COUNT = 20;
    /**
     * Label for the top scores table (updates dynamically).
     */
    public static final String TOP_SCORES_LABEL = "Your top %d scores:";
    /**
     * Label for the WPM column in the top scores table.
     */
    public static final String WPM_COLUMN_NAME = "WPM";
    /**
     * Label for the accuracy column in the top scores table.
     */
    public static final String ACCURACY_COLUMN_NAME = "Accuracy";
    /**
     * Label for the date and time column in the top scores table.
     */
    public static final String DATETIME_COLUMN_NAME = "Date and time";

    /**
     * Time format for the game timer. (mm:ss)
     */
    public static final String TIME_FORMAT_MINUTES_SECONDS = "Time: %02d:%02d";
    /**
     * Time format for the game timer. (hh:mm:ss)
     */
    public static final String TIME_FORMAT_HOURS_MINUTES_SECONDS = "Time: %02d:%02d:%02d";
    /**
     * Message to be displayed when the game is over.
     */
    public static final String GAME_FINISHED = "You have finished the game in %d seconds with %.1f WPM and %.2f%% accuracy.";
    /**
     * WPM label format.
     */
    public static final String WPM_INFO = "WPM: %.1f";
    /**
     * Accuracy label format.
     */
    public static final String ACCURACY_INFO = "Accuracy: %.2f%%";
    /**
     * Number of seconds to wait before the game starts.
     */
    public static final int START_COUNTDOWN = 3;

    /**
     * JSON error message.
     */
    public static final String JSON_ERROR = "Error while reading JSON file: %s";
    /**
     * File not found error message.
     */
    public static final String NOT_FOUND_ERROR = "Resource not found.";
    /**
     * Text formatting error message.
     */
    public static final String TEXT_FORMAT_ERROR = "An error occurred while changing the text. Please restart the game.";
}
