package cn.lv.jewelry.brand.service;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.com.ql.wiseBeijing.serviceUtil.ReturnValue;
import cn.com.ql.wiseBeijing.serviceUtil.StringUtil;
import cn.lv.jewelry.brand.dao.BrandCompanyDao;
import cn.lv.jewelry.brand.dao.BrandDao;
import cn.lv.jewelry.brand.dao.BrandPersonalDao;
import cn.lv.jewelry.brand.daoBean.Brand;
import cn.lv.jewelry.brand.daoBean.BrandCompany;
import cn.lv.jewelry.brand.daoBean.BrandPersonal;
import cn.lv.jewelry.brand.daoBean.BrandStatus;
import cn.lv.jewelry.index.indexBrand.frontBean.FBrandBean;
import cn.xxtui.support.util.MeaasgeUtil;
import cn.xxtui.support.util.ResultMsgConstant;
import cn.xxtui.support.util.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Component
@Transactional
public class BrandService extends BasicDao<Brand> {
    private final static Logger logger = LoggerFactory.getLogger(BrandService.class);
    private MeaasgeUtil me = new MeaasgeUtil();
    @Resource(name = "brandDao")
    private BrandDao brandDao;
    @Resource(name = "brandCompanyDao")
    private BrandCompanyDao brandCompanyDao;
    @Resource(name = "brandPersonalDao")
    private BrandPersonalDao brandPersonalDao;

    /**
     * 保存品牌方公共信息
     * @param fBrandBean 相关参数
     * @param type 类型 1企业 2个人
     * @return Boolean
     * @author liumengwei
     * @Date 2018/2/10
     */
    public Brand saveBrand(FBrandBean fBrandBean, Integer type) {
        Brand brand = new Brand();
        try {
            brand.setAddress(fBrandBean.getAddress());
            brand.setBrandName(fBrandBean.getBrandName());
            brand.setBrandPic(fBrandBean.getBrandPic());
            brand.setDescription(fBrandBean.getDescription());
            brand.setCreateTime(String.valueOf(System.currentTimeMillis() / 1000));
            brand.setType(type);
            brand.setStatus(BrandStatus.NORMAL.getStatus());
            brand.setEmail(fBrandBean.getEmail());
            brand.setMobile(fBrandBean.getPhone());
            String brandId = brandDao.save(brand).toString();
            if (StringUtil.isEmpty(brandId)) {
                return null;
            }
            brand.setId(Long.parseLong(brandId));
            return brand;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return brand;
    }

    /**
     * 添加企业品牌方注册信息
     * @param fBrandBean 相关参数
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     * @author liumengwei
     * @Date 2018/2/10
     */
    @Transactional
    public ReturnValue brandCompanyRegist(FBrandBean fBrandBean) {
        ReturnValue rv = new ReturnValue<>();
        try {
            Brand brand = saveBrand(fBrandBean, 1);
            if (brand == null) {
                rv.setObject(null);
                rv.setFlag(ReturnValue.FLAG_FAIL);
                rv.setMeg(me.getValue(ResultMsgConstant.registFailed));
            }
            BrandCompany company = new BrandCompany();
            company.setBrandId(brand);
            company.setCompanyName(fBrandBean.getCompanyName());
            company.setBusinessLicensePic(fBrandBean.getBusinessLicensePic());
            company.setTelephone(fBrandBean.getTelephone());
            company.setContact(fBrandBean.getContact());
            company.setCreateTime(String.valueOf(System.currentTimeMillis() / 1000));
            String brandCompanyId = brandCompanyDao.save(company).toString();
            Boolean emptyResult = StringUtil.isEmpty(brandCompanyId);
            rv.setObject(emptyResult ? "null" : company);
            rv.setFlag(emptyResult ? ReturnValue.FLAG_FAIL : ReturnValue.FLAG_SUCCESS);
            rv.setMeg(emptyResult? me.getValue(ResultMsgConstant.registFailed) : me.getValue(ResultMsgConstant.registSuccess));
        } catch (Exception ex) {
            return SystemException.setResult(rv, ex, logger);
        }
        return rv;
    }

    /**
     * 添加个人品牌方注册信息
     * @param fBrandBean 相关参数
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     * @author liumengwei
     * @Date 2018/2/10
     */
    @Transactional
    public ReturnValue brandPersonalRegist(FBrandBean fBrandBean) {
        ReturnValue rv = new ReturnValue<>();
        try {
            Brand brand = saveBrand(fBrandBean, 1);
            if (brand == null) {
                rv.setObject(null);
                rv.setFlag(ReturnValue.FLAG_FAIL);
                rv.setMeg(me.getValue(ResultMsgConstant.registFailed));
            }
            BrandPersonal personal = new BrandPersonal();
            personal.setBrandId(brand);
            personal.setGivename(fBrandBean.getGivename());
            personal.setIdNumber(fBrandBean.getIdNumber());
            personal.setIdCardBack(fBrandBean.getIdCardBack());
            personal.setIdCardFront(fBrandBean.getIdCardFront());
            personal.setIdCardInHand(fBrandBean.getIdCardInHand());
            personal.setCreateTime(String.valueOf(System.currentTimeMillis() / 1000));
            String brandCompanyId = brandPersonalDao.save(personal).toString();
            Boolean emptyResult = StringUtil.isEmpty(brandCompanyId);
            rv.setObject(emptyResult ? "null" : personal);
            rv.setFlag(emptyResult ? ReturnValue.FLAG_FAIL : ReturnValue.FLAG_SUCCESS);
            rv.setMeg(emptyResult? me.getValue(ResultMsgConstant.registFailed) : me.getValue(ResultMsgConstant.registSuccess));
        } catch (Exception ex) {
            return SystemException.setResult(rv, ex, logger);
        }
        return rv;
    }

    /**
     * 品牌方登陆
     * @param fBrandBean 相关参数
     * @return cn.com.ql.wiseBeijing.serviceUtil.ReturnValue
     * @author liumengwei
     * @Date 2018/2/10
     */
    @Transactional
    public ReturnValue brandCompanyLogin(FBrandBean fBrandBean) {
        ReturnValue rv = new ReturnValue<>();
        try {


            rv.setObject(null);
            rv.setFlag(ReturnValue.FLAG_SUCCESS);
            rv.setMeg(me.getValue(ResultMsgConstant.addSuccess));
        } catch (Exception ex) {
            return SystemException.setResult(rv, ex, logger);
        }
        return rv;
    }


}
