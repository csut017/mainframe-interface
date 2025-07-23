package mainframe.client.messages.constantCategory;
import mainframe.client.*;
import java.io.IOException;
public class Create implements Message {
    private long _id;
    private String _name;
    public Create() {}
    public Create(String name) {
        _name = name;
    }
    @Override
    public Status send(Connection connection) throws IOException
    {
        MessageRequest request = new MessageRequest(
            MessageRequest.UPDATE,
            "constantCategory.create");
        request.setValue("name", _name);
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            String idValue = response.getValue("id");
            _id = idValue == null ? null : Long.parseLong(idValue);
            _name = response.getValue("name");
        }
        return response.getStatus();
    }
    public Long getId()
    {
        return _id;
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
