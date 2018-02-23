package cn.lv.jewelry.activity.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.activity.daoBean.ActivityFocusExplore;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ActivityFocusExploreDao extends BasicDao<ActivityFocusExplore> {
    @Bean
    public ActivityFocusExploreDao activityFocusDao() {
        return new ActivityFocusExploreDao();
    }
    private final static Logger logger = LoggerFactory.getLogger(ActivityFocusExploreDao.class);

    /**
     * 获取用户是否关注发布者
     * @param ppid 活动发布者权限id
     * @param pid 用户权限id
     * @return java.util.List
     * @author liumengwei
     * @Date 2017/10/18
     */
    public ActivityFocusExplore getActivityFocusExplore(Long ppid, Long pid) {
        Session session = getSession();
        Object query = null;
        try {
            String sql =
                    "from ActivityFocusExplore where privilegeId = :pid and privilegePuid = :ppid";
            query = session.createQuery(sql)
                    .setLong("ppid", ppid)
                    .setLong("pid", pid)
                    .uniqueResult();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return (ActivityFocusExplore) query;
    }

    /**
     * 删除用户关注信息
     * @param puid 活动发布者权限id
     * @param uid 用户id
     * @return java.lang.Boolean
     */
    public Boolean deleteFocusInfo(Long puid, Long uid) {
        Session session = getSession();
        Integer query = null;
        try {
            String hql =
                    "delete from activity_focus_explore afe\n" +
                            "where privilege_puid = (select id from activity_privilege where user_id = :puid)\n" +
                            "and privilege_id = (select id from activity_privilege where user_id = :uid)";
            query = session.createQuery(hql)
                    .setLong("uid", uid)
                    .setLong("puid", puid)
                    .executeUpdate();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return query == 1;
    }

    /**
     * 查询该用户是否关注该发布者
     * @param uid 用户id
     * @param puid 发布者id
     * @return java.lang.String
     */
    public Boolean getUserAttentionExpore(String uid, Long puid) {
        Session session = getSession();
        try {
            String hql =
                    "select e.id \n" +
                            "from activity_focus_explore e\n" +
                            "  RIGHT JOIN  activity_privilege p on p.user_id= e.privilege_puid \n" +
                            "where e.privilege_id = (select id from activity_privilege where p.user_id = :uid) \n" +
                            "    and e.privilege_puid = (SELECT id FROM activity_privilege WHERE p.user_id = :puid)";
            List list = session.createSQLQuery(hql)
                    .setLong("uid", Long.parseLong(uid))
                    .setLong("puid", puid)
                    .list();

            if (list != null && list.size() >= 1) {
                return true;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return false;
    }
}