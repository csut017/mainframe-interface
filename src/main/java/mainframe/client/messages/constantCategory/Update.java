package mainframe.client.messages.constantCategory;
import mainframe.client.*;
import java.io.IOException;
public class Update implements Message {
    private long _id;
    private String _name;
    public Update() {}
    public Update(long id, String name) {
        _id = id;
        _name = name;
    }
    @Override
    public Status send(Connection connection) throws IOException
    {
        MessageRequest request = new MessageRequest(
            MessageRequest.UPDATE,
            "constantCategory.update");
        request.setValue("id", Long.toString(_id));
        request.setValue("name", _name);
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _name = response.getValue("name");
        }
        return response.getStatus();
    }
    public void setId(Long value)
    {
        _id = value;
    }
    public void setName(String value)
    {
        _name = value;
    }
    public String getName()
    {
        return _name;
    }
}
