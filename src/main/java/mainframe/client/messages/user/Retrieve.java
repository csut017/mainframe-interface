package mainframe.client.messages.user;
import mainframe.client.*;
import java.io.IOException;
public class Retrieve implements Message {
    private long _id;
    private String _lastNumber;
    private String _login;
    private String _name;
    private String _organisation;
    public Retrieve() {}
    public Retrieve(long id) {
        _id = id;
    }
    @Override
    public Status send(Connection connection) throws IOException
    {
        MessageRequest request = new MessageRequest(
            MessageRequest.QUERY,
            "user.retrieve");
        request.setValue("id", Long.toString(_id));
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _name = response.getValue("name");
            _login = response.getValue("login");
            String idValue = response.getValue("id");
            _id = idValue == null ? 0 : Long.parseLong(idValue);
            _lastNumber = response.getValue("lastNumber");
            _organisation = response.getValue("organisation");
        }
        return response.getStatus();
    }
    public void setId(Long value) {
        _id = value;
    }
    public Long getId() {
        return _id;
    }
    public String getLastNumber() {
        return _lastNumber;
    }
    public String getLogin() {
        return _login;
    }
    public String getName() {
        return _name;
    }
    public String getOrganisation() {
        return _organisation;
    }
}
