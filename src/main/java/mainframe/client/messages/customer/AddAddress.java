package mainframe.client.messages.customer;
import mainframe.client.*;
import java.io.IOException;
public class AddAddress implements Message {
    private String _city;
    private String _country;
    private String _customer;
    private long _id;
    private boolean _isMailing;
    private boolean _isPrimary;
    private String _line1;
    private String _line2;
    private String _otherType;
    private String _postCode;
    private String _suburb;
    private String _type;
    public AddAddress() {}
    public AddAddress(String city, String country, String customer, boolean isMailing, boolean isPrimary, String line1, String line2, String otherType, String postCode, String suburb, String type) {
        _city = city;
        _country = country;
        _customer = customer;
        _isMailing = isMailing;
        _isPrimary = isPrimary;
        _line1 = line1;
        _line2 = line2;
        _otherType = otherType;
        _postCode = postCode;
        _suburb = suburb;
        _type = type;
    }
    public static final String TYPE_RESIDENTIAL = "Residential";
    public static final String TYPE_POSTAL = "Postal";
    public static final String TYPE_FORWARDING = "Forwarding";
    public static final String TYPE_DELEGATED = "Delegated";
    public static final String TYPE_OTHER = "Other";
    @Override
    public Status send(Connection connection) throws IOException
    {
        MessageRequest request = new MessageRequest(
            MessageRequest.UPDATE,
            "customer.addAddress");
        request.setValue("city", _city);
        request.setValue("country", _country);
        request.setValue("customer", _customer);
        request.setValue("isMailing", _isMailing ? "y" : "n");
        request.setValue("isPrimary", _isPrimary ? "y" : "n");
        request.setValue("line1", _line1);
        request.setValue("line2", _line2);
        request.setValue("otherType", _otherType);
        request.setValue("postCode", _postCode);
        request.setValue("suburb", _suburb);
        request.setValue("type", _type);
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _customer = response.getValue("customer");
            _type = response.getValue("type");
            _otherType = response.getValue("otherType");
            String isPrimaryValue = response.getValue("isPrimary");
            _isPrimary = "y".equals(isPrimaryValue);
            String isMailingValue = response.getValue("isMailing");
            _isMailing = "y".equals(isMailingValue);
            _line1 = response.getValue("line1");
            _line2 = response.getValue("line2");
            _suburb = response.getValue("suburb");
            _city = response.getValue("city");
            _country = response.getValue("country");
            _postCode = response.getValue("postCode");
            String idValue = response.getValue("id");
            _id = idValue == null ? null : Long.parseLong(idValue);
        }
        return response.getStatus();
    }
    public void setCity(String value)
    {
        _city = value;
    }
    public String getCity()
    {
        return _city;
    }
    public void setCountry(String value)
    {
        _country = value;
    }
    public String getCountry()
    {
        return _country;
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
    public void setIsMailing(Boolean value)
    {
        _isMailing = value;
    }
    public Boolean getIsMailing()
    {
        return _isMailing;
    }
    public void setIsPrimary(Boolean value)
    {
        _isPrimary = value;
    }
    public Boolean getIsPrimary()
    {
        return _isPrimary;
    }
    public void setLine1(String value)
    {
        _line1 = value;
    }
    public String getLine1()
    {
        return _line1;
    }
    public void setLine2(String value)
    {
        _line2 = value;
    }
    public String getLine2()
    {
        return _line2;
    }
    public void setOtherType(String value)
    {
        _otherType = value;
    }
    public String getOtherType()
    {
        return _otherType;
    }
    public void setPostCode(String value)
    {
        _postCode = value;
    }
    public String getPostCode()
    {
        return _postCode;
    }
    public void setSuburb(String value)
    {
        _suburb = value;
    }
    public String getSuburb()
    {
        return _suburb;
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
