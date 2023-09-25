package homestay.module.service.impl;

import homestay.module.entity.City;
import homestay.module.mapper.CityMapper;
import homestay.module.service.CityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 城市表 服务实现类
 * </p>
 *
 * @author handou
 * @since 2023-09-09
 */
@Service
public class CityServiceImpl extends ServiceImpl<CityMapper, City> implements CityService {

}
