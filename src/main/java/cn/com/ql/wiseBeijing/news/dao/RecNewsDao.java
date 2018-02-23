package cn.com.ql.wiseBeijing.news.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.com.ql.wiseBeijing.news.daoBean.RecNews;
import cn.xxtui.support.page.Page;
import cn.xxtui.support.page.PageWaterfallAdapter;
import cn.xxtui.support.page.ResultTransfer;

@Component
public class RecNewsDao extends BasicDao<RecNews> {
	@Bean
	public RecNewsDao recNewsDao() {
		return new RecNewsDao();
	}

	@Resource(name="newsDao")
	private NewsDao newsDao;

	

	

	
	

	/***
	 * 绑定的推荐新闻
	 * 
	 * @param pid 父新闻
	 * @param amount
	 * @param platform
	 * @return
	 */
	public Page<Map> getRecNewsByPID(String pid, int amount, String platform) {
		Session session = getSession();
		Query query = session
				.createQuery("from RecNews where pnid=:pid and platform=:platform");
		query.setString("pid", pid);
		query.setString("platform", platform);
		PageWaterfallAdapter adapter = new PageWaterfallAdapter(query);
		Page<Map> page = adapter.execute(0, amount, new ResultTransfer<Map>() {
			@Override
			public List<Map> transfer(List list) {
				Iterator<RecNews> it = list.iterator();
				List<Map> rl = new ArrayList<Map>();
				while (it.hasNext()) {
					RecNews o = it.next();
					Map map = newsDao.getNews(o.getNid_list(), o.getPlatform());	
					rl.add(map);
				}
				return rl;
			}
		});
		return page;
	}

	/**
	 * 
	 * @param belong
	 *            :parent nid
	 * @param nid
	 *            :child nid
	 */
	/**
	 * 
	 * @param pnid:parent nid
	 * @param cnid:child nid
	 */
	public Serializable saveRecNews(String pnid,String cnid,String platform)
	{
		RecNews topic=new RecNews();
		topic.setPlatform(platform);
		topic.setPnid(pnid);
		topic.setNid_list(cnid);
		return save(topic);
	}

	/**
	 * 推荐解除绑定
	 * @param pnid
	 * @param platform
	 * @return
	 */
	public Serializable recNewsUnBind(String pnid,String platform) {
		Session session=getSession();
		Query query=session.createQuery("delete from RecNews where pnid=:pnid and platform=:platform");
		query.setString("pnid", pnid);
		query.setString("platform", platform);
		return String.valueOf(query.executeUpdate());
	}
}
