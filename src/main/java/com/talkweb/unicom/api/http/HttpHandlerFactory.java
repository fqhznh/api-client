package com.talkweb.unicom.api.http;

import com.talkweb.unicom.api.bind.IBindFactory;
import com.talkweb.unicom.api.http.impl.HttpClientHandler;
import com.talkweb.unicom.api.http.impl.HttpUrlConnectionHanddler;
import com.talkweb.unicom.api.utils.ClassUtils;

public class HttpHandlerFactory implements IBindFactory<IHttpHandler> {

    @Override
    public IHttpHandler create() {
//        if(ClassUtils.exists("org.apache.http.HttpEntity")) {
//            return new HttpClientHandler();
//        }
        return new HttpUrlConnectionHanddler();
    }
}
