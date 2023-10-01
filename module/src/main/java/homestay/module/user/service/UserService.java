package homestay.module.user.service;


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

    public BigInteger insert(User user){
        userMapper.insert(user);
        return user.getId();
    }
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
    public User extractByPhone(String phone, String countryCode){
        return userMapper.extractByPhone(phone,countryCode);
    }
    public User extractByEmail(String email){
        return userMapper.extractByEmail(email);
    }
    public User extractUserByWxOpenId(String wechatOpenId){
        return userMapper.extractUserByWxOpenId(wechatOpenId);
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
    public void refreshUserLoginContext(BigInteger id,String ip, int time){
        User user = new User();
        user.setId(id);
        user.setLastLoginIp(ip);
        user.setLastLoginTime(time);
        user.setUpdateTime(time);
        try{
            update(user);
        }catch (Exception ignored){

        }
    }
    @Transactional(rollbackFor = Exception.class)
    public BigInteger registerUser(String name,String phone,Integer gender, String avatar,String password,
                                   String country,String province,String city,String ipAddress){
        if(BaseUtils.isEmpty(avatar)){
            avatar = gender.equals(GENDER_MALE.getCode()) ? ImageUtils.getDefaultMaleAvatar() :ImageUtils.getDefaultFeMaleAvatar();

        }
        User newUser = new User();
        int now = BaseUtils.currentSeconds();
        newUser.setUsername(name);
        newUser.setPhone(phone);
        newUser.setCountryCode("86");
        newUser.setGender(gender);
        newUser.setAvatar(avatar);
        newUser.setPassword(SignUtils.marshal(password));
        newUser.setCountry(country);
        newUser.setProvince(province);
        newUser.setCity(city);
        newUser.setRegisterTime(now);
        newUser.setLastLoginTime(now);
        newUser.setRegisterIp(ipAddress);
        newUser.setLastLoginIp(ipAddress);
        newUser.setCreateTime(now);
        newUser.setIsDeleted(0);
        insert(newUser);

        return  newUser.getId();
    }

    public boolean login(String phone,String countryCode,String password,
                         boolean noPasswd,boolean remember,int lifeTime){
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
        return noPasswd || SignUtils.marshal(password).equals(user.getPassword());

    }
    public boolean login(String phone,String password){
        return login(phone,"86",password,false,true,SignUtils.getExpirationTime());
    }
    public boolean login(String phone,  String password,boolean noPasswd, boolean remember) {
        return login(phone, "86",password, noPasswd, remember, SignUtils.getExpirationTime());
    }

    public boolean bindPhoneForEmailUser(User user,String countryCode,String phone){
        if(DataUtils.isPhoneNumber(phone)){
            return false;
        }
        User upUser = new User();
        upUser.setId(user.getId());
        upUser.setPhone(phone);
        upUser.setCountryCode(countryCode);
        try {
            update(user);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public boolean changePassword(User user,String password){
        User upUser = new User();
        upUser.setId(user.getId());
        upUser.setPassword(SignUtils.marshal(password));
        try {
            update(upUser);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public boolean changeAvatar(User user,String avatar){
        User upUser = new User();
        upUser.setId(user.getId());
        upUser.setAvatar(avatar);
        try {
            update(upUser);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public boolean changeUsername(User user,String username){
        User upUser = new User();
        upUser.setId(user.getId());
        upUser.setUsername(username);
        try {
            update(upUser);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public boolean changeCoverImage(User user,String image){
        User upUser = new User();
        upUser.setId(user.getId());
        upUser.setCoverImage(image);
        try {
            update(upUser);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public boolean changePersonalProfile(User user,String personalProfile){
        User upUser = new User();
        upUser.setId(user.getId());
        upUser.setPersonalProfile(personalProfile);
        try {
            update(upUser);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public boolean changeGender(User user,Integer gender){
        if(UserDefine.isGender(gender)){
            throw new RuntimeException("not right gender");
        }
        User upUser = new User();
        upUser.setId(user.getId());
        upUser.setGender(gender);
        try {
            update(upUser);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public User getByUsername(String usermapper){
        return userMapper.getByUsername(usermapper);
    }

    public List<User> getUsersForConsole(int page, int pageSize, String username, String phone) {
        int begin = (page - 1) * pageSize;
        return userMapper.getUsersForConsole(begin, pageSize, "desc", username,phone);
    }

    public int getUsersTotalForConsole(String username, String phone) {
        return userMapper.getUsersTotalForConsole(username,phone);
    }

    public String getUserIdsForSearch(String username) {
        if (BaseUtils.isEmpty(username)) {
            return "-1";
        }

        List<User> users = userMapper.getUsersByUsername(username);
        String userIds;
        if (BaseUtils.isEmpty(users)) {
            userIds = "-1";
        } else {
            StringBuffer bUserIds = new StringBuffer();
            for (User user:users) {
                bUserIds.append(user.getId()).append(",");
            }
            bUserIds.deleteCharAt(bUserIds.length() - 1);
            userIds = bUserIds.toString();
        }

        return userIds;
    }
}
