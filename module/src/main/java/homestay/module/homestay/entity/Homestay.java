package homestay.module.homestay.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 民宿实体表
 */
@Data
@Accessors(chain = true)
public class Homestay {

    private BigInteger id;
    private BigInteger userId;
    /**
     * 民宿图片
     */
    private String images;
    /**
     * 民宿名字
     */
    private String title;
    /**
     * 城市id
     */
    private BigInteger cityId;
    /**
     * 民宿地址
     */
    private String location;
    /**
     * 经度
     */
    private BigDecimal longitude;
    /**
     * 纬度
     */
    private BigDecimal latitude;
    /**
     * 民宿电话
     */
    private String phone;
    /**
     * 周边景点
     */
    private String surroundings;
    private Integer createTime;
    private Integer updateTime;
    private Integer isDeleted;


}
