package mainframe.client.messages.constantCategory;
import mainframe.client.*;
import java.io.IOException;
public class DeleteItem implements Message {
    private long _category;
    private String _name;
    public DeleteItem() {}
    public DeleteItem(long category, String name) {
        _category = category;
        _name = name;
    }
    @Override
    public Status send(Connection connection) throws IOException
    {
        MessageRequest request = new MessageRequest(
            MessageRequest.UPDATE,
            "constantCategory.deleteItem");
        request.setValue("category", Long.toString(_category));
        request.setValue("name", _name);
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _name = response.getValue("name");
        }
        return response.getStatus();
    }
    public void setCategory(Long value)
    {
        _category = value;
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
