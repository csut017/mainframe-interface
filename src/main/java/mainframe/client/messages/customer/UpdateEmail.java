package mainframe.client.messages.customer;
import mainframe.client.*;
import java.io.IOException;
public class UpdateEmail implements Message {
    private String _address;
    private String _customer;
    private long _id;
    private boolean _isPrimary;
    private String _otherType;
    private String _type;
    public UpdateEmail() {}
    public UpdateEmail(String address, String customer, long id, boolean isPrimary, String otherType, String type) {
        _address = address;
        _customer = customer;
        _id = id;
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
            "customer.updateEmail");
        request.setValue("address", _address);
        request.setValue("customer", _customer);
        request.setValue("id", Long.toString(_id));
        request.setValue("isPrimary", _isPrimary ? "y" : "n");
        request.setValue("otherType", _otherType);
        request.setValue("type", _type);
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _customer = response.getValue("customer");
            String idValue = response.getValue("id");
            _id = idValue == null ? null : Long.parseLong(idValue);
            _type = response.getValue("type");
            _otherType = response.getValue("otherType");
            _address = response.getValue("address");
            String isPrimaryValue = response.getValue("isPrimary");
            _isPrimary = "y".equals(isPrimaryValue);
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
    public void setId(Long value)
    {
        _id = value;
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
