package cn.lv.jewelry.product.service;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.com.ql.wiseBeijing.serviceUtil.PageMapUtil;
import cn.com.ql.wiseBeijing.serviceUtil.ReturnValue;
import cn.lv.jewelry.activity.dao.ActivityDao;
import cn.lv.jewelry.activity.dao.LvTagDao;
import cn.lv.jewelry.activity.daoBean.Activity;
import cn.lv.jewelry.activity.service.ActivityService;
import cn.lv.jewelry.brand.daoBean.Brand;
import cn.lv.jewelry.designer.dao.DesignerDao;
import cn.lv.jewelry.designer.daoBean.Designer;
import cn.lv.jewelry.designer.service.DesignerService;
import cn.lv.jewelry.index.indexActivity.frontBean.NodeBeanVideo;
import cn.lv.jewelry.index.indexDesigner.frontBean.DesignerItemBean;
import cn.lv.jewelry.index.indexProduct.frontBean.FProductCategoryItemBean;
import cn.lv.jewelry.index.indexProduct.frontBean.ProductImageBean;
import cn.lv.jewelry.index.indexProduct.frontBean.ProductItemBean;
import cn.lv.jewelry.index.indexProduct.frontBean.ProductRelativeActivityItemBean;
import cn.lv.jewelry.index.indexProduct.frontBean.ProductToProductItemBean;
import cn.lv.jewelry.index.indexProduct.frontBean.ProductTypeItemBean;
import cn.lv.jewelry.product.dao.*;
import cn.lv.jewelry.product.daoBean.*;
import cn.xxtui.support.page.Page;
import cn.xxtui.support.page.PageWrap;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Transactional
public class ProductService extends BasicDao<Product> {

    @Resource(name = "productDao")
    private ProductDao productDao;
    // TODO 下行是之前的，用到了brand表，2018-2-9号修改表设计需注释掉该代码
//    @Resource(name = "brandDao")
//    private BrandDao brandDao;
    @Resource(name = "designerDao")
    private DesignerDao designerDao;
    @Resource(name = "productCategoryDao")
    private ProductCategoryDao productCategoryDao;
    @Resource(name = "productEmbedDao")
    private ProductEmbedDao productEmbedDao;
    @Resource(name = "productImageDao")
    private ProductImageDao productImageDao;
    @Resource(name = "productMaterialDao")
    private ProductMaterialDao productMaterialDao;
    @Resource(name = "productOriginDao")
    private ProductOriginDao productOriginDao;
    @Resource(name = "productStyleDao")
    private ProductStyleDao productStyleDao;
    @Resource(name = "productDesignerDao")
    private ProductDesignerDao productDesignerDao;
    @Resource(name = "activityDao")
    private ActivityDao activityDao;
    @Resource(name = "productActivityDao")
    private ProductActivityDao productActivityDao;
    @Resource(name = "productTypeDao")
    private ProductTypeDao productTypeDao;
    @Resource(name = "productToProductDao")
    private ProductToProductDao productToProductDao;
    @Resource(name = "lvTagDao")
    private LvTagDao lvTagDao;

    @Resource(name = "activityService")
    private ActivityService activityService;

    public ReturnValue<ProductItemBean> saveProduct(ProductItemBean productBean) {
        ReturnValue<ProductItemBean> rv = new ReturnValue<ProductItemBean>();
        try {
            //TODO 可借和可租数据的插入逻辑
            Product product = getProduct(productBean);
            Integer i = 0;

            if (productDao.save(product) != i) {
                //添加tag
                for (String tag : productBean.getTags()) {
                    lvTagDao.save(product.getId(), tag, 2);
                }
                productBean.setId(String.valueOf(product.getId()));
                saveProductDesigner(productBean);
                rv.setObject(productBean);
                rv.setFlag(ReturnValue.FLAG_SUCCESS);
            }
        } catch (Exception ex) {
            rv.setFlag(ReturnValue.FLAG_EXCEPTION);
            ex.printStackTrace();
        }
        return rv;
    }

    public ReturnValue<ProductToProductItemBean> saveProductToProduct(ProductToProductItemBean productBean) {
        ReturnValue<ProductToProductItemBean> rv = new ReturnValue<>();
        try {
            ProductToProduct productToProduct = getProductToProduct(productBean);
            Integer i = 0;

            if (productToProduct != null && productToProductDao.save(productToProduct) != i) {

                productBean.setId(String.valueOf(productToProduct.getId()));
                rv.setObject(productBean);
                rv.setFlag(ReturnValue.FLAG_SUCCESS);
            }
        } catch (Exception ex) {
            rv.setFlag(ReturnValue.FLAG_EXCEPTION);
            rv.setObject(new ProductToProductItemBean());
            ex.printStackTrace();
        }
        return rv;
    }

    private ProductToProduct getProductToProduct(ProductToProductItemBean productBean) {
        ProductToProduct productToProduct = null;
        if (null != productBean) {
            String mid = productBean.getMid();
            String rid = productBean.getRid();
            if (null != mid && !"".equals(mid.trim()) && null != rid && !"".equals(rid.trim())) {
                productToProduct = new ProductToProduct();
                productToProduct.setMid(Integer.parseInt(mid));
                productToProduct.setRid(Integer.parseInt(rid));
                productToProduct.setStatus(Integer.parseInt(productBean.getStatus()));

            }
        }
        return productToProduct;
    }

    public ReturnValue<DesignerItemBean> saveProductDesigner(ProductItemBean productItemBean) {
        ReturnValue<DesignerItemBean> rv = new ReturnValue<DesignerItemBean>();
        ProductDesigner productDesigner = new ProductDesigner();
        productDesigner.setDid(Long.parseLong(productItemBean.getDesigner()));
        productDesigner.setPid(Long.parseLong(productItemBean.getId()));
        productDesigner.setStatus(0);
        productDesignerDao.save(productDesigner);
        Designer designer = designerDao.get(Designer.class, Long.parseLong(productItemBean.getDesigner()));
        DesignerItemBean designerItemBean = new DesignerItemBean();
        designerItemBean.setName(designer.getName());
        designerItemBean.setId(String.valueOf(designer.getId()));
        designerItemBean.setAvatar_url(designer.getAvatar());
        rv.setFlag(ReturnValue.FLAG_SUCCESS);
        rv.setObject(designerItemBean);
        return rv;
    }

    private Product getProduct(ProductItemBean productItemBean) {
        if (null == productItemBean) {
            return null;
        }
        Product product = new Product();
        product.setDescription(productItemBean.getDescription());
        // TODO 下行是之前的，用到了brand表，2018-2-9号修改表设计需注释掉该代码
//        product.setBrand(brandDao.get(Brand.class, Long.parseLong(productItemBean.getBrand())));
        product.setCategory(productCategoryDao.get(ProductCategory.class, Long.parseLong(productItemBean.getCategory())));
        product.setCoverUrl(productItemBean.getCoverUrl());
        product.setEmbed(productEmbedDao.get(ProductEmbed.class, Long.parseLong(productItemBean.getEmbed())));
        product.setIsRent(Integer.parseInt(productItemBean.getIsRent()));
        product.setLength(productItemBean.getLength());
        product.setMaterial(productMaterialDao.get(ProductMaterial.class, Long.parseLong(productItemBean.getMaterial())));
        product.setName(productItemBean.getName());
        product.setOrigin(productOriginDao.get(ProductOrigin.class, Long.parseLong(productItemBean.getOrigin())));
        product.setPostscript(productItemBean.getPostscript());
        product.setPrice(Double.parseDouble(productItemBean.getPrice()));
        product.setSize(productItemBean.getSize());
        product.setStatus(Integer.parseInt(productItemBean.getStatus()));
        product.setStyle(productStyleDao.get(ProductStyle.class, Long.parseLong(productItemBean.getStyle())));
        product.setType(Integer.parseInt(productItemBean.getType()));
        //TODO
        product.setRentCount(0);
        product.setBuyCount(0);

        return product;
    }

    /**
     * 单个商品详情
     *
     * @param id
     * @return
     */
    public ProductItemBean queryJewelrys(String id) {
        List<ProductItemBean> productItemBeans = new ArrayList<ProductItemBean>(1);
        Product product = null;
        try {
            long productType = Long.parseLong(id);
            product = productDao.get(Product.class, productType);
            productItemBeans.add(getProductItemBeanRelativeProduct(product));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productItemBeans.size() == 1 ? productItemBeans.get(0) : null;
    }

    /**
     * 商品列表
     *
     * @param type
     * @param page
     * @return
     */
    public ReturnValue<Map<String, Object>> queryJewelrys(String type, String page) {
        ReturnValue<Map<String, Object>> rb = new ReturnValue();
        rb.setFlag(ReturnValue.FLAG_FAIL);
        Page<Product> pageResult = null;
        List<ProductItemBean> productItemBeans = null;
        //PageWrap<ProductItemBean> p = new PageWrap<>();

        Page<Product> products = null;
        try {
            int productPage = Integer.parseInt(page);
            if ("all".equals(type.trim())) {
                //查询所有商品
                products = productDao.getProduct(productPage);
                productItemBeans = getProductItemBeans(products.getPageContent());
            } else {
                int productType = Integer.parseInt(type);
                products = productDao.getProduct(productType, productPage);
                productItemBeans = getProductItemBeans(products.getPageContent());
            }
            Map<String,Object> map=PageMapUtil.getMap(products);
            map.put("data",productItemBeans);
            rb.setObject(map);
            rb.setFlag(ReturnValue.FLAG_SUCCESS);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rb;
    }

    private List<ProductItemBean> getProductItemBeans(List<Product> products) {
        List<ProductItemBean> productItemBeans = new ArrayList<>();
        if (null == products || products.size() < 1) {
            return productItemBeans;
        }
        for (Product product : products) {
            productItemBeans.add(getProductItemBeanRelativeProduct(product));
        }
        return productItemBeans;
    }

    private ProductItemBean getProductItemBeanRelativeProduct(Product product) {
        ProductItemBean productItemBean = getProductItemBean(product);
        List<ProductItemBean> productItemBeans = new ArrayList<>();
        for (Product product1 : product.getProducts()) {
            productItemBeans.add(getProductItemBean(product1));
        }
        productItemBean.setRelativeProductItemBeans(productItemBeans);

        return productItemBean;
    }

    private ProductItemBean getProductItemBean(Product product) {
        ProductItemBean productItemBean = new ProductItemBean();

        productItemBean.setPostscript(product.getPostscript());
        productItemBean.setSize(product.getSize());
        productItemBean.setStatus(String.valueOf(product.getStatus()));
        productItemBean.setProductStyle(product.getStyle());
        // TODO 以下方法是之前的，用到了brand表，2018-2-9号修改表设计需注释掉该代码
//        productItemBean.setBrandObject(product.getBrand());
        productItemBean.setProductCategory(product.getCategory());
        productItemBean.setCoverUrl(product.getCoverUrl());
        productItemBean.setProductMaterial(product.getMaterial());
        productItemBean.setDescription(product.getDescription());
        productItemBean.setProductEmbed(product.getEmbed());
        productItemBean.setId(String.valueOf(product.getId()));
        productItemBean.setIsRent(String.valueOf(product.getIsRent()));
        productItemBean.setLength(product.getLength());
        productItemBean.setName(product.getName());
        productItemBean.setProductOrigin(product.getOrigin());
        productItemBean.setPrice(String.valueOf(product.getPrice()));
        productItemBean.setType(String.valueOf(product.getType()));
        productItemBean.setProductDesigners(DesignerService.getDesignerItemBeans(product.getDesigners()));
        productItemBean.setProductImageBeans(getProductImageBeans(product.getImages()));

        productItemBean.setProductVideos(getProductNodeBeanVideos(product.getProductVideos()));
        //productItemBean.setProductActivityBeans(activityService.getActivityBean(productDao.getRelativeActivities(product.getId())));

        return productItemBean;
    }

    private List<NodeBeanVideo> getProductNodeBeanVideos(List<ProductVideo> productVideos) {
        List<NodeBeanVideo> nodeBeanVideos = new ArrayList<>();
        if (null == productVideos || productVideos.size() < 1) {
            return nodeBeanVideos;
        }
        NodeBeanVideo nodeBeanVideo = null;
        for (ProductVideo productVideo : productVideos) {
            nodeBeanVideo = new NodeBeanVideo();
            nodeBeanVideo.setId(String.valueOf(productVideo.getId()));
            nodeBeanVideo.setImg(productVideo.getImg());
            nodeBeanVideo.setPath(productVideo.getVideo());
            nodeBeanVideo.setStatus(String.valueOf(productVideo.getStatus()));

            nodeBeanVideos.add(nodeBeanVideo);
        }
        return nodeBeanVideos;
    }

    private List<ProductImageBean> getProductImageBeans(List<ProductImage> images) {
        List<ProductImageBean> productImageBeans = new ArrayList<>();
        ProductImageBean productImageBean = null;
        for (ProductImage productImage : images) {
            productImageBean = new ProductImageBean();
            productImageBean.setType(String.valueOf(productImage.getType()));
            productImageBean.setId(String.valueOf(productImage.getId()));
            productImageBean.setRid(String.valueOf(productImage.getPid().getId()));
            productImageBean.setPath(productImage.getContent());
            //TODO 信息不完整
            productImageBeans.add(productImageBean);
        }
        return productImageBeans;
    }

    // TODO 下方法是之前的，用到了brand表，2018-2-9号修改表设计需注释掉该代码
    /**
     * 查询品牌
     *
     * @param type
     * @param page
     * @return
     */
//    public List<BrandItemBean> queryBrands(String type, String page) {
//        List<BrandItemBean> brandItemBean = null;
//        List<Brand> brands = null;
//        try {
//            int brandType = Integer.parseInt(type);
//            int brandPage = Integer.parseInt(page);
//            brands = brandDao.getBrand(brandType, brandPage);
//            brandItemBean = getBrandItemBean(brands);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return brandItemBean;
//    }

    /**
     * 根据品牌查询商品
     *
     * @param brand
     * @return
     */
    public List<ProductItemBean> queryProductByBrand(Brand brand) {
        List<ProductItemBean> productItemBeans = null;
        List<Product> products = null;
        try {
            //查询所有商品
            products = productDao.getProductBrand(brand,1);
            productItemBeans = getProductItemBeans(products);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productItemBeans;
    }

    // TODO 下方法是之前的，用到了brand表，2018-2-9号修改表设计需注释掉该代码
//    private List<BrandItemBean> getBrandItemBean(List<Brand> brands) {
//        List<BrandItemBean> brandItemBeans = new ArrayList<BrandItemBean>();
//        BrandItemBean brandItemBean = null;
//        if (null == brands || brands.size() < 1) {
//            return brandItemBeans;
//        }
//        for (Brand brand : brands) {
//            brandItemBean = new BrandItemBean();
//            brandItemBean.setId(String.valueOf(brand.getId()));
//            brandItemBean.setType(String.valueOf(brand.getType()));
//            brandItemBean.setDescription(brand.getDescription());
//            brandItemBean.setName(brand.getName());
//            brandItemBean.setLogo(brand.getLogo());
//            brandItemBean.setStatus(brand.getStatus());
//            brandItemBean.setProductItemBeans(queryProductByBrand(brand));
//
//            brandItemBeans.add(brandItemBean);
//        }
//        return brandItemBeans;
//    }

    public List<DesignerItemBean> queryDesigner(String type, String page) {
        List<DesignerItemBean> desingerItemBean = null;
        List<Designer> designer = null;
        try {
            int designerType = Integer.parseInt(type);
            int designerPage = Integer.parseInt(page);
            designer = designerDao.getDesigner(designerType, designerPage);
            desingerItemBean = getDesignerItemBean(designer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return desingerItemBean;
    }

    /**
     * 获取单个设计师
     * @param designer_id
     * @return
     */
    public DesignerItemBean querySingleDesigner(int designer_id) {
        Designer designer = null;
        DesignerItemBean desingerItemBean=null;
        try {
            designer = designerDao.getDesigner(designer_id);
            desingerItemBean = getDesignerItemBean(designer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return desingerItemBean;
    }

    private List<DesignerItemBean> getDesignerItemBean(List<Designer> designers) {
        List<DesignerItemBean> designerItemBeans = new ArrayList<DesignerItemBean>();
        DesignerItemBean designerItemBean = null;
        if (null == designers || designers.size() < 1) {
            return designerItemBeans;
        }
        for (Designer designer : designers) {
            designerItemBean = getDesignerItemBean(designer);
            designerItemBeans.add(designerItemBean);
        }
        return designerItemBeans;
    }

    private DesignerItemBean getDesignerItemBean(Designer designer) {
        DesignerItemBean designerItemBean;
        designerItemBean = new DesignerItemBean();
        designerItemBean.setName(designer.getName());
        designerItemBean.setDescription(designer.getDescription());
        designerItemBean.setId(String.valueOf(designer.getId()));
        designerItemBean.setAvatar_url(designer.getAvatar());
//            designerItemBean.setBrand(designer.getBrand());
        designerItemBean.setNation(designer.getNation());
        designerItemBean.setStatus(String.valueOf(designer.getStatus()));
        designerItemBean.setStory(designer.getStory());
        designerItemBean.setStyle(designer.getStory());
        designerItemBean.setVerify_info(designer.getComposition());
        long did = designer.getId();
        Page<ProductDesigner> page = productDesignerDao.productDesigner(did, 1);
        List<ProductDesigner> productDesignerList = page.getPageContent();
        List<ProductItemBean> list = new ArrayList<>();
        for (ProductDesigner productDesigner : productDesignerList) {
            Product product = productDao.get(Product.class, productDesigner.getPid());
            ProductItemBean productItemBean = getProductItemBean(product);
            productItemBean.setDesigner(null);
            productItemBean.setProductActivityBeans(null);
            productItemBean.setProductVideos(null);
            productItemBean.setRelativeProductItemBeans(null);
            list.add(productItemBean);

        }
        designerItemBean.setProducts(list);
        return designerItemBean;
    }

    public ReturnValue<ProductImageBean> saveProductImage(ProductImageBean productImageBean) {
        ReturnValue<ProductImageBean> rv = new ReturnValue<>();
        ProductImage productImage = new ProductImage();
        productImage.setType(Integer.parseInt(productImageBean.getType()));
        productImage.setContent(productImageBean.getPath());
        productImage.setExtend(productImageBean.getDescription());
        try {

            Product product = productDao.get(Product.class, Long.parseLong(productImageBean.getRid()));
            if (product == null)
                throw new RuntimeException("object is not exists " + productImageBean.getRid());
            productImage.setPid(product);
            productImageDao.save(productImage);
            rv.setObject(productImageBean);
            rv.setFlag(ReturnValue.FLAG_SUCCESS);
        } catch (Exception ex) {
            ex.printStackTrace();
            rv.setMeg(ex.getMessage());
            rv.setFlag(ReturnValue.FLAG_FAIL);
        }
        return rv;
    }

    public List<ProductRelativeActivityItemBean> queryProductRelativeActivity(String pid) {
        long pidType = Long.parseLong(pid);
        List<ProductActivity> productActivities = productActivityDao.getProductRelativeActivity(pidType);
        List<ProductRelativeActivityItemBean> productRelativeActivityItemBeans = new ArrayList<>();
        for (ProductActivity productActivity : productActivities) {
            Activity activity = activityDao.get(Activity.class, productActivity.getAid());
            productRelativeActivityItemBeans.add(getProductRelativeActivityItemBean(activity));
        }

        return productRelativeActivityItemBeans;
    }

    private ProductRelativeActivityItemBean getProductRelativeActivityItemBean(Activity activity) {
        ProductRelativeActivityItemBean productRelativeActivityItemBean = new ProductRelativeActivityItemBean();
        productRelativeActivityItemBean.setId(String.valueOf(activity.getId()));
        productRelativeActivityItemBean.setHeadPic(activity.getIndexPic());
        productRelativeActivityItemBean.setSubject(activity.getSubject());

        return productRelativeActivityItemBean;
    }

    public List<FProductCategoryItemBean> queryCategory(String page) {
        //TODO pageSize
        List<ProductCategory> productCategories = productCategoryDao.getCategory(1);

        return getProductCategoryItemBeans(productCategories);
    }

    private List<FProductCategoryItemBean> getProductCategoryItemBeans(List<ProductCategory> productCategories) {
        List<FProductCategoryItemBean> productCategoryItemBeans = new ArrayList<>();
        if (null == productCategories || productCategories.size() < 1) {
            return productCategoryItemBeans;
        }
        FProductCategoryItemBean productCategoryItemBean = null;
        for (ProductCategory productCategory : productCategories) {
            productCategoryItemBean = new FProductCategoryItemBean();
            productCategoryItemBean.setName(productCategory.getName());
            productCategoryItemBean.setId(String.valueOf(productCategory.getId()));
            productCategoryItemBean.setStatus(productCategory.getStatus());

            productCategoryItemBeans.add(productCategoryItemBean);
        }
        return productCategoryItemBeans;
    }

    public List<ProductEmbedItemBean> queryProductEmbedBeans(String page) {
        //TODO pageSize
        List<ProductEmbed> productEmbeds = productEmbedDao.getEmbeds(1);
        List<ProductEmbedItemBean> productEmbedItemBeans = new ArrayList<>();
        for (ProductEmbed productEmbed : productEmbeds) {
            productEmbedItemBeans.add(new ProductEmbedItemBean(productEmbed));
        }

        return productEmbedItemBeans;
    }

    public List<ProductMaterialItemBean> queryProductMaterialBeans(String page) {
        //TODO pageSize
        List<ProductMaterial> productMaterials = productMaterialDao.getMaterials(1);
        List<ProductMaterialItemBean> productMaterialItemBeans = new ArrayList<>();
        for (ProductMaterial productMaterial : productMaterials) {
            productMaterialItemBeans.add(new ProductMaterialItemBean(productMaterial));
        }

        return productMaterialItemBeans;
    }

    public List<ProductOriginItemBean> queryProductOriginBeans(String page) {
        //TODO pageSize
        List<ProductOrigin> productMaterials = productOriginDao.getOrigins(1);
        List<ProductOriginItemBean> productOriginBeans = new ArrayList<>();
        for (ProductOrigin productOrigin : productMaterials) {
            productOriginBeans.add(new ProductOriginItemBean(productOrigin));
        }

        return productOriginBeans;
    }

    public List<ProductStyleItemBean> queryProductStyleBeans(String page) {
        //TODO pageSize
        List<ProductStyle> productMaterials = productStyleDao.getStyles(1);
        List<ProductStyleItemBean> productStyleItemBeans = new ArrayList<>();
        for (ProductStyle productStyle : productMaterials) {
            productStyleItemBeans.add(new ProductStyleItemBean(productStyle));
        }

        return productStyleItemBeans;
    }

    public List<ProductTypeItemBean> queryProductType() {
        List<ProductType> productTypes = productTypeDao.getProductType();
        List<ProductTypeItemBean> productTypeItemBeans = getProductTypeItemBeans(productTypes);

        return productTypeItemBeans;
    }

    private List<ProductTypeItemBean> getProductTypeItemBeans(List<ProductType> productTypes) {
        List<ProductTypeItemBean> productTypeItemBeans = new ArrayList<>();
        if (null == productTypes || productTypes.size() < 1) {
            return productTypeItemBeans;
        }
        ProductTypeItemBean productTypeItemBean = null;
        for (ProductType productType : productTypes) {
            productTypeItemBean = new ProductTypeItemBean();
            productTypeItemBean.setId(String.valueOf(productType.getId()));
            productTypeItemBean.setName(productType.getName());
            productTypeItemBean.setStatus(String.valueOf(productType.getStatus()));
            productTypeItemBeans.add(productTypeItemBean);
        }

        return productTypeItemBeans;
    }

    public ReturnValue<Map<String, Object>> queryProductByName(String page, String name) {
        ReturnValue<Map<String, Object>> rb = new ReturnValue();
        rb.setFlag(ReturnValue.FLAG_FAIL);
        Page<Product> pageResult = null;
        List<ProductItemBean> productItemBeans = null;
        PageWrap<ProductItemBean> p = new PageWrap<>();

        try {
            int pageNum = Integer.parseInt(page);
            pageResult = productDao.getProductByName(pageNum, name);
            productItemBeans = getProductItemBeans(pageResult.getPageContent());

            p.setHasNextPage(pageResult.isHasNextPage());
            p.setList(productItemBeans);
            p.setCount(pageResult.getCount());
            p.setCurrent(pageResult.getCurrent());
            rb.setObject(PageMapUtil.getMap(p));
            rb.setFlag(ReturnValue.FLAG_SUCCESS);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rb;
    }
}
