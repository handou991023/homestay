package homestay.app.controller;

import com.alibaba.fastjson.JSON;
import homestay.app.domian.*;
import homestay.module.entity.City;
import homestay.module.entity.Homestay;
import homestay.module.service.CityService;
import homestay.module.service.HomestayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
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
    @RequestMapping("/homestay/info")
    public HomestayVO homestayInfo(@RequestParam(name = "homestayId")BigInteger id){
        Homestay homestay = homestayService.getHomestayInfoById(id);
        if (homestay == null){
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
        long udTime = (long)updateTime*1000;
        Date updateDate = new Date(udTime);
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
    @RequestMapping("/homestay/all")
    public List<HomestayListVO> homestayAll() {
        List<Homestay> homestayList = homestayService.getAllHomestayAllInfo();
        List<HomestayListVO> list = new ArrayList<>();
        for (Homestay homestay:homestayList) {
            HomestayListVO entry = new HomestayListVO();

            City city = cityService.getCityInfoById(homestay.getCityId());
            entry.setCityName(city.getName());
            entry.setImage(homestay.getImages().split("\\$")[0]);
            entry.setHomestayId(homestay.getId());

            entry.setTitle(homestay.getTitle());
            entry.setLatitude(homestay.getLatitude());
            entry.setLongitude(homestay.getLongitude());
            list.add(entry);
        }
        return list;
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
            entry.setImage(homestay.getImages().split("\\$")[0]);
            entry.setHomestayId(homestay.getId());
            entry.setTitle(homestay.getTitle());
            entry.setLatitude(homestay.getLatitude());
            entry.setLongitude(homestay.getLongitude());
            list.add(entry);
        }
        BaseListVO baseListVO = new BaseListVO();
        Boolean isEnd = pageSize - homestayList.size() > 0;
        baseListVO.setIsEnd(isEnd);

        WPVO wpvo = new WPVO();
        wpvo.setPage(page + 1);
        wpvo.setTitle(title);
        String jsonString = JSON.toJSONString(wpvo);
        byte[] encodeWp = Base64.getUrlEncoder().encode(jsonString.getBytes(StandardCharsets.UTF_8));
        baseListVO.setList(list);
        baseListVO.setWp(new String(encodeWp,StandardCharsets.UTF_8).trim());
        return baseListVO;
    }

    @PostMapping("/upload")
    public String upload(@RequestParam(value = "file") MultipartFile file){
        //file效验
        if(file.isEmpty()){
            return "图片上传失败";
        }
        //file重命名
        String originalFilename = file.getOriginalFilename();//原来的图片名
        String ext = "." + originalFilename.split("\\.")[1];
        String uuid = UUID.randomUUID().toString().replace("-","");
        String fileName = uuid + ext;
        //上传图片
        ApplicationHome applicationHome = new ApplicationHome(this.getClass());
        String pre = applicationHome.getDir().getParentFile().getParentFile().getAbsolutePath() +
                "\\src\\main\\resources\\static\\images\\";
        String path = pre + fileName;
        try {
            file.transferTo(new File(path));
            return path;
        } catch (IOException e) {
            e.printStackTrace();
            return  "图片上传失败";
        }
    }
}
