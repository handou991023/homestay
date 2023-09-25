package homestay.console.domian;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class CityBaseListVO {
    private List<CityListVO> list;
    private int total;
    private int pageSize;

}
