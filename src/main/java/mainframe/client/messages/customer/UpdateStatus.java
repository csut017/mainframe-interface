package mainframe.client.messages.customer;
import mainframe.client.*;
import java.io.IOException;
public class UpdateStatus implements Message {
    private String _customer;
    private String _status;
    public UpdateStatus() {}
    public UpdateStatus(String customer, String status) {
        _customer = customer;
        _status = status;
    }
    public static final String STATUS_ACTIVE = "Active";
    public static final String STATUS_INACTIVE = "Inactive";
    @Override
    public Status send(Connection connection) throws IOException
    {
        MessageRequest request = new MessageRequest(
            MessageRequest.UPDATE,
            "customer.updateStatus");
        request.setValue("customer", _customer);
        request.setValue("status", _status);
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _customer = response.getValue("customer");
            _status = response.getValue("status");
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
    public void setStatus(String value)
    {
        _status = value;
    }
    public String getStatus()
    {
        return _status;
    }
}
