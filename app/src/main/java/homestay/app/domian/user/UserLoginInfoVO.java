package homestay.app.domian.user;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserLoginInfoVO {
    private UserInfoVO userInfo;
    private String sign;
}