package homestay.console.controller;

import com.alibaba.fastjson.JSON;
import homestay.console.domian.*;
import homestay.module.entity.City;
import homestay.module.entity.Homestay;
import homestay.module.service.CityService;
import homestay.module.service.HomestayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class HomestayController {
    @Autowired
    private HomestayService homestayService;
    @Autowired
    private CityService cityService;
    private final int pageSize = 5;//每页数据量
    @RequestMapping("/homestay/create")
    public String homestayCreate(@RequestParam(name = "homestayId",required = false)BigInteger homestayId,
                               @RequestParam(name = "cityId")BigInteger cityId,
                               @RequestParam(name = "images")String images,
                               @RequestParam(name = "title")String title,
                               @RequestParam(name = "location")String location,
                               @RequestParam(name = "longitude")BigDecimal longitude,
                               @RequestParam(name = "latitude") BigDecimal latitude,
                               @RequestParam(name = "phone")String phone,
                               @RequestParam(name = "surroundings",required = false)String surroundings) {

        try {
                BigInteger result = homestayService.editHomestay(homestayId, cityId, images, title.trim(), location.trim(), longitude, latitude, phone.trim(), surroundings);
                return result + "成功 ";

        }catch(RuntimeException e){
                return e.getLocalizedMessage();
            }
    }
    @RequestMapping("/homestay/update")
    public String homestayUpdate(@RequestParam(name = "homestayId",required = false)BigInteger homestayId,
                                 @RequestParam(name = "cityId")BigInteger cityId,
                                 @RequestParam(name = "images")String images,
                                 @RequestParam(name = "title")String title,
                                 @RequestParam(name = "location")String location,
                                 @RequestParam(name = "longitude")BigDecimal longitude,
                                 @RequestParam(name = "latitude") BigDecimal latitude,
                                 @RequestParam(name = "phone")String phone,
                                 @RequestParam(name = "surroundings",required = false)String surroundings) {

        try {
            BigInteger result = homestayService.editHomestay(homestayId, cityId, images, title.trim(), location.trim(), longitude, latitude, phone.trim(), surroundings);
            return result + "成功 ";

        }catch(RuntimeException e){
            return e.getLocalizedMessage();
        }
    }
    @RequestMapping("/homestay/delete")
    public String homestayDelete(@RequestParam(name = "homestayId")BigInteger homestayId){
        int result = homestayService.deleteHomestay(homestayId);
        return  1 == result ? "成功" : "失败";
    }

    @RequestMapping("/homestay/info")
    public HomestayVO homestayInfo(@RequestParam(name = "homestayId")BigInteger id){
        Homestay homestay = homestayService.getHomestayInfoById(id);
        if(homestay == null){
            return null;
        }
        HomestayVO entry = new HomestayVO();
        Integer createTime = homestay.getCreateTime();
        long creTime = (long)createTime*1000;
        Date createDate = new Date(creTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 定义日期格式
        String formattedCreateDate = sdf.format(createDate);
        entry.setCreateTime(formattedCreateDate);

        Integer updateTime = homestay.getUpdateTime();
        long UDTime = (long)updateTime*1000;
        Date updateDate = new Date(UDTime);
        String formattedUpdateDate = sdf.format(updateDate);
        entry.setUpdateTime(formattedUpdateDate);

        City city = cityService.getCityInfoById(homestay.getCityId());
        entry.setCityName(city.getName());

        String[] images = homestay.getImages().split("\\$");
        entry.setImages(Arrays.asList(images));
        entry.setTitle(homestay.getTitle());
        entry.setLocation(homestay.getLocation());
        entry.setLatitude(homestay.getLatitude());
        entry.setLongitude(homestay.getLongitude());
        entry.setPhone(homestay.getPhone());
        List<SurroundingsVO> surroundingList = JSON.parseArray(homestay.getSurroundings(), SurroundingsVO.class);
        entry.setSurroundings(surroundingList);

        return entry;
    }
    @RequestMapping("/homestay/list")
    public BaseListVO homestayList(@RequestParam(name = "title", required = false)String title, @RequestParam(name = "wp" , required = false) String wp) {
        int page;
        if (ObjectUtils.isEmpty(wp)) {
            page = 1;

        } else {
            byte[] base64 = Base64.getDecoder().decode(wp);
            String baseWp = new String(base64, StandardCharsets.UTF_8);
            WPVO wpvo = JSON.parseObject(baseWp, WPVO.class);
            if (wpvo.getPage() == null) {
                page = 1;
            } else {
                page = wpvo.getPage();
                title = wpvo.getTitle();
            }
        }
        List<Homestay> homestayList = homestayService.getPagingQuery(title, page, pageSize);
        List<HomestayListVO> list = new ArrayList<>();
        for (Homestay homestay : homestayList) {
            HomestayListVO entry = new HomestayListVO();

            City city = cityService.getCityInfoById(homestay.getCityId());
            if (city == null) {
                continue;
            }
            entry.setCityName(city.getName());

            entry.setImage(homestay.getImages().split("\\$")[0]);
            entry.setHomestayId(homestay.getId());
            entry.setTitle(homestay.getTitle());
            entry.setLatitude(homestay.getLatitude());
            entry.setLongitude(homestay.getLongitude());
            list.add(entry);
        }
        BaseListVO baseListVO = new BaseListVO();
        WPVO wpvo = new WPVO();
        wpvo.setPage(page + 1);
        wpvo.setTitle(title);
        String jsonString = JSON.toJSONString(wpvo);
        byte[] encodeWp = Base64.getUrlEncoder().encode(jsonString.getBytes(StandardCharsets.UTF_8));
        baseListVO.setList(list);
        baseListVO.setWp(new String(encodeWp,StandardCharsets.UTF_8).trim());
        return baseListVO;
    }
}
