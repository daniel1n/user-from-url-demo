package com.qqlin.demo.service.impl;

import com.qqlin.demo.entity.ResponseData;
import com.qqlin.demo.service.UserToJsonService;
import com.qqlin.demo.utils.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lin.qingquan
 * @date 2020-11-9 11:33
 * @Description:
 */
@Service
public class UserToJsonServiceImpl implements UserToJsonService {

    @Autowired
    HttpClient httpClient;

    @Override
    public ResponseData listUser(String username, String password) {

        String url = "http://10.194.160.100:8007/upermission/getModifyLog/v2/role";
        String token = getToken(username, password);
        Map<String, Object> map = new HashMap<>();

        //参数
        /*RequireQuery requireQuery = new RequireQuery();
        requireQuery.setTimestamp(System.currentTimeMillis());
        requireQuery.setTimestamp(1605059050);
        requireQuery.setData_type(new int[]{6});
        requireQuery.setUnit_id(new String[]{
                "5bce98b6f48c002f1c2f8596",
                "5d0ba222b6c3f87fa5fcba44",
                "5bce98caf48c002f4625ec5d",
                "5bce994af48c002f5255b72d",
                "5beb91a3f48c00238ad731f6",
                "5f19072bde1b17992c996cf0",
        });
        String jsonQuery = JSONObject.toJSONString(requireQuery);
        JSONObject query = JSONObject.parseObject(jsonQuery);*/

        //参数
        // 30分钟之前修改的信息
        String timestamp = String.valueOf((System.currentTimeMillis() / 1000) - 30 * 60);
//        String timestamp = "1605059050";
        String query = "{\"timestamp\": " + timestamp + ", \"unit_id\": [\"5beb91a3f48c00238ad731f6\", \"5d0ba222b6c3f87fa5fcba44\",\"5f19072bde1b17992c996cf0\",\"5bce98b6f48c002f1c2f8596\", \"5bce98caf48c002f4625ec5d\",\"5bce994af48c002f5255b72d\", \"5beb91a3f48c00238ad731f6\"], \"data_type\": [6]}";
        map.put("query", query);
        map.put("token", token);

        ResponseData responseData = null;
        try {
            responseData = httpClient.doPost(url, map);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseData;
    }

    /**
     * 获取外部接口的token
     *
     * @return token
     */
    private String getToken(String username, String password) {

        //调用的url
        String url = "http://localhost:8000/upermission/api/user/token/2";
        //参数
        String passwordMd5 = MD5(password);
        String timestamp = String.valueOf(System.currentTimeMillis());
        String key = "theKey";
        String secret = "e533ca102340f1962b3461035103d706";
        String text = key + secret + timestamp + username + passwordMd5;
        String sign = MD5(text);

        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("timestamp", timestamp);
        map.put("key", key);
        map.put("sign", sign);

        Map<String, Object> data = null;
        String access_token = null;
        try {
            ResponseData responseData = httpClient.doGet(url, map);
            //远程调用返回的结果集
            data = (Map) responseData.getData();
            access_token = (String) data.get("data");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return access_token;
    }

    private String MD5(String str) {
        String md5 = DigestUtils.md5DigestAsHex(str.getBytes());
        return md5;
    }
}
