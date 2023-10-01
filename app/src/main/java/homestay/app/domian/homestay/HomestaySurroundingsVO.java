package homestay.app.domian.homestay;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class HomestaySurroundingsVO {
    private String scenicspot;
    private String distance;
}
