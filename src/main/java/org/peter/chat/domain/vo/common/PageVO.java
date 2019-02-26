package org.peter.chat.domain.vo.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
public class PageVO<T> {
    /**
     * 当前页号
     */
    private long current;

    /**
     * 页大小
     */
    private long size;

    /**
     * 总页数
     */
    private long pages;

    /**
     * 总数据数量
     */
    private long total;

    /**
     * 页内数据
     */
    private List<T> records;


    /**
     * 将mp页对象直接装换为自定义的pageVO
     *
     * @param page
     * @return
     */
    public static <T> PageVO<T> transTo(Page<T> page) {
        PageVO<T> pageVO = new PageVO<>();
        pageVO.setCurrent(page.getCurrent())
                .setSize(page.getSize())
                .setTotal(page.getTotal())
                .setRecords(page.getRecords())
                .setPages(page.getPages());
        return pageVO;
    }

    /**
     * 将mp页对象装换为自定义的pageVO,
     * 并装换为指定的vo类型
     *
     * @param page
     * @return
     */
    public static <T, E> PageVO<E> transTo(Page<T> page, Class<E> voClass) {
        if (voClass != null) {
            // 新的vo页对象
            PageVO<E> pageVO = new PageVO<>();
            BeanUtils.copyProperties(page, pageVO);

            List<E> voList = page.getRecords()
                    .stream()
                    .map(entity -> {
                        E voObject = null;
                        try {
                            // 获取vo的无参构造函数
                            Constructor<E> constructor = voClass.getConstructor(null);

                            // 通过无参构造函数新实例化一个空的vo对象
                            voObject = constructor.newInstance(null);

                            // 拷贝entity对象属性
                            BeanUtils.copyProperties(entity, voObject);
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                            System.out.println("该vo没有空的构造函数");
                        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        return voObject;
                    }).collect(Collectors.toList());
            pageVO.setRecords(voList);
            return pageVO;
        }
        throw new RuntimeException("voClass is null");
    }
}
