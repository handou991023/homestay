package homestay.console.domian.homestay;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WPVO {
    private String title;
    private Integer page;
}
