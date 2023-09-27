package homestay.console.domian.homestay;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@Accessors(chain = true)
public class HomestayListVO {
    private BigInteger homestayId;

    private String cityName;

    private String image;
    private String title;
    private BigDecimal longitude;

    private BigDecimal latitude;


}
