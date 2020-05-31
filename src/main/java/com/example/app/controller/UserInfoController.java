package com.example.app.controller;

import com.example.app.exception.BusinessException;
import com.example.app.service.UserInfoService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.guzt.starter.shirojwt.util.JwtUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 测试 shirojwt
 *
 * @author admin
 */
@RestController
@RequestMapping("/api")
public class UserInfoController {

    @Resource
    private UserInfoService userInfoService;

    @PostMapping("login")
    public Map<String, String> login(String userName, String password) {
        Map<String, String> loginInfo = userInfoService.login(userName, password);
        if (loginInfo == null || loginInfo.isEmpty()) {
            BusinessException.create("用户名或密码错误");
        }

        loginInfo.put("token", JwtUtil.sign(userName, loginInfo.get(UserInfoService.passwordKey), loginInfo.get(UserInfoService.saltKey)));
        return loginInfo;
    }

    @PostMapping("init")
    public String init() {
        userInfoService.init();
        return "success";
    }

    @RequiresRoles("admin")
    @PostMapping("saveUser")
    public String saveUser(@RequestBody  Map<String, String> user) {
        userInfoService.saveUser(user);
        return "success";
    }

    @RequiresPermissions("admin:update")
    @PutMapping("updateUser")
    public String updateUser(@RequestBody Map<String, String> user) {
        userInfoService.updateUser(user);
        return "success";
    }

    @RequiresRoles(value = {"admin","user"})
    @GetMapping("getUserInfoByUserName")
    public Map<String, String> getUserInfoByUserName(String userName) {
        return userInfoService.getUserByUserName(userName);
    }

    /**
     * 这里采用 url 权限控制，非注解控制
     *
     * @param userName 用户名称
     * @return Map
     */
    @GetMapping("selectUserInfoByUserName")
    public Map<String, String> selectUserInfoByUserName(String userName) {
        return userInfoService.getUserByUserName(userName);
    }

    @GetMapping("logout")
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            subject.logout();
        }
        return "退出成功";
    }

}
