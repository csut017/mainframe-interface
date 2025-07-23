package mainframe.client.messages.loan;
import mainframe.client.*;
import java.io.IOException;
public class UpdateStatus implements Message {
    private String _loanNumber;
    private String _status;
    public UpdateStatus() {}
    public UpdateStatus(String loanNumber, String status) {
        _loanNumber = loanNumber;
        _status = status;
    }
    public static final String STATUS_NEW = "New";
    public static final String STATUS_PENDING = "Pending";
    public static final String STATUS_ACTIVE = "Active";
    public static final String STATUS_DEFAULTED = "Defaulted";
    public static final String STATUS_CANCELLED = "Cancelled";
    @Override
    public Status send(Connection connection) throws IOException
    {
        MessageRequest request = new MessageRequest(
            MessageRequest.UPDATE,
            "loan.updateStatus");
        request.setValue("loanNumber", _loanNumber);
        request.setValue("status", _status);
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _loanNumber = response.getValue("loanNumber");
            _status = response.getValue("status");
        }
        return response.getStatus();
    }
    public void setLoanNumber(String value)
    {
        _loanNumber = value;
    }
    public String getLoanNumber()
    {
        return _loanNumber;
    }
    public void setStatus(String value)
    {
        _status = value;
    }
    public String getStatus()
    {
        return _status;
    }
}
