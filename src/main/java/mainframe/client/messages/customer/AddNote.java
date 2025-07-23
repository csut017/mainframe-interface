package mainframe.client.messages.customer;
import mainframe.client.*;
import java.io.IOException;
public class AddNote implements Message {
    private String _author;
    private String _customer;
    private long _id;
    private String _lines;
    private String _type;
    public AddNote() {}
    public AddNote(String customer, String lines, String type) {
        _customer = customer;
        _lines = lines;
        _type = type;
    }
    @Override
    public Status send(Connection connection) throws IOException
    {
        MessageRequest request = new MessageRequest(
            MessageRequest.UPDATE,
            "customer.addNote");
        request.setValue("customer", _customer);
        request.setValue("lines", _lines);
        request.setValue("type", _type);
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _customer = response.getValue("customer");
            _type = response.getValue("type");
            _author = response.getValue("author");
            _lines = response.getValue("lines");
            String idValue = response.getValue("id");
            _id = idValue == null ? null : Long.parseLong(idValue);
        }
        return response.getStatus();
    }
    public String getAuthor()
    {
        return _author;
    }
    public void setCustomer(String value)
    {
        _customer = value;
    }
    public String getCustomer()
    {
        return _customer;
    }
    public Long getId()
    {
        return _id;
    }
    public void setLines(String value)
    {
        _lines = value;
    }
    public String getLines()
    {
        return _lines;
    }
    public void setType(String value)
    {
        _type = value;
    }
    public String getType()
    {
        return _type;
    }
}
