package cn.lv.jewelry.activity.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.com.ql.wiseBeijing.serviceUtil.StringUtil;
import cn.lv.jewelry.activity.daoBean.Activity;
import cn.lv.jewelry.activity.daoBean.ActivityFocus;
import cn.lv.jewelry.activity.daoBean.ActivityPrivilege;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/21 0021.
 */
@Component
public class ActivityFocusDao extends BasicDao<ActivityFocus> {
    @Bean
    public ActivityFocusDao activityFocusDao() {
        return new ActivityFocusDao();
    }
    private final static Logger logger = LoggerFactory.getLogger(ActivityFocusDao.class);

    /**
     * 获取关注人数
     * @param aid 活动id
     * @return java.lang.String
     */
    public Integer getActivityFocusNumber(Long aid) {
        Session session = getSession();
        try {
            String hql =
                    "select " +
                            "   count(id) " +
                            "from " +
                            "   cn.lv.jewelry.activity.daoBean.ActivityFocus " +
                            "where " +
                            "   aid=:aid ";
            Object list = session.createQuery(hql)
                    .setLong("aid", aid)
                    .uniqueResult();
            if (list != null)
                return Integer.parseInt(list.toString());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

    /**
     * 获取总人数
     * @param uid 用户id
     * @param aid 活动id
     * @return java.lang.Integer
     * @author liumengwei
     * @data 2017-11-15
     */
    public List<Map<String, Object>> getTotalPeopleForFocus(Long uid, Long aid) {
        Session session = getSession();
        List<Map<String, Object>> totalPeopleForFocus = null;
        try {
            String hql =
                    " SELECT\n" +
                            "  id,\n" +
                            "  username,\n" +
                            "  IDNumber\n" +
                            "FROM user\n" +
                            "WHERE status = 1\n" +
                            "      AND id IN (\n" +
                            "  SELECT p.user_id\n" +
                            "  FROM activity_focus f\n" +
                            "    RIGHT JOIN activity_privilege p on p.id = f.privilege_id\n" +
                            "  WHERE aid = :aid\n" +
                            "        AND p.user_id != :uid\n" +
                            "        AND p.user_id NOT IN (\n" +
                            "    SELECT p.user_id\n" +
                            "    FROM activity_attence a\n" +
                            "      RIGHT JOIN activity_privilege p on p.id = a.privilege_id\n" +
                            "    WHERE a.aid = :aid\n" +
                            "          AND p.user_id != :uid\n" +
                            "          AND a.status = 0\n" +
                            "  )\n" +
                            ")";
            totalPeopleForFocus = session.createSQLQuery(hql)
                    .setLong("uid", uid)
                    .setLong("aid", aid)
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                    .list();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return totalPeopleForFocus;
    }

    /**
     * 获取男生总人数
     * @param uid 用户id
     * @param aid 活动id
     * @return java.lang.Integer
     * @author liumengwei
     * @data 2017-11-15
     */
    public Integer getNumberOfBoysForFocus(Long uid, Long aid) {
        Session session = getSession();
        List numberOfBoys = null;
        try {
            String hql = "select count(*)\n" +
                    "from user\n" +
                    "where gender = 0\n" +
                    "      and status = 1\n" +
                    "      and id in (\n" +
                    "  SELECT p.user_id\n" +
                    "  FROM activity_focus f\n" +
                    "    RIGHT JOIN activity_privilege p on p.id = f.privilege_id\n" +
                    "  WHERE p.user_id != :uid\n" +
                    "        AND f.aid = :aid\n" +
                    "        AND p.user_id NOT IN (\n" +
                    "    SELECT p.user_id\n" +
                    "    FROM activity_attence a\n" +
                    "      RIGHT JOIN activity_privilege p on p.id = a.privilege_id\n" +
                    "    WHERE a.status = 0\n" +
                    "          AND a.aid = :aid\n" +
                    "          AND p.user_id != :uid\n" +
                    "  )\n" +
                    ")";
            numberOfBoys = session.createSQLQuery(hql)
                    .setLong("uid", uid)
                    .setLong("aid", aid)
                    .list();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return Integer.parseInt(numberOfBoys.get(0).toString());
    }

    /**
     * 查询用户是否关注
     * @param aid 活动id
     * @param pid 用户qxid
     * @return java.lang.Boolean
     */
    public ActivityFocus getActivityFocus(Long aid, Long pid) {
        Session session = getSession();
        Object query = null;
        try {
            String hql =
                    "from ActivityFocus\n" +
                            "where aid = :aid and privilegeId = :pid";
            query = session.createQuery(hql)
                    .setLong("pid", pid)
                    .setLong("aid", aid)
                    .uniqueResult();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return (ActivityFocus) query;
    }

    /**
     * 删除用户关注信息
     * @param aid 活动id
     * @param uid 用户id
     * @return java.lang.Boolean
     */
    public Boolean deleteFocusInfo(Long aid, Integer uid) {
        Session session = getSession();
        Integer query = null;
        try {
            String hql =
                    "delete from cn.lv.jewelry.activity.daoBean.ActivityFocus where aid = :aid and uid = :uid";
            query = session.createQuery(hql)
                    .setInteger("uid", uid)
                    .setLong("aid", aid)
                    .executeUpdate();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return query == 1;
    }

    /**
     * 查询用户关注的活动
     * @param pid 用户id
     * @return java.util.List
     */
    public List<ActivityFocus> getActivityFocus(Long pid, String activitySubject) {
        Session session = getSession();
        List<ActivityFocus> query = new ArrayList<>();
        try {
            String hql =
                        "FROM ActivityFocus \n" +
                        "WHERE privilegeId = :pid\n" +
                        "      AND aid NOT IN (\n" +
                        "  SELECT aid\n" +
                        "  FROM ActivityAttence \n" +
                        "  WHERE privilegeId = :pid\n" +
                        "        AND status NOT IN (2, 4, 5)\n" +
                        ")";
            if (activitySubject != null) {
                String sqlWhere = "SELECT\n" +
                        "  f.id,\n" +
                        "  p.id as pid,\n" +
                        "  f.aid,\n" +
                        "  f.a_status,\n" +
                        "  f.create_time,\n" +
                        "  f.extend\n" +
                        "FROM activity_focus f\n" +
                        "  RIGHT JOIN activity a ON f.aid = a.id\n" +
                        "WHERE f.privilege_id = :pid\n" +
                        "  AND a.subject LIKE '%" + activitySubject + "%'\n" +
                        "  AND aid NOT IN (\n" +
                        "    SELECT aid\n" +
                        "    FROM activity_attence att\n" +
                        "      RIGHT JOIN activity_privilege p on att.privilege_id = p.id\n" +
                        "    WHERE p.user_id = :uid\n" +
                        "      AND att.status NOT IN (2, 4, 5)\n" +
                        ") " +
                        "and a.check_status = 1 " +
                        "and a.is_remove = 0 " +
                        "and a.draft = 0 " +
                        "and a.status not in (4,5) ";
                List<Map<String, Object>> queryMap = session.createSQLQuery(sqlWhere)
                        .setLong("pid", pid)
                        .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                        .list();
                for (Map<String, Object> map : queryMap) {
                    ActivityFocus focus = new ActivityFocus();
                    focus.setaStatus(Integer.parseInt(map.get("a_status").toString()));
                    focus.setCreateTime(map.get("create_time").toString());
                    ActivityPrivilege privilege = new ActivityPrivilege();
                    privilege.setId(Long.parseLong(map.get("pid").toString()));
                    focus.setPrivilegeId(privilege);
                    Activity activity = new Activity();
                    activity.setId(Long.parseLong(map.get("aid").toString()));
                    focus.setAid(activity);
                    focus.setExtend(StringUtil.isEmpty(map.get("extend") + "") ? map.get("extend").toString() : "");
                    focus.setId(Long.parseLong(map.get("id").toString()));
                    query.add(focus);
                }
            } else {
                query = session.createQuery(hql)
                        .setLong("pid", pid)
                        .list();
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return query;
    }

    /**
     * 查询该用户是否关注该活动
     * @param pid 用户权限id
     * @param aid 活动id
     * @return java.lang.String
     */
    public String getActivityFocusStatusById(Long pid, Long aid) {
        Session session = getSession();
        try {
            String hql =
                    "select aStatus\n" +
                            "from ActivityFocus\n" +
                            "where privilegeId = :pid\n" +
                            "  and aid = :aid";
            List list = session.createQuery(hql)
                    .setLong("pid", pid)
                    .setLong("aid", aid)
                    .list();

            if (list != null && list.size() >= 1) {
                return list.get(0).toString();
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return "";
    }
}