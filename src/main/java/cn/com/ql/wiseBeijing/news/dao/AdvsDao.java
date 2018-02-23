package cn.com.ql.wiseBeijing.news.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.com.ql.wiseBeijing.dec.dao.DecNewsDao;
import cn.com.ql.wiseBeijing.dec.daoBean.DecMainNews;
import cn.com.ql.wiseBeijing.news.daoBean.AdvBind;
import cn.com.ql.wiseBeijing.news.daoBean.Advs;
import cn.com.ql.wiseBeijing.news.daoBean.MainNews;
import cn.xxtui.support.page.Page;
import cn.xxtui.support.page.PageWaterfallAdapter;
import cn.xxtui.support.page.ResultTransfer;
import cn.xxtui.support.util.DateUtil;

@Component
public class AdvsDao extends BasicDao<Advs> {
	@Resource(name = "newsDao")
	private NewsDao newsDao;
	@Autowired
	private DecNewsDao decNewsDao;
	@Bean
	public AdvsDao advsDao() {
		return new AdvsDao();
	}

	/**
	 * 获取启动广告
	 * 
	 * @return
	 */
	public Page<Map> getAdvLaunch() {
		Session session = getSession();
		String current = DateUtil.getCurrentDateForSql();
		Query query = getSession()
				.createQuery(
						"from Advs where target = 1 and '"+current+"'>=beginTime  and '"+current+"' <=endTime");
		PageWaterfallAdapter adapterr = new PageWaterfallAdapter(query);
		Page<Map> page = adapterr.execute(0, 10, new ResultTransfer<Map>() {
			@Override
			public List<Map> transfer(List list) {
				Iterator<Advs> it = list.iterator();
				List<Map> rl = new ArrayList<Map>();
				while (it.hasNext()) {
					Advs o = it.next();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("pnid", o.getId());
					map.put("title", o.getTitle());
					map.put("pubdate", o.getBeginTime());
					map.put("largeimage", o.getLargeimage());
					map.put("type", o.getType());
					map.put("createtime", o.getCreateTime());
					map.put("pubtime", o.getBeginTime());
					map.put("status", o.getStatus());
					map.put("content", o.getContent());
					rl.add(map);
				}
				return rl;
			}
		});
		return page;
	}

	public Page<Map> getSearchAll(int p, int amount ,String target, String title) {
		Session session = getSession();
		String current = DateUtil.getCurrentDateForSql();
		String sql = "from Advs where ";
		if (title != null)
			sql += " title like '%" + title + "%' and ";
		sql += "target=:target and status='0' order by createTime desc";

		Query query = getSession().createQuery(sql);
		query.setString("target", target);
		return getAllAdv(session, query, p, amount);
	}

	public Page<Map> getSearchWork(int p, int amount, 
			String target,String title) {
		String current = DateUtil.getCurrentDateForSql();
		String sql = "from Advs where ";
		if (title != null) {
			sql += " title like '%" + title + "%' and ";
		}

		sql +="'"+ current + "'<=endTime and '" + current
				+ "'>=beginTime and target=:target and status='0' order by createTime desc";

		Session session = getSession();
		Query query = getSession().createQuery(sql);
		query.setString("target", target);
		return getAllAdv(session, query, p, amount);
	}

	public Page<Map> getSearchUnBegin(int p, int amount, String target,
			String title) {
		String current = DateUtil.getCurrentDateForSql();
		String sql = "from Advs where ";
		if (title != null) {
			sql += " title like '%" + title + "%' and ";
		}
		sql += "'"+current
				+ "'<=beginTime and target=:target  and status='0' order by createTime desc";
		Session session = getSession();
		Query query = getSession().createQuery(sql);
		query.setString("target", target);
		return getAllAdv(session, query, p, amount);
	}

	public Page<Map> getSearchEnd(int p, int amount, String target, String title) {
		String current = DateUtil.getCurrentDateForSql();
		String sql = "from Advs where ";
		if (title != null) {
			sql += " title like '%" + title + "%' and ";
		}
		sql += "'"+current+ "'>=endTime and target=:target and status='0' order by createTime desc";
		Session session = getSession();
		Query query = getSession().createQuery(sql);
		query.setString("target", target);
		return getAllAdv(session, query, p, amount);
	}

	/**
	 * 获取所有广告
	 * 
	 * @return
	 */
	private Page<Map> getAllAdv(Session session, Query query, int p, int amount) {
		PageWaterfallAdapter adapterr = new PageWaterfallAdapter(query);
		Page<Map> page = adapterr.execute(p, amount, new ResultTransfer<Map>() {
			@Override
			public List<Map> transfer(List list) {
				Iterator<Advs> it = list.iterator();
				List<Map> rl = new ArrayList<Map>();
				while (it.hasNext()) {
					Advs o = it.next();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("aid", String.valueOf(o.getId()));
					map.put("title", o.getTitle());
					map.put("endtime", o.getEndTime());
					map.put("largeimage", o.getLargeimage());
					map.put("type", o.getType());
					map.put("createtime", o.getCreateTime());
					map.put("begintime", o.getBeginTime());
					map.put("status", o.getStatus());
					map.put("content", o.getContent());
					map.put("target", o.getTarget());
					if (DateUtil.comepareTime(o.getBeginTime(), o.getEndTime()))
						map.put("adv_status", "0");
					else if (DateUtil.comepareTimeToday(o.getEndTime()))
						map.put("adv_status", "2");
					else if (!DateUtil.comepareTimeToday(o.getBeginTime()))
						map.put("adv_status", "1");
					rl.add(map);
				}
				return rl;
			}
		});
		return page;
	}

	/***
	 * 绑定的广告
	 * 
	 * @param id
	 * @param amount
	 * @param platform
	 * @return
	 */
	public Page<Map> getAdvByParentID(String id, int amount, String platform) {
		Session session = getSession();
		String current = DateUtil.getCurrentDateForSql();
		//iax
		Query query = session
				.createQuery("from AdvBind as t left join fetch t.adv as main where t.pnid=:pid and t.platform=:platform and main.id is not null and '"
									+ current
									+ "' <=main.endTime and main.target = 0 ");
		query.setString("pid", id);
		query.setString("platform", platform);
		PageWaterfallAdapter adapter = new PageWaterfallAdapter(query);
		Page<Map> page = adapter.execute(0, amount, new ResultTransfer<Map>() {

			@Override
			public List<Map> transfer(List list) {
				Iterator<AdvBind> it = list.iterator();
				List<Map> rl = new ArrayList<Map>();
				while (it.hasNext()) {
					AdvBind o = it.next();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("pnid", o.getPnid());
					map.put("title", o.getAdv().getTitle());
					map.put("pubdate", o.getAdv().getBeginTime());
					map.put("largeimage", o.getAdv().getLargeimage());
					map.put("type", o.getAdv().getType());
					map.put("createtime", o.getCreatetime());
					map.put("pubtime", o.getAdv().getBeginTime());
					map.put("status", o.getStatus());
					map.put("content", o.getAdv().getContent());
					map.put("aid", String.valueOf(o.getAdv().getId()));
					rl.add(map);
				}
				return rl;
			}
		});
		if (page.getPageContent().isEmpty()) {
			query = getSession()
					.createQuery(
							"from Advs where '" + current + "'<=endTime and '"
									+ current
									+ "'>=beginTime and status='0'  and target = 0 order by createTime desc");
			PageWaterfallAdapter adapterr = new PageWaterfallAdapter(query);
			page = adapterr.execute(0, 10, new ResultTransfer<Map>() {
				@Override
				public List<Map> transfer(List list) {
					Iterator<Advs> it = list.iterator();
					List<Map> rl = new ArrayList<Map>();
					while (it.hasNext()) {
						Advs o = it.next();
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("title", o.getTitle());
						map.put("pubdate", o.getBeginTime());
						map.put("largeimage", o.getLargeimage());
						map.put("type", o.getType());
						map.put("createtime", o.getCreateTime());
						map.put("pubtime", o.getBeginTime());
						map.put("content", o.getContent());
						map.put("status", o.getStatus());
						rl.add(map);
					}
					return rl;
				}
			});
		}
		for(Map<String, Object> advMap:page.getPageContent()){
			String newsId = advMap.get("content").toString();
			MainNews mn= newsDao.get(MainNews.class,newsId);
			if(mn!=null){
				advMap.put("advNews", mn);
			}
			DecMainNews dmn = decNewsDao.get(DecMainNews.class, newsId);
			if(mn!=null){
				advMap.put("advDecNews", dmn);
			}
		}
		return page;
	}

	/**
	 * 
	 * @param belong
	 *            :parent nid
	 * @param nid
	 *            :child nid
	 */
	public Serializable saveAdvBind(MainNews belong, Advs adv) {
		AdvBind topic = new AdvBind();
		topic.setAdv(adv);
		topic.setPnid(belong.getNid());
		topic.setPlatform("news");
		return getSession().save(topic);
	}

	/**
	 * 广告解除绑定（每日资讯）
	 * @param belong
	 * @param adv
	 * @return
	 */
	public Serializable AdvUnBind(MainNews belong) {
		AdvBind topic = new AdvBind();
		Session session=getSession();
		Query query=session.createQuery("delete from AdvBind where pnid=:pnid and platform=:platform");
		query.setString("pnid", belong.getNid());
		query.setString("platform", "news");
		return String.valueOf(query.executeUpdate());
	}
	
	
	/**
	 * 
	 * @param belong
	 *            :parent nid
	 * @param nid
	 *            :child nid
	 */
	public Serializable saveAdvDecBind(DecMainNews belong, Advs adv) {
		AdvBind topic = new AdvBind();
		topic.setAdv(adv);
		topic.setPnid(belong.getNid());
		topic.setPlatform("dec");
		return getSession().save(topic);
	}
	/**
	 * 广告解除绑定（民生）
	 * @param belong
	 * @param adv
	 * @return
	 */
	public Serializable AdvDecUnBind(DecMainNews belong) {
		AdvBind topic = new AdvBind();
		Session session=getSession();
		Query query=session.createQuery("delete from AdvBind where pnid=:pnid and platform=:platform");
		query.setString("pnid", belong.getNid());
		query.setString("platform", "dec");
		return String.valueOf(query.executeUpdate());
	}
}
