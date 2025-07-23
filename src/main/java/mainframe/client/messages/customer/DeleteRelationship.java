package mainframe.client.messages.customer;
import mainframe.client.*;
import java.io.IOException;
public class DeleteRelationship implements Message {
    private String _customer;
    private long _id;
    public DeleteRelationship() {}
    public DeleteRelationship(String customer, long id) {
        _customer = customer;
        _id = id;
    }
    @Override
    public Status send(Connection connection) throws IOException
    {
        MessageRequest request = new MessageRequest(
            MessageRequest.UPDATE,
            "customer.deleteRelationship");
        request.setValue("customer", _customer);
        request.setValue("id", Long.toString(_id));
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _customer = response.getValue("customer");
            String idValue = response.getValue("id");
            _id = idValue == null ? null : Long.parseLong(idValue);
        }
        return response.getStatus();
    }
    public void setCustomer(String value)
    {
        _customer = value;
    }
    public String getCustomer()
    {
        return _customer;
    }
    public void setId(Long value)
    {
        _id = value;
    }
    public Long getId()
    {
        return _id;
    }
}
