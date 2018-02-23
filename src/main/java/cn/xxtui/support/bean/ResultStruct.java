package cn.xxtui.support.bean;

import cn.com.ql.wiseBeijing.serviceUtil.ReturnValue;
import cn.com.ql.wiseBeijing.serviceUtil.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

public class ResultStruct {
    private String status = ResultBean.OK;// 默认0 ok
    private Object body = "null";
    private String msg = "";

    /**
     * 设置返回结果集
     * @param rv 请求datebase后的结果集
     * @param resultStruct 将要返回的结果集
     * @param <T>
     * @return cn.xxtui.support.bean.ResultStruct
     * @author liumengwei
     * @date 2017/11/30
     */
    public static <T> ResultStruct setResultStructInfo (ReturnValue<T> rv, ResultStruct resultStruct) {
        if (rv.getFlag() == ReturnValue.FLAG_SUCCESS) {
            T object = rv.getObject();
            resultStruct.setBody(object == null ? "null" : object);
        }
        resultStruct.setStatus(String.valueOf(rv.getFlag()));
        String meg = rv.getMeg();
        resultStruct.setMsg(StringUtil.isEmpty(meg) ? "" : meg);
        return resultStruct;
    }

    @JSONField(serialize = false, deserialize = false)
    public final static String OK = ResultBean.OK;
    @JSONField(serialize = false, deserialize = false)
    public final static String ERROR = ResultBean.ERROR;
    @JSONField(serialize = false, deserialize = false)
    public final static String PROTECT = ResultBean.PROTECT;
    @JSONField(serialize = false, deserialize = false)
    public final static String NotAssess = ResultBean.NotAssess;
    @JSONField(serialize = false, deserialize = false)
    public final static String UNREAL_NAME = ResultBean.UNREAL_NAME;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
