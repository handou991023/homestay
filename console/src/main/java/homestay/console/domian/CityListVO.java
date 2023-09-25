package homestay.console.domian;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
public class CityListVO {
    private BigInteger id;
    private String name;
}
