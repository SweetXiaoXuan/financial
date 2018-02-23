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
import cn.com.ql.wiseBeijing.news.daoBean.Collect;
import cn.com.ql.wiseBeijing.user.dao.UserBindDao;
import cn.com.ql.wiseBeijing.user.dao.UserDao;
import cn.xxtui.support.page.Page;
import cn.xxtui.support.page.PageWaterfallAdapter;
import cn.xxtui.support.page.ResultTransfer;

@Component
public class CollectDao extends BasicDao<Collect> {
	@Bean
	public CollectDao collectDao() {
		return new CollectDao();
	}

	@Resource(name = "userDao")
	private UserDao userDao;
	@Resource(name = "userBindDao")
	private UserBindDao userBindDao;
	@Resource(name="likesDao")
	private LikesDao likesDao;
	@Resource(name = "newsDao")
	private NewsDao newsDao;
	public Page<Map> getCollectByUser(String user, int current) {
		Session session = getSession();
		Query query = session
				.createQuery("from Collect where userid=:userid order by id desc");
		query.setString("userid", user);
		PageWaterfallAdapter adapter = new PageWaterfallAdapter(query);
		Page<Map> page = adapter.execute(current, 20,
				new ResultTransfer<Map>() {
					@Override
					public List<Map> transfer(List list) {
						Iterator<Collect> it = list.iterator();
						List<Map> rl = new ArrayList<Map>();
						while (it.hasNext()) {
							Map<String, Object> map = new HashMap<String, Object>();
							Collect comment = it.next();
							map.put("status", comment.getStatus());
							map.put("platform", comment.getPlatform());
							map.put("ccid", comment.getCid());
							map.put("cid", String.valueOf(comment.getId()));
							map.put("createTime", comment.getCreatetime());
							map.put("details", newsDao.getNews(comment.getCid(), comment.getPlatform()));
							rl.add(map);
						}
						return rl;
					}
				});
		return page;
	}
	
}
