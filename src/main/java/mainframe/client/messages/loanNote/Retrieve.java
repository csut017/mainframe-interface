package mainframe.client.messages.loanNote;
import mainframe.client.*;
import java.io.IOException;
public class Retrieve implements Message {
    private String _author;
    private String _customerName;
    private String _dateAdded;
    private long _id;
    private String _lines;
    private String _loanNumber;
    private String _number;
    private String _numberOfLines;
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
            "loanNote.retrieve");
        request.setValue("id", Long.toString(_id));
        request.setValue("number", _number);
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _loanNumber = response.getValue("loanNumber");
            _customerName = response.getValue("customerName");
            String idValue = response.getValue("id");
            _id = idValue == null ? null : Long.parseLong(idValue);
            _type = response.getValue("type");
            _dateAdded = response.getValue("dateAdded");
            _lines = response.getValue("lines");
            _numberOfLines = response.getValue("numberOfLines");
            _author = response.getValue("author");
        }
        return response.getStatus();
    }
    public void setId(Long value) {
        _id = value;
    }
    public void setNumber(String value) {
        _number = value;
    }
    public String getAuthor() {
        return _author;
    }
    public String getCustomerName() {
        return _customerName;
    }
    public String getDateAdded() {
        return _dateAdded;
    }
    public Long getId() {
        return _id;
    }
    public String getLines() {
        return _lines;
    }
    public String getLoanNumber() {
        return _loanNumber;
    }
    public String getNumberOfLines() {
        return _numberOfLines;
    }
    public String getType() {
        return _type;
    }
}
