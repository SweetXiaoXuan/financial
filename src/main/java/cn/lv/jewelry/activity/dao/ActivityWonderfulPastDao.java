package cn.lv.jewelry.activity.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.activity.daoBean.ActivityWonderfulPast;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by 24593 on 2017/11/26.
 */
@Component
public class ActivityWonderfulPastDao extends BasicDao<ActivityWonderfulPast> {
        @Bean
        public ActivityWonderfulPastDao activityWonderfulPastDao() {
            return new ActivityWonderfulPastDao();
        }
    private final static Logger logger = LoggerFactory.getLogger(ActivityWonderfulPastDao.class);

    /**
     * 查询该活动下是否有该图文精彩
     * @param aid 活动id
     * @param cid 图文id
     * @return Boolean
     * @author liumengwei
     * @date 201081/10
     */
    public ActivityWonderfulPast getInfoByAidCid(Long aid, String cid) {
        Session session = getSession();
        Object object = null;
        try {
            String sql = "from ActivityWonderfulPast where aid = :aid and cid = :cid";
            object = session.createQuery(sql)
                    .setLong("aid", aid)
                    .setLong("cid", Long.parseLong(cid))
                    .uniqueResult();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return (ActivityWonderfulPast) object;
    }
}
