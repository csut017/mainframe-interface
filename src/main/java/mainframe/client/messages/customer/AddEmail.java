package mainframe.client.messages.customer;
import mainframe.client.*;
import java.io.IOException;
public class AddEmail implements Message {
    private String _address;
    private String _customer;
    private long _id;
    private boolean _isPrimary;
    private String _otherType;
    private String _type;
    public AddEmail() {}
    public AddEmail(String address, String customer, boolean isPrimary, String otherType, String type) {
        _address = address;
        _customer = customer;
        _isPrimary = isPrimary;
        _otherType = otherType;
        _type = type;
    }
    public static final String TYPE_PERSONAL = "Personal";
    public static final String TYPE_WORK = "Work";
    public static final String TYPE_OTHER = "Other";
    @Override
    public Status send(Connection connection) throws IOException
    {
        MessageRequest request = new MessageRequest(
            MessageRequest.UPDATE,
            "customer.addEmail");
        request.setValue("address", _address);
        request.setValue("customer", _customer);
        request.setValue("isPrimary", _isPrimary ? "y" : "n");
        request.setValue("otherType", _otherType);
        request.setValue("type", _type);
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _customer = response.getValue("customer");
            _type = response.getValue("type");
            _otherType = response.getValue("otherType");
            _address = response.getValue("address");
            String isPrimaryValue = response.getValue("isPrimary");
            _isPrimary = "y".equals(isPrimaryValue);
            String idValue = response.getValue("id");
            _id = idValue == null ? null : Long.parseLong(idValue);
        }
        return response.getStatus();
    }
    public void setAddress(String value)
    {
        _address = value;
    }
    public String getAddress()
    {
        return _address;
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
    public void setIsPrimary(Boolean value)
    {
        _isPrimary = value;
    }
    public Boolean getIsPrimary()
    {
        return _isPrimary;
    }
    public void setOtherType(String value)
    {
        _otherType = value;
    }
    public String getOtherType()
    {
        return _otherType;
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
