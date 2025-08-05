package mainframe.client.messages.customerAddress;
import mainframe.client.*;
import java.io.IOException;
public class Retrieve implements Message {
    private String _city;
    private String _country;
    private String _customerName;
    private String _customerNumber;
    private long _id;
    private boolean _isMailing;
    private boolean _isPrimary;
    private String _line1;
    private String _line2;
    private String _number;
    private String _otherType;
    private String _postCode;
    private String _suburb;
    private String _type;
    public Retrieve() {}
    public Retrieve(long id, String number) {
        _id = id;
        _number = number;
    }
    @Override
    public Status send(Connection connection) throws IOException
    {
        MessageRequest request = new MessageRequest(
            MessageRequest.QUERY,
            "customerAddress.retrieve");
        request.setValue("id", Long.toString(_id));
        request.setValue("number", _number);
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _customerNumber = response.getValue("customerNumber");
            _customerName = response.getValue("customerName");
            _type = response.getValue("type");
            _otherType = response.getValue("otherType");
            String idValue = response.getValue("id");
            _id = idValue == null ? 0 : Long.parseLong(idValue);
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
        }
        return response.getStatus();
    }
    public void setId(Long value) {
        _id = value;
    }
    public void setNumber(String value) {
        _number = value;
    }
    public String getCity() {
        return _city;
    }
    public String getCountry() {
        return _country;
    }
    public String getCustomerName() {
        return _customerName;
    }
    public String getCustomerNumber() {
        return _customerNumber;
    }
    public Long getId() {
        return _id;
    }
    public Boolean getIsMailing() {
        return _isMailing;
    }
    public Boolean getIsPrimary() {
        return _isPrimary;
    }
    public String getLine1() {
        return _line1;
    }
    public String getLine2() {
        return _line2;
    }
    public String getOtherType() {
        return _otherType;
    }
    public String getPostCode() {
        return _postCode;
    }
    public String getSuburb() {
        return _suburb;
    }
    public String getType() {
        return _type;
    }
}
