package homestay.console;

import com.alibaba.fastjson.JSON;
import homestay.console.annotations.VerifiedUser;
import homestay.module.user.entity.User;
import homestay.module.user.service.UserService;
import homestay.module.utils.BaseUtils;
import homestay.module.utils.SignUtils;
import homestay.module.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigInteger;
@Slf4j
public class UserAuthorityResolver implements HandlerMethodArgumentResolver {
    @Autowired
    private UserService userService;
    private final boolean isCheckAuthority;

    public UserAuthorityResolver(@NotNull ApplicationArguments appArguments) {
        String[] arguments = appArguments.getSourceArgs();
        if (arguments == null || arguments.length <= 3) {
            isCheckAuthority = true;
            return ;
        }

        String isMockUserLogin = arguments[2];
        if (BaseUtils.isEmpty(isMockUserLogin)) {
            isCheckAuthority = true;
        } else {
            isCheckAuthority = Boolean.parseBoolean(isMockUserLogin);
        }
        log.info("Check user authority: {}", Boolean.toString(isCheckAuthority));
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> type = parameter.getParameterType();
        return type.isAssignableFrom(User.class) && parameter.hasParameterAnnotation(VerifiedUser.class);
    }

    @Override
    public Object resolveArgument(@NotNull MethodParameter parameter,
                                  ModelAndViewContainer container,
                                  @NotNull NativeWebRequest request,
                                  WebDataBinderFactory factory) {

        if (isCheckAuthority) {
            String isAppS = SpringUtils.getProperty("application.isapp");
            boolean isApp = isAppS.equals("1");
            HttpServletRequest sRequest = (HttpServletRequest)request.getNativeRequest();
            if(isApp){
                String signKey = SpringUtils.getProperty("application.sign.key");
                String sign = sRequest.getHeader(signKey);
                if(!BaseUtils.isEmpty(sign)){
                    BigInteger userId = SignUtils.parseSign(sign);
                    log.info("userId: {}, sign: {}", userId, sign);
                    if (!BaseUtils.isEmpty(userId)) {
                        return userService.getById(userId);
                    }
                }
                return null;
            }else{
                HttpSession session = sRequest.getSession(false);
                if(BaseUtils.isEmpty(session)){
                    return null;
                }
                String signKey = SpringUtils.getProperty("application.session.key");
                Object value = session.getAttribute(signKey);
                if (value == null) {
                    return null;
                }

                String sValue = (String)value;
                return JSON.parseObject(sValue, User.class);
            }
        }
        return userService.getById(BigInteger.valueOf(1));
    }
}
