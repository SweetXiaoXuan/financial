package cn.lv.jewelry.product.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.product.daoBean.ProductMaterial;
import cn.xxtui.support.page.Page;
import cn.xxtui.support.page.PageWaterfallAdapter;
import cn.xxtui.support.page.ResultTransfer;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMaterialDao extends BasicDao<ProductMaterial> {
    @Bean
    public ProductMaterialDao productMaterialDao() {
        return new ProductMaterialDao();
    }

    public List<ProductMaterial> getMaterials(int pageNum) {
        Session session = getSession();
        String hql = "from cn.lv.jewelry.product.daoBean.ProductMaterial";
        Query query = session.createQuery(hql);
        PageWaterfallAdapter pageWaterfallAdapter = new PageWaterfallAdapter(query);
        Page<ProductMaterial> pageBean = pageWaterfallAdapter.execute(pageNum, 50, new ResultTransfer<ProductMaterial>() {
            @Override
            public List<ProductMaterial> transfer(List list) {
                return list;
            }
        });
        return pageBean.getPageContent();
    }

}
