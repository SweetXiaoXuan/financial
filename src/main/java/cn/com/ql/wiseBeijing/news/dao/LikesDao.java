package cn.com.ql.wiseBeijing.news.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.com.ql.wiseBeijing.news.daoBean.Comment;
import cn.com.ql.wiseBeijing.news.daoBean.Likes;
import cn.com.ql.wiseBeijing.user.dao.UserBindDao;
import cn.com.ql.wiseBeijing.user.dao.UserDao;
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

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
public class LikesDao extends BasicDao<Likes> {
	@Bean
	public LikesDao likesDao() {
		return new LikesDao();
	}
	private final static Logger logger = LoggerFactory.getLogger(LikesDao.class);
	@Resource(name = "userDao")
	private UserDao userDao;
	@Resource(name = "userBindDao")
	private UserBindDao userBindDao;
	@Resource(name = "newsDao")
	private NewsDao newsDao;
	@Resource(name = "commentDao")
	private CommentDao commentDao;

	/**
	 * 获取用户的点赞（赞别人）
	 * @param user
	 * @param current
	 * @return
	 */
	public Page<Map> getLikeByUser(String user, int current) {
		Session session = getSession();
		Query query = session
				.createQuery("from Likes as l left join fetch l.cid as c  where l.uid=:userid order by l.id desc");
		query.setString("userid", user);
		return getLikes(current, query);
	}

	/**
	 * 获取用户是否点赞
	 * @param aid 活动id
	 * @param privilegeId 用户id
	 * @param acid 图文id
	 * @return java.lang.Boolean
	 */
	public Likes getLikesByAidUidCid(Long aid, Long privilegeId, Long acid) {
		Session session = getSession();
		Object object = session.createQuery("from Likes where privilegeId = :privilegeId and aid = :aid and acid = :acid")
				.setLong("privilegeId", privilegeId)
				.setLong("aid", aid)
				.setLong("acid", acid)
				.uniqueResult();
		return (Likes) object;
	}

	public Integer getLikesNum(Integer id) {
		Session session = getSession();
		Object query = null;
		try {
			String sql =
					"select count(l.id) as num\n" +
							"from likes l\n" +
							"  RIGHT JOIN activity_privilege p on l.privilege_id = p.id\n" +
							"  LEFT JOIN user u on p.user_id = u.id\n" +
							"  LEFT JOIN activity_comment c on l.cid = c.id\n" +
							"where l.cid = :cid\n" +
							"      and c.comment_status = 0\n" +
							"      and u.status = 1 ";
			query =
					session.createSQLQuery(sql)
							.setLong("cid", id)
							.uniqueResult();
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return Integer.parseInt(query.toString());
	}

	/**
	 * 获取点赞信息(圈子详情页、活动详情页)
	 * @param aid
	 * @param cid
	 * @return
	 */
	public List<Map<String, Object>> getLikesById(Integer cid, String aid) {
		Session session = getSession();
		List<Map<String, Object>> query = null;
		try {
			String sql =
					"select u.id as uid, u.username as username, u.headpic as headPic\n" +
							"from likes l\n" +
							"  RIGHT JOIN activity_privilege p on l.privilege_id = p.id\n" +
							"  LEFT JOIN user u on p.user_id = u.id\n" +
							"  LEFT JOIN activity_comment c on l.cid = c.id\n" +
							"where l.cid = :cid\n" +
							"      and c.comment_status = 0\n" +
							"      and u.status = 1";
			query =
					session.createSQLQuery(sql)
							.setLong("cid", cid)
							.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
							.list();
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return query;
	}

	private Page<Map> getLikes(int current, Query query) {
		PageWaterfallAdapter adapter = new PageWaterfallAdapter(query);
		Page<Map> page = adapter.execute(current, 20,
				new ResultTransfer<Map>() {
					@Override
					public List<Map> transfer(List list) {
						Iterator<Likes> it = list.iterator();
						List<Map> rl = new ArrayList<Map>();
						while (it.hasNext()) {
							Map<String, Object> map = new HashMap<String, Object>();
							Likes like = it.next();
							Comment comment = commentDao.get(Comment.class, like.getCid());
							map.put("status", like.getStatus());
							map.put("luser",
									userDao.getUser(comment.getUserid()));
							map.put("ltime", like.getCreatetime());
							String cComment = comment.getComment();
							String news_platform = comment.getPlatform();
							String news_id = comment.getNewsid();
							Map mapn=newsDao.getNews(news_id, news_platform);
							Long uid = like.getPrivilegeId().getUid();
							Map mapu=userDao.getUser(uid.toString());
							mapu.put("comment", cComment);
							map.put("detail_news",mapn);
							map.put("detail_comment", mapu);
							rl.add(map);
						}
						return rl;
					}
				});
		return page;
	}

	/**
	 * 用户的评论被点赞
	 * @param uid 发表评论的用户
	 * @param platform
	 * @param current
	 * @return
	 */
	public Page<Map> getLCommentByNewsID(final String uid,String platform,
										 int current) {
		Session session = getSession();
		//Query query = session.createQuery("from Likes as l left join fetch l.cid as c where cid in (select id from Comment where userid=:userid and platform=:platform and status='0' order by id desc) order by id desc");
		Query query = session.createQuery("from Likes as l left join fetch l.cid as c where cid in (select id from Comment where userid=:userid and status='0' order by id desc) order by c.id desc");
		query.setString("userid", uid);
		//query.setString("platform", platform);
		return getLikes(current, query);
	}


	/**
	 * 判断有没有点赞
	 *
	 * @param cid
	 * @param uid
	 * @return
	 */
	public boolean isClickLike(int cid, String uid) {
		Session session = getSession();
		Query query = session
				.createQuery("from Likes where cid=:cid and uid=:uid");
		query.setInteger("cid", cid);
		query.setString("uid", uid);
		List o = query.list();
		System.out.println(o+"ffcc");
		if (o == null || o.isEmpty())
			return false;
		else
			return true;
	}

	public long getLikesCountsByComment(int cid) {
		Session session = getSession();
		Query query = session
				.createQuery("select count (*) from Likes where cid=:cid");
		query.setInteger("cid", cid);
		return (Long) (query.uniqueResult());
	}
}
