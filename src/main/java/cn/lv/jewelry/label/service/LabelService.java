package cn.lv.jewelry.label.service;

import cn.com.ql.wiseBeijing.dao.BasicDao;
import cn.lv.jewelry.label.daobean.Label;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 24593 on 2018/2/10.
 */
@Component
@Transactional
public class LabelService extends BasicDao<Label> {
}
