package mainframe.client.messages.user;
import mainframe.client.*;
import java.io.IOException;
import java.util.ArrayList;
public class List implements Message {
    private String _organisation;
    private long _page;
    private long _size;
    private long _total;
    private ArrayList<Item> _data = new ArrayList<>();
    public List() {}
    public List(String organisation, long page, long size) {
        _organisation = organisation;
        _page = page;
        _size = size;
    }
    @Override
    public Status send(Connection connection) throws IOException
    {
        MessageRequest request = new MessageRequest(
            MessageRequest.QUERY,
            "user.list");
        request.setValue("organisation", _organisation);
        request.setValue("page", Long.toString(_page));
        request.setValue("size", Long.toString(_size));
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            String pageValue = response.getValue("page");
            _page = pageValue == null ? 0 : Long.parseLong(pageValue);
            String totalValue = response.getValue("total");
            _total = totalValue == null ? 0 : Long.parseLong(totalValue);
            long itemsCount = Long.parseLong(response.getValue("items"));
            for (int i = 1; i <= itemsCount; i++) {
                String nameValue = response.getValue(i + ":name");
                String loginValue = response.getValue(i + ":login");
                String idText = response.getValue(i + ":id");
                long idValue = idText == null ? null : Long.parseLong(idText);
                _data.add(new Item(idValue, loginValue, nameValue));
            }
        }
        return response.getStatus();
    }
    public void setOrganisation(String value) {
        _organisation = value;
    }
    public void setPage(Long value) {
        _page = value;
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
        private long _id;
        private String _login;
        private String _name;
        public Item(long id, String login, String name) {
            _id = id;
            _login = login;
            _name = name;
        }
        public Long getId() {
            return _id;
        }
        public String getLogin() {
            return _login;
        }
        public String getName() {
            return _name;
        }
    }
}
