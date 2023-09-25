package homestay.module.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

/**
 * 城市实体表
 */
@Data
@Accessors(chain = true)
public class City {
    private BigInteger id;
    /**
     * 城市图片
     */
    private String image;
    /**
     * 城市名
     */
    private String name;
    private Integer createTime;
    private Integer updateTime;
    private Integer isDeleted;
}
