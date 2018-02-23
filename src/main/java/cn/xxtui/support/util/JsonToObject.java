package cn.xxtui.support.util;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
/**
 * 将一个json生成指定的对象，如果是规范化的话
 * @author starlee
 *
 */
public class JsonToObject {
	private String json;
	private JsonParser jsonParser;

	public JsonToObject(String json) {
		this.json = json;
		this.jsonParser = Json.createParser(new StringReader(this.json));
	}

	private <T> Map<String, Field> extractField(Class<T> clazz) {
		Map<String, Field> fields = new HashMap<String, Field>();
		for (Field fd : clazz.getDeclaredFields()) {
			JsonType jt = fd.getAnnotation(JsonType.class);
			if (jt != null) {
				if (jt.name().equals("")) {
					fields.put(fd.getName(), fd);
				} else {
					fields.put(jt.name(), fd);
				}
			}
		}
		return fields;
	}

	/**
	 * 对于数组情况的迭代处理
	 * 
	 * @param clazz
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException 
	 */
	public <T> List<T> getList(Class<T> clazz) throws InstantiationException,
			IllegalAccessException, NoSuchFieldException {
		Map<String, Field> fields = extractField(clazz);
		List<T> arrayList = new ArrayList<T>();
		T obj = null;
		int flag = 0;
		while (jsonParser.hasNext()) {
			if (flag == 1) {
				break;
			}
			Event event = jsonParser.next();
			switch (event) {
			case START_OBJECT:
				obj = clazz.newInstance();
				break;
			case KEY_NAME:
				keyNext(fields, obj);
				break;
			case END_OBJECT: {
				arrayList.add(obj);
				break;
			}
			case END_ARRAY: {
				flag = 1;
				break;
			}
			default:
				break;
			}
		}
		return arrayList;
	}

	public <T> T getObject(Class<T> clazz) throws InstantiationException,
			IllegalAccessException, NoSuchFieldException, SecurityException {
		Map<String, Field> fields = extractField(clazz);
		T obj = clazz.newInstance();
		int flag = 0;
		while (jsonParser.hasNext()) {
			if (flag == 1) {
				break;
			}
			Event event = jsonParser.next();
			switch (event) {
			case START_OBJECT:
				break;
			case KEY_NAME:
				keyNext(fields, obj);
				break;
			case END_OBJECT:
			{
				flag=1;
				break;
			}
			default:
				break;
			}
		}
		return obj;
	}

	private void keyNext(Map<String, Field> fields, Object obj)
			throws IllegalAccessException, InstantiationException,
			NoSuchFieldException {
		Field fieldKey = fields.get(jsonParser.getString());
		if(fieldKey==null)
		{
			return;
		}
		JsonType jt = fieldKey.getAnnotation(JsonType.class);
		Event ev = jsonParser.next();// 判断下一个元素是什么
		if (jt.type() == JsonTypeEnum.LIST) {
			if (ev != Event.START_ARRAY) {
				throw new RuntimeException("类型不匹配，要求数组对象");
			} else {
				if (jt.clazz() == null) {
					throw new RuntimeException("请指定对象(List集合中的对象类型)");
				}
				fieldKey.setAccessible(true);
				fieldKey.set(obj, getList(jt.clazz()));// 处理数组，不好处理
			}
		} 
		if(jt.type()==JsonTypeEnum.OBJECT)
		{
			if (ev != Event.START_OBJECT) {
				throw new RuntimeException("类型不匹配，要求对象");
			} else {
				if (jt.clazz() == null) {
					throw new RuntimeException("请指定对象");
				}
				fieldKey.setAccessible(true);
				fieldKey.set(obj, getObject(jt.clazz()));// 处理数组，不好处理
			}
		}
		else {
			if (jt.type() == JsonTypeEnum.STRING) {
				if (ev != Event.VALUE_STRING) {
					throw new RuntimeException("类型不匹配，要求string");
				} else {
					fieldKey.setAccessible(true);
					fieldKey.set(obj, jsonParser.getString());
				}
			}
			if (jt.type() == JsonTypeEnum.INT) {
				if (ev != Event.VALUE_NUMBER) {
					throw new RuntimeException("类型不匹配，要求int");
				} else {
					fieldKey.setAccessible(true);
					fieldKey.set(obj, jsonParser.getInt());
				}
			}
		}
	}

	public static void main(String[] args) {
		
		
		
	}
}
