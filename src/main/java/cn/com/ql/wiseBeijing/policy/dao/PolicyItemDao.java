package cn.com.ql.wiseBeijing.policy.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.com.ql.wiseBeijing.policy.daoBean.PolicyItem;
import cn.xxtui.support.page.Page;
import cn.xxtui.support.page.PageWaterfallAdapter;
import cn.xxtui.support.page.ResultTransfer;

@Component
public class PolicyItemDao extends BasicDao<PolicyItem> {

	@Bean
	public PolicyItemDao policyItemDao() {
		return new PolicyItemDao();
	}
	/**
	 * 根据title与关键字词进行搜索
	 * @param keywords
	 * @param current
	 * @param amount
	 * @return
	 */
	public Page<Map> search(String keywords, int current, int amount) {
		final Session session = getSession();
		String hql = "select main.nid,main.title,main.description,main.keywords,main.pubdate,main.listimage,main.largeimage,main.smallimage,main.template,main.type,main.pid from PolicyItem as main where main.title like '%"
				+ keywords + "%' or main.keywords like '%" + keywords + "%' and main.status=0 order by main.createtime desc";
		Query newsQuery = session.createQuery(hql);
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
							map.put("nid", o[0]);
							map.put("title", o[1]);
							map.put("description", o[2]);
							map.put("keywords", o[3]);
							map.put("pubdate", o[4]);
							map.put("listimage", o[5]);
							map.put("largeimage", o[6]);
							map.put("smallimage", o[7]);
							map.put("template", o[8]);
							map.put("type", o[9]);
							map.put("pid", o[10]);
							rcl.add(map);
						}
						return rcl;
					}
				});
		return page;
	}
	
	public int delete(String id){
		String hql = "update PolicyItem set status=:status where pid=:id";
		Query newsQuery = getSession().createQuery(hql);
		newsQuery.setString("id", id);
		newsQuery.setString("status","1");
		return newsQuery.executeUpdate();
	}
	public int del(String id){
		String hql = "update PolicyItem set status=:status where id=:id";
		Query newsQuery = getSession().createQuery(hql);
		newsQuery.setString("status","1");
		newsQuery.setString("id", id);
		return newsQuery.executeUpdate();
	}
	public PolicyItem get(String id){
		return (PolicyItem) getSession().get(PolicyItem.class, id);
	}
	public void update(PolicyItem item){
		String hql = "update PolicyItem set title=:title,keywords=:keywords,content=:content,description=:description where nid=:id";
		Query query =	getSession().createQuery(hql);
		query.setString("title", item.getTitle());
		query.setString("keywords", item.getKeywords());
		query.setString("content", item.getContent());
		query.setString("description", item.getDescription());
		query.setString("id", item.getNid());
		int size = query.executeUpdate();
		System.out.println(size);
	}
	
}
