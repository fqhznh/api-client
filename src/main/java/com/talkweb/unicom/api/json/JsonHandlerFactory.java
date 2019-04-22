package com.talkweb.unicom.api.json;

import com.talkweb.unicom.api.bind.IBindFactory;
import com.talkweb.unicom.api.exception.ApiException;
import com.talkweb.unicom.api.json.impl.FastJsonHandler;
import com.talkweb.unicom.api.json.impl.GsonJsonHandler;
import com.talkweb.unicom.api.json.impl.JacksonJsonHandler;
import com.talkweb.unicom.api.utils.ClassUtils;

public class JsonHandlerFactory implements IBindFactory<IJsonHandler> {

    @Override
    public IJsonHandler create() {
        if(ClassUtils.exists("com.fasterxml.jackson.databind.ObjectMapper")) {
            return new JacksonJsonHandler();
        }
        if(ClassUtils.exists("com.google.gson.Gson")) {
            return new GsonJsonHandler();
        }
        if(ClassUtils.exists("com.alibaba.fastjson.JSON")) {
            return new FastJsonHandler();
        }
        throw new ApiException("8000", "暂时只支持fastjson、gson、jackson这三种JSON处理库，请添加一种");
    }

}
