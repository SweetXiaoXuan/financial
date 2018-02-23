package cn.xxtui.support.util;

import cn.com.ql.wiseBeijing.serviceUtil.ReturnValue;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.List;

/**
 * Created by 24593 on 2017/12/23.
 */
public class ImageUtils {
    private static MeaasgeUtil me = new MeaasgeUtil();
    /**
     *  http访问图片
     * @param url 图片地址
     * @return java.lang.Integer
     * @throws IOException
     * @author liumengwei
     * @date 2017/12/30
     */
    public static Integer checkInamge(String url) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);
        StatusLine line = response.getStatusLine();
        return line.getStatusCode();
    }

    /**
     * 通过http访问图片地址，查看图片是否真实存在
     * @param resultStruct 结果
     * @param imagePath 图片地址
     * @return cn.xxtui.support.bean.ResultStruct
     * @throws IOException
     * @author liumengwei
     * @date 2017/12/30
     */
    public static ReturnValue accessImage(ReturnValue resultStruct, List<String> imagePath) throws IOException {
        for (int i = 0; i < imagePath.size(); i++) {
            Integer code = ImageUtils.checkInamge(imagePath.get(i));
            if (code != 200) {
                resultStruct.setFlag(ReturnValue.FLAG_FAIL);
                resultStruct.setMeg(me.getValue(ResultMsgConstant.picError));
                return resultStruct;
            }
        }
        return resultStruct;
    }
}
