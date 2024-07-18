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
@TableName("directory_authority")
@EntityScan
public class DirectoryAuthorityEntity {

    @TableId(value = "id",type = IdType.AUTO)
    private int id;

    @TableField("userId")
    private int userId;

    @TableField("directoryId")
    private int directoryId;

    @TableField("authority")
    private Character authority;
}
