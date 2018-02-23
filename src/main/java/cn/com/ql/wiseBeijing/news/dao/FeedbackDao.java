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
import cn.com.ql.wiseBeijing.news.daoBean.Feedback;
import cn.com.ql.wiseBeijing.user.dao.UserBindDao;
import cn.com.ql.wiseBeijing.user.dao.UserDao;
import cn.xxtui.support.page.Page;
import cn.xxtui.support.page.PageWaterfallAdapter;
import cn.xxtui.support.page.ResultTransfer;

@Component
public class FeedbackDao extends BasicDao<Feedback> {
	@Bean
	public FeedbackDao feedbackDao() {
		return new FeedbackDao();
	}

	@Resource(name = "userDao")
	private UserDao userDao;
	@Resource(name = "userBindDao")
	private UserBindDao userBindDao;
	@Resource(name="likesDao")
	private LikesDao likesDao;

	public Page<Map> getByWID(String wid,int current,int amount)
	{
		final Session session = getSession();
		String hql = "from Feedback where workID=:wid order  by id desc";
		Query newsQuery = session.createQuery(hql);
		newsQuery.setString("wid", wid);
		return getFeedAll(current, amount, newsQuery);
	}
	
	public int updateStatusByWID(String wid,String status)
	{
		final Session session = getSession();
		String hql = "update Feedback set status=:status where workID=:wid";
		Query newsQuery = session.createQuery(hql);
		newsQuery.setString("wid", wid);
		newsQuery.setString("status", status);
		return newsQuery.executeUpdate();
	}
	
	public Page<Map> search(int current, int amount) {
		final Session session = getSession();
		String hql = "select workID,userid,max(createtime),max(status),category from Feedback group by workID,userid order  by id desc";
		Query newsQuery = session.createQuery(hql);
		return getGroupByWid(current, amount, newsQuery);
	}
	
	public Page<Map> searchByStatus( String status,int current, int amount) {
		final Session session = getSession();
		String hql = "select workID,userid,max(createtime),max(status),category from Feedback where status=:status group by workID,userid  order  by id desc";
		Query newsQuery = session.createQuery(hql);
		newsQuery.setString("status", status);
		return getGroupByWid(current, amount, newsQuery);
	}

	private Page<Map> getGroupByWid(int current, int amount, Query newsQuery) {
		PageWaterfallAdapter adapter = new PageWaterfallAdapter(newsQuery);
		Page<Map> page = adapter.execute(current, amount,
				new ResultTransfer<Map>() {
					@Override
					public List<Map> transfer(List list) {
						Iterator<Object[]> it = list.iterator();
						List<Map> rcl = new ArrayList<Map>();
						while (it.hasNext()) {
							Object[] o = it.next();
							Map<String, Object> map = new HashMap<String, Object>();
							
							map.put("wid", o[0]);
							map.put("createTime", o[2]);
							map.put("status", o[3]);
							String uid=(String)o[1];
							if(uid!=null&&!uid.trim().equals(""))
							{
								map.put("user", userDao.getUser(uid));
							}
							else
							{
								map.put("user","{}");
							}
							rcl.add(map);
						}
						return rcl;
					}
				});
		return page;
	}
	

	
	
	
	private Page<Map> getFeedAll(int current, int amount, Query newsQuery) {
		PageWaterfallAdapter adapter = new PageWaterfallAdapter(newsQuery);
		Page<Map> page = adapter.execute(current, amount,
				new ResultTransfer<Map>() {
					@Override
					public List<Map> transfer(List list) {
						Iterator<Feedback> it = list.iterator();
						List<Map> rcl = new ArrayList<Map>();
						while (it.hasNext()) {
							Feedback o = it.next();
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("comment", o.getComment());
							map.put("createTime", o.getCreatetime());
							map.put("email", o.getEmail());
							map.put("uid", o.getUserid());
							map.put("wid", o.getWorkID());
							map.put("status", o.getStatus());
							map.put("category", o.getCategory());
							map.put("type",o.getType());
							rcl.add(map);
						}
						return rcl;
					}
				});
		return page;
	}
	
}
