package cn.com.ql.wiseBeijing.news.frontService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.BeanParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import cn.com.ql.wiseBeijing.imageService.frontBean.FImages;
import cn.com.ql.wiseBeijing.news.daoBean.Advs;
import cn.com.ql.wiseBeijing.news.frontBean.FAdv;
import cn.com.ql.wiseBeijing.news.frontBean.FComment;
import cn.com.ql.wiseBeijing.news.frontBean.FFeedback;
import cn.com.ql.wiseBeijing.news.frontBean.FNews;
import cn.com.ql.wiseBeijing.news.frontBean.FWarning;
import cn.com.ql.wiseBeijing.news.service.NewsService;
import cn.com.ql.wiseBeijing.serviceUtil.ReturnValue;
import cn.com.ql.wiseBeijing.user.frontservice.FUserService;
import cn.xxtui.support.bean.ResultBean;
import cn.xxtui.support.bean.ResultList;
import cn.xxtui.support.session.AccessType;
import cn.xxtui.support.session.ResourceAccess;
import cn.xxtui.support.util.DateUtil;
import cn.xxtui.support.util.ValidateMode;
import cn.xxtui.support.util.XXMediaType;

@Path("/news/")
@Component
public class FNewsService {

	private Logger log = LogManager.getLogger(FNewsService.class);
	@Resource(name = "newsService")
	private NewsService ns;

	@Bean
	public FNewsService fnewsService() {
		return new FNewsService();
	}

	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/pub")
	public Response publicNews(@BeanParam FNews fnews) {
		ResultBean rb = new ResultBean();
		if (ValidateMode.isNull(fnews.getLargeimage(), fnews.getListimage(),
				fnews.getDescription(), fnews.getSmallimage(),
				fnews.getTitle(), fnews.getType(), fnews.getKeywords())) {
			rb.setStatus(ResultBean.ERROR);
			rb.setBody(FUserService.error(
					"please make sure the need fileds are not null").toString());
		} else {
			try {
				fnews.setDescription(fnews.getDescription().replace("\"", "\\\""));
				fnews.setTitle(fnews.getTitle().replace("\"", "\\\""));
				fnews.setKeywords(fnews.getKeywords().replace("\"", "\\\""));
				ReturnValue<FNews> rv = ns.saveMainNews(fnews);
				if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
					rb.setBody(rv.getObject());
					rb.setStatus(ResultBean.OK);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
				rb.setStatus(ResultBean.ERROR);
				rb.setBody(ex.getMessage());
			}
		}
		return Response.ok(rb.toString()).build();
	}



	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/test/abc")
	@ResourceAccess(accessType = AccessType.CMS_AUTHORIZE)
	public Response getTest(@BeanParam FNews fnews,
			@QueryParam("nid") String nid)
	{
		ResultBean bean =new ResultBean();
		
		Map<String,Object> map=new HashMap<String,Object>();
		Map<String,String> mapp=new HashMap<String,String>();
		Map<String,String> mappp=new HashMap<String,String>();
		mappp.put("df", "ddd");
		List<Object> list=new ArrayList<Object>();
		list.add(mappp);
		list.add("abc");
		
		map.put("lixing", "dddd");
		mapp.put("string", "li\"xing");
		mapp.put("string", nid);
		map.put("testts", list);
		
		map.put("test", mapp);
		String obj=JSONObject.toJSONString(map);
		bean.setBody(obj);
		return Response.ok(bean.toString()).build();
	}
	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/update/{nid}")
	@ResourceAccess(accessType = AccessType.CMS_AUTHORIZE)
	public Response updateNews(@BeanParam FNews fnews,
			@PathParam("nid") String nid) {
		ResultBean rb = new ResultBean();
		if (ValidateMode.isNull(fnews.getLargeimage(), fnews.getListimage(),
				fnews.getDescription(), fnews.getSmallimage(),
				fnews.getTitle(), fnews.getType(), fnews.getKeywords())) {
			rb.setStatus(ResultBean.ERROR);
			rb.setBody(FUserService.error(
					"please make sure the need fileds are not null").toString());
		} else {
			try {
				fnews.setDescription(fnews.getDescription().replace("\"", ""));
				fnews.setTitle(fnews.getTitle().replace("\"", ""));
				fnews.setKeywords(fnews.getKeywords().replace("\"", ""));
				ReturnValue<FNews> rv = ns.updateNews(nid, fnews);
				if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
					rb.setBody(rv.getObject());
					rb.setStatus(ResultBean.OK);
				} else {
					rb.setBody(rv.getMeg());
					rb.setStatus(ResultBean.ERROR);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				rb.setStatus(ResultBean.ERROR);
				rb.setBody(ex.getMessage());
			}
		}
		return Response.ok(rb.toString()).build();
	}

	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/updateNewsStatus/{nid}")
	@ResourceAccess(accessType = AccessType.CMS_AUTHORIZE)
	public Response updateNewsStatus(@FormParam("status") String status,
			@PathParam("nid") String nid) {
		ResultBean rb = new ResultBean();
		if (ValidateMode.isNull(status, nid)) {
			rb.setStatus(ResultBean.ERROR);
			rb.setBody(FUserService.error(
					"please make sure the need fileds are not null").toString());
		} else {
			try {

				ReturnValue<String> rv = ns.updateNewsStatus(nid, status);
				if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
					rb.setBody(rv.getObject());
					rb.setStatus(ResultBean.OK);
				}

			} catch (Exception ex) {

				rb.setStatus(ResultBean.ERROR);
				rb.setBody(ex.getMessage());
			}
		}
		return Response.ok(rb.toString()).build();
	}

	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/updateTopics/{belong}")
	@ResourceAccess(accessType = AccessType.CMS_AUTHORIZE)
	public Response updateTopics(@PathParam("belong") String belong,
			@FormParam("nid") String[] nid) {
		if (ValidateMode.isNull(belong) || nid == null) {
			ResultBean rb = new ResultBean();
			rb.setStatus(ResultBean.ERROR);
			rb.setBody(FUserService.error(
					"please make sure the needed fileds are not null")
					.toString());
			return Response.ok(rb.toString()).build();
		} else {
			ReturnValue<List<Map<String, String>>> rv = ns.updateTopicChild(
					belong, nid);
			if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
				ResultList rb = new ResultList();
				rb.setStatus(ResultBean.OK);
				rb.setBody(rv.getObject());
				return Response.ok(rb.toString()).build();
			} else {
				ResultBean rb = new ResultBean();
				rb.setStatus(ResultBean.ERROR);
				rb.setBody("save error");
				return Response.ok(rb.toString()).build();
			}
		}
	}

	/**
	 * 广告一个发布结口就ok了
	 * 
	 * @param fadv
	 * @return
	 */
	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/pubAdv")
	public Response publicAdv(@BeanParam FAdv fadv) {
		ResultBean rb = new ResultBean();
		if (!DateUtil.comepareTimeShort(fadv.getBeginTime(), fadv.getEndTime())) {
			rb.setStatus(ResultBean.ERROR);
			rb.setBody(FUserService.error(
					"what are doing,the endtime is less than begintime")
					.toString());
		} else if (ValidateMode.isNull(fadv.getContent(), fadv.getLargeimage(),
				fadv.getTitle(), fadv.getType(), fadv.getBeginTime(),
				fadv.getEndTime())) {
			rb.setStatus(ResultBean.ERROR);
			rb.setBody(FUserService.error(
					"please make sure the need fileds are not null").toString());
		} else {
			try {
				fadv.setTitle(fadv.getTitle().replace("\"", ""));
				ReturnValue<FAdv> rv = ns.saveAdv(fadv);
				if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
					rb.setBody(rv.getObject());
					rb.setStatus(ResultBean.OK);
				}
			} catch (Exception ex) {
				rb.setStatus(ResultBean.ERROR);
				rb.setBody(ex.getMessage());
			}
		}
		return Response.ok(rb.toString()).build();
	}

	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/adv/launch")
	public Response advLaunch() {
		ResultBean rb = new ResultBean();
		try {
			ReturnValue<Map> rv = ns.getLaunchAdv();
			if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
				rb.setBody(rv.getObject());
				rb.setStatus(ResultBean.OK);
			}
		} catch (Exception ex) {
			rb.setStatus(ResultBean.ERROR);
			rb.setBody("exception");
		}
		return Response.ok(rb.toString()).build();
	}

	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/saveImage")
	@ResourceAccess(accessType = AccessType.CMS_AUTHORIZE)
	public Response saveImages(@BeanParam FImages fimages) {
		ResultBean rb = new ResultBean();
		if (ValidateMode.isNull(fimages.getUrl())) {
			rb.setStatus(ResultBean.ERROR);
			rb.setBody(FUserService.error(
					"please make sure the needed fileds are not null")
					.toString());
		} else {
			rb.setStatus(ResultBean.OK);
			ReturnValue<FImages> rv = ns.saveNewsImages(fimages.getUrl(),
					fimages.getDescription());
			rb.setBody(rv.getObject());
		}
		return Response.ok(rb.toString()).build();
	}

	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/cancelCard/{cardID}")
	@ResourceAccess(accessType = AccessType.CMS_AUTHORIZE)
	public Response cancelCard(@PathParam("cardID") String cardID) {
		ResultBean rb = new ResultBean();
		if (ValidateMode.isNull(cardID)) {
			rb.setStatus(ResultBean.ERROR);
			rb.setBody(FUserService.error(
					"please make sure the needed fileds are not null")
					.toString());
		} else {
			ReturnValue<String> rv = ns.updateCardCancel(cardID);
			if (rv.getFlag() == ReturnValue.FLAG_FAIL) {
				rb.setMsg(rv.getMeg());
				rb.setStatus(ResultBean.ERROR);
			} else {
				rb.setBody(rv.getObject());
				rb.setStatus(ResultBean.OK);
			}
		}
		return Response.ok(rb.toString()).build();
	}

	/**
	 * 更新卡片顺序
	 * 
	 * @param cardID
	 * @return
	 */
	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/updateCardSort")
	@ResourceAccess(accessType = AccessType.CMS_AUTHORIZE)
	public Response cancelCard(@FormParam("sort") String[] cardID) {
		ResultBean rb = new ResultBean();
		if (cardID == null) {
			rb.setStatus(ResultBean.ERROR);
			rb.setBody(FUserService.error(
					"please make sure the needed fileds are not null")
					.toString());
		} else {
			Integer[] ids = new Integer[cardID.length];
			int i = 0;
			try {
				for (String id : cardID) {
					ids[i] = Integer.parseInt(id);
					i++;
				}
				ReturnValue<List<Map<String, String>>> rv = ns
						.updateCardSort(ids);
				if (rv.getFlag() == ReturnValue.FLAG_FAIL) {
					rb.setMsg(rv.getMeg());
					rb.setStatus(ResultBean.ERROR);
				} else {
					Map map = new HashMap();
					map.put("results", rv.getObject());
					rb.setBody(map);
					rb.setStatus(ResultBean.OK);
				}
			} catch (Exception ex) {
				rb.setMsg(ex.getMessage());
				rb.setStatus(ResultBean.ERROR);
			}
		}
		return Response.ok(rb.toString()).build();
	}

	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/saveCard")
	@ResourceAccess(accessType = AccessType.CMS_AUTHORIZE)
	public Response saveCard(@FormParam("news_array") String[] news,
			@FormParam("template") String template,
			@FormParam("pubdate") String pubdate) {
		ResultBean rb = new ResultBean();
		if (ValidateMode.isNull(template, pubdate) || news == null) {
			rb.setStatus(ResultBean.ERROR);
			rb.setBody(FUserService.error(
					"please make sure the needed fileds are not null")
					.toString());
		} else {
			StringBuffer sb = new StringBuffer();
			int i = 0;
			for (String str : news) {
				i++;
				sb.append(str);
				if (i < news.length) {
					sb.append(",");
				}
			}
			ReturnValue<String> r = ns.saveCard(sb.toString(), template,
					pubdate);
			if (r.getFlag() == ReturnValue.FLAG_FAIL) {
				rb.setStatus(ResultBean.ERROR);
				rb.setMsg(r.getMeg());
			} else {
				rb.setStatus(ResultBean.OK);
				rb.setMsg(r.getObject());
			}
		}
		return Response.ok(rb.toString()).build();
	}

	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/saveComment")
	@ResourceAccess(accessType = AccessType.AUTHORIZE)
	public Response saveComment(@BeanParam FComment comment) {
		ResultBean rb = new ResultBean();
		if (ValidateMode.isNull(comment.getComment(), comment.getNewsid(),
				comment.getUserid())) {
			rb.setStatus(ResultBean.ERROR);
			rb.setBody(FUserService.error(
					"please make sure the needed fileds are not null")
					.toString());
		} else {
			rb.setStatus(ResultBean.OK);
			ReturnValue<String> rv = ns.saveComment(comment.getComment(),
					comment.getNewsid(), comment.getUserid(),
					comment.getCommentid());
			rb.setBody(rv.getObject());
		}
		return Response.ok(rb.toString()).build();
	}

	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/saveWarning")
	@ResourceAccess(accessType = AccessType.AUTHORIZE)
	public Response saveWarning(@BeanParam FWarning comment) {
		ResultBean rb = new ResultBean();
		if (ValidateMode.isNull(comment.getComment(), comment.getNewsid(),
				comment.getUserid(), comment.getType())) {
			rb.setStatus(ResultBean.ERROR);
			rb.setBody(FUserService.error(
					"please make sure the needed fileds are not null")
					.toString());
		} else {
			int cid = 0;
			try {
				cid = Integer.parseInt(comment.getCommentid());
			} catch (NumberFormatException ex) {
				rb.setStatus(ResultBean.OK);
				rb.setMsg("error:" + ex.getMessage());
				return Response.ok(rb.toString()).build();
			}
			rb.setStatus(ResultBean.OK);
			ReturnValue<String> rv = ns.saveWarning(comment.getComment(),
					comment.getNewsid(), comment.getUserid(),
					comment.getType(), cid);
			rb.setBody(rv.getObject());
		}
		return Response.ok(rb.toString()).build();
	}

	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/saveCategoryImage/{belong}")
	@ResourceAccess(accessType = AccessType.CMS_AUTHORIZE)
	public Response saveCategoryImages(@PathParam("belong") String belong,
			@BeanParam FImages fimages) {
		ResultBean rb = new ResultBean();
		if (ValidateMode.isNull(belong, fimages.getUrl(),
				fimages.getDescription())) {
			rb.setStatus(ResultBean.ERROR);
			rb.setBody(FUserService.error(
					"please make sure the needed fileds are not null")
					.toString());
		} else {
			try {
				ReturnValue rv = ns.saveImagesBelongNews(belong, fimages);
				if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
					rb.setStatus(ResultBean.OK);
					rb.setBody(rv.getObject());
				} else {
					rb.setStatus(ResultBean.ERROR);
					rb.setBody("save error");
				}
			} catch (RuntimeException e) {
				e.printStackTrace();
				rb.setStatus(ResultBean.ERROR);
				rb.setBody(e.getMessage());
			}

		}
		return Response.ok(rb.toString()).build();
	}

	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/updateCategoryImage/{belong}")
	@ResourceAccess(accessType = AccessType.CMS_AUTHORIZE)
	public Response updateCategoryImages(@PathParam("belong") String belong,
			@FormParam("pid") String[] pid) {
		ResultBean rb = new ResultBean();
		if (ValidateMode.isNull(belong) || pid == null) {
			rb.setStatus(ResultBean.ERROR);
			rb.setBody(FUserService.error(
					"please make sure the needed fileds are not null")
					.toString());
		} else {
			try {
				ReturnValue rv = ns.updateImagesBelongNews(belong, pid);
				if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
					rb.setStatus(ResultBean.OK);
					rb.setBody(rv.getObject());
				} else {
					rb.setStatus(ResultBean.ERROR);
					rb.setBody("save error");
				}
			} catch (RuntimeException e) {
				e.printStackTrace();
				rb.setStatus(ResultBean.ERROR);
				rb.setBody(e.getMessage());
			}

		}
		return Response.ok(rb.toString()).build();
	}

	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/saveTopics/{belong}")
	@ResourceAccess(accessType = AccessType.CMS_AUTHORIZE)
	public Response saveTopics(@PathParam("belong") String belong,
			@FormParam("nid") String[] nid) {
		if (ValidateMode.isNull(belong) || nid == null) {
			ResultBean rb = new ResultBean();
			rb.setStatus(ResultBean.ERROR);
			rb.setBody(FUserService.error(
					"please make sure the needed fileds are not null")
					.toString());
			return Response.ok(rb.toString()).build();
		} else {
			ReturnValue<List<Map<String, String>>> rv = ns.saveTopicChild(
					belong, nid);
			if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
				ResultList rb = new ResultList();
				rb.setStatus(ResultBean.OK);
				rb.setBody(rv.getObject());
				return Response.ok(rb.toString()).build();
			} else {
				ResultBean rb = new ResultBean();
				rb.setStatus(ResultBean.ERROR);
				rb.setBody("save error");
				return Response.ok(rb.toString()).build();
			}
		}
	}

	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/news/{id}")
	// @ResourceAccess(accessType=AccessType.CMS_AUTHORIZE)
	public Response getNews(@PathParam("id") String id) {
		ResultBean rb = new ResultBean();
		ReturnValue<FNews> rv = ns.getNewsByID(id);
		if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
			rb.setStatus(ResultBean.OK);
			FNews fn = rv.getObject();
			fn.setContent("");
			rb.setBody(fn);
		} else {
			rb.setStatus(ResultBean.ERROR);
		}
		return Response.ok(rb.toString()).build();
	}

	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/advs/search")
	@ResourceAccess(accessType = AccessType.CMS_AUTHORIZE)
	public Response getAdvs(@FormParam("title") String title,
			@FormParam("status") String status,
			@FormParam("target") String target, @QueryParam("page") String page) {
		int p = 0;
		try {
			p = Integer.parseInt(page);
		} catch (Exception ex) {
			ex.printStackTrace();
			p = 0;
		}
		ResultBean rb = new ResultBean();
		ReturnValue<Map> rv = ns.getSearchAdv(title, status, p, target);
		if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
			rb.setStatus(ResultBean.OK);
			rb.setBody(rv.getObject());
		} else {
			rb.setStatus(ResultBean.ERROR);
		}
		return Response.ok(rb.toString()).build();
	}

	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/advs/{aid}")
	public Response getAdvs(@PathParam("aid") String id) {
		ResultBean rb = new ResultBean();
		try {
			int aid = Integer.parseInt(id);

			ReturnValue<Advs> rv = ns.getAdv(aid);
			if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
				rb.setStatus(ResultBean.OK);
				rb.setBody(rv.getObject());
			} else {
				rb.setStatus(ResultBean.ERROR);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			rb.setStatus(ResultBean.ERROR);
		}
		return Response.ok(rb.toString()).build();
	}

	/**
	 * 新闻时间戳检查，是否更新判断
	 * 
	 * @param id
	 * @return
	 */
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/isNew/{id}")
	public Response isNew(@PathParam("id") String id) {
		ResultBean rb = new ResultBean();
		ReturnValue<FNews> rv = ns.getNewsByID(id);
		if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
			String date = rv.getObject().getPubdate();
			rb.setBody(date);
			rb.setStatus(ResultBean.OK);
			/*
			 * DateFormat format=DateFormat.getDateInstance(); Date d; try { d =
			 * format.parse(date); rb.setBody(d.getTime());
			 * rb.setStatus(ResultBean.OK); } catch (ParseException e) {
			 * rb.setStatus(ResultBean.ERROR); rb.setMsg("wrong date format");
			 * e.printStackTrace(); }
			 */
		} else {
			rb.setStatus(ResultBean.ERROR);
		}
		return Response.ok(rb.toString()).build();
	}

	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/news/html/{id}")
	// @ResourceAccess(accessType=AccessType.CMS_AUTHORIZE)
	public Response publicNewsContentHtml(@PathParam("id") String id) {
		ResultBean rb = new ResultBean();
		ReturnValue<FNews> rv = ns.getNewsByID(id);
		if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
			return Response.ok(rv.getObject().getContent()).build();
		} else {
			rb.setStatus(ResultBean.ERROR);
		}
		return Response.ok(rb.toString()).build();
	}

	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/news/comment/{id}")
	public Response getNewsComment(@PathParam("id") String id,
			@FormParam("uid") String uid, @QueryParam("page") String page) {
		int p = 0;
		try {
			p = Integer.parseInt(page);
		} catch (Exception ex) {
			ex.printStackTrace();
			p = 0;
		}
		ResultBean rb = new ResultBean();
		ReturnValue<Map> rv = ns.getCommentByNewsId(uid, id, p);
		if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
			rb.setBody(rv.getObject());
			rb.setStatus(ResultBean.OK);
			return Response.ok(rb.toString()).build();
		} else {
			rb.setStatus(ResultBean.ERROR);
		}
		return Response.ok(rb.toString()).build();
	}

	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/date/{date}")
	public Response getNewsLisst(@PathParam("date") String date) {
		ResultBean rb = new ResultBean();
		rb.setStatus(ResultBean.OK);
		ReturnValue<Map> rv = ns.getNewsList(date);
		// JSONObject map=new JSONObject();
		Map map = new HashMap();
		map.put("indexList", rv.getObject());
		rb.setBody(map);
		return Response.ok(rb.toString()).build();
	}

	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/currentDate")
	public Response getCurrentDateNewsList() {
		ResultBean rb = new ResultBean();
		rb.setStatus(ResultBean.OK);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		ReturnValue<Map> rv = ns.getNewsList(dateFormat.format(new Date()));
		// JSONObject map=new JSONObject();
		Map map = new HashMap();
		List<Map> list = new ArrayList<Map>();
		list.add(rv.getObject());
		map.put("indexList", list);
		rb.setBody(map);
		return Response.ok(rb.toString()).build();
	}

	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/currentDate/{date}")
	public Response getCurrentDateNewsList(@PathParam("date") String date) {
		ResultBean rb = new ResultBean();
		rb.setStatus(ResultBean.OK);
		ReturnValue<Map> rv = ns.getNewsList(date);
		// JSONObject map=new JSONObject();
		Map map = new HashMap();
		List<Map> list = new ArrayList<Map>();
		list.add(rv.getObject());
		map.put("indexList", list);
		rb.setBody(map);
		return Response.ok(rb.toString()).build();
	}

	/**
	 * 不经过card直接返回当天所有新闻,yyyy-MM-dd
	 * 
	 * @return
	 */
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/dateNews/{date}")
	@ResourceAccess(accessType = AccessType.CMS_AUTHORIZE)
	public Response getDateNewsList(@PathParam("date") String date) {
		ResultBean rb = new ResultBean();
		rb.setStatus(ResultBean.OK);
		// SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		ReturnValue<List<Map>> rv = ns.getNewsListNoCard(date);
		// JSONObject map=new JSONObject();
		Map map = new HashMap();
		map.put("indexList", rv.getObject());
		rb.setBody(map);
		return Response.ok(rb.toString()).build();
	}

	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/sevenDatesList")
	@ResourceAccess(accessType = AccessType.CMS_AUTHORIZE)
	public Response getNewsSevenList(@QueryParam("show") String show) {
		ResultBean rb = new ResultBean();
		rb.setStatus(ResultBean.OK);
		boolean b = false;
		if (show != null && "true".equals(show)) {
			b = true;
		}
		ReturnValue<List<Map>> rv = ns.getNewsSevenDaysList(new Date(), b);
		// JSONObject map=new JSONObject();
		Map map = new HashMap();
		map.put("indexList", rv.getObject());
		rb.setBody(map);
		return Response.ok(rb.toString()).build();
	}

	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/sevenDates/{beginDate}")
	public Response getNewsSevenList(@PathParam("beginDate") String date,
			@QueryParam("show") String show) {
		ResultBean rb = new ResultBean();
		rb.setStatus(ResultBean.OK);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date beginDate = new Date();
		try {
			beginDate = dateFormat.parse(date);
			boolean b = false;
			if (show != null && "true".equals(show)) {
				b = true;
			}
			ReturnValue<List<Map>> rv = ns.getNewsSevenDaysList(beginDate, b);
			// JSONObject map=new JSONObject();
			Map map = new HashMap();
			map.put("indexList", rv.getObject());
			rb.setBody(map);
			return Response.ok(rb.toString()).build();
		} catch (ParseException e) {
			rb.setStatus(ResultBean.ERROR);
			rb.setMsg("wrong date format");
			e.printStackTrace();
			return Response.ok(rb.toString()).build();
		}
	}

	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/saveLikes/{cid}")
	@ResourceAccess(accessType = AccessType.AUTHORIZE)
	public Response saveLikes(@PathParam("cid") String nid,
			@FormParam("uid") String uid) {
		ResultBean rb = new ResultBean();
		rb.setStatus(ResultBean.OK);
		try {
			int cid = Integer.parseInt(nid);
			ReturnValue<String> rv = ns.saveLikes(cid, uid);
			rb.setBody(rv.getObject());
			return Response.ok(rb.toString()).build();
		} catch (Exception e) {
			rb.setStatus(ResultBean.ERROR);
			if (log.isInfoEnabled()) {
				log.info(e.getMessage());
			}
			rb.setMsg("need comment id ");
			e.printStackTrace();
			return Response.ok(rb.toString()).build();
		}
	}

	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/saveCollect/{nid}")
	@ResourceAccess(accessType = AccessType.AUTHORIZE)
	public Response saveCollect(@PathParam("nid") String nid,
			@FormParam("uid") String uid) {
		ResultBean rb = new ResultBean();
		if (ValidateMode.isNull(nid, uid)) {
			rb.setMsg("exist null value");
			rb.setStatus(ResultBean.ERROR);
			return Response.ok(rb.toString()).build();
		}
		rb.setStatus(ResultBean.OK);
		try {
			ReturnValue<String> rv = ns.saveCollect(nid, uid);
			rb.setBody(rv.getObject());
			return Response.ok(rb.toString()).build();
		} catch (Exception e) {
			rb.setStatus(ResultBean.ERROR);
			rb.setMsg(e.getMessage() + "collect fail");
			e.printStackTrace();
			return Response.ok(rb.toString()).build();
		}
	}

	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/saveFeedback")
	@ResourceAccess(accessType = AccessType.AUTHORIZE)
	public Response saveFeedback(@BeanParam FFeedback fb) {
		ResultBean rb = new ResultBean();
		rb.setStatus(ResultBean.OK);
		try {
			ReturnValue<String> rv = ns.saveFeedback(fb);
			rb.setBody(rv.getObject());
			return Response.ok(rb.toString()).build();
		} catch (Exception e) {
			rb.setStatus(ResultBean.ERROR);
			rb.setMsg(e.getMessage() + "feedback fail");
			e.printStackTrace();
			return Response.ok(rb.toString()).build();
		}
	}

	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/comment/search")
	@ResourceAccess(accessType = AccessType.CMS_AUTHORIZE)
	public Response getCommentSearch(@FormParam("uid") String uid,
			@FormParam("status") String status,
			@QueryParam("page") String page,
			@QueryParam("keyword") String keyword) {
		ResultBean rb = new ResultBean();
		int current = 0;
		try {
			current = Integer.parseInt(page);
		} catch (Exception ex) {
			current = 0;
		}
		ReturnValue<Map> rv = ns
				.getCommentSearch(uid, status, current, keyword);
		if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
			rb.setStatus(ResultBean.OK);
			Map fn = rv.getObject();
			rb.setBody(fn);
		} else {
			rb.setStatus(ResultBean.ERROR);
		}
		return Response.ok(rb.toString()).build();
	}

	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/comment/update")
	@ResourceAccess(accessType = AccessType.CMS_AUTHORIZE)
	public Response commentUpdate(@FormParam("cid") String cid[],
			@FormParam("status") String status) {
		ResultBean rb = new ResultBean();
		if (status == null) {
			rb.setStatus(ResultBean.ERROR);
			rb.setMsg("cid cann't null");
		} else {
			ReturnValue<String> rv = ns.updateCommentStatus(cid, status);
			if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
				rb.setStatus(ResultBean.OK);
				rb.setBody(rv.getObject());
			} else {
				rb.setStatus(ResultBean.ERROR);
				rb.setMsg(rv.getMeg());
			}
		}
		return Response.ok(rb.toString()).build();
	}

	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/advs/update/{aid}")
	public Response updateAdv(@BeanParam FAdv fadv, @PathParam("aid") String aid) {
		ResultBean rb = new ResultBean();
		int id = 0;
		try {
			id = Integer.parseInt(aid);
		} catch (Exception ex) {
			rb.setStatus(ResultBean.ERROR);
			return Response.ok(rb.toString()).build();
		}
		ns.updateAdvs(fadv, id);
		rb.setStatus(ResultBean.OK);
		return Response.ok(rb.toString()).build();
	}

	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/warning/update/{id}")
	@ResourceAccess(accessType = AccessType.CMS_AUTHORIZE)
	public Response updateWarning(@PathParam("id") String aid,
			@FormParam("status") String status) {
		ResultBean rb = new ResultBean();
		int id = 0;
		try {
			id = Integer.parseInt(aid);
		} catch (Exception ex) {
			rb.setStatus(ResultBean.ERROR);
			return Response.ok(rb.toString()).build();
		}
		ReturnValue<String> rv = ns.updateWarning(id, status);
		rb.setStatus(ResultBean.OK);
		rb.setBody(rv.getObject());
		return Response.ok(rb.toString()).build();
	}

	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/feedback/update/{wid}")
	@ResourceAccess(accessType = AccessType.CMS_AUTHORIZE)
	public Response updateFeedback(@PathParam("wid") String wid,
			@FormParam("status") String status) {
		ResultBean rb = new ResultBean();
		ReturnValue<String> rv = ns.updateFeedBack(wid, status);
		if (rv.getFlag() != ReturnValue.FLAG_SUCCESS) {
			rb.setStatus(ResultBean.ERROR);
			rb.setMsg(rv.getMeg());
			return Response.ok(rb.toString()).build();
		}
		rb.setStatus(ResultBean.OK);
		rb.setBody(rv.getObject());
		return Response.ok(rb.toString()).build();
	}

	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/advs/delete/{aid}")
	@ResourceAccess(accessType = AccessType.CMS_AUTHORIZE)
	public Response deleteAdv(@PathParam("aid") String aid) {
		ResultBean rb = new ResultBean();
		int id = 0;
		try {
			id = Integer.parseInt(aid);
		} catch (Exception ex) {
			rb.setStatus(ResultBean.ERROR);
			return Response.ok(rb.toString()).build();
		}
		ReturnValue<String> rv = ns.deleteAdv(id);
		rb.setStatus(ResultBean.OK);
		rb.setBody(rv.getObject());
		return Response.ok(rb.toString()).build();
	}

	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/collect/delete/{cid}")
	public Response deleteCollect(@PathParam("cid") String cid,
			@FormParam("uid") String uid) {
		ResultBean rb = new ResultBean();
		int id = 0;
		try {
			id = Integer.parseInt(cid);
		} catch (Exception ex) {
			rb.setStatus(ResultBean.ERROR);
			return Response.ok(rb.toString()).build();
		}
		ReturnValue<String> rs = ns.cancelCollect(id, uid);
		rb.setStatus(ResultBean.OK);
		rb.setBody(rs.getObject());
		return Response.ok(rb.toString()).build();
	}

	/**
	 * 从用户角度他的评论被评论列表
	 * 
	 * @param uid
	 * @param page
	 * @return
	 */
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/notice/comment/{uid}")
	public Response getCComment(@PathParam("uid") String uid,
			@QueryParam("page") String page) {
		int p = 0;
		ResultBean rb = new ResultBean();
		try {
			p = Integer.parseInt(page);
		} catch (Exception ex) {
			p = 0;
		}
		ReturnValue<Map> rv = ns.getNoticeComment(uid, p);
		if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
			rb.setBody(rv.getObject());
			rb.setStatus(ResultBean.OK);
		} else {
			rb.setStatus(ResultBean.OK);
			rb.setMsg("no results");
		}
		return Response.ok(rb.toString()).build();
	}

	/**
	 * 从评论的角度，他被评论的列表
	 * 
	 * @param cid
	 * @param page
	 * @return
	 */
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/ccommentList/{cid}")
	// @MobileResourceAccess
	// @ResourceAccess
	public Response getCCommentList(@PathParam("cid") String cid,
			@QueryParam("page") String page) {
		int p = 0;
		ResultBean rb = new ResultBean();
		try {
			p = Integer.parseInt(page);
		} catch (Exception ex) {
			p = 0;
		}
		if (ValidateMode.isNull(cid)) {
			rb.setMsg("cid must not null");
			rb.setStatus(ResultBean.ERROR);
			return Response.ok(rb.toString()).build();
		}
		int pcid = 0;
		try {
			pcid = Integer.parseInt(cid);
		} catch (Exception ex) {
			rb.setMsg("wrong cid");
			rb.setStatus(ResultBean.ERROR);
			return Response.ok(rb.toString()).build();
		}
		ReturnValue<Map> rv = ns.getCComments(pcid, p);
		if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
			rb.setBody(rv.getObject());
			rb.setStatus(ResultBean.OK);
		} else {
			rb.setStatus(ResultBean.OK);
			rb.setMsg("no results");
		}
		return Response.ok(rb.toString()).build();
	}

	/**
	 * 用户角度，他的评论被点赞列表
	 * 
	 * @param uid
	 * @param page
	 * @return
	 */
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/notice/likes/{uid}")
	public Response getLComment(@PathParam("uid") String uid,
			@QueryParam("page") String page) {
		int p = 0;
		ResultBean rb = new ResultBean();
		try {
			p = Integer.parseInt(page);
		} catch (Exception ex) {
			p = 0;
		}
		ReturnValue<Map> rv = ns.getNoticeLikes(uid, p);
		if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
			rb.setBody(rv.getObject());
			rb.setStatus(ResultBean.OK);
		} else {
			rb.setStatus(ResultBean.OK);
			rb.setMsg("no results");
		}
		return Response.ok(rb.toString()).build();
	}

	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/otherList/feedbacks")
	@ResourceAccess(accessType = AccessType.CMS_AUTHORIZE)
	public Response getFeedbacks(@QueryParam("page") String page,
			@QueryParam("status") String status) {
		int p = 0;
		ResultBean rb = new ResultBean();
		try {
			p = Integer.parseInt(page);
		} catch (Exception ex) {
			p = 0;
		}
		ReturnValue<Map> rv = null;
		if (status == null)
			rv = ns.getFeedBacks(p);
		else
			rv = ns.getFeedbackByStatus(status, p);
		if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
			rb.setBody(rv.getObject());
			rb.setStatus(ResultBean.OK);
		} else {
			rb.setStatus(ResultBean.OK);
			rb.setMsg("no results");
		}
		return Response.ok(rb.toString()).build();
	}

	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/feedback/get/{wid}")
	@ResourceAccess(accessType = AccessType.CMS_AUTHORIZE)
	public Response getFeedbackByWID(@PathParam("wid") String wid) {
		ResultBean rb = new ResultBean();
		ReturnValue<Map> rv = ns.getFeedbackByWID(wid);
		if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
			rb.setBody(rv.getObject());
			rb.setStatus(ResultBean.OK);
		} else {
			rb.setStatus(ResultBean.OK);
			rb.setMsg("no results");
		}
		return Response.ok(rb.toString()).build();
	}

	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/otherList/warnings")
	@ResourceAccess(accessType = AccessType.CMS_AUTHORIZE)
	public Response getWarnings(@QueryParam("page") String page,
			@QueryParam("status") String status) {
		int p = 0;
		ResultBean rb = new ResultBean();
		try {
			p = Integer.parseInt(page);
		} catch (Exception ex) {
			p = 0;
		}
		ReturnValue<Map> rv = null;
		if (status == null)
			rv = ns.getWarnings(p);
		else
			rv = ns.getWarningByStatus(status, p);
		if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
			rb.setBody(rv.getObject() == null ? "" : rv.getObject());
			rb.setStatus(ResultBean.OK);
		} else {
			rb.setStatus(ResultBean.OK);
			rb.setMsg("no results");
		}
		return Response.ok(rb.toString()).build();
	}

	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/cards/search")
	@ResourceAccess(accessType = AccessType.CMS_AUTHORIZE)
	public Response getCardsSearch(@QueryParam("nid") String nid) {
		ResultBean rb = new ResultBean();
		ReturnValue<Map> rv = ns.getCardsSearch(nid);
		if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
			rb.setBody(rv.getObject());
			rb.setStatus(ResultBean.OK);
		} else {
			rb.setStatus(ResultBean.OK);
			rb.setMsg("no results");
		}
		return Response.ok(rb.toString()).build();
	}

	public static void main(String[] args) {

	}
}
