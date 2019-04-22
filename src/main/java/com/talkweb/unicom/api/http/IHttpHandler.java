package com.talkweb.unicom.api.http;

import java.util.Map;

public interface IHttpHandler {

    public IHttpHandler readTimeout(int readTimeout);

    public IHttpHandler connectTimeout(int connectTimeout);

    public IHttpHandler charset(String charset);

    public String get(String url) throws Exception;

    public String post(String url, Object body) throws Exception;

    public String postForm(String url) throws Exception;

    public IHttpHandler header(String name, String value);

    public IHttpHandler param(String name, Object value);

    public Map<String, Object> getParams();
}
