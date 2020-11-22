package com.qqlin.demo.mapper;

import com.qqlin.demo.entity.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

/**
 * @author lin.qingquan
 * @version 1.0
 * @date 2020-11-22 19:26
 * @Description:
 */
public interface UserMapper {

    // 新增用户
    @Insert("insert into `report_user` ("
            + "`userId`,`loginName`,`passWord`,`userName`,`userRole`,`phone`,`email`,`icon`,`createDate`,`state`,`userDesc`)  "
            + " values "
            + "(#{userId},#{loginName},#{passWord},#{userName},#{userRole},#{phone},#{email},#{icon},#{createDate},#{state},#{userDesc})")
    int saveUser(UserInfo userInfo);

    // 修改用户名
    @Update("update `report_user` set `userName`= #{userName}"
            + " where `loginName`= #{loginName}")
    int updUserName(UserInfo userInfo);
}
