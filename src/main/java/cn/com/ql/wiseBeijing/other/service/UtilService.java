package cn.com.ql.wiseBeijing.other.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.com.ql.wiseBeijing.dec.dao.DecNewsDao;
import cn.com.ql.wiseBeijing.news.dao.NewsDao;
import cn.com.ql.wiseBeijing.policy.dao.PolicyItemDao;
import cn.com.ql.wiseBeijing.serviceUtil.PageMapUtil;
import cn.com.ql.wiseBeijing.serviceUtil.ReturnValue;
import cn.xxtui.support.page.Page;

@Component
public class UtilService {
	@Bean
	public UtilService utilService() {
		return new UtilService();
	}
	@Resource(name="newsDao")
	private NewsDao newsDao;
	@Resource(name="decNewsDao")
	private DecNewsDao decNewsDao;
	@Resource(name="policyItemDao")
	private PolicyItemDao policyItemDao;
	@Transactional
	public ReturnValue<Map> search(String keywords)
	{
		Page<Map> map_news=newsDao.search(keywords, 0, 3);
		Page<Map> map_dec=decNewsDao.search(keywords, 0, 3);
		Page<Map> map_policy=policyItemDao.search(keywords, 0, 3);
		Map map=new HashMap();
		map.put("news", map_news.getPageContent());
		map.put("democracy", map_dec.getPageContent());
		map.put("policy", map_policy.getPageContent());
		ReturnValue<Map> r=new ReturnValue<Map>();
		r.setFlag(ReturnValue.FLAG_SUCCESS);
		r.setObject(map);
		return r;
	}
	@Transactional
	public ReturnValue<Map> searchNews(String keywords,int current,int amount)
	{
		Map<String,Object> map=new HashMap<String,Object>();
		Page<Map> map_news=newsDao.search(keywords, current, amount);
		ReturnValue<Map> r=new ReturnValue<Map>();
		r.setFlag(ReturnValue.FLAG_SUCCESS);
		r.setObject(PageMapUtil.getMap(map_news));
		return r; 
	}
	@Transactional
	public ReturnValue<Map> searchDemocracy(String keywords,int current,int amount)
	{
		Page<Map> map_news=decNewsDao.search(keywords, current, amount);
		ReturnValue<Map> r=new ReturnValue<Map>();
		r.setFlag(ReturnValue.FLAG_SUCCESS);
		r.setObject(PageMapUtil.getMap(map_news));
		return r;
	}
	@Transactional
	public ReturnValue<Map> searchPolicy(String keywords,int current,int amount)
	{
		Page<Map> map_news=policyItemDao.search(keywords, current, amount);
		ReturnValue<Map> r=new ReturnValue<Map>();
		r.setFlag(ReturnValue.FLAG_SUCCESS);
		r.setObject(PageMapUtil.getMap(map_news));
		return r;
	}
}
