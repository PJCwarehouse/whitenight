package com.whitenight.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.Date;

@Getter
@Setter
@TableName("document")
@EntityScan
public class DocumentEntity {

    @TableId(value = "id",type = IdType.AUTO)
    private int id;

    @TableField("type")
    private String type;

    @TableField("userId")
    private int userId;

    @TableField("parentId")
    private int parentId;

    @TableField("name")
    private String name;

    @TableField("url")
    private String url;

    @TableField("time")
    private Date time;

    @TableField("size")
    private double size;

    @TableField("sizeUnit")
    private String sizeUnit;

    @TableField("visibleType")
    private int visibleType;

    public DocumentEntity(){
    }
    public DocumentEntity(String type, int userId, int parentId, String name, Date time, double size, String sizeUnit, int visibleType){
        this.type = type;
        this.userId = userId;
        this.parentId = parentId;
        this.name = name;
        this.time = time;
        this.size = size;
        this.sizeUnit = sizeUnit;
        this.visibleType = visibleType;
    }

    @Override
    public String toString() {
        return "DocumentEntity{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", userId=" + userId +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", time=" + time +
                ", size=" + size +
                ", sizeUnit='" + sizeUnit + '\'' +
                ", visibleType=" + visibleType +
                '}';
    }
}
