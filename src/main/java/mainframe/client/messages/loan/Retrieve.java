package mainframe.client.messages.loan;
import mainframe.client.*;
import java.io.IOException;
public class Retrieve implements Message {
    private String _customerName;
    private String _customerNumber;
    private String _loanNumber;
    private String _number;
    private double _paymentAmount;
    private String _paymentFrequency;
    private double _principal;
    private double _rateAmount;
    private String _rateFrequency;
    private String _rateType;
    private String _renewalDate;
    private String _startDate;
    private String _status;
    public Retrieve() {}
    public Retrieve(String number) {
        _number = number;
    }
    @Override
    public Status send(Connection connection) throws IOException
    {
        MessageRequest request = new MessageRequest(
            MessageRequest.QUERY,
            "loan.retrieve");
        request.setValue("number", _number);
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _customerNumber = response.getValue("customerNumber");
            _customerName = response.getValue("customerName");
            _loanNumber = response.getValue("loanNumber");
            _rateType = response.getValue("rateType");
            String rateAmountValue = response.getValue("rateAmount");
            _rateAmount = rateAmountValue == null ? null : Double.parseDouble(rateAmountValue);
            _rateFrequency = response.getValue("rateFrequency");
            String paymentAmountValue = response.getValue("paymentAmount");
            _paymentAmount = paymentAmountValue == null ? null : Double.parseDouble(paymentAmountValue);
            _paymentFrequency = response.getValue("paymentFrequency");
            String principalValue = response.getValue("principal");
            _principal = principalValue == null ? null : Double.parseDouble(principalValue);
            _startDate = response.getValue("startDate");
            _renewalDate = response.getValue("renewalDate");
            _status = response.getValue("status");
        }
        return response.getStatus();
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
    public String getLoanNumber() {
        return _loanNumber;
    }
    public Double getPaymentAmount() {
        return _paymentAmount;
    }
    public String getPaymentFrequency() {
        return _paymentFrequency;
    }
    public Double getPrincipal() {
        return _principal;
    }
    public Double getRateAmount() {
        return _rateAmount;
    }
    public String getRateFrequency() {
        return _rateFrequency;
    }
    public String getRateType() {
        return _rateType;
    }
    public String getRenewalDate() {
        return _renewalDate;
    }
    public String getStartDate() {
        return _startDate;
    }
    public String getStatus() {
        return _status;
    }
}
