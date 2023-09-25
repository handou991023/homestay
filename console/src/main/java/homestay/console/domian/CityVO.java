package homestay.console.domian;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;
@Data
@Accessors(chain = true)
public class CityVO {
    private BigInteger id;
    private String createTime;
    private String updateTime;
    private String name;
    private String image;
}
