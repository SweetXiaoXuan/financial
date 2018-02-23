package cn.lv.jewelry.designer.dao;

import cn.xxtui.support.page.Page;
import cn.xxtui.support.page.PageWaterfallAdapter;
import cn.xxtui.support.page.ResultTransfer;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.designer.daoBean.Designer;

import java.util.List;

@Component
public class DesignerDao extends BasicDao<Designer>{

	@Bean
    public DesignerDao desingerDao() {
        return new DesignerDao();
    }

    /**
     * 根据设计师类型得到设计师列表
     * @param designerType
     * @param designerPage
     * @return
     */
    public List<Designer> getDesigner(int designerType, int designerPage) {
        Session session = getSession();
        String hql = "from cn.lv.jewelry.designer.daoBean.Designer b where b.type=:type";
        Query query = session.createQuery(hql);
        query.setInteger("type",designerType);
        PageWaterfallAdapter pageWaterfallAdapter = new PageWaterfallAdapter(query);
        Page<Designer> pageBean = pageWaterfallAdapter.execute(designerPage, 10, new ResultTransfer<Designer>() {
            @Override
            public List<Designer> transfer(List list) {
                return list;
            }
        });
        return pageBean.getPageContent();
    }

    /**
     * 根据设计师ID,得到设计师
     * @param designerID
     * @return
     */
    public Designer getDesigner(int designerID)
    {
        Session session=getSession();
        String hql="from cn.lv.jewelry.designer.daoBean.Designer b where b.id=:designerID";
        Query query=session.createQuery(hql);
        query.setInteger("designerID",designerID);
        return (Designer) query.uniqueResult();
    }
}
