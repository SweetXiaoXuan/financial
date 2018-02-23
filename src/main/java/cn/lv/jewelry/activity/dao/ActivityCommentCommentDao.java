package cn.lv.jewelry.activity.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.activity.daoBean.ActivityCommentComment;
import cn.lv.jewelry.activity.daoBean.ActivityCommentStatus;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/23 0023.
 */
@Component
public class ActivityCommentCommentDao extends BasicDao<ActivityCommentComment> {
    @Bean
    public ActivityCommentCommentDao activityCommentCommentDao() {
        return new ActivityCommentCommentDao();
    }

    /**
     * 获取评论的评论的数量
     * @param cid 评论id
     * @return java.lang.Integer
     */
    public Integer getCommentNumber(Long cid) {
        Session session = getSession();
        String hql =
                "select count(id) " +
                        "from cn.lv.jewelry.activity.daoBean.ActivityCommentComment " +
                        "where cid = :cid " +
                        "and commentStatus = :commentStatus";
        List list = session.createQuery(hql)
                .setLong("cid", cid)
                .setInteger("commentStatus", ActivityCommentStatus.NORMAL.getStatus())
                .list();
        if (list.get(0) != null) {

            String num = list.get(0).toString();
            return Integer.parseInt(num);
        }
        return 0;
    }

    /**
     * 获取评论的评论
     * @param cid 评论id
     * @return java.util.List
     */
    public List<Map<String, Object>> getCommentCommentByAid(Long cid) {
        Session session = getSession();
        String sql =
                "select  c.id as cid, ifnull(u.id, ub.id) as uid, c.comment_time as commentTime, c.comment_content as commentContent,\n" +
                        "        ifnull(u.headpic, ub.headpic) as headPic, ifnull(u.username, ub.username) as username\n" +
                        "from activity_comment_comment c\n" +
                        "  LEFT JOIN activity_privilege p on c.privilege_id = p.id\n" +
                        "  LEFT JOIN user u on p.user_id = u.id and p.user_type = 0\n" +
                        "  LEFT JOIN user_bind ub on p.user_id = ub.id and p.user_type = 2\n" +
                        "where  c.cid = :ccid\n" +
                        "       and c.comment_status = 0 and c.is_delete = 0\n" +
                        "order by c.id";
        List<Map<String, Object>> list = session.createSQLQuery(sql)
                .setLong("ccid", cid)
                .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
                .list();
        return list;
    }
}