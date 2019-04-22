package com.talkweb.unicom.api.http.impl;

import com.talkweb.unicom.api.utils.AssertUtils;
import com.talkweb.unicom.api.utils.JsonUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUrlConnectionHanddler extends AbstractHttpClient {

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

    @Override
    public String get(String url) throws Exception {
        if(url == null) {
            return null;
        }
        HttpURLConnection conn = getConnection(getUrlAndParams(url), "GET");
        if(conn == null) {
            return null;
        }
        return read(conn);
    }

    @Override
    public String post(String url, Object body) throws Exception {
        if(url == null) {
            return null;
        }
        HttpURLConnection conn = getConnection(getUrlAndParams(url), "POST");
        if(conn == null) {
            return null;
        }
        conn.setDoInput(true);
        conn.setDoOutput(true);
        if(body != null) {
            conn.addRequestProperty("Content-type", "application/json");
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            if(body instanceof String) {
                os.writeBytes((String) body);
            } else {
                os.writeBytes(JsonUtils.toJson(body));
            }
            os.flush();
        }
        return read(conn);
    }
}
