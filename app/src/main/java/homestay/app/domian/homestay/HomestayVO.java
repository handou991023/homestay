package homestay.app.domian.homestay;



import homestay.app.domian.upload.ImageVO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
public class HomestayVO {
    private String createTime;
    private String updateTime;
    private Integer cityId;
    private String cityName;
    private List<ImageVO> images;
    private String title;
    private String location;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String phone;


    private List<HomestaySurroundingsVO> surroundings;
   /* private String Str = JSON.toJSONString(this.surroundings,true);*/


}
