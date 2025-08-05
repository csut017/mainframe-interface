package mainframe.client.messages.loanCoborrower;
import mainframe.client.*;
import java.io.IOException;
public class Retrieve implements Message {
    private String _coborrowerName;
    private String _coborrowerNumber;
    private String _customerName;
    private long _id;
    private String _loanNumber;
    private String _number;
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
            "loanCoborrower.retrieve");
        request.setValue("id", Long.toString(_id));
        request.setValue("number", _number);
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _loanNumber = response.getValue("loanNumber");
            _customerName = response.getValue("customerName");
            String idValue = response.getValue("id");
            _id = idValue == null ? 0 : Long.parseLong(idValue);
            _type = response.getValue("type");
            _coborrowerNumber = response.getValue("coborrowerNumber");
            _coborrowerName = response.getValue("coborrowerName");
        }
        return response.getStatus();
    }
    public void setId(Long value) {
        _id = value;
    }
    public void setNumber(String value) {
        _number = value;
    }
    public String getCoborrowerName() {
        return _coborrowerName;
    }
    public String getCoborrowerNumber() {
        return _coborrowerNumber;
    }
    public String getCustomerName() {
        return _customerName;
    }
    public Long getId() {
        return _id;
    }
    public String getLoanNumber() {
        return _loanNumber;
    }
    public String getType() {
        return _type;
    }
}
