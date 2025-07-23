package mainframe.client.messages.loan;
import mainframe.client.*;
import java.io.IOException;
public class UpdateNote implements Message {
    private String _author;
    private long _id;
    private String _lines;
    private String _loanNumber;
    private String _type;
    public UpdateNote() {}
    public UpdateNote(long id, String lines, String loanNumber, String type) {
        _id = id;
        _lines = lines;
        _loanNumber = loanNumber;
        _type = type;
    }
    @Override
    public Status send(Connection connection) throws IOException
    {
        MessageRequest request = new MessageRequest(
            MessageRequest.UPDATE,
            "loan.updateNote");
        request.setValue("id", Long.toString(_id));
        request.setValue("lines", _lines);
        request.setValue("loanNumber", _loanNumber);
        request.setValue("type", _type);
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _loanNumber = response.getValue("loanNumber");
            String idValue = response.getValue("id");
            _id = idValue == null ? null : Long.parseLong(idValue);
            _type = response.getValue("type");
            _author = response.getValue("author");
            _lines = response.getValue("lines");
        }
        return response.getStatus();
    }
    public String getAuthor()
    {
        return _author;
    }
    public void setId(Long value)
    {
        _id = value;
    }
    public Long getId()
    {
        return _id;
    }
    public void setLines(String value)
    {
        _lines = value;
    }
    public String getLines()
    {
        return _lines;
    }
    public void setLoanNumber(String value)
    {
        _loanNumber = value;
    }
    public String getLoanNumber()
    {
        return _loanNumber;
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
