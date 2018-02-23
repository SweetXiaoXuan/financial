package cn.lv.jewelry.activity.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.activity.daoBean.ActivityComment;
import cn.lv.jewelry.activity.daoBean.ActivityCommentStatus;
import cn.lv.jewelry.activity.daoBean.ActivityPrivilege;
import cn.lv.jewelry.activity.daoBean.ActivityPrivilegeStatus;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/23 0023.
 */
@Component
public class ActivityCommentDao extends BasicDao<ActivityComment> {
    @Bean
    public ActivityCommentDao activityCommentDao() {
        return new ActivityCommentDao();
    }
    private final static Logger logger = LoggerFactory.getLogger(ActivityCommentDao.class);
    /**
     * 获取评论总数
     * @param aid
     * @return
     */
    public Integer getCommentNumber(Long aid, Long pid) {
        Session session = getSession();
        String sql =
                "select count(*)\n" +
                        "from activity_comment\n" +
                        "where aid = :aid\n" +
                        "      and privilege_id = :pid\n" +
                        "      and comment_status = 0";
        Object list = session.createSQLQuery(sql)
                .setLong("aid", aid)
                .setLong("pid", pid)
                .uniqueResult();
        return Integer.parseInt(list.toString());
    }

    /**
     * 获取举办方发布图文信息
     * @param aid 活动id
     * @return java.util.List
     */
    public Map<String,Object> getOrganizerCommentById(Long aid) {
        Session session = getSession();
        Map<String,Object> query = null;
        try {
            String sql =
                    "SELECT u.id as uid,\n" +
                            "       u.username as username,\n" +
                            "       u.headpic as headPic,\n" +
                            "       t.cid as cid,\n" +
                            "       t.comment_content as commentContent,\n" +
                            "       t.comment_time as commentTime,\n" +
                            "       c.comment_type as commentType\n" +
                            "FROM activity_comment c\n" +
                            "  RIGHT JOIN activity_privilege p on c.privilege_id = p.id\n" +
                            "  RIGHT JOIN USER u ON u.id = p.user_id\n" +
                            "  LEFT JOIN activity_comment_text t ON t.cid = c.id\n" +
                            "WHERE c.aid = :aid\n" +
                            "      AND c.comment_status = 0\n" +
                            "      AND t.comment_status = 0\n" +
                            "      and c.comment = 1\n" +
                            "      and u.status = 1\n" +
                            "ORDER BY t.cid desc\n" +
                            "limit 1";
            query = (Map<String,Object>) session.createSQLQuery(sql)
                    .setLong("aid", aid)
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                    .uniqueResult();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return query;
    }

    /**
     * 获取用户发布图文信息
     * @param aid 活动id
     * @param page 页码
     * @param organizersCid 主办方最新一条评论id
     * @return java.util.List
     */
    public Page<Map<String,Object>> getUserCommentById(Long aid, Integer page, Object organizersCid) {
        Session session = getSession();
        Query query = null;
        try {
            StringBuffer sql = new StringBuffer(
                    "SELECT u.id as uid, " +
                    "u.username as username, " +
                    "u.headpic as headPic," +
                    "t.cid as cid, " +
                    "t.comment_content as commentContent, " +
                    "t.comment_time as commentTime," +
                    "c.comment_type as commentType " +
                    "FROM activity_comment c " +
                    "  RIGHT JOIN activity_privilege p on c.privilege_id = p.id" +
                    "  RIGHT JOIN USER u ON u.id = p.user_id " +
                    "  LEFT JOIN activity_comment_text t ON t.cid = c.id " +
                    "WHERE c.aid = :aid " +
                    "AND u.status = 1 " +
                    "AND c.comment_status = 0 " +
                    "AND t.comment_status = 0 " +
                    "and c.comment = 1 " +
                    "AND u.id in (" +
                        "select user_id " +
                        "from activity_privilege " +
                        "where status = 0 " +
                    ")");
            StringBuffer sqlOrderBy = new StringBuffer(" ORDER BY t.cid desc");
            if (organizersCid != null) {
                sql.append(" and c.id != :organizersCid ");
            }
            sql.append(sqlOrderBy);
            query = session.createSQLQuery(sql.toString());
            query.setLong("aid", aid);
            if (organizersCid != null) {
                query.setLong("organizersCid", Long.parseLong(organizersCid.toString()));
            }
            query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        Page<Map<String,Object>> pageMap = null;
        pageMap = PageWaterfallAdapter.pagination(query, pageMap, page, 20);
        return pageMap;
    }

    /**
     * 查看用户发布所有图文
     * @param uid 用户id
     * @param page 页码
     * @return cn.xxtui.support.page.Page
     * @author liumengwei
     * @date 2017-11-16
     */
    public Page<Map<String, Object>> getReleaseGraphicList(Long uid, Integer page) {
        Session session = getSession();
        Query query;
        Page<Map<String, Object>> pageMap = null;
        try {
            String sql =
                    "SELECT u.id as uid,\n" +
                            "       u.username as username,\n" +
                            "       u.headpic as headPic,\n" +
                            "       t.cid as cid,\n" +
                            "       t.comment_content as commentContent,\n" +
                            "       t.comment_time as commentTime,\n" +
                            "       c.comment_type as commentType\n" +
                            "FROM activity_comment c\n" +
                            "  RIGHT JOIN activity_privilege p on c.privilege_id = p.id\n" +
                            "  RIGHT JOIN USER u ON u.id = p.user_id\n" +
                            "  LEFT JOIN activity_comment_text t ON t.cid = c.id\n" +
                            "WHERE  c.comment_status = 0\n" +
                            "      AND t.comment_status = 0\n" +
                            "      and c.comment = 1\n" +
                            "      and u.status = 1\n" +
                            "  and p.user_id = :uid\n" +
                            "ORDER BY t.cid desc";
            query = session.createSQLQuery(sql)
                    .setLong("uid", uid)
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
            pageMap = PageWaterfallAdapter.pagination(query, pageMap, page, 20);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return pageMap;
    }

    /**
     * 获取举办方发布图文信息
     * @param cid 评论id
     * @param userType 用户类型
     * @return java.util.List
     */
    public Map<String,Object> getComments(Long cid, Integer userType) {
        Session session = getSession();
        Map<String,Object> query = null;
        try {
            StringBuffer sql = new StringBuffer(
                    "SELECT u.id as uid, " +
                    "u.username as username, " +
                    "u.headpic as headPic," +
                    "t.cid as cid, " +
                    "t.comment_content as commentContent, " +
                    "t.comment_time as commentTime," +
                    "c.comment_type as commentType," +
                    "c.is_delete as isDelete," +
                    "p.user_type as userType  " +
                    "FROM activity_comment c " +
                    "  RIGHT JOIN activity_privilege p on c.privilege_id = p.id" +
                    "  LEFT JOIN activity_comment_text t ON t.cid = c.id "
                    );
            StringBuffer sqlUser = new StringBuffer("  RIGHT JOIN USER u ON u.id = p.user_id WHERE u.status = 1");
            StringBuffer sqlUserBind = new StringBuffer("  right join user_bind u on u.id = p.user_id  WHERE u.status = 0 ");
            StringBuffer sqlAfter = new StringBuffer(
                    " AND c.comment_status = 0" +
                    " AND t.comment_status = 0" +
                    " and cid = :cid ");

            if (userType == ActivityPrivilegeStatus.PERSONAL.getStatus()) {
                sql.append(sqlUser).append(sqlAfter);
            } else if (userType == ActivityPrivilegeStatus.RHIRD_PARTY.getStatus()) {
                sql.append(sqlUserBind).append(sqlAfter);
            }
            query = (Map<String,Object>) session.createSQLQuery(sql.toString())
                    .setLong("cid", cid)
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                    .uniqueResult();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return query;
    }

    /**
     * 查询图文
     * @param aid 活动id
     * @param page 页码
     * @param organizersCid 主办方最新一条评论id
     * @return java.util.List
     */
    public Page<Map<String,Object>> getCommentsForType(
            Integer type, Long aid, String uid, Integer page, Object organizersCid) {
        Session session = getSession();
        Query query = null;
        try {
            StringBuffer sql = new StringBuffer(
                    "SELECT ifnull(u.id, ub.id) as uid, " +
                            "ifnull(u.username, ub.username) as username," +
                            " ifnull(u.headpic, ub.headpic) as headPic, " +
                            "t.cid as cid, " +
                            "c.aid as aid, " +
                            "t.comment_content as commentContent, " +
                            "t.comment_time as commentTime," +
                            "c.comment_type as commentType," +
                            "p.user_type as userType," +
                            "c.is_delete as isDelete " +
                            "FROM activity_comment c " +
                            "  RIGHT JOIN activity_privilege p on c.privilege_id = p.id" +
                            "  LEFT JOIN activity_comment_text t ON t.cid = c.id " +
                            "  LEFT JOIN USER u ON p.user_id = u.id and p.user_type = 0 " +
                            "  LEFT JOIN user_bind ub ON p.user_id = ub.id and p.user_type = 2 " +
                            "WHERE (u.status = 1 or ub.`status` = 0) " +
                            "AND c.comment_status = 0 " +
                            "AND t.comment_status = 0 " +
                            "and c.comment = 1 " +
                            "and c.comment_type not in (6, 7) "
                            );
            StringBuffer sqlLimit = new StringBuffer(" limit 1");
            StringBuffer sqlWHereUid = new StringBuffer(" and (u.id = :uid or ub.id = :uid)");
            StringBuffer sqlWHereUidIn = new StringBuffer(
                    " AND c.privilege_id in (select id from activity_privilege where status = 0 ) ");
            StringBuffer sqlWhereAid = new StringBuffer(" and c.aid = :aid");
            StringBuffer sqlWhereOrganizersCid = new StringBuffer(" and c.id != :organizersCid ");
            StringBuffer sqlOrderBy = new StringBuffer(" ORDER BY t.cid desc");
            StringBuffer sqlWhereNotInCid = new StringBuffer(" and c.id not in (\n" +
                    "  select id from activity_comment where privilege_id not in (\n" +
                    "    select id from activity_privilege where id in (\n" +
                    "      select privilege_id from activity_take where aid = :aid\n" +
                    "    )\n" +
                    "  )\n" +
                    ")");
            if (type == 0) {// 全部
                sql.append(sqlWHereUid).append(sqlOrderBy);
                query = session.createSQLQuery(sql.toString());
                query.setLong("uid", Long.parseLong(uid));
            } else if (type == 1) {// 主办方
                sql.append(sqlWhereAid).append(sqlWhereNotInCid).append(sqlOrderBy);
                query = session.createSQLQuery(sql.toString());
                query.setLong("aid", aid);
            } else if (type == 2) {// 用户
                sql.append(sqlWhereAid).append(sqlWHereUidIn);
                if (organizersCid != null) {
                    sql.append(sqlWhereOrganizersCid);
                }
                sql.append(sqlOrderBy);
                query = session.createSQLQuery(sql.toString());
                query.setLong("aid", aid);
                if (organizersCid != null) {
                    query.setLong("organizersCid", Long.parseLong(organizersCid.toString()));
                }
            }
            query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        Page<Map<String,Object>> pageMap = null;
        if (type == 1) {
            pageMap = PageWaterfallAdapter.pagination(query, pageMap, page, 1);
        } else {
            pageMap = PageWaterfallAdapter.pagination(query, pageMap, page, 20);
        }
        return pageMap;
    }

    /**
     * 获取往期精彩
     * @param aid 活动id
     * @param page 页码
     * @return cn.xxtui.support.page.Page
     * @author liumengwei
     * @date 2017-11-26
     */
    public Page<Map<String,Object>> getWonderfulPast(Long aid, Integer page) {
        Session session = getSession();
        Query query = null;
        try {
            String sql = "select\n" +
                    "  eu.uid,\n" +
                    "  eu.headPic,\n" +
                    "  eu.username,\n" +
                    "  t.comment_content as commentContent,\n" +
                    "  t.comment_time as commentTime,\n" +
                    "  c.comment_type as commentType,\n" +
                    "  c.id as cid," +
                    "  p.user_type as userType,\n" +
                    "  c.is_delete as isDelete\n" +
                    "from activity_comment c\n" +
                    "RIGHT JOIN activity a on c.aid = a.id\n" +
                    "RIGHT JOIN activity_privilege p on c.privilege_id = p.id\n" +
                    "LEFT JOIN activity_comment_text t on t.cid = c.id\n" +
                    "LEFT JOIN activity_wonderful_past wp on wp.cid = c.id\n" +
                    "JOIN (\n" +
                    "    select\n" +
                    "      u.id as uid,\n" +
                    "      u.username as username,\n" +
                    "      u.headpic as headPic,\n" +
                    "      p.id as pid\n" +
                    "    from user u\n" +
                    "      LEFT JOIN activity_privilege p on p.user_id = u.id\n" +
                    "      where u.status = 1\n" +
                    "    UNION\n" +
                    "    select\n" +
                    "      e.id as uid,\n" +
                    "      e.name as username,\n" +
                    "      e.logo_pic as headPic,\n" +
                    "      p.id as pid\n" +
                    "    from enterprise e\n" +
                    "      LEFT JOIN activity_privilege p on p.user_id = e.id\n" +
                    "      where e.status = 0\n" +
                    "    ) eu on eu.pid = p.id\n" +
                    "where c.comment_status = 0\n" +
                    "and t.comment_status = 0\n" +
                    "and wp.is_delete= 0\n" +
                    "and c.comment = 1\n" +
                    "and c.aid = :aid";
//                    "select e.id as uid, e.headpic as headPic, e.name as username, t.cid as cid,\n" +
////                    "select e.id as uid, e.logo_pic as headPic, e.name as username, t.cid as cid,\n" +
//                            "       t.comment_time as commentTime, t.comment_content as commentContent,\n" +
//                            "       c.comment_type as commentType\n" +
//                            "from activity_comment c\n" +
//                            "  RIGHT JOIN activity_privilege p on c.privilege_id = p.id\n" +
//                            "  LEFT JOIN activity_comment_text t on c.id = t.cid\n" +
////                            "  LEFT JOIN enterprise e on p.user_id = e.id\n" +
//                            "  LEFT JOIN user e on p.user_id = e.id\n" +
//                            "  LEFT JOIN activity_wonderful_past wp on c.id = wp.cid\n" +
//                            "where c.aid = :aid\n" +
//                            "      and c.comment_status = 0\n" +
//                            "      and t.comment_status = 0\n" +
////                            "      and p.user_type = 1\n" +
//                            "      and c.comment = 1\n" +
//                            "      and wp.is_delete = 0\n" +
//                            "order by t.cid";
            query = session.createSQLQuery(sql)
                    .setLong("aid", aid)
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        Page<Map<String,Object>> pageMap = null;
        pageMap = PageWaterfallAdapter.pagination(query, pageMap, page, 20);
        return pageMap;
    }

    /**
     * 获取评论数量
     * @param aid 活动id
     * @param status 状态
     * @return java.lang.Integer
     */
    public Integer getCommentNumber(Long aid, int status) {
        Session session = getSession();
        StringBuffer hql = new StringBuffer();
        List list;
        try {
            hql.append("select count(id) " +
                    "from cn.lv.jewelry.activity.daoBean.ActivityComment " +
                    "where aid = :aid " +
                    "and commentStatus = 0 " +
                    "and is_delete = 0 " +
                    " and commentType = :type");
            list = session.createQuery(hql.toString())
                    .setLong("aid", aid)
                    .setInteger("type", status)
                    .list();

            if (list.get(0) != null) {
                String num = list.get(0).toString();
                return Integer.parseInt(num);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return 0;
    }

    public List<Map<String, Object>> getCommentByAid(Long aid, int page) {
        Session session = getSession();
        List listMap = new ArrayList();
        String sql =
                "select c.id as cid, c.uid as uid, c.comment_time as commentTime, u.headpic as headPic, " +
                        "u.username as username, t.comment_content as commentContent " +
                        "from " +
                        "activity_comment c, user u, " +
                        "activity_comment_text t " +
                        "where u.id = c.uid " +
                        "and t.cid = c.id " +
                        "and c.aid = :aid " +
                        "and c.comment_status = :CcommentStatus " +
                        "and t.comment_status = :TcommentStatus " +
                        "order by c.comment_time desc";
        Query query = session.createSQLQuery(sql)
                .setLong("aid", aid)
                .setInteger("CcommentStatus", ActivityCommentStatus.NORMAL.getStatus())
                .setInteger("TcommentStatus", ActivityCommentStatus.NORMAL.getStatus())
                .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        Page<Map<String,Object>> pageMap = null;
        pageMap = PageWaterfallAdapter.pagination(query, pageMap, page, 20);
        List<Map<String,Object>> list = pageMap.getPageContent();
        if (list != null && list.size() > 0) {
            for (Map<String, Object> map1 : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("cid", map1.get("cid"));
                map.put("uid", map1.get("uid"));
                map.put("commentTime", map1.get("commentTime"));
                map.put("commentContent", map1.get("commentContent"));
                map.put("username", map1.get("username"));
                map.put("headPic", map1.get("headPic"));
                listMap.add(map);
            }
        }
        return listMap;
    }

    /**
     * 查询该活动未读消息id
     * @param pid 用户权限id
     * @param aid 活动id
     * @return java.util.List
     * @author liumengwei
     */
    public List<Map<String, Object>> getCommentByUid(Long pid, Long aid) {
        Session session = getSession();
        List<Map<String, Object>> listMap = new ArrayList<>();
        try {
            String sql = "SELECT *\n" +
                    "FROM activity_comment ac\n" +
                    "WHERE privilege_id = :pid\n" +
                    "  AND aid = :aid\n" +
                    "  AND comment_status = 0\n" +
                    "  AND comment_type not in (6, 7)\n" +
                    "  AND id NOT IN (\n" +
                    "    SELECT cid\n" +
                    "    FROM activity_comment_read  \n" +
                    "    WHERE privilege_id = :pid\n" +
                    ")";
            listMap = session.createSQLQuery(sql)
                    .setLong("aid", aid)
                    .setLong("pid", pid)
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                    .list();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return listMap;
    }

    /**
     * 查看一级评论
     * @param aid 活动id
     * @param page 页码
     * @param status 状态
     * @return java.util.List
     */
    public Page<Map<String,Object>> getCommentByAid(Long aid, int page, int status) {
        Session session = getSession();
        StringBuffer sql = new StringBuffer();
        Query query = null;
        try {
            sql.append("select c.id as cid, u.id as uid, c.comment_type as commentType,\n" +
                    "       c.comment_time as commentTime, u.headpic as headPic,\n" +
                    "       u.username as username, t.comment_content as commentContent,\n" +
                    "       p.user_type as userType, c.is_delete as isDelete " +
                    "from activity_comment c\n" +
                    "  RIGHT JOIN activity_privilege p on c.privilege_id = p.id\n" +
                    "  LEFT JOIN user u on u.id = p.user_id\n" +
                    "  LEFT JOIN activity_comment_text t on t.cid = c.id\n" +
                    "where c.aid = :aid\n" +
                    "      and u.status = 1\n" +
                    "      and c.comment_status = 0\n" +
                    "      and t.comment_status = 0 " +
                    "      and c.is_delete = 0 " +
                    "      and c.comment_type = :type " +
                    "order by c.id desc ");
            query = session.createSQLQuery(sql.toString())
                    .setLong("aid", aid)
                    .setInteger("type", status)
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        Page<Map<String,Object>> pageMap = null;
        pageMap = PageWaterfallAdapter.pagination(query, pageMap, page, 20);
        return pageMap;
    }

    /**
     * 查询活动主办方权限信息
     * @param cid 图文id
     * @return Integer
     * @author liumengwei
     * @since 2018/1/19
     */
    public List<ActivityPrivilege> getUserOrOrganizer(Long cid) {
        Session session = getSession();
        List<ActivityPrivilege> query = null;
        try {
            String hql = "from ActivityPrivilege " +
                    "where id in (" +
                    "   select privilegeId from ActivityTake " +
                    "   where aid = (" +
                    "       select aid from ActivityComment " +
                    "       where id = :cid" +
                    "   )" +
                    ")";
                query = session.createQuery(hql)
                        .setLong("cid", cid)
                        .list();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return query;
    }


}
