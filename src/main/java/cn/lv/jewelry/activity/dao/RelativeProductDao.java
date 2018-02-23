package cn.lv.jewelry.activity.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.activity.daoBean.*;
import org.hibernate.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by lixing on 16/3/19.
 */
@Component
public class RelativeProductDao extends BasicDao<RelativeProduct> {
    @Bean
    public RelativeProductDao relativeProductDao() {
        return new RelativeProductDao();
    }

    public List<RelativeProduct> getProduct(long aid, int num) {
        Session session = getSession();
        String hql = "FROM cn.lv.jewelry.activity.daoBean.RelativeProduct r where r.aid=:aid";
        List<RelativeProduct> relativeProducts = session.createQuery(hql).setLong("aid", aid).setFirstResult(0).setMaxResults(num).list();
        return relativeProducts;
    }
}
