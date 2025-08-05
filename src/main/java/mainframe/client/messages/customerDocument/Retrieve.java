package mainframe.client.messages.customerDocument;
import mainframe.client.*;
import java.io.IOException;
public class Retrieve implements Message {
    private String _customerName;
    private String _customerNumber;
    private String _fileName;
    private String _fileType;
    private long _id;
    private String _number;
    private long _size;
    private String _type;
    public Retrieve() {}
    public Retrieve(long id, String number) {
        _id = id;
        _number = number;
    }
    @Override
    public Status send(Connection connection) throws IOException
    {
        MessageRequest request = new MessageRequest(
            MessageRequest.QUERY,
            "customerDocument.retrieve");
        request.setValue("id", Long.toString(_id));
        request.setValue("number", _number);
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _customerNumber = response.getValue("customerNumber");
            _customerName = response.getValue("customerName");
            String idValue = response.getValue("id");
            _id = idValue == null ? 0 : Long.parseLong(idValue);
            _type = response.getValue("type");
            _fileName = response.getValue("fileName");
            _fileType = response.getValue("fileType");
            String sizeValue = response.getValue("size");
            _size = sizeValue == null ? 0 : Long.parseLong(sizeValue);
        }
        return response.getStatus();
    }
    public void setId(Long value) {
        _id = value;
    }
    public void setNumber(String value) {
        _number = value;
    }
    public String getCustomerName() {
        return _customerName;
    }
    public String getCustomerNumber() {
        return _customerNumber;
    }
    public String getFileName() {
        return _fileName;
    }
    public String getFileType() {
        return _fileType;
    }
    public Long getId() {
        return _id;
    }
    public Long getSize() {
        return _size;
    }
    public String getType() {
        return _type;
    }
}
