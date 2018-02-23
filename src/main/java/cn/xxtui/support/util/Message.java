package cn.xxtui.support.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Message extends DefaultHandler {
	private String defaultPath = "message.xml";
	private Map<String, String> messages = new HashMap<String, String>();
	private InputStream in;
	private boolean message = false;
	private String value = null;
	private String key = null;
	private Logger log = LogManager.getLogger(Message.class.getName());
	private File file;
	private long filelastModify = 0l;
	private boolean threadHasStart=false;

	private Message() {
	}

	public void parse() {
		if(!threadHasStart)
		{
			Timer timer=new Timer();
			timer.schedule(new Reload(), 60000, 10000);
			threadHasStart=true;
		}
		String prefix = getSys();
		this.file = new File(prefix + defaultPath);
		try {
			in = new FileInputStream(file);
			filelastModify = file.lastModified();
		} catch (FileNotFoundException e1) {
			if (log.isInfoEnabled()) {
				log.info(e1.getMessage());
				log.info("use the default message.xml");
			}
			in = this.getClass().getClassLoader()
					.getResourceAsStream(defaultPath);
		}

		try {
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			parser.parse(in, this);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getSys() {
		String prefix = "";
		String sysType = System.getProperty("os.name").toUpperCase();
		if (sysType.contains("WINDOWS")) {
			prefix = "D://";
		}
		if (sysType.contains("LINUX") || sysType.contains("UNIX")) {
			prefix = "/data/";
		}
		return prefix;
	}

	private static Message mes = new Message();

	public static Message getInstance() {
		return mes;
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (value != null && this.key != null) {
			this.messages.put(key, value.trim());
			key = null;
			value = null;
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (qName == "message") {
			if (attributes.getLength() == 0) {
				return;
			}
			String key = attributes.getValue("key");
			if (key == null)
				return;
			this.key = key;
			message = true;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (message) {
			value = new String(ch, start, length);
			message = false;
		}
	}

	public String getValue(String key) {
		String str = this.messages.get(key);
		if (str == null)
			return "undefined Msg";
		else
			return str;
	}

	public static void main(String[] args) {
		Message me = Message.getInstance();
		me.parse();
		System.out.println(me.getValue("abc"));
		System.out.println(me.getValue("user"));
		System.out.println(me.getValue("dduser"));
	}

	private class Reload extends TimerTask {
		public Reload()
		{
			if(log.isInfoEnabled())
			{
				log.info("开始启动热加载message.xml配置文件 ");
			}
		}
		public void run() {
			String prefix = getSys();
			file = new File(prefix + defaultPath);
			if (file.exists()) {
				long current = file.lastModified();
				if (current > filelastModify)
				{
					if(log.isDebugEnabled())
					{
						log.info("文件message.xml被修改，重新加载……");
					}
					parse();// 重新加载这个文件
				}
			}
		}
	}
}
