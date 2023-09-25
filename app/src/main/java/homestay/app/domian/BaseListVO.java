package homestay.app.domian;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
@Data
@Accessors(chain = true)
public class BaseListVO {
    private List<HomestayListVO> list;
    private Boolean isEnd;
    private String wp;

}
