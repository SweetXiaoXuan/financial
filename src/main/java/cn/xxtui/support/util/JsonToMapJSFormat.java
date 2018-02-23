package cn.xxtui.support.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
import javax.json.stream.JsonParsingException;


import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/*import org.codehaus.jackson.JsonFactory;
 import org.codehaus.jackson.JsonParseException;
 import org.codehaus.jackson.JsonParser;*/

public class JsonToMapJSFormat {

	public static void main(String[] args) {
		// String headers = new String(get.getResponseBody());
		// ByteArrayInputStream bytes=new ByteArrayInputStream();
		// //HttpMethodParams source = new HttpMethodParams();
		// HttpMethodParams mid = new HttpMethodParams();
		// source.setParameter("source", "3402488755");
		// mid.setParameter("id", "1720962692");
		// getMid.setParams(source);
		// getMid.setParams(mid);
		// Header head=new Header(name, value);
		// getMid.addRequestHeader(header);
		//这个更好
		JsonToMapJSFormat weibo=new JsonToMapJSFormat();
		String json = "{\"name\":\"mkyong\", \"age\":\"29\",\"test\":{\"abc\":1}}";
		System.out.println(json);
		StringReader reader = new StringReader(json);
		JsonParser parser = Json.createParser(reader);
		weibo.parser(parser, "");
		
		Map<String,String> map = new HashMap<String,String>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			map = mapper.readValue(json, 
				    new TypeReference<HashMap<String,Object>>(){});
			System.out.println(map.toString());
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Map<String,Object> getMap(String json)
	{
		String result =json;
		StringReader reader = new StringReader(result);
		JsonParser parser = Json.createParser(reader);
		try
		{
			parser(parser, "");
		}
		catch(JsonParsingException ex)
		{
			this.originJson="{\"request\": \"/2/statuses/show.json\",\"error_code\": \"201XX\",\"error\": \""+ex.getMessage()+"\"}";
			this.map.put("error_code", "201XX");
			this.map.put("error", ex.getMessage());
			return this.map;
		}
		return this.map;
	}
	
	private Map<String, Object> map = new HashMap<String, Object>();
	private String originJson;
	
	public String getOriginJson() {
		return originJson;
	}

	private  void parser(JsonParser parser, String prefix) {
		boolean array = false;
		while (parser.hasNext()) {
			Event e = parser.next();
			switch (e) {
			case START_OBJECT:
				String newPrefix=prefix;
				if (!prefix.equals(""))
					newPrefix += ".";
				parser(parser, newPrefix);
				prefix=drawBackKey(prefix, array);
				break;
			case KEY_NAME:
				String key = parser.getString();
				prefix = prefix + key;
				break;
			case VALUE_FALSE:
				map.put(prefix, false);
				prefix = drawBackKey(prefix, array);
				break;
			case VALUE_NUMBER:
				map.put(prefix, parser.getLong());
				prefix = drawBackKey(prefix, array);
				break;
			case VALUE_STRING:
				map.put(prefix, parser.getString());
				prefix = drawBackKey(prefix, array);
				break;
			case VALUE_NULL:
				map.put(prefix, null);
				prefix = drawBackKey(prefix, array);
				break;
			case VALUE_TRUE:
				map.put(prefix, true);
				prefix = drawBackKey(prefix, array);
				break;
			case START_ARRAY:
				array = true;
				//map.put(prefix, "ARR");
				break;
			case END_ARRAY:
				array = false;
				prefix = drawBackKey(prefix, array);
				break;
			case END_OBJECT:
				// 每取完一个object，回退
				return;
			}
		}
		//System.out.println("----------"+map.toString());
	}

	/**
	 * 每取完一个值使key回退到未取值前状态。比如user.id,取完id后，变成user. 如果没有.,则回退到空白
	 * 
	 * @param prefix
	 * @param array 如果是数组中，则key不要回退了
	 * @return
	 */
	private String drawBackKey(String prefix, boolean array) {
		if (array)
			return prefix;// 数组不要回退key
		if (prefix.contains(".")) {
			prefix = prefix.substring(0, prefix.lastIndexOf(".") + 1);
		} else {
			prefix = "";
		}
		return prefix;
	}

	
}
