package mainframe.client;

import mainframe.client.messages.Login;
import okhttp3.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;

public class HttpConnection implements Connection {
    public static final MediaType MEDIA_TYPE_DATA = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient _client;
    private final String _baseUrl;
    private String _token = null;
    private long _transactionId = 0;

    public HttpConnection(String baseUrl) {
        _baseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
        _client = initializeClient();
    }

    private OkHttpClient initializeClient() {
        // initialize a new HTTP client
        // WARNING: this code is DANGEROUS - we are ignoring all SSL validation errors
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };

        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }
        OkHttpClient.Builder newBuilder = new OkHttpClient.Builder();
        newBuilder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0]);
        newBuilder.hostnameVerifier((hostname, session) -> true);
        return newBuilder.build();
    }

    @Override
    public MessageResponse send(MessageRequest request) {
        ++_transactionId;
        JSONObject requestData = new JSONObject();
        StringBuilder hashBuilder = new StringBuilder();
        requestData.put("number", _transactionId);
        hashBuilder.append(_transactionId);
        hashBuilder.append(":");
        String requestType = request.getRequestType();
        if (requestType != null && !requestType.isEmpty()) {
            requestData.put("type", requestType);
            hashBuilder.append(requestType);
            hashBuilder.append(":");
        }

        String dataJson = DataParser.convertToData(request);
        requestData.put("data", dataJson);
        hashBuilder.append(dataJson);

        String hash = hashBuilder.toString();
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            return StatusMessage.INTERNAL_FAILURE.generateMessageResponse(_transactionId);
        }
        byte[] encodedhash = digest.digest(hash.getBytes(StandardCharsets.UTF_8));
        String hashString = Base64.getEncoder().encodeToString(encodedhash);

        requestData.put("hash", hashString);
        String postBody = requestData.toJSONString();

        Request.Builder builder = new Request.Builder()
                .url(_baseUrl + "api/" + request.getMessageType())
                .post(RequestBody.create(postBody, MEDIA_TYPE_DATA))
                .addHeader("Content-Type", "application/json; charset=utf-8");
        if (_token != null)  builder.addHeader("Authorization", "Bearer " + _token);
        Request httpRequest = builder.build();

        try (Response httpResponse = _client.newCall(httpRequest).execute()) {
            int statusCode = httpResponse.code();
            if (!httpResponse.isSuccessful() && statusCode != 400)
                throw new IOException("Unexpected code " + httpResponse);

            String responseData = httpResponse.body().string();
            HashMap<String, String> values = DataParser.convertResponseFromData(responseData);
            if (statusCode == 400) return new MessageResponse(
                    new Status(StatusMessage.SERVER_FAILURE.getCode(), values.get("message"), _transactionId),
                    new HashMap<>());
            if (values.containsKey("data")) {
                values = DataParser.convertResponseFromData(values.get("data"));
            }
            return new MessageResponse(new Status(_transactionId), values);
        } catch (IOException ex) {
            return StatusMessage.NETWORK_FAILURE_UNAVAILABLE.generateMessageResponse(_transactionId);
        } catch (ParseException e) {
            return StatusMessage.UNABLE_TO_PARSE.generateMessageResponse(_transactionId);
        }
    }

    @Override
    public Status Login(String username, String password) throws IOException {
        Login message = new Login();
        message.setUserName(username);
        message.setpassword(password);
        Status result = message.send(this);
        if (result.getWasSuccessful()) {
            _token = message.getToken();
            _transactionId = message.getLastNumber();
        }
        return result;
    }

    @Override
    public void Logout() {
        _token = null;
    }

    @Override
    public void close() throws IOException {
        _client.dispatcher().executorService().shutdown();
    }

    @Override
    public boolean isConnected() {
        Request httpRequest = new Request.Builder()
                .url(_baseUrl + "api/ping")
                .get()
                .build();

        try (Response httpResponse = _client.newCall(httpRequest).execute()) {
            if (!httpResponse.isSuccessful()) throw new IOException("Unexpected code " + httpResponse);

            String data = httpResponse.body().string();
            return data.equalsIgnoreCase("online") || data.equalsIgnoreCase("ok");
        } catch (IOException ex) {
            return false;
        }
    }
}

