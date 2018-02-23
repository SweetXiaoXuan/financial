package cn.lv.jewelry.index.indexBrand.frontService;

import cn.com.ql.wiseBeijing.serviceUtil.ReturnValue;
import cn.lv.jewelry.activity.daoBean.ActivityPrivilegeStatus;
import cn.lv.jewelry.activity.service.ActivityService;
import cn.lv.jewelry.brand.service.BrandService;
import cn.lv.jewelry.index.indexBrand.frontBean.FBrandBean;
import cn.xxtui.support.bean.ResultBean;
import cn.xxtui.support.bean.ResultStruct;
import cn.xxtui.support.util.MeaasgeUtil;
import cn.xxtui.support.util.ResultMsgConstant;
import cn.xxtui.support.util.SystemException;
import cn.xxtui.support.util.XXMediaType;
import com.alibaba.fastjson.JSON;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BeanParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * 品牌方相关
 */
@Component
@Path("/lv/brand")
public class FBrandService {
    private final static Logger logger = LoggerFactory.getLogger(FBrandService.class);
    @Bean
    public FBrandService fBrandService() {
        return new FBrandService();
    }
    private MeaasgeUtil me = new MeaasgeUtil();

    @Resource(name = "brandService")
    private BrandService brandService;
    @Resource(name = "activityService")
    private ActivityService activityService;

    /**
     * 企业品牌注册
     * @param fBrandBean 相关参数
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2018/2/10
     */
    @Path("/brandCompanyRegist")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response brandCompanyRegist(
            @BeanParam FBrandBean fBrandBean,
            @Context HttpServletRequest request) {
        ResultStruct resultStruct = new ResultStruct();
        resultStruct.setStatus(ResultBean.ERROR);
        resultStruct.setMsg(me.getValue(ResultMsgConstant.registFailed));
        try {
            // 添加用户信息
            ReturnValue<FBrandBean> rv = brandService.brandCompanyRegist(fBrandBean);
            JSONObject o = new JSONObject(JSON.toJSONString(rv.getObject()));
            if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
                Long uid = o.getLong("uid");
                // 添加用户权限信息
                ReturnValue rvPrivilege = activityService.saveActivityPrivilege(
                        uid,
                        ActivityPrivilegeStatus.PERSONAL.getStatus()
                );
                if (rvPrivilege.getFlag() == ReturnValue.FLAG_SUCCESS) {
                    return brandCompanyLogin(fBrandBean, request);
                } else {
                    resultStruct.setStatus(ResultBean.ERROR);
                    resultStruct.setMsg(rvPrivilege.getMeg());
                    return Response.ok(resultStruct.toString()).build();
                }
            } else {
                resultStruct.setMsg(rv.getMeg());
                return Response.ok(resultStruct.toString()).build();
            }
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * 个人品牌注册
     * @param fBrandBean 相关参数
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2018/2/10
     */
    @Path("/brandPersonalRegist")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response brandPersonalRegist(
            @BeanParam FBrandBean fBrandBean,
            @Context HttpServletRequest request) {
        ResultStruct resultStruct = new ResultStruct();
        resultStruct.setStatus(ResultBean.ERROR);
        resultStruct.setMsg(me.getValue(ResultMsgConstant.registFailed));
        try {
            // 添加用户信息
            ReturnValue<FBrandBean> rv = brandService.brandPersonalRegist(fBrandBean);
            JSONObject o = new JSONObject(JSON.toJSONString(rv.getObject()));
            if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
                Long uid = o.getLong("uid");
                // 添加用户权限信息
                ReturnValue rvPrivilege = activityService.saveActivityPrivilege(
                        uid,
                        ActivityPrivilegeStatus.PERSONAL.getStatus()
                );
                if (rvPrivilege.getFlag() == ReturnValue.FLAG_SUCCESS) {
                    return brandCompanyLogin(fBrandBean, request);
                } else {
                    resultStruct.setStatus(ResultBean.ERROR);
                    resultStruct.setMsg(rvPrivilege.getMeg());
                    return Response.ok(resultStruct.toString()).build();
                }
            } else {
                resultStruct.setMsg(rv.getMeg());
                return Response.ok(resultStruct.toString()).build();
            }
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }

    /**
     * 密码登陆
     * @param fBrandBean 接收用户相关参数
     * @param request 请求体
     * @return javax.ws.rs.core.Response
     * @author liumengwei
     * @Date 2018/2/10
     */
    @POST
    @Produces({ XXMediaType.TEXTUTF8 })
    @Path("/brandCompanyLogin")
    public Response brandCompanyLogin(
            @BeanParam FBrandBean fBrandBean,
            @Context HttpServletRequest request) {
        ResultStruct resultStruct = new ResultStruct();
        try {
            // 密码登陆
            ReturnValue<FBrandBean> fuser = brandService.brandCompanyLogin(fBrandBean);
            if (fuser.getFlag() == ReturnValue.FLAG_SUCCESS) {
                // 登陆成功拼接返回数据
                fBrandBean = fuser.getObject();
                resultStruct.setMsg(me.getValue(ResultMsgConstant.loginSuccess));
                Map<String, Object> map = new HashMap<>();
                Map<String, Object> mapUser = new HashMap<>();
//                map.put("uid", "00" + fuser.getObject().getUid());// 00表示自已平台
//                map.put("login_time",
//                        String.valueOf(DateUtil.getCurrentDate().getTime()));
//                mapUser.put("phone", user.getPhone());
//                mapUser.put("platform_uid", fuser.getObject().getUid());
//                mapUser.put("givename", "");
//                mapUser.put("verified", fuser.getObject().getVerified());
//                if (user.getHeadPic() != null)
//                    mapUser.put("headPic", user.getHeadPic());
//                else
//                    mapUser.put("headPic", "");
//                map.put("user", mapUser);
//                map.put("pid", user.getPid());// 权限id
//                map.put("userType", user.getUserType());// 用户类型
                resultStruct.setBody(map);
                resultStruct.setStatus(ResultBean.OK);
                return Response.ok(resultStruct.toString()).build();
            } else {
                resultStruct.setStatus(ResultStruct.ERROR);
                resultStruct.setMsg(fuser.getMeg());
                Map<String, Object> map = new HashMap<>();
                map.put("error", fuser.getMeg());
                resultStruct.setBody(map);
                return Response.ok(resultStruct.toString()).build();
            }
        } catch (Exception ex) {
            return SystemException.setResult(resultStruct, ex, logger);
        }
    }
}
