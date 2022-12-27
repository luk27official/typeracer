package cz.lukaspolak.typeracer;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;

public class Statistics {

    public void saveStatistics(double wpm, double accuracy) {
        //round doubles to 2 decimal places
        wpm = Math.round(wpm * 100.0) / 100.0;
        accuracy = Math.round(accuracy * 100.0) / 100.0;

        try (InputStream in = getClass().getResourceAsStream(Constants.SCORES_FILE)) {
            if(in == null) {
                throw new IOException("Resource not found.");
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            JSONObject obj = new JSONObject(new JSONTokener(reader));
            obj.getJSONArray(Constants.JSON_SCORES_KEY).put(new JSONObject().put(Constants.JSON_WPM_KEY, wpm).put(Constants.JSON_ACCURACY_KEY, accuracy).put(Constants.JSON_DATETIME_KEY, System.currentTimeMillis()));

            System.out.println(obj.toString(Constants.JSON_INDENT));

            try (FileWriter file = new FileWriter(getClass().getResource(Constants.SCORES_FILE).getPath())) {
                file.write(obj.toString(Constants.JSON_INDENT));
                file.flush();
            }
        }
        catch (IOException e) {
            System.err.println("Error while reading JSON file: " + e.getMessage());
        }
    }
}
