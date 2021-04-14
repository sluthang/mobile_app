package za.co.wethinkcode.robot.server;

import org.json.simple.JSONObject;

public class ResponseBuilder {
    JSONObject response;
    public ResponseBuilder(){
        response = new JSONObject();
        response.put("data", null);
    }

    public void add(String key, Object value) {
        response.put(key, value);
    }

    public String getValue(String key) {
        if (response.get(key) == null) {
            return "";
        }
        else return (String)response.get(key);
    }

    public void addData(Object data) {
        response.put("data", data);
    }

    @Override
    public String toString(){
        return this.response.toJSONString();
    }
}
