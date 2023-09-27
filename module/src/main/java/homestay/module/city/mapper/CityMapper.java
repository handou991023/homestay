package homestay.module.city.mapper;

import homestay.module.city.entity.City;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigInteger;
import java.util.List;

/**
 *
 */
@Mapper
public interface CityMapper {
    /**
     *
     * @param id
     * @return
     */
    @Select("select * from city where id=#{id} and is_deleted = 0")
    City getById(@Param("id") BigInteger id);

    /**
     *
     * @return
     */
    @Select("select * from city where is_deleted = 0")
    List<City> getAll();

    /**
     *
     * @param city
     * @return
     */
    int update(@Param("city") City city);

    /**
     *
     * @param city
     * @return
     */
    int insert(@Param("city") City city);

    /**
     *
     * @param id
     * @param time
     * @return
     */
    @Update("update city set is_deleted=1, update_time=#{time} where id=#{id} limit 1")
    int delete(@Param("id") BigInteger id, @Param("time") Integer time);

    /**
     *
     * @param name
     * @param offset
     * @param pageSize
     * @return
     */
    List<City> getPagingQuery(@Param("name") String name,@Param("offset") int offset, @Param("pageSize") int pageSize);

    /**
     *
     * @param id
     * @return
     */
    @Select("select * from city where id=#{id}")
    City extractById(@Param("id") Integer id);

    /**
     *
     * @param name
     * @return
     */
    int getPagingQueryTotal(@Param("name") String name);

    /**
     *
     * @param title
     * @return
     */
    List<City> getIdColumn(@Param("title")String title);

    List<City> getCityList(@Param("cityIds") String cityIds);
}
