package mainframe.client.messages.loanPayment;
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
            "loanPayment.list");
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
                String idText = response.getValue(i + ":id");
                long idValue = idText == null ? null : Long.parseLong(idText);
                String dateValue = response.getValue(i + ":date");
                String amountPaidText = response.getValue(i + ":amountPaid");
                double amountPaidValue = amountPaidText == null ? null : Double.parseDouble(amountPaidText);
                _data.add(new Item(amountPaidValue, dateValue, idValue));
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
        private String _date;
        private long _id;
        public Item(double amountPaid, String date, long id) {
            _amountPaid = amountPaid;
            _date = date;
            _id = id;
        }
        public Double getAmountPaid() {
            return _amountPaid;
        }
        public String getDate() {
            return _date;
        }
        public Long getId() {
            return _id;
        }
    }
}
