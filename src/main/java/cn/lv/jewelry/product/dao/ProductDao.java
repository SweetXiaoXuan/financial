package cn.lv.jewelry.product.dao;

import cn.lv.jewelry.activity.dao.ActivityDao;
import cn.lv.jewelry.activity.daoBean.Activity;
import cn.lv.jewelry.activity.daoBean.RelativeProduct;
import cn.lv.jewelry.brand.daoBean.Brand;
import cn.xxtui.support.page.Page;
import cn.xxtui.support.page.PageWaterfallAdapter;
import cn.xxtui.support.page.ResultTransfer;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.product.daoBean.Product;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductDao extends BasicDao<Product> {
    @Bean
    public ProductDao productDao() {
        return new ProductDao();
    }

    @Resource(name="activityDao")
    private ActivityDao activityDao;

    public Product getProductToAid(long id) {
        Product product = null;
        try {
            Session session = getSession();
            String hql = "from cn.lv.jewelry.product.daoBean.Product p where p.id=:id";
            Query query = session.createQuery(hql);
            query.setLong("id", id);
            product = (Product) query.list().get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return product;
    }

    public Page<Product> getProduct(int productType, int productPage) {
        Session session = getSession();
        String hql = "from cn.lv.jewelry.product.daoBean.Product p where p.type=:type ORDER BY p.id DESC";
        Query query = session.createQuery(hql);
        query.setInteger("type", productType);
        PageWaterfallAdapter pageWaterfallAdapter = new PageWaterfallAdapter(query);
        Page<Product> pageBean = pageWaterfallAdapter.execute(productPage, 10, new ResultTransfer<Product>() {
            @Override
            public List<Product> transfer(List list) {
                return list;
            }
        });
        return pageBean;
    }

    public Page<Product> getProduct(int productPage) {
        Session session = getSession();
        String hql = "from cn.lv.jewelry.product.daoBean.Product p ORDER BY p.id DESC";
        Query query = session.createQuery(hql);
        PageWaterfallAdapter pageWaterfallAdapter = new PageWaterfallAdapter(query);
        Page<Product> pageBean = pageWaterfallAdapter.execute(productPage, 10, new ResultTransfer<Product>() {
            @Override
            public List<Product> transfer(List list) {
                return list;
            }
        });
        return pageBean;
    }

    public List<Product> getProductBrand(Brand brand, int productPage) {
        Session session = getSession();
        String hql = "from cn.lv.jewelry.product.daoBean.Product p where brand=:brand ORDER BY p.id DESC";
        Query query = session.createQuery(hql);
        query.setEntity("brand",brand);
        PageWaterfallAdapter pageWaterfallAdapter = new PageWaterfallAdapter(query);
        Page<Product> pageBean = pageWaterfallAdapter.execute(productPage, 10, new ResultTransfer<Product>() {
            @Override
            public List<Product> transfer(List list) {
                return list;
            }
        });
        return pageBean.getPageContent();
    }

    /**
     * 获取相关活动
     * @param pid
     * @return
     */
    public  List<Activity> getRelativeActivities(long pid)
    {
        Session session=getSession();
        String hql = "FROM cn.lv.jewelry.activity.daoBean.RelativeProduct r  where r.pid=:pid";

        List<RelativeProduct> relativeProducts = session.createQuery(hql).setLong("pid", pid).setFirstResult(0).setMaxResults(10).list();
        List<Activity> activities=new ArrayList<>();
        for(RelativeProduct relativeProduct:relativeProducts)
        {
            Activity activity=activityDao.get(Activity.class,relativeProduct.getAid());
            activities.add(activity);
        }
        return activities;
    }

    public Page<Product> getProductByName(int pageNum, String name) {
        try {
            name = new String(name.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Session session = getSession();
        String hql = "from cn.lv.jewelry.product.daoBean.Product p where p.name like :name ORDER BY p.id DESC";
        Query query = session.createQuery(hql);
        query.setString("name", "%" + name + "%");
        PageWaterfallAdapter pageWaterfallAdapter = new PageWaterfallAdapter(query);
        Page<Product> pageBean = pageWaterfallAdapter.execute(pageNum, 10, new ResultTransfer<Product>() {
            @Override
            public List<Product> transfer(List list) {
                return list;
            }
        });

        return pageBean;
    }

}