package com.lib;


import org.apache.http.Header;

public class NetworkInstance {

    String json;
    String message;
    boolean ready;
    int code;
    public Header[] responseHeaders = null;

    static NetworkInstance instance = null;

    private NetworkInstance() {
    }

    public String getJson() {
        return json;
    }

    public void setJson(String s) {
        json = s;
    }


    public String getMsg() {
        return message;
    }

    public void setMsg(String s) {
        message = s;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int c) {
        code = c;
    }


    public static NetworkInstance getInstance() {
        if (instance == null)
            instance = new NetworkInstance();

        return instance;
    }


    public class errCode {
        public String code;
        public String message;
    }

    public Header[] getHeaders() {
        return responseHeaders;
    }

    public void setHeaders(Header[] headers) {
        responseHeaders = headers;
    }
}
