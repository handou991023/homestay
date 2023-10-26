package homestay.app.controller.homestay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import homestay.app.domian.homestay.*;
import homestay.app.domian.upload.ImageVO;
import homestay.module.utils.BaseUtils;
import homestay.module.utils.Response;
import homestay.module.city.entity.City;
import homestay.module.homestay.entity.Homestay;
import homestay.module.city.service.CityService;
import homestay.module.homestay.service.HomestayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

@RestController
public class HomestayController {
    @Autowired
    private HomestayService homestayService;
    @Autowired
    private CityService cityService;

    @RequestMapping("/homestay/info")
    public Response<HomestayVO> homestayInfo(@RequestParam(name = "homestayId")BigInteger id){
        Homestay homestay = homestayService.getHomestayInfoById(id);
        if (BaseUtils.isEmpty(homestay)){
            return new Response<>(4004);
        }
        HomestayVO entry = new HomestayVO();
        Integer createTime = homestay.getCreateTime();
        long creTime = (long)createTime*1000;
        Date createDate = new Date(creTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 定义日期格式
        String formattedCreateDate = sdf.format(createDate);
        entry.setCreateTime(formattedCreateDate);

        Integer updateTime = homestay.getUpdateTime();
        long udTime = (long)updateTime*1000;
        Date updateDate = new Date(udTime);
        String formattedUpdateDate = sdf.format(updateDate);
        entry.setUpdateTime(formattedUpdateDate);

        City city = cityService.getCityInfoById(homestay.getCityId());
        entry.setCityName(city.getName());

        String[] images = homestay.getImages().split("\\$");
        List<ImageVO> imageVOList= new ArrayList<>();
        for(String image : images){

            ImageVO imageVO = new ImageVO();
            int[] wh = new int[2];
            String[] imageStr = homestay.getImages().split("\\$")[0].split("_");
            if(imageStr.length >= 2){
                String[] imageStrEnd = imageStr[imageStr.length -1].split("\\.");
                String[] imageParam = imageStrEnd[0].split("x");
                if(imageParam.length == 2 && BaseUtils.isNumeric(imageParam[0]) && BaseUtils.isNumeric(imageParam[1])){
                    wh[0] = new Integer(imageParam[0]);
                    wh[1] = new Integer(imageParam[1]);
                }
            }
            double imageAr;
            if(wh[1] > 0){
                double ar = new BigDecimal(wh[0]).doubleValue() / wh[1];
                BigDecimal bg = new BigDecimal(ar);
                imageAr = bg.setScale(2, RoundingMode.HALF_UP).doubleValue();
            }else {
                imageAr = 0;
            }

            imageVO.setSrc(homestay.getImages().split("\\$")[0]);
            imageVO.setAr(imageAr);
            imageVOList.add(imageVO);
        }
        entry.setImages(imageVOList);
        entry.setTitle(homestay.getTitle());
        entry.setLocation(homestay.getLocation());
        entry.setLatitude(homestay.getLatitude());
        entry.setLongitude(homestay.getLongitude());
        entry.setPhone(homestay.getPhone());
        List<HomestaySurroundingsVO> surroundingList = JSON.parseArray(homestay.getSurroundings(), HomestaySurroundingsVO.class);
        entry.setSurroundings(surroundingList);
        return new Response<>(1001,entry);
    }
    @RequestMapping("/homestay/list")
    public Response<BaseHomestayListVO> homestayList(@RequestParam(name = "title", required = false)String title,
                                                     @RequestParam(name = "wp" , required = false) String wp) {
        int page;
        if (ObjectUtils.isEmpty(wp)) {
            page = 1;
        } else {
            byte[] base64 = Base64.getDecoder().decode(wp);
            String baseWp = new String(base64, StandardCharsets.UTF_8);
            HomestayWPVO wpvo = JSON.parseObject(baseWp, HomestayWPVO.class);
            if (wpvo.getPage() == null) {
                page = 1;
            } else {
                page = wpvo.getPage();
                title = wpvo.getTitle();
            }
        }
        //每页数据量
        int pageSize = 5;
        List<Homestay> homestayList = homestayService.getPagingQuery(title, page, pageSize);
        Map<BigInteger,City> cityMap = new HashMap<>();
        List<BigInteger> cityIdList = new ArrayList<>();
        for (Homestay homestay : homestayList) {
            cityIdList.add(homestay.getCityId());
        }
        List<City> cityList = cityService.getCityList(cityIdList);
        for (City city : cityList){
            cityMap.put(city.getId(),city);
        }
        List<HomestayListVO> list = new ArrayList<>();
        for (Homestay homestay : homestayList) {
            HomestayListVO entry = new HomestayListVO();
            City city = cityMap.get(homestay.getCityId());
            if(city == null){
                continue;
            }
            entry.setCityName(city.getName());
            ImageVO imageVO = new ImageVO();
            int[] wh = new int[2];
            String[] imageStr = homestay.getImages().split("\\$")[0].split("_");
            if(imageStr.length >= 2){
                String[] imageStrEnd = imageStr[imageStr.length -1].split("\\.");
                String[] imageParam = imageStrEnd[0].split("x");
                if(imageParam.length == 2 && BaseUtils.isNumeric(imageParam[0]) && BaseUtils.isNumeric(imageParam[1])){
                    wh[0] = new Integer(imageParam[0]);
                    wh[1] = new Integer(imageParam[1]);
                }
            }
            double imageAr;
            if(wh[1] > 0){
                double ar = new BigDecimal(wh[0]).doubleValue() / wh[1];
                BigDecimal bg = new BigDecimal(ar);
                imageAr = bg.setScale(2, RoundingMode.HALF_UP).doubleValue();
            }else {
                imageAr = 0;
            }
            imageVO.setSrc(homestay.getImages().split("\\$")[0]);
            imageVO.setAr(imageAr);

            entry.setImage(imageVO);
            entry.setHomestayId(homestay.getId());
            entry.setTitle(homestay.getTitle());
            entry.setLatitude(homestay.getLatitude());
            entry.setLongitude(homestay.getLongitude());
            list.add(entry);
        }
        BaseHomestayListVO baseListVO = new BaseHomestayListVO();
        Boolean isEnd = pageSize - homestayList.size() > 0;
        baseListVO.setIsEnd(isEnd);

        HomestayWPVO wpvo = new HomestayWPVO();
        wpvo.setPage(page + 1);
        wpvo.setTitle(title);
        String jsonString = JSON.toJSONString(wpvo);
        byte[] encodeWp = Base64.getUrlEncoder().encode(jsonString.getBytes(StandardCharsets.UTF_8));
        baseListVO.setList(list);
        baseListVO.setWp(new String(encodeWp,StandardCharsets.UTF_8).trim());
        return new Response<>(1001,baseListVO);
    }
}
