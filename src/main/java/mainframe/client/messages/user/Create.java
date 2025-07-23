package mainframe.client.messages.user;
import mainframe.client.*;
import java.io.IOException;
public class Create implements Message {
    private long _id;
    private String _login;
    private String _name;
    private long _organisation;
    private String _password;
    private long _permissions;
    public Create() {}
    public Create(String login, String name, long organisation, String password, long permissions) {
        _login = login;
        _name = name;
        _organisation = organisation;
        _password = password;
        _permissions = permissions;
    }
    @Override
    public Status send(Connection connection) throws IOException
    {
        MessageRequest request = new MessageRequest(
            MessageRequest.UPDATE,
            "user.create");
        request.setValue("login", _login);
        request.setValue("name", _name);
        request.setValue("organisation", Long.toString(_organisation));
        request.setValue("password", _password);
        request.setValue("permissions", Long.toString(_permissions));
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            String idValue = response.getValue("id");
            _id = idValue == null ? null : Long.parseLong(idValue);
            _name = response.getValue("name");
            _login = response.getValue("login");
            String permissionsValue = response.getValue("permissions");
            _permissions = permissionsValue == null ? null : Long.parseLong(permissionsValue);
        }
        return response.getStatus();
    }
    public Long getId()
    {
        return _id;
    }
    public void setLogin(String value)
    {
        _login = value;
    }
    public String getLogin()
    {
        return _login;
    }
    public void setName(String value)
    {
        _name = value;
    }
    public String getName()
    {
        return _name;
    }
    public void setOrganisation(Long value)
    {
        _organisation = value;
    }
    public void setPassword(String value)
    {
        _password = value;
    }
    public void setPermissions(Long value)
    {
        _permissions = value;
    }
    public Long getPermissions()
    {
        return _permissions;
    }
}
