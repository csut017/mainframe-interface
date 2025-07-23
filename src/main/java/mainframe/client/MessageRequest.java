package mainframe.client;

import java.util.HashMap;

// a request to send to the mainframe
public final class MessageRequest {
    public static final String QUERY = "query";
    public static final String LOGIN = "login";
    public static final String UPDATE = "update";

    private final String _messageType;
    private final String _requestType;
    private final HashMap<String, String> _rawData = new HashMap<>();

    // initialize a new Request instance
    public MessageRequest(String messageType, String requestType) {
        _messageType = messageType;
        _requestType = requestType;
    }

    // gets a data value
    public String getValue(String key) {

        return _rawData.get(key);
    }

    // sets a data value
    public void setValue(String key, String value) {

        _rawData.put(key, value);
    }

    // gets the request type.
    public String getMessageType() {
        return _messageType;
    }

    public String getRequestType() {
        return _requestType;
    }

    public String[] getValueKeys() {

        return _rawData.keySet().toArray(new String[0]);
    }

    public String[] getValues() {

        return _rawData.values().toArray(new String[0]);
    }
}
