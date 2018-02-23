package cn.lv.jewelry.activity.daoBean;

/**
 * Created by lixing on 16/3/23.
 */
public enum  ActivityContentType {
    TEXT(0),IMG(1),RIMG(2),AUDIO(3),VEDIO(4);

    private int v;
    ActivityContentType(int v)
    {
       this.v=v;
    }
    public int getV()
    {
        return this.v;
    }
}
