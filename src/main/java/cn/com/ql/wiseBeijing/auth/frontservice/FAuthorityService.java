package cn.com.ql.wiseBeijing.auth.frontservice;

import cn.com.ql.wiseBeijing.auth.daoBean.Api;
import cn.com.ql.wiseBeijing.auth.daoBean.Manger;
import cn.com.ql.wiseBeijing.auth.daoBean.UserApi;
import cn.com.ql.wiseBeijing.auth.frontBean.FApi;
import cn.com.ql.wiseBeijing.auth.service.AuthorityService;
import cn.com.ql.wiseBeijing.serviceUtil.ReturnValue;
import cn.com.ql.wiseBeijing.user.frontBean.FNewUserBean;
import cn.com.ql.wiseBeijing.user.frontservice.FUserService;
import cn.com.ql.wiseBeijing.user.service.UserService;
import cn.xxtui.support.bean.ResultBean;
import cn.xxtui.support.bean.ResultList;
import cn.xxtui.support.bean.ResultStruct;
import cn.xxtui.support.session.AccessType;
import cn.xxtui.support.session.MobileResourceAccess;
import cn.xxtui.support.session.ResourceAccess;
import cn.xxtui.support.session.SessionConstant;
import cn.xxtui.support.util.DateUtil;
import cn.xxtui.support.util.MeaasgeUtil;
import cn.xxtui.support.util.ResultMsgConstant;
import cn.xxtui.support.util.ValidateMode;
import cn.xxtui.support.util.XXMediaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BeanParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/auth/")
@Component
public class FAuthorityService {
    private static Logger logger= LogManager.getLogger(FAuthorityService.class);
    @Autowired
    private AuthorityService authService;
    @Resource(name = "userService")
    private UserService us;
    private MeaasgeUtil me = new MeaasgeUtil();

    @Bean
    public FAuthorityService fauthService() {
        return new FAuthorityService();
    }

    @Produces({XXMediaType.TEXTUTF8})
    @GET
    @Path("menu")
    // @ResourceAccess(accessType = AccessType.AUTHORIZE)
    public Response reset(@FormParam("id") String id) {
        ResultBean result = new ResultBean();
        try {
            result.setStatus(ResultBean.OK);
            result.setBody(authService.getRootMenu());
            result.setStatus(ResultBean.OK);
            return Response.ok(result.toString()).build();
        } catch (Exception ex) {
            result.setStatus(ResultBean.ERROR);
            result.setMsg(ex.getMessage());
            return Response.ok("重置失败").build();
        }
    }

    @Produces({XXMediaType.TEXTUTF8})
    @GET
    @Path("getURLInfo")
    public Response getURLInfo(@QueryParam("url") String url) {
        ResultStruct result = new ResultStruct();
        String path=URLDecoder.decode(url);
        try {
            Api api = authService.apiInfo(path);
            if (api != null) {
                FApi fapi_ = new FApi(api);
                result.setBody(fapi_);
                result.setStatus(ResultStruct.OK);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(),ex);
            result.setMsg(ex.getMessage());
        }
        return Response.ok(result.toString()).build();
    }

    @Produces({XXMediaType.TEXTUTF8})
    @POST
    @Path("addManger")
//	@ResourceAccess(accessType = AccessType.CMS_AUTHORIZE)
    public Response addManger(
            @BeanParam cn.com.ql.wiseBeijing.auth.frontBean.Manger manger) {
        ResultBean bean = new ResultBean();
        if (ValidateMode.isNull(manger.getUsername(), manger.getPassword(),
                manger.getEditorcode(), manger.getEditor())) {
            bean.setStatus(ResultBean.ERROR);
            bean.setBody(FUserService.error(
                    "please make sure the need fileds are not null").toString());

            return Response.ok(bean.toString()).build();
        }
        try {
            if (authService.isRename(manger.getUsername())) {
                bean.setStatus(ResultBean.ERROR);
                bean.setBody("the username is rename");
                return Response.ok(bean.toString()).build();
            }
            Manger manger2 = new Manger();
            manger2.setUsername(manger.getUsername());
            manger2.setPassword(manger.getPassword());
            manger2.setEditor(manger.getEditor());
            manger2.setEditorcode(manger.getEditorcode());
            manger2.setStatus("0");
            bean.setBody("Manger id is " + authService.addManger(manger2));
            bean.setStatus(ResultBean.OK);
            bean.setMsg(me.getValue(ResultMsgConstant.success));
        } catch (Exception e) {
            // TODO: handle exception
            bean.setMsg(e.getMessage());
            bean.setStatus(ResultBean.ERROR);
        }
        return Response.ok(bean.toString()).build();
    }

    @Produces({XXMediaType.TEXTUTF8})
    @GET
    @Path("manger")
    @ResourceAccess(accessType = AccessType.CMS_AUTHORIZE)
    @MobileResourceAccess
    public Response manger() {
        ResultBean bean = new ResultBean();
        try {
            ReturnValue<Map> rt = authService.getList();
            if (rt.getFlag() == ReturnValue.FLAG_SUCCESS) {
                bean.setBody(rt.getObject());
                bean.setStatus(ResultBean.OK);
                bean.setMsg("success!");
            } else {
                bean.setStatus(ResultBean.ERROR);
                bean.setMsg("success!");
            }
        } catch (Exception e) {
            // TODO: handle exception
            bean.setMsg(e.getMessage());
            bean.setStatus(ResultBean.ERROR);
        }
        return Response.ok(bean.toString()).build();
    }

    /**
     * 管理员登陆
     * @param request 请求体
     * @param name 用户名
     * @param pass 密码
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2017/10/21
     */
    @Produces({XXMediaType.TEXTUTF8})
    @POST
    @Path("cmsLogin")
    public Response cmsLogin(@Context HttpServletRequest request,
                          @FormParam("name") String name,
                          @FormParam("pass") String pass) {
        ResultStruct resultStruct = new ResultStruct();
        try {
            // 密码登陆
            FNewUserBean user = new FNewUserBean();
            user.setUsername(name);
            user.setPassword(pass);
            ReturnValue<FNewUserBean> fuser = us.login(user);
            if (fuser.getFlag() == ReturnValue.FLAG_SUCCESS) {
                // 登陆成功拼接返回数据
                user = fuser.getObject();
                resultStruct.setMsg(me.getValue(ResultMsgConstant.loginSuccess));
                Map<String, Object> map = new HashMap<>();
                Map<String, Object> mapUser = new HashMap<>();
                map.put("uid", "00" + fuser.getObject().getUid());// 00表示自已平台
                map.put("login_time",
                        String.valueOf(DateUtil.getCurrentDate().getTime()));
                mapUser.put("phone", user.getPhone());
                mapUser.put("platform_uid", fuser.getObject().getUid());
                mapUser.put("givename", "");
                mapUser.put("verified", fuser.getObject().getVerified());
                if (user.getHeadPic() != null)
                    mapUser.put("headPic", user.getHeadPic());
                else
                    mapUser.put("headPic", "");
                map.put("user", mapUser);
                map.put("pid", user.getPid());// 权限id
                map.put("userType", user.getUserType());// 用户类型
                resultStruct.setBody(map);
                resultStruct.setStatus(ResultBean.OK);
                return Response.ok(resultStruct.toString()).build();
            } else {
                resultStruct.setStatus(ResultStruct.ERROR);
                resultStruct.setMsg(me.getValue(ResultMsgConstant.loginFail));
                Map<String, Object> map = new HashMap<>();
                map.put("error", fuser.getMeg());
                resultStruct.setBody(map);
                return Response.ok(resultStruct.toString()).build();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            resultStruct.setStatus(ResultBean.ERROR);
            resultStruct.setMsg(ex.getMessage());
            return Response.ok(resultStruct.toString()).build();
        }
    }

    @Produces({XXMediaType.TEXTUTF8})
    @POST
    @Path("login")
    public Response login(@Context HttpServletRequest request,
                          @FormParam("username") String username,
                          @FormParam("password") String password,
                          @FormParam("code") String code) {
        ResultBean result = new ResultBean();
        try {
            String sid = request.getSession().getId();
            String captcha = (String) request.getSession().getAttribute(
                    SessionConstant.CAPTCHA);
            if (ValidateMode.isNull(code, username, password)) {
                result.setBody("code username or password is null,please check");
                result.setMsg("1");// 字段存在NULL
                result.setStatus(ResultBean.ERROR);
                return Response.ok(result.toString()).build();
            }
            if (!code.toLowerCase().equals(captcha.toLowerCase())) {
                result.setStatus(ResultBean.ERROR);
                result.setBody("check code is error " + sid + ":" + captcha);
                result.setMsg("2");// 验证码不对
                return Response.ok(result.toString()).build();
            }
            Manger manager = authService.login(username, password);
            if (manager == null) {
                result.setStatus(ResultBean.ERROR);
                result.setBody("the username or password is error");
                result.setMsg("3");// 用户名或密码错误
                return Response.ok(result.toString()).build();
            }
            request.getSession().setAttribute(SessionConstant.CMS_MANGER_ID,
                    String.valueOf(manager.getId()));

            // UID
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("uid", String.valueOf(manager.getId()));
            map.put("rootMenu", authService.getRootMenu().getObject());
            result.setBody(map);
            result.setStatus(ResultBean.OK);
            result.setMsg("0");
            return Response.ok(result.toString()).build();
        } catch (Exception ex) {
            ex.printStackTrace();
            result.setStatus(ResultBean.ERROR);
            result.setMsg(ex.getMessage());
            return Response.ok(result.toString()).build();
        }
    }

    @Produces({XXMediaType.TEXTUTF8})
    @GET
    @Path("grant")
    public Response grantAuthority(@QueryParam("uid") String uid,
                                   @QueryParam("authids") String authids) {

        ResultBean bean = new ResultBean();
        try {
            authService.grantAuthority(uid, authids);
            bean.setMsg("grant success!!");
            bean.setStatus(ResultBean.OK);
            return Response.ok(bean.toString()).build();
        } catch (Exception e) {
            // TODO: handle exception
            bean.setMsg(e.getMessage());
            bean.setStatus(ResultBean.ERROR);
            return Response.ok(bean.toString()).build();
        }
    }

    @Produces({XXMediaType.TEXTUTF8})
    @GET
    @Path("deleteManger")
//	@ResourceAccess(accessType = AccessType.CMS_AUTHORIZE)
    public Response grantAuthority(@FormParam("mid") String mid) {
        ResultBean bean = new ResultBean();
        if (ValidateMode.isNull(mid)) {
            bean.setMsg("1");
            bean.setBody("mid is null");
            bean.setStatus(ResultBean.ERROR);
            return Response.ok(bean.toString()).build();
        }
        try {
            Manger mange = new Manger();
            mange.setId(Integer.parseInt(mid));
            ReturnValue rv = authService.deleteManger(mange.getId());
            if (rv.getFlag() == ReturnValue.FLAG_FAIL) {
                bean.setMsg(rv.getMeg());
                bean.setStatus(ResultBean.ERROR);
            } else {
                bean.setMsg("grant success!!");
                bean.setStatus(ResultBean.OK);
            }
            return Response.ok(bean.toString()).build();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            bean.setMsg("the manger not found,please referh try again");
            bean.setStatus(ResultBean.ERROR);
            return Response.ok(bean.toString()).build();
        }
    }

    @Produces({XXMediaType.TEXTUTF8})
    @GET
    @Path("getUApi")
//	@ResourceAccess(accessType = AccessType.CMS_AUTHORIZE)
    public Response getUApi() {
        ResultList<Map<String, Object>> map = new ResultList<>();
        try {
            List<Map<String, Object>> list = authService.getUApiList();
            map.setBody(list);
            map.setStatus(ResultBean.OK);
            return Response.ok(map.toString()).build();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            map.setMsg("found exception!!");
            map.setStatus(ResultBean.ERROR);
            return Response.ok(map.toString()).build();
        }
    }

    @Produces({XXMediaType.TEXTUTF8})
    @GET
    @Path("updateUApi")
//	@ResourceAccess(accessType = AccessType.CMS_AUTHORIZE)
    public Response updateApi(@FormParam("id") String id, @FormParam("msg") String msg, @FormParam("status") String status) {
        ResultBean bean = new ResultBean();
        try {
            UserApi user = authService.updateStatus(id, msg, status);
            bean.setBody(user);
            bean.setStatus(ResultBean.OK);
            return Response.ok(bean.toString()).build();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            bean.setMsg("found exception!!");
            bean.setStatus(ResultBean.ERROR);
            return Response.ok(bean.toString()).build();
        }
    }

}
