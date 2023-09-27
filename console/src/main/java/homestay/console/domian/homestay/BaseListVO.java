package homestay.console.domian.homestay;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class BaseListVO {
    private List<HomestayListVO> list;
    private int total;
    private int pageSize;
    private String wp;
}
