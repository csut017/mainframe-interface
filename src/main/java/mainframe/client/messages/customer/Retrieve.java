package mainframe.client.messages.customer;
import mainframe.client.*;
import java.io.IOException;
public class Retrieve implements Message {
    private String _dateOfBirth;
    private String _familyName;
    private String _firstName;
    private String _nationality;
    private String _number;
    private String _status;
    public Retrieve() {}
    public Retrieve(String number) {
        _number = number;
    }
    @Override
    public Status send(Connection connection) throws IOException
    {
        MessageRequest request = new MessageRequest(
            MessageRequest.QUERY,
            "customer.retrieve");
        request.setValue("number", _number);
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _familyName = response.getValue("familyName");
            _firstName = response.getValue("firstName");
            _dateOfBirth = response.getValue("dateOfBirth");
            _nationality = response.getValue("nationality");
            _status = response.getValue("status");
            _number = response.getValue("number");
        }
        return response.getStatus();
    }
    public void setNumber(String value) {
        _number = value;
    }
    public String getDateOfBirth() {
        return _dateOfBirth;
    }
    public String getFamilyName() {
        return _familyName;
    }
    public String getFirstName() {
        return _firstName;
    }
    public String getNationality() {
        return _nationality;
    }
    public String getNumber() {
        return _number;
    }
    public String getStatus() {
        return _status;
    }
}
