package homestay.module.user.mapper;

import homestay.module.user.entity.User;
import org.apache.ibatis.annotations.*;
import java.math.BigInteger;
import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from user WHERE id=#{id} and is_ban = 0 and is_deleted = 0")
    User getById(@Param("id") BigInteger id);

    @Select("select * from user WHERE id in (${ids}) and is_ban = 0 and is_deleted = 0")
    List<User> getByIds(@Param("ids") String ids);

    @Select("select * from user WHERE id=#{id}")
    User extractById(@Param("id") BigInteger id);

    @Select("select * from user WHERE phone=#{phone} and country_code=#{countryCode} and is_ban = 0 and is_deleted = 0")
    User getByPhone(@Param("phone") String phone, @Param("countryCode") String countryCode);
    @Select("select * from user WHERE phone=#{phone} and country_code=#{countryCode} and password=#{password} and is_ban = 0 and is_deleted = 0")
    User getLogin(@Param("phone") String phone, @Param("countryCode") String countryCode,@Param("password")String password);
    @Select("select * from user WHERE phone=#{phone} and country_code=#{countryCode}")
    User extractByPhone(@Param("phone") String phone, @Param("countryCode") String countryCode);


    int update(@Param("user") User user);

    int insert(@Param("user") User user);

    @Update("update user set is_deleted=1, update_time=#{time} where id=#{id} limit 1")
    int delete(@Param("id") BigInteger id, @Param("time") Integer time);

    User getUserLogin(@Param("phone") String phone,@Param("password") String password);
}
