package cn.lv.jewelry.activity.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.activity.daoBean.ActivityCommentRead;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/10/15 0015.
 */
@Component
public class ActivityCommentReadDao extends BasicDao<ActivityCommentRead>{
    @Bean
    public ActivityCommentReadDao activityCommentReadDao() {
        return new ActivityCommentReadDao();
    }
    private final static Logger logger = LoggerFactory.getLogger(ActivityCommentReadDao.class);

    /**
     * 获取未读消息数
     * @param pid 用户权限id
     * @param aid 活动id
     * @return java.lang.Integer
     * @author liumengwei
     * @Date 2017/10/15
     */
    public Integer getInfoNum(Long pid, Long aid) {
        Session session = getSession();
        Object count = null;
        try {
            String sql =
                    "SELECT count(r.id)\n" +
                            "FROM activity_comment_read r\n" +
                            "  RIGHT JOIN activity_comment c ON r.cid = c.id\n" +
                            "WHERE r.privilege_id = :pid\n" +
                            "      AND c.comment_status = 0\n" +
                            "      AND c.aid = :aid";
            count = session.createSQLQuery(sql)
                    .setLong("aid", aid)
                    .setLong("pid", pid)
                    .uniqueResult()
            ;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
       return Integer.parseInt(count.toString());
    }
}
