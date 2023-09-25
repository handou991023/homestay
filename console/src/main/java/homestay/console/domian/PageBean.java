package homestay.console.domian;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PageBean {
    private int page;   //页码
    private int offset;// 一开始的位置
    private int pageSize;//每页显示的条数

    public PageBean(int page, int offset, int pageSize) {
        this.page = page;
        this.offset = offset;
        this.pageSize = pageSize;
    }
}
