package cn.lv.jewelry.activity.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.activity.daoBean.ActivityContent;
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
public class ActivityContentDao extends BasicDao<ActivityContent> {
    private final static Logger logger = LoggerFactory.getLogger(ActivityContentDao.class);

    @Bean
    public ActivityContentDao activityDao() {
        return new ActivityContentDao();
    }
    public boolean existContent(long aid,int activityStatus)
    {
        List list=selectContent(aid, activityStatus);
        if(list.isEmpty())
        {
            return false;
        }
        else
            return true;
    }

    public List selectContent(long aid, int activityStatus) {
        Session session = getSession();
        Query query = null;
        try {
            String sql="from ActivityContent where aid=:aid and activityStatus=:activityStatus";
            query = session.createQuery(sql);
            query.setLong("aid",aid);
            query.setInteger("activityStatus",activityStatus);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return query.list();
    }

    /**
     * 获取活动详细信息
     * @param aid 活动id
     * @return cn.lv.jewelry.activity.daoBean.ActivityContent
     * @author liumengwei
     * @date 2017-11-8
     */
    public ActivityContent getActivityContent(Long aid) {
        Session session = getSession();
        ActivityContent list = null;
        try {
            String hql = "from cn.lv.jewelry.activity.daoBean.ActivityContent where aid = :aid";
            list = (ActivityContent) session.createQuery(hql)
                    .setLong("aid", aid)
                    .uniqueResult();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return list;
    }
}
