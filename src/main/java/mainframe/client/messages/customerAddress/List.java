package mainframe.client.messages.customerAddress;
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
            "customerAddress.list");
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
                String typeValue = response.getValue(i + ":type");
                String idText = response.getValue(i + ":id");
                long idValue = idText == null ? null : Long.parseLong(idText);
                String isPrimaryValue = response.getValue(i + ":isPrimary");
                String isMailingValue = response.getValue(i + ":isMailing");
                String cityValue = response.getValue(i + ":city");
                String countryValue = response.getValue(i + ":country");
                _data.add(new Item(cityValue, countryValue, idValue, isMailingValue, isPrimaryValue, typeValue));
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
        private String _city;
        private String _country;
        private long _id;
        private String _isMailing;
        private String _isPrimary;
        private String _type;
        public Item(String city, String country, long id, String isMailing, String isPrimary, String type) {
            _city = city;
            _country = country;
            _id = id;
            _isMailing = isMailing;
            _isPrimary = isPrimary;
            _type = type;
        }
        public String getCity() {
            return _city;
        }
        public String getCountry() {
            return _country;
        }
        public Long getId() {
            return _id;
        }
        public String getIsMailing() {
            return _isMailing;
        }
        public String getIsPrimary() {
            return _isPrimary;
        }
        public String getType() {
            return _type;
        }
    }
}
