package cn.lv.jewelry.label.daobean;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by 24593 on 2018/2/10.
 */
public class Label {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    // 标签父id，一级标签为空，二级及其以下级别标签不为空
    @Column(name = "parent_id")
    private Long parentId;
    // 标签名
    private String name;
    @Column(name = "create_time")
    private String createTime;
    // 类型：1平台定义 2自定义
    private Integer type;
    // 标签状态 0正常 1异常
    private Integer status;
    // 是否删除 0否 1是
    @Column(name = "is_delete")
    private Integer isDelete;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }
}
