package mainframe.client.messages.customer;
import mainframe.client.*;
import java.io.IOException;
public class Create implements Message {
    private String _dateOfBirth;
    private String _familyName;
    private String _firstName;
    private String _nationality;
    private String _number;
    private long _organisation;
    private String _status;
    private String _title;
    public Create() {}
    public Create(String dateOfBirth, String familyName, String firstName, String nationality, long organisation, String title) {
        _dateOfBirth = dateOfBirth;
        _familyName = familyName;
        _firstName = firstName;
        _nationality = nationality;
        _organisation = organisation;
        _title = title;
    }
    @Override
    public Status send(Connection connection) throws IOException
    {
        MessageRequest request = new MessageRequest(
            MessageRequest.UPDATE,
            "customer.create");
        request.setValue("dateOfBirth", _dateOfBirth);
        request.setValue("familyName", _familyName);
        request.setValue("firstName", _firstName);
        request.setValue("nationality", _nationality);
        request.setValue("organisation", Long.toString(_organisation));
        request.setValue("title", _title);
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _familyName = response.getValue("familyName");
            _title = response.getValue("title");
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
    public String getNumber()
    {
        return _number;
    }
    public void setOrganisation(Long value)
    {
        _organisation = value;
    }
    public String getStatus()
    {
        return _status;
    }
    public void setTitle(String value)
    {
        _title = value;
    }
    public String getTitle()
    {
        return _title;
    }
}
