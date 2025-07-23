package mainframe.client.messages.loanSummary;
import mainframe.client.*;
import java.io.IOException;
public class Retrieve implements Message {
    private String _customerName;
    private double _finalPaymentAmount;
    private String _finalPaymentDate;
    private String _loanNumber;
    private String _number;
    private long _numberOfPayments;
    private double _totalAmountPaid;
    private double _totalInterestPaid;
    public Retrieve() {}
    public Retrieve(String number) {
        _number = number;
    }
    @Override
    public Status send(Connection connection) throws IOException
    {
        MessageRequest request = new MessageRequest(
            MessageRequest.QUERY,
            "loanSummary.retrieve");
        request.setValue("number", _number);
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _loanNumber = response.getValue("loanNumber");
            _customerName = response.getValue("customerName");
            String totalAmountPaidValue = response.getValue("totalAmountPaid");
            _totalAmountPaid = totalAmountPaidValue == null ? null : Double.parseDouble(totalAmountPaidValue);
            String totalInterestPaidValue = response.getValue("totalInterestPaid");
            _totalInterestPaid = totalInterestPaidValue == null ? null : Double.parseDouble(totalInterestPaidValue);
            String numberOfPaymentsValue = response.getValue("numberOfPayments");
            _numberOfPayments = numberOfPaymentsValue == null ? null : Long.parseLong(numberOfPaymentsValue);
            _finalPaymentDate = response.getValue("finalPaymentDate");
            String finalPaymentAmountValue = response.getValue("finalPaymentAmount");
            _finalPaymentAmount = finalPaymentAmountValue == null ? null : Double.parseDouble(finalPaymentAmountValue);
        }
        return response.getStatus();
    }
    public void setNumber(String value) {
        _number = value;
    }
    public String getCustomerName() {
        return _customerName;
    }
    public Double getFinalPaymentAmount() {
        return _finalPaymentAmount;
    }
    public String getFinalPaymentDate() {
        return _finalPaymentDate;
    }
    public String getLoanNumber() {
        return _loanNumber;
    }
    public Long getNumberOfPayments() {
        return _numberOfPayments;
    }
    public Double getTotalAmountPaid() {
        return _totalAmountPaid;
    }
    public Double getTotalInterestPaid() {
        return _totalInterestPaid;
    }
}
