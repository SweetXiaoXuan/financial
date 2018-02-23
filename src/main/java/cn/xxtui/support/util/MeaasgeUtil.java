package cn.xxtui.support.util;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MeaasgeUtil {
    private String defaultPath = "message.xml";
    private static List<Map<String, String>> list = new ArrayList<>();
    public MeaasgeUtil() {
        try {
            getMessage();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws DocumentException {
        MeaasgeUtil me = new MeaasgeUtil();
        String value = me.getValue("canNotEmpty");
        System.out.println(value);
    }

    public String getValue(String key) {
        String str = null;
        String value = null;
        for (Map<String, String> map : list) {
            str = map.get("name");
            if (key.equals(str)) {
                value = map.get("value");
            }
        }
        if (str == null)
            return "undefined Msg";
        else
            return value;
    }

    public void getMessage() throws DocumentException {
        //使用dom4j解析xml,生成dom树
        SAXReader reader = new SAXReader();
        Document doc = reader.read(new File(getSys() + defaultPath));
        //得到根节点
        Element root = doc.getRootElement();
        //得到所有子节点
        Iterator<Element> it = root.elementIterator();
        while(it.hasNext()){
            Element stuElem = it.next();
            //输出属性：id
            List<Attribute> attrList = stuElem.attributes();
            Map<String, String> messages = new HashMap<>();
            for(Attribute attr :attrList){
                messages.put(attr.getName(), attr.getValue());
            }
            list.add(messages);
            //输出子元素
            Iterator <Element>it2 = stuElem.elementIterator();
            while(it2.hasNext()){
                Element elem = it2.next();
                String name = elem.getName();
                String text = elem.getText();
                System.out.println(name + "----->" + text);
            }
        }
    }

    private String getSys() {
        String prefix = "";
        String sysType = System.getProperty("os.name").toUpperCase();
        if (sysType.contains("WINDOWS")) {
            prefix = "D://";
        }
        if (sysType.contains("LINUX") || sysType.contains("UNIX")) {
            prefix = "/data/workspace/";
        }
        return prefix;
    }


//    public static void main(String[] args) throws DocumentException {
//        //使用dom4j解析scores2.xml,生成dom树
//        SAXReader reader = new SAXReader();
//        Document doc = reader.read(new File("D:\\message.xml"));
//        //得到根节点：students
//        Element root = doc.getRootElement();
//        //得到students的所有子节点：student
//        Iterator<Element> it = root.elementIterator();
//
//        //处理每个student
//        while(it.hasNext()){
//            //得到每个学生
//            Element stuElem =it.next();
//            //System.out.println(stuElem);
//            //输出学生的属性：id
//            List<Attribute> attrList = stuElem.attributes();
//            for(Attribute attr :attrList){
//                String name = attr.getName();
//                String value = attr.getValue();
//                System.out.println(name+"----->"+value);
//            }
//            //输出学生的子元素：name，course，score
//            Iterator <Element>it2 = stuElem.elementIterator();
//            while(it2.hasNext()){
//                Element elem = it2.next();
//                String name = elem.getName();
//                String text = elem.getText();
//                System.out.println(name+"----->"+text);
//            }
//            System.out.println();
//        }
//    }
}
