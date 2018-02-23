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
import org.hibernate.transform.Transformers;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.com.ql.wiseBeijing.dec.dao.DecNewsDao;
import cn.com.ql.wiseBeijing.dec.daoBean.DecMainNews;
import cn.com.ql.wiseBeijing.imageService.dao.ImagesDao;
import cn.com.ql.wiseBeijing.imageService.daoBean.Images;
import cn.com.ql.wiseBeijing.news.daoBean.CardNews;
import cn.com.ql.wiseBeijing.news.daoBean.MainNews;
import cn.com.ql.wiseBeijing.policy.dao.PolicyItemDao;
import cn.com.ql.wiseBeijing.policy.daoBean.PolicyItem;
import cn.com.ql.wiseBeijing.serviceUtil.NewsPlatform;
import cn.xxtui.support.page.Page;
import cn.xxtui.support.page.PageWaterfallAdapter;
import cn.xxtui.support.page.ResultTransfer;
import cn.xxtui.support.util.DateUtil;

@Component
public class NewsDao extends BasicDao<MainNews> {

	@Bean
	public NewsDao newsDao() {
		return new NewsDao();
	}

	@Resource(name = "decNewsDao")
	private DecNewsDao decNewsDao;
	@Resource(name = "policyItemDao")
	private PolicyItemDao policyItemDao;
	public List<Map<String, Object>> getRecNews(MainNews news,int amount){
		final Session session = getSession();
		String splitKeywords[] = news.getKeywords().split(",");
		List<Map<String, Object>> mapList = new ArrayList<>();
		String keywords ="(";
		//第一推荐  关键字推荐
		for(String key:splitKeywords){
			keywords+=" keywords like '%"+key+"%' or";
		}
		if(!keywords.equals("(")){
			keywords = keywords.substring(0,keywords.length()-2);
		}
		keywords = keywords+")";
		//第二推荐  新闻来源推荐
//	    String sources = "1=1";
//	    if(news.getSources()!=null){
//	    	sources = " sources = '"+news.getSources()+"'";
//	    }
    	String hql = "select * from MainNews where "+keywords+" and nid !='"+news.getNid()+"' and status = 0  order by createtime desc limit 0,"+amount;
    	Query query2  = session.createSQLQuery(hql).setResultTransformer(
			     Transformers.ALIAS_TO_ENTITY_MAP);
    	mapList = query2.list();
    	for(Map m:mapList){
     		Images large = imagesDao
					.get(Images.class,m.get("largeimage")+"");
			Images list1 = imagesDao.get(Images.class,m.get("listimage")+"");
			Images small = imagesDao
					.get(Images.class,m.get("smallimage")+"");
			if (large != null)
				m.put("iso_largeimage", large.getUrl());
			else
				m.put("iso_largeimage", "");
			if (list1 != null)
				m.put("iso_listimage", list1.getUrl());
			else
				m.put("iso_listimage", "");
			if (small != null)
				m.put("iso_smallimage", small.getUrl());
			else
				m.put("iso_smallimage", "");
    	}
    	for(Map<String, Object> m : mapList){
			m.put("isSystem", true);
		}
    	if(mapList.size()==4){
    		return mapList;
    	}
    	int len = amount - mapList.size();
    	if(len==0)return mapList;
    	String aleradyNid="nid not in(";
    	for(Map<String, Object> map:mapList){
    		aleradyNid +=map.get("nid")+",";
    	}
    	if(!aleradyNid.contains(",")){
    		aleradyNid = "1=1";
    	}else{
    		aleradyNid = aleradyNid.substring(0,aleradyNid.length()-1)+")";
    	}
    	String shql = "select * from MainNews where "+aleradyNid+" and status = 0 order by createtime desc limit 0,"+len;
    	Query query3  = session.createSQLQuery(shql).setResultTransformer(
			     Transformers.ALIAS_TO_ENTITY_MAP);
    	mapList.addAll(query3.list());
    	for(Map<String, Object> m : mapList){
			m.put("isSystem", true);
		}
    	for(Map m:mapList){
     		Images large = imagesDao
					.get(Images.class,m.get("largeimage")+"");
			Images list1 = imagesDao.get(Images.class,m.get("listimage")+"");
			Images small = imagesDao
					.get(Images.class,m.get("smallimage")+"");
			if (large != null)
				m.put("iso_largeimage", large.getUrl());
			else
				m.put("iso_largeimage", "");
			if (list1 != null)
				m.put("iso_listimage", list1.getUrl());
			else
				m.put("iso_listimage", "");
			if (small != null)
				m.put("iso_smallimage", small.getUrl());
			else
				m.put("iso_smallimage", "");
    	}
		return mapList;
	}
	public static void main(String[] args) {
	}
	/**
	 * 根据不同平台获取不同的新闻或内容
	 * 
	 * @param newsid
	 * @param platform
	 * @return
	 */
	public Map getNews(String newsid, String platform) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("platform", platform);
		if (platform.equals(NewsPlatform.NEWS.getName())) {
			MainNews mainNews = get(MainNews.class, newsid);
			map.put("title", mainNews.getTitle());
			map.put("nid", mainNews.getNid());
			map.put("type", mainNews.getType());
			map.put("urls", mainNews.getUrls());
			Images large = imagesDao
					.get(Images.class, mainNews.getLargeimage());
			Images list = imagesDao.get(Images.class, mainNews.getListimage());
			Images small = imagesDao
					.get(Images.class, mainNews.getSmallimage());
			if (large != null)
				map.put("iso_largeimage", large.getUrl());
			else
				map.put("iso_largeimage", "");
			if (list != null)
				map.put("iso_listimage", list.getUrl());
			else
				map.put("iso_listimage", "");
			if (small != null)
				map.put("iso_smallimage", small.getUrl());
			else
				map.put("iso_smallimage", "");
			return map;
		}
		if (platform.equals(NewsPlatform.DEC.getName())) {
			DecMainNews mainNews = decNewsDao.get(DecMainNews.class, newsid);
			map.put("title", mainNews.getTitle());
			map.put("nid", mainNews.getNid());
			map.put("type", mainNews.getType());
			Images large = imagesDao
					.get(Images.class, mainNews.getLargeimage());
			Images list = imagesDao.get(Images.class, mainNews.getListimage());
			Images small = imagesDao
					.get(Images.class, mainNews.getSmallimage());
			map.put("urls", mainNews.getUrls());
			if (large != null)
				map.put("iso_largeimage", large.getUrl());
			else
				map.put("iso_largeimage", "");
			if (list != null)
				map.put("iso_listimage", list.getUrl());
			else
				map.put("iso_listimage", "");
			if (small != null)
				map.put("iso_smallimage", small.getUrl());
			else
				map.put("iso_smallimage", "");
			return map;
		}
		if (platform.equals(NewsPlatform.POLICY.getName())) {
			PolicyItem mainNews = policyItemDao.get(PolicyItem.class, newsid);
			map.put("title", mainNews.getTitle());
			map.put("nid", mainNews.getNid());
			map.put("type", mainNews.getType());
			Images large = imagesDao
					.get(Images.class, mainNews.getLargeimage());
			Images list = imagesDao.get(Images.class, mainNews.getListimage());
			Images small = imagesDao
					.get(Images.class, mainNews.getSmallimage());
			map.put("urls", mainNews.getUrls());
			if (large != null)
				map.put("iso_largeimage", large.getUrl());
			else
				map.put("iso_largeimage", "");
			if (list != null)
				map.put("iso_listimage", list.getUrl());
			else
				map.put("iso_listimage", "");
			if (small != null)
				map.put("iso_smallimage", small.getUrl());
			else
				map.put("iso_smallimage", "");
			return map;
		}
		return map;
	}

	@Resource(name = "imagesDao")
	private ImagesDao imagesDao;

	/**
	 * 根据title与关键字词进行搜索
	 * 
	 * @param keywords
	 * @param current
	 * @param amount
	 * @return
	 */
	public Page<Map> search(String keywords, int current, int amount) {
		final Session session = getSession();
		String hql = "select main.nid,main.title,main.description,main.keywords,main.pubdate,main.listimage,main.largeimage,main.smallimage,main.template,main.type,main.editor,main.urls,main.status,main.createtime from MainNews as main where main.title like '%"
				+ keywords + "%' or main.keywords like '%" + keywords + "%' order by main.createtime desc";
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
							map.put("ios_listimage",
									getImage(String.valueOf(o[5])));
							map.put("listimage", o[5]);
							map.put("ios_largeimage",
									getImage(String.valueOf(o[6])));
							map.put("largeimage", o[6]);
							map.put("ios_smallimage",
									getImage(String.valueOf(o[7])));
							map.put("smallimage", o[7]);
							map.put("template", o[8]);
							map.put("type", o[9]);
							map.put("editor", o[10]);
							map.put("urls", o[11]);
							map.put("status", o[12]);
							map.put("createtime", o[13]);
							rcl.add(map);
						}
						return rcl;
					}
				});
		return page;
	}

	/**
	 * 根据天进行匹配样式2015-07-09
	 * 
	 * @param keywords
	 * @param current
	 * @param amount
	 * @return
	 */
	public Page<Map> searchDate(String date, int current, int amount) {
		final Session session = getSession();
		String hql = "select main.nid,main.title,main.description,main.keywords,main.pubdate,main.listimage,main.largeimage,main.smallimage,main.template,main.type,main.editor,main.urls,main.status,main.createtime from MainNews as main where main.pubdate like '"
				+ "%" + date + "%' order by main.createtime desc";
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
							map.put("ios_listimage",
									getImage(String.valueOf(o[5])));
							map.put("listimage", o[5]);
							map.put("ios_largeimage",
									getImage(String.valueOf(o[6])));
							map.put("largeimage", o[6]);
							map.put("ios_smallimage",
									getImage(String.valueOf(o[7])));
							map.put("smallimage", o[7]);
							map.put("template", o[8]);
							map.put("type", o[9]);
							map.put("editor", o[10]);
							map.put("urls", o[11]);
							map.put("status", o[12]);
							map.put("createtime", o[13]);
							rcl.add(map);
						}
						return rcl;
					}
				});
		return page;
	}

	/**
	 * 目前这种方式是极其低效的，要用数据存储过程或其它
	 * 
	 * @param date
	 * @param amount
	 * @return
	 */
	public Page<Map> newsList(String date, int amount) {
		final Session session = getSession();
		String cardHql = "from CardNews where pubdate like '" + "%" + date
				+ "%' and status='0' order by sort asc,id desc";
		Query cardQuery = session.createQuery(cardHql);
		return getCards(amount, session, cardQuery);
	}

	/**根据newsid进行搜索
	 * @param date
	 * @param amount
	 * @return
	 */
	public Page<Map> searchCardByNewsID(String newsarray, int amount) {
		final Session session = getSession();
		String cardHql = "from CardNews where newsarray like '" + "%" + newsarray
				+ "%' and status='0' order by pubdate desc, sort asc";
		Query cardQuery = session.createQuery(cardHql);
		return getCards(amount, session, cardQuery);
	}
	
	
	private Page<Map> getCards(int amount, final Session session,
			Query cardQuery) {
		PageWaterfallAdapter cardAdapter = new PageWaterfallAdapter(cardQuery);
		Page<Map> cardPage = cardAdapter.execute(0, amount - 1,
				new ResultTransfer<Map>() {
					@Override
					public List<Map> transfer(List list) {
						Iterator<CardNews> it = list.iterator();
						List<Map> rl = new ArrayList<Map>();
						while (it.hasNext()) {
							String hql = "select main.nid,main.title,main.description,main.keywords,main.pubdate,main.listimage,main.largeimage,main.smallimage,main.template,main.type,main.editor,main.urls,main.status,main.createtime from MainNews as main where main.nid=:nid";
							CardNews cn = it.next();
							String template = cn.getTemplate();
							String[] newsArray = cn.getNewsarray().split(",");
							Map<String, Object> rmap = new HashMap<String, Object>();
							List<Map> rcl = new ArrayList<Map>();
							for (String nid : newsArray) {
								Query newsQuery = session.createQuery(hql);
								newsQuery.setString("nid", nid.trim());
								Object[] o = (Object[]) newsQuery
										.uniqueResult();
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("nid", o[0]);
								map.put("title", o[1]);
								map.put("description", o[2]);
								map.put("keywords", o[3]);
								map.put("pubdate", o[4]);
								map.put("ios_listimage",
										getImage(String.valueOf(o[5])));
								map.put("listimage", o[5]);
								map.put("ios_largeimage",
										getImage(String.valueOf(o[6])));
								map.put("largeimage", o[6]);
								map.put("ios_smallimage",
										getImage(String.valueOf(o[7])));
								map.put("smallimage", o[7]);
								map.put("template", o[8]);
								map.put("type", o[9]);
								map.put("editor", o[10]);
								map.put("urls", o[11]);
								map.put("status", o[12]);
								map.put("createtime", o[13]);
								rcl.add(map);
							}
							rmap.put("template", template);
							rmap.put("cardID", cn.getId());
							rmap.put("news", rcl);
							rmap.put("pubdate", cn.getPubdate()==null||cn.getPubdate().length()<10?cn.getPubdate():cn.getPubdate().substring(0, 10));
							rl.add(rmap);
						}
						return rl;
					}

				});
		return cardPage;
	}

	private String getImage(String id) {
		if (id != null) {
			Images image = imagesDao.get(Images.class, id);
			if (image != null)
				return image.getUrl();
			else
				return null;
		} else {
			return null;
		}
	}

	public void getTest(){
		String sql = "select title from menu";
		getSession().createQuery(sql);
	}
	
	public Serializable saveCard(CardNews cardNews) {
		Session session = getSession();
		cardNews.setStatus("0");
		cardNews.setCreatedate(DateUtil.getCurrentDateForSql());
		cardNews.setSort("0");
		return session.save(cardNews);
	}
	
}
