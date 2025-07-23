package mainframe.client.messages.constantCategory;
import mainframe.client.*;
import java.io.IOException;
public class AddItem implements Message {
    private long _category;
    private String _name;
    private long _order;
    public AddItem() {}
    public AddItem(long category, String name, long order) {
        _category = category;
        _name = name;
        _order = order;
    }
    @Override
    public Status send(Connection connection) throws IOException
    {
        MessageRequest request = new MessageRequest(
            MessageRequest.UPDATE,
            "constantCategory.addItem");
        request.setValue("category", Long.toString(_category));
        request.setValue("name", _name);
        request.setValue("order", Long.toString(_order));
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _name = response.getValue("name");
            String orderValue = response.getValue("order");
            _order = orderValue == null ? null : Long.parseLong(orderValue);
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
    public void setOrder(Long value)
    {
        _order = value;
    }
    public Long getOrder()
    {
        return _order;
    }
}
