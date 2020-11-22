package com.qqlin.demo.service;


import com.qqlin.demo.entity.ResponseData;

/**
 * @author lin.qingquan
 * @date 2020-11-9 11:31
 * @Description:
 */
public interface UserToJsonService {

    ResponseData listUser(String username, String password);
}
