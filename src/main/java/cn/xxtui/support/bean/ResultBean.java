package cn.xxtui.support.bean;

import org.apache.commons.collections.map.LinkedMap;

import javax.xml.bind.annotation.XmlRootElement;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用来专门生成返回给客户端的结果bean
 * 
 * @author StarLee
 * 
 */
@XmlRootElement(name = "response")
public class ResultBean {
	private String status = ResultBean.OK;// 默认0 ok
	private Object body;
	private String msg = "";
	public final static String OK = "0";
	public final static String ERROR = "1";
	public final static String PROTECT = "2";
	public final static String NotAssess="4";
	public final static String UNREAL_NAME="10001";

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Object getBody() {
		return body;
	}

	/**
	 * 可以设置集合类（map） 可以设置对象，(后面已经过期，可以设置Map或List集合)但对象的集合类型的成员变量里面的对象不能是集合对象，对于非法的我现在还没有让其抛出异常
	 * 
	 * @param body
	 */
	public void setBody(Object body) {
		if (Map.class.isAssignableFrom(body.getClass())) {
			ResultMap resultMap = new ResultMap();
			resultMap.setMap((Map) body);
			this.body = resultMap;
		} else
			this.body = body;
		
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String toString() {
		Map<String, Type> bodyMap = toInnerString();
		Map<String, Type> map = new HashMap<String, Type>();
		if (bodyMap != null) {
			// JSONObject bodyobject = new JSONObject(bodyMap);
			// map.put("body", bodyobject.toString());
			Type bodyType = new Type();
			bodyType.setContent(map2Json(bodyMap));
			bodyType.setType(Type.OTHER);
			map.put("body", bodyType);
		} else {
			Type emptyType = new Type();
			if (body == null) {
				emptyType.setContent("null");
			} else
				emptyType.setContent(body.toString());
			emptyType.setType(Type.PRIMITIVE);
			map.put("body", emptyType);
		}
		Type statusType = new Type();
		statusType.setContent(this.status);
		map.put("status", statusType);
		Type msgType = new Type();
		msgType.setContent(this.msg);
		map.put("msg", msgType);
		// JSONObject strResult = new JSONObject(map);
		// return strResult.toString();
		return map2Json(map);
	}
	protected Map<String, Type> toInnerString() {
		Map<String, Type> bodyMap = null;
		if (body != null) {
			if (ResultMap.class.isAssignableFrom(body.getClass())) {
				bodyMap = new LinkedMap();
				ResultMap resultMap = (ResultMap) body;
				Set<Map.Entry<String, Object>> set = resultMap.getMap()
						.entrySet();
				for (Map.Entry<String, Object> entry : set) {
					Type type = new Type();
					Object o = entry.getValue();
					if(o==null)
						type.setContent("null");
					else if (primitiveType(o.getClass())) {
						type.setContent(o);
					} else {
						//处理List
						
						if (List.class.isAssignableFrom(o.getClass())) {
							ResultList list = new ResultList();
							list.setBody((List) o);
							type.setContent(list.toInnerString());
						} 
						//处理Map
						else if (Map.class.isAssignableFrom(o.getClass())) {
							ResultBean list = new ResultBean();
							list.setBody((Map) o);
							type.setContent(map2Json(list.toInnerString()));
						} 
						else {
							type.setContent(map2Json(extract(o)));
						}
					}
					type.setType(Type.PRIMITIVE);
					bodyMap.put(entry.getKey(), type);
				}
			} else {
				if (primitiveType(body.getClass()))// 如果body是基本类型
				{
				} else
					bodyMap = extract(body);
			}
		}
		return bodyMap;
	}

	private boolean primitiveType(Class clazz) {
		if (clazz.isPrimitive() || Integer.class.isAssignableFrom(clazz)
				|| Double.class.isAssignableFrom(clazz)
				|| Float.class.isAssignableFrom(clazz)
				|| Double.class.isAssignableFrom(clazz)
				|| Long.class.isAssignableFrom(clazz)
				|| Character.class.isAssignableFrom(clazz)
				|| Byte.class.isAssignableFrom(clazz)
				|| Short.class.isAssignableFrom(clazz)
				|| Boolean.class.isAssignableFrom(clazz)
				|| String.class.isAssignableFrom(clazz)) {
			return true;
		} else
			return false;
	}

	/**
	 * 前提：集合字段里的元素不允许是集合类的子类元素,集合中必须保存一样类型的数据,我并没有处理这种情况
	 * 
	 * @param body
	 * @return
	 */
	protected Map<String, Type> extract(Object body) {
		Map<String, Type> bodyMap;
		Field[] fields = body.getClass().getDeclaredFields();
		bodyMap = new HashMap<String, Type>();
		int length = fields.length;
		String[] des = new String[length];
		for (Field field : fields) {
			try {
				String name = field.getName();
				Type value = null;
				field.setAccessible(true);
				Object o = field.get(body);
				if (o == null) {
					value = new Type();
					value.setContent("null");
					value.setType(Type.PRIMITIVE);
					bodyMap.put(name, value);
					continue;
				} else {
					// 是map的情况
					if (Map.class.isAssignableFrom(field.getType())) {
						field.setAccessible(true);
						Map map = (Map) o;
						Set<Map.Entry<String, Object>> entries = map.entrySet();
						for (Map.Entry<String, Object> entry : entries) {
							Object entryObj = entry.getValue();
							Type type = new Type();
							if (!primitiveType(entryObj.getClass())) {
								Map<String, Type> extractMap = extract(entryObj);
								type.setType(Type.OTHER);
								type.setContent(map2Json(extractMap));
							} else {
								type.setType(Type.PRIMITIVE);
								type.setContent(entryObj.toString());
							}
							map.put(entry.getKey(), type);
						}
						value = new Type();
						value.setType(Type.OTHER);
						value.setContent(map2Json(map));
						// value = map2Json(map);
					}
					// 是list情况(里面不要存在集合类)
					// 数组没有处理
					else if (List.class.isAssignableFrom(field.getType())) {
						field.setAccessible(true);
						List list = (List) o;
						/*List listResult = null;
						if (!list.isEmpty()) {
							listResult = new ArrayList();
						}
						for (Object obj : list) {
							value = new Type();
							if (!primitiveType(obj.getClass())) {
								listResult.add(map2Json(extract(obj)));

							} else {
								listResult.add(obj.toString());
							}
						}
						value.setType(Type.LIST);
						value.setContent(listResult);*/
						value=new Type();
						ResultList listt = new ResultList();
						listt.setBody(list);
						value.setContent(listt.toInnerString());
						value.setType(Type.PRIMITIVE);
					}

					else if (!primitiveType(field.getType())) {
						value = new Type();
						value.setContent(map2Json(extract(o)));
						value.setType(Type.OTHER);
					} else {
						// value = BeanUtils.getProperty(body, name);
						value = new Type();
						value.setContent(o);
						value.setType(Type.PRIMITIVE);
					}
					bodyMap.put(name, value);
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bodyMap;
	}

	protected String map2Json(Map<String, Type> map) {
		Set<Map.Entry<String, Type>> entries = map.entrySet();
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		int i = 0;
		for (Map.Entry<String, Type> entry : entries) {
			i++;
			builder.append("\"");
			builder.append(entry.getKey());
			builder.append("\"");
			builder.append(":");
			Type value = entry.getValue();
			if (value.type == Type.LIST) {
				builder.append("[");
				List list = (List) value.getContent();
				int n = list.size();
				for (Object obj : list) {
					n--;
					builder.append(obj);
					if (n != 0) {
						builder.append(",");
					}
				}
				builder.append("]");
			} else {
				if (value.type == Type.PRIMITIVE) {
					boolean splash = true;
					if (value!=null&&value.getContent()!=null&&String.class.isAssignableFrom(value.getContent()
							.getClass())) {
						String realVal = value.getContent().toString().trim();
						if (realVal.length() == 0
								|| (!"null".equals(realVal)
										&& '[' != realVal.charAt(0) && '{' != realVal
										.charAt(0)))
							builder.append("\"");
						else
							splash = false;
					} else
						splash = false;
					builder.append(value.getContent());
					if (splash) {
						builder.append("\"");
					}
				} else
					builder.append(value.getContent());
			}
			if (i != entries.size()) {
				builder.append(",");
			}
		}

		builder.append("}");
		return builder.toString();
	}

	protected class Type {
		private int type = 0;// 0(全加引号)是基本类型(包含String)，1(加[])是List,2(加{})是map或者其它非基本类型
		private Object content;
		public static final int PRIMITIVE = 0;
		public static final int LIST = 1;
		public static final int OTHER = 2;

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public Object getContent() {
			return content;
		}

		public void setContent(Object content) {
			this.content = content;
		}

	}

}
