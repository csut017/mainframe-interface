package mainframe.client.messages.customer;
import mainframe.client.*;
import java.io.IOException;
public class AddDocument implements Message {
    private String _customer;
    private String _fileName;
    private String _fileType;
    private long _id;
    private long _size;
    private String _type;
    public AddDocument() {}
    public AddDocument(String customer, String fileName, String fileType, long size, String type) {
        _customer = customer;
        _fileName = fileName;
        _fileType = fileType;
        _size = size;
        _type = type;
    }
    @Override
    public Status send(Connection connection) throws IOException
    {
        MessageRequest request = new MessageRequest(
            MessageRequest.UPDATE,
            "customer.addDocument");
        request.setValue("customer", _customer);
        request.setValue("fileName", _fileName);
        request.setValue("fileType", _fileType);
        request.setValue("size", Long.toString(_size));
        request.setValue("type", _type);
        MessageResponse response = connection.send(request);
        if (response.getStatus().getWasSuccessful()) {
            _customer = response.getValue("customer");
            _type = response.getValue("type");
            _fileName = response.getValue("fileName");
            _fileType = response.getValue("fileType");
            String sizeValue = response.getValue("size");
            _size = sizeValue == null ? null : Long.parseLong(sizeValue);
            String idValue = response.getValue("id");
            _id = idValue == null ? null : Long.parseLong(idValue);
        }
        return response.getStatus();
    }
    public void setCustomer(String value)
    {
        _customer = value;
    }
    public String getCustomer()
    {
        return _customer;
    }
    public void setFileName(String value)
    {
        _fileName = value;
    }
    public String getFileName()
    {
        return _fileName;
    }
    public void setFileType(String value)
    {
        _fileType = value;
    }
    public String getFileType()
    {
        return _fileType;
    }
    public Long getId()
    {
        return _id;
    }
    public void setSize(Long value)
    {
        _size = value;
    }
    public Long getSize()
    {
        return _size;
    }
    public void setType(String value)
    {
        _type = value;
    }
    public String getType()
    {
        return _type;
    }
}
