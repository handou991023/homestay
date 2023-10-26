package homestay.app.domian.homestay;

import homestay.app.domian.upload.ImageVO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@Accessors(chain = true)
public class HomestayListVO {
    private BigInteger homestayId;

    private String cityName;
    private ImageVO image;
    private String title;
    private BigDecimal longitude;
    private BigDecimal latitude;

}
