package cn.xxtui.support.session;

import java.util.HashMap;
import java.util.Map;

public class SessionControl {
	private Map<String,Map<String,Map<String,String>>> sessionMap=new HashMap<String,Map<String,Map<String,String>>>();
	private static SessionControl control=new SessionControl();
	public static SessionControl getInstance()
	{
		return control;
	}
	public void set(String identify,String key,Map.Entry<String, String> pairs)
	{

		Map<String,Map<String,String>> valueMap=null;
		if(sessionMap.containsKey(identify))
		{
			valueMap=sessionMap.get(identify);
		}
		else
		{
			valueMap=new HashMap<String,Map<String,String>>();
			sessionMap.put(identify, valueMap);
		}
		Map<String,String> values=valueMap.get(key);
		if(values==null)
		{
			values=new HashMap<String,String>();
			valueMap.put(key, values);
		}
		values.put(pairs.getKey(), pairs.getValue());
		
	}
	
	public Map<String,String> get(String identify,String key)
	{
		Map<String,Map<String,String>> valueMap=sessionMap.get(identify);
		if(valueMap==null)
		{
			return null;
		}
		else{
			return valueMap.get(key);
		}
	}
}
