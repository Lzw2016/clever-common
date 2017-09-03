package org.clever.common.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 排序查询基础类
 * <p>
 * 作者：lzw <br/>
 * 创建时间：2017-09-03 22:15 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QueryBySort extends BaseRequest {
    private static final long serialVersionUID = 1L;
}
