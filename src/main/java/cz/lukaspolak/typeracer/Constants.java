package cz.lukaspolak.typeracer;

public class Constants {
    public static final String GAME_TITLE = "TypeRacer Game";
    public static final String SCORES_FILE = "/scores.json";
    public static final String TEXTS_FILE = "/texts.json";

    public static final String JSON_TEXTS_KEY = "texts";
    public static final String JSON_SCORES_KEY = "scores";
    public static final String JSON_WPM_KEY = "wpm";
    public static final String JSON_ACCURACY_KEY = "accuracy";
    public static final String JSON_DATETIME_KEY = "datetime";

    public static final int JSON_INDENT = 4;

    public static final int WINDOW_WIDTH = 600;
    public static final int WINDOW_HEIGHT = 500;
    public static final String DEFAULT_FONT = "Arial";
    public static final int GAME_FONT_SIZE = 20;
    public static final int TEXTINPUT_FONT_SIZE = 15;

    public static final String READY_MESSAGE = "Get ready in %d...";

    public static final int TOP_SCORES_COUNT = 20;
    public static final String TOP_SCORES_LABEL = "Your top %d scores:";
    public static final String WPM_COLUMN_NAME = "WPM";
    public static final String ACCURACY_COLUMN_NAME = "Accuracy";
    public static final String DATETIME_COLUMN_NAME = "Date and time";

    public static final String TIME_FORMAT_MINUTES_SECONDS = "Time: %02d:%02d";
    public static final String TIME_FORMAT_HOURS_MINUTES_SECONDS = "Time: %02d:%02d:%02d";
    public static final String GAME_FINISHED = "You have finished the game in %d seconds with %.1f WPM and %.2f%% accuracy.";
    public static final String WPM_INFO = "WPM: %.1f";
    public static final String ACCURACY_INFO = "Accuracy: %.2f%%";
    public static final int START_COUNTDOWN = 3;

    public static final String JSON_ERROR = "Error while reading JSON file: %s";
    public static final String NOT_FOUND_ERROR = "Resource not found.";
    public static final String TEXT_FORMAT_ERROR = "An error occurred while changing the text. Please restart the game.";
}
