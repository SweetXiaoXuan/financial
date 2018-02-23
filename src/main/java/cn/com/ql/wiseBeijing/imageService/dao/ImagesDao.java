package cn.com.ql.wiseBeijing.imageService.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.com.ql.wiseBeijing.dec.daoBean.DecMainNews;
import cn.com.ql.wiseBeijing.imageService.daoBean.Images;
import cn.com.ql.wiseBeijing.imageService.daoBean.ImagesDecBelong;
import cn.com.ql.wiseBeijing.imageService.daoBean.ImagesNewsBelong;
import cn.com.ql.wiseBeijing.news.daoBean.MainNews;
import cn.xxtui.support.page.Page;
import cn.xxtui.support.page.PageWaterfallAdapter;
import cn.xxtui.support.page.ResultTransfer;

@Component
public class ImagesDao extends BasicDao<Images> {
	public ImagesDao imagesDao() {
		return new ImagesDao();
	}

	@Deprecated
	public Page<Images> getListByNewsID(String belong_id,
			String belong_category, int amount) {
		Session session = getSession();
		Query query = session
				.createQuery("from Images where belong_id=:belong_id and belong_category=:belong_category");
		query.setString("belong_id", belong_id);
		query.setString("belong_category", belong_category);
		PageWaterfallAdapter adapter = new PageWaterfallAdapter(query);
		Page<Images> page = adapter.execute(0, amount - 1,
				new ResultTransfer<Images>() {

					@Override
					public List<Images> transfer(List list) {
						return list;
					}
				});
		return page;
	}

	/**
	 * 每日资讯
	 * 
	 * @param belong_id
	 * @param amount
	 * @return
	 */
	public Page<Map<String, Object>> getListByNewsID(String belong_id,
			int amount) {
		Session session = getSession();
		Query query = session
				.createQuery("from ImagesNewsBelong as ib left join fetch ib.imagesid as image where ib.belong_category='news' and ib.newsid=:newsid order by ib.id asc");
		query.setString("newsid", belong_id);
		PageWaterfallAdapter adapter = new PageWaterfallAdapter(query);
		Page<Map<String, Object>> page = adapter.execute(0, amount - 1,
				new ResultTransfer<Map<String, Object>>() {
					@Override
					public List<Map<String, Object>> transfer(List list) {
						Iterator<ImagesNewsBelong> it = list.iterator();
						List<Map<String, Object>> rl = new ArrayList<Map<String, Object>>();
						while (it.hasNext()) {
							Map<String, Object> map = new HashMap<String, Object>();
							ImagesNewsBelong images = it.next();

							if (images.getImagesid().getPid()
									.equals(images.getNewsid().getLargeimage())
									|| images
											.getImagesid()
											.getPid()
											.equals(images.getNewsid()
													.getListimage())
									|| images
											.getImagesid()
											.getPid()
											.equals(images.getNewsid()
													.getSmallimage()))
								continue;
							map.put("description", images.getImagesid()
									.getDescription());
							map.put("url", images.getImagesid().getUrl());
							map.put("pid", images.getImagesid().getPid());
							rl.add(map);
						}
						return rl;
					}
				});
		return page;
	}

	/**
	 * 民生
	 * 
	 * @param belong_id
	 * @param amount
	 * @return
	 */
	public Page<Map<String, Object>> getListByDecID(String belong_id, int amount) {
		Session session = getSession();
		Query query = session
				.createQuery("from ImagesDecBelong as ib left join fetch ib.imagesid as image where ib.belong_category='dec' and ib.newsid=:newsid order by ib.id asc");
		query.setString("newsid", belong_id);
		PageWaterfallAdapter adapter = new PageWaterfallAdapter(query);
		Page<Map<String, Object>> page = adapter.execute(0, amount - 1,
				new ResultTransfer<Map<String, Object>>() {
					@Override
					public List<Map<String, Object>> transfer(List list) {
						Iterator<ImagesDecBelong> it = list.iterator();
						List<Map<String, Object>> rl = new ArrayList<Map<String, Object>>();
						while (it.hasNext()) {
							Map<String, Object> map = new HashMap<String, Object>();
							ImagesDecBelong images = it.next();
							if (images.getImagesid().getPid()
									.equals(images.getNewsid().getLargeimage())
									|| images
											.getImagesid()
											.getPid()
											.equals(images.getNewsid()
													.getListimage())
									|| images
											.getImagesid()
											.getPid()
											.equals(images.getNewsid()
													.getSmallimage()))
								continue;
							map.put("description", images.getImagesid()
									.getDescription());
							map.put("url", images.getImagesid().getUrl());
							map.put("pid", images.getImagesid().getPid());
							rl.add(map);
						}
						return rl;
					}
				});
		return page;
	}
	public Serializable saveNewsImagesBelong(MainNews belong, Images imageid) {
		Session session = getSession();
		ImagesNewsBelong nebelong = new ImagesNewsBelong();
		nebelong.setImagesid(imageid);
		nebelong.setNewsid(belong);
		return session.save(nebelong);
	}
	public int unbindNewsImagesBelong(MainNews belong, Images imageid) {
		Session session = getSession();
		Query query=session.createQuery("delete from ImagesNewsBelong where imagesid=:imagesid and newsid=:newsid ");
		query.setString("imagesid", imageid.getPid());
		query.setString("newsid", belong.getNid());
		return query.executeUpdate();
	}
	public int unbindNewsImagesBelong(MainNews belong) {
		Session session = getSession();
		StringBuilder sb=new StringBuilder();
		sb.append("(");
		sb.append("'"+belong.getLargeimage()+"',");
		sb.append("'"+belong.getListimage()+"',");
		sb.append("'"+belong.getSmallimage()+"'");
		sb.append(")");
		String sql="delete from ImagesNewsBelong where newsid=:newsid and belong_category='news' and imagesid not in "+sb.toString();
		Query query=session.createQuery(sql);
		query.setString("newsid", belong.getNid());
		return query.executeUpdate();
	}
	/**
	 * 删除的是缩略三张图
	 * @param belong
	 * @return
	 */
	public int unbindIndexNewsImagesBelong(MainNews belong) {
		Session session = getSession();
		StringBuilder sb=new StringBuilder();
		sb.append("(");
		sb.append("'"+belong.getLargeimage()+"',");
		sb.append("'"+belong.getListimage()+"',");
		sb.append("'"+belong.getSmallimage()+"'");
		sb.append(")");
		String sql="delete from ImagesNewsBelong where newsid=:newsid and belong_category='news' and imagesid  in "+sb.toString();
		Query query=session.createQuery(sql);
		query.setString("newsid", belong.getNid());
		return query.executeUpdate();
	}

	public int unbindDecNewsImagesBelong(DecMainNews belong) {
		Session session = getSession();
		StringBuilder sb=new StringBuilder();
		sb.append("(");
		sb.append("'"+belong.getLargeimage()+"',");
		sb.append("'"+belong.getListimage()+"',");
		sb.append("'"+belong.getSmallimage()+"'");
		sb.append(")");
		Query query=session.createQuery("delete from ImagesDecBelong where newsid=:newsid and belong_category='dec' and imagesid not in "+sb.toString());
		query.setString("newsid", belong.getNid());
		return query.executeUpdate();
	}
	/**
	 * 删除的是缩略三张图
	 * @param belong
	 * @return
	 */
	public int unbindIndexDecNewsImagesBelong(DecMainNews belong) {
		Session session = getSession();
		StringBuilder sb=new StringBuilder();
		sb.append("(");
		sb.append("'"+belong.getLargeimage()+"',");
		sb.append("'"+belong.getListimage()+"',");
		sb.append("'"+belong.getSmallimage()+"'");
		sb.append(")");
		Query query=session.createQuery("delete from ImagesDecBelong where newsid=:newsid and belong_category='dec' and imagesid  in "+sb.toString());
		query.setString("newsid", belong.getNid());
		return query.executeUpdate();
	}
	
	
	public int unbindDecNewsImagesBelong(DecMainNews belong, Images imageid) {
		Session session = getSession();
		Query query=session.createQuery("delete from ImagesDecBelong where imagesid=:imagesid and newsid=:newsid ");
		query.setString("imagesid", imageid.getPid());
		query.setString("newsid", belong.getNid());
		return query.executeUpdate();
	}
	
	
	public Serializable saveDecImagesBelong(DecMainNews belong, Images imageid) {
		Session session = getSession();
		ImagesDecBelong nebelong = new ImagesDecBelong();
		nebelong.setImagesid(imageid);
		nebelong.setNewsid(belong);
		return session.save(nebelong);
	}
}
