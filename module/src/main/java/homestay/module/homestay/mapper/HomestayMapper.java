package homestay.module.homestay.mapper;


import homestay.module.homestay.entity.Homestay;
import org.apache.ibatis.annotations.*;
import java.math.BigInteger;
import java.util.List;
@Mapper
public interface HomestayMapper {
    @Select("select * from homestay where id=#{id} and is_deleted = 0")
    Homestay getById(@Param("id") BigInteger id);
    List<Homestay> getAll();
    int update(@Param("homestay") Homestay homestay);
    int insert(@Param("homestay") Homestay homestay);
    @Update("update homestay set is_deleted=1, update_time=#{time} where id=#{id} limit 1")
    int delete(@Param("id") BigInteger id, @Param("time") Integer time);
    List<Homestay> getPagingQuery(@Param("title") String title,@Param("offset") int offset, @Param("pageSize") int pageSize,@Param("cityIds")String cityIds);
    @Select("select * from homestay where id=#{id}")
    Homestay extractById(@Param("id") BigInteger id);
    int getPagingQueryTotal(@Param("title") String title,@Param("cityIds")String cityIds);

}


