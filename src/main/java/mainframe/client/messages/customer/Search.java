package mainframe.client.messages.customer;
import mainframe.client.*;
import java.io.IOException;
import java.util.ArrayList;
public class Search implements Message {
    private long _page;
    private String _search;
    private long _size;
    private long _total;
    private ArrayList<Item> _data = new ArrayList<>();
    public Search() {}
    public Search(long page, String search, long size) {
        _page = page;
        _search = search;
        _size = size;
    }
    @Override
    public Status send(Connection connection) throws IOException
    {
        MessageRequest request = new MessageRequest(
            MessageRequest.QUERY,
            "customer.search");
        request.setValue("page", Long.toString(_page));
        request.setValue("search", _search);
        request.setValue("size", Long.toString(_size));
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            String pageValue = response.getValue("page");
            _page = pageValue == null ? 0 : Long.parseLong(pageValue);
            String totalValue = response.getValue("total");
            _total = totalValue == null ? 0 : Long.parseLong(totalValue);
            long itemsCount = Long.parseLong(response.getValue("items"));
            for (int i = 1; i <= itemsCount; i++) {
                String familyNameValue = response.getValue(i + ":familyName");
                String firstNameValue = response.getValue(i + ":firstName");
                String titleValue = response.getValue(i + ":title");
                String dateOfBirthValue = response.getValue(i + ":dateOfBirth");
                String statusValue = response.getValue(i + ":status");
                String numberValue = response.getValue(i + ":number");
                _data.add(new Item(dateOfBirthValue, familyNameValue, firstNameValue, numberValue, statusValue, titleValue));
            }
        }
        return response.getStatus();
    }
    public void setPage(Long value) {
        _page = value;
    }
    public void setSearch(String value) {
        _search = value;
    }
    public void setSize(Long value) {
        _size = value;
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
        private String _dateOfBirth;
        private String _familyName;
        private String _firstName;
        private String _number;
        private String _status;
        private String _title;
        public Item(String dateOfBirth, String familyName, String firstName, String number, String status, String title) {
            _dateOfBirth = dateOfBirth;
            _familyName = familyName;
            _firstName = firstName;
            _number = number;
            _status = status;
            _title = title;
        }
        public String getDateOfBirth() {
            return _dateOfBirth;
        }
        public String getFamilyName() {
            return _familyName;
        }
        public String getFirstName() {
            return _firstName;
        }
        public String getNumber() {
            return _number;
        }
        public String getStatus() {
            return _status;
        }
        public String getTitle() {
            return _title;
        }
    }
}
