package com.qqlin.demo.schedule;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qqlin.demo.entity.ResponseData;
import com.qqlin.demo.entity.UserInfo;
import com.qqlin.demo.mapper.UserMapper;
import com.qqlin.demo.service.UserToJsonService;
import com.qqlin.demo.utils.DateUtil;
import com.qqlin.demo.utils.HttpClient;
import com.qqlin.demo.utils.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lin.qingquan
 * @version 1.0
 * @date 2020-11-22 19:17
 * @Description: 定时任务，每 30分钟更新用户数据
 */
@RestController
public class ScheduledServiceToUser {

    private static Logger logger = LoggerFactory.getLogger(ScheduledServiceToUser.class);

    @Autowired
    HttpClient httpClient;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserToJsonService userToJsonService;

    /**
     * 定时任务：30分钟 刷新一次
     *
     * @return ResponseData
     */
    @ResponseBody
//    @Scheduled(cron = "0 */30 * * * ?")
    @Scheduled(cron = "0 */1 * * * ?")
    public ResponseData taskSyncSchedulerToUser() throws IOException {

        //调用的url
        String url = "http://localhost:6019/listUserFromUrl";

        //参数
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> data = null;

        try {
            ResponseData responseData = httpClient.doGet(url, map);
            //远程调用返回的结果集
            data = (Map) responseData.getData();
            logger.debug("data: " + data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*模拟接口数据*/
        /*Map<String, Object> data = null;

        ResponseData responseData = new ResponseData();
        File userJsonFile = new File("src/main/resources/response.json");
        InputStream userInputStream = new FileInputStream(userJsonFile);

        String jsonString = IOUtils.toString(userInputStream, "utf-8");
        HashMap hashMap = JSON.parseObject(jsonString, HashMap.class);
        data = hashMap;*/
        /*模拟接口数据*/


        //Json数据解包，增量更新表
        if (data.get("code").equals(200)) {
            JSONObject user_json = new JSONObject(data);

            final String data1 = user_json.getString("data");
            final JSONObject jsonObject = JSONObject.parseObject((data1));


            //获取嵌套json里key为data的数据
            final String data2 = jsonObject.getString("data");
            JSONObject jsonObject1 = JSONObject.parseObject(data2);

            //获得嵌套json里data的数据
            JSONArray array = new JSONArray(jsonObject1.getJSONArray("data"));

            try {
                for (int i = 0; i < array.size(); i++) {
                    final JSONObject jsonObject2 = array.getJSONObject(i);

                    String operate_type = jsonObject2.getString("operate_type");

                    //如果 "operate_type" = add,则新增表
                    if (operate_type.equals("add")) {

                        String loginName = jsonObject2.getString("name");
                        String userName = jsonObject2.getString("alias");
                        String password = jsonObject2.getString("password");
                        String phone_number = jsonObject2.getString("phone_number");
                        String email = jsonObject2.getString("email");
                        String userRole = "dev";

                        String userIdFormLoginName = loginName + DateUtil.getNowDateTime();
                        String userId = MD5Util.MD5Encode(userIdFormLoginName, "UTF-8");

                        UserInfo userInfo = new UserInfo();
                        userInfo.setUserId(userId);
                        userInfo.setLoginName(loginName);
                        userInfo.setUserName(userName);
                        userInfo.setUserRole(userRole);
                        userInfo.setPassWord(password);
                        userInfo.setPhone(phone_number);
                        userInfo.setEmail(email);
                        userInfo.setCreateDate(new Date());
                        logger.debug("userInfo:" + userInfo);
                        userMapper.saveUser(userInfo);

                    }

                    //如果 "operate_type" = update,则更新表
                    if (operate_type.equals("update")) {
                        String loginName = jsonObject2.getString("name");
                        String userName = jsonObject2.getString("alias");
                        String password = jsonObject2.getString("password");
                        String phone_number = jsonObject2.getString("phone_number");
                        String email = jsonObject2.getString("email");

                        UserInfo userInfo = new UserInfo();
                        userInfo.setLoginName(loginName);
                        userInfo.setUserName(userName);
                        userInfo.setPassWord(password);
                        userInfo.setPhone(phone_number);
                        userInfo.setEmail(email);
                        userInfo.setUpdateDate(new Date());
                        logger.debug("userInfo:" + userInfo);
                        userMapper.updUserName(userInfo);
                    }
                }
                return new ResponseData(200, "添加成功", null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ResponseData(400, "没有渠道接口的数据", null);
    }

    /**
     * 获取用户中心的接口数据
     *
     * @return ResponseData
     */
    @RequestMapping("/listUserFromUrl")
    public ResponseData listBusiness() {
        return userToJsonService.listUser("userName", "password");
    }
}
