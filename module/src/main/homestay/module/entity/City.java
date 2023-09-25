package homestay.module.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 城市表
 * </p>
 *
 * @author handou
 * @since 2023-09-09
 */
@Data
  @EqualsAndHashCode(callSuper = true)
    @TableName("city")
public class City extends BaseEntity {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      /**
     * 图片
     */
      private String image;

      /**
     * 城市名
     */
      private String name;

      @TableField(fill = FieldFill.INSERT)
      private Integer createTime;

      @TableField(fill = FieldFill.INSERT_UPDATE)
      private Integer updateTime;

    private Integer isDeleted;


}
