package homestay.app.controller.user;

import homestay.app.annotations.VerifiedUser;
import homestay.app.domian.user.UserInfoVO;
import homestay.app.domian.user.UserLoginInfoVO;
import homestay.module.user.entity.User;
import homestay.module.user.service.UserDefine;
import homestay.module.user.service.UserService;
import homestay.module.utils.BaseUtils;
import homestay.module.utils.IpUtils;
import homestay.module.utils.Response;
import homestay.module.utils.SignUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @RequestMapping("/user/login")
    public Response<UserLoginInfoVO> login(@RequestParam(name = "phone") String phone,
                                           @RequestParam(name = "password") String password){

        //合法用户登录
        boolean result = userService.login(phone,password);
        if (!result){
            return new Response<UserLoginInfoVO>(4004);
        }
        User user = userService.getByPhone(phone);
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        userService.refreshUserLoginContext(user.getId(), IpUtils.getIpAddress(request),BaseUtils.currentSeconds());;

        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setGender(user.getGender());
        userInfoVO.setName(user.getUsername());
        userInfoVO.setAvatar(user.getAvatar());
        userInfoVO.setUserId(user.getId());
        userInfoVO.setPhone(user.getPhone());

        UserLoginInfoVO loginInfoVO = new UserLoginInfoVO();
        loginInfoVO.setUserInfo(userInfoVO);
        loginInfoVO.setSign(SignUtils.makeSign(user.getId()));
        return new Response<>(1001,loginInfoVO);
    }

    @RequestMapping("/user/register")
    public Response<UserLoginInfoVO> register(@RequestParam(name = "phone") String phone,
                                              @RequestParam(name = "gender") Integer gender,
                                              @RequestParam(name = "avatar",required = false)String avatar,
                                              @RequestParam(name = "name") String name,
                                              @RequestParam(name = "password")String password,
                                              @RequestParam(name = "country",required = false)String country,
                                              @RequestParam(name = "province",required = false)String province,
                                              @RequestParam(name = "city",required = false)String city){
        //如果用户已经注册过了，则按照登录处理，返回sign
        User user = userService.extractByPhone(phone,"86");
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        BigInteger newUserId;
        if (!BaseUtils.isEmpty(user)){
            //如果用户被禁止登录
            if (user.getIsDeleted().equals(1) || user.getIsBan().equals(1)){
                return new Response<>(1010);
            }
            newUserId = user.getId();
            userService.refreshUserLoginContext(user.getId(),IpUtils.getIpAddress(request),BaseUtils.currentSeconds());;

        }else {
            //注册新用户
            if (UserDefine.isGender(gender)){
                return new Response<>(2015);
            }
            try{
                newUserId = userService.registerUser(name,phone,gender,avatar,password,country,province,city,IpUtils.getIpAddress(request));
            }catch (Exception e){
                return new Response<UserLoginInfoVO>(4004);
            }
        }
        user = userService.getById(newUserId);
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setGender(user.getGender());
        userInfoVO.setName(user.getUsername());
        userInfoVO.setAvatar(user.getAvatar());
        userInfoVO.setPhone(user.getPhone());
        userInfoVO.setUserId(user.getId());

        UserLoginInfoVO loginInfoVO = new UserLoginInfoVO();
        loginInfoVO.setUserInfo(userInfoVO);
        loginInfoVO.setSign(SignUtils.makeSign(user.getId()));
        return new Response<>(1001,loginInfoVO);
    }

}
