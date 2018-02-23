package cn.xxtui.support.util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

/**
 * 将整个json生成一个Map
 * 
 * @author starlee
 * 
 */
public class JsonToMap {
	public class MapValue {
		private JsonTypeEnum type;
		private Object object;

		public JsonTypeEnum getType() {
			return type;
		}

		public void setType(JsonTypeEnum type) {
			this.type = type;
		}

		public Object getObject() {
			return object;
		}

		public void setObject(Object object) {
			this.object = object;
		}

	}

	private JsonParser jsonParser;

	public JsonToMap(String str) {
		this.jsonParser = Json.createParser(new StringReader(str));
	}

	public List<Map<String, MapValue>> getMapList() {
		List<Map<String, MapValue>> list =new ArrayList<Map<String, MapValue>>();
		int flag=0;
		while(jsonParser.hasNext())
		{
			if(flag==1)
			{
				break;
			}
			Event ev=jsonParser.next();
			switch (ev) {
			case START_OBJECT:
				list.add(getMap());
				break;
			case END_ARRAY:
				flag=1;
				break;
			default:
				break;
			}
		}
		return list;
	}
	
	public Map<String, MapValue> getMap() {
		Map<String, MapValue> map = new HashMap<String, MapValue>();
		int flag = 0;
		while (jsonParser.hasNext()) {
			if (flag == 1)
				break;
			Event ev = jsonParser.next();
			switch (ev) {
			case START_OBJECT:
				break;
			case KEY_NAME:
				String key=jsonParser.getString();
				Event e = jsonParser.next();
				if(e==Event.START_OBJECT)
				{
					MapValue value=new MapValue();
					value.setType(JsonTypeEnum.OBJECT);
					value.setObject(getMap());
					map.put(key, value);
				}
				if(e==Event.VALUE_STRING)
				{
					MapValue value=new MapValue();
					value.setType(JsonTypeEnum.STRING);
					value.setObject(jsonParser.getString());
					map.put(key, value);
				}
				if(e==Event.VALUE_NUMBER)
				{
					MapValue value=new MapValue();
					value.setType(JsonTypeEnum.INT);
					value.setObject(jsonParser.getInt());
					map.put(key, value);
				}
				if(e==Event.START_ARRAY)
				{
					MapValue value=new MapValue();
					value.setType(JsonTypeEnum.LIST);
					value.setObject(getMapList());
					map.put(key, value);
				}
				break;
			case END_OBJECT:
				flag = 1;
				break;
			default:
				break;
			}
		}
		return map;
	}
}
