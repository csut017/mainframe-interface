package mainframe.client;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;

public final class DataParser {

    public static String convertToData(MessageRequest request) {
        JSONObject data = new JSONObject();

        for (String key : request.getValueKeys()) {
            data.put(key, request.getValue(key));
        }

        return data.toJSONString();
    }

    public static HashMap<String, String> convertResponseFromData(String value) throws ParseException {
        JSONParser parser = new JSONParser();
        HashMap<String, String> values = new HashMap<>();
        JSONObject data = (JSONObject) parser.parse(value);
        for (Object key : data.keySet()) {
            Object jsonValue = data.get(key);
            if (jsonValue != null) {
                if (jsonValue instanceof JSONArray) {
                    JSONArray array = (JSONArray) jsonValue;
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject item = (JSONObject) array.get(i);
                        for (Object itemKey : item.keySet()) {
                            Object itemValue = item.get(itemKey);
                            if (itemValue != null) values.put((i + 1) + ":" + (String) itemKey, itemValue.toString());
                        }
                    }
                    values.put((String) key, Integer.toString(array.size()));
                } else {
                    values.put((String) key, jsonValue.toString());
                }
            }
        }

        return values;
    }
}
