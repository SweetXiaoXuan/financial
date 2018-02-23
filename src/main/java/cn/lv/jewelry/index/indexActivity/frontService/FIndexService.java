package cn.lv.jewelry.index.indexActivity.frontService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import cn.lv.jewelry.index.indexActivity.frontBean.ActivityBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import cn.lv.jewelry.activity.daoBean.ActivityStatusType;
import cn.lv.jewelry.activity.service.ActivityService;
import cn.lv.jewelry.index.indexActivity.frontBean.NodeBean;
import cn.lv.jewelry.index.indexActivity.frontBean.NodeBeanVideo;
import cn.xxtui.support.bean.ResultBean;
import cn.xxtui.support.util.XXMediaType;

@Component
@Path("/lv/index")
public class FIndexService {
	@Resource(name = "activityService")
	private ActivityService activityService;
	@Bean
	public FIndexService fIndexService() {
		return new FIndexService();
	}

	@Path("indexActive")
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	public Response indexActivity() {
		ResultBean bean = new ResultBean();
		bean.setStatus(ResultBean.OK);
		Map<String, Object> map = new HashMap<String, Object>();
		//加状态、最新时间3条、将举办，枚举
		List<ActivityBean> list = activityService.getActivitingTop(6, ActivityStatusType.UPCOMING.getV());
		map.put("activies", list);
		String r = JSONObject.toJSONString(map);
		bean.setBody(r);
		return Response.ok(bean.toString()).build();
	}

	@Path("indexShare")
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	public Response indexShare() {
		ResultBean bean = new ResultBean();
		bean.setStatus(ResultBean.OK);
		Map<String, Object> map = new HashMap<String, Object>();
		//已经举办完，状态
		List<ActivityBean> list = activityService.getCompleteActivity(6, ActivityStatusType.COMPLETE.getV());
		map.put("share", list);
		String r = JSONObject.toJSONString(map);
		bean.setBody(r);
		return Response.ok(bean.toString()).build();
	}

	@Path("indexInfo")
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	public Response indexInfo() {
		ResultBean bean = new ResultBean();
		bean.setStatus(ResultBean.OK);
		Map<String, Object> map = new HashMap<String, Object>();
		List<NodeBean> list = new ArrayList<NodeBean>();
		list.add(NodeBean.getInstance("1", NodeBean.pre_path + "index/info_1_590x588.jpg", "描述1","0"));
		list.add(NodeBean.getInstance("2", NodeBean.pre_path + "index/info_2_590x588.jpg", "描述2","0"));
		list.add(NodeBean.getInstance("3", NodeBean.pre_path + "index/info_3_590x588.jpg", "描述3","0"));
		list.add(NodeBean.getInstance("3", NodeBean.pre_path + "index/info_4_590x588.jpg", "描述3","0"));
		List<NodeBean> list1 = new ArrayList<NodeBean>();
		list1.add(NodeBean.getInstance("1", NodeBean.pre_path + "index/info_1_590x588.jpg", "描述1","0"));
		list1.add(NodeBean.getInstance("2", NodeBean.pre_path + "index/info_2_590x588.jpg", "描述2","0"));
		list1.add(NodeBean.getInstance("3", NodeBean.pre_path + "index/info_3_590x588.jpg", "描述3","0"));
		list1.add(NodeBean.getInstance("3", NodeBean.pre_path + "index/info_4_590x588.jpg", "描述3","0"));
		List<NodeBean> list2 = new ArrayList<NodeBean>();
		list2.add(NodeBean.getInstance("1", NodeBean.pre_path + "index/info_1_590x588.jpg", "描述1","0"));
		list2.add(NodeBean.getInstance("2", NodeBean.pre_path + "index/info_2_590x588.jpg", "描述2","0"));
		list2.add(NodeBean.getInstance("3", NodeBean.pre_path + "index/info_3_590x588.jpg", "描述3","0"));
		list2.add(NodeBean.getInstance("3", NodeBean.pre_path + "index/info_4_590x588.jpg", "描述3","0"));
		List<List> listt = new ArrayList<List>();
		listt.add(list);
		listt.add(list1);
		listt.add(list2);
		map.put("info", listt);
		String r = JSONObject.toJSONString(map);
		bean.setBody(r);
		return Response.ok(bean.toString()).build();
	}


	@Path("indexVideo")
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	public Response indexVideo() {
		ResultBean bean = new ResultBean();
		bean.setStatus(ResultBean.OK);
		Map<String, Object> map = new HashMap<String, Object>();
		List<NodeBeanVideo> list = new ArrayList<NodeBeanVideo>();
		list.add(NodeBeanVideo.getInstance("1", NodeBean.pre_path+"video/1.mp4", "描述1",NodeBean.pre_path+"index/share_1_1242x700.jpg","0"));
		list.add(NodeBeanVideo.getInstance("2", NodeBean.pre_path+"video/2.mp4", "描述2",NodeBean.pre_path+"index/active_1_1242x700.jpg","0"));
		map.put("videos", list);
		String r = JSONObject.toJSONString(map);
		bean.setBody(r);
		return Response.ok(bean.toString()).build();
	}

	@Path("testScript")
	@GET
	@Produces({ XXMediaType.TEXTUTF8 })
	public Response indexTest(@QueryParam("abc") String abc,@FormParam("bcd") String bcd) {
		ResultBean bean = new ResultBean();
		Map<String,String> map=new HashMap<>();
		map.put("videos", abc);
		map.put("bcd",bcd);
		String r = JSONObject.toJSONString(map);
		bean.setBody(r);
		return Response.ok(bean.toString()).build();
	}
}
