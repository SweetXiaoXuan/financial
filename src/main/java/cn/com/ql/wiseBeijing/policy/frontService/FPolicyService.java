package cn.com.ql.wiseBeijing.policy.frontService;

import java.util.ArrayList;
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

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import cn.com.ql.wiseBeijing.policy.frontBean.FPolicyItem;
import cn.com.ql.wiseBeijing.policy.service.PolicyService;
import cn.com.ql.wiseBeijing.serviceUtil.ReturnValue;
import cn.xxtui.support.bean.ResultBean;
import cn.xxtui.support.bean.ResultList;
import cn.xxtui.support.session.AccessType;
import cn.xxtui.support.session.ResourceAccess;
import cn.xxtui.support.util.XXMediaType;

@Path("/policy/")
@Component
public class FPolicyService {
	@Bean
	public FPolicyService fpolicyService() {
		return new FPolicyService();
	}

	@Resource(name = "policyService")
	private PolicyService ps;

	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/indexAll")
	@ResourceAccess(accessType=AccessType.CMS_AUTHORIZE)
	public Response getAllIndex() {
		ResultList rb = new ResultList();
		rb.setBody(ps.getIndexAll().getObject());
		rb.setStatus(ResultBean.OK);
		return Response.ok(rb.toString()).build();
	}
	
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/isIndexNew")
	public Response isIndexNew() {
		ResultBean rb = new ResultBean();
		ReturnValue<String> rv=ps.isUpdateIndex();
		if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
			String date=rv.getObject();
			rb.setBody(date);
			rb.setStatus(ResultBean.OK);
			/*DateFormat format=DateFormat.getDateTimeInstance();
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

	/**
	 * 获取政务指南详细，非列表 json
	 * 
	 * @return
	 */
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/getDetailJson/{id}")
	public Response getDetailJson(@PathParam("id") String id) {
		ResultBean rb = new ResultBean();
		ReturnValue<FPolicyItem> fpp = ps.getDetailLevel(id);
		if (fpp.getFlag() == ReturnValue.FLAG_SUCCESS) {
			rb.setStatus(ResultBean.OK);
			rb.setBody(fpp.getObject());
		} else {
			rb.setStatus(ResultBean.ERROR);
		}
		return Response.ok(rb.toString()).build();
	}

	/**
	 * 获取政务指南详细，内容详细内容 json
	 * 
	 * @return
	 */
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/getDetailContent/{id}")
	public Response getDetailContent(@PathParam("id") String id) {
		ResultBean rb = new ResultBean();
		ReturnValue<String> fpp = ps.getDetailContent(id);
		if (fpp.getFlag() == ReturnValue.FLAG_SUCCESS) {
			return Response.ok(fpp.getObject()).build();
		} else {
			rb.setStatus(ResultBean.ERROR);
			return Response.ok(rb.toString()).build();
		}
	}

	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/isNew/{id}")
	public Response isNew(@PathParam("id") String id) {
		ResultBean rb = new ResultBean();
		ReturnValue<FPolicyItem> rv = ps.getDetailLevel(id);
		if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
			String date=rv.getObject().getPubdate();
			//DateFormat format=DateFormat.getDateTimeInstance();
			rb.setBody(date);
			rb.setStatus(ResultBean.OK);
			/*Date d;
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
	
	/**
	 * 一级列表
	 * 
	 * @param title
	 * @param logo
	 * @param hasNext
	 * @return
	 */
	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/addFirstLevel")
	@ResourceAccess(accessType=AccessType.CMS_AUTHORIZE)
	public Response addFirstLevel(@FormParam("title") String title,
			@FormParam("logo") String logo,@FormParam("url") String url,@FormParam("hasNext") String hasNext) {
		ResultBean rb = new ResultBean();
		boolean next = false;
		if ("1".equals(hasNext))
			next = true;
		ReturnValue<Map> fpp = ps.saveFirstLevel(title, logo,url,next);
		if (fpp.getFlag() == ReturnValue.FLAG_SUCCESS) {
			rb.setStatus(ResultBean.OK);
			rb.setBody(fpp.getObject());
		} else {
			rb.setStatus(ResultBean.ERROR);
		}
		return Response.ok(rb.toString()).build();
	}
	/**
	 * 二级列表
	 * 就是一级列表next为1的需要加,否则不需要
	 * @param title
	 * @param logo
	 * @param hasNext
	 * @return
	 */
	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/addSecondLevel")
	@ResourceAccess(accessType=AccessType.CMS_AUTHORIZE)
	public Response addSecondLevel(@FormParam("title") String title,
			@FormParam("pid") String parent_id) {
		ResultBean rb = new ResultBean();
		try {
			int pid = Integer.parseInt(parent_id);
			ReturnValue<Map> fpp = ps.saveSecondLevel(title, pid);
			if (fpp.getFlag() == ReturnValue.FLAG_SUCCESS) {
				rb.setStatus(ResultBean.OK);
				rb.setBody(fpp.getObject());
			} else {
				rb.setStatus(ResultBean.ERROR);
			}
		} catch (Exception ex) {
			rb.setStatus(ResultBean.ERROR);
			rb.setMsg("add failed " + ex.getMessage());
		}
		return Response.ok(rb.toString()).build();
	}

	/**
	 * 三级或二级列表
	 * 一级next为0的，二级next为0,再往往下一级列表实际是添加详情
	 * @param title
	 * @param logo
	 * @param hasNext
	 * @return
	 */
	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/addThirdLevel")
	@ResourceAccess(accessType=AccessType.CMS_AUTHORIZE)
	public Response addSecondLevel(@BeanParam FPolicyItem item,
			@FormParam("pid") String parent_id) {
		ResultBean rb = new ResultBean();
		boolean next = false;
		try {
			int pid = Integer.parseInt(parent_id);
			ReturnValue<Map> fpp = ps.saveDetailLevel(item, pid);
			if (fpp.getFlag() == ReturnValue.FLAG_SUCCESS) {
				rb.setStatus(ResultBean.OK);
				rb.setBody(fpp.getObject());
			} else {
				rb.setStatus(ResultBean.ERROR);
			}
		} catch (Exception ex) {
			rb.setStatus(ResultBean.ERROR);
			rb.setMsg("add failed " + ex.getMessage());
		}
		return Response.ok(rb.toString()).build();
	}
	
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/index")
	public Response getIndex() {
		ResultList rb = new ResultList();
		rb.setStatus(ResultBean.OK);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		list.add(test("教育test", "1"));
		list.add(test("劳动就业test", "2"));
		list.add(test("社会保障test", "3"));
		rb.setBody(list);
		return Response.ok(rb.toString()).build();
	}

	
	
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/item/{id}")
	public Response getItem(@PathParam("id") String id) {
		ResultList rb = new ResultList();
		id = "1";
		rb.setStatus(ResultBean.OK);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		list.add(test("教育考试test", "1"));
		list.add(test("入学转学test", "2"));
		rb.setBody(list);
		return Response.ok(rb.toString()).build();
	}
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/delete/{id}")
	@ResourceAccess(accessType=AccessType.CMS_AUTHORIZE)
	public Response delpolicy(@PathParam("id") String id,@QueryParam("isItem") boolean isItem) {
		ResultBean rb = new ResultBean();
		ReturnValue<String> rs= ps.delete(id,isItem);
		rb.setMsg(rs.getMeg());
		rb.setStatus(rs.getFlag()+"");
		return Response.ok(rb.toString()).build();
	}
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/children/{id}")
	public Response getChildren(@PathParam("id") String id) {
		ResultList rb = new ResultList();
		id = "1";
		rb.setStatus(ResultBean.OK);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		list.add(test("高考高招test", "1"));
		list.add(test("高中会考test", "2"));
		list.add(test("中考中招test", "3"));
		list.add(test("学生申诉test", "4"));
		rb.setBody(list);
		return Response.ok(rb.toString()).build();
	}
	
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/get/{id}")
	public Response getData(@PathParam("id") String id,@QueryParam("isItem") boolean isItem){
		ResultBean bean = new ResultBean();
		bean.setBody(ps.get(id, isItem).getObject());
		bean.setStatus(ResultBean.OK);
		return Response.ok(bean.toString()).build();
	}
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/getContent/{id}")
	public Response getContent(@PathParam("id") String id){
		return Response.ok(ps.getContent(id).getObject().toString()).build();
	}
	@POST
	@Produces({ XXMediaType.TEXTUTF8 })
	@Path("/update/{id}")
	public Response update(@PathParam("id") String id,@FormParam("title") String title,@FormParam("description") String description,@FormParam("keywords") String keywords,@FormParam("content") String content,@FormParam("isItem") boolean isItem){
		ResultBean bean = new ResultBean();
		try {
			ps.update(id, title, description, keywords, content, isItem);
			bean.setBody(id);
			bean.setMsg("success!!");
			bean.setStatus(ResultBean.OK);
		} catch (Exception e) {
			// TODO: handle exception
			bean.setMsg(e.getMessage());
			bean.setStatus(ResultBean.ERROR);
		}
		return Response.ok(bean.toString()).build();
	}
	private Map<String, String> test(String name, String id) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", name);
		map.put("id", id);
		return map;
	}
}
