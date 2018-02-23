package cn.com.ql.wiseBeijing.other.frontService;

import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import cn.com.ql.wiseBeijing.other.service.UtilService;
import cn.com.ql.wiseBeijing.serviceUtil.ReturnValue;
import cn.xxtui.support.bean.ResultBean;
import cn.xxtui.support.session.AccessType;
import cn.xxtui.support.session.ResourceAccess;
import cn.xxtui.support.util.XXMediaType;

@Component
@Path("/utils")
public class FUtilService {
	@Bean
	public FUtilService futilService(){
		return new FUtilService();
	}
	@Resource(name="utilService")
	private UtilService us;
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/search")
	@ResourceAccess(accessType=AccessType.CMS_AUTHORIZE)
	public Response getSearch(@QueryParam("keywords") String keywords)
	{
		ReturnValue<Map> r=us.search(keywords);
		ResultBean rb=new ResultBean();
		if(r.getFlag()==ReturnValue.FLAG_SUCCESS)
		{
			rb.setStatus(ResultBean.OK);
			rb.setBody(r.getObject());
		}
		else
		{
			rb.setStatus(ResultBean.ERROR);
		}
		return Response.ok(rb.toString()).build();
	}
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/search/moreNews")
	public Response getMoreNewsSearch(@QueryParam("keywords") String keywords,@QueryParam("page") String page)
	{
		int p=0;
		try
		{
			p=Integer.parseInt(page);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			p=0;
		}
		ReturnValue<Map> r=us.searchNews(keywords, p, 50);
		ResultBean rb=new ResultBean();
		if(r.getFlag()==ReturnValue.FLAG_SUCCESS)
		{
			rb.setStatus(ResultBean.OK);
			rb.setBody(r.getObject());
		}
		else
		{
			rb.setStatus(ResultBean.ERROR);
		}
		return Response.ok(rb.toString()).build();
	}
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/search/moreDemocracy")
	@ResourceAccess(accessType=AccessType.CMS_AUTHORIZE)
	public Response getMoreDemoracySearch(@QueryParam("keywords") String keywords,@QueryParam("page") String page)
	{
		int p=0;
		try
		{
			p=Integer.parseInt(page);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			p=0;
		}
		ReturnValue<Map> r=us.searchDemocracy(keywords, p, 50);
		ResultBean rb=new ResultBean();
		if(r.getFlag()==ReturnValue.FLAG_SUCCESS)
		{
			rb.setStatus(ResultBean.OK);
			rb.setBody(r.getObject());
		}
		else
		{
			rb.setStatus(ResultBean.ERROR);
		}
		return Response.ok(rb.toString()).build();
	}
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/search/morePolicy")
	public Response getMorePolicySearch(@QueryParam("keywords") String keywords,@QueryParam("page") String page)
	{
		int p=0;
		try
		{
			p=Integer.parseInt(page);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			p=0;
		}
		ReturnValue<Map> r=us.searchPolicy(keywords, p, 50);
		ResultBean rb=new ResultBean();
		if(r.getFlag()==ReturnValue.FLAG_SUCCESS)
		{
			rb.setStatus(ResultBean.OK);
			rb.setBody(r.getObject());
		}
		else
		{
			rb.setStatus(ResultBean.ERROR);
		}
		return Response.ok(rb.toString()).build();
	}
}
