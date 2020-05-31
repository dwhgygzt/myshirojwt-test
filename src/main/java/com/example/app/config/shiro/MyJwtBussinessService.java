package com.example.app.config.shiro;

import com.example.app.service.UserInfoService;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.guzt.starter.shirojwt.component.JwtBussinessService;
import org.guzt.starter.shirojwt.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * JwtBussinessService 配置类
 *
 * @author admin
 */
@Service
public class MyJwtBussinessService extends JwtBussinessService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public MyJwtBussinessService() {
        logger.info("MyJwtBussinessService 初始化");
    }

    @Override
    public AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals, String realmName) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        logger.debug("进入 授权 doGetAuthorizationInfo");
        logger.debug("the toke is  {}", principals.toString());
        String userName = JwtUtil.getUserName(principals.toString());

        Map<String, String> user = UserInfoService.MYSQL_USER_TABLE.get(userName);
        String spit = ",";
        // 该用户具有哪些权限
        for (String permission : user.get(UserInfoService.permissionsKey).split(spit)) {
            authorizationInfo.addStringPermission(permission);
        }
        // 该用户具有哪些角色
        for (String role : user.get(UserInfoService.rolesKey).split(spit)) {
            authorizationInfo.addRole(role);
        }

        return authorizationInfo;
    }

    @Override
    public AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth, String realmName) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        logger.debug("进入 认证 doGetAuthenticationInfo");
        logger.debug("the toke is  {}", token);
        // token是否过期
        Date expiresDate = JwtUtil.getExpiresAt(token);
        if (expiresDate == null) {
            throw new IncorrectCredentialsException("token 不正确");
        } else if (expiresDate.before(new Date())) {
            throw new ExpiredCredentialsException("token 过期了");
        }
        // 验证 token是否有效
        String userName = JwtUtil.getUserName(token);
        if (userName == null) {
            throw new IncorrectCredentialsException("token 不正确");
        }
        // 验证用户是否存在
        Map<String, String> user = UserInfoService.MYSQL_USER_TABLE.get(userName);
        if (user == null) {
            throw new UnknownAccountException("用户不存在");
        }
        // 用户最终认证
        String password = user.get(UserInfoService.passwordKey);
        String salt = user.get(UserInfoService.saltKey);
        return new SimpleAuthenticationInfo(token, password, ByteSource.Util.bytes(salt), realmName);
    }

    @Override
    public void onAccessDenied(HttpServletRequest request, HttpServletResponse response, boolean isTokenExists, ShiroException ex) throws IOException {
        defaultPrintJson(response, "{\"code\":\"-1\",\"data\":{\"bussinessCode\":\"401\"},\"message\":\"" + ex.getLocalizedMessage() + "\"}");
    }

    @Override
    public String refreshOldToken(String oldToken) {
        String userName = JwtUtil.getUserName(oldToken);
        Map<String, String> user = UserInfoService.MYSQL_USER_TABLE.get(userName);
        return JwtUtil.sign(userName, user.get(UserInfoService.passwordKey), user.get(UserInfoService.saltKey));
    }
}
