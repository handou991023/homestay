package homestay.app.domian.homestay;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class HomestayImageVO {
    private String src;
    private double ar;
}
