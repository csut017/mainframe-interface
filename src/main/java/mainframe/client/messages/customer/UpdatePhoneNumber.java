package mainframe.client.messages.customer;
import mainframe.client.*;
import java.io.IOException;
public class UpdatePhoneNumber implements Message {
    private boolean _canTxt;
    private String _countryCode;
    private String _customer;
    private long _id;
    private boolean _isPrimary;
    private String _number;
    private String _otherType;
    private String _type;
    public UpdatePhoneNumber() {}
    public UpdatePhoneNumber(boolean canTxt, String countryCode, String customer, long id, boolean isPrimary, String number, String otherType, String type) {
        _canTxt = canTxt;
        _countryCode = countryCode;
        _customer = customer;
        _id = id;
        _isPrimary = isPrimary;
        _number = number;
        _otherType = otherType;
        _type = type;
    }
    public static final String TYPE_HOME = "Home";
    public static final String TYPE_MOBILE = "Mobile";
    public static final String TYPE_WORK = "Work";
    public static final String TYPE_OTHER = "Other";
    @Override
    public Status send(Connection connection) throws IOException
    {
        MessageRequest request = new MessageRequest(
            MessageRequest.UPDATE,
            "customer.updatePhoneNumber");
        request.setValue("canTxt", _canTxt ? "y" : "n");
        request.setValue("countryCode", _countryCode);
        request.setValue("customer", _customer);
        request.setValue("id", Long.toString(_id));
        request.setValue("isPrimary", _isPrimary ? "y" : "n");
        request.setValue("number", _number);
        request.setValue("otherType", _otherType);
        request.setValue("type", _type);
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _customer = response.getValue("customer");
            String idValue = response.getValue("id");
            _id = idValue == null ? null : Long.parseLong(idValue);
            _type = response.getValue("type");
            _otherType = response.getValue("otherType");
            _countryCode = response.getValue("countryCode");
            _number = response.getValue("number");
            String isPrimaryValue = response.getValue("isPrimary");
            _isPrimary = "y".equals(isPrimaryValue);
            String canTxtValue = response.getValue("canTxt");
            _canTxt = "y".equals(canTxtValue);
        }
        return response.getStatus();
    }
    public void setCanTxt(Boolean value)
    {
        _canTxt = value;
    }
    public Boolean getCanTxt()
    {
        return _canTxt;
    }
    public void setCountryCode(String value)
    {
        _countryCode = value;
    }
    public String getCountryCode()
    {
        return _countryCode;
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
    public void setNumber(String value)
    {
        _number = value;
    }
    public String getNumber()
    {
        return _number;
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
