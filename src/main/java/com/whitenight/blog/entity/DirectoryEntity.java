package com.whitenight.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Getter
@Setter
@TableName("directory")
@EntityScan
public class DirectoryEntity {
    @TableId(value = "id",type = IdType.AUTO)
    private int id;

    @TableField("userId")
    private int userId;

    @TableField("parent_tableId")
    private int parent_tableId;

    @TableField("directoryName")
    private String directoryName;

    @TableField("openness")
    private int openness;

    @Override
    public String toString() {
        return "DirectoryEntity{" +
                "id=" + id +
                ", userId=" + userId +
                ", parent_tableId=" + parent_tableId +
                ", directoryName='" + directoryName + '\'' +
                ", openness=" + openness +
                '}';
    }
}
