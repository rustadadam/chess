package ui;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Map;

import model.AuthData;
import model.GameData;
import model.GameRequest;
import model.UserData;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public void clearDataBase() throws Exception {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null);
    }

    public Object listGames() throws Exception { //The auth should be in the header?
        var path = "/game";
        return this.makeRequest("GET", path, null, Map.class);
    }

    public void joinGame(GameRequest gameRequest) throws Exception {
        var path = "/game";
        this.makeRequest("PUT", path, gameRequest, null);
    }

    public Object createGame(GameData gameData) throws Exception {
        var path = "/game";
        return this.makeRequest("POST", path, gameData, Map.class);
    }

    public Object login(UserData userData) throws Exception {
        var path = "/session";
        return this.makeRequest("POST", path, userData, AuthData.class);
    }

    public void logout() throws Exception {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null);
    }

    public AuthData register(UserData userData) throws Exception {
        var path = "/user";
        return this.makeRequest("POST", path, userData, AuthData.class);
    }


    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws Exception {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, Exception {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new Exception("failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }


}
