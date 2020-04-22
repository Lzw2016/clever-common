package org.clever.common.model.request;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页查询基础类
 * <p>
 * 作者：lzw <br/>
 * 创建时间：2017-09-02 00:41 <br/>
 */
@SuppressWarnings("deprecation")
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
     * 是否进行 count 查询
     */
    @Setter
    @Getter
    @ApiModelProperty(value = "是否进行 count 查询", position = 6)
    private boolean isSearchCount = true;
    /**
     * 分页
     */
    @ApiModelProperty(hidden = true, accessMode = ApiModelProperty.AccessMode.READ_ONLY, readOnly = true, position = Integer.MAX_VALUE)
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
     * 设置分页对象
     */
    public void page(IPage<?> page) {
        this.page = page;
    }

    /**
     * 获取请求参数对应的 IPage 对象<br />
     * <strong>注意: 当前方法指定要在分页查询执行之后调用否则数据不准确</strong>
     * <pre>
     *     {@code
     *     return query.result(permissionMapper.findByPage(query));
     *     }
     * </pre>
     */
    public <T> Page<T> result(List<T> records) {
        if (page == null) {
            page = new Page<>(getPageNo(), getPageSize());
        }
        Page<T> newPage = new Page<>();
        newPage.setTotal(page.getTotal());
        newPage.setSize(page.getSize());
        newPage.setCurrent(page.getCurrent());
        newPage.setSearchCount(page.isSearchCount());
        newPage.setPages(page.getPages());
        newPage.setRecords(records);
        List<String> orderFields = this.getOrderFields();
        List<String> sorts = this.getSortsSql();
        List<OrderItem> orders = new ArrayList<>(orderFields.size());
        for (int index = 0; index < orderFields.size(); index++) {
            OrderItem orderItem = new OrderItem();
            orderItem.setColumn(orderFields.get(index));
            String sort = ASC;
            if (sorts.size() > index) {
                sort = sorts.get(index);
            }
            orderItem.setAsc(ASC.equalsIgnoreCase(StringUtils.trim(sort)));
            orders.add(orderItem);
        }
        newPage.setOrders(orders);
        return newPage;
    }
}
