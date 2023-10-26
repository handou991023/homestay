package homestay.module.homestay.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class HomestayContentDto {
    private String type;
    private String content;
}
