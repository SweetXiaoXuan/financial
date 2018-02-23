package cn.lv.jewelry.activity.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.com.ql.wiseBeijing.serviceUtil.StringUtil;
import cn.lv.jewelry.activity.daoBean.Activity;
import cn.lv.jewelry.activity.daoBean.ActivityAttenceStatus;
import cn.lv.jewelry.activity.daoBean.ActivityContent;
import cn.lv.jewelry.activity.daoBean.ActivityContentType;
import cn.lv.jewelry.activity.daoBean.ActivityRead;
import cn.lv.jewelry.activity.daoBean.ActivityReviewType;
import cn.lv.jewelry.activity.daoBean.ActivityStatusType;
import cn.xxtui.support.page.Page;
import cn.xxtui.support.page.PageWaterfallAdapter;
import cn.xxtui.support.page.ResultTransfer;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lixing on 16/3/19.
 */
@Component
public class ActivityDao extends BasicDao<Activity> {
    @Bean
    public ActivityDao activityDao() {
        return new ActivityDao();
    }

    private final static Logger logger = LoggerFactory.getLogger(ActivityDao.class);

    /**
     * 获取审核列表
     *
     * @param status 审核状态
     * @param type   活动类型
     * @param page   页码
     * @param top    小分栏状态
     * @return cn.xxtui.support.page.Page
     * @author liumengwei
     * @Date 2017/9/13
     */
    public Page<Activity> getActivityReviewList(Integer status, Integer type, Integer page, Integer top) {
        Session session = getSession();
        Query query = null;
        try {
            StringBuffer sql = new StringBuffer("FROM cn.lv.jewelry.activity.daoBean.Activity ");
            StringBuffer where = new StringBuffer(" WHERE ");
            StringBuffer whereStatus = new StringBuffer(" status = :status ");
            StringBuffer and = new StringBuffer(" AND ");
            StringBuffer whereAndSql = new StringBuffer(" checkStatus = :checkStatus ");
            StringBuffer whereAndSqlRemote = new StringBuffer(" isRemove = :isRemove ");
            StringBuffer whereAndSqlDraft = new StringBuffer(" and draft = 0 ");
            StringBuffer sqlAppendPushIndexPage = new StringBuffer(" pushIndexPage = :pushIndexPage ");
            StringBuffer sqlAppendPushColumnsPage = new StringBuffer(" pushColumnsPage = :pushColumnsPage ");
            StringBuffer sqlAppendupTimeColumns = new StringBuffer(" (upTimeColumns != null or upTimeColumns !='')");
            StringBuffer sqlAppendUpTime = new StringBuffer(" (upTime != null or upTime !='')");
            StringBuffer sqlOrderBy = new StringBuffer(" order by reviewTime desc ");
            if (top == null || top == ActivityReviewType.ALL.getType()) {
                if (status == ActivityReviewType.DELETE.getType()) {
                    sql.append(where).append(whereAndSqlRemote)
                            .append(and).append(whereStatus).append(and).append(whereAndSql).append(whereAndSqlDraft).append(sqlOrderBy);
                    query = session.createQuery(sql.toString())
                            .setInteger("isRemove", ActivityReviewType.DELETED.getType())
                            .setInteger("status", type)
                            .setInteger("checkStatus", status)
                    ;
                } else {
                    sql.append(where).append(whereStatus).append(and).append(whereAndSql).append(whereAndSqlDraft);
                    if (status != ActivityReviewType.PENDING_REVIEW.getType()) {
                        sql.append(sqlOrderBy);
                    }
                    query = session.createQuery(sql.toString())
                            .setInteger("status", type)
                            .setInteger("checkStatus", status)
                    ;
                }
            } else {
                if (top == ActivityReviewType.PUSH_INDEX.getType()) {
                    sql.append(where).append(whereStatus).append(and).append(whereAndSql)
                            .append(and).append(sqlAppendPushIndexPage).append(whereAndSqlDraft).append(sqlOrderBy);
                    query = session.createQuery(sql.toString())
                            .setInteger("status", type)
                            .setInteger("checkStatus", status)
                            .setInteger("pushIndexPage", ActivityReviewType.IS_PUSH_INDEX.getType())
                    ;
                } else if (top == ActivityReviewType.STICKY.getType()) {
                    sql.append(where).append(whereStatus).append(and).append(whereAndSql)
                            .append(and).append(sqlAppendUpTime).append(whereAndSqlDraft).append(sqlOrderBy);
                    query = session.createQuery(sql.toString())
                            .setInteger("status", type)
                            .setInteger("checkStatus", status)
                    ;
                } else if (top == ActivityReviewType.PUSH_COLUMNS.getType()) {
                    sql.append(where).append(whereStatus).append(and).append(whereAndSql)
                            .append(and).append(sqlAppendPushColumnsPage).append(whereAndSqlDraft).append(sqlOrderBy);
                    query = session.createQuery(sql.toString())
                            .setInteger("status", type)
                            .setInteger("checkStatus", status)
                            .setInteger("pushColumnsPage", ActivityReviewType.IS_PUSH_COLUMNS.getType())
                    ;
                } else if (top == ActivityReviewType.STICKY_COLUMNS.getType()) {
                    sql.append(where).append(whereStatus).append(and).append(whereAndSql)
                            .append(and).append(sqlAppendupTimeColumns).append(whereAndSqlDraft).append(sqlOrderBy);
                    query = session.createQuery(sql.toString())
                            .setInteger("status", type)
                            .setInteger("checkStatus", status)
                    ;
                } else {
                    sql.append(where).append(whereStatus).append(and).append(whereAndSql)
                            .append(and).append(sqlAppendPushIndexPage).append(and)
                            .append(sqlAppendPushColumnsPage).append(and)
                            .append(" (upTimeColumns is null or upTimeColumns ='') and (upTime is null or upTime ='') ")
                            .append(whereAndSqlDraft).append(sqlOrderBy);
                    query = session.createQuery(sql.toString())
                            .setInteger("status", type)
                            .setInteger("checkStatus", status)
                            .setInteger("pushColumnsPage", ActivityReviewType.NOT_PUSH_COLUMNS.getType())
                            .setInteger("pushIndexPage", ActivityReviewType.NOT_PUSH_INDEX.getType())
                    ;
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        PageWaterfallAdapter pageWaterfallAdapter = new PageWaterfallAdapter(query);
        Page<Activity> pageMap =
                pageWaterfallAdapter.execute(page, 10, new ResultTransfer<Activity>() {

                    @Override
                    public List<Activity> transfer(List list) {
                        return list;
                    }
                });
        return pageMap;
    }

    /**
     * 获取审核列表总页数
     *
     * @param status 审核状态
     * @param type   活动类型
     * @param top    小分栏状态
     * @return cn.xxtui.support.page.Page
     * @author liumengwei
     * @Date 2017/9/14
     */
    public Long getTotalPages(Integer status, Integer type, Integer top) {
        Session session = getSession();
        Object query = null;
        try {
            StringBuffer total = new StringBuffer("select count(id) FROM cn.lv.jewelry.activity.daoBean.Activity ");
            StringBuffer sqlAppendWhere = new StringBuffer(" WHERE ");
            StringBuffer sqlAppendStatus = new StringBuffer(" status = :status ");
            StringBuffer sqlAppendCheckStatus = new StringBuffer(" checkStatus = :checkStatus ");
            StringBuffer sqlAppendAND = new StringBuffer(" AND ");
            StringBuffer sqlAppendIsRemote = new StringBuffer(" isRemove = :isRemove ");
            StringBuffer sqlAppendPushIndexPage = new StringBuffer(" pushIndexPage = :pushIndexPage");
            StringBuffer sqlAppendUpTime = new StringBuffer(" (upTime != null or upTime !='')");
            StringBuffer sqlAppendDraft = new StringBuffer(" and draft = 0");
            if (top == null || top == ActivityReviewType.ALL.getType()) {
                if (status == ActivityStatusType.DELETE.getV()) {
                    total.append(sqlAppendWhere).append(sqlAppendStatus)
                            .append(sqlAppendAND).append(sqlAppendCheckStatus)
                            .append(sqlAppendAND).append(sqlAppendIsRemote).append(sqlAppendDraft);
                    query = session.createQuery(total.toString())
                            .setInteger("isRemove", ActivityReviewType.DELETED.getType())
                            .setInteger("status", status)
                            .setInteger("checkStatus", ActivityReviewType.DELETE.getType())
                            .uniqueResult();
                } else {
                    total.append(sqlAppendWhere).append(sqlAppendStatus)
                            .append(sqlAppendAND).append(sqlAppendCheckStatus).append(sqlAppendDraft);
                    query = session.createQuery(total.toString())
                            .setInteger("status", type)
                            .setInteger("checkStatus", status)
                            .uniqueResult();
                }
            } else {
                if (top == ActivityReviewType.PUSH_INDEX.getType()) {
                    total.append(sqlAppendWhere).append(sqlAppendStatus)
                            .append(sqlAppendAND).append(sqlAppendCheckStatus)
                            .append(sqlAppendAND).append(sqlAppendPushIndexPage).append(sqlAppendDraft);
                    query = session.createQuery(total.toString())
                            .setInteger("status", type)
                            .setInteger("pushIndexPage", ActivityReviewType.IS_PUSH_INDEX.getType())
                            .setInteger("checkStatus", status)
                            .uniqueResult();
                } else {
                    total.append(sqlAppendWhere).append(sqlAppendStatus)
                            .append(sqlAppendAND).append(sqlAppendCheckStatus)
                            .append(sqlAppendAND).append(sqlAppendUpTime).append(sqlAppendDraft);
                    query = session.createQuery(total.toString())
                            .setInteger("status", type)
                            .setInteger("checkStatus", status)
                            .uniqueResult();
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        Long totalPages = new PageWaterfallAdapter().totalPages(Long.parseLong(query.toString()), 10);
        return totalPages;
    }

    /**
     * 获取发现草稿列表
     * @param pid    权限id
     * @param page   页码
     * @param status 活动状态 3探索 2发现
     * @return cn.xxtui.support.page.Page
     * @author liumengwei
     * @Date 2017/11/11
     */
    public Page<Activity> getFindDraftList(Long pid, Integer page, Integer status) {
        Session session = getSession();
        Query query = null;
        try {
            String sql = "from cn.lv.jewelry.activity.daoBean.Activity" +
                    " where privilegeId = :pid " +
                    "and draft = 1 " +
                    "and isRemove = 0 " +
                    "and status = :status " +
                    "and checkStatus not in (2, 3) " +
                    "order by id desc";
            query = session.createQuery(sql)
                    .setLong("pid", pid)
                    .setInteger("status", status)
            ;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        Page<Activity> pageMap = null;
        pageMap = PageWaterfallAdapter.pagination(query, pageMap, page, 1000);
        return pageMap;
    }

    /**
     * 获取草稿总页数
     *
     * @param pid    发布者权限id
     * @param status 活动状态
     * @return java.lang.Long
     * @author liumengwei
     * @date 2017/12/9
     */
    public Long getDraftPages(Long pid, Integer status) {
        Session session = getSession();
        Object query = null;
        try {
            String hql =
                    " select count(*) from cn.lv.jewelry.activity.daoBean.Activity" +
                            " where privilegeId = :pid " +
                            "and draft = 1 " +
                            "and isRemove = 0 " +
                            " and status = :status " +
                            " and checkStatus not in (2, 3) ";
            query = session.createQuery(hql)
                    .setLong("pid", pid)
                    .setInteger("status", status)
                    .uniqueResult();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        Long totalPages = new PageWaterfallAdapter().totalPages(Long.parseLong(query.toString()), 1000);
        return totalPages;
    }

    /**
     * 查询用户参与的活动
     *
     * @param pid  权限id
     * @param page 页码
     * @return cn.xxtui.support.page.Page
     * @author liumengwei
     * @Date 2017/11/11
     */
    public Page<Map<String, Object>> getTheParticipationList(Long pid, Integer page, String status) {
        Session session = getSession();
        Query query = null;
        try {
            StringBuffer sql = new StringBuffer(
                    "select a.activity_number, a.check_status, a.register_end_time, a.subject, a.id, a.status, a.start_time, a.end_time " +
                            "from activity a " +
                            "RIGHT JOIN activity_privilege p on a.privilege_id = p.id " +
                            "RIGHT JOIN activity_attence att on a.id = att.aid " +
                            "where att.status = 1 " +
                            "and p.status = 0 " +
                            "and a.draft = 0 " +
                            "and a.check_status = 1 " +
                            "and a.is_remove  = 0 " +
                            "and att.privilege_id = :pid ");
            StringBuffer sqlWhereStatus = new StringBuffer("and a.status in (1, 2)");
            StringBuffer sqlWhereActivityStatus = new StringBuffer("and a.status = 0");
            if (StringUtil.isEmpty(status)) {
                sql.append(sqlWhereStatus);
            } else {
                sql.append(sqlWhereActivityStatus);
            }
            query = session.createSQLQuery(sql.toString())
                    .setLong("pid", pid)
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
            ;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        Page<Map<String, Object>> pageMap = null;
        pageMap = PageWaterfallAdapter.pagination(query, pageMap, page, 10);
        return pageMap;
    }

    /**
     * 获取审核状态下各活动数量
     *
     * @param status 审核状态
     * @return cn.xxtui.support.page.Page
     * @author liumengwei
     * @Date 2017/9/14
     */
    public Map<String, Object> getActivityNumsByStatus(Integer status, Integer top) {
        Session session = getSession();
        List<Map<String, Object>> query = null;
        try {
            StringBuffer total = new StringBuffer("select ");
            StringBuffer sum0 = new StringBuffer(" ifnull(sum(if(status=0,1,0)), 0) as COMPLETE ");
            StringBuffer sum1 = new StringBuffer(" ifnull(sum(if(status=1,1,0)), 0) as ONGOING ");
            StringBuffer sum2 = new StringBuffer(" ifnull(sum(if(status=3,1,0)), 0) as UPCOMING ");
            StringBuffer sum3 = new StringBuffer(" ifnull(sum(if(status=2,1,0)), 0) as UNPUBLISHED ");
            StringBuffer fromSql = new StringBuffer(" from activity ");
            StringBuffer where = new StringBuffer(" WHERE ");
            StringBuffer whereSql = new StringBuffer(" check_status = :checkStatus ");
            StringBuffer whereSqlRemote = new StringBuffer(" is_remove = :isRemove ");
            StringBuffer whereSqlDraft = new StringBuffer(" and draft = 0 and status not in(4,5)");
            if (top == null || top == ActivityReviewType.ALL.getType()) {
                if (status == ActivityStatusType.DELETE.getV()) {
                    total.append(sum0).append(sum1).append(sum2).append(sum3).append(fromSql)
                            .append(where).append(whereSqlRemote).append(" and ").append(whereSql).append(whereSqlDraft);
                    System.out.println(total);
                    query = session.createSQLQuery(total.toString())
                            .setInteger("isRemove", ActivityReviewType.DELETED.getType())
                            .setInteger("check_status", status)
                            .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list()
                    ;
                } else if (status == 0) {
                    total.append(sum2 + ",").append(sum1 + ",").append(sum0 + ",").append(sum3).append(fromSql)
                            .append(where).append(whereSql).append(whereSqlDraft);
                    query = session.createSQLQuery(total.toString())
                            .setInteger("checkStatus", status)
                            .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list()
                    ;
                } else {
                    total.append(sum2 + ",").append(sum3 + ",").append(sum1 + ",").append(sum0)
                            .append(fromSql).append(where).append(whereSql).append(whereSqlDraft);
                    System.out.println(total);
                    query = session.createSQLQuery(
                            total.toString())
                            .setInteger("checkStatus", status)
                            .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list()
                    ;
                }
            } else {
                if (top == ActivityReviewType.PUSH_INDEX.getType()) {
                    total.append(sum2 + ",").append(sum3 + ",").append(sum1 + ",").append(sum0)
                            .append(fromSql).append(where).append(whereSql)
                            .append(" and push_index_page = :pushIndexPage").append(whereSqlDraft);
                    query = session.createSQLQuery(
                            total.toString())
                            .setInteger("pushIndexPage", ActivityReviewType.IS_PUSH_INDEX.getType())
                            .setInteger("checkStatus", status)
                            .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list()
                    ;
                } else if (top == ActivityReviewType.STICKY.getType()) {
                    total.append(sum2 + ",").append(sum3 + ",").append(sum1 + ",").append(sum0)
                            .append(fromSql).append(where).append(whereSql)
                            .append(" and (up_time != null or up_time !='')").append(whereSqlDraft);
                    query = session.createSQLQuery(
                            total.toString())
                            .setInteger("checkStatus", status)
                            .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list()
                    ;
                } else if (top == ActivityReviewType.PUSH_COLUMNS.getType()) {
                    query = session.createSQLQuery(
                            total.append(sum2 + ",").append(sum3 + ",").append(sum1 + ",").append(sum0)
                                    .append(fromSql).append(where).append(whereSql)
                                    .append(" and push_columns_page = :pushColumnsPage")
                                    .append(whereSqlDraft).toString())
                            .setInteger("pushColumnsPage", ActivityReviewType.IS_PUSH_COLUMNS.getType())
                            .setInteger("checkStatus", status)
                            .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list()
                    ;
                } else if (top == ActivityReviewType.STICKY_COLUMNS.getType()) {
                    query = session.createSQLQuery(
                            total.append(sum2 + ",").append(sum3 + ",").append(sum1 + ",").append(sum0)
                                    .append(fromSql).append(where).append(whereSql)
                                    .append(" and (up_time_columns != null or up_time_columns !='')")
                                    .append(whereSqlDraft).toString())
                            .setInteger("checkStatus", status)
                            .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list()
                    ;
                } else if (top == ActivityReviewType.KEEP_IT.getType()) {
                    total.append(sum2 + ",").append(sum3 + ",").append(sum1 + ",").append(sum0)
                            .append(fromSql).append(where).append(whereSql)
                            .append(" and (up_time_columns is null or up_time_columns ='')")
                            .append(" and (up_time is null or up_time ='')")
                            .append(" and push_index_page = :pushIndexPage")
                            .append(" and push_columns_page = :pushColumnsPage")
                            .append(whereSqlDraft);
                    query = session.createSQLQuery(total.toString())
                            .setInteger("checkStatus", status)
                            .setInteger("pushColumnsPage", ActivityReviewType.NOT_PUSH_COLUMNS.getType())
                            .setInteger("pushIndexPage", ActivityReviewType.NOT_PUSH_INDEX.getType())
                            .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list()
                    ;
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return query.get(0);
    }

    /**
     * 修改审核状态
     *
     * @param aid    活动id
     * @param status 审核状态
     * @return java.lang.Boolean
     * @author liumengwei
     * @Date 2017/9/13
     */
    public Boolean modifyTheAuditStatus(Long aid, Integer status) {
        Session session = getSession();
        Integer updateResult = null;
        try {
            String sql =
                    "update cn.lv.jewelry.activity.daoBean.Activity " +
                            "set checkStatus = :checkStatus, " +
                            "upTimeColumns = :upTimeColumns, " +
                            "pushColumnsPage = 0, " +
                            "pushIndexPage = 0, " +
                            "upTime = :upTime, " +
                            "reviewTime = :reviewTime " +
                            "where id = :id " +
                            "and status not in (4,5) " +
                            "and draft = 0 " +
                            "and isRemove = 0";
            updateResult = session.createQuery(sql)
                    .setLong("id", aid)
                    .setString("upTimeColumns", null)
                    .setString("upTime", null)
                    .setInteger("checkStatus", status)
                    .setLong("reviewTime", System.currentTimeMillis() / 1000)
                    .executeUpdate()
            ;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        if (updateResult != null) {
            return updateResult == 1;
        }
        return false;
    }

    /**
     * 根据关键词模糊查询
     *
     * @param name 活动名称关键词
     * @return java.lang.Boolean
     * @author liumengwei
     * @Date 2017/9/13
     */
    public Page<Activity> searchActivityList(String name, Integer page) {
        Session session = getSession();
        Page<Activity> activityPage = null;
        try {
            String sql =
                    "from cn.lv.jewelry.activity.daoBean.Activity " +
                            "where subject like :name " +
                            "and status not in (4,5) " +
                            "and draft = 0 " +
                            "and checkStatus = 1 " +
                            "and isRemove = 0 " +
                            "order by insertTime asc";
            Query query = session.createQuery(sql).setString("name", "%" + name + "%");
            activityPage = PageWaterfallAdapter.pagination(query, activityPage, page, 10);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return activityPage;
    }

    /**
     * 根据权限id查询该用户发布的活动
     *
     * @param pid             权限id
     * @param page            页码
     * @param activitySubject 活动主题关键词
     * @return cn.xxtui.support.page.Page
     * @author liumengwei
     * @Date 2017/9/24
     */
    public Page<Map<String, Object>> getActivityByEid(Long pid, Integer page, String activitySubject) {
        Session session = getSession();
        Page<Map<String, Object>> mapPage = null;
        try {
            Query query;
            StringBuffer sql = new StringBuffer(
                    "select a.status as status, a.id as aid, a.subject as subject, " +
                            "a.index_pic as indexPic, a.privilege_id as eid " +
                            "from activity a " +
                            "RIGHT JOIN activity_privilege p on p.id = a.privilege_id " +
                            "where a.privilege_id = :pid " +
                            "and a.check_status = 1 " +
                            "and a.status not in (4,5) " +
                            "and a.draft = 0 " +
                            "and a.is_remove = 0");
            if (activitySubject != null) {
                sql.append(" and a.subject like '%" + activitySubject + "%'");
            }
            query = session.createSQLQuery(sql.toString()).setLong("pid", pid)
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
            mapPage = PageWaterfallAdapter.pagination(query, mapPage, page, 1000);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return mapPage;
    }

    /**
     * 查询用户报名 关注 发布所有活动
     *
     * @param pid 权限id
     * @param page 页码
     * @param activitySubject 活动主题关键词
     * @return cn.xxtui.support.page.Page
     * @author liumengwei
     * @Date 2017/9/24
     */
    public Page<Map<String, Object>> getUserActivitie(Long pid, Integer page, String activitySubject) {
        Session session = getSession();
        Page<Map<String, Object>> mapPage = null;
        try {
            Query query;
            StringBuffer sql = new StringBuffer("SELECT *\n" +
                    "FROM (\n" +
                    "       SELECT\n" +
                    "         id          AS aid,\n" +
                    "         4           AS status,\n" +
                    "         insert_time AS time,\n" +
                    "         status      AS activityStatus,\n" +
                    "         subject,\n" +
                    "         index_pic as indexPic\n" +
                    "       FROM activity\n" +
                    "       WHERE privilege_id = :pid\n" +
                    "         AND status NOT IN (4, 5)\n" +
                    "         AND check_status = 1\n" +
                    "         AND draft = 0\n" +
                    "         AND is_remove = 0" +
                    "       UNION\n" +
                    "       SELECT\n" +
                    "         attence.aid,\n" +
                    "         CASE attence.status\n" +
                    "         WHEN 0\n" +
                    "           THEN 1\n" +
                    "         WHEN 1\n" +
                    "           THEN IF(unix_timestamp(now()) > a.start_time, 2, 3)\n" +
                    "         ELSE 5 END           AS status,\n" +
                    "         attence.attence_time AS TIME,\n" +
                    "         a.status             AS activityStatus,\n" +
                    "         a.subject,\n" +
                    "         a.index_pic as indexPic" +
                    "       FROM activity_attence attence\n" +
                    "         RIGHT JOIN activity a ON attence.aid = a.id\n" +
                    "       WHERE attence.privilege_id = :pid AND attence.status NOT IN (2, 4, 5)\n" +
                    "         AND a.status NOT IN (4, 5)\n" +
                    "         AND a.check_status = 1\n" +
                    "         AND a.draft = 0\n" +
                    "         AND a.is_remove = 0" +
                    "       UNION\n" +
                    "       SELECT\n" +
                    "         aid,\n" +
                    "         0           AS status,\n" +
                    "         create_time AS time,\n" +
                    "         a.status    AS activityStatus,\n" +
                    "         a.subject,\n" +
                    "         a.index_pic as indexPic\n" +
                    "       FROM activity_focus f\n" +
                    "         RIGHT JOIN activity a ON f.aid = a.id\n" +
                    "       WHERE f.privilege_id = :pid " +
                    "       AND a.status NOT IN (4, 5)\n" +
                    "       AND a.check_status = 1\n" +
                    "       AND a.draft = 0\n" +
                    "       AND a.is_remove = 0" +
                    "         AND aid NOT IN (\n" +
                    "         SELECT aid\n" +
                    "         FROM activity_attence\n" +
                    "         WHERE privilege_id = :pid " +
                    "         AND status NOT IN (2, 4, 5)\n" +
                    "       )\n" +
                    "     ) hodays\n");
            StringBuffer sqlOrder = new StringBuffer(" ORDER BY time DESC");
            if (activitySubject != null) {
                sql.append(" where subject like '%" + activitySubject + "%'");
            }
            sql.append(sqlOrder);
            query = session.createSQLQuery(sql.toString())
                    .setLong("pid", pid)
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
            mapPage = PageWaterfallAdapter.pagination(query, mapPage, page, 1000);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return mapPage;
    }

    /**
     * 根据权限id查询该用户发布的活动
     * @param pid 权限id
     * @param aid 活动id
     * @return cn.xxtui.support.page.Page
     * @author liumengwei
     * @Date 2018/1/13
     */
    public Activity getActivityByPidAid(Long aid, Long pid) {
        Session session = getSession();
        Object object = null;
        try {
            String hql = "from Activity where id = :aid and privilegeId = :pid";
            object = session.createQuery(hql)
                    .setLong("aid", aid)
                    .setLong("pid", pid)
                    .uniqueResult();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return (Activity) object;
    }

    /**
     * 根据id查询活动状态
     *
     * @param aid 活动id
     * @return java.lang.Integer
     */
    public Integer getActivityById(String aid) {
        Session session = getSession();
        Query query = null;
        try {
            String hql = "select status from cn.lv.jewelry.activity.daoBean.Activity " +
                    "where id = :id and status not in (4,5) and draft = 0 and check_status = 1 and is_remove = 0";
            query = session.createQuery(hql);
            query.setLong("id", Long.parseLong(aid));
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return Integer.parseInt(query.list().get(0).toString());
    }

    /**
     * 获取阅读者信息
     *
     * @param start
     * @param aid
     * @return
     */
    public Page<ActivityRead> getActivityReader(int start, long aid) {
        Session session = getSession();
        String hql = "from cn.lv.jewelry.activity.daoBean.ActivityRead where aid=:aid order by readTime desc ";
        Query query = session.createQuery(hql);
        query.setLong("aid", aid);
        PageWaterfallAdapter pageWaterfallAdapter = new PageWaterfallAdapter(query);
        return pageWaterfallAdapter.execute(start, 10, new ResultTransfer<ActivityRead>() {

            @Override
            public List<ActivityRead> transfer(List list) {
                return list;
            }
        });
    }

    /**
     * 获取用户头像
     *
     * @param aid 活动id
     * @return java.util.List
     */
    public List<Map<String, Object>> getUserPic(long aid) {
        Session session = getSession();
        List<Map<String, Object>> query = null;
        try {
            String sql =
                    "select ifnull(u.headpic, '') as headPic, u.id as uid, u.username as username, p.id as pid\n" +
                            "from user u\n" +
                            "  RIGHT JOIN activity_privilege p on u.id = p.user_id\n" +
                            "  LEFT JOIN activity_attence a on a.privilege_id = p.id\n" +
                            "where a.aid = :aid\n" +
                            "      and a.status = :status";
            query =
                    session.createSQLQuery(sql)
                            .setLong("aid", aid)
                            .setInteger("status", ActivityAttenceStatus.PARTICIPATE.getStatus())
                            .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                            .list();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return query;
    }

    /**
     * 获取阅读总人数
     *
     * @param aid 活动id
     * @return java.lang.String
     */
    public Integer getActivityReaderNumber(Long aid) {
        Session session = getSession();
        try {
            String hql =
                    "select " +
                            "   count(id) " +
                            "from " +
                            "   cn.lv.jewelry.activity.daoBean.ActivityRead " +
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
     * 获取所有活动列表信息
     *
     * @param start  页码
     * @param status 活动类型
     * @return cn.xxtui.support.page.Page
     * @author liumengwei
     * @date 2017-11-17
     */
    public Page<Activity> getAppActivityList(int start, int status) {
        Session session = getSession();
        Page<Activity> pageMap = null;
        Query query = null;
        try {
            String hql = "from cn.lv.jewelry.activity.daoBean.Activity " +
                    "where status = :status " +
                    "and isRemove = 0 " +
                    "and draft = 0 " +
                    "and checkStatus = 1" +
                    "and (upTimeColumns is not null or pushColumnsPage = 1) " +
                    "order by upTime desc, insertTime desc ";
            if (status == -1)
                hql = "from cn.lv.jewelry.activity.daoBean.Activity " +
                        "where upTime is not null or pushIndexPage = 1" +
                        "and isRemove = 0 " +
                        "and status not in(4,5) " +
                        "and draft = 0 " +
                        "and checkStatus = 1" +
                        " order by upTime desc, insertTime desc ";
            query = session.createQuery(hql);
            if (status != -1)
                query.setInteger("status", status);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return PageWaterfallAdapter.pagination(query, pageMap, start, 10);
    }

    /**
     * 获取所有活动列表信息
     *
     * @param start
     * @param status
     */
    public Page<Activity> getActivityList(int start, int status) {
        Session session = getSession();
        Page<Activity> pageMap = null;
        Query query = null;
        try {
            String hql = "from cn.lv.jewelry.activity.daoBean.Activity where status=:status order by upTime desc, insertTime desc ";
            if (status == -1)
                hql = "from cn.lv.jewelry.activity.daoBean.Activity  order by upTime desc, insertTime desc ";
            query = session.createQuery(hql);
            if (status != -1)
                query.setInteger("status", status);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return PageWaterfallAdapter.pagination(query, pageMap, start, 10);
    }

    public List<Map<String, Object>> getActivitysAttenceByUid(Long uid, Integer page) {
        Session session = getSession();
        List<Map<String, Object>> query = null;
        try {
            String sql = "select a.subject as subject, a.id as aid, a.status as status from  ACTIVITY_ATTENCE attence " +
                    "left join USER u on attence.uid = u.id " +
                    "left join ACTIVITY a on attence.aid = a.id " +
                    "where attence.uid = :uid and attence.status = :status order by a.id desc ";
            query = session.createSQLQuery(sql).
                    setLong("uid", uid).
                    setInteger("status", ActivityAttenceStatus.PARTICIPATE.getStatus())
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return query;
    }

    public List<Map<String, Object>> getActivitysFocusByUid(Long uid, Integer page) {
        Session session = getSession();
        List<Map<String, Object>> query = null;
        try {
            String sql = "select a.subject as subject, a.id as aid, a.status as status from  ACTIVITY_FOCUS focus " +
                    "left join USER u on focus.uid = u.id " +
                    "left join ACTIVITY a on focus.aid = a.id where focus.uid = :uid order by a.id desc ";
            query = session.createSQLQuery(sql).
                    setLong("uid", uid).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return query;
    }

    public List<Map<String, Object>> getUsersByAid(Long aid) {
        Session session = getSession();
        List<Map<String, Object>> query = null;
        try {
            String sql = "select u.id as uid, u.headPic as headPic from  ACTIVITY_ATTENCE attence " +
                    "left join USER u on attence.uid = u.id " +
                    "left join ACTIVITY a on attence.aid = a.id where attence.aid = :aid order by u.id desc limit 9 ";
            query = session.createSQLQuery(sql).
                    setLong("aid", aid).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return query;
    }

    /**
     * 根据活动名称获取活动
     *
     * @param start
     * @param name
     */
    public Page<Activity> getActivityByName(int start, String name) {
        Session session = getSession();
        String hql = "from cn.lv.jewelry.activity.daoBean.Activity a where a.subject like :name order by id desc ";
        Query query = session.createQuery(hql);
        query.setString("name", "%" + name + "%");
        PageWaterfallAdapter pageWaterfallAdapter = new PageWaterfallAdapter(query);
        return pageWaterfallAdapter.execute(start, 10, new ResultTransfer<Activity>() {

            @Override
            public List<Activity> transfer(List list) {
                return list;
            }
        });
    }

    /**
     * 首页即将开始的活动索引
     *
     * @param n
     * @return
     */
    public List<Activity> getACtivityTop(int n, int status) {
        Session session = getSession();
        String hql = "from Activity a where a.status=:status order by a.id DESC";
        List<Activity> activityList = session.createQuery(hql).setInteger("status", status).setFirstResult(0).setMaxResults(n).list();

        return activityList;
    }

    /**
     * 首页已经完成的活动索引
     *
     * @param n
     * @param status
     * @return
     */
    public List<Activity> getCompleteACtivityTop(int n, int status) {
        Session session = getSession();
        String hql = "from Activity a where a.status=:status order by a.id  DESC";
        List<Activity> activityList = session.createQuery(hql).setInteger("status", status).setFirstResult(0).setMaxResults(n).list();

        return activityList;
    }

    /**
     * 获取活动举办方信息
     *
     * @param start 页码
     * @param aid   活动id
     * @return cn.xxtui.support.page.Page
     */
    public Page<ActivityContent> getActivityRelativeIMG(int start, long aid) {
        Session session = getSession();
        Page<ActivityContent> page = null;
        Query query = null;
        try {
            String hql = "from ActivityContent where aid=:aid and type=:type order by id desc ";
            query = session.createQuery(hql);
            query.setLong("aid", aid);
            query.setInteger("type", ActivityContentType.RIMG.getV());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        page = PageWaterfallAdapter.pagination(query, page, start, 10);
        return page;
    }

    /**
     * 获取活动内容
     * order by id
     *
     * @param aid
     * @return
     */
    public List<ActivityContent> getActivityContent(long aid) {
        Session session = getSession();
        String hql = "from cn.lv.jewelry.activity.daoBean.ActivityContent " +
                "where aid = :aid " +
                "and type != :type " +
                "order by id asc ";
        Query query = session.createQuery(hql);
        query.setLong("aid", aid);
        query.setInteger("type", ActivityContentType.RIMG.getV());
        return query.list();
    }

    /**
     * 查询用户发布的活动
     *
     * @param pid  权限id
     * @param page 页码
     * @return java.util.List
     * @author liumengwei
     * @Date 2017/9/9
     */
    public Page<Map<String, Object>> getPublishingActivity(Long pid, Integer page) {
        Session session = getSession();
        Page<Map<String, Object>> pageMap = null;
        try {
            StringBuffer sql = new StringBuffer(
                    "SELECT a.activity_number, a.id, a.subject, a.status, a.end_time, " +
                            "a.start_time, a.check_status, a.register_end_time " +
                            "FROM activity_privilege p " +
                            "LEFT JOIN activity a ON p.id = a.privilege_id \n" +
                            "left JOIN user u ON u.id = p.user_id and p.user_type = 0\n" +
                            "left JOIN user_bind ub on ub.id = p.user_id and p.user_type = 2  " +
                            "WHERE p.id = :pid " +
                            "AND p.status = 0 " +
                            "AND (u.status = 1 or ub.status = 0) " +
                            "and a.status in (0, 1, 2, 3) " +
                            "and a.draft = 0 " +
                            "and a.check_status not in (3) " +
                            "and a.is_remove  = 0 " +
                            "order by a.id desc");
            Query query = session.createSQLQuery(sql.toString())
                    .setLong("pid", pid)
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
            PageWaterfallAdapter pageWaterfallAdapter = new PageWaterfallAdapter(query);
            pageMap =
                    pageWaterfallAdapter.execute(page, 1000, new ResultTransfer<Map<String, Object>>() {

                        @Override
                        public List<Map<String, Object>> transfer(List list) {
                            return list;
                        }
                    });
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return pageMap;
    }

    /**
     * 查询用户发布的活动
     *
     * @param pid  权限id
     * @param page 页码
     * @return java.util.List
     * @author liumengwei
     * @Date 2017/9/9
     */
    public Page<Map<String, Object>> getActivitys(Integer page, Long pid) {
        Session session = getSession();
        Page<Map<String, Object>> pageMap = null;
        try {
            String hql =
                    "select p.user_id as uid, ac.id as aid, ac.subject as subject, ac.status as status " +
                            "from activity_privilege p" +
                            "  RIGHT JOIN activity ac on ac.privilege_id = p.id" +
                            "  LEFT JOIN activity_take at on at.aid = ac.id and at.privilege_id = p.id" +
                            " where ac.privilege_id = :privilege_id or at.privilege_id = :privilege_id " +
                            "and ac.is_remove = 0 " +
                            "and ac.draft = 0 " +
                            "and ac.check_status = 1 " +
                            "and ac.status not in (4, 5)";
            Query query = session.createSQLQuery(hql)
                    .setLong("privilege_id", pid)
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
     * 查询用户发布的活动
     *
     * @param pid  权限id
     * @param page 页码
     * @return java.util.List
     * @author liumengwei
     * @Date 2017/9/9
     */
    public Page<Activity> getPublishingActivityCircle(Long pid, Integer page) {
        Session session = getSession();
        Page<Activity> pageMap = null;
        try {
            String hql = "from Activity " +
                    "where privilegeId = :pid " +
                    "and status = 0 " +
                    "and draft = 0 " +
                    "and checkStatus = 1 " +
                    "and isRemove  = 0 " +
                    "order by id desc";
            Query query = session.createQuery(hql)
                    .setLong("pid", pid);
            pageMap = PageWaterfallAdapter.pagination(query, pageMap, page, 1000);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return pageMap;
    }

    /**
     * 查询用户发起圈子下用户列表
     *
     * @param aid  活动id
     * @param pid  权限id
     * @param page 页码
     * @return java.util.List
     * @author liumengwei
     * @Date 2017/11/24
     */
    public List<Map<String, Object>> getInitiatedCircleForUserList(Long aid, Long pid, Integer page) {
        Session session = getSession();
        List<Map<String, Object>> query = new ArrayList<>();
        try {
            String sql =
                    "select ifnull(u.username, ub.username) as username, ifnull(u.headpic, ub.headpic) as headpic,\n" +
                            "  ifnull(u.id, ub.id) as uid, att.privilege_id as pid\n" +
                            "from activity_attence att\n" +
                            "RIGHT JOIN activity_privilege p on att.privilege_id = p.id\n" +
                            "LEFT JOIN user u on u.id = p.user_id and p.user_type = 0\n" +
                            "LEFT JOIN user_bind ub on ub.id = p.user_id and p.user_type = 2\n" +
                            "LEFT JOIN activity a on att.aid = a.id\n" +
                            "where (u.status = 1 or ub.status = 0)\n" +
                            "    and att.aid=a.id\n" +
                            "      and p.id=att.privilege_id\n" +
                            "      and a.privilege_id=:pid\n" +
                            "      and a.status=0\n" +
                            "      and a.draft=0\n" +
                            "      and a.check_status=1\n" +
                            "      and a.is_remove=0\n" +
                            "      and att.aid=:aid\n" +
                            "      and att.status=1";
            query = session.createSQLQuery(sql)
                    .setLong("pid", pid)
                    .setLong("aid", aid)
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                    .list();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return query;
    }
}
