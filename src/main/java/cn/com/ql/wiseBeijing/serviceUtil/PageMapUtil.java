package cn.com.ql.wiseBeijing.serviceUtil;

import java.util.HashMap;
import java.util.Map;

import cn.xxtui.support.page.Page;

public class PageMapUtil {
	/**
	 * 将page转成map
	 * @param page
	 * @return
	 */
	public static Map<String,Object> getMap(Page page)
	{
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("data", page.getPageContent());
		map.put("hasNext", page.isHasNextPage());
		return map;
	}

}
