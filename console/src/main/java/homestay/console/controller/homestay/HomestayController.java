package homestay.console.controller.homestay;

import com.alibaba.fastjson.JSON;
import homestay.console.annotations.VerifiedUser;
import homestay.console.domian.homestay.*;
import homestay.module.city.entity.City;
import homestay.module.homestay.entity.Homestay;
import homestay.module.city.service.CityService;
import homestay.module.homestay.service.HomestayService;
import homestay.module.user.entity.User;
import homestay.module.user.service.UserService;
import homestay.module.utils.BaseUtils;
import homestay.module.utils.Response;
import homestay.module.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    private UserService userService;

    @RequestMapping("/homestay/create")
    public Response<HomestayVO> homestayCreate(@VerifiedUser User loginUser,
                                               @RequestParam(name = "userId") BigInteger relateUserId,
                                               @RequestParam(name = "cityId")BigInteger cityId,
                                               @RequestParam(name = "images")String images,
                                               @RequestParam(name = "title")String title,
                                               @RequestParam(name = "location")String location,
                                               @RequestParam(name = "longitude")BigDecimal longitude,
                                               @RequestParam(name = "latitude") BigDecimal latitude,
                                               @RequestParam(name = "phone")String phone,
                                               @RequestParam(name = "surroundings",required = false)String surroundings) {
        if (BaseUtils.isEmpty(loginUser)) {
            return new Response<>(1002);
        }
        title = title.trim();
        if (BaseUtils.isEmpty(title)) {
            return new Response<>(3051);
        }
        if (!BaseUtils.isEmpty(relateUserId)){
            User user = userService.getById(relateUserId);
            if (BaseUtils.isEmpty(user)){
                return new Response<>(3052);
            }
        }

        try {
                BigInteger newHomestayId = homestayService.editHomestay(null, relateUserId, cityId, images, title.trim(), location.trim(), longitude, latitude, phone.trim(), surroundings);
                Homestay homestay = homestayService.getHomestayInfoById(newHomestayId);
                HomestayVO homestayVO = new HomestayVO();
                homestayVO.setHomestayId(newHomestayId);
                homestayVO.setImages(Collections.singletonList(homestay.getImages()));
                homestayVO.setTitle(homestay.getTitle());

            return new Response<>(1001,homestayVO);
        }catch(RuntimeException e){
            return new Response<>(4004);
            }
    }
    @RequestMapping("/homestay/update")
    public Response<HomestayVO> homestayUpdate(@VerifiedUser User loginUser,
                                               @RequestParam(name = "homestayId",required = false)BigInteger homestayId,
                                               @RequestParam(name = "userId") BigInteger relateUserId,
                                               @RequestParam(name = "cityId")BigInteger cityId,
                                               @RequestParam(name = "images")String images,
                                               @RequestParam(name = "title")String title,
                                               @RequestParam(name = "location")String location,
                                               @RequestParam(name = "longitude")BigDecimal longitude,
                                               @RequestParam(name = "latitude") BigDecimal latitude,
                                               @RequestParam(name = "phone")String phone,
                                               @RequestParam(name = "surroundings",required = false)String surroundings) {
        if (BaseUtils.isEmpty(loginUser)) {
            return new Response<>(1002);
        }
        //parameters check
        title = title.trim();
        if(BaseUtils.isEmpty(title)){
            return new Response<>(3051);
        }
        if (!BaseUtils.isEmpty(relateUserId)){
            User user = userService.getById(relateUserId);
            if (BaseUtils.isEmpty(user)){
                return new Response<>(3052);
            }
        }
        Homestay old = homestayService.getHomestayInfoById(homestayId);
        if(BaseUtils.isEmpty(old)){
            return new Response<>(4004);
        }

        try {
            BigInteger newHomestayId = homestayService.editHomestay(homestayId,relateUserId, cityId, images, title.trim(), location.trim(), longitude, latitude, phone.trim(), surroundings);
            Homestay homestay = homestayService.getHomestayInfoById(newHomestayId);
            HomestayVO homestayVO = new HomestayVO();
            homestayVO.setHomestayId(newHomestayId);
            homestayVO.setImages(Collections.singletonList(homestay.getImages()));
            homestayVO.setTitle(homestay.getTitle());
            return new Response<>(1001,homestayVO);

        }catch(RuntimeException e){
            return new Response<>(4004);
        }
    }
    @RequestMapping("/homestay/delete")
    public Response<Integer> homestayDelete(@VerifiedUser User loginUser,
                                           @RequestParam(name = "homestayId")BigInteger homestayId){
        if (BaseUtils.isEmpty(loginUser)) {
            return new Response<>(1002);
        }
        if (BaseUtils.isEmpty(homestayId)) {
            return new Response<>(4004);
        }
        try {
            homestayService.deleteHomestay(homestayId);
            return new Response<>(1001);
        } catch (Exception exception) {
            return new Response<>(4004);
        }
    }

    @RequestMapping("/homestay/info")
    public Response<HomestayVO> homestayInfo(@VerifiedUser User loginUser,
                                             @RequestParam(name = "homestayId")BigInteger id){
        if (BaseUtils.isEmpty(loginUser)) {
            return new Response<>(1002);
        }
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

        return new Response<>(1001,entry);
    }
    @RequestMapping("/homestay/list")
    public Response<BaseListVO> homestayList(@VerifiedUser User loginUser,
                                             @RequestParam(name = "title", required = false)String title,
                                             @RequestParam(name = "wp" , required = false) String wp) {
        if (BaseUtils.isEmpty(loginUser)) {
            return new Response<>(1002);
        }
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
        //每页数据量
        String pageSize = SpringUtils.getProperty("application.pagesize");
        List<Homestay> homestayList = homestayService.getPagingQuery(title, page, Integer.parseInt(pageSize));
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
        int total = homestayService.getPagingQueryTotal(title);
        baseListVO.setList(list);
        baseListVO.setPageSize(Integer.parseInt(pageSize));
        baseListVO.setTotal(total);
        baseListVO.setWp(new String(encodeWp,StandardCharsets.UTF_8).trim());
        return new Response<>(1001,baseListVO);
    }
}
