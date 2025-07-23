package mainframe.client.messages.loan;
import mainframe.client.*;
import java.io.IOException;
public class AddPayment implements Message {
    private double _amountPaid;
    private double _amountRemaining;
    private String _date;
    private long _id;
    private double _interestAmount;
    private String _loanNumber;
    private double _principalAmount;
    public AddPayment() {}
    public AddPayment(double amountPaid, String date, String loanNumber) {
        _amountPaid = amountPaid;
        _date = date;
        _loanNumber = loanNumber;
    }
    @Override
    public Status send(Connection connection) throws IOException
    {
        MessageRequest request = new MessageRequest(
            MessageRequest.UPDATE,
            "loan.addPayment");
        request.setValue("amountPaid", Double.toString(_amountPaid));
        request.setValue("date", _date);
        request.setValue("loanNumber", _loanNumber);
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _loanNumber = response.getValue("loanNumber");
            _date = response.getValue("date");
            String amountPaidValue = response.getValue("amountPaid");
            _amountPaid = amountPaidValue == null ? null : Double.parseDouble(amountPaidValue);
            String principalAmountValue = response.getValue("principalAmount");
            _principalAmount = principalAmountValue == null ? null : Double.parseDouble(principalAmountValue);
            String interestAmountValue = response.getValue("interestAmount");
            _interestAmount = interestAmountValue == null ? null : Double.parseDouble(interestAmountValue);
            String amountRemainingValue = response.getValue("amountRemaining");
            _amountRemaining = amountRemainingValue == null ? null : Double.parseDouble(amountRemainingValue);
            String idValue = response.getValue("id");
            _id = idValue == null ? null : Long.parseLong(idValue);
        }
        return response.getStatus();
    }
    public void setAmountPaid(Double value)
    {
        _amountPaid = value;
    }
    public Double getAmountPaid()
    {
        return _amountPaid;
    }
    public Double getAmountRemaining()
    {
        return _amountRemaining;
    }
    public void setDate(String value)
    {
        _date = value;
    }
    public String getDate()
    {
        return _date;
    }
    public Long getId()
    {
        return _id;
    }
    public Double getInterestAmount()
    {
        return _interestAmount;
    }
    public void setLoanNumber(String value)
    {
        _loanNumber = value;
    }
    public String getLoanNumber()
    {
        return _loanNumber;
    }
    public Double getPrincipalAmount()
    {
        return _principalAmount;
    }
}
