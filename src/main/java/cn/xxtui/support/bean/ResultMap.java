package cn.xxtui.support.bean;

import java.util.Map;

/**
 * 针对于ResultBean的Body是Map的情况
 * @author starlee
 *
 */
public class ResultMap {
	private Map<String,Object> map;

	
	public Map<String,Object> getMap() {
		return map;
	}
	/**
	 * Map的Value不能用集合类，只能是对象
	 * @return
	 */
	public void setMap(Map<String,Object> map) {
		this.map = map;
	}
	
}
