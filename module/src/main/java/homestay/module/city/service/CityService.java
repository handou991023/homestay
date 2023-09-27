package homestay.module.city.service;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import homestay.module.city.entity.City;
import homestay.module.city.mapper.CityMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.math.BigInteger;

import java.util.List;

@Service
public class CityService {
    @Resource
    private CityMapper cityMapper;
    /**
     *
     * @return
     */
    public List<City> getAllCityAllInfo(){
        return cityMapper.getAll();
    }

    /**
     *
     * @param id
     * @return
     */
    public City getCityInfoById(BigInteger id){

        return cityMapper.getById(id);
    }

    /**
     *
     * @param name
     * @return
     */
    public int getPagingQueryTotal(String name){
        return cityMapper.getPagingQueryTotal(name);
    }

    /**
     *
     * @param title
     * @return
     */
    public List<City> getIdColumn(String title){
        return cityMapper.getIdColumn(title);
    }

    /**
     *
     * @param name
     * @param page
     * @param pageSize
     * @return
     */
    public List<City> getPagingQuery(String name, int page, int pageSize){
        int offset = (page-1)*pageSize;
        return cityMapper.getPagingQuery(name,offset,pageSize);
    }

    public List<City> getCityList(List<BigInteger> cityIdList){
        StringBuilder ids = new StringBuilder();
        String cityIds = "0";
        if (cityIdList.size() > 0){
            for (BigInteger cityId : cityIdList){
                ids.append(cityId).append(",");
            }
            ids.deleteCharAt(ids.length()-1);
            cityIds = ids.toString();
        }
        return cityMapper.getCityList(cityIds);
    }

    /**
     * @param id
     * @param name
     * @param image
     * @return
     */
    public BigInteger editCity(BigInteger id, String name, String image){
        int timestamp = (int)(System.currentTimeMillis()/1000);
        if (name == null || name.equals("")){
            throw new RuntimeException("name输入错误,请重新输入");
        } else if (image == null || image.equals("")){
            throw new RuntimeException("image输入错误,请重新输入");
        }else{
            City city = new City();
            city.setImage(image);
            city.setName(name);
            city.setUpdateTime(timestamp);
            city.setIsDeleted(0);
            if(ObjectUtils.isEmpty(id)){
                city.setCreateTime(timestamp);
                cityMapper.insert(city);
                id = city.getId();
            }else {
                City cityId = cityMapper.getById(id);
                if(ObjectUtils.isEmpty(cityId)){
                    throw new RuntimeException("此id不存在，请重新输入");
                }
                city.setId(id);
                city.setIsDeleted(0);
                cityMapper.update(city);
            }
        }

        return id;
    }

    /**
     *
     * @param id
     * @return
     */
    public int deleteCity(BigInteger id){
        return cityMapper.delete(id,(int)(System.currentTimeMillis()/1000));
    }
}
