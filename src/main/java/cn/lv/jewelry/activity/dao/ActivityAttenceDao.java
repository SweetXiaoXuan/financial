package cn.lv.jewelry.activity.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.com.ql.wiseBeijing.serviceUtil.StringUtil;
import cn.lv.jewelry.activity.daoBean.Activity;
import cn.lv.jewelry.activity.daoBean.ActivityAttence;
import cn.lv.jewelry.activity.daoBean.ActivityAttenceStatus;
import cn.lv.jewelry.activity.daoBean.ActivityPrivilege;
import cn.xxtui.support.page.Page;
import cn.xxtui.support.page.PageWaterfallAdapter;
import org.hibernate.Criteria;
import org.hibernate.Query;
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
public class ActivityAttenceDao extends BasicDao<ActivityAttence>  {
    @Bean
    public ActivityAttenceDao activityAttenceDao() {
        return new ActivityAttenceDao();
    }

    private final static Logger logger = LoggerFactory.getLogger(ActivityAttenceDao.class);
    /**
     * 获取报名人数
     * @param aid 活动id
     * @param page 页码
     * @return java.lang.String
     */
    public Integer getActivityAttenceNumber(Long aid, Integer page) {
        Session session = getSession();
        Object list = null;
        try {
            StringBuffer hql = new StringBuffer("select " +
                    "count(id) " +
                    "from " +
                    "cn.lv.jewelry.activity.daoBean.ActivityAttence " +
                    "where " +
                    "aid=:aid " +
                    "and status not in(2,4,5)");
            list = session.createQuery(hql.toString())
                    .setLong("aid", aid)
                    .uniqueResult();
            if (page != null) {
                hql.append(" and status = :status");
                list = session.createQuery(hql.toString())
                        .setLong("aid", aid)
                        .setInteger("status", ActivityAttenceStatus.PARTICIPATE.getStatus())
                        .uniqueResult();
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        if (list != null)
            return Integer.parseInt(list.toString());
        return null;
    }

    /**
     * 获取用户报名情况
     * @param aid 活动id
     * @param pid 用户权限id
     * @param activitySubject 活动主题关键词
     * @return java.lang.String
     */
    public Object getAttenceStatusByAidAndUid(Long aid, Long pid, String activitySubject) {
        Session session = getSession();
        Object list = null;
        try {
            String sql = "select status " +
                    "from ActivityAttence " +
                    "where privilegeId = :pid and aid = :aid";
            if (activitySubject != null) {
                String sqlWhere = "select a.id, p.user_id as uid, a.aid, a.attence_time, a.extend, a.status\n" +
                        " from activity_attence a \n" +
                        "   RIGHT JOIN activity ON a.aid = activity.id\n" +
                        "where a.privilege_id = :pid \n" +
                        "      and a.aid = :aid \n" +
                        "      and activity.subject like '%" + activitySubject + "%'";
                list = session.createSQLQuery(sqlWhere)
                        .setLong("pid", pid)
                        .setLong("aid", aid)
                        .uniqueResult();
            } else {
                list = session.createQuery(sql)
                        .setLong("pid", pid)
                        .setLong("aid", aid)
                        .uniqueResult();
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return list;
    }

    /**
     * 获取用户报名情况
     * @param pid 用户权限id
     * @param activitySubject 活动主题关键词
     * @return java.lang.String
     */
    public List<ActivityAttence> getActivityAttence(String activitySubject, Long pid) {
        Session session = getSession();
        List<ActivityAttence> list = new ArrayList<>();
        try {
            String sql =
                    "from ActivityAttence where privilegeId = :pid and status not in (2,4,5)";
            if (activitySubject != null) {
                String sqlWhere = "select a.id, a.privilege_id as pid, a.aid, a.attence_time, a.extend, a.status\n" +
                        "from activity_attence a \n" +
                        "  RIGHT JOIN activity ON a.aid = activity.id\n" +
                        "where a.privilege_id = :pid \n" +
                        "      and a.status not in (2,4,5)\n" +
                        "      and activity.subject like '%" + activitySubject + "%'";
                List<Map<String, Object>> mapList = session.createSQLQuery(sqlWhere)
                        .setLong("pid", pid)
                        .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                        .list();
                for (Map<String, Object> map : mapList) {
                    ActivityAttence attence = new ActivityAttence();
                    Activity activity = new Activity();
                    activity.setId(Long.parseLong(map.get("aid").toString()));
                    attence.setAid(activity);
                    attence.setAttenceTime(Long.parseLong(map.get("attence_time").toString()));
                    attence.setExtend(StringUtil.isEmpty(map.get("extend") + "") ? map.get("extend").toString() : "");
                    attence.setId(Long.parseLong(map.get("id").toString()));
                    attence.setStatus(Long.parseLong(map.get("status").toString()));
                    ActivityPrivilege privilege = new ActivityPrivilege();
                    privilege.setId(Long.parseLong(map.get("pid").toString()));
                    attence.setPrivilegeId(privilege);
                }
            } else {
                list = session.createQuery(sql)
                        .setLong("pid", pid)
                        .list();
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return list;
    }

    /**
     * 获取审核列表总页数
     * @param aid 活动id
     * @return java.lang.Long
     * @author liumengwei
     * @Date 2017/10/21
     */
    public Integer getTotalPages(Long pid, Long aid) {
        Session session = getSession();
        Object query = null;
        try {
            StringBuffer total = new StringBuffer(
                    "SELECT count(u.id)\n" +
                            "FROM activity_attence att\n" +
                            "  RIGHT JOIN activity_privilege p ON att.privilege_id = p.id\n" +
                            "  RIGHT JOIN user u ON u.id = p.user_id\n" +
                            "  RIGHT JOIN activity a ON a.id = att.aid\n" +
                            "WHERE a.privilege_id = :pid\n" +
                            "      AND a.status NOT IN (4, 5)\n" +
                            "      AND att.status NOT IN (2, 4, 5)\n" +
                            "      AND a.check_status = 0\n" +
                            "      AND a.draft = 0\n" +
                            "      AND p.status = 0\n" +
                            "      AND u.status = 1\n" +
                            "      AND a.is_remove = 0\n" +
                            "      AND a.id = :aid");
                    query = session.createSQLQuery(total.toString())
                            .setLong("pid", pid)
                            .setLong("aid", aid)
                            .uniqueResult();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        Long totalPages = new PageWaterfallAdapter().totalPages(Long.parseLong(query.toString()), 10);
        return Integer.parseInt(totalPages.toString());
    }

    /**
     * 获取我参与的活动下其他用户总页数
     * @param pid 用户权限id
     * @param aid 活动id
     * @return java.lang.Integer
     * @author liumengwei
     * @Date 2017/11/15
     */
    public Integer getActivityAttendUserListTotalPages(Long pid, Long aid) {
        Session session = getSession();
        Object query = null;
        try {
            String total = "select count(*) " +
                    "from activity_attence att  " +
                    "RIGHT JOIN activity_privilege p on att.privilege_id = p.id " +
                    "RIGHT JOIN activity a on a.id = att.aid " +
                    "where att.status = 1 " +
                    "and att.aid = :aid " +
                    "and p.id != :pid " +
                    "and a.status not in (4,5) " +
                    "and a.draft = 0 " +
                    "and a.check_status = 1 " +
                    "and is_remove = 0";
                    query = session.createSQLQuery(total)
                            .setLong("pid", pid)
                            .setLong("aid", aid)
                            .uniqueResult();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        Long totalPages = new PageWaterfallAdapter().totalPages(Long.parseLong(query.toString()), 10);
        return Integer.parseInt(totalPages.toString());
    }

    /**
     * 获取我参与的活动下其他用户列表
     * @param pid 用户权限id
     * @param aid 活动id
     * @return java.lang.Integer
     * @author liumengwei
     * @Date 2017/11/15
     */
    public Page<Map<String, Object>> getAttendUserList(Long pid, Long aid, Integer page) {
        Session session = getSession();
        Page<Map<String, Object>> list = null;
        Query query = null;
        try {
            String total = "select att.* " +
                    " from activity_attence att  " +
                    "RIGHT JOIN activity_privilege p on att.privilege_id = p.id " +
                    "RIGHT JOIN activity a on a.id = att.aid " +
                    "where att.status = 1 " +
                    "and att.aid = :aid " +
                    "and a.draft = 0 " +
                    "and a.is_remove = 0 " +
                    "and a.check_status = 1 " +
                    "and p.id != :pid";
                    query = session.createSQLQuery(total)
                            .setLong("pid", pid)
                            .setLong("aid", aid)
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                            ;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        list = PageWaterfallAdapter.pagination(query, list, page, 20);
        return list;
    }


    /**
     * 获取发布的活动报名人信息
     * @param aid 活动id
     * @param registraStatus 通过状态 0已报名和预通过 1已通过
     * @param pid 权限id
     * @param page 页码
     * @return cn.xxtui.support.page.Page
     * @author liumengwei
     * @Date 2017/10/12
     */
    public Page<Map<String, Object>> getActivityRegistrationInformation(
            Long aid, Long pid, Integer registraStatus, Integer page) {
        Session session = getSession();
        Page<Map<String, Object>> list = null;
        try {
            Query query = null;
            StringBuffer sql = new StringBuffer(
                    "select a.register_end_time as registerEndTime, IFNULL(u.headpic,ub.headpic) as headpic, IFNULL(u.id,ub.id) as id,\n" +
                            "  IFNULL(u.username,ub.username) as username, att.status, att.self_introduce, p.id as pid\n" +
                            "from activity_attence att\n" +
                            "  RIGHT JOIN activity_privilege p on att.privilege_id = p.id\n" +
                            "  left join user u on u.id = p.user_id and p.user_type = 0\n" +
                            "  left JOIN user_bind ub on ub.id = p.user_id and p.user_type = 2 " +
                            "  RIGHT join activity a on a.id = att.aid\n" +
                            "where (u.status = 1 or ub.status = 0)\n" +
                            "      and a.privilege_id = :pid\n" +
                            "      and a.status not in (4,5)\n" +
                            "      and a.check_status = 1\n" +
                            "      and a.draft = 0\n" +
                            "      and a.is_remove = 0\n" +
                            "      and a.id = :aid ");
            StringBuffer sqlWhereStatus = new StringBuffer("and att.status = :status order by att.id");
            StringBuffer sqlInStatus = new StringBuffer("and att.status in (:status1, :status2) order by att.id");
            if ("0".equals(registraStatus.toString())) {
                query = session.createSQLQuery(sql.append(sqlInStatus).toString());
                query.setInteger("status1", ActivityAttenceStatus.SIGNUP.getStatus());
                query.setInteger("status2", ActivityAttenceStatus.PRE_PASS.getStatus());
            }
            if ("1".equals(registraStatus.toString())) {
                query = session.createSQLQuery(sql.append(sqlWhereStatus).toString());
                query.setInteger("status", ActivityAttenceStatus.PARTICIPATE.getStatus());
            }
            query.setLong("pid", pid);
            query.setLong("aid", aid);
            query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
            list = PageWaterfallAdapter.pagination(query, list, page, 20);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return list;
    }

    /**
     * 删除用户报名信息
     * @param aid 活动id
     * @param uid 用户id
     * @return java.lang.Boolean
     */
    public Boolean deleteAttentInfo(Long aid, Integer uid) {
        Session session = getSession();
        Integer query = null;
        try {
            String hql =
                    "delete from cn.lv.jewelry.activity.daoBean.ActivityAttence where aid = :aid and uid = :uid";
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
     * 查询用户是否报名
     * @param aid 活动id
     * @param pid 用户权限id
     * @return java.lang.Boolean
     */
    public ActivityAttence getActivityAttence(Long aid, Long pid, Integer status) {
        Session session = getSession();
        Object query = null;
        try {
            StringBuffer sql = new StringBuffer(
                    "from ActivityAttence where privilegeId = :pid and aid = :aid\n");
            if (status == null) {
                sql.append(" and status not in (2,4,5)");
                query = session.createQuery(sql.toString())
                        .setLong("pid", pid)
                        .setLong("aid", aid)
                        .uniqueResult();
            } else {
                sql.append(" and status = 1");
                query = session.createQuery(sql.toString())
                        .setLong("pid", pid)
                        .setLong("aid", aid)
                        .uniqueResult();
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return (ActivityAttence) query;
    }

    /**
     * 查询用户关注的活动
     * @param page
     * @param pid
     * @return
     */
    public Page<Map<String, Object>> getActivitys(Integer page, Long pid) {
        Session session = getSession();
        Page<Map<String, Object>> pageMap = null;
        try {
            String hql =
                    "select p.user_id as uid, a.aid as aid, ac.subject as subject, ac.status as status\n" +
                            "from activity_attence a\n" +
                            "  left join activity ac on a.aid = ac.id\n" +
                            "  RIGHT JOIN activity_privilege p on a.privilege_id = p.id\n" +
                            "where p.id = :pid\n" +
                            "      and a.status = 1\n" +
                            "      and ac.is_remove = 0\n" +
                            "      and ac.draft = 0\n" +
                            "      and ac.check_status = 1\n" +
                            "      and ac.status not in (4, 5)";
            Query query = session.createSQLQuery(hql)
                    .setLong("pid", pid)
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
            if (query.list() != null && query.list().size() > 0) {
                pageMap = PageWaterfallAdapter.pagination(query, pageMap, page, 1000);
            } else {
                return null;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return pageMap;
    }

    /**
     * 查询用户参与的活动
     * @param uid 用户id
     * @return java.util.List
     * @author liumengwei
     * @data 2017-11-15
     */
    public List<Map<String, Object>> getActivitys( Long uid) {
        Session session = getSession();
        List<Map<String, Object>> pageMap = null;
        try {
            String hql =
                    "select p.user_id as uid, a.aid as aid, ac.subject as subject\n" +
                            "from activity_attence a\n" +
                            "left join activity ac on a.aid = ac.id\n" +
                            "  RIGHT JOIN activity_privilege p on p.id = a.privilege_id\n" +
                            "where p.user_id = :uid\n" +
                            "and a.status = 1\n" +
                            "and ac.is_remove = 0\n" +
                            "and ac.draft = 0\n" +
                            "and ac.check_status = 1\n" +
                            "and ac.status not in (4, 5)";
            pageMap = session.createSQLQuery(hql)
                    .setLong("uid", uid)
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                    .list();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return pageMap;
    }

    /**
     * 获取总人数
     * @param uid 用户id
     * @param aid 活动id
     * @return java.lang.Integer
     * @author liumengwei
     * @data 2017-11-15
     */
    public List<Map<String, Object>> getTotalPeopleForAttence(Long uid, Long aid) {
        Session session = getSession();
        List<Map<String, Object>> getTotalPeopleForAttence = null;
        try {
            String hql =
                    "select id, username, IDNumber \n" +
                            "from user \n" +
                            "where status = 1 \n" +
                            "and id in (\n" +
                            "select p.user_id \n" +
                            "from activity_attence a\n" +
                            "  RIGHT JOIN activity_privilege p on p.id = a.privilege_id\n" +
                            "where a.aid = :aid \n" +
                            "and p.user_id != :uid \n" +
                            "and a.status = 0\n" +
                            ")";
            getTotalPeopleForAttence = session.createSQLQuery(hql)
                    .setLong("uid", uid)
                    .setLong("aid", aid)
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                    .list();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return getTotalPeopleForAttence;
    }

    /**
     * 获取男生总人数
     * @param uid 用户id
     * @param aid 活动id
     * @return java.lang.Integer
     * @author liumengwei
     * @data 2017-11-15
     */
    public Integer getNumberOfBoysForAttence(Long uid, Long aid) {
        Session session = getSession();
        List numberOfBoys = null;
        try {
            String hql = "select count(*)\n" +
                    "from user\n" +
                    "where status = 1\n" +
                    "      and gender = 0\n" +
                    "      and id in (\n" +
                    "  select p.user_id\n" +
                    "  from activity_attence a\n" +
                    "    RIGHT JOIN activity_privilege p on p.id = a.privilege_id\n" +
                    "  where a.status = 0\n" +
                    "        and a.aid = :aid\n" +
                    "        and p.user_id != :uid\n" +
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
     * 获取已通过人数
     * @param aid 活动id
     * @param pid 用户权限id
     * @return java.lang.Integer
     */
    public Integer getAttenceNumByAidPid(Long aid, Long pid) {
        Session session = getSession();
        Object query = null;
        try {
            String hql = "select count(*) " +
                    "from activity a " +
                    "LEFT JOIN activity_attence attence  on attence.aid = a.id " +
                    "RIGHT JOIN activity_privilege p on a.privilege_id = p.id " +
                    "where p.id = :pid " +
                    "and a.id = :aid " +
                    "AND p.status = 0 " +
                    "AND a.status in (0,1,2,3) " +
                    "and a.draft = 0 " +
                    "and a.check_status = 0 " +
                    "and a.is_remove  = 0 " +
                    "and attence.status = 1";
            query = session.createSQLQuery(hql)
                    .setLong("pid", pid)
                    .setLong("aid", aid)
                    .uniqueResult();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return Integer.parseInt(query.toString());
    }

    /**
     * 获取预通过人数
     * @param aid 活动id
     * @param pid 用户权限id
     * @return java.lang.Integer
     */
    public Integer getPrepassedNumByAidPid(Long aid, Long pid) {
        Session session = getSession();
        Object query = null;
        try {
            String hql = "select count(*) " +
                    "from activity a " +
                    "LEFT JOIN activity_attence attence  on attence.aid = a.id " +
                    "RIGHT JOIN activity_privilege p on a.privilege_id = p.id " +
                    "where p.id = :pid " +
                    "and a.id = :aid " +
                    "AND p.status = 0 " +
                    "AND a.status in (0,1,2,3) " +
                    "and a.draft = 0 " +
                    "and a.check_status = 0 " +
                    "and a.is_remove  = 0 " +
                    "and attence.status = 3";
            query = session.createSQLQuery(hql)
                    .setLong("pid", pid)
                    .setLong("aid", aid)
                    .uniqueResult();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return Integer.parseInt(query.toString());
    }

    /**
     * 用户报名状态修改
     * @param strPid 用户权限id数组
     * @param aid 活动id
     * @param status 审核状态
     * @return java.lang.Boolean
     * @author liumengwei
     * @Date 2017/10/13
     */
    public Boolean updateUserAttentStatus(String[] strPid, Long aid, Integer status) {
        Session session = getSession();
        Integer updateResult = null;
        try {
            StringBuffer sql = new StringBuffer(
                    "update activity_attence a\n" +
                            "  RIGHT JOIN activity_privilege p on a.privilege_id = p.id\n" +
                            "set a.status = :status\n" +
                            "where a.aid = :aid\n" +
                            "and a.status not in (2,4,5)\n" +
                            "and p.id in (");
//            String[] result = signUpUid.split(",");
            for (int i = 0; i <strPid.length; i++) {
                sql.append(strPid[i]);
                if (i < strPid.length -1)
                    sql.append(", ");
            }
            updateResult = session.createSQLQuery(sql.append(")").toString())
                    .setInteger("status", status)
                    .setLong("aid", aid)
                    .executeUpdate()
            ;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return updateResult != 0;
    }

    /**
     * 查询用户集合
     * @param aid 活动id
     * @param type 查看的用户类型
     * @return java.util.List
     * @author liumengwei
     */
    public Page<Map<String, Object>> getUsers(Integer aid, Integer page, Integer type) {
        Session session = getSession();
        Query query;
        Page<Map<String, Object>> pageMap = null;
        try {
            String hql =
                    "select ifnull(u.username, ub.username) as username, ifnull(u.headpic, ub.headpic) as headPic, " +
                            "ifnull(u.id, ub.id) as uid, p.id as pid\n" +
                            "from activity_privilege p\n" +
                            "  LEFT JOIN activity_attence a on a.privilege_id = p.id\n" +
                            "  LEFT JOIN user u on u.id = p.user_id and p.user_type = 0\n" +
                            "  LEFT JOIN user_bind ub on ub.id = p.user_id and p.user_type = 2\n" +
                            "where a.aid = :aid\n" +
                            "      and a.status = 1";
            if (type == 0) {
                query = session.createSQLQuery(hql)
                        .setInteger("aid", aid)
                        .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
            } else {
                // 查询用户报名异常和没有报名的用户信息
                String sql =
                        "select ifnull(u.username, ub.username) as username, ifnull(u.headpic, ub.headpic) as headPic, " +
                                "ifnull(u.id, ub.id) as uid , p.id as pid " +
                        "from activity_focus f " +
                            "join activity_privilege p on f.privilege_id = p.id " +
                                "  LEFT JOIN user u on u.id = p.user_id and p.user_type = 0\n" +
                                "  LEFT JOIN user_bind ub on ub.id = p.user_id and p.user_type = 2\n" +
                        "where f.aid = :aid " +
                            "and f.privilege_id in (" +
                                "select privilege_id " +
                                "from activity_attence " +
                                "where aid = :aid " +
                                    "and status in (0,4,5)" +
                            ") " +
                            "and (u.status = 1 or ub.status = 0) " +
                        "UNION " +
                        "select ifnull(u.username, ub.username) as username, ifnull(u.headpic, ub.headpic) as headPic," +
                                " ifnull(u.id, ub.id) as uid, p.id as pid " +
                        "from activity_focus f " +
                            "join activity_privilege p on f.privilege_id = p.id " +
                                "  LEFT JOIN user u on u.id = p.user_id and p.user_type = 0\n" +
                                "  LEFT JOIN user_bind ub on ub.id = p.user_id and p.user_type = 2\n" +
                        "where f.aid = :aid " +
                            "and f.privilege_id not in (" +
                                "select privilege_id " +
                                "from activity_attence " +
                                "where aid = :aid " +
                                "and status != 2" +
                            ")" +
                            "and (u.status = 1 or ub.status = 0) ";
                query = session.createSQLQuery(sql)
                        .setInteger("aid", aid)
                        .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
            }
            pageMap = PageWaterfallAdapter.pagination(query, pageMap, page, 1000);
        } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
        }
        return pageMap;
    }

    /**
     * 获取圈子下用户列表
     * @param aid 活动id
     * @param page 页码
     * @return cn.xxtui.support.page.Page
     * @author liumengwei
     * @date 2017-11-23
     */
    public List<Map<String, Object>> getUsers(Long aid, Integer page) {
        Session session = getSession();
        List<Map<String, Object>> query = new ArrayList<>();
        try {
            String sql =
                    "select ifnull(u.id, ub.id) as uid, ifnull(u.username, ub.username) as username, " +
                            "ifnull(u.headpic, ub.headpic) as headpic, att.privilege_id as pid\n" +
                            "from activity_attence att\n" +
                            "  RIGHT JOIN activity_privilege p on att.privilege_id = p.id\n" +
                            "LEFT JOIN user u on p.user_id = u.id and p.user_type = 0\n" +
                            "LEFT JOIN user_bind ub on p.user_id = ub.id and p.user_type = 2\n" +
                            "where (u.status = 1 or ub.status = 0)\n" +
                            "      and att.aid = :aid\n" +
                            "and p.status = 0\n" +
                            "AND att.status = 1 ";
                query = session.createSQLQuery(sql)
                        .setLong("aid", aid)
                        .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                        .list();
        } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
        }
        return query;
    }

    /**
     * 查询该用户是否报名该活动
     * @param pid 用户权限id
     * @param aid 活动id
     * @return java.lang.String
     */
    public Integer getActivityAttenceStatusById(String pid, Long aid) {
        Session session = getSession();
        try {
            String hql =
                        "from ActivityAttence where privilegeId = :pid and aid = :aid";
            ActivityAttence attence = (ActivityAttence) session.createQuery(hql)
                    .setLong("aid", aid)
                    .setLong("pid", Long.parseLong(pid))
                    .uniqueResult();
            if (attence != null) {
                Integer status = Integer.parseInt(String.valueOf(attence.getStatus()));
                if (ActivityAttenceStatus.SIGNUP.getStatus() == status)
                    return ActivityAttenceStatus.SIGNUP.getStatus();
                else if (ActivityAttenceStatus.PARTICIPATE.getStatus() == status)
                    return ActivityAttenceStatus.PARTICIPATE.getStatus();
                else
                    return ActivityAttenceStatus.ABNORMAL.getStatus();
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }
}
