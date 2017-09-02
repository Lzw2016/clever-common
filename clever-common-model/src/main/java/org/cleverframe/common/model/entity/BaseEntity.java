package org.cleverframe.common.model.entity;

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
    Character SHOW = '1';
    /**
     * 表示隐藏的常值
     */
    Character HIDE = '0';

    /**
     * 是
     */
    Character YES = '1';
    /**
     * 否
     */
    Character NO = '0';

    /**
     * 删除标记名称
     */
    String FIELD_DEL_FLAG = "delFlag";
    /**
     * 删除标记,1：正常
     */
    Character DEL_FLAG_NORMAL = '1';
    /**
     * 删除标记,2：删除
     */
    Character DEL_FLAG_DELETE = '2';
    /**
     * 删除标记,3：审核
     */
    Character DEL_FLAG_AUDIT = '3';

    /**
     * 自身关联实体类的fullPath属性分隔标识
     */
    Character FULL_PATH_SPLIT = '-';
}
