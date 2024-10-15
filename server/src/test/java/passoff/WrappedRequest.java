package passoff;

import spark.Request;

import java.util.HashMap;
import java.util.Map;

public class WrappedRequest extends Request {
    private final Map<String, String> params;

    public WrappedRequest() {
        super();
        params = new HashMap<>();
    }

    public void setQueryParam(String key, String value) {
        params.put(key, value);
    }

    @Override
    public String queryParams(String key) {
        return params.get(key);
    }
}
