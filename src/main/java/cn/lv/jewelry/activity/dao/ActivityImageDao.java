package cn.lv.jewelry.activity.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.activity.daoBean.ActivityImage;
import cn.lv.jewelry.activity.daoBean.RelativeProduct;
import org.hibernate.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by lixing on 16/3/19.
 */
@Component
public class ActivityImageDao extends BasicDao<RelativeProduct> {
    @Bean
    public ActivityImageDao activityImageDao() {
        return new ActivityImageDao();
    }

    public List<ActivityImage> getActivityImage(long aid) {
        Session session = getSession();
        String hql = "FROM cn.lv.jewelry.activity.daoBean.ActivityImage where aid=:aid";
        List<ActivityImage> relativeProducts = session.createQuery(hql).setLong("aid", aid).list();
        return relativeProducts;
    }
}
