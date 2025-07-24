package mainframe.client;

import mainframe.client.messages.Login;

import java.io.IOException;
import java.util.HashMap;

public class StubConnection implements Connection {
    private Boolean _isConnected = false;
    private MessageResponse _response;

    @Override
    public void close() throws IOException {
        // Don't do anything
    }

    public void setIsConnected(Boolean isConnected) {
        _isConnected = isConnected;
    }

    public void setResponse(MessageResponse response) {
        _response = response;
    }

    @Override
    public boolean isConnected() {
        return _isConnected;
    }

    @Override
    public MessageResponse send(MessageRequest request) {
        return _response;
    }

    @Override
    public Status Login(String username, String password) throws IOException {
        Login message = new Login();
        message.setUserName(username);
        message.setpassword(password);
        Status result = message.send(this);
        return result;
    }

    public void setupSuccessfulLoginResult(String token, String userName, long lastNumber) {
        HashMap<String, String> values = new HashMap<>();
        values.put("token", token);
        values.put("userName", userName);
        values.put("lastNumber", String.valueOf(lastNumber));
        _response = new MessageResponse(
                new Status(1),
                values);
    }

    public void setupFailedLoginResult(int errorCode, String errorMessage) {
        HashMap<String, String> values = new HashMap<>();
        _response = new MessageResponse(
                new Status(errorCode, errorMessage, 1),
                values);
    }

    @Override
    public void Logout() {
        // Don't do anything
    }
}
