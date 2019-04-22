package com.talkweb.unicom.api;

import com.talkweb.unicom.api.http.IHttpHandler;
import com.talkweb.unicom.api.utils.HttpClient;
import com.talkweb.unicom.api.utils.JsonUtils;
import com.talkweb.unicom.api.utils.TripleDES;
import com.talkweb.unicom.api.wrapper.IResultWrapper;
import com.talkweb.unicom.api.wrapper.impl.DefaultResultWrapperImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ApiClient {

    public static ApiClientImpl create(String sid, String secret) {
        IHttpHandler httpHandler = HttpClient.create();
        return new ApiClientImpl(httpHandler, new DefaultResultWrapperImpl(), sid, secret);
    }

    public static ApiClientImpl create(String sid, String secret, IResultWrapper resultWrapper) {
        IHttpHandler httpHandler = HttpClient.create();
        if(resultWrapper == null) {
            resultWrapper = new DefaultResultWrapperImpl();
        }
        return new ApiClientImpl(httpHandler, resultWrapper, sid, secret);
    }

    public static class ApiClientImpl {
        private IHttpHandler httpHandler;
        private String sid;
        private String secret;
        private Long timestamp;
        private IResultWrapper resultWrapper;

        public ApiClientImpl(IHttpHandler httpHandler, IResultWrapper resultWrapper, String sid, String secret) {
            this.sid = sid;
            this.secret = secret;
            this.httpHandler = httpHandler;
            this.resultWrapper = resultWrapper;
        }

        public ApiClientImpl readTimeout(int readTimeout) {
            if(httpHandler != null) {
                httpHandler.readTimeout(readTimeout);
            }
            return this;
        }

        public ApiClientImpl connectTimeout(int connectTimeout) {
            if(httpHandler != null) {
                httpHandler.connectTimeout(connectTimeout);
            }
            return this;
        }

        public ApiClientImpl charset(String charset) {
            if(httpHandler != null) {
                httpHandler.charset(charset);
            }
            return this;
        }

        /**
         * 进行数字签名
         */
        private void doSign(Object body) {
            httpHandler.header("sid", sid).header("timestamp", timestamp + "");
            StringBuilder builder = new StringBuilder();
            builder.append(sid).append("&").append(timestamp);
            if(body == null) {
                Map<String, Object> params = httpHandler.getParams();
                if(params != null && params.size() > 0) {
                    List<String> paramNames = new ArrayList<>(params.keySet());
                    Collections.sort(paramNames);
                    for (String name : paramNames) {
                        Object value = params.get(name);
                        if(value != null) {
                            builder.append("&").append(name).append("=").append(value);
                        }
                    }
                }
            } else {
                String bodyStr = JsonUtils.toJson(body);
                builder.append("&").append(TripleDES.encrypt(bodyStr, secret));
            }
            builder.append("&").append(secret);
            httpHandler.header("sign", builder.toString());
        }

        public <T> T get(String url, Class<T> clazz) throws Exception {
            if(httpHandler != null) {
                doSign(null);
                String result = httpHandler.get(url);
                return resultWrapper.wrapper(result, clazz);
            }
            return null;
        }

        public <T> T post(String url, Object body, Class<T> clazz) throws Exception {
            if(httpHandler != null) {
                doSign(body);
                String result = httpHandler.post(url, body);
                return resultWrapper.wrapper(result, clazz);
            }
            return null;
        }

        public <T> T postForm(String url, Class<T> clazz) throws Exception {
            if(httpHandler != null) {
                doSign(null);
                String result = httpHandler.postForm(url);
                if(result != null) {
                    return resultWrapper.wrapper(result, clazz);
                }
            }
            return null;
        }

        public ApiClientImpl header(String name, String value) {
            if(httpHandler != null) {
                httpHandler.header(name, value);
            }
            return this;
        }

        public ApiClientImpl param(String name, Object value) {
            if(httpHandler != null) {
                httpHandler.param(name, value);
            }
            return this;
        }
    }

}
