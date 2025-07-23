package mainframe.client.messages.loan;
import mainframe.client.*;
import java.io.IOException;
public class DeleteCoborrower implements Message {
    private long _id;
    private String _loanNumber;
    public DeleteCoborrower() {}
    public DeleteCoborrower(long id, String loanNumber) {
        _id = id;
        _loanNumber = loanNumber;
    }
    @Override
    public Status send(Connection connection) throws IOException
    {
        MessageRequest request = new MessageRequest(
            MessageRequest.UPDATE,
            "loan.deleteCoborrower");
        request.setValue("id", Long.toString(_id));
        request.setValue("loanNumber", _loanNumber);
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _loanNumber = response.getValue("loanNumber");
            String idValue = response.getValue("id");
            _id = idValue == null ? null : Long.parseLong(idValue);
        }
        return response.getStatus();
    }
    public void setId(Long value)
    {
        _id = value;
    }
    public Long getId()
    {
        return _id;
    }
    public void setLoanNumber(String value)
    {
        _loanNumber = value;
    }
    public String getLoanNumber()
    {
        return _loanNumber;
    }
}
