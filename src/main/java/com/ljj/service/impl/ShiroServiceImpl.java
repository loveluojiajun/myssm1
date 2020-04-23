package com.ljj.service.impl;

import com.ljj.mapper.ShiroMapper;
import com.ljj.entity.Permission;
import com.ljj.entity.User;
import com.ljj.service.ShiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/10.
 */
@Service("shiroService")
public class ShiroServiceImpl implements ShiroService {

    @Autowired
    private ShiroMapper shiroMapper;

    public User getUserByUserName(String username) {
        //根据账号获取账号密码
        User userByUserName = shiroMapper.getUserByUserName(username);
        return userByUserName;
    }

    public List<Permission> getPermissionsByUser(User user) {
        //获取到用户角色userRole
        List<Integer> roleId = shiroMapper.getUserRoleByUserId(user.getId());
        List<Permission> perArrary = new ArrayList<>();

        if (roleId!=null&&roleId.size()!=0) {
            //根据roleid获取peimission
            for (Integer i : roleId) {
                perArrary.addAll(shiroMapper.getPermissionsByRoleId(i));
            }
        }

        System.out.println(perArrary);
        return perArrary;
    }


}
