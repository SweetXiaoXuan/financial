package cn.lv.jewelry.activity.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.activity.daoBean.Activity;
import cn.lv.jewelry.activity.daoBean.RelativeActivity;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by lixing on 16/3/19.
 */
@Component
public class RelativeActivityDao extends BasicDao<RelativeActivity> {
    @Bean
    public RelativeActivity relativeActivity() {
        return new RelativeActivity();
    }
    private final static Logger logger = LoggerFactory.getLogger(RelativeActivityDao.class);

    public List<RelativeActivity> getRelativeActivity(long maid) {
        Session session = getSession();
        String hql;
        List<RelativeActivity> list = null;
        try {
            hql = "from cn.lv.jewelry.activity.daoBean.RelativeActivity where maid = :maid";
            Query query = session.createQuery(hql);
            query.setLong("maid", maid);
            list = query.list();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return list;
    }

    public List<RelativeActivity> getRelativeActivity(long aid, Integer num) {
        Session session = getSession();
        String hql;
        hql = "from cn.lv.jewelry.activity.daoBean.RelativeActivity where maid=:maid";
        Query query = session.createQuery(hql);
        query.setLong("maid", aid);
        query.setFirstResult(0);
        if (num != null)
            query.setMaxResults(num);
        List<RelativeActivity> list = query.list();
        return list;
    }

    public boolean exist(RelativeActivity relativeActivity) {
        Session session = getSession();
        Query query;
        StringBuffer hql = new StringBuffer("from RelativeActivity where maid=:maid ");
        Activity rid = relativeActivity.getRaid();
        if (rid == null) {
            query = session.createQuery(hql.toString());
            query.setLong("maid",relativeActivity.getMaid().getId());
        } else {
            hql.append(" and raid=:raid");
            query = session.createQuery(hql.toString());
            query.setLong("maid",relativeActivity.getMaid().getId());
            query.setLong("raid",relativeActivity.getRaid().getId());
        }
        Object object = query.uniqueResult();
        if(object==null)
            return false;
        else
            return true;

    }
}
