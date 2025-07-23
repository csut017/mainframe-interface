package mainframe.client.messages.loan;
import mainframe.client.*;
import java.io.IOException;
public class Update implements Message {
    private String _customerNumber;
    private String _loanNumber;
    private double _paymentAmount;
    private String _paymentFrequency;
    private double _principal;
    private double _rateAmount;
    private String _rateFrequency;
    private String _rateType;
    private String _renewalDate;
    private String _startDate;
    private String _status;
    public Update() {}
    public Update(String customerNumber, String loanNumber, double paymentAmount, String paymentFrequency, double principal, double rateAmount, String rateFrequency, String rateType, String renewalDate, String startDate) {
        _customerNumber = customerNumber;
        _loanNumber = loanNumber;
        _paymentAmount = paymentAmount;
        _paymentFrequency = paymentFrequency;
        _principal = principal;
        _rateAmount = rateAmount;
        _rateFrequency = rateFrequency;
        _rateType = rateType;
        _renewalDate = renewalDate;
        _startDate = startDate;
    }
    public static final String RATETYPE_FIXED = "Fixed";
    public static final String RATETYPE_FLOATING = "Floating";
    public static final String RATETYPE_INTERESTONLY = "InterestOnly";
    public static final String RATEFREQUENCY_DAILY = "Daily";
    public static final String RATEFREQUENCY_WEEKLY = "Weekly";
    public static final String RATEFREQUENCY_FORTNIGHTLY = "Fortnightly";
    public static final String RATEFREQUENCY_MONTHLY = "Monthly";
    public static final String RATEFREQUENCY_YEARLY = "Yearly";
    public static final String PAYMENTFREQUENCY_WEEKLY = "Weekly";
    public static final String PAYMENTFREQUENCY_FORTNIGHTLY = "Fortnightly";
    public static final String PAYMENTFREQUENCY_MONTHLY = "Monthly";
    public static final String PAYMENTFREQUENCY_YEARLY = "Yearly";
    public static final String PAYMENTFREQUENCY_ONRENEWAL = "Onrenewal";
    @Override
    public Status send(Connection connection) throws IOException
    {
        MessageRequest request = new MessageRequest(
            MessageRequest.UPDATE,
            "loan.update");
        request.setValue("customerNumber", _customerNumber);
        request.setValue("loanNumber", _loanNumber);
        request.setValue("paymentAmount", Double.toString(_paymentAmount));
        request.setValue("paymentFrequency", _paymentFrequency);
        request.setValue("principal", Double.toString(_principal));
        request.setValue("rateAmount", Double.toString(_rateAmount));
        request.setValue("rateFrequency", _rateFrequency);
        request.setValue("rateType", _rateType);
        request.setValue("renewalDate", _renewalDate);
        request.setValue("startDate", _startDate);
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _loanNumber = response.getValue("loanNumber");
            _customerNumber = response.getValue("customerNumber");
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
    public void setCustomerNumber(String value)
    {
        _customerNumber = value;
    }
    public String getCustomerNumber()
    {
        return _customerNumber;
    }
    public void setLoanNumber(String value)
    {
        _loanNumber = value;
    }
    public String getLoanNumber()
    {
        return _loanNumber;
    }
    public void setPaymentAmount(Double value)
    {
        _paymentAmount = value;
    }
    public Double getPaymentAmount()
    {
        return _paymentAmount;
    }
    public void setPaymentFrequency(String value)
    {
        _paymentFrequency = value;
    }
    public String getPaymentFrequency()
    {
        return _paymentFrequency;
    }
    public void setPrincipal(Double value)
    {
        _principal = value;
    }
    public Double getPrincipal()
    {
        return _principal;
    }
    public void setRateAmount(Double value)
    {
        _rateAmount = value;
    }
    public Double getRateAmount()
    {
        return _rateAmount;
    }
    public void setRateFrequency(String value)
    {
        _rateFrequency = value;
    }
    public String getRateFrequency()
    {
        return _rateFrequency;
    }
    public void setRateType(String value)
    {
        _rateType = value;
    }
    public String getRateType()
    {
        return _rateType;
    }
    public void setRenewalDate(String value)
    {
        _renewalDate = value;
    }
    public String getRenewalDate()
    {
        return _renewalDate;
    }
    public void setStartDate(String value)
    {
        _startDate = value;
    }
    public String getStartDate()
    {
        return _startDate;
    }
    public String getStatus()
    {
        return _status;
    }
}
