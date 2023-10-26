package homestay.module.user.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import homestay.module.user.entity.User;
import homestay.module.user.mapper.UserMapper;
import homestay.module.utils.BaseUtils;
import homestay.module.utils.DataUtils;
import homestay.module.utils.ImageUtils;
import homestay.module.utils.SignUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

import static homestay.module.user.service.UserDefine.GENDER_MALE;


@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    public User getById(BigInteger id){
        return userMapper.getById(id);
    }
    public List<User> getByIds(List<BigInteger> idList){
        StringBuilder userIds = new StringBuilder();
        for (BigInteger id : idList){
            userIds.append(id).append(",");
        }
        userIds.deleteCharAt(userIds.length() - 1);
        return userMapper.getByIds((userIds.toString()));
    }
    public User getByPhone(String phone ,String countryCode){
        return userMapper.getByPhone(phone,countryCode);

    }
    public User getByPhone(String phone){
        return userMapper.getByPhone(phone,"86");
    }
    public User getLogin(String phone,String password){
        return userMapper.getLogin(phone,"86",password);
    }
    public User extractByPhone(String phone, String countryCode){
        return userMapper.extractByPhone(phone,countryCode);
    }

    public int update(User user){
        return userMapper.update(user);
    }
    public void unsafeUpdate(User user){
        int result = userMapper.update(user);
        if(result == 0){
            throw new RuntimeException("update error");
        }
    }
    public int delete(BigInteger userId){
        return userMapper.delete(userId, BaseUtils.currentSeconds());
    }
    public void refreshUserLoginContext(BigInteger id, String ip, int time) {
        User user = new User();
        user.setId(id);
        user.setLastLoginIp(ip);
        user.setLastLoginTime(time);
        user.setUpdateTime(time);
        try {
            update(user);
        } catch (Exception ignored) {

        }

    }
    @Transactional(rollbackFor = Exception.class)
    public BigInteger registerUser(String phone,Integer gender, String avatar,String name,String password,
                                   String country,String province,String city){
        int now = BaseUtils.currentSeconds();
        if (BaseUtils.isEmpty(avatar)) {
            avatar = gender.equals(GENDER_MALE.getCode()) ? ImageUtils.getDefaultMaleAvatar() : ImageUtils.getDefaultFeMaleAvatar();
        }
        User user = new User();
        user.setUsername(name);
        user.setPhone(phone);
        user.setCountryCode("86");
        user.setGender(gender);
        user.setAvatar(avatar);
        user.setPassword(SignUtils.marshal(password));
        user.setCountry(country);

        user.setRegisterTime(now);
        user.setProvince(province);
        user.setCity(city);
        user.setIsBan(0);
        user.setCreateTime(now);
        user.setUpdateTime(now);
        user.setIsDeleted(0);
        userMapper.insert(user);

        return  user.getId();
    }

    public boolean login(String phone,String countryCode,String password,int lifeTime){
        if (lifeTime < 0){
            return false;
        }
        // check phone
        if (!DataUtils.isPhoneNumber(phone) || BaseUtils.isEmpty(countryCode)){
            return false;
        }
        User user = getByPhone(phone,countryCode);
        if (BaseUtils.isEmpty(user)){
            return false;
        }
        return SignUtils.marshal(password).equals(user.getPassword());

    }
    public boolean login(String phone,String password){

        return login(phone, "86",password, SignUtils.getExpirationTime());
    }
    public boolean addToUserService(User user,BigInteger id){
        return true;
    }

}
