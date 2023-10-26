package homestay.module.user.entity;


import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
public class User {

    /**
     * 用户id
     */
    private BigInteger id;

    private String countryCode;

    /**
     * 用户手机号码
     */
    private String phone;

    /**
     * 注册邮箱号
     */
    private String email;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户头像url
     */
    private String avatar;

    /**
     * 用户背景封面
     */
    private String coverImage;

    /**
     * 性别，1-男，2-女
     */
    private Integer gender;

    /**
     * 生日 1999-01-01
     */
    private String birthday;

    private String country;

    private String province;

    private String city;
    /**
     * 注册ip
     */
    private String registerIp;
    /**
     * 最后登录时间
     */
    private Integer lastLoginTime;

    /**
     * 最后登录ip
     */
    private String lastLoginIp;

    /**
     * 是否禁用1-是0-否
     */
    private Integer isBan;

    /**
     * json
     */
    private Integer registerTime;
    private Integer createTime;

    /**
     * 修改时间
     */
    private Integer updateTime;

    /**
     * 是否删除 1-是 0-否
     */
    private Integer isDeleted;

}
