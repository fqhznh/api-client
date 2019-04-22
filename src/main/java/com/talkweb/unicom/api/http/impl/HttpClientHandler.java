package com.talkweb.unicom.api.http.impl;

import com.talkweb.unicom.api.exception.ApiException;
import com.talkweb.unicom.api.http.IHttpHandler;
import com.talkweb.unicom.api.utils.AssertUtils;
import com.talkweb.unicom.api.utils.JsonUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientHandler extends AbstractHttpClient implements IHttpHandler {

    private static PoolingHttpClientConnectionManager poolManager = getPoolConnManager();

    /**
     * 初始化连接池
     * @return
     */
    private static PoolingHttpClientConnectionManager getPoolConnManager() {
        LayeredConnectionSocketFactory socketFactory = null;
        try {
            socketFactory = new SSLConnectionSocketFactory(SSLContext.getDefault());
        } catch (Exception e) {
            throw new ApiException("8000", e.getMessage(), e);
        }
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("https", socketFactory)
                .register("http", new PlainConnectionSocketFactory())
                .build();

        PoolingHttpClientConnectionManager poolManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        poolManager.setMaxTotal(2000);
        poolManager.setDefaultMaxPerRoute(200);

        return poolManager;
    }

    private CloseableHttpClient getHttpClient() {
        return HttpClients.custom()
                .setConnectionManager(poolManager)
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(connectTimeout)
                        .setConnectionRequestTimeout(connectTimeout)
                        .setSocketTimeout(readTimeout).build())
                .build();
    }

    @Override
    public String get(String url) throws Exception {
        AssertUtils.notNull(url, "请求地址不能为空");

        HttpGet httpGet = null;
        if(params != null && params.size() > 0) {
            httpGet = new HttpGet(buildUri(url, params));
        } else {
            httpGet = new HttpGet(url);
        }
        if ( headers != null && headers.size() > 0 ) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpGet.addHeader(entry.getKey(), entry.getValue());
            }
        }
        CloseableHttpResponse response = getHttpClient().execute(httpGet);
        return getResult(response);
    }

    /**
     * 获取请求结果
     * @param response
     * @return
     * @throws Exception
     */
    private String getResult(CloseableHttpResponse response) throws Exception {
        String result = null;
        if(response != null) {
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(response.getEntity(), charset);
                    EntityUtils.consume(response.getEntity());
                }
            } finally {
                try {
                    response.close();
                } catch (Exception e) {}
            }
        }
        return result;
    }

    @Override
    public String post(String url, Object body) throws Exception {
        AssertUtils.notNull(url, "请求地址不能为空");

        HttpPost httpPost = null;
        if ( params != null && params.size() > 0 ) {
            httpPost = new HttpPost(buildUri(url, params));
        } else {
            httpPost = new HttpPost(url);
        }

        if ( headers != null && headers.size() > 0 ) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.addHeader(entry.getKey(), entry.getValue());
            }
        }

        if(body != null) {
            httpPost.setEntity(new StringEntity(JsonUtils.toJson(body), ContentType.create("application/json", charset)));
        }
        CloseableHttpResponse response = getHttpClient().execute(httpPost);
        return getResult(response);
    }

    private List<NameValuePair> getNameValuePairs(Map<String, Object> params) {
        List<NameValuePair> pairList = new ArrayList<>(params.size());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
            pairList.add(pair);
        }
        return pairList;
    }

    private URI buildUri(String url, Map<String, Object> params) throws Exception {
        URIBuilder uriBuilder = new URIBuilder(url);
        uriBuilder.addParameters(getNameValuePairs(params));
        return uriBuilder.build();
    }


}
