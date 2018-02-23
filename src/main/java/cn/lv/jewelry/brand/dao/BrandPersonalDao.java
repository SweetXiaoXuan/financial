package cn.lv.jewelry.brand.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.brand.daoBean.BrandPersonal;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by 24593 on 2018/2/10.
 */
@Component
public class BrandPersonalDao extends BasicDao<BrandPersonal> {
    @Bean
    public BrandPersonalDao brandPersonalDao(){
        return new BrandPersonalDao();
    }
}
