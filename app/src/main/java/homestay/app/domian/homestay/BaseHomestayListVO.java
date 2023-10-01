package homestay.app.domian.homestay;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
@Data
@Accessors(chain = true)
public class BaseHomestayListVO {
    private List<HomestayListVO> list;
    private Boolean isEnd;
    private String wp;

}
