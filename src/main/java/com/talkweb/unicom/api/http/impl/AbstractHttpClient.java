package com.talkweb.unicom.api.http.impl;

import com.talkweb.unicom.api.http.IHttpHandler;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class AbstractHttpClient implements IHttpHandler{

    protected int readTimeout = 50000;

    protected int connectTimeout = 50000;

    protected String charset = "UTF-8";

    protected Map<String, String> headers = new HashMap<>();

    protected Map<String, Object> params = new HashMap<>();

    public AbstractHttpClient() {
        headers.put("Accept", "*/*");
        headers.put("Connection", "keep-alive");
    }


    @Override
    public IHttpHandler header(String name, String value) {
        headers.put(name, value);
        return this;
    }

    @Override
    public IHttpHandler param(String name, Object value) {
        params.put(name, value);
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public IHttpHandler readTimeout(int readTimeout) {
        if(readTimeout > 0) {
            this.readTimeout = readTimeout;
        }
        return this;
    }

    @Override
    public IHttpHandler connectTimeout(int connectTimeout) {
        if(connectTimeout > 0) {
            this.connectTimeout = connectTimeout;
        }
        return this;
    }

    @Override
    public IHttpHandler charset(String charset) {
        if(charset != null) {
            this.charset = charset;
        }
        return this;
    }

    @Override
    public String postForm(String url) throws Exception {
        headers.put("Content-type", "application/x-www-form-urlencoded");
        return post(url, null);
    }

    private String getQueryString(boolean encode) throws IOException {
        if(params == null || params.size() == 0) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        Iterator<String> it = params.keySet().iterator();
        while (it.hasNext()) {
            String name = it.next();
            if(encode) {
                builder.append(name).append("=").append(URLEncoder.encode(getParamValue(name), charset));
            } else {
                builder.append(name).append("=").append(getParamValue(name));
            }
            if(it.hasNext()) {
                builder.append("&");
            }
        }
        return builder.toString();
    }

    private String getParamValue(String name) {
        Object value = params.get(name);
        if(value == null) {
            return "";
        }
        return value + "";
    }

    protected String getUrlAndParams(String url) throws IOException {
        String queryString = getQueryString(true);
        if(queryString != null) {
            if(url.indexOf("?") == -1) {
                url = url + "?" + queryString;
            } else {
                url = url + "&" + queryString;
            }
        }
        return url;
    }
}
