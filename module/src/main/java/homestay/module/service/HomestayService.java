package homestay.module.service;

import homestay.module.entity.City;
import homestay.module.entity.Homestay;
import homestay.module.mapper.HomestayMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
@Service
public class HomestayService {

    @Resource
    private HomestayMapper homestayMapper;
    @Autowired
    private CityService cityService;
    public Homestay getHomestayInfoById(BigInteger id){

        return homestayMapper.getById(id);
    }
    public List<Homestay> getAllHomestayAllInfo(){
        return  homestayMapper.getAll();
    }

    public List<Homestay> getPagingQuery(String title,int page, int pageSize){
        int offset = (page-1)*pageSize;
        List<City> cityList = cityService.getIdColumn(title);
        StringBuilder ids = new StringBuilder();
        String cityIds = null;
        if (cityList.size() > 0){
            for (City city : cityList) {
                ids.append( city.getId()  + ",");
            }
            ids.deleteCharAt(ids.length()-1);
            cityIds = ids.toString();
        }
        return homestayMapper.getPagingQuery(title,offset,pageSize,cityIds);
    }
    public Homestay extractById(BigInteger id){

        return homestayMapper.extractById(id);
    }

    public int getPagingQueryTotal(String title){
        List<City> cityList = cityService.getIdColumn(title);
        StringBuilder ids = new StringBuilder();
        String cityIds = null;
        if (cityList.size() >0) {
            for (City city : cityList) {
                ids.append(city.getId()).append(",");
            }
            ids.deleteCharAt(ids.length()-1);
            cityIds = ids.toString();
        }
        return homestayMapper.getPagingQueryTotal(title,cityIds);
    }

    public BigInteger editHomestay(BigInteger id,BigInteger cityId,String images, String title, String location, BigDecimal longitude, BigDecimal latitude, String phone, String surroundings){
        int timestamp = (int)(System.currentTimeMillis()/1000);

        City city = cityService.getCityInfoById(cityId);
        if (city == null){
            throw new RuntimeException("此cityId不存在");
        }
        if (ObjectUtils.isEmpty(images)){
            throw new RuntimeException("images输入错误");
        }
        if (ObjectUtils.isEmpty(title)){
            throw new RuntimeException("title输入错误");
        }
        if (ObjectUtils.isEmpty(location)) {
            throw new RuntimeException("location输入错误");
        }
        if (latitude == null) {
            throw new RuntimeException("latitude输入错误");
        }
        if (longitude == null) {
            throw new RuntimeException("longitude输入错误");
        }
        if (ObjectUtils.isEmpty(phone)) {
            throw new RuntimeException("phone输入错误");
        }
        Homestay homestay = new Homestay();
        homestay.setCityId(cityId);
        homestay.setImages(images);
        homestay.setTitle(title);
        homestay.setLocation(location);
        homestay.setLongitude(longitude);
        homestay.setLatitude(latitude);
        homestay.setPhone(phone);
        homestay.setSurroundings(surroundings);
        homestay.setUpdateTime(timestamp);
        homestay.setIsDeleted(0);

        if(ObjectUtils.isEmpty(id)){
            homestay.setCreateTime(timestamp);
            homestayMapper.insert(homestay);
            id = homestay.getId();

        }else {
            Homestay home = homestayMapper.getById(id);
            if(ObjectUtils.isEmpty(home)){
                throw new RuntimeException("此id不存在，请重新输入");
            }
            homestay.setId(id);
            homestayMapper.update(homestay);
        }
        return id;

    }
    public int deleteHomestay(BigInteger id){
        return homestayMapper.delete(id ,(int)(System.currentTimeMillis()/1000));
    }

}


