package cz.lukaspolak.typeracer;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.*;

/**
 * This class is responsible for storing and managing game statistics such as WPM and typing accuracy.
 */
public class Statistics {

    /**
     * An empty constructor.
     */
    public Statistics() {
    }

    /**
     * This method is responsible for loading statistics from the scores file.
     * The method extracts the data from the file and returns it as a list of JSON objects.
     * If the file does not exist, the method returns null.
     * The data is sorted by the given statistics criteria (WPM/accuracy/timestamp).
     * @param count number of top statistics to load
     * @param criteria criteria to sort the statistics by
     * @return sorted list of JSON objects representing top statistics
     */
    public JSONObject[] getTopScores(int count, StatisticsCriteria criteria) {
        try (InputStream in = getClass().getResourceAsStream(Constants.SCORES_FILE)) {
            if(in == null) {
                throw new IOException(Constants.NOT_FOUND_ERROR);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            JSONObject obj = new JSONObject(new JSONTokener(reader));
            JSONArray scores = obj.getJSONArray(Constants.JSON_SCORES_KEY);

            List<JSONObject> jsonValues = new ArrayList<>();
            for (int i = 0; i < scores.length(); i++) {
                jsonValues.add(scores.getJSONObject(i));
            }

            jsonValues.sort(criteria.getComparator());

            return jsonValues.stream().limit(count).toArray(JSONObject[]::new);
        }
        catch (IOException e) {
            System.err.printf((Constants.JSON_ERROR) + "%n", e.getMessage());
        }
        return null;
    }

    /**
     * This method is responsible for saving the statistics to the scores file.
     * The method appends the statistics to the file.
     * If the file does not exist, the method creates it.
     * @param wpm WPM of the game
     * @param accuracy typing accuracy of the game
     */
    public void saveStatistics(double wpm, double accuracy) {
        //round doubles to 2 decimal places
        wpm = Math.round(wpm * 100.0) / 100.0;
        accuracy = Math.round(accuracy * 100.0) / 100.0;

        try (InputStream in = getClass().getResourceAsStream(Constants.SCORES_FILE)) {
            if(in == null) {
                throw new IOException(Constants.NOT_FOUND_ERROR);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            JSONObject obj = new JSONObject(new JSONTokener(reader));
            obj.getJSONArray(Constants.JSON_SCORES_KEY).put(new JSONObject().put(Constants.JSON_WPM_KEY, wpm).put(Constants.JSON_ACCURACY_KEY, accuracy).put(Constants.JSON_DATETIME_KEY, System.currentTimeMillis()));

            try (FileWriter file = new FileWriter(Objects.requireNonNull(getClass().getResource(Constants.SCORES_FILE)).getPath())) {
                file.write(obj.toString(Constants.JSON_INDENT));
                file.flush();
            }
        }
        catch (IOException e) {
            System.err.printf((Constants.JSON_ERROR) + "%n", e.getMessage());
        }
    }
}
