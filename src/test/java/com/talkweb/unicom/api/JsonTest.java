package com.talkweb.unicom.api;

import com.talkweb.unicom.api.json.IJsonHandler;
import com.talkweb.unicom.api.json.JsonHandlerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JsonTest {

    public static void main(String[] args) {
        IJsonHandler jsonHandler = new JsonHandlerFactory().create();

        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", 222);
        map.put("key3", 33.44);
        map.put("key4", new Date());

        String json = jsonHandler.toJson(map);
        System.out.println(json);

        System.out.println(jsonHandler.fromJson(json, HashMap.class));
    }

}
