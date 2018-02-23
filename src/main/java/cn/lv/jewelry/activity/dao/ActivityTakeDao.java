package cn.lv.jewelry.activity.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.activity.daoBean.ActivityTake;
import cn.lv.jewelry.activity.daoBean.ActivityTakeStatus;
import cn.xxtui.support.page.Page;
import cn.xxtui.support.page.PageWaterfallAdapter;
import cn.xxtui.support.page.ResultTransfer;
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
 * Created by huayang on 17/3/5.
 */
@Component
public class ActivityTakeDao extends BasicDao<ActivityTake> {
    @Bean
    public ActivityTakeDao activityDao() {
        return new ActivityTakeDao();
    }
    private final static Logger logger = LoggerFactory.getLogger(ActivityTakeDao.class);

    public Page<ActivityTake> getActivityTake(Integer start, Long aid, Long eid) {
        Session session=getSession();
        String hql="from ActivityTake where aid=:aid and eid = :eid and status = :status order by id desc ";
        Query query=session.createQuery(hql);
        query.setLong("aid",aid);
        query.setLong("eid",eid);
        query.setInteger("status", ActivityTakeStatus.NORMAL.getStatus());
        PageWaterfallAdapter pageWaterfallAdapter = new PageWaterfallAdapter(query);
        return pageWaterfallAdapter.execute(start, 10, new ResultTransfer<ActivityTake>() {
            @Override
            public List<ActivityTake> transfer(List list) {
                return list;
            }
        });
    }

    /**
     * 查询活动承办方类型
     * @param aid
     * @return
     */
    public List<Map<String, Object>> getActivityTakeInfo(Long aid, Long eid) {
        Session session = getSession();
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            String hql =
                    "SELECT " +
                            "   p.userType  " +
                            "FROM " +
                            "   cn.lv.jewelry.activity.daoBean.ActivityTake t, " +
                            "   cn.lv.jewelry.activity.daoBean.ActivityPrivilege p " +
                            "WHERE " +
                            "   t.privilegeId = p.id " +
                            "AND " +
                            "   t.aid = :aid " +
                            "AND " +
                            "   p.id = :eid " +
                            "AND " +
                            "   t.status = 0 " +
                            "AND " +
                            "   p.status = 0";
            list = session.createQuery(hql)
                    .setLong("aid", aid)
                    .setLong("eid", eid)
                    .list();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return list;
    }

    /**
     * 查询活动承办方详细信息：用户
     * @param aid
     * @return
     */
    public List<Map<String, Object>> getTakeUserInfo(Long aid, Long eid) {
        Session session = getSession();
        List<Map<String, Object>> listMap = new ArrayList<>();
        try {
            String hql =
                    "select\n" +
                            "  p.id as pid, u.headpic, u.username\n" +
                            "from\n" +
                            "  user u,\n" +
                            "  activity_privilege p,\n" +
                            "  activity_take t\n" +
                            "where\n" +
                            "  t.privilege_id = p.id\n" +
                            "  AND\n" +
                            "  u.id = p.user_id\n" +
                            "  AND\n" +
                            "  u.status = 1\n" +
                            "  AND\n" +
                            "  p.status = 0\n" +
                            "  AND\n" +
                            "  t.status = 0\n" +
                            "  and\n" +
                            "  aid = :aid\n" +
                            "  AND\n" +
                            "  p.id = :eid";
            listMap = session.createSQLQuery(hql)
                    .setLong("aid", aid)
                    .setLong("eid", eid)
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                    .list();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return listMap;
    }

    /**
     * 查询活动承办方详细信息：企业
     * @param aid
     * @return
     */
    public List<Map<String, Object>> getTakeEnterpriseInfo(Long aid, Long eid) {
        Session session = getSession();
        List<Map<String, Object>> listMap = new ArrayList<>();
        try {
            String hql =
                    "select " +
                            "   p.id as pid, e.logoPic as headpic, e.name as username " +
                            "from " +
                            "   cn.lv.jewelry.enterprise.daoBean.Enterprise e, " +
                            "   cn.lv.jewelry.activity.daoBean.ActivityTake t, " +
                            "   cn.lv.jewelry.activity.daoBean.ActivityPrivilege p " +
                            "where " +
                            "   t.privilegeId = p.id " +
                            "AND " +
                            "   e.id = p.uid " +
                            "and " +
                            "   t.aid = :aid " +
                            "and " +
                            "   e.status = 0 " +
                            "and " +
                            "   p.status = 0 " +
                            "and " +
                            "   t.status = 0 " +
                            "and " +
                            "   p.id = :eid";
            listMap = session.createQuery(hql)
                    .setLong("eid", eid)
                    .setLong("aid", aid)
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                    .list();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return listMap;
    }

    /**
     * 查询该活动所有举办方相关权限id
     * @param aid 活动id
     * @return
     */
    public List getActivityEnterprise(Long aid) {
        Session session=getSession();
        List list;
        String sql = "SELECT p.id\n" +
                "FROM activity_take t\n" +
                "  RIGHT JOIN activity_privilege p ON t.privilege_id = p.id\n" +
                "WHERE t.aid = :aid\n" +
                "      AND t.status = 0\n" +
                "      AND p.status = 0";
        Query query = session.createSQLQuery(sql);
        query.setLong("aid",aid);
        list = query.list();
        return list;
    }

    /**
     * 查询该权限id是否是该活动的主办方或协办方
     * @param aid 活动id
     * @param pid 权限id
     * @return java.util.List
     * @author liumengwei
     * @date 2017/11/25
     */
    public ActivityTake getActivityTake(Long aid, Long pid) {
        Session session = getSession();
        String hql = "from ActivityTake " +
                "where aid = :aid " +
                "and privilegeId = :eid " +
                "and status = 0 ";
        Object query = session.createQuery(hql).
                setLong("aid",aid).
                setLong("eid",pid).
                uniqueResult();
        ActivityTake activityTakes = (ActivityTake) query;
        return activityTakes;
    }
}
