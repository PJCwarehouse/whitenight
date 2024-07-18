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
@TableName("directory_data")
@EntityScan
public class DirectoryDataEntity {

    @TableId(value = "id",type = IdType.AUTO)
    private int id;

    @TableField("directoryId")
    private int directoryId;

    @TableField("name")
    private String name;

    @TableField("url")
    private String url;

    @TableField("time")
    private Date time;

    @Override
    public String toString() {
        return "DirectoryEntity{" +
                "id=" + id +
                ", directoryId=" + directoryId +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", time=" + time +
                '}';
    }
}
