package mainframe.client.messages.loan;
import mainframe.client.*;
import java.io.IOException;
import java.util.ArrayList;
public class List implements Message {
    private String _customerName;
    private String _customerNumber;
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
            "loan.list");
        request.setValue("number", _number);
        request.setValue("page", Long.toString(_page));
        request.setValue("size", Long.toString(_size));
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _customerNumber = response.getValue("customerNumber");
            _customerName = response.getValue("customerName");
            String pageValue = response.getValue("page");
            _page = pageValue == null ? 0 : Long.parseLong(pageValue);
            String totalValue = response.getValue("total");
            _total = totalValue == null ? 0 : Long.parseLong(totalValue);
            long itemsCount = Long.parseLong(response.getValue("items"));
            for (int i = 1; i <= itemsCount; i++) {
                String loanNumberValue = response.getValue(i + ":loanNumber");
                String principalValue = response.getValue(i + ":principal");
                String startDateValue = response.getValue(i + ":startDate");
                String statusValue = response.getValue(i + ":status");
                _data.add(new Item(loanNumberValue, principalValue, startDateValue, statusValue));
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
    public String getCustomerNumber() {
        return _customerNumber;
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
        private String _loanNumber;
        private String _principal;
        private String _startDate;
        private String _status;
        public Item(String loanNumber, String principal, String startDate, String status) {
            _loanNumber = loanNumber;
            _principal = principal;
            _startDate = startDate;
            _status = status;
        }
        public String getLoanNumber() {
            return _loanNumber;
        }
        public String getPrincipal() {
            return _principal;
        }
        public String getStartDate() {
            return _startDate;
        }
        public String getStatus() {
            return _status;
        }
    }
}
