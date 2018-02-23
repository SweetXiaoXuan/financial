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
import cn.com.ql.wiseBeijing.policy.daoBean.PolicyCategory;
import cn.com.ql.wiseBeijing.policy.daoBean.PolicyItem;
import cn.xxtui.support.page.Page;
import cn.xxtui.support.page.PageWaterfallAdapter;
import cn.xxtui.support.page.ResultTransfer;
import cn.xxtui.support.util.DateUtil;

@Component
public class PolicyCategoryDao extends BasicDao<PolicyCategory> {

	@Bean
	public PolicyCategoryDao policyCategoryDao() {
		return new PolicyCategoryDao();
	}

	/**
	 * 整个列表中最大的时间
	 * 
	 * @return
	 */
	public String maxTime() {
		final Session session = getSession();
		Query query = session
				.createQuery("select max(pubdate) from PolicyCategory");
		String max_category = (String) query.uniqueResult();
		query = session.createQuery("select max(pubdate) from PolicyItem");
		String max_item = (String) query.uniqueResult();
		if(max_category==null&&max_item==null)
		{
			return DateUtil.getCurrentDateForSql();
		}
		if(max_category==null)
			return max_item;
		if(max_item==null)
			return max_category;
		if (DateUtil.comepareTimeFull(max_category, max_item))
			return max_item;
		else
			return max_category;
	}

	/**
	 * 递归得到所有的标题
	 * 
	 * @return
	 */
	public Page<Map> getAllIndex() {
		final Session session = getSession();
		Query query = session.createQuery("from PolicyCategory where clevel=0 and status='0'");
		PageWaterfallAdapter cardAdapter = new PageWaterfallAdapter(query);
		Page<Map> cardPage = cardAdapter.execute(0, 99,
				new ResultTransfer<Map>() {

					@Override
					public List<Map> transfer(List list) {
						return iter(list, 0);
					}
					private List<Map> iter(List list, int depth) {
						Iterator<PolicyCategory> pcit = list.iterator();
						List rv = new ArrayList();
						while (pcit.hasNext()) {
							PolicyCategory pc = pcit.next();
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("id", String.valueOf(pc.getId()));
							map.put("title", pc.getTitle());
							map.put("next", "1");
							map.put("logo", pc.getListimage());
							map.put("depth", depth);
							if (pc.getUrls() != null&&!pc.getUrls().trim().equals("")) {
								map.put("type", "url");
								map.put("next", "0");
								map.put("url", pc.getUrls());
							} else
								map.put("type", "list");
							rv.add(map);
							int o = depth;
							depth++;
							if (depth > 3) {
								continue;// 超过两层
							}
							// if (pc.getNext() == 0) {
							List all = new ArrayList();
							Query query_child_web = session
									.createQuery("from PolicyItem where pid=:pid and status='0'");
							query_child_web.setInteger("pid", pc.getId());
							List<PolicyItem> lpolicyItem = query_child_web
									.list();
							Iterator<PolicyItem> itPolicyItem = lpolicyItem
									.iterator();
							List rvv = new ArrayList();
							while (itPolicyItem.hasNext()) {
								PolicyItem item = itPolicyItem.next();
								Map<String, Object> mapp = new HashMap<String, Object>();
								mapp.put("id", item.getNid());
								mapp.put("title", item.getTitle());
								mapp.put("next", "0");
								mapp.put("logo", item.getListimage());
								mapp.put("depth", depth);
								if (item.getUrls() != null&&!item.getUrls().trim().equals("")) {
									mapp.put("type", "url");
									mapp.put("url", item.getUrls());
								} else
									mapp.put("type", "web");
								rvv.add(mapp);
							}
							all.addAll(rvv);
							// map.put("children", rvv);
							// } else {

							Query query_child_list = session
									.createQuery("from PolicyCategory where parent_id=:pid and status='0' and clevel!=0");
							query_child_list.setInteger("pid", pc.getId());
							List<Map> lm = iter(query_child_list.list(), depth);
							all.addAll(lm);
							map.put("children", all);
							depth = o;// 恢复depth值
							// }
						}
						return rv;
					}
				});
		return cardPage;
	}
	
	public int delete(String id){
		String hql = "update PolicyCategory set status=:status where id=:id";
		Query newsQuery = getSession().createQuery(hql);
		newsQuery.setString("id", id);
		newsQuery.setString("status","1");
		return newsQuery.executeUpdate();
	}
	public PolicyCategory get(String id){
		Integer ids = Integer.parseInt(id);
		return (PolicyCategory) getSession().get(PolicyCategory.class, ids);
	}
	public void update(PolicyCategory category){
		String hql = "update PolicyCategory set title=:title,keywords=:keywords,description=:description where id=:id";
		Query query =	getSession().createQuery(hql);
		query.setString("title", category.getTitle());
		query.setString("keywords", category.getKeywords());
		query.setString("description", category.getDescription());
		query.setInteger("id", category.getId());
		query.executeUpdate();
	}
	
}
