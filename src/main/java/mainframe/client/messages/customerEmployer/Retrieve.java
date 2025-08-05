package mainframe.client.messages.customerEmployer;
import mainframe.client.*;
import java.io.IOException;
public class Retrieve implements Message {
    private String _customerName;
    private String _customerNumber;
    private long _id;
    private String _name;
    private String _number;
    private String _position;
    private double _salary;
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
            "customerEmployer.retrieve");
        request.setValue("id", Long.toString(_id));
        request.setValue("number", _number);
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _customerNumber = response.getValue("customerNumber");
            _customerName = response.getValue("customerName");
            String idValue = response.getValue("id");
            _id = idValue == null ? 0 : Long.parseLong(idValue);
            _type = response.getValue("type");
            _name = response.getValue("name");
            _position = response.getValue("position");
            String salaryValue = response.getValue("salary");
            _salary = salaryValue == null ? null : Double.parseDouble(salaryValue);
        }
        return response.getStatus();
    }
    public void setId(Long value) {
        _id = value;
    }
    public void setNumber(String value) {
        _number = value;
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
    public String getName() {
        return _name;
    }
    public String getPosition() {
        return _position;
    }
    public Double getSalary() {
        return _salary;
    }
    public String getType() {
        return _type;
    }
}
