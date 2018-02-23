package cn.com.ql.wiseBeijing.dec.frontService;

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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import cn.com.ql.wiseBeijing.dec.frontBean.FDecNews;
import cn.com.ql.wiseBeijing.dec.service.DecNewsService;
import cn.com.ql.wiseBeijing.dec.util.Weather;
import cn.com.ql.wiseBeijing.imageService.frontBean.FImages;
import cn.com.ql.wiseBeijing.news.frontBean.FComment;
import cn.com.ql.wiseBeijing.news.frontBean.FNews;
import cn.com.ql.wiseBeijing.serviceUtil.ReturnValue;
import cn.com.ql.wiseBeijing.user.frontservice.FUserService;
import cn.xxtui.support.bean.ResultBean;
import cn.xxtui.support.bean.ResultList;
import cn.xxtui.support.session.AccessType;
import cn.xxtui.support.session.ResourceAccess;
import cn.xxtui.support.util.ValidateMode;
import cn.xxtui.support.util.XXMediaType;

@Path("/democracy/")
@Component

public class FDecNewsService {

	@Resource(name = "decNewsService")
	private DecNewsService ns;

	@Bean
	public FDecNewsService fdecNewsService() {
		return new FDecNewsService();
	}
	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/pub")
	@ResourceAccess(accessType=AccessType.CMS_AUTHORIZE)
	public Response publicNews(@BeanParam FDecNews fnews) {
		ResultBean rb = new ResultBean();
		if (ValidateMode.isNull(fnews.getLargeimage(),
				fnews.getListimage(), fnews.getDescription(),
				fnews.getSmallimage(), fnews.getTitle(), fnews.getType(),
				fnews.getKeywords())) {
			rb.setStatus(ResultBean.ERROR);
			rb.setBody(FUserService.error(
					"please make sure the need fileds are not null").toString());
		} else {
			try {
				ReturnValue<FDecNews> rv = ns.saveMainNews(fnews);
				if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
					rb.setBody(rv.getObject());
					rb.setStatus(ResultBean.OK);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				rb.setStatus(ResultBean.ERROR);
				rb.setBody("Exception:"+ex.getMessage());
			}
		}
		return Response.ok(rb.toString()).build();
	}
	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/saveImage")
	public Response saveImages(@BeanParam FImages fimages) {
		ResultBean rb = new ResultBean();
		if (ValidateMode.isNull(fimages.getDescription(), fimages.getUrl())) {
			rb.setStatus(ResultBean.ERROR);
			rb.setBody(FUserService.error(
					"please make sure the needed fileds are not null").toString());
		} else {
			rb.setStatus(ResultBean.OK);
			ReturnValue<FImages> rv = ns.saveDecImages(fimages.getUrl(),
					fimages.getDescription());
			rb.setBody(rv.getObject());
		}
		return Response.ok(rb.toString()).build();
	}
	
	
	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/saveCategoryImage/{belong}")
	@ResourceAccess(accessType=AccessType.CMS_AUTHORIZE)
	public Response saveCategoryImages(@PathParam("belong") String belong,
			@BeanParam FImages fimages) {
		ResultBean rb = new ResultBean();
		if (ValidateMode.isNull(belong,fimages.getUrl(),fimages.getDescription())) {
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
	@Path("/saveTopics/{belong}")
	@ResourceAccess(accessType=AccessType.CMS_AUTHORIZE)
	public Response saveTopics(@PathParam("belong") String belong,
			@FormParam("nid") String[] nid) {
		
		if (ValidateMode.isNull(belong)||nid==null) {
			ResultBean rb = new ResultBean();
			rb.setStatus(ResultBean.ERROR);
			rb.setBody(FUserService.error(
					"please make sure the needed fileds are not null")
					.toString());
			return Response.ok(rb.toString()).build();
		} else {
			ReturnValue<List<Map<String, String>>> rv = ns.saveTopicChild(belong, nid);
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
	
	
	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/saveCard")
	@ResourceAccess(accessType=AccessType.CMS_AUTHORIZE)
	public Response saveCard(@FormParam("news_array") String[] news,
			@FormParam("template") String template,@FormParam("pubdate") String pubdate) {
		ResultBean rb = new ResultBean();
		if (ValidateMode.isNull(template) || news == null) {
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
			ReturnValue<String>  r=ns.saveCard(sb.toString(), template,pubdate);
			if(r.getFlag()==ReturnValue.FLAG_FAIL)
			{
				rb.setStatus(ResultBean.ERROR);
				rb.setMsg(r.getMeg());
			}
			else
			{
				rb.setStatus(ResultBean.OK);
				rb.setMsg(r.getObject());
			}
		}
		return Response.ok(rb.toString()).build();
	}
	
	
	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/cancelCard/{cardID}")
	@ResourceAccess(accessType=AccessType.CMS_AUTHORIZE)
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
	@ResourceAccess(accessType=AccessType.CMS_AUTHORIZE)
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
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/democ/{id}")
	//@ResourceAccess(accessType=AccessType.CMS_AUTHORIZE)
	public Response getNews(@PathParam("id") String id) {
		ResultBean rb = new ResultBean();
		ReturnValue<FDecNews> rv = ns.getNewsByID(id);
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
	@Path("/democ_mobile/{id}")
	//@ResourceAccess(accessType=AccessType.CMS_AUTHORIZE)
	public Response getMobileNews(@PathParam("id") String id) {
		ResultBean rb = new ResultBean();
		ReturnValue<FDecNews> rv = ns.getNewsByID(id);
		if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
			rb.setStatus(ResultBean.OK);
			FDecNews fDec = rv.getObject();
			fDec.setContent("");
			rb.setBody(fDec);
		} else {
			rb.setStatus(ResultBean.ERROR);
		}
		return Response.ok(rb.toString()).build();
	}


	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/news/html/{id}")
	//@ResourceAccess(accessType=AccessType.CMS_AUTHORIZE)
	public Response getNewsContentHtml(@PathParam("id") String id) {
		ResultBean rb = new ResultBean();
		ReturnValue<FDecNews> rv = ns.getNewsByID(id);
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
	public Response getNewsComment(@PathParam("id") String id,@FormParam("uid") String uid,@QueryParam("page") String page) {
		int p = 0;
		try {
			p = Integer.parseInt(page);
		} catch (Exception ex) {
			ex.printStackTrace();
			p = 0;
		}
		ResultBean rb = new ResultBean();
		ReturnValue<Map> rv = ns.getCommentByNewsId(uid,id, p);
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

	/**
	 * 不经过card直接返回当天所有新闻,yyyy-MM-dd
	 * 
	 * @return
	 */
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/dateNews/{date}")
	@ResourceAccess(accessType=AccessType.CMS_AUTHORIZE)
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
	
	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/saveComment")
	@ResourceAccess(accessType=AccessType.AUTHORIZE)
	public Response saveComment(@BeanParam FComment comment) {
		ResultBean rb = new ResultBean();
		if (ValidateMode.isNull(comment.getComment(), comment.getNewsid())) {
			rb.setStatus(ResultBean.ERROR);
			rb.setBody(FUserService.error(
					"please make sure the needed fileds are not null")
					.toString());
		} else {
			rb.setStatus(ResultBean.OK);
			ReturnValue<String> rv = ns.saveComment(comment.getComment(),
					comment.getNewsid(), comment.getUserid(),comment.getCommentid());
			rb.setBody(rv.getObject());
		}
		return Response.ok(rb.toString()).build();
	}
	

	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/saveWarning")
	@ResourceAccess(accessType = AccessType.AUTHORIZE)
	public Response saveWarning(@BeanParam FComment comment) {
		ResultBean rb = new ResultBean();
		if (ValidateMode.isNull(comment.getComment(), comment.getNewsid(),comment.getUserid())) {
			rb.setStatus(ResultBean.ERROR);
			rb.setBody(FUserService.error(
					"please make sure the needed fileds are not null")
					.toString());
		} else {
			rb.setStatus(ResultBean.OK);
			ReturnValue<String> rv = ns.saveWarning(comment.getComment(),
					comment.getNewsid(), comment.getUserid());
			rb.setBody(rv.getObject());
		}
		return Response.ok(rb.toString()).build();
	}
	
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/currentDate")
	public Response getCurrentDateNewsList() {
		ResultBean rb = new ResultBean();
		rb.setStatus(ResultBean.OK);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		ReturnValue<Map> rv = ns.getNewsList(dateFormat
				.format(new Date()));
		// JSONObject map=new JSONObject();
		Map map = new HashMap();
		List<Map> list=new ArrayList<Map>();
		list.add(rv.getObject());
		map.put("indexList",list );
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
		List<Map> list=new ArrayList<Map>();
		list.add(rv.getObject());
		map.put("indexList",list );
		rb.setBody(map);
		return Response.ok(rb.toString()).build();
	}
	
	
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/sevenDates")
	@ResourceAccess(accessType=AccessType.CMS_AUTHORIZE)
	public Response getNewsSevenList(@QueryParam("show") String show) {
		ResultBean rb = new ResultBean();
		rb.setStatus(ResultBean.OK);
		boolean b=false;
		if(show!=null&&"true".equals(show))
		{
			b=true;
		}
		ReturnValue<List<Map>> rv = ns.getNewsSevenDaysList(new Date(),b);
		// JSONObject map=new JSONObject();
		Map map = new HashMap();
		map.put("indexList", rv.getObject());
		rb.setBody(map);
		return Response.ok(rb.toString()).build();
	}

	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/sevenDates/{beginDate}")
	public Response getNewsSevenList(@PathParam("beginDate") String date,@QueryParam("show") String show) {
		ResultBean rb = new ResultBean();
		rb.setStatus(ResultBean.OK);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date beginDate = new Date();
		try {
			boolean b=false;
			if(show!=null&&"true".equals(show))
			{
				b=true;
			}
			beginDate = dateFormat.parse(date);
			ReturnValue<List<Map>> rv = ns.getNewsSevenDaysList(beginDate,b);
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

	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/predict/weather/{date}/{areaid}")
	public Response getWeather(@PathParam("date") String date,
			@PathParam("areaid") String areaid) {
		ResultBean rb = new ResultBean();
		rb.setStatus(ResultBean.OK);
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(Weather.get(areaid, "forecast_v", date));
		try {
			HttpResponse response = client.execute(get);
			String result = EntityUtils.toString(response.getEntity(),"UTF-8");
			rb.setBody(result);
		} catch (Exception ex) 
		{
			rb.setStatus(ResultBean.ERROR);
			ex.printStackTrace();
			rb.setMsg(ex.getMessage());
		}
		return Response.ok(rb.toString()).build();
	}
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/predict/weather_index/{date}/{areaid}")
	public Response getIndex(@PathParam("date") String date,
			@PathParam("areaid") String areaid) {
		ResultBean rb = new ResultBean();
		rb.setStatus(ResultBean.OK);
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(Weather.getIndex(areaid, date));
		try {
			HttpResponse response = client.execute(get);
			String result = EntityUtils.toString(response.getEntity(),"UTF-8");
			rb.setBody(result);
		} catch (Exception ex) 
		{
			rb.setStatus(ResultBean.ERROR);
			ex.printStackTrace();
			rb.setMsg(ex.getMessage());
		}
		return Response.ok(rb.toString()).build();
	}
	
	/**
	 * 新闻时间戳检查，是否更新判断
	 * @param id
	 * @return
	 */
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/isNew/{id}")
	public Response isNew(@PathParam("id") String id) {
		ResultBean rb = new ResultBean();
		ReturnValue<FDecNews> rv = ns.getNewsByID(id);
		if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
			String date=rv.getObject().getPubdate();
			rb.setBody(date);
			rb.setStatus(ResultBean.OK);
			/*DateFormat format=DateFormat.getDateInstance();
			Date d;
			try {
				d = format.parse(date);
				rb.setBody(d.getTime());
				rb.setStatus(ResultBean.OK);
			} catch (ParseException e) {
				rb.setStatus(ResultBean.ERROR);
				rb.setMsg("wrong date format");
				e.printStackTrace();
			}*/
		} else {
			rb.setStatus(ResultBean.ERROR);
		}
		return Response.ok(rb.toString()).build();
	}
	
	
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/code/weather")
	public Response getWeatherCode(@QueryParam("likename") String likeName) {
		ResultList<Map> rb = new ResultList<Map>();
		rb.setStatus(ResultBean.OK);
		ReturnValue<List<Map>> rv=ns.getWeatherArea(likeName);
		rb.setBody(rv.getObject());
		return Response.ok(rb.toString()).build();
	}
	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/saveCollect/{nid}")
	public Response saveCollect(@PathParam("nid") String nid,@FormParam("uid") String uid) {
		ResultBean rb = new ResultBean();
		rb.setStatus(ResultBean.OK);
		try {
			ReturnValue<String> rv = ns.saveCollect(nid, uid);
			rb.setBody(rv.getObject());
			return Response.ok(rb.toString()).build();
		} catch (Exception e) {
			rb.setStatus(ResultBean.ERROR);
			rb.setMsg(e.getMessage());
			e.printStackTrace();
			return Response.ok(rb.toString()).build();
		}
	}
	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/updateTopics/{belong}")
	@ResourceAccess(accessType=AccessType.CMS_AUTHORIZE)
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
	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/updateCategoryImage/{belong}")
	@ResourceAccess(accessType=AccessType.CMS_AUTHORIZE)
	public Response updateCategoryImages(@PathParam("belong") String belong,
			@FormParam("pid") String[] pid) {
		ResultBean rb = new ResultBean();
		if (ValidateMode.isNull(belong)||pid==null) {
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
				rb.setBody(":"+e.getMessage());
			}

		}
		return Response.ok(rb.toString()).build();
	}
	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/update/{nid}")
	@ResourceAccess(accessType=AccessType.CMS_AUTHORIZE)
	public Response updateNews(@BeanParam FNews fnews,@PathParam("nid") String nid) {
		ResultBean rb = new ResultBean();
		if (ValidateMode.isNull(fnews.getLargeimage(),
				fnews.getListimage(), fnews.getDescription(),
				fnews.getSmallimage(), fnews.getTitle(), fnews.getType(),
				fnews.getKeywords())) {
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
	@ResourceAccess(accessType=AccessType.CMS_AUTHORIZE)
	public Response updateNewsStatus(@FormParam("status") String  status,@PathParam("nid") String nid) {
		ResultBean rb = new ResultBean();
		if (ValidateMode.isNull(status,nid)) {
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
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/cards/search")
	@ResourceAccess(accessType=AccessType.CMS_AUTHORIZE)
	public Response getCardsSearch(@QueryParam("nid") String nid) {
		ResultBean rb = new ResultBean();
		ReturnValue<Map> rv=ns.getCardsSearch(nid);
		if(rv.getFlag()==ReturnValue.FLAG_SUCCESS)
		{
			rb.setBody(rv.getObject());
			rb.setStatus(ResultBean.OK);
		}
		else
		{
			rb.setStatus(ResultBean.OK);
			rb.setMsg("no results");
		}
		return Response.ok(rb.toString()).build();
	}
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
}
