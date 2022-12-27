package cz.lukaspolak.typeracer;

import org.json.JSONObject;

import java.util.Comparator;

public enum StatisticsCriteria {
    WPM,
    ACCURACY,
    DATETIME;

    public Comparator<? super Object> getComparator() {
        switch (this) {
            case WPM:
                return (o1, o2) -> {
                    JSONObject obj1 = (JSONObject) o1;
                    JSONObject obj2 = (JSONObject) o2;
                    return Double.compare(obj2.getDouble(Constants.JSON_WPM_KEY), obj1.getDouble(Constants.JSON_WPM_KEY));
                };
            case ACCURACY:
                return (o1, o2) -> {
                    JSONObject obj1 = (JSONObject) o1;
                    JSONObject obj2 = (JSONObject) o2;
                    return Double.compare(obj2.getDouble(Constants.JSON_ACCURACY_KEY), obj1.getDouble(Constants.JSON_ACCURACY_KEY));
                };
            case DATETIME:
                return (o1, o2) -> {
                    JSONObject obj1 = (JSONObject) o1;
                    JSONObject obj2 = (JSONObject) o2;
                    return Long.compare(obj2.getLong(Constants.JSON_DATETIME_KEY), obj1.getLong(Constants.JSON_DATETIME_KEY));
                };
            default:
                return null;
        }
    }
}