package homestay.console.domian;

import lombok.Data;
import lombok.experimental.Accessors;
import java.math.BigDecimal;
import java.util.List;
@Data
@Accessors(chain = true)
public class HomestayVO {

    private String cityName;
    private String createTime;
    private String updateTime;
    private List<String> images;
    private String title;
    private String location;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String phone;

    private List<SurroundingsVO> surroundings;

}
