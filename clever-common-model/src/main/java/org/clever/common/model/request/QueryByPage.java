package org.clever.common.model.request;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;

/**
 * 分页查询基础类
 * <p>
 * 作者：lzw <br/>
 * 创建时间：2017-09-02 00:41 <br/>
 */
@EqualsAndHashCode(callSuper = false)
public class QueryByPage extends QueryBySort {
    private static final long serialVersionUID = 1L;

    /**
     * 每页的数据量 - 最大值
     */
    private static final int PAGE_SIZE_MAX = 100;

    /**
     * 每页的数据量(1 <= pageSize <= 100)
     */
    @ApiModelProperty(value = "每页的数据量", position = 5)
    private int pageSize = 10;

    /**
     * 当前页面的页码数(pageNo >= 1)
     */
    @ApiModelProperty(value = "当前页面的页码数", position = 6)
    private int pageNo = 1;

    /**
     * 分页
     */
    @ApiModelProperty(hidden = true, accessMode = ApiModelProperty.AccessMode.READ_ONLY, readOnly = true, position = 999999999)
    private IPage<?> page;

    /*--------------------------------------------------------------
     * 			getter、setter
     * -------------------------------------------------------------*/

    public int getPageSize() {
        if (pageSize > PAGE_SIZE_MAX) {
            pageSize = PAGE_SIZE_MAX;
        }
        if (pageSize < 1) {
            pageSize = 1;
        }
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        if (pageSize > PAGE_SIZE_MAX) {
            pageSize = PAGE_SIZE_MAX;
        }
        if (pageSize < 1) {
            pageSize = 1;
        }
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        if (pageNo < 1) {
            pageNo = 1;
        }
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        if (pageNo < 1) {
            pageNo = 1;
        }
        this.pageNo = pageNo;
    }

    /**
     * 获取请求参数对应的 IPage 对象
     */
    public IPage<?> result() {
        if (page == null) {
            page = new Page<>(getPageNo(), getPageSize());
        }
        return page;
    }

    /**
     * 设置分页对象
     */
    public void page(IPage<?> page) {
        this.page = page;
    }
}
