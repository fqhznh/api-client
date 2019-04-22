package com.talkweb.unicom.api;

import com.talkweb.unicom.api.http.IHttpHandler;
import com.talkweb.unicom.api.utils.*;
import com.talkweb.unicom.api.wrapper.IResultWrapper;
import com.talkweb.unicom.api.wrapper.impl.ApiResultWrapperImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ApiClient {

    public static ApiClientImpl create(String sid, String secret) {
        AssertUtils.notNull(sid, "接入参数无效");
        AssertUtils.notNull(secret, "接入密钥无效");
        IHttpHandler httpHandler = HttpClient.create();
        return new ApiClientImpl(httpHandler, new ApiResultWrapperImpl(secret), sid, secret);
    }

    public static ApiClientImpl create(String sid, String secret, IResultWrapper resultWrapper) {
        AssertUtils.notNull(sid, "接入参数无效");
        AssertUtils.notNull(secret, "接入密钥无效");

        IHttpHandler httpHandler = HttpClient.create();
        if(resultWrapper == null) {
            resultWrapper = new ApiResultWrapperImpl(secret);
        }
        return new ApiClientImpl(httpHandler, resultWrapper, sid, secret);
    }

    public static class ApiClientImpl {
        private IHttpHandler httpHandler;
        private String sid;
        private String secret;
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
        private void doSign(String body) {
            Long timestamp = System.currentTimeMillis() / 1000;
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
                builder.append("&").append(body);
            }
            builder.append("&").append(secret);
            httpHandler.header("sign", MD5.getMD5(builder.toString()));
        }

        public <T> T get(String url, Class<T> clazz) throws Exception {
            if(httpHandler != null) {
                doSign(null);
                String result = httpHandler.get(url);
                return resultWrapper.wrapper(result, clazz);
            }
            return null;
        }

        public <T> List<T> getWithList(String url, Class<T> clazz) throws Exception {
            if(httpHandler != null) {
                doSign(null);
                String result = httpHandler.get(url);
                return resultWrapper.wrapperList(result, clazz);
            }
            return null;
        }

        public <T> T post(String url, Object body, Class<T> clazz) throws Exception {
            if(httpHandler != null) {
                String result = null;
                if(body == null) {
                    doSign(null);
                    result = httpHandler.post(url, body);
                } else {
                    String bodyStr = JsonUtils.toJson(body);
                    String encodeBodyStr = TripleDES.encrypt(bodyStr, secret);
                    doSign(encodeBodyStr);
                    result = httpHandler.post(url, encodeBodyStr);
                }
                return resultWrapper.wrapper(result, clazz);
            }
            return null;
        }

        public <T> List<T> postWithList(String url, Object body, Class<T> clazz) throws Exception {
            if(httpHandler != null) {
                String result = null;
                if(body == null) {
                    doSign(null);
                    result = httpHandler.post(url, body);
                } else {
                    String bodyStr = JsonUtils.toJson(body);
                    String encodeBodyStr = TripleDES.encrypt(bodyStr, secret);
                    doSign(encodeBodyStr);
                    result = httpHandler.post(url, encodeBodyStr);
                }
                return resultWrapper.wrapperList(result, clazz);
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

        public <T> List<T> postFormWithList(String url, Class<T> clazz) throws Exception {
            if(httpHandler != null) {
                doSign(null);
                String result = httpHandler.postForm(url);
                if(result != null) {
                    return resultWrapper.wrapperList(result, clazz);
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
