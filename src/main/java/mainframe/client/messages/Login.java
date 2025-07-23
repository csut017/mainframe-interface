package mainframe.client.messages;

import mainframe.client.*;
import org.json.simple.JSONObject;

import java.io.IOException;

public class Login implements Message {
    private String _userName;
    private String _password;
    private String _token;
    private long _lastNumber;
    private String _currentUser;

    @Override
    public Status send(Connection connection) throws IOException {
        MessageRequest request = new MessageRequest(
                MessageRequest.LOGIN,
                "");
        request.setValue("userName", _userName);
        request.setValue("password", _password);
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _token = response.getValue("token");
            _currentUser = response.getValue("userName");
            _lastNumber = Long.parseLong(response.getValue("lastNumber"));
        }

        return response.getStatus();
    }

    public void setUserName(String value) {
        _userName = value;
    }

    public void setpassword(String value) {
        _password = value;
    }

    public String getToken() {
        return _token;
    }

    public long getLastNumber() {
        return _lastNumber;
    }

    public String getCurrentUser() {
        return _currentUser;
    }
}
