package com.talkweb.unicom.api.utils;

import com.talkweb.unicom.api.http.HttpHandlerFactory;
import com.talkweb.unicom.api.http.IHttpHandler;

public class HttpClient {

    private static IHttpHandler httpHandler = new HttpHandlerFactory().create();

    public static void setHttpHandler(IHttpHandler httpHandler) {
        HttpClient.httpHandler = httpHandler;
    }

    public static IHttpHandler create() {
        return httpHandler;
    }

}
