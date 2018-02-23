package cn.com.ql.wiseBeijing.news.dao;

import java.io.Serializable;
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
import cn.com.ql.wiseBeijing.news.daoBean.MainNews;
import cn.com.ql.wiseBeijing.news.daoBean.Topics;
import cn.xxtui.support.page.Page;
import cn.xxtui.support.page.PageWaterfallAdapter;
import cn.xxtui.support.page.ResultTransfer;

@Component
public class TopicsDao extends BasicDao<Topics> {
	@Bean
	public TopicsDao topicsDao()
	{
		return new TopicsDao();
	}
	
	public Page<Map> getTopicsByParentID(String id,int amount)
	{
		Session session=getSession();
		Query query=session.createQuery("from Topics as t left join fetch t.mainNews as main where t.pnid=:pid and main.nid is not null");
		query.setString("pid", id);
		PageWaterfallAdapter adapter=new PageWaterfallAdapter(query);
		Page<Map> page=adapter.execute(0, amount, new ResultTransfer<Map>() {

			@Override
			public List<Map> transfer(List list) {
				Iterator<Topics> it=list.iterator();
				List<Map> rl=new ArrayList<Map>();
				while(it.hasNext())
				{
					Topics o=it.next();
					Map<String,Object> map=new HashMap<String,Object>();
					map.put("nid", o.getMainNews().getNid());
					map.put("title", o.getMainNews().getTitle());
					map.put("description", o.getMainNews().getDescription());
					map.put("keywords", o.getMainNews().getKeywords());
					map.put("pubdate", o.getMainNews().getPubdate());
					map.put("listimage", o.getMainNews().getListimage());
					map.put("largeimage", o.getMainNews().getLargeimage());
					map.put("smallimage", o.getMainNews().getSmallimage());
					map.put("template", o.getMainNews().getTemplate());
					map.put("type", o.getMainNews().getType());
					map.put("pnid", o.getPnid());
					map.put("urls", o.getMainNews().getUrls());
					map.put("topic_createtime", o.getCreatetime());
					map.put("topic_pubtime", o.getPubtime());
					map.put("topic_status", o.getStatus());
					rl.add(map);
				}
				return rl;
			}
		});
		return page;
	}
	/**
	 * 
	 * @param belong:parent nid
	 * @param nid:child nid
	 */
	public Serializable saveTopics(MainNews belong,MainNews nid)
	{
		Topics topic=new Topics();
		topic.setMainNews(nid);
		topic.setPnid(belong.getNid());
		return save(topic);
	}
	/**
	 * 删除与这条新闻绑定的所有子新闻
	 * @param belong
	 * @return
	 */
	public int unBindTopics(MainNews belong)
	{
		Session session =getSession();
		Query query=session.createQuery("delete from Topics where pnid=:pnid");
		query.setString("pnid", belong.getNid());
		return query.executeUpdate();
	}
}
