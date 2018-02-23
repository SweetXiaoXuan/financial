package cn.lv.jewelry.product.dao;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.product.daoBean.ProductDesigner;
import cn.xxtui.support.page.Page;
import cn.xxtui.support.page.PageWaterfallAdapter;
import cn.xxtui.support.page.ResultTransfer;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by lixing on 16/5/5.
 */
@Component
public class ProductDesignerDao extends BasicDao<ProductDesigner> {
    @Bean
    public ProductDesignerDao productDesignerDao() {

        return new ProductDesignerDao();
    }

    public Page<ProductDesigner> productDesigner(long did, int page) {
        Session session = getSession();
        String hql = "from ProductDesigner where did =:did";
        Query query = session.createQuery(hql);
        query.setLong("did", did);
        PageWaterfallAdapter pageWaterfallAdapter = new PageWaterfallAdapter(query);
        Page<ProductDesigner> pages = pageWaterfallAdapter.execute(page, 20, new ResultTransfer<ProductDesigner>() {
            @Override
            public List<ProductDesigner> transfer(List list) {

                return list;
            }
        });
        return pages;
    }
}
