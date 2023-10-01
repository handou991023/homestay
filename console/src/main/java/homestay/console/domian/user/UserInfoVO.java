package homestay.console.domian.user;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserInfoVO {
    private String userName;
    private Integer userGender;
    private String userPhone;
    private String userAvatar;

}
