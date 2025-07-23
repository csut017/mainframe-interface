package mainframe.client.messages.constantItem;
import mainframe.client.*;
import java.io.IOException;
import java.util.ArrayList;
public class List implements Message {
    private long _category;
    private String _categoryId;
    private String _categoryName;
    private long _page;
    private long _size;
    private long _total;
    private ArrayList<Item> _data = new ArrayList<>();
    public List() {}
    public List(long category, long page, long size) {
        _category = category;
        _page = page;
        _size = size;
    }
    @Override
    public Status send(Connection connection) throws IOException
    {
        MessageRequest request = new MessageRequest(
            MessageRequest.QUERY,
            "constantItem.list");
        request.setValue("category", Long.toString(_category));
        request.setValue("page", Long.toString(_page));
        request.setValue("size", Long.toString(_size));
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _categoryId = response.getValue("categoryId");
            _categoryName = response.getValue("categoryName");
            String pageValue = response.getValue("page");
            _page = pageValue == null ? null : Long.parseLong(pageValue);
            String totalValue = response.getValue("total");
            _total = totalValue == null ? null : Long.parseLong(totalValue);
            long itemsCount = Long.parseLong(response.getValue("items"));
            for (int i = 1; i <= itemsCount; i++) {
                String nameValue = response.getValue(i + ":name");
                String orderText = response.getValue(i + ":order");
                long orderValue = orderText == null ? null : Long.parseLong(orderText);
                _data.add(new Item(nameValue, orderValue));
            }
        }
        return response.getStatus();
    }
    public void setCategory(Long value) {
        _category = value;
    }
    public void setPage(Long value) {
        _page = value;
    }
    public void setSize(Long value) {
        _size = value;
    }
    public String getCategoryId() {
        return _categoryId;
    }
    public String getCategoryName() {
        return _categoryName;
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
        private String _name;
        private long _order;
        public Item(String name, long order) {
            _name = name;
            _order = order;
        }
        public String getName() {
            return _name;
        }
        public Long getOrder() {
            return _order;
        }
    }
}
