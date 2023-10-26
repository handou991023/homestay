package homestay.app.domian.upload;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ImageVO {
    private String src;
    private double ar;
}
