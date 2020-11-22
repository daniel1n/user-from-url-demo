package com.qqlin.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo {

    private String userId;
    private String loginName;
    private String passWord;
    private String userName;
    private String userRole;
    private String phone;
    private String email;
    private String icon;
    private Date createDate;
    private String state;
    private String userDesc;
    private Date updateDate;

}
