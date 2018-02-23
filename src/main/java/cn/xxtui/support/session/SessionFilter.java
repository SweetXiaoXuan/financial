package cn.xxtui.support.session;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.Resource;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.RequestHandler;
import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.jaxrs.model.OperationResourceInfo;
import org.apache.cxf.message.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import cn.com.ql.wiseBeijing.auth.service.AuthorityService;
import cn.com.ql.wiseBeijing.serviceUtil.ReturnValue;
import cn.com.ql.wiseBeijing.session.service.MobileSessionService;
import cn.xxtui.support.bean.ResultBean;
import cn.xxtui.support.util.DateUtil;
import cn.xxtui.support.util.XXMediaType;

/**
 * 用户访问资源方法时要进行的拦截处理，目录方法（或类）上使用ResourceAccess注解进行标识
 * 
 * @author starlee
 */
public class SessionFilter implements RequestHandler {

	@Resource(name = "mobileSessionService")
	private MobileSessionService ms;
	@Autowired
	private AuthorityService authService;
	private Logger log = LogManager.getLogger(SessionFilter.class.getName());

	@Override
	public Response handleRequest(Message message, ClassResourceInfo classInfo) {
		// HttpServletRequest request =
		// (HttpServletRequest)m.get(AbstractHTTPDestination.HTTP_REQUEST);
		String uri = (String) message.get(Message.REQUEST_URI);
		String method = (String) message.get(Message.HTTP_REQUEST_METHOD);
		String info = " request method is :" + method + " and uri is :" + uri;
		Map<String, String> cookies = getRequestCookies(message);
		String device = cookies.get(SessionConstant.DEVICE);
		cookies.put("requestUrl", uri);
		// 如何处理可以参考一下JAXRSInterceptor这个类，里面有很多有意思的东西，这个自定类的执行也是在那里面调用的
		// 比如个message.getExchange().put(OperationResourceInfo.class, ori);
		// ProviderFactory provider = ProviderFactory.getInstance(message);
		OperationResourceInfo ori = message.getExchange().get(OperationResourceInfo.class);
		if (ori == null) {
			log.info("there is no OperationResourceInfo");
			ResultBean result = new ResultBean();
			result.setStatus(ResultBean.ERROR);
			result.setMsg(info);
			return Response.ok(result.toString()).header("Content-Type", XXMediaType.TEXTUTF8).build();
		}
		ResourceAccess ra = ori.getAnnotatedMethod().getAnnotation(ResourceAccess.class);
		MobileResourceAccess mra = ori.getAnnotatedMethod().getAnnotation(MobileResourceAccess.class);

		//String mid = (String) message.getExchange().getSession().get(SessionConstant.CMS_MANGER_ID);
		//String uid = cookies.get(SessionConstant.COOKIE_UID);
		//String checkId = mid == null ? uid : mid;
		// api访问检测
		// Map<String, Object> checkMap =
		// authService.isUserThrough(uri.toString(),checkId);
		// 地址尚未进行配置
		// checkApi(checkMap);
		if (ra != null || mra != null) {
			return null;
		}
		// return judge(message, ra, cookies);// 检查方法上

		// 移动检测
		return null;
	}
	private void checkApi(Map<String, Object> checkMap) {
		if (checkMap.size() > 0) {
			if (!(boolean) checkMap.get("isThrough")) {
				// 访问配置库来决定是否有权限访问
				/*
				 * ResultBean result = new ResultBean();
				 * result.setStatus(ResultBean.NotAssess);
				 * result.setMsg(checkMap.get("msg")==null?"访问受限制":checkMap.get(
				 * "msg").toString()); return
				 * Response.ok(result.toString()).header("Content-Type",
				 * XXMediaType.TEXTUTF8).build();
				 */
			}
		}
	}

	private Map<String, String> getRequestCookies(Message message) {
		Map<String, String> cookies = new HashMap<String, String>();
		Map map_header = (Map) message.get(Message.PROTOCOL_HEADERS);
		if (map_header != null) {
			log.info("header++++++++++++++++" + map_header.toString());
		}
		Object obj = map_header.get("Cookie");
		if (obj != null) {
			log.info("cookies+++++++++++++++" + obj.toString());
		}
		if (obj == null) {
			return cookies;
		}
		List<String> cookieStr = (List) obj;// ArryayList,并且只有一个元素
		StringTokenizer strtoken = new StringTokenizer(cookieStr.get(0), ";");
		while (strtoken.hasMoreTokens()) {
			String cookie = strtoken.nextToken();
			int position = cookie.indexOf("=");
			if (position > 0) {
				String key = cookie.substring(0, position);
				String value = cookie.substring(position + 1, cookie.length());
				cookies.put(key.trim(), value.trim());
			}
		}
		return cookies;
	}

	private Response judge(Message message, ResourceAccess ra, Map<String, String> cookies) {
		AccessType type = ra.accessType();
		SessionControl sc = SessionControl.getInstance();
		String rsid = cookies.get(SessionConstant.COOKIE_RSID);
		String uid = cookies.get(SessionConstant.COOKIE_UID);
		Map<String, String> str = sc.get(SessionConstant.CATEGORY_USER, rsid);// 暂时不用了
		ReturnValue<Map> rv = ms.getSession(rsid);
		switch (type) {
		case AUTHORIZE:
			log.info("rsid--------------------" + rsid);
			log.info("uid--------------------" + uid);
			if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
				ms.updateLastTime(DateUtil.getCurrentDateForSql());
				Map map = rv.getObject();
				String suid = (String) map.get("uid");
				if (suid.equals(uid)) {
					return null;
				} else {
					ResultBean result = new ResultBean();
					result.setStatus("3");
					result.setMsg("to access this resource,you should log in");
					return Response.ok(result.toString()).header("Content-Type", XXMediaType.TEXTUTF8).build();
				}
			} else {
				ResultBean result = new ResultBean();
				result.setStatus(ResultBean.PROTECT);
				result.setMsg("to access this resource,you should log in");
				return Response.ok(result.toString()).header("Content-Type", XXMediaType.TEXTUTF8).build();
			}
		case CMS_AUTHORIZE:
			String mid = (String) message.getExchange().getSession().get(SessionConstant.CMS_MANGER_ID);
			log.info("CMS_mid--------------------" + mid);
			if (mid != null) {
				// TODO，验证是不是真正本人
				// 得到用户菜单 requestUrl
				String requestUrl = cookies.get("requestUrl");
				if (!authService.isThrough(mid, requestUrl)) {
					ResultBean result = new ResultBean();
					result.setStatus(ResultBean.PROTECT);
					result.setMsg("to access this resource,You do not have permission");
					return Response.ok(result.toString()).header("Content-Type", XXMediaType.TEXTUTF8).build();
				}
				// 检查用户权限
				// 检查用户菜单
				return null;
			} else {
				ResultBean result = new ResultBean();
				result.setStatus(ResultBean.PROTECT);
				result.setMsg("to access this resource,you should log in");
				return Response.ok(result.toString()).header("Content-Type", XXMediaType.TEXTUTF8).build();
			}
		default:
			return null;
		}
	}
}
