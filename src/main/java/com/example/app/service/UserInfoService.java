package com.example.app.service;

import com.example.app.exception.BusinessException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 测试 shirojwt service
 *
 * @author admin
 */
@Service
public class UserInfoService {

    /**
     * 这里模拟 用户信息表
     */
    public static final Map<String, Map<String, String>> MYSQL_USER_TABLE = new ConcurrentHashMap<>();
    private String userName = "userName";
    public static String passwordKey = "password";
    public static String saltKey = "salt";
    public static String rolesKey = "roles";
    public static String permissionsKey = "permissions";

    @PostConstruct
    public void init() {
        // 初始化一个管理员账号
        Map<String, String> admin = new HashMap<>(16);
        admin.put(userName, "admin");
        admin.put(UserInfoService.passwordKey, md5("root123"));
        admin.put(UserInfoService.saltKey, "kd8io9w");
        admin.put(UserInfoService.rolesKey, "admin,user");
        admin.put(UserInfoService.permissionsKey, "admin:save,admin:select,admin:update,user:select");
        MYSQL_USER_TABLE.put("admin", admin);
    }

    public Map<String, String> getUserByUserName(String userName) {
        if (userName == null) {
            return null;
        }
        return MYSQL_USER_TABLE.get(userName);
    }

    public Map<String, String> login(String userName, String password) {
        if (userName == null || password == null) {
            return null;
        }
        Map<String, String> user = MYSQL_USER_TABLE.get(userName);
        if (user == null){
            return null;
        }
        if (!user.get(UserInfoService.passwordKey).equals(md5(password))) {
            return null;
        }
        return user;
    }

    public void saveUser(Map<String, String> user) {
        userInfoCheck(user);

        String userNameKey = user.get(userName);
        if (MYSQL_USER_TABLE.containsKey(userNameKey)) {
            BusinessException.create("用户" + userNameKey + "已经存在");
        }
        // 密码加密
        user.put(UserInfoService.passwordKey, md5(user.get(UserInfoService.passwordKey)));

        MYSQL_USER_TABLE.put(userNameKey, user);
    }

    public void updateUser(Map<String, String> user) {
        userInfoCheck(user);

        String userNameKey = user.get(userName);
        if (!MYSQL_USER_TABLE.containsKey(userNameKey)) {
            BusinessException.create("用户" + userNameKey + "不经存在");
        }

        MYSQL_USER_TABLE.put(userNameKey, user);
    }

    private void userInfoCheck(Map<String, String> user) {
        if (user == null || user.isEmpty()) {
            BusinessException.create("用户信息为空");
        }
        if (!user.containsKey(userName)) {
            BusinessException.create("用户名不存在");
        }
        if (!user.containsKey(UserInfoService.passwordKey)) {
            BusinessException.create("用户密码不存在");
        }
        if (!user.containsKey(saltKey)) {
            BusinessException.create("用户密码加盐值不存在");
        }
    }

    private String md5(String password) {
        // 随便模拟一个md5算法
        return "OIIWEI" + password + "MD5";
    }

}
