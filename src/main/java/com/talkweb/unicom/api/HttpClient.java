package com.talkweb.unicom.api;

import com.talkweb.unicom.api.utils.AssertUtils;
import com.talkweb.unicom.api.utils.JsonUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HttpClient {

    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> params = new HashMap<>();

    private Integer readTimeout = 5000;

    private Integer connectTimeout = 5000;

    private String charset = "UTF-8";

    public HttpClient() {
        header("Accept", "*/*");
        header("Connection", "keep-alive");
        header("Content-type", "text/plain;utf-8");
    }

    public HttpClient accept(String accept) {
        header("Accept", accept);
        return this;
    }

    public HttpClient readTimeout(Integer readTimeout) {
        if(readTimeout != null) {
            this.readTimeout = readTimeout;
        }
        return this;
    }

    public HttpClient connectTimeout(Integer connectTimeout) {
        if(connectTimeout != null) {
            this.connectTimeout = connectTimeout;
        }
        return this;
    }

    public HttpClient charset(String charset) {
        if(charset != null) {
            this.charset = charset;
        }
        return this;
    }

    /**
     * 设置Content-Type
     * @param contentType
     * @return
     */
    public HttpClient contentType(String contentType) {
        header("Content-type", contentType);
        return this;
    }

    /**
     * 添加头部信息
     * @param name
     * @param value
     * @return
     */
    public HttpClient header(String name, String value) {
        if(name != null && value != null) {
            headers.put(name, value);
        }
        return this;
    }

    /**
     * 添加参数
     * @param name
     * @param value
     * @return
     */
    public HttpClient param(String name, String value) {
        if(name != null && value != null) {
            params.put(name, value);
        }
        return this;
    }

    /**
     * 获取请求链接
     * @param url
     * @param method
     * @return
     * @throws IOException
     */
    private HttpURLConnection getConnection(String url, String method) throws IOException {

        AssertUtils.notNull(url, "请求地址不能为空");

        URL connUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) connUrl.openConnection();
        conn.setConnectTimeout(connectTimeout);
        conn.setReadTimeout(readTimeout);

        conn.setRequestMethod(method);
        conn.setInstanceFollowRedirects(true);


        if(headers != null && headers.size() > 0) {
            for(String headerKey : headers.keySet()) {
                conn.addRequestProperty(headerKey, headers.get(headerKey));
            }
        }

        return conn;
    }

    /**
     * 读取请求结果
     * @param conn
     * @return
     * @throws IOException
     */
    private String read(HttpURLConnection conn) throws IOException {
        if(conn == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        BufferedReader br = null;
        try {
            conn.connect();
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));

            //jdk1.7
            String line = null;
            while((line = br.readLine()) != null) {
                builder.append(line + "\n");
            }

        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch (Exception e) {}
            }
        }
        return builder.toString();
    }

    /**
     * GET请求
     * @param url
     * @return
     * @throws IOException
     */
    public String get(String url) throws IOException {
        if(url == null) {
            return null;
        }
        String queryString = getQueryString(true);
        if(queryString != null) {
            if(url.indexOf("?") == -1) {
                url = url + "?" + queryString;
            } else {
                url = url + "&" + queryString;
            }
        }
        HttpURLConnection conn = getConnection(url, "GET");
        if(conn == null) {
            return null;
        }
        return read(conn);
    }

    /**
     * 获取查询参数
     * @return
     * @throws IOException
     */
    private String getQueryString(boolean encode) throws IOException {
        if(params == null || params.size() == 0) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        Iterator<String> it = params.keySet().iterator();
        while (it.hasNext()) {
            String name = it.next();
            if(encode) {
                builder.append(name).append("=").append(URLEncoder.encode(params.get(name), charset));
            } else {
                builder.append(name).append("=").append(params.get(name));
            }
            if(it.hasNext()) {
                builder.append("&");
            }
        }
        return builder.toString();
    }

    /**
     * POST请求
     * @param url
     * @return
     * @throws IOException
     */
    public String post(String url, Object body) throws IOException {
        if(url == null) {
            return null;
        }
        String queryString = getQueryString(true);
        if(queryString != null) {
            if(url.indexOf("?") == -1) {
                url = url + "?" + queryString;
            } else {
                url = url + "&" + queryString;
            }
        }
        HttpURLConnection conn = getConnection(url, "POST");
        if(conn == null) {
            return null;
        }
        conn.setDoInput(true);
        conn.setDoOutput(true);
        if(body != null) {
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            if(body instanceof String) {
                os.writeBytes((String) body);
            } else {
                os.writeBytes(JsonUtils.toJson(body));
            }
        }
        return read(conn);
    }

    /**
     * POST请求
     * @param url
     * @return
     * @throws IOException
     */
    public String postForm(String url) throws IOException {
        contentType("application/x-www-form-urlencoded");
        return post(url, null);
    }

}
