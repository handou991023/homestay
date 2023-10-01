package homestay.app.domian.user;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;
@Data
@Accessors(chain = true)
public class UserInfoVO {
    private BigInteger userId;
    private String name;
    private Integer gender;
    private String phone;
    private String avatar;
}
