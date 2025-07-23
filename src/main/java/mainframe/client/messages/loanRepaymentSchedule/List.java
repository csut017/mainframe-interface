package mainframe.client.messages.loanRepaymentSchedule;
import mainframe.client.*;
import java.io.IOException;
import java.util.ArrayList;
public class List implements Message {
    private String _customerName;
    private String _loanNumber;
    private String _number;
    private long _page;
    private long _size;
    private long _total;
    private ArrayList<Item> _data = new ArrayList<>();
    public List() {}
    public List(String number, long page, long size) {
        _number = number;
        _page = page;
        _size = size;
    }
    @Override
    public Status send(Connection connection) throws IOException
    {
        MessageRequest request = new MessageRequest(
            MessageRequest.QUERY,
            "loanRepaymentSchedule.list");
        request.setValue("number", _number);
        request.setValue("page", Long.toString(_page));
        request.setValue("size", Long.toString(_size));
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _loanNumber = response.getValue("loanNumber");
            _customerName = response.getValue("customerName");
            String pageValue = response.getValue("page");
            _page = pageValue == null ? null : Long.parseLong(pageValue);
            String totalValue = response.getValue("total");
            _total = totalValue == null ? null : Long.parseLong(totalValue);
            long itemsCount = Long.parseLong(response.getValue("items"));
            for (int i = 1; i <= itemsCount; i++) {
                String paymentNumberText = response.getValue(i + ":paymentNumber");
                long paymentNumberValue = paymentNumberText == null ? null : Long.parseLong(paymentNumberText);
                String dateValue = response.getValue(i + ":date");
                String amountPaidText = response.getValue(i + ":amountPaid");
                double amountPaidValue = amountPaidText == null ? null : Double.parseDouble(amountPaidText);
                String principalAmountText = response.getValue(i + ":principalAmount");
                double principalAmountValue = principalAmountText == null ? null : Double.parseDouble(principalAmountText);
                String interestAmountText = response.getValue(i + ":interestAmount");
                double interestAmountValue = interestAmountText == null ? null : Double.parseDouble(interestAmountText);
                String amountRemainingText = response.getValue(i + ":amountRemaining");
                double amountRemainingValue = amountRemainingText == null ? null : Double.parseDouble(amountRemainingText);
                _data.add(new Item(amountPaidValue, amountRemainingValue, dateValue, interestAmountValue, paymentNumberValue, principalAmountValue));
            }
        }
        return response.getStatus();
    }
    public void setNumber(String value) {
        _number = value;
    }
    public void setPage(Long value) {
        _page = value;
    }
    public void setSize(Long value) {
        _size = value;
    }
    public String getCustomerName() {
        return _customerName;
    }
    public String getLoanNumber() {
        return _loanNumber;
    }
    public Long getPage() {
        return _page;
    }
    public Long getTotal() {
        return _total;
    }
    public Item[] getItems() {
        return _data.toArray(new Item[_data.size()]);
    }
    public class Item {
        private double _amountPaid;
        private double _amountRemaining;
        private String _date;
        private double _interestAmount;
        private long _paymentNumber;
        private double _principalAmount;
        public Item(double amountPaid, double amountRemaining, String date, double interestAmount, long paymentNumber, double principalAmount) {
            _amountPaid = amountPaid;
            _amountRemaining = amountRemaining;
            _date = date;
            _interestAmount = interestAmount;
            _paymentNumber = paymentNumber;
            _principalAmount = principalAmount;
        }
        public Double getAmountPaid() {
            return _amountPaid;
        }
        public Double getAmountRemaining() {
            return _amountRemaining;
        }
        public String getDate() {
            return _date;
        }
        public Double getInterestAmount() {
            return _interestAmount;
        }
        public Long getPaymentNumber() {
            return _paymentNumber;
        }
        public Double getPrincipalAmount() {
            return _principalAmount;
        }
    }
}
