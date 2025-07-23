package mainframe.client.messages.customer;
import mainframe.client.*;
import java.io.IOException;
public class AddEmployer implements Message {
    private String _customer;
    private long _id;
    private String _name;
    private String _position;
    private double _salary;
    private String _type;
    public AddEmployer() {}
    public AddEmployer(String customer, String name, String position, double salary, String type) {
        _customer = customer;
        _name = name;
        _position = position;
        _salary = salary;
        _type = type;
    }
    public static final String TYPE_PRIMARY = "Primary";
    public static final String TYPE_SECONDARY = "Secondary";
    @Override
    public Status send(Connection connection) throws IOException
    {
        MessageRequest request = new MessageRequest(
            MessageRequest.UPDATE,
            "customer.addEmployer");
        request.setValue("customer", _customer);
        request.setValue("name", _name);
        request.setValue("position", _position);
        request.setValue("salary", Double.toString(_salary));
        request.setValue("type", _type);
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _customer = response.getValue("customer");
            _type = response.getValue("type");
            _name = response.getValue("name");
            _position = response.getValue("position");
            String salaryValue = response.getValue("salary");
            _salary = salaryValue == null ? null : Double.parseDouble(salaryValue);
            String idValue = response.getValue("id");
            _id = idValue == null ? null : Long.parseLong(idValue);
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
    public Long getId()
    {
        return _id;
    }
    public void setName(String value)
    {
        _name = value;
    }
    public String getName()
    {
        return _name;
    }
    public void setPosition(String value)
    {
        _position = value;
    }
    public String getPosition()
    {
        return _position;
    }
    public void setSalary(Double value)
    {
        _salary = value;
    }
    public Double getSalary()
    {
        return _salary;
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
