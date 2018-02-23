package cn.lv.jewelry.index.indexFashion.frontService;


import cn.com.ql.wiseBeijing.serviceUtil.ReturnValue;
import cn.lv.jewelry.fashion.service.FashionService;
import cn.lv.jewelry.index.indexFashion.frontBean.FashionItemBean;
import cn.lv.jewelry.index.indexFashion.frontBean.SpecialCardBean;
import cn.lv.jewelry.index.indexFashion.frontBean.SpecialCardItemBean;
import cn.lv.jewelry.index.indexFashion.frontBean.SpecialItemBean;
import cn.xxtui.support.bean.ResultBean;
import cn.xxtui.support.util.XXMediaType;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 * Created by lixing on 16/4/9.
 */
@Component
@Path("/lv/indexFashion")
public class FIndexFashionService {
    @Bean
    public FIndexFashionService fIndexFashionService() {
        return new FIndexFashionService();
    }

    @Resource(name = "fashionService")
    private FashionService fashionService;

    /*************************  query ****************************************************************/

    /**
     * 查询专场基础信息id
     *
     * @return
     */
    @Path("/querySpecial/{sid}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response querySpecial(@PathParam("sid") String sid) {
        ResultBean rb = new ResultBean();
        rb.setStatus(ResultBean.ERROR);

        ReturnValue<SpecialItemBean> rv = fashionService.querySpeical(sid);
        if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
            rb.setBody(JSONObject.toJSONString(rv.getObject()));
            rb.setStatus(ResultBean.OK);
        } else {
            rb.setMsg(rv.getMeg());
        }

        return Response.ok(rb.toString()).build();
    }

    /**
     * 根据专场id查询专场卡片信息
     *
     * @return
     */
    @Path("/querySpecialCards/{sid}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response querySpecialCards(@PathParam("sid") String sid) {
        ResultBean rb = new ResultBean();
        rb.setStatus(ResultBean.ERROR);

        ReturnValue<SpecialCardBean> rv = fashionService.querySpeicalCards(sid);
        if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
            rb.setBody(JSONObject.toJSONString(rv.getObject()));
            rb.setStatus(ResultBean.OK);
        } else {
            rb.setMsg(rv.getMeg());
        }

        return Response.ok(rb.toString()).build();
    }

    /**
     * 专场
     *
     * @return
     */
    @Path("/querySpecials/{page}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response querySpecials(@PathParam("page") String page) {
        ResultBean rb = new ResultBean();
        rb.setStatus(ResultBean.ERROR);
        int p = 0;
        try {
            p = Integer.parseInt(page);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        ReturnValue<Map> rv = fashionService.querySpeicals(p);
        if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
            rb.setBody(JSONObject.toJSONString(rv.getObject()));
            rb.setStatus(ResultBean.OK);
        } else {
            rb.setMsg(rv.getMeg());
        }
        return Response.ok(rb.toString()).build();
    }

    /**
     * 查询达人信息
     *
     * @return
     */
    @Path("/queryFashionMan/{id}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response queryFashionMan(@PathParam("id") String id) {
        ResultBean rb = new ResultBean();
        rb.setStatus(ResultBean.ERROR);

        ReturnValue<FashionItemBean> rv = fashionService.queryFashionMan(id);
        if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
            rb.setBody(JSONObject.toJSONString(rv.getObject()));
            rb.setStatus(ResultBean.OK);
        } else {
            rb.setMsg(rv.getMeg());
        }

        return Response.ok(rb.toString()).build();
    }


    /************************* insert ****************************************************************/


    /**
     * 插入专场基本信息
     *
     * @param specialItemBean
     * @return
     */
    @Path("/addSpecial")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response saveSpecial(@BeanParam SpecialItemBean specialItemBean) {

        ResultBean rb = new ResultBean();
        rb.setStatus(ResultBean.ERROR);
        ReturnValue<SpecialItemBean> rv = fashionService.saveSpecial(specialItemBean);
        if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
            specialItemBean.setId(rv.getObject().getId());
            rb.setBody(JSONObject.toJSONString(specialItemBean));
            rb.setStatus(ResultBean.OK);
        } else {
            rb.setMsg(rv.getMeg());
        }

        return Response.ok(rb.toString()).build();
    }


    /**
     * 插入专场卡片信息
     *
     * @param request
     * @return
     */
    @Path("/addSpecialCards")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({XXMediaType.TEXTUTF8})
    public Response saveSpecialCards(@Context HttpServletRequest request) {
        ResultBean rb = null;
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String line = reader.readLine();
            StringBuilder sbb = new StringBuilder();
            while (line != null) {
                sbb.append(line);
                line = reader.readLine();
            }
            reader.close();

            List<SpecialCardItemBean> specialCardItemBeans = JSON.parseArray(sbb.toString(), SpecialCardItemBean.class);

            SpecialCardBean specialCardBean = new SpecialCardBean();
            specialCardBean.setSpecialCardItemBeans(specialCardItemBeans);

            rb = new ResultBean();
            rb.setStatus(ResultBean.ERROR);
            ReturnValue<SpecialCardBean> rv = fashionService.saveSpecialCards(specialCardBean);
            if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
                rb.setBody(JSONObject.toJSONString(specialCardBean));
                rb.setStatus(ResultBean.OK);
            } else {
                rb.setMsg(rv.getMeg());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return Response.ok(rb.toString()).build();
    }
}
