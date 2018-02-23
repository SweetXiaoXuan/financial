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
import cn.com.ql.wiseBeijing.news.daoBean.Comment;
import cn.com.ql.wiseBeijing.news.daoBean.Warning;
import cn.com.ql.wiseBeijing.user.dao.UserBindDao;
import cn.com.ql.wiseBeijing.user.dao.UserDao;
import cn.xxtui.support.page.Page;
import cn.xxtui.support.page.PageWaterfallAdapter;
import cn.xxtui.support.page.ResultTransfer;

@Component
public class WarningDao extends BasicDao<Warning> {
	@Bean
	public WarningDao warningDao() {
		return new WarningDao();
	}

	@Resource(name = "userDao")
	private UserDao userDao;
	@Resource(name = "userBindDao")
	private UserBindDao userBindDao;
	@Resource(name = "likesDao")
	private LikesDao likesDao;
	@Resource(name = "newsDao")
	private NewsDao newsDao;
	@Resource(name = "decNewsDao")
	private DecNewsDao decNewsDao;
	@Resource(name = "commentDao")
	private CommentDao commentDao;

	public Page<Map> getWarningByUser(String user, int current) {
		Session session = getSession();
		Query query = session
				.createQuery("from Warning where userid=:userid order by id desc");
		query.setString("userid", user);
		return get(current, 20, query);
	}

	public Page<Map> getWarnings(int current, int amount) {
		Session session = getSession();
		Query query = session.createQuery("from Warning order by createtime desc ");
		return get(current, 20, query);
	}

	public Page<Map> getWarningByID(int id) {
		Session session = getSession();
		Query query = session.createQuery("from Warning where id=:id");
		query.setInteger("id", id);
		return get(0, 20, query);
	}

	public Page<Map> getWarningByStatus(String status, int current, int amount) {
		Session session = getSession();
		Query query = session.createQuery("from Warning where status=:status order by createtime desc");
		query.setString("status", status);
		return get(current, 20, query);
	}

	private Page<Map> get(int current, int amount, Query query) {
		PageWaterfallAdapter adapter = new PageWaterfallAdapter(query);
		Page<Map> page = adapter.execute(current, amount,
				new ResultTransfer<Map>() {
					@Override
					public List<Map> transfer(List list) {
						Iterator<Warning> it = list.iterator();
						List<Map> rl = new ArrayList<Map>();
						while (it.hasNext()) {
							Map<String, Object> map = new HashMap<String, Object>();
							Warning comment = it.next();
							System.out.println(comment.getId());
							if (comment.getCommentid() != 0) {
								Warning pcomment = get(Warning.class,
										comment.getCommentid());
								if(pcomment!=null){
									Map<String, Object> user = userDao
											.getUser(pcomment.getUserid());
									map.put("pcommentuser", user);
									map.put("pcomment", pcomment);
								}
							}
							map.put("wid", String.valueOf(comment.getId()));
							map.put("status", comment.getStatus());
							map.put("comment", comment.getComment());
							map.put("comment_time", comment.getCreatetime());
							map.put("user",userDao.getUser(comment.getUserid()));
							map.put("type", comment.getType());
							String platform = comment.getPlatform();
							
							/*
							 * map.put("news", newsDao.getNews(
							 * comment.getNewsid(), platform));
							 */
							Comment c = null;
							try {
								c = commentDao.get(Comment.class,comment.getCommentid());
							} catch (Exception ex) {
								ex.printStackTrace();
							}
							if (c != null)
							{
								map.put("ccomment", c);
								map.put("cuser", userDao.getUser(c.getUserid()));
							}
							else
							{
								map.put("ccomment", "{}");
								map.put("cuser", "{}");
							}
							map.put("platform", platform);
							rl.add(map);
						}
						return rl;
					}
				});
		return page;
	}
}
