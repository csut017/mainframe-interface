package mainframe.client.messages.customerPhoneNumber;
import mainframe.client.*;
import java.io.IOException;
public class Retrieve implements Message {
    private boolean _canTxt;
    private String _countryCode;
    private String _customerName;
    private String _customerNumber;
    private long _id;
    private boolean _isPrimary;
    private String _number;
    private String _otherType;
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
            "customerPhoneNumber.retrieve");
        request.setValue("id", Long.toString(_id));
        request.setValue("number", _number);
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _customerNumber = response.getValue("customerNumber");
            _customerName = response.getValue("customerName");
            _type = response.getValue("type");
            _otherType = response.getValue("otherType");
            String idValue = response.getValue("id");
            _id = idValue == null ? null : Long.parseLong(idValue);
            _countryCode = response.getValue("countryCode");
            _number = response.getValue("number");
            String isPrimaryValue = response.getValue("isPrimary");
            _isPrimary = "y".equals(isPrimaryValue);
            String canTxtValue = response.getValue("canTxt");
            _canTxt = "y".equals(canTxtValue);
        }
        return response.getStatus();
    }
    public void setId(Long value) {
        _id = value;
    }
    public void setNumber(String value) {
        _number = value;
    }
    public Boolean getCanTxt() {
        return _canTxt;
    }
    public String getCountryCode() {
        return _countryCode;
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
    public Boolean getIsPrimary() {
        return _isPrimary;
    }
    public String getNumber() {
        return _number;
    }
    public String getOtherType() {
        return _otherType;
    }
    public String getType() {
        return _type;
    }
}
