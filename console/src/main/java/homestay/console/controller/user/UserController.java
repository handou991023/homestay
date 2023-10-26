package homestay.console.controller.user;


import com.alibaba.fastjson.JSON;
import homestay.console.annotations.VerifiedUser;
import homestay.console.domian.user.UserInfoVO;
import homestay.module.user.entity.User;
import homestay.module.user.service.UserService;
import homestay.module.utils.BaseUtils;
import homestay.module.utils.IpUtils;
import homestay.module.utils.Response;
import homestay.module.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @RequestMapping("/user/login/web")
    public Response<UserInfoVO> loginWeb(@VerifiedUser User loginUser,
                                         HttpSession httpSession,
                                         @RequestParam(name = "phone")String phone,
                                         @RequestParam(name = "password")String password){


        if (!BaseUtils.isEmpty(loginUser)) {
            return new Response<>(4004);
        }
        User user = userService.getLogin(phone,password);
        if (user == null){
            return new Response<>(4004);
        }
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        userService.refreshUserLoginContext(user.getId(), IpUtils.getIpAddress(request), BaseUtils.currentSeconds());

        UserInfoVO userInfo = new UserInfoVO();
        userInfo.setUserGender(user.getGender());
        userInfo.setUserName(user.getUsername());
        userInfo.setUserPhone(user.getPhone());
        userInfo.setUserAvatar(user.getAvatar());

        // 写session
        httpSession.setAttribute(SpringUtils.getProperty("application.session.key"), JSON.toJSONString(user));

        return new Response<>(1001, userInfo);
    }
}
