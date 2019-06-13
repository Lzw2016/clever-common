package org.clever.common.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

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

    private static final String ASC = "ASC";
    private static final String DESC = "DESC";

    /**
     * 排序字段(单字段排序-低优先级)
     */
    private String orderField;
    /**
     * 排序类型ASC DESC(单字段排序-低优先级)
     */
    private String sort;

    /**
     * 排序字段集合
     */
    private List<String> orderFields = new ArrayList<>(1);
    /**
     * 排序类型 ASC DESC
     */
    private List<String> sorts = new ArrayList<>(1);

    public List<String> getOrderFields() {
        if (orderFields == null) {
            orderFields = new ArrayList<>(1);
        }
        if (orderFields.size() <= 0 && StringUtils.isNotBlank(orderField)) {
            orderFields.add(orderField);
        }
        return orderFields;
    }

    public List<String> getSorts() {
        if (sorts == null) {
            sorts = new ArrayList<>(1);
        }
        if (StringUtils.isNotBlank(orderField) && StringUtils.isBlank(sort)) {
            sort = ASC;
        }
        if (sorts.size() <= 0 && StringUtils.isNotBlank(sort)) {
            sorts.add(sort);
        }
        orderFields = getOrderFields();
        List<String> sortsTmp = new ArrayList<>();
        for (int index = 0; index < orderFields.size(); index++) {
            if (sorts.size() > index) {
                String s = StringUtils.trim(sorts.get(index));
                if (!DESC.equalsIgnoreCase(s) && !ASC.equalsIgnoreCase(s)) {
                    s = ASC;
                }
                sortsTmp.add(s);
                continue;
            }
            sortsTmp.add(ASC);
        }
        sorts = sortsTmp;
        return sorts;
    }
}
