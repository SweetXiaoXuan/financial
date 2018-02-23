package cn.lv.jewelry.activity.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.activity.daoBean.ActivityPrivilege;
import cn.lv.jewelry.activity.daoBean.ActivityPrivilegeStatus;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/19 0019.
 */
@Component
public class ActivityPrivilegeDao extends BasicDao<ActivityPrivilege>  {
    @Bean
    public ActivityPrivilege activityPrivilege() {
        return new ActivityPrivilege();
    }
    private final static Logger logger = LoggerFactory.getLogger(ActivityPrivilegeDao.class);

    /**
     * 修改权限状态
     * @param pid 用户权限id
     * @return java.lang.Integer
     * @author liumengwei
     */
    public Integer updateActivityPrivilege(Long pid, Integer userType) {
        Session session = getSession();
        int updateResult = -1;
        try {
            String hql =
                    "update cn.lv.jewelry.activity.daoBean.ActivityPrivilege " +
                            "set publishActivityPrivilege = :publishActivityPrivilege " +
                            "where id = :pid and userType = :userType";
            Query query = session.createQuery(hql)
                    .setString("publishActivityPrivilege"
                            , ActivityPrivilegeStatus.CANNOTPUBLISHED.getStatus().toString())
                    .setLong("pid", pid)
                    .setInteger("userType", userType);
            updateResult = query.executeUpdate();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return updateResult;
    }

    /**
     * 根据pid获取用户权限信息
     * @param pid 权限id
     * @param activitySubject 活动主题关键词
     * @return org.hibernate.Query
     */
    public Query getPrivilegeByPid(Long pid, String activitySubject) {
        Session session = getSession();
        Query query = null;
        try {
            String hql =
                    "from cn.lv.jewelry.activity.daoBean.ActivityPrivilege " +
                            "where id = :pid";
            if (activitySubject != null) {
                String sql = "select p.id, p.user_type, p.user_id, p.publish_activity_privilege, " +
                        "p.take_acitivity_privilege, p.status " +
                        "from activity_privilege p " +
                        "right join activity a on p.id = a.privilege_id " +
                        "where p.id = :pid " +
                        "and a.check_status = 1 " +
                        "and a.is_remove = 0 " +
                        "and a.draft = 0 " +
                        "and a.status not in (4,5) " +
                        "and a.subject like '%" + activitySubject + "%'";
                query = session.createSQLQuery(sql)
                        .setLong("pid", pid);
            } else {
                query = session.createQuery(hql)
                        .setLong("pid", pid);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return query;
    }

    /**
     * 获取用户权限信息
     * @param uid 用户id
     * @param type 用户类型
     * @return cn.lv.jewelry.activity.daoBean.ActivityPrivilege
     */
    public ActivityPrivilege getId(long uid, Integer type) {
        Session session = getSession();
        String hql =
                "from cn.lv.jewelry.activity.daoBean.ActivityPrivilege " +
                "where uid = :user_id and userType = :user_type and status = 0";
        Object list = session.createQuery(hql)
                .setLong("user_id", uid)
                .setInteger("user_type", type)
                .uniqueResult();
        return (ActivityPrivilege) list;
    }

    /**
     * 获取用户权限信息
     * @param uid 用户id
     * @return cn.lv.jewelry.activity.daoBean.ActivityPrivilege
     */
    public ActivityPrivilege getId(long uid) {
        Session session = getSession();
        Object list = null;
        try {
            String hql =
                    "from cn.lv.jewelry.activity.daoBean.ActivityPrivilege " +
                            "where " +
                            "   uid=:uid and userType = 0";
            list = session.createQuery(hql).setLong("uid", uid).uniqueResult();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return (ActivityPrivilege) list;
    }

    /**
     * 查询权限状态
     * @param pid 权限id
     * @param uid 用户id
     * @return java.lang.Integer
     */
    public Integer getPrivilegeStatus(Integer pid, Integer uid) {
        Session session = getSession();
        try {
            String hql =
                    "select publishActivityPrivilege as publishActivityPrivilege " +
                            "from cn.lv.jewelry.activity.daoBean.ActivityPrivilege " +
                            "where " +
                            "   id=:pid " +
                            "and uid = :uid " +
                            "and status = 0";
            List<Map<String, Object>> list = session.createQuery(hql)
                    .setInteger("pid", pid)
                    .setInteger("uid", uid)
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                    .list();
            if (list != null) {
                for (Map<String, Object> map : list) {
                    String privilegeStatus = map.get("publishActivityPrivilege").toString();
                    return Integer.parseInt(privilegeStatus);
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

}