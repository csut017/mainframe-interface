package mainframe.client.messages.loan;
import mainframe.client.*;
import java.io.IOException;
public class AddCoborrower implements Message {
    private String _coborrowerNumber;
    private long _id;
    private String _loanNumber;
    private String _type;
    public AddCoborrower() {}
    public AddCoborrower(String coborrowerNumber, String loanNumber, String type) {
        _coborrowerNumber = coborrowerNumber;
        _loanNumber = loanNumber;
        _type = type;
    }
    public static final String TYPE_COBORROWER = "Coborrower";
    public static final String TYPE_GUARANTOR = "Guarantor";
    @Override
    public Status send(Connection connection) throws IOException
    {
        MessageRequest request = new MessageRequest(
            MessageRequest.UPDATE,
            "loan.addCoborrower");
        request.setValue("coborrowerNumber", _coborrowerNumber);
        request.setValue("loanNumber", _loanNumber);
        request.setValue("type", _type);
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _loanNumber = response.getValue("loanNumber");
            _type = response.getValue("type");
            _coborrowerNumber = response.getValue("coborrowerNumber");
            String idValue = response.getValue("id");
            _id = idValue == null ? null : Long.parseLong(idValue);
        }
        return response.getStatus();
    }
    public void setCoborrowerNumber(String value)
    {
        _coborrowerNumber = value;
    }
    public String getCoborrowerNumber()
    {
        return _coborrowerNumber;
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
    public void setType(String value)
    {
        _type = value;
    }
    public String getType()
    {
        return _type;
    }
}
