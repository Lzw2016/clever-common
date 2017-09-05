package org.clever.common.model.entity;

import java.io.Serializable;

/**
 * 实体类接口<br/>
 * <p/>
 * 作者：LiZW <br/>
 * 创建时间：2016-5-12 9:26 <br/>
 */
public interface BaseEntity extends Serializable {
    /**
     * 表示显示的常值
     */
    char SHOW = '1';
    /**
     * 表示隐藏的常值
     */
    char HIDE = '0';

    /**
     * 是
     */
    char YES = '1';
    /**
     * 否
     */
    char NO = '0';

    /**
     * 删除标记名称
     */
    String FIELD_DEL_FLAG = "delFlag";
    /**
     * 删除标记,1：正常
     */
    char DEL_FLAG_NORMAL = '1';
    /**
     * 删除标记,2：删除
     */
    char DEL_FLAG_DELETE = '2';
    /**
     * 删除标记,3：审核
     */
    char DEL_FLAG_AUDIT = '3';

    /**
     * 自身关联实体类的fullPath属性分隔标识
     */
    char FULL_PATH_SPLIT = '-';
}