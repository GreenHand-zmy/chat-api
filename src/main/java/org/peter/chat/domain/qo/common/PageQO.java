package org.peter.chat.domain.qo.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 分页查询对象
 *
 * @param <T>
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class PageQO<T> implements Serializable {
    /**
     * 页号
     */
    private long pageNum;

    /**
     * 一页的数量
     */
    private long pageSize;

    /**
     * 查询条件
     */
    private T condition;

    /**
     * 排序字段(升序)
     */
    private String asc;

    /**
     * 排序字段(降序)
     */
    private String desc;

    /**
     * 装换为mp查询参数
     * @param <E>
     * @return
     */
    public <E> Page<E> transTo() {
        Page<E> page = new Page<>();
        page.setSize(pageSize)
                .setAsc(asc)
                .setDesc(desc)
                .setCurrent(pageNum);

        return page;
    }
}
