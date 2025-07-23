package mainframe.client.messages.customer;
import mainframe.client.*;
import java.io.IOException;
public class UpdateRelationship implements Message {
    private String _customer;
    private long _id;
    private String _otherType;
    private String _secondaryCustomerNumber;
    private String _type;
    public UpdateRelationship() {}
    public UpdateRelationship(String customer, long id, String otherType, String secondaryCustomerNumber, String type) {
        _customer = customer;
        _id = id;
        _otherType = otherType;
        _secondaryCustomerNumber = secondaryCustomerNumber;
        _type = type;
    }
    public static final String TYPE_PARENT = "Parent";
    public static final String TYPE_SPOUSE = "Spouse";
    public static final String TYPE_LEGALAUTHORITY = "LegalAuthority";
    public static final String TYPE_OTHER = "Other";
    @Override
    public Status send(Connection connection) throws IOException
    {
        MessageRequest request = new MessageRequest(
            MessageRequest.UPDATE,
            "customer.updateRelationship");
        request.setValue("customer", _customer);
        request.setValue("id", Long.toString(_id));
        request.setValue("otherType", _otherType);
        request.setValue("secondaryCustomerNumber", _secondaryCustomerNumber);
        request.setValue("type", _type);
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _customer = response.getValue("customer");
            String idValue = response.getValue("id");
            _id = idValue == null ? null : Long.parseLong(idValue);
            _type = response.getValue("type");
            _otherType = response.getValue("otherType");
            _secondaryCustomerNumber = response.getValue("secondaryCustomerNumber");
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
    public void setOtherType(String value)
    {
        _otherType = value;
    }
    public String getOtherType()
    {
        return _otherType;
    }
    public void setSecondaryCustomerNumber(String value)
    {
        _secondaryCustomerNumber = value;
    }
    public String getSecondaryCustomerNumber()
    {
        return _secondaryCustomerNumber;
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
