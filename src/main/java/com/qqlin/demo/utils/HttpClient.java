package com.qqlin.demo.utils;

import com.alibaba.fastjson.JSON;
import com.qqlin.demo.entity.ResponseData;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 远程调用工具类
 */
@Component
public class HttpClient {
    private CloseableHttpClient httpClient;

    public HttpClient() {
        // 1 创建httpClient，相当于打开浏览器
        httpClient = HttpClients.createDefault();
    }

    /**
     * get请求
     *
     * @param url
     * @param map
     * @return HttpResult
     * @throws Exception
     */
    public ResponseData doGet(String url, Map<String, Object> map) throws Exception {

        // 声明URIBuilder
        URIBuilder uriBuilder = new URIBuilder(url);

        // 判断参数map是否为非空
        if (map != null) {
            // 遍历参数
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                // 设置参数
                uriBuilder.setParameter(entry.getKey(), entry.getValue().toString());
            }
        }

        // 2 创建httpGet对象，相当于设置url请求地址
        HttpGet httpGet = new HttpGet(uriBuilder.build());

        // 3 使用HttpClient执行httpGet，相当于按回车，发起请求
        return getResponseData(httpGet);
    }

    /**
     * post请求
     *
     * @param url
     * @param map
     * @return com.example.HttpClient.HttpResult
     * @throws Exception
     */
    public ResponseData doPost(String url, Map<String, Object> map) throws Exception {
        // 声明httpPost请求
        HttpPost httpPost = new HttpPost(url);

        // 判断map不为空
        if (map != null) {
            // 声明存放参数的List集合
            List<NameValuePair> params = new ArrayList<>();

            // 遍历map，设置参数到list中
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                params.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            }

            // 创建form表单对象
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, "UTF-8");

            // 把表单对象设置到httpPost中
            httpPost.setEntity(formEntity);
        }

        // 使用HttpClient发起请求，返回response
        return getResponseData(httpPost);
    }

    /**
     * Put请求
     *
     * @param url
     * @param map
     * @return com.example.HttpClient.HttpResult
     * @throws Exception
     */
    public ResponseData doPut(String url, Map<String, Object> map) throws Exception {
        // 声明httpPost请求
        HttpPut httpPut = new HttpPut(url);

        // 判断map不为空
        if (map != null) {
            // 声明存放参数的List集合
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            // 遍历map，设置参数到list中
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                params.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            }

            // 创建form表单对象
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, "UTF-8");

            // 把表单对象设置到httpPost中
            httpPut.setEntity(formEntity);
        }

        // 使用HttpClient发起请求，返回response
        return getResponseData(httpPut);
    }

    /**
     * Delete请求
     *
     * @param url
     * @param map
     * @return com.example.HttpClient.HttpResult
     * @throws Exception
     */
    public ResponseData doDelete(String url, Map<String, Object> map) throws Exception {

        // 声明URIBuilder
        URIBuilder uriBuilder = new URIBuilder(url);

        // 判断参数map是否为非空
        if (map != null) {
            // 遍历参数
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                // 设置参数
                uriBuilder.setParameter(entry.getKey(), entry.getValue().toString());
            }
        }

        // 2 创建httpGet对象，相当于设置url请求地址
        HttpDelete httpDelete = new HttpDelete(uriBuilder.build());

        // 3 使用HttpClient执行httpGet，相当于按回车，发起请求
        return getResponseData(httpDelete);
    }

    /**
     * 使用HttpClient执行httpGet，相当于按回车，发起请求
     *
     * @param httpUriRequest 请求
     * @return
     * @throws IOException
     */
    private ResponseData getResponseData(HttpUriRequest httpUriRequest) throws IOException {
        // 3 使用HttpClient执行httpGet，相当于按回车，发起请求
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpUriRequest);
        } catch (IOException e) {
            ResponseData responseData = new ResponseData();

            responseData.setCode(404);
            responseData.setMessage("请求失败");
            return responseData;
        }

        // 4 解析结果，封装返回对象httpResult，相当于显示相应的结果
        // 状态码
        // response.getStatusLine().getStatusCode();
        // 响应体，字符串，如果response.getEntity()为空，下面这个代码会报错,所以解析之前要做非空的判断
        // EntityUtils.toString(response.getEntity(), "UTF-8");
        ResponseData responseData = new ResponseData();
        // 解析数据封装HttpResult
        if (response.getEntity() != null) {
            //httpResult = new HttpResult(response.getStatusLine().getStatusCode(),EntityUtils.toString(response.getEntity(),"UTF-8"));
            responseData.setCode(response.getStatusLine().getStatusCode());
            String s = EntityUtils.toString(response.getEntity(), "UTF-8");

            responseData.setData(JSON.parseObject(s, HashMap.class));

        } else {
            //httpResult = new HttpResult(response.getStatusLine().getStatusCode(), "");
            responseData.setCode(response.getStatusLine().getStatusCode());
            //httpResult.setBody("");
        }

        // 返回
        return responseData;
    }
}
