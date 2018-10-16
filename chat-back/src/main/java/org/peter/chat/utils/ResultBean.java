package org.peter.chat.utils;

import lombok.Data;
import lombok.experimental.Accessors;
import org.peter.chat.enums.ResultStatus;

/**
 * 结果对象
 *
 * @param <T>
 */
@Data
@Accessors(chain = true)
public class ResultBean<T> {
    // 数据
    private T data;

    // 状态码
    private int code;

    // 状态信息
    private String info;

    public ResultBean() {
    }

    public ResultBean success() {
        code = ResultStatus.SUCCESS.getCode();
        info = ResultStatus.SUCCESS.getInfo();
        return this;
    }

    public ResultBean<T> success(T data) {
        code = ResultStatus.SUCCESS.getCode();
        info = ResultStatus.SUCCESS.getInfo();
        this.data = data;
        return this;
    }

    public ResultBean failed() {
        code = ResultStatus.FAIL.getCode();
        info = ResultStatus.FAIL.getInfo();
        return this;
    }

    public ResultBean failed(String info) {
        code = ResultStatus.FAIL.getCode();
        this.info = info;
        return this;
    }
}
