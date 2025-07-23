package mainframe.client.messages.customer;
import mainframe.client.*;
import java.io.IOException;
public class Update implements Message {
    private String _dateOfBirth;
    private String _familyName;
    private String _firstName;
    private String _nationality;
    private String _number;
    private String _status;
    public Update() {}
    public Update(String dateOfBirth, String familyName, String firstName, String nationality, String number) {
        _dateOfBirth = dateOfBirth;
        _familyName = familyName;
        _firstName = firstName;
        _nationality = nationality;
        _number = number;
    }
    @Override
    public Status send(Connection connection) throws IOException
    {
        MessageRequest request = new MessageRequest(
            MessageRequest.UPDATE,
            "customer.update");
        request.setValue("dateOfBirth", _dateOfBirth);
        request.setValue("familyName", _familyName);
        request.setValue("firstName", _firstName);
        request.setValue("nationality", _nationality);
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
    public void setDateOfBirth(String value)
    {
        _dateOfBirth = value;
    }
    public String getDateOfBirth()
    {
        return _dateOfBirth;
    }
    public void setFamilyName(String value)
    {
        _familyName = value;
    }
    public String getFamilyName()
    {
        return _familyName;
    }
    public void setFirstName(String value)
    {
        _firstName = value;
    }
    public String getFirstName()
    {
        return _firstName;
    }
    public void setNationality(String value)
    {
        _nationality = value;
    }
    public String getNationality()
    {
        return _nationality;
    }
    public void setNumber(String value)
    {
        _number = value;
    }
    public String getNumber()
    {
        return _number;
    }
    public String getStatus()
    {
        return _status;
    }
}
