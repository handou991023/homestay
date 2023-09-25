package homestay.app.domian;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SurroundingsVO {
    private String scenicspot;
    private String distance;
}
