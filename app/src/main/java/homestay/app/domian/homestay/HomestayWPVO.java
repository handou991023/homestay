package homestay.app.domian.homestay;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class HomestayWPVO {
    private String title;
    private Integer page;
}
