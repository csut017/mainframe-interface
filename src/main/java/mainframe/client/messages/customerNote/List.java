package mainframe.client.messages.customerNote;
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
            "customerNote.list");
        request.setValue("number", _number);
        request.setValue("page", Long.toString(_page));
        request.setValue("size", Long.toString(_size));
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _customerNumber = response.getValue("customerNumber");
            _customerName = response.getValue("customerName");
            String pageValue = response.getValue("page");
            _page = pageValue == null ? null : Long.parseLong(pageValue);
            String totalValue = response.getValue("total");
            _total = totalValue == null ? null : Long.parseLong(totalValue);
            long itemsCount = Long.parseLong(response.getValue("items"));
            for (int i = 1; i <= itemsCount; i++) {
                String idText = response.getValue(i + ":id");
                long idValue = idText == null ? null : Long.parseLong(idText);
                String typeValue = response.getValue(i + ":type");
                String dateAddedValue = response.getValue(i + ":dateAdded");
                String numberOfLinesValue = response.getValue(i + ":numberOfLines");
                String authorValue = response.getValue(i + ":author");
                _data.add(new Item(authorValue, dateAddedValue, idValue, numberOfLinesValue, typeValue));
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
        private String _author;
        private String _dateAdded;
        private long _id;
        private String _numberOfLines;
        private String _type;
        public Item(String author, String dateAdded, long id, String numberOfLines, String type) {
            _author = author;
            _dateAdded = dateAdded;
            _id = id;
            _numberOfLines = numberOfLines;
            _type = type;
        }
        public String getAuthor() {
            return _author;
        }
        public String getDateAdded() {
            return _dateAdded;
        }
        public Long getId() {
            return _id;
        }
        public String getNumberOfLines() {
            return _numberOfLines;
        }
        public String getType() {
            return _type;
        }
    }
}
