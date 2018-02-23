package cn.com.ql.wiseBeijing.news.service;

import cn.com.ql.wiseBeijing.imageService.dao.ImagesDao;
import cn.com.ql.wiseBeijing.imageService.daoBean.Images;
import cn.com.ql.wiseBeijing.imageService.frontBean.FImages;
import cn.com.ql.wiseBeijing.news.dao.AdvsDao;
import cn.com.ql.wiseBeijing.news.dao.CollectDao;
import cn.com.ql.wiseBeijing.news.dao.CommentDao;
import cn.com.ql.wiseBeijing.news.dao.FeedbackDao;
import cn.com.ql.wiseBeijing.news.dao.LikesDao;
import cn.com.ql.wiseBeijing.news.dao.NewsDao;
import cn.com.ql.wiseBeijing.news.dao.RecNewsDao;
import cn.com.ql.wiseBeijing.news.dao.TopicsDao;
import cn.com.ql.wiseBeijing.news.dao.WarningDao;
import cn.com.ql.wiseBeijing.news.daoBean.Advs;
import cn.com.ql.wiseBeijing.news.daoBean.CardNews;
import cn.com.ql.wiseBeijing.news.daoBean.Collect;
import cn.com.ql.wiseBeijing.news.daoBean.Comment;
import cn.com.ql.wiseBeijing.news.daoBean.Feedback;
import cn.com.ql.wiseBeijing.news.daoBean.Likes;
import cn.com.ql.wiseBeijing.news.daoBean.MainNews;
import cn.com.ql.wiseBeijing.news.daoBean.Warning;
import cn.com.ql.wiseBeijing.news.frontBean.FAdv;
import cn.com.ql.wiseBeijing.news.frontBean.FFeedback;
import cn.com.ql.wiseBeijing.news.frontBean.FNews;
import cn.com.ql.wiseBeijing.news.util.NewsType;
import cn.com.ql.wiseBeijing.serviceUtil.PageMapUtil;
import cn.com.ql.wiseBeijing.serviceUtil.ReturnValue;
import cn.com.ql.wiseBeijing.user.dao.UserDao;
import cn.lv.jewelry.activity.dao.ActivityPrivilegeDao;
import cn.xxtui.support.bean.ResultList;
import cn.xxtui.support.page.Page;
import cn.xxtui.support.util.DateUtil;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.logging.log4j.LogManager;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
public class NewsService {

	@Resource(name = "newsDao")
	private NewsDao newsDao;
	@Resource(name = "imagesDao")
	private ImagesDao imagesDao;
	@Resource(name = "topicsDao")
	private TopicsDao topicsDao;
	@Resource(name = "commentDao")
	private CommentDao commentDao;
	@Resource(name = "warningDao")
	private WarningDao warningDao;
	@Resource(name = "collectDao")
	private CollectDao collectDao;
	@Resource(name = "likesDao")
	private LikesDao likesDao;
	@Resource(name = "userDao")
	private UserDao userDao;
	@Resource(name = "advsDao")
	private AdvsDao advDao;
	@Resource(name = "feedbackDao")
	private FeedbackDao feedbackDao;
	@Resource(name = "recNewsDao")
	private RecNewsDao recNewsDao;
	@Resource(name = "activityPrivilegeDao")
	private ActivityPrivilegeDao activityPrivilegeDao;

	@Bean
	public NewsService newsService() {
		return new NewsService();
	}

	private org.apache.logging.log4j.Logger log = LogManager
			.getLogger(NewsService.class);

	@Transactional
	public ReturnValue<Map> getNewsList(String date) {
		// 取100条信息
		Page<Map> page = newsDao.newsList(date, 100);
		ReturnValue<Map> rv = new ReturnValue<Map>();
		List<Map> list = page.getPageContent();
		rv.setFlag(ReturnValue.FLAG_SUCCESS);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("date", date);
		if (list == null)
			list = new ArrayList<Map>();
		map.put("data", list);
		rv.setObject(map);
		return rv;
	}

	@Transactional
	public ReturnValue<List<Map>> getNewsListNoCard(String date) {
		// 取100条信息
		Page<Map> page = newsDao.searchDate(date, 0, 100);
		ReturnValue<List<Map>> rv = new ReturnValue<List<Map>>();
		List<Map> list = page.getPageContent();
		rv.setFlag(ReturnValue.FLAG_SUCCESS);
		rv.setObject(list);
		return rv;
	}

	/**
	 * 从开始日期前7天的新闻
	 * 
	 * @param beginDate
	 * @return
	 */
	@Transactional
	public ReturnValue<List<Map>> getNewsSevenDaysList(Date beginDate,
			boolean blankFilter) {
		List<Map> listt = new ArrayList<Map>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(beginDate);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		int max_days=0;
		for (int i = 0; i < 7;) {
			max_days++;
			if(max_days>365)
				break;//从指定天最大往后搜索365天（补全7天，在blankFilter为false时），如果往后365天都没有，那就是一年没更新了，就不存在意义
			Map<String, Object> map = new HashMap<String, Object>();
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			Date d = calendar.getTime();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String date = dateFormat.format(d);
			// 取100条信息
			Page<Map> page = newsDao.newsList(date, 100);
			List<Map> list = page.getPageContent();
			if (list.isEmpty() && !blankFilter)
				continue;
			i++;
			map.put("date", date);
			if (list == null)
				list = new ArrayList<Map>();
			map.put("data", list);
			listt.add(map);
		}
		ReturnValue<List<Map>> rv = new ReturnValue<List<Map>>();
		rv.setFlag(ReturnValue.FLAG_SUCCESS);
		rv.setObject(listt);
		return rv;
	}

	@Transactional
	public ReturnValue<FNews> getNewsByID(String id) {
		FNews fn = new FNews();
		MainNews mn = newsDao.get(MainNews.class, id);
		ReturnValue<FNews> rv = new ReturnValue<FNews>();
		if (mn == null) {
			rv.setFlag(ReturnValue.FLAG_FAIL);
			return rv;
		}
		NewsType type = NewsType.get(mn.getType());
		switch (type) {
		case GROUP_IMAGES:
			// Page<Images> pageImage = imagesDao.getListByNewsID(id, "news",
			// 100);
			Page<Map<String, Object>> pageImage = imagesDao.getListByNewsID(id,
					100);
			fn.setAdditionalList(pageImage.getPageContent());
			getOthers(id, fn,mn);
			break;
		case TOPIC:
			Page<Map> pageTopic = topicsDao.getTopicsByParentID(id, 100);
			fn.setAdditionalList(pageTopic.getPageContent());
			break;
		default:
			getOthers(id, fn,mn);
			break;
		}
		fn.setComment_flag(mn.getComment_flag());
		fn.setContent(mn.getContent());
		fn.setCreatetime(mn.getCreatetime());
		fn.setCreator(mn.getCreator());
		fn.setDescription(mn.getDescription());
		fn.setEditor(mn.getEditor());
		fn.setReporter(mn.getReporter());
		fn.setKeywords(mn.getKeywords());
		fn.setLargeimage(mn.getLargeimage());
		fn.setListimage(mn.getListimage());
		fn.setSmallimage(mn.getSmallimage());
		fn.setNid(mn.getNid());
		fn.setPubdate(mn.getPubdate());
		fn.setShare_flag(mn.getShare_flag());
		fn.setTitle(mn.getTitle());
		fn.setType(mn.getType());
		fn.setSources(mn.getSources());
		fn.setUrls(mn.getUrls());
		rv.setObject(fn);
		rv.setFlag(ReturnValue.FLAG_SUCCESS);
		return rv;
	}

	private void getOthers(String id, FNews fn,MainNews mn) {
		Page<Map> adv = advDao.getAdvByParentID(id, 100, "news");
		Map<String, Object> map = new HashMap<String, Object>();
		ResultList<Map> list = new ResultList<Map>();
	
		list.setBody(adv.getPageContent());
		map.put("adv", list.toInnerString().toString());
		Page<Map> recMap=recNewsDao.getRecNewsByPID(id, 100, "news");
		for(Map<String, Object> m : recMap.getPageContent()){
			m.put("isSystem", false);
		}
		if(mn.getType().equals("1")&&recMap.getPageContent().size()<4){
			recMap.getPageContent().addAll(newsDao.getRecNews(mn, 4-recMap.getPageContent().size()));
		}else if(!mn.getType().equals("4")&&recMap.getPageContent().size()<3){
			recMap.getPageContent().addAll(newsDao.getRecNews(mn, 3-recMap.getPageContent().size()));
		}
	 	for (Map<String, Object> temp :recMap.getPageContent()) {
	 		temp.put("content", "");
		}
	 	
	 	list.setBody(recMap.getPageContent());
		map.put("recNews", list.toInnerString().toString());
		fn.setAddtionalMap(map);
	}

	@Transactional(rollbackFor = Exception.class)
	public ReturnValue<FNews> saveMainNews(FNews news) {
		MainNews mn = new MainNews();
		mn.setContent(news.getContent());
		mn.setComment_flag(news.getComment_flag());
		mn.setCreatetime(news.getCreatetime());
		mn.setCreator(news.getCreator());
		mn.setDescription(news.getDescription());
		mn.setEditor(news.getEditor());
		mn.setKeywords(news.getKeywords());
		mn.setLargeimage(news.getLargeimage());
		mn.setListimage(news.getListimage());
		mn.setPubdate(news.getPubdate());
		mn.setPubstatus(news.getPubstatus());
		mn.setReporter(news.getReporter());
		mn.setShare_flag(news.getShare_flag());
		mn.setSmallimage(news.getSmallimage());
		mn.setSort_flag(news.getSort_flag());
		mn.setSources(news.getSources());
		mn.setStatus(news.getStatus());
		mn.setTitle(news.getTitle());
		mn.setType(news.getType());
		mn.setUrls(news.getUrls());
		ReturnValue<FNews> rv = new ReturnValue<FNews>();
		String nid = newsDao.save(mn).toString();
		ReturnValue r1 = this.saveImagesBelongNews(nid, news.getLargeimage());
		ReturnValue r2 = this.saveImagesBelongNews(nid, news.getListimage());
		ReturnValue r3 = this.saveImagesBelongNews(nid, news.getSmallimage());
		if (r1.getFlag() == ReturnValue.FLAG_FAIL
				|| r2.getFlag() == ReturnValue.FLAG_FAIL
				|| r3.getFlag() == ReturnValue.FLAG_FAIL) {
			if (log.isInfoEnabled()) {
				log.info("---------------transaction rollback----------"
						+ news.getLargeimage() + ":" + news.getListimage()
						+ ":" + news.getSmallimage());
			}
			
		}
		String advss = news.getAdv();
		if (advss != null && !advss.trim().equals("")) {
			String[] advs = advss.split(",");
			for (String adv : advs) {
				int ad = Integer.parseInt(adv);
				Advs a = advDao.get(Advs.class, ad);
				if (a == null) {
					throw new RuntimeException("transaction rollback"
							+ "adverise id " + adv + " not exist");
				} else {
					advDao.saveAdvBind(mn, a);
				}
			}
		}
		/**
		 * 保存推荐新闻
		 */
		String recNews = news.getRecNews();
		if (recNews != null && !recNews.trim().equals("")) {
			String[] recNewss = recNews.split(",");
			for (String rec : recNewss) {
				MainNews a = newsDao.get(MainNews.class, rec);
				if (a == null) {
					throw new RuntimeException("transaction rollback"
							+ "rec news id " + rec + " not exist");
				} else {
					recNewsDao.saveRecNews(mn.getNid(), a.getNid(), "news");
				}
			}
		}
		news.setNid(nid);
		news.setContent("");
		rv.setFlag(ReturnValue.FLAG_SUCCESS);
		rv.setObject(news);
		return rv;
	}

	@Transactional
	public ReturnValue<Map> getLaunchAdv() {
		ReturnValue<Map> rv = new ReturnValue<Map>();
		Page<Map> page = advDao.getAdvLaunch();
		List<Map> list = page.getPageContent();
		Map<String, String> map = new HashMap<String, String>();
		if (list.isEmpty()) {
			map.put("flag", "0");
		} else {
			map = list.get(0);
			map.put("flag", "1");
		}
		rv.setObject(map);
		rv.setFlag(ReturnValue.FLAG_SUCCESS);
		return rv;
	}

	@Transactional
	public ReturnValue<Advs> getAdv(int id) {
		ReturnValue<Advs> rv = new ReturnValue<Advs>();
		Advs adv = advDao.get(Advs.class, id);
		if (adv != null) {
			rv.setObject(adv);
			rv.setFlag(ReturnValue.FLAG_SUCCESS);
		} else {
			rv.setMeg("adv is not exist");
			rv.setFlag(ReturnValue.FLAG_FAIL);
		}
		return rv;
	}

	@Transactional
	public ReturnValue<FAdv> saveAdv(FAdv fadv) throws Exception {
		ReturnValue<FAdv> rv = new ReturnValue<FAdv>();
		Advs ad = new Advs();
		ad.setBeginTime(fadv.getBeginTime());
		ad.setContent(fadv.getContent());
		ad.setCreateTime(DateUtil.getCurrentDateForSql());
		ad.setEndTime(fadv.getEndTime());
		ad.setLargeimage(fadv.getLargeimage());
		ad.setStatus("0");
		ad.setType(fadv.getType());
		if (fadv.getSort() != null)
			ad.setSort(fadv.getSort());
		else
			ad.setSort("0");
		ad.setType(fadv.getType());
		ad.setTitle(fadv.getTitle());
		if (fadv.getTarget() == null)
			ad.setTarget("0");
		else
			ad.setTarget(fadv.getTarget());
		try {
			advDao.save(ad);
			fadv.setId(ad.getId());
			rv.setFlag(ReturnValue.FLAG_SUCCESS);
			rv.setObject(fadv);
			return rv;
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}

	}

	@Transactional
	public ReturnValue<FImages> saveNewsImages(String url, String description) {
		Images images = new Images();
		images.setBelong_category("news");
		images.setBelong_id("0");
		images.setStatus(0);
		images.setUploadtime(DateUtil.getCurrentDateForSql());
		images.setUrl(url);
		images.setDescription(description);
		imagesDao.save(images);
		FImages fImages = new FImages();
		fImages.setDescription(images.getDescription());
		fImages.setPid(images.getPid());
		fImages.setUrl(images.getUrl());
		ReturnValue<FImages> returnValue = new ReturnValue<FImages>();
		returnValue.setFlag(ReturnValue.FLAG_SUCCESS);
		returnValue.setObject(fImages);
		return returnValue;
	}

	/**
	 * 组图或图文
	 * 
	 * @param belong
	 * @param imagesid
	 *            图片id
	 * @return
	 */
	@Transactional
	public ReturnValue<Map<String, String>> saveImagesBelongNews(String belong,
			String imagesid) {
		ReturnValue<Map<String, String>> r = new ReturnValue<Map<String, String>>();
		MainNews mainNews = (MainNews) newsDao.get(MainNews.class, belong);
		Images images = imagesDao.get(Images.class, imagesid);
		if (mainNews != null && images != null) {
			r.setFlag(ReturnValue.FLAG_SUCCESS);
			imagesDao.saveNewsImagesBelong(mainNews, images);
		} else {
			r.setFlag(ReturnValue.FLAG_FAIL);
		}
		return r;
	}

	/**
	 * 更新组图(批量删除之前的，添加新的)
	 * 
	 * @param belong
	 * @param imagesid
	 *            图片id
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public ReturnValue<List<Map<String, String>>> updateImagesBelongNews(
			String belong, String[] imagesid) {
		ReturnValue<List<Map<String, String>>> r = new ReturnValue<List<Map<String, String>>>();
		MainNews mainNews = (MainNews) newsDao.get(MainNews.class, belong);
		imagesDao.unbindNewsImagesBelong(mainNews);// 先删除
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (String pid : imagesid) {
			Images images = imagesDao.get(Images.class, pid);
			if (mainNews != null && images != null) {
				imagesDao.saveNewsImagesBelong(mainNews, images);
				Map<String, String> map = new HashMap<String, String>();
				map.put("pid", pid);
				map.put("status", "ok");
				list.add(map);
			} else {
				throw new RuntimeException("save fail,please check your pids");
			}
		}
		r.setFlag(ReturnValue.FLAG_SUCCESS);
		r.setObject(list);
		return r;
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public ReturnValue<Map<String, String>> saveImagesBelongNews(String belong,
			FImages imagesid) throws RuntimeException {
		ReturnValue<Map<String, String>> r = new ReturnValue<Map<String, String>>();
		MainNews mainNews = (MainNews) newsDao.get(MainNews.class, belong);
		Images img = new Images();
		img.setBelong_category("news");
		img.setBelong_id(belong);
		img.setDescription(imagesid.getDescription());
		img.setUrl(imagesid.getUrl());
		try {
			imagesDao.save(img);
			if (mainNews != null && img != null) {
				r.setFlag(ReturnValue.FLAG_SUCCESS);
				String id = imagesDao.saveNewsImagesBelong(mainNews, img)
						.toString();
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", id);
				r.setObject(map);
			} else {
				r.setFlag(ReturnValue.FLAG_FAIL);
			}
			return r;
		} catch (Exception e) {
			throw new RuntimeException("pic save failed");
		}

	}

	@Transactional
	public ReturnValue<List<Map<String, String>>> saveTopicChild(String belong,
			String[] ids) {
		ReturnValue<List<Map<String, String>>> r = new ReturnValue<List<Map<String, String>>>();
		MainNews pmain = newsDao.get(MainNews.class, belong);
		if (pmain == null) {
			r.setFlag(ReturnValue.FLAG_FAIL);
		} else {
			List<Map<String, String>> re = new ArrayList<Map<String, String>>();
			int i = 0;

			for (String id : ids) {
				Map<String, String> result = new HashMap<String, String>();
				re.add(result);
				MainNews cmain = newsDao.get(MainNews.class, id);
				if (cmain != null) {
					result.put("id", id);
					result.put("status", "success");
					i++;
					String tid = topicsDao.saveTopics(pmain, cmain).toString();
				} else {
					result.put("id", id);
					result.put("status", "fail");
				}
				r.setFlag(ReturnValue.FLAG_SUCCESS);

				r.setObject(re);
			}
		}
		return r;
	}

	@Transactional
	public ReturnValue<List<Map<String, String>>> updateTopicChild(
			String belong, String[] ids) {
		ReturnValue<List<Map<String, String>>> r = new ReturnValue<List<Map<String, String>>>();
		MainNews pmain = newsDao.get(MainNews.class, belong);
		if (pmain == null) {
			r.setFlag(ReturnValue.FLAG_FAIL);
		} else {
			topicsDao.unBindTopics(pmain);// 删除之前所有的
			List<Map<String, String>> re = new ArrayList<Map<String, String>>();
			int i = 0;
			for (String id : ids) {
				Map<String, String> result = new HashMap<String, String>();
				re.add(result);
				MainNews cmain = newsDao.get(MainNews.class, id);
				if (cmain != null) {
					result.put("id", id);
					result.put("status", "success");
					i++;
					String tid = topicsDao.saveTopics(pmain, cmain).toString();
				} else {
					result.put("id", id);
					result.put("status", "fail");
				}
				r.setFlag(ReturnValue.FLAG_SUCCESS);

				r.setObject(re);
			}
		}
		return r;
	}

	/**
	 * 保存评论
	 * 
	 * @param comment
	 * @param newsid
	 * @param comment_user
	 *            ,这个userid带有平台标识，00,01,02,03,04
	 * @return
	 */
	@Transactional
	public ReturnValue<String> saveComment(String comment, String newsid,
			String comment_user, int conmmentId) {
		MainNews mn = newsDao.get(MainNews.class, newsid);
		Map user = userDao.getUser(comment_user);
		ReturnValue<String> r = new ReturnValue<String>();
		if (mn != null && user != null) {
			r.setFlag(ReturnValue.FLAG_SUCCESS);
			Comment c = new Comment();
			if (comment != null) {
				comment = comment.replace("'", "");
				comment = comment.replace("\n", "");
			}
			c.setComment(comment);
			c.setNewsid(newsid);
			c.setUserid(comment_user);
			c.setCommentid(conmmentId);
			c.setPlatform("news");
			c.setStatus("1");
			c.setCreatetime(DateUtil.getCurrentDateForSql());
			String id = commentDao.save(c).toString();
			r.setObject(id);
		} else {
			r.setFlag(ReturnValue.FLAG_FAIL);
			r.setMeg("there are some problem");
		}
		return r;
	}

	@Transactional
	public ReturnValue<String> saveFeedback(FFeedback f) {
		Map user = userDao.getUser(f.getUserid());
		ReturnValue<String> r = new ReturnValue<String>();
		if (user != null) {
			Feedback fb = new Feedback();
			fb.setComment(f.getComment());
			fb.setCreatetime(DateUtil.getCurrentDateForSql());
			fb.setEmail(f.getEmail());
			fb.setPhone(f.getPhone());
			fb.setStatus("1");
			fb.setUserid(f.getUserid());
			fb.setWorkID(f.getWorkID());
			fb.setType(f.getType());
			fb.setPhone_version(f.getPhone_version());
			fb.setClient_version(f.getClient_version());
			fb.setOs_version(f.getOs_version());
			r.setFlag(ReturnValue.FLAG_SUCCESS);
			String id = feedbackDao.save(fb).toString();
			r.setObject(id);
		} else {
			r.setFlag(ReturnValue.FLAG_FAIL);
			r.setMeg("there are some problem");
		}
		return r;
	}

	/**
	 * 保存举报
	 * 
	 * @param comment
	 * @param newsid
	 * @param comment_user
	 *            ,这个userid带有平台标识，00,01,02,03,04
	 * @return
	 */
	@Transactional
	public ReturnValue<String> saveWarning(String comment, String newsid,
			String comment_user, String type, int commentid) {
		MainNews mn = newsDao.get(MainNews.class, newsid);
		Map user = userDao.getUser(comment_user);
		ReturnValue<String> r = new ReturnValue<String>();
		if (mn != null && user != null) {
			r.setFlag(ReturnValue.FLAG_SUCCESS);
			Warning c = new Warning();
			if (comment != null) {
				comment = comment.replace("'", "");
				comment = comment.replace("\n", "");
			}
			c.setComment(comment);
			c.setNewsid(newsid);
			c.setUserid(comment_user);
			c.setCommentid(0);
			c.setPlatform("news");
			c.setStatus("1");
			c.setType(type);
			c.setCreatetime(DateUtil.getCurrentDateForSql());
			c.setCommentid(commentid);
			String id = warningDao.save(c).toString();
			r.setObject(id);
		} else {
			r.setFlag(ReturnValue.FLAG_SUCCESS);
			r.setMeg("there are some problem");
			r.setObject("there are some problem");
		}
		return r;
	}

	/**
	 * 保存点赞
	 * 
	 * @param commentid
	 * @param userid
	 *            ,这个userid带有平台标识，00,01,02,03,04
	 * @return
	 */
	@Transactional
	public ReturnValue<String> saveLikes(int commentid, String userid) {
		ReturnValue<String> rv = new ReturnValue<String>();
		Comment c = commentDao.get(Comment.class, commentid);
		Map user = userDao.getUser(userid);
		if (c != null && user != null) {
			if (likesDao.isClickLike(c.getId(), userid)) {
				rv.setFlag(ReturnValue.FLAG_SUCCESS);
				rv.setObject("clicked");
				return rv;
			}
			Likes likes = new Likes();
			likes.setCid(Long.parseLong(String.valueOf(c.getId())));
			likes.setCreatetime(DateUtil.getCurrentDateForSql());
			likes.setPlatform("news");
			likes.setStatus("0");
			likes.setPrivilegeId(activityPrivilegeDao.getId(Long.parseLong(userid)));
			String id = likesDao.save(likes).toString();
			rv.setFlag(ReturnValue.FLAG_SUCCESS);
			rv.setObject(id);
		} else {
			rv.setFlag(ReturnValue.FLAG_FAIL);
			rv.setMeg("save failed");
		}
		return rv;
	}

	/**
	 * 保存点赞
	 * 
	 * @param nid
	 * @param userid
	 *            ,这个userid带有平台标识，00,01,02,03,04
	 * @return
	 */
	@Transactional
	public ReturnValue<String> saveCollect(String nid, String userid) {
		ReturnValue<String> rv = new ReturnValue<String>();
		MainNews c = newsDao.get(MainNews.class, nid);
		Map user = userDao.getUser(userid);
		if (c != null && user != null) {
			Collect likes = new Collect();
			likes.setCid(nid);
			likes.setCreatetime(DateUtil.getCurrentDateForSql());
			likes.setPlatform("news");
			likes.setStatus("0");
			likes.setUserid(userid);
			String id = collectDao.save(likes).toString();
			rv.setFlag(ReturnValue.FLAG_SUCCESS);
			rv.setObject(id);
		} else {
			rv.setFlag(ReturnValue.FLAG_FAIL);
			rv.setMeg("save failed");
		}
		return rv;
	}

	@Transactional
	public ReturnValue<List<Map<String, String>>> updateCardSort(
			Integer[] cardIDs) {
		int i = 0;
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		ReturnValue<List<Map<String, String>>> rv = new ReturnValue<List<Map<String, String>>>();
		rv.setObject(list);
		rv.setFlag(ReturnValue.FLAG_SUCCESS);
		for (int cardID : cardIDs) {
			Map<String, String> map = new HashMap<String, String>();
			CardNews card = (CardNews) newsDao.getSession().get(CardNews.class,
					cardID);
			if (card != null) {
				if (!String.valueOf(i).equals(card.getSort())) {
					card.setSort(String.valueOf(i));
					newsDao.getSession().update(card);
				}
				map.put(String.valueOf(cardIDs), String.valueOf(i));
				i++;
			} else {
				map.put(String.valueOf(cardIDs), "not exist");
			}
			list.add(map);
		}
		return rv;
	}

	@Transactional
	public ReturnValue<String> updateCardCancel(String cardID) {
		ReturnValue<String> r = new ReturnValue<String>();
		try {
			int id = Integer.parseInt(cardID);
			return updateCardStatus(id, "1");
		} catch (Exception ex) {
			r.setFlag(ReturnValue.FLAG_FAIL);
			r.setMeg(ex.getMessage());
			return r;
		}
	}

	@Transactional
	public ReturnValue<String> updateNews(String nid, String status) {
		ReturnValue<String> r = new ReturnValue<String>();
		try {
			MainNews mainNews = newsDao.get(MainNews.class, nid);
			if (mainNews == null) {
				r.setFlag(ReturnValue.FLAG_FAIL);
				r.setMeg("news not exist");
				return r;
			} else {
				r.setFlag(ReturnValue.FLAG_SUCCESS);
				newsDao.getSession().update(mainNews);
				r.setObject(nid);
				return r;
			}
		} catch (Exception ex) {
			r.setFlag(ReturnValue.FLAG_FAIL);
			r.setMeg(ex.getMessage());
			return r;
		}
	}

	/**
	 * 更新新闻
	 * 
	 * @param nid
	 * @param news
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public ReturnValue<FNews> updateNews(String nid, FNews news) {
		ReturnValue<FNews> r = new ReturnValue<FNews>();
		try {
			MainNews mn = newsDao.get(MainNews.class, nid);
			if (mn == null) {
				r.setFlag(ReturnValue.FLAG_FAIL);
				r.setMeg("news not exist");
				return r;
			} else {
				mn.setContent(news.getContent());
				// mn.setComment_flag(news.getComment_flag());
				// mn.setCreatetime(news.getCreatetime());
				mn.setCreator(news.getCreator());
				mn.setDescription(news.getDescription());
				mn.setEditor(news.getEditor());
				mn.setKeywords(news.getKeywords());
				mn.setPubdate(news.getPubdate());
				mn.setPubstatus(news.getPubstatus());
				mn.setReporter(news.getReporter());
				mn.setShare_flag(news.getShare_flag());
				mn.setLargeimage(news.getLargeimage());
				mn.setListimage(news.getListimage());
				mn.setSmallimage(news.getSmallimage());
				mn.setSort_flag(news.getSort_flag());
				mn.setSources(news.getSources());
				mn.setStatus(news.getStatus());
				mn.setTitle(news.getTitle());
				mn.setType(news.getType());
				mn.setUrls(news.getUrls());
				r.setFlag(ReturnValue.FLAG_SUCCESS);
				/* 先进行这些图标的删除 */

				/*imagesDao.unbindNewsImagesBelong(mn,
						imagesDao.get(Images.class, mn.getLargeimage()));
				imagesDao.unbindNewsImagesBelong(mn,
						imagesDao.get(Images.class, mn.getListimage()));
				imagesDao.unbindNewsImagesBelong(mn,
						imagesDao.get(Images.class, mn.getSmallimage()));*/

				/* 再删除绑定的广告 */
				/* 先进行这些图标的删除 */
				imagesDao.unbindIndexNewsImagesBelong(mn);
//				/* 再删除绑定的广告 */
				advDao.AdvUnBind(mn);
				recNewsDao.recNewsUnBind(mn.getNid(),"news");
				imagesDao.unbindIndexNewsImagesBelong(mn);
				// /* 再删除绑定的广告 */
				advDao.AdvUnBind(mn);
				//
				recNewsDao.recNewsUnBind(mn.getNid(), "news");
				ReturnValue r1 = this.saveImagesBelongNews(nid,
						news.getLargeimage());
				ReturnValue r2 = this.saveImagesBelongNews(nid,
						news.getListimage());
				ReturnValue r3 = this.saveImagesBelongNews(nid,
						news.getSmallimage());
				if (r1.getFlag() == ReturnValue.FLAG_FAIL
						|| r2.getFlag() == ReturnValue.FLAG_FAIL
						|| r3.getFlag() == ReturnValue.FLAG_FAIL) {
					if (log.isInfoEnabled()) {
						log.info("---------------transaction rollback----------"
								+ news.getLargeimage()
								+ ":"
								+ news.getListimage()
								+ ":"
								+ news.getSmallimage());
					}
					throw new RuntimeException("transaction rollback");
				}
				String advss = news.getAdv();
				if (advss != null && !advss.trim().equals("")) {
					String[] advs = advss.split(",");
					for (String adv : advs) {
						int ad = Integer.parseInt(adv);
						Advs a = advDao.get(Advs.class, ad);
						if (a == null) {
							throw new RuntimeException("transaction rollback"
									+ "adverise id " + adv + " not exist");
						} else {
							advDao.saveAdvBind(mn, a);
						}
					}
				}
				/**
				 * 保存推荐新闻
				 */
				String recNews = news.getRecNews();
				if (recNews != null && !recNews.trim().equals("")) {
					String[] recNewss = recNews.split(",");
					for (String rec : recNewss) {
						MainNews a = newsDao.get(MainNews.class, rec);
						if (a == null) {
							throw new RuntimeException("transaction rollback"
									+ "rec news id " + rec + " not exist");
						} else {
							recNewsDao.saveRecNews(mn.getNid(), a.getNid(),
									"news");
						}
					}
				}
				news.setNid(nid);
				news.setContent("");
				newsDao.getSession().update(mn);
				r.setFlag(ReturnValue.FLAG_SUCCESS);
				r.setObject(news);
				return r;
			}
		} catch (Exception ex) {
			r.setFlag(ReturnValue.FLAG_FAIL);
			r.setMeg(ex.getMessage());
			return r;
		}
	}

	/**
	 * 更新新闻
	 * 
	 * @param nid
	 * @param status
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public ReturnValue<String> updateNewsStatus(String nid, String status) {
		ReturnValue<String> r = new ReturnValue<String>();
		try {
			MainNews mn = newsDao.get(MainNews.class, nid);
			if (mn == null) {
				r.setFlag(ReturnValue.FLAG_FAIL);
				r.setMeg("news not exist");
				return r;
			} else {
				mn.setStatus(status);
				newsDao.getSession().update(mn);
				r.setFlag(ReturnValue.FLAG_SUCCESS);
				r.setObject(mn.getNid());
				return r;
			}
		} catch (Exception ex) {
			r.setFlag(ReturnValue.FLAG_FAIL);
			r.setMeg(ex.getMessage());
			return r;
		}
	}

	@Transactional
	private ReturnValue<String> updateCardStatus(int id, String status) {
		ReturnValue<String> r = new ReturnValue<String>();
		CardNews card = (CardNews) newsDao.getSession().get(CardNews.class, id);
		if (card != null) {
			r.setFlag(ReturnValue.FLAG_SUCCESS);
			card.setStatus(status);
			newsDao.getSession().update(card);
			r.setObject(String.valueOf(card.getId()));
		} else {
			r.setFlag(ReturnValue.FLAG_FAIL);
			r.setMeg("card is not exist");
		}
		return r;
	}

	/**
	 * 
	 * @param news_array
	 * @param template
	 *            0为一个news,1为三个news
	 * @return
	 */
	@Transactional
	public ReturnValue<String> saveCard(String news_array, String template,
			String pubdate) {
		ReturnValue<String> r = new ReturnValue<String>();
		if (!template.equals("0") && !template.equals("1")) {
			r.setFlag(ReturnValue.FLAG_FAIL);
			r.setMeg("template must be 0 or 1");
			return r;
		} else {
			if (template.equals("0")) {
				MainNews main = newsDao.get(MainNews.class, news_array);
				if (main == null) {
					r.setMeg("the news does not exist " + news_array);
					r.setFlag(ReturnValue.FLAG_FAIL);
					return r;
				}
			}
			if (template.equals("1")) {
				String[] arrayNewsId = news_array.split(",");
				if (arrayNewsId.length != 3) {
					r.setMeg("should three news,but it only "
							+ arrayNewsId.length);
					r.setFlag(ReturnValue.FLAG_FAIL);
					return r;
				} else {
					for (String newsid : arrayNewsId) {
						MainNews main = newsDao.get(MainNews.class, newsid);
						if (main != null) {
							continue;
						} else {
							r.setMeg("the news does not exist " + newsid);
							r.setFlag(ReturnValue.FLAG_FAIL);
							return r;
						}
					}
				}
			}
			CardNews cardNews = new CardNews();
			cardNews.setNewsarray(news_array);
			cardNews.setTemplate(template);
			if (pubdate == null)
				cardNews.setPubdate(DateUtil.getCurrentDateForSql());
			else
				cardNews.setPubdate(pubdate);
			String id = newsDao.saveCard(cardNews).toString();
			r.setFlag(ReturnValue.FLAG_SUCCESS);
			r.setObject(id);
			return r;
		}
	}

	/**
	 * 
	 * @param uid
	 * @param newsid
	 * @param current
	 * @return
	 */
	@Transactional
	public ReturnValue<Map> getCommentByNewsId(String uid, String newsid,
			int current) {
		ReturnValue<Map> rv = new ReturnValue<Map>();
		Page<Map> page = commentDao.getCommentByNewsID(uid, newsid, "news",
				current);
		rv.setFlag(ReturnValue.FLAG_SUCCESS);
		rv.setObject(PageMapUtil.getMap(page));
		return rv;
	}

	/**
	 * 获取评论的评论列表
	 * 
	 * @param pcid
	 * @param current
	 * @return
	 */
	@Transactional
	public ReturnValue<Map> getCComments(int pcid, int current) {
		ReturnValue<Map> rv = new ReturnValue<Map>();
		Page<Map> page = commentDao.getCCommentByPCID(pcid, current);
		rv.setFlag(ReturnValue.FLAG_SUCCESS);
		rv.setObject(PageMapUtil.getMap(page));
		return rv;
	}

	@SuppressWarnings("unused")
	@Transactional
	public ReturnValue<Map> getCommentSearch(String uid, String status,
			int current, String keyword) {
		ReturnValue<Map> rv = new ReturnValue<Map>();
		Page<Map> page = null;
		if (uid != null && status != null) {
			page = commentDao.getCommentStatusAndUsername(uid, status, current);
		} else if (keyword != null) {
			page = commentDao.getCommentKeyword(keyword, status, current);
		} else if (status != null) {
			page = commentDao.getCommentStatus(status, current);
		} else if (uid != null) {
			page = commentDao.getCommentByUser(uid, current);
		} else {
			page = commentDao.getCommentAll(current);
		}
		rv.setFlag(ReturnValue.FLAG_SUCCESS);
		rv.setObject(PageMapUtil.getMap(page));
		return rv;
	}

	@Transactional
	public ReturnValue<String> updateCommentStatus(String[] cids, String status) {
		ReturnValue<String> rv = new ReturnValue<String>();
		StringBuilder sb = new StringBuilder();
		for (String cid : cids) {
			int cc = 0;
			try {
				cc = Integer.parseInt(cid);
			} catch (Exception ex) {
				rv.setFlag(ReturnValue.FLAG_FAIL);
				rv.setMeg(cid + " not exist");
				return rv;
			}
			Comment c = commentDao.get(Comment.class, cc);
			if (c == null) {
				rv.setFlag(ReturnValue.FLAG_FAIL);
				rv.setMeg(cid + " not exist");
				return rv;
			} else {
				rv.setFlag(ReturnValue.FLAG_SUCCESS);
				c.setStatus(status);
				commentDao.getSession().update(c);
				sb.append(String.valueOf(c.getId()) + ";");
			}
		}
		rv.setObject(sb.toString());
		return rv;
	}

	@Transactional
	public ReturnValue<Map> getSearchAdv(String title, String status, int p,
			String target) {
		ReturnValue<Map> rv = new ReturnValue<Map>();
		Page<Map> page = null;
		int amount = 20;
		if (status == null)
			page = advDao.getSearchAll(p, amount, target, title);
		else {
			if ("1".equals(status)) {
				page = advDao.getSearchUnBegin(p, amount, target, title);
			} else if ("2".equals(status)) {
				page = advDao.getSearchEnd(p, amount, target, title);
			} else if ("0".equals(status)) {
				page = advDao.getSearchWork(p, amount, target, title);
			} else {
				rv.setFlag(ReturnValue.FLAG_SUCCESS);
				rv.setObject(new HashMap());
				return rv;
			}
		}
		rv.setFlag(ReturnValue.FLAG_SUCCESS);
		rv.setObject(PageMapUtil.getMap(page));
		return rv;
	}

	@Transactional
	public ReturnValue<String> updateAdvs(FAdv fadv, int id) {
		ReturnValue<String> rv = new ReturnValue<String>();
		Advs ad = advDao.get(Advs.class, id);
		ad.setBeginTime(fadv.getBeginTime());
		ad.setContent(fadv.getContent());
		ad.setEndTime(fadv.getEndTime());
		ad.setLargeimage(fadv.getLargeimage());
		ad.setTitle(fadv.getTitle());
		ad.setTarget(fadv.getTarget());
		ad.setType(fadv.getType());
		advDao.getSession().update(ad);
		return rv;
	}

	@Transactional
	public ReturnValue<String> deleteAdv(int id) {
		ReturnValue<String> rv = new ReturnValue<String>();
		rv.setFlag(ReturnValue.FLAG_SUCCESS);
		Advs ad = advDao.get(Advs.class, id);
		ad.setStatus("1");
		advDao.getSession().update(ad);
		rv.setObject("success");
		return rv;
	}

	@Transactional
	public ReturnValue<Map> getNoticeComment(String uid, int current) {
		ReturnValue<Map> rv = new ReturnValue<Map>();
		Page<Map> page = commentDao.getCCommentByNewsID(uid, "news", current);
		rv.setObject(PageMapUtil.getMap(page));
		rv.setFlag(ReturnValue.FLAG_SUCCESS);
		return rv;
	}

	@Transactional
	public ReturnValue<Map> getNoticeLikes(String uid, int current) {
		ReturnValue<Map> rv = new ReturnValue<Map>();
		Page<Map> page = likesDao.getLCommentByNewsID(uid, "news", current);
		rv.setObject(PageMapUtil.getMap(page));
		rv.setFlag(ReturnValue.FLAG_SUCCESS);
		return rv;
	}

	@Transactional
	public ReturnValue<Map> getFeedBacks(int current) {
		ReturnValue<Map> rv = new ReturnValue<Map>();
		Page<Map> page = feedbackDao.search(current, 20);
		rv.setObject(PageMapUtil.getMap(page));
		rv.setFlag(ReturnValue.FLAG_SUCCESS);
		return rv;
	}

	@Transactional
	public ReturnValue<Map> getWarnings(int current) {
		ReturnValue<Map> rv = new ReturnValue<Map>();
		Page<Map> page = warningDao.getWarnings(current, 20);
		rv.setObject(PageMapUtil.getMap(page));
		rv.setFlag(ReturnValue.FLAG_SUCCESS);
		return rv;
	}

	@Transactional
	public ReturnValue<List<Map>> getWarningByID(int id) {
		ReturnValue<List<Map>> rv = new ReturnValue<List<Map>>();
		Page<Map> page = warningDao.getWarningByID(id);
		rv.setObject(page.getPageContent());
		rv.setFlag(ReturnValue.FLAG_SUCCESS);
		return rv;
	}

	@Transactional
	public ReturnValue<Map> getWarningByStatus(String status, int current) {
		ReturnValue<Map> rv = new ReturnValue<Map>();
		Page<Map> page = warningDao.getWarningByStatus(status, current, 20);
		rv.setObject(PageMapUtil.getMap(page));
		rv.setFlag(ReturnValue.FLAG_SUCCESS);
		return rv;
	}

	@Transactional
	public ReturnValue<Map> getFeedbackByWID(String wid) {
		ReturnValue<Map> rv = new ReturnValue<Map>();
		Page<Map> page = feedbackDao.getByWID(wid, 0, 100);
		rv.setObject(PageMapUtil.getMap(page));
		rv.setFlag(ReturnValue.FLAG_SUCCESS);
		return rv;
	}

	@Transactional
	public ReturnValue<Map> getFeedbackByStatus(String status, int current) {
		ReturnValue<Map> rv = new ReturnValue<Map>();
		Page<Map> page = feedbackDao.searchByStatus(status, current, 20);
		rv.setObject(PageMapUtil.getMap(page));
		rv.setFlag(ReturnValue.FLAG_SUCCESS);
		return rv;
	}

	@Transactional
	public ReturnValue<Map> getCardsSearch(String newsid) {
		ReturnValue<Map> rv = new ReturnValue<Map>();
		Page<Map> page = newsDao.searchCardByNewsID(newsid, 100);
		List<Map> list = page.getPageContent();
		Iterator<Map> it = list.iterator();
		Map<String, List<Map>> rm = new LinkedMap();
		for (Map map : list) {
			String pubdate = (String) map.get("pubdate");
			List<Map> l = rm.get(pubdate);
			if (l == null) {
				l = new ArrayList<Map>();
			}
			l.add(map);
			rm.put(pubdate, l);
		}
		// while(it.hasNext())
		// {
		// Map map=it.next();
		// String pubdate=(String)map.get("pubdate");
		// if(!rm.containsKey(pubdate))
		// {
		// List<Map> l=new ArrayList<Map>();
		// l.add(map);
		// rm.put(pubdate, l);
		// }
		// else
		// {
		// List<Map> l=rm.get(pubdate);
		// l.add(map);
		// }
		// }
		Map<String, Object> r = new HashMap<String, Object>();
		r.put("data", rm);
		r.put("hasNext", page.isHasNextPage());
		rv.setObject(r);
		rv.setFlag(ReturnValue.FLAG_SUCCESS);
		return rv;
	}

	/**
	 * 取消收藏
	 * 
	 * @param cid
	 * @param uid
	 * @return
	 */
	@Transactional
	public ReturnValue<String> cancelCollect(int cid, String uid) {
		ReturnValue<String> rv = new ReturnValue<String>();
		Collect collect = collectDao.get(Collect.class, cid);
		if (collect != null && collect.getUserid().equals(uid)) {
			collectDao.getSession().delete(collect);
			rv.setFlag(ReturnValue.FLAG_SUCCESS);
			rv.setObject(String.valueOf(cid));
		} else {
			rv.setFlag(ReturnValue.FLAG_FAIL);
			rv.setMeg("no collect");
		}
		return rv;
	}

	/**
	 * 修改反馈状态
	 * 
	 * @return
	 */
	@Transactional
	public ReturnValue<String> updateFeedBack(String wid, String status) {
		ReturnValue<String> rv = new ReturnValue<String>();
		int i = feedbackDao.updateStatusByWID(wid, status);
		if (i > 0) {
			rv.setFlag(ReturnValue.FLAG_SUCCESS);
			rv.setObject(String.valueOf(i));
		} else {
			rv.setFlag(ReturnValue.FLAG_FAIL);
			rv.setMeg(" update none");
		}
		return rv;
	}

	/**
	 * 修改举报状态
	 * 
	 * @return
	 */
	@Transactional
	public ReturnValue<String> updateWarning(int id, String status) {
		ReturnValue<String> rv = new ReturnValue<String>();
		Warning warning = warningDao.get(Warning.class, id);
		if (warning != null) {
			//冻结评论
			Comment comment = commentDao.get(Comment.class, warning.getCommentid());
			comment.setStatus(status);
			commentDao.getSession().update(comment);
			warning.setStatus(status);
			warningDao.getSession().update(warning);
			rv.setFlag(ReturnValue.FLAG_SUCCESS);
			rv.setObject(String.valueOf(id));
		} else {
			rv.setFlag(ReturnValue.FLAG_FAIL);
			rv.setMeg("no collect");
		}
		return rv;
	}
}
