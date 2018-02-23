package cn.lv.jewelry.brand.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.brand.daoBean.BrandCompany;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by 24593 on 2018/2/10.
 */
@Component
public class BrandCompanyDao extends BasicDao<BrandCompany> {
    @Bean
    public BrandCompanyDao brandCompanyDao(){
        return new BrandCompanyDao();
    }
}
