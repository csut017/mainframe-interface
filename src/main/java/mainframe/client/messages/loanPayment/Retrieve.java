package mainframe.client.messages.loanPayment;
import mainframe.client.*;
import java.io.IOException;
public class Retrieve implements Message {
    private double _amountPaid;
    private double _amountRemaining;
    private String _customerName;
    private String _date;
    private long _id;
    private double _interestAmount;
    private String _loanNumber;
    private String _number;
    private double _principalAmount;
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
            "loanPayment.retrieve");
        request.setValue("id", Long.toString(_id));
        request.setValue("number", _number);
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _loanNumber = response.getValue("loanNumber");
            _customerName = response.getValue("customerName");
            String idValue = response.getValue("id");
            _id = idValue == null ? 0 : Long.parseLong(idValue);
            _date = response.getValue("date");
            String amountPaidValue = response.getValue("amountPaid");
            _amountPaid = amountPaidValue == null ? null : Double.parseDouble(amountPaidValue);
            String principalAmountValue = response.getValue("principalAmount");
            _principalAmount = principalAmountValue == null ? null : Double.parseDouble(principalAmountValue);
            String interestAmountValue = response.getValue("interestAmount");
            _interestAmount = interestAmountValue == null ? null : Double.parseDouble(interestAmountValue);
            String amountRemainingValue = response.getValue("amountRemaining");
            _amountRemaining = amountRemainingValue == null ? null : Double.parseDouble(amountRemainingValue);
        }
        return response.getStatus();
    }
    public void setId(Long value) {
        _id = value;
    }
    public void setNumber(String value) {
        _number = value;
    }
    public Double getAmountPaid() {
        return _amountPaid;
    }
    public Double getAmountRemaining() {
        return _amountRemaining;
    }
    public String getCustomerName() {
        return _customerName;
    }
    public String getDate() {
        return _date;
    }
    public Long getId() {
        return _id;
    }
    public Double getInterestAmount() {
        return _interestAmount;
    }
    public String getLoanNumber() {
        return _loanNumber;
    }
    public Double getPrincipalAmount() {
        return _principalAmount;
    }
}
