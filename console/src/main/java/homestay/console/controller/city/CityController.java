package homestay.console.controller.city;

import homestay.console.domian.city.CityBaseListVO;
import homestay.console.domian.city.CityListVO;
import homestay.console.domian.city.CityVO;
import homestay.module.city.entity.City;
import homestay.module.city.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@RestController
public class CityController {
    @Autowired
    private CityService cityService;
    @RequestMapping("/city/list")
    public CityBaseListVO cityList(@RequestParam(value = "page") int page,
                                   @RequestParam(name = "name",required = false) String name)
    {int pageSize = 5;//每页数据量
        List<City> cityList = cityService.getPagingQuery(name,page,pageSize);
        List<CityListVO> list = new ArrayList<>();
        for (City city : cityList) {
            CityListVO entry = new CityListVO();
            entry.setId(city.getId());
            entry.setName(city.getName());
            list.add(entry);
        }
        int total = cityService.getPagingQueryTotal(name);
        CityBaseListVO baseListVO = new CityBaseListVO();
        baseListVO.setList(list);
        baseListVO.setTotal(total);
        baseListVO.setPageSize(pageSize);
        return baseListVO;
    }

    @RequestMapping("/city/info")
    public CityVO cityInfo(@RequestParam(name = "cityId")BigInteger id){
        City city = cityService.getCityInfoById(id);
        if(city == null){
            return null;
        }
        CityVO entry = new CityVO();
        Integer createTime = city.getCreateTime();
        long createTime1 = (long)createTime*1000;
        Date date1 = new Date(createTime1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(date1);
        entry.setCreateTime(formattedDate);

        Integer updateTime = city.getUpdateTime();
        long updateTime1 = (long)updateTime*1000;
        Date date = new Date(updateTime1);
        String formattedDate1 = sdf.format(date);
        entry.setUpdateTime(formattedDate1);

        entry.setId(city.getId());
        entry.setName(city.getName());
        entry.setImage(city.getImage());
        return entry;
    }

    @RequestMapping("/city/create")
    public String cityCreate(@RequestParam(name = "id",required = false) BigInteger id,
                        @RequestParam(name = "name") String name,
                        @RequestParam(name = "image") String image){
        try{
            BigInteger result = cityService.editCity(id,name,image);
            return result + "成功";
        }catch (RuntimeException e){
            return e.getLocalizedMessage();
        }
    }

    /**
     *
     * @param id
     * @param name
     * @param image
     * @return
     */
    @RequestMapping("/city/update")
    public String cityUpdate(@RequestParam(name = "id",required = false) BigInteger id,
                           @RequestParam(name = "name") String name,
                           @RequestParam(name = "image") String image){
        try{
            BigInteger result = cityService.editCity(id,name,image);
            return result + "成功";
        }catch (RuntimeException e){
            return e.getLocalizedMessage();
        }
    }
    @RequestMapping("/city/delete")
    public String cityDelete(@RequestParam(name = "cityId")BigInteger id){
        int result = cityService.deleteCity(id);
        return 1 == result ? "成功" : "失败";
    }


}
