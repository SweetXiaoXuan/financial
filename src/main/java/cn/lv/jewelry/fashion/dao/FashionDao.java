package cn.lv.jewelry.fashion.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.fashion.daoBean.Fashion;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class FashionDao extends BasicDao<Fashion>{

	@Bean
    public FashionDao fashionDao() {
        return new FashionDao();
    }


}
