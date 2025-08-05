package mainframe.client.messages.organisation;
import mainframe.client.*;
import java.io.IOException;
public class Retrieve implements Message {
    private long _id;
    private String _name;
    public Retrieve() {}
    public Retrieve(long id) {
        _id = id;
    }
    @Override
    public Status send(Connection connection) throws IOException
    {
        MessageRequest request = new MessageRequest(
            MessageRequest.QUERY,
            "organisation.retrieve");
        request.setValue("id", Long.toString(_id));
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _name = response.getValue("name");
            String idValue = response.getValue("id");
            _id = idValue == null ? 0 : Long.parseLong(idValue);
        }
        return response.getStatus();
    }
    public void setId(Long value) {
        _id = value;
    }
    public Long getId() {
        return _id;
    }
    public String getName() {
        return _name;
    }
}
