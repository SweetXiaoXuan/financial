package cn.lv.jewelry.index.indexProduct.frontService;

import cn.com.ql.wiseBeijing.serviceUtil.ReturnValue;
import cn.lv.jewelry.index.indexActivity.frontService.FActivityService;
import cn.lv.jewelry.index.indexDesigner.frontBean.DesignerItemBean;
import cn.lv.jewelry.index.indexDesigner.frontBean.DesingerBean;
import cn.lv.jewelry.index.indexProduct.frontBean.*;
import cn.lv.jewelry.product.daoBean.ProductEmbedItemBean;
import cn.lv.jewelry.product.daoBean.ProductMaterialItemBean;
import cn.lv.jewelry.product.daoBean.ProductOriginItemBean;
import cn.lv.jewelry.product.daoBean.ProductStyleItemBean;
import cn.lv.jewelry.product.service.ProductService;
import cn.xxtui.support.bean.ResultBean;
import cn.xxtui.support.util.XXMediaType;
import com.alibaba.fastjson.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lixing on 16/4/9.
 */
@Component
@Path("/lv/indexProduct")
public class FIndexProductService {
    @Bean
    public FIndexProductService fIndexProductService() {
        return new FIndexProductService();
    }

    @Resource(name = "productService")
    private ProductService productService;
    @Resource(name = "fActivityService")
    private FActivityService fActivityService;


    /*************************  query ****************************************************************/

    /**
     * 跑马灯
     *
     * @return
     */
    @Path("/marquee")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response marquee() {
        MarqueeBean marqueeBean = new MarqueeBean();

        List<MarqueeItemBean> list = new ArrayList<MarqueeItemBean>();
        ReturnValue<Map<String, Object>> rv = productService.queryJewelrys("all", "1");
        List<ProductItemBean> productItemBeanList = (List<ProductItemBean>) rv.getObject().get("data");
        if (productItemBeanList != null) {
            int i = 0;
            for (ProductItemBean productItemBean : productItemBeanList) {
                i++;
                if (i > 6) {
                    break;
                }
                list.add(getMarqueeBean(productItemBean));
            }
        }
        marqueeBean.setCarousels(list);
        ResultBean resultBean = new ResultBean();
        resultBean.setStatus(ResultBean.OK);
        resultBean.setBody(JSONObject.toJSONString(marqueeBean));
        return Response.ok(resultBean.toString()).build();
    }

    private <T> MarqueeItemBean<T> getMarqueeBean(ProductItemBean type) {
        MarqueeItemBean desingerBeanMarqueeBean = new MarqueeItemBean();
        desingerBeanMarqueeBean.setId(type.getId());
        desingerBeanMarqueeBean.setCover_url(type.getCoverUrl());
        desingerBeanMarqueeBean.setType("0");
        type.setProductImageBeans(null);
        type.setProductActivityBeans(null);
        type.setProductVideos(null);
        type.setProductDesigners(null);
        type.setRelativeProductItemBeans(null);
        desingerBeanMarqueeBean.setExtention(type);
        return desingerBeanMarqueeBean;
    }

    //人气专场
    @Path("/popular/{type}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response popular(@PathParam("type") String type) {
        if ("".equals(type)) {

        }
        PopularBean popularBean = new PopularBean();
        List<PopularItemBean> list = new ArrayList<PopularItemBean>();
        list.add(new PopularItemBean());
        popularBean.setPopularities(list);

        ResultBean resultBean = new ResultBean();
        resultBean.setStatus(ResultBean.OK);
        resultBean.setBody(JSONObject.toJSONString(popularBean));
        return Response.ok(resultBean.toString()).build();
    }

    /**
     * 查询设计师
     *
     * @param type
     * @return
     */
    @Path("/designer/{type}/{page}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response designer(@PathParam("type") String type, @PathParam("page") String page) {
        DesingerBean desingerBean = new DesingerBean();
        List<DesignerItemBean> list = productService.queryDesigner(type, page);
        ResultBean resultBean = new ResultBean();
        desingerBean.setDesigners(list);
        resultBean.setStatus(ResultBean.OK);
        resultBean.setBody(JSONObject.toJSONString(desingerBean));
        return Response.ok(resultBean.toString()).build();
    }

    /**
     * 查询设计师
     *
     * @param id
     * @return
     */
    @Path("/singleDesigner/{id}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response designer(@PathParam("id") String id) {
        ResultBean resultBean = new ResultBean();
        try {
            int did = Integer.parseInt(id);
            DesignerItemBean designerItemBean = productService.querySingleDesigner(did);
            resultBean.setStatus(ResultBean.OK);
            resultBean.setBody(JSONObject.toJSON(designerItemBean));
        }
        catch (Exception ex)
        {
            resultBean.setStatus(ResultBean.ERROR);
            resultBean.setMsg(ex.getMessage());
        }
        return Response.ok(resultBean.toString()).build();
    }
    /**
     * 查询品牌信息
     *
     * @param type
     * @return
     */
    @Path("/brands/{type}/{page}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response brand(@PathParam("type") String type, @PathParam("page") String page) {
        BrandBean brandBean = new BrandBean();
        // TODO 以下方法是之前的，用到了brand表，2018-2-9号修改表设计需注释掉该代码
//        List<BrandItemBean> list = productService.queryBrands(type, page);
        ResultBean resultBean = new ResultBean();
        resultBean.setStatus(ResultBean.OK);
//        brandBean.setBrands(list);
        resultBean.setBody(JSONObject.toJSONString(brandBean));
        return Response.ok(resultBean.toString()).build();
    }

    /**
     * 查询商品类别信息
     *
     * @param page
     * @return
     */
    @Path("/queryCategory/{page}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response queryCategory(@PathParam("page") String page) {
        ProductCategoryBean productCategory = new ProductCategoryBean();
        List<FProductCategoryItemBean> list = productService.queryCategory(page);
        ResultBean resultBean = new ResultBean();
        resultBean.setStatus(ResultBean.OK);
        productCategory.setProductCategoryItemBeans(list);
        resultBean.setBody(JSONObject.toJSONString(productCategory));
        return Response.ok(resultBean.toString()).build();
    }

    /**
     * 查询商品镶嵌信息
     *
     * @param page
     * @return
     */
    @Path("/queryEmbed/{page}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response queryEmbed(@PathParam("page") String page) {
        ProductEmbedBean productEmbedBean = new ProductEmbedBean();
        List<ProductEmbedItemBean> list = productService.queryProductEmbedBeans(page);
        ResultBean resultBean = new ResultBean();
        resultBean.setStatus(ResultBean.OK);
        productEmbedBean.setProductEmbedItemBeans(list);
        resultBean.setBody(JSONObject.toJSONString(productEmbedBean));
        return Response.ok(resultBean.toString()).build();
    }

    /**
     * 查询商品材质信息
     *
     * @param page
     * @return
     */
    @Path("/queryMaterial/{page}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response queryMaterial(@PathParam("page") String page) {
        ProductMaterialBean productMaterialBean = new ProductMaterialBean();
        List<ProductMaterialItemBean> list = productService.queryProductMaterialBeans(page);
        ResultBean resultBean = new ResultBean();
        resultBean.setStatus(ResultBean.OK);
        productMaterialBean.setProductMaterialItemBeans(list);
        resultBean.setBody(JSONObject.toJSONString(productMaterialBean));
        return Response.ok(resultBean.toString()).build();
    }

    /**
     * 查询商品产地信息
     *
     * @param page
     * @return
     */
    @Path("/queryOrigin/{page}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response queryOrigin(@PathParam("page") String page) {
        ProductOriginBean productOriginBean = new ProductOriginBean();
        List<ProductOriginItemBean> list = productService.queryProductOriginBeans(page);
        ResultBean resultBean = new ResultBean();
        resultBean.setStatus(ResultBean.OK);
        productOriginBean.setProductOriginBeans(list);
        resultBean.setBody(JSONObject.toJSONString(productOriginBean));
        return Response.ok(resultBean.toString()).build();
    }

    /**
     * 查询商品风格信息
     *
     * @param page
     * @return
     */
    @Path("/queryStyle/{page}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response queryStyle(@PathParam("page") String page) {
        ProductStyleBean productStyle = new ProductStyleBean();
        List<ProductStyleItemBean> list = productService.queryProductStyleBeans(page);
        ResultBean resultBean = new ResultBean();
        resultBean.setStatus(ResultBean.OK);
        productStyle.setProductStyleBeans(list);
        resultBean.setBody(JSONObject.toJSONString(productStyle));
        return Response.ok(resultBean.toString()).build();
    }

    /**
     * 分页查询商品 | 查询所有商品
     *
     * @param type
     * @return
     */
    @Path("/queryJewelry/{type}/{page}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response jewelrys(@PathParam("type") String type, @PathParam("page") String page) {


//        ProductBean productBean = new ProductBean();
        ReturnValue<Map<String, Object>> list = productService.queryJewelrys(type, page);
        ResultBean resultBean = new ResultBean();
        resultBean.setStatus(ResultBean.OK);
//        productBean.setProducts(list);
        resultBean.setBody(JSONObject.toJSONString(list));
        return Response.ok(resultBean.toString()).build();
    }

    /**
     * 查询单件商品
     *
     * @param id
     * @return
     */
    @Path("/queryProduct/{id}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response product(@PathParam("id") String id) {
        ProductItemBean productItemBean = productService.queryJewelrys(id);
        ResultBean resultBean = new ResultBean();
        resultBean.setStatus(ResultBean.OK);
        resultBean.setBody(JSONObject.toJSONString(productItemBean));
        return Response.ok(resultBean.toString()).build();
    }

    /**
     * 按照名称查询商品
     *
     * @param name
     * @return
     */
    @Path("/queryProductByName/{type}/{page}/{name}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response queryProductByName(@PathParam("type") String type, @PathParam("page") String page, @PathParam("name") String name) {
        ReturnValue<Map<String, Object>> list = null;
        if ("*".equals(type.trim())) {
            type = "all";
        }
        if (null != name && "*".equals(name.trim())) {
            list = productService.queryJewelrys(type, page);
        } else {
            list = productService.queryProductByName(page, name);
        }
        ResultBean resultBean = new ResultBean();
        resultBean.setStatus(ResultBean.OK);
        resultBean.setBody(list.getObject());
        return Response.ok(resultBean.toString()).build();
    }


    /**
     * 查询商品的相关活动
     *
     * @param pid
     * @return
     */
    @Path("/queryProductActivity/{pid}")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response realtiveActivity(@PathParam("pid") String pid) {
        ProductRelativeActivityBean productRelativeActivityBean = new ProductRelativeActivityBean();
        List<ProductRelativeActivityItemBean> productRelativeActivityItemBeans = productService.queryProductRelativeActivity(pid);
        productRelativeActivityBean.setProductRelativeActivities(productRelativeActivityItemBeans);
        ResultBean resultBean = new ResultBean();
        resultBean.setStatus(ResultBean.OK);
        resultBean.setBody(JSONObject.toJSONString(productRelativeActivityBean));
        return Response.ok(resultBean.toString()).build();
    }


    /**
     * 查询商品的类型信息
     *
     * @return
     */
    @Path("/queryProductType")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response queryProductType() {
        ProductTypeBean productTypeBean = new ProductTypeBean();
        List<ProductTypeItemBean> productTypeItemBeans = productService.queryProductType();
        productTypeBean.setProductTypeItemBeans(productTypeItemBeans);
        ResultBean resultBean = new ResultBean();
        resultBean.setStatus(ResultBean.OK);
        resultBean.setBody(JSONObject.toJSONString(productTypeBean));
        return Response.ok(resultBean.toString()).build();
    }

    /************************* insert ****************************************************************/

    /**
     * 插入商品信息
     *
     * @param productItemBean
     * @return
     */
    @Path("/addJewelry")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response saveJewelry(@BeanParam ProductItemBean productItemBean) {
        return fActivityService.saveJewelry(productItemBean);
    }

    /**
     * 插入商品图片
     *
     * @param productImageBean
     * @return
     */
    @Path("/addProductImage/{rid}")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response saveProductImage(@PathParam("rid") String rid, @BeanParam ProductImageBean productImageBean) {

        ResultBean rb = new ResultBean();
        rb.setStatus(ResultBean.ERROR);
        if (ProductImageType.isExist(productImageBean.getType())) {
            productImageBean.setRid(rid);
            ReturnValue<ProductImageBean> rv = productService.saveProductImage(productImageBean);
            if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
                productImageBean.setId(rv.getObject().getId());
                rb.setBody(JSONObject.toJSONString(productImageBean));
                rb.setStatus(ResultBean.OK);
            } else {
                rb.setMsg(rv.getMeg());
            }
        } else {
            rb.setMsg("type not exists");
        }
        return Response.ok(rb.toString()).build();
    }

    /**
     * 插入商品的相关活动
     *
     * @param productActivityBean
     * @return
     */
    @Path("/addProductActivity")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response saveProductActivity(@BeanParam ProductActivityBean productActivityBean) {

        return fActivityService.saveJewelry(null);
    }

    /**
     * 商品插入相关商品
     *
     * @param productToProductItemBean
     * @return
     */
    @Path("/addProductToProduct")
    @POST
    @Produces({XXMediaType.TEXTUTF8})
    public Response saveProductToProduct(@BeanParam ProductToProductItemBean productToProductItemBean) {

        ResultBean resultBean = new ResultBean();
        resultBean.setStatus(ResultBean.OK);
        ReturnValue<ProductToProductItemBean> rv = productService.saveProductToProduct(productToProductItemBean);
        if (rv.getFlag() == 0) {
            resultBean.setStatus(ResultBean.OK);
            resultBean.setBody(rv.getObject());
        } else {
            resultBean.setStatus(ResultBean.ERROR);
        }

        return Response.ok(resultBean.toString()).build();

    }

    /**
     * 查询Tag
     *
     * @return
     */
    @Path("/queryTags")
    @GET
    @Produces({XXMediaType.TEXTUTF8})
    public Response queryTags() {
        List<TagItemBean> list = new ArrayList<TagItemBean>();
        for (int code = 1; code < 11; code++) {
            list.add(getTagItemBean(String.valueOf(code), "tag_" + code));
        }
        ResultBean resultBean = new ResultBean();
        resultBean.setStatus(ResultBean.OK);
        TagBean tagBean = new TagBean();
        tagBean.setTagBeans(list);
        resultBean.setBody(JSONObject.toJSONString(tagBean));
        return Response.ok(resultBean.toString()).build();
    }

    private TagItemBean getTagItemBean(String id, String name) {
        TagItemBean itemBean = new TagItemBean();
        itemBean.setId(id);
        itemBean.setName(name);
        itemBean.setStatus(0);
        return itemBean;
    }
}
