package com.talkweb.unicom.api;

import com.talkweb.unicom.api.utils.JsonUtils;
import com.talkweb.unicom.api.utils.StringMap;

public class ApiClientTest {

    private static String url = "https://fifa.wo186.tv/api/";
    private static String sid = "h5game";
    private static String secret = "d663bd873d93a76ab2ce11e9";

    private static String getUser(ApiClient.ApiClientImpl apiClient, String openId) throws Exception {
        return apiClient.param("openId", openId).get(url + "wx/user.do", String.class);
    }

    private static String sendTemplateMessage(ApiClient.ApiClientImpl apiClient, String openId) throws Exception {
        String data = "{\"key1\":{\"value\":\"value1\"}, \"key2\":{\"value\":\"value2\"}}";
        StringMap templateData = JsonUtils.fromJson(data, StringMap.class);
        return apiClient.param("openId", openId)
                .param("templateId", "11111")
                .param("url", "http://www.baidu.com")
                .post(url + "wx/sendTemplateMessage.do", templateData, String.class);
    }

    public static void main(String[] args) throws Exception {


        ApiClient.ApiClientImpl apiClient = ApiClient.create(sid, secret);

        String openId = "ojCvV0hmRcPa39xGckwKJQ8M6Wp8";

        System.out.println(getUser(apiClient, openId));

        System.out.println(sendTemplateMessage(apiClient, openId));

    }

}
