package cn.com.ql.wiseBeijing.news.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.com.ql.wiseBeijing.dec.dao.DecNewsDao;
import cn.com.ql.wiseBeijing.dec.daoBean.DecMainNews;
import cn.com.ql.wiseBeijing.imageService.dao.ImagesDao;
import cn.com.ql.wiseBeijing.imageService.daoBean.Images;
import cn.com.ql.wiseBeijing.news.daoBean.Comment;
import cn.com.ql.wiseBeijing.news.daoBean.MainNews;
import cn.com.ql.wiseBeijing.policy.dao.PolicyItemDao;
import cn.com.ql.wiseBeijing.policy.daoBean.PolicyItem;
import cn.com.ql.wiseBeijing.serviceUtil.NewsPlatform;
import cn.com.ql.wiseBeijing.user.dao.UserBindDao;
import cn.com.ql.wiseBeijing.user.dao.UserDao;
import cn.xxtui.support.page.Page;
import cn.xxtui.support.page.PageWaterfallAdapter;
import cn.xxtui.support.page.ResultTransfer;

@Component
public class CommentDao extends BasicDao<Comment> {
	@Bean
	public CommentDao commentDao() {
		return new CommentDao();
	}

	@Resource(name = "userDao")
	private UserDao userDao;
	@Resource(name = "userBindDao")
	private UserBindDao userBindDao;
	@Resource(name="likesDao")
	private LikesDao likesDao;
	@Resource(name="newsDao")
	private NewsDao newsDao;
	@Resource(name="decNewsDao")
	private DecNewsDao decNewsDao;
	@Resource(name="policyItemDao")
	private PolicyItemDao policyItemDao;
	@Resource(name="imagesDao")
	private ImagesDao imagesDao;
	public Page<Map> getCommentByUser(String user, int current) {
		Session session = getSession();
		Query query = session
				.createQuery("from Comment where userid=:userid order by id desc");
		query.setString("userid", user);
		PageWaterfallAdapter adapter = new PageWaterfallAdapter(query);
		Page<Map> page = adapter.execute(current, 20,
				new ResultTransfer<Map>(){
					@Override
					public List<Map> transfer(List list) {
						Iterator<Comment> it = list.iterator();
						List<Map> rl = new ArrayList<Map>();
						while (it.hasNext()) {
							Map<String, Object> map = new HashMap<String, Object>();
							Comment comment = it.next();
							map.put("pcommentuser", "{}");
							map.put("pcomment", "{}");
							if (comment.getCommentid() != 0) {
								Comment pcomment = get(Comment.class,
										comment.getCommentid());
								Map<String, Object> user = userDao.getUser(pcomment.getUserid());
								map.put("pcommentuser", user);
								map.put("pcomment", pcomment);
							}
							String platform=comment.getPlatform();
							map.put("detail", newsDao.getNews(comment.getNewsid(), platform));
							map.put("status", comment.getStatus());
							map.put("comment", comment.getComment());
							map.put("comment_time", comment.getCreatetime());
							map.put("cid", String.valueOf(comment.getId()));
							rl.add(map);
						}
						return rl;
					}

					
				});
		return page;
	}
	
	
	private Page<Map> getComments(Session session,Query query,int current)
	{
		PageWaterfallAdapter adapter = new PageWaterfallAdapter(query);
		Page<Map> page = adapter.execute(current, 20,
				new ResultTransfer<Map>() {
					@Override
					public List<Map> transfer(List list) {
						Iterator<Comment> it = list.iterator();
						List<Map> rl = new ArrayList<Map>();
						while (it.hasNext()) {
							Map<String, Object> map = new HashMap<String, Object>();
							Comment comment = it.next();
							if (comment.getCommentid() != 0) {
								Comment pcomment = get(Comment.class,
										comment.getCommentid());
								
								Map<String, Object> user = userDao.getUser(pcomment.getUserid());
								map.put("pcommentuser", user);
								map.put("pcomment", pcomment);
							}
							map.put("status", comment.getStatus());
							map.put("comment", comment.getComment());
							map.put("comment_time", comment.getCreatetime());
							map.put("platform", comment.getPlatform());
							map.put("cid", String.valueOf(comment.getId()));
							map.put("user", userDao.getUser(comment.getUserid()));
							rl.add(map);
						}
						return rl;
					}
				});
		return page;
	}
	
	public Page<Map> getCommentStatus(String status, int current) {
		Session session = getSession();
		Query query = session
				.createQuery("from Comment where status=:status order by id desc");
		query.setString("status", status);
		return getComments(session, query, current);
	}
	
	public Page<Map> getCommentAll(int current) {
		Session session = getSession();
		Query query = session
				.createQuery("from Comment order by id desc");
		return getComments(session, query, current);
	}
	
	public Page<Map> getCommentStatusAndUsername(String userid,String status, int current) {
//		Session session = getSession();
//		String comment = "and 1=1 ";
//		String uid = " and 1=1 ";
//		if(userid!=null){
//			comment = "and comment like '%"+userid.substring(2)+"%'";
//			uid = " and userid like '%"+userid.substring(2)+"%'";
//		}
//		Query query = session
//				.createQuery("from Comment where status=:status "+uid+" "+comment+" order by id desc");
//		query.setString("status", status);
		Session session = getSession();
		Query query = session
				.createQuery("from Comment where status=:status and userid=:userid order by id desc");
		query.setString("status", status);
		query.setString("userid", userid);
		return getComments(session, query, current);
	}
	public Page<Map> getCommentKeyword(String keyword,String status,int current) {
//		Session session = getSession();
//		String comment = "and 1=1 ";
//		String uid = " and 1=1 ";
//		if(userid!=null){
//			comment = "and comment like '%"+userid.substring(2)+"%'";
//			uid = " and userid like '%"+userid.substring(2)+"%'";
//		}
//		Query query = session
//				.createQuery("from Comment where status=:status "+uid+" "+comment+" order by id desc");
//		query.setString("status", status);
		Session session = getSession();
		if(status!=null){
			status = "status = "+status;
		}else{
			status = " 1 = 1";
		}
		Query query = session
				.createQuery("from Comment where "+status+" and comment like '%"+keyword+"%' order by id desc");
		return getComments(session, query, current);
	}
	public Page<Map> getCommentByNewsID(final String uid,String newsid, String platform,
			int current) {
		Session session = getSession();
		Query query = session
				.createQuery("from Comment where newsid=:newsid and platform=:platform and status='0' and commentid='0' order by id desc");
		query.setString("newsid", newsid);
		query.setString("platform", platform);
		return getCommentByUser(uid, current, query);
	}
	/**
	 * 获取评论的评论的列表
	 * @param pcid
	 * @param current
	 * @return
	 */
	public Page<Map> getCCommentByPCID(int pcid,int current) {
		Session session = getSession();
		Query query = session
				.createQuery("from Comment where commentid=:commentid and status='0' order by id desc");
		query.setInteger("commentid", pcid);
		return getComments(session, query, current);
	}
	/**
	 * 获取评论的评论的数目
	 * @param pcid
	 * @return
	 */
	public long getCCommentsCountsByPCID(int pcid) {
		Session session = getSession();
		Query query = session
				.createQuery("select count (*) from Comment where commentid=:commentid and status='0'");
		query.setInteger("commentid", pcid);
		return (Long) (query.uniqueResult());
	}
	/**
	 * 
	 * @param uid 指的是当前看这些评论的用户
	 * @param current
	 * @param query
	 * @return
	 */
	private Page<Map> getCommentByUser(final String uid, int current,
			Query query) {
		PageWaterfallAdapter adapter = new PageWaterfallAdapter(query);
		Page<Map> page = adapter.execute(current, 20,
				new ResultTransfer<Map>() {
					@Override
					public List<Map> transfer(List list) {
						Iterator<Comment> it = list.iterator();
						List<Map> rl = new ArrayList<Map>();
						while (it.hasNext()) {
							Map<String, Object> map = new HashMap<String, Object>();
							Comment comment = it.next();
							if (comment.getCommentid() != 0) {
								Comment pcomment = get(Comment.class,
										comment.getCommentid());
								
								Map<String, Object> user = userDao.getUser(pcomment
										.getUserid());
								map.put("pcommentuser", user);
								map.put("pcomment", pcomment);
							}
							String userid = comment.getUserid();
							if (userid.length() < 3) {
								// 异常
							} else {
								Map<String, Object> user = userDao.getUser(userid);
								map.put("user", user);
							}
							if(likesDao.isClickLike(comment.getId(), uid))
							{
								map.put("clicked", "clicked");
							}
							else
							{
								map.put("clicked", "0");
							}
							map.put("comment", comment.getComment());
							map.put("comment_time", comment.getCreatetime());
							map.put("status", comment.getStatus());
							map.put("likes", String.valueOf(likesDao.getLikesCountsByComment(comment.getId())));
							map.put("ccomments", String.valueOf(getCCommentsCountsByPCID(comment.getId())));
							map.put("cid", String.valueOf(comment.getId()));
							rl.add(map);
						}
						return rl;
					}
				});
		return page;
	}
	
	
	/**
	 * 评论被评论
	 * @param uid 用户的评论
	 * @param newsid
	 * @param platform
	 * @param current
	 * @return
	 */
	public Page<Map> getCCommentByNewsID(final String uid,String platform,
			int current) {
		Session session = getSession();
		//Query query = session.createQuery("from Comment where commentid in (select id from Comment where userid=:userid and platform=:platform and status='0' order by id desc) order by id desc");
		Query query = session.createQuery("from Comment where commentid in (select id from Comment where userid=:userid  and status='0' order by id desc) and status=0 order by id desc");
		query.setString("userid", uid);
		//query.setString("platform", platform);
		return getComments(current, query);
	}
	
	/**
	 * 获取评论
	 * @param current
	 * @param query
	 * @return
	 */
	private Page<Map> getComments(int current,
			Query query) {
		PageWaterfallAdapter adapter = new PageWaterfallAdapter(query);
		Page<Map> page = adapter.execute(current, 20,
				new ResultTransfer<Map>() {
					@Override
					public List<Map> transfer(List list) {
						Iterator<Comment> it = list.iterator();
						List<Map> rl = new ArrayList<Map>();
						while (it.hasNext()) {
							Map<String, Object> map = new HashMap<String, Object>();
							Comment comment = it.next();
							if (comment.getCommentid() != 0) {
								Comment pcomment = get(Comment.class,
										comment.getCommentid());
								
								Map<String, Object> user = userDao.getUser(pcomment
										.getUserid());
								map.put("pcommentuser", user);
								map.put("pcomment", pcomment);
							}
							String userid = comment.getUserid();
							if (userid.length() < 3) {
								// 异常
							} else {
								Map<String, Object> user = userDao.getUser(userid);
								map.put("user", user);
							}
							
							map.put("detail", newsDao.getNews(comment.getNewsid(), comment.getPlatform()));
							map.put("comment", comment.getComment());
							map.put("comment_time", comment.getCreatetime());
							map.put("status", comment.getStatus());
							map.put("likes", String.valueOf(likesDao.getLikesCountsByComment(comment.getId())));
							map.put("ccomments", String.valueOf(getCCommentsCountsByPCID(comment.getId())));
							map.put("cid", String.valueOf(comment.getId()));
							rl.add(map);
						}
						return rl;
					}
				});
		return page;
	}
	
}
