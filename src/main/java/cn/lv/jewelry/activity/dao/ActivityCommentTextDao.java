package cn.lv.jewelry.activity.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.activity.daoBean.ActivityCommentStatus;
import cn.lv.jewelry.activity.daoBean.ActivityCommentText;
import org.hibernate.Criteria;
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
public class ActivityCommentTextDao extends BasicDao<ActivityCommentText> {
    @Bean
    public ActivityCommentTextDao activityCommentTextDao() {
        return new ActivityCommentTextDao();
    }
    private final static Logger logger = LoggerFactory.getLogger(ActivityCommentText.class);

    public List<Map<String, Object>> getActivityCommentText(Long cid) {
        Session session = getSession();
        Map<String, Object> map = new HashMap<>();
        List listMap = new ArrayList();
        String hql =
                "select commentContent as commentContent " +
                "from cn.lv.jewelry.activity.daoBean.ActivityCommentText " +
                "where cid = :cid " +
                "and commentStatus = :commentStatus";
        List<Map<String, Object>> list = session.createQuery(hql)
                .setLong("cid", cid)
                .setInteger("commentStatus", ActivityCommentStatus.NORMAL.getStatus())
                .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                .list();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                map.put("commentContent", list.get(i).get("commentContent"));
                listMap.add(map);
            }
        }
        return listMap;
    }

    /**
     * 获取该评论信息
     * @param cid 评论id
     * @return cn.lv.jewelry.activity.daoBean.ActivityCommentText
     * @author liumengwei
     * @date 2017/12/10
     */
    public List<Map<String, Object>> getActivityComment(Long cid) {
        Session session = getSession();
        List<Map<String, Object>> text = null;
        try {
            String sql =
                    "select ac.aid as aid, ac.comment_type as commentType, act.id, act.cid as cid,\n" +
                            "       act.comment_content commentContent, act.comment_time as commentTime,\n" +
                            "       act.comment_status as commentStatus, act.a_status as aStatus,\n" +
                            "  u.username, u.headpic as headPic, u.id as uid, p.user_type as userType," +
                            "ac.is_delete as isDelete\n" +
                            "from activity_comment ac\n" +
                            "  RIGHT JOIN activity_privilege p on ac.privilege_id = p.id\n" +
                            "  LEFT JOIN activity_comment_text act on act.cid = ac.id\n" +
                            "  RIGHT JOIN user u on p.user_id = u.id\n" +
                            "where ac.id = :cid\n" +
                            "      and ac.comment_status = 0\n" +
                            "      and act.comment_status = 0\n" +
                            "      and u.status = 1";
            text = session.createSQLQuery(sql)
                    .setLong("cid", cid)
                    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
            .list();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return text;
    }
}