package pro.chenggang.example.spring.cloud.gateway.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pro.chenggang.example.spring.cloud.gateway.response.info.Info;

/**
 * @classDesc:
 * @author: chenggang
 * @createTime: 2018/10/22
 * @version: v1.0.0
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ResponseResult<T> {

    @JSONField(ordinal = 1)
    private String code;
    @JSONField(ordinal = 2)
    private String msg;
    @JSONField(ordinal = 3)
    private T data;

    public ResponseResult(Info responseInfo) {
        this.code = responseInfo.getCode();
        this.msg = responseInfo.getMsg();
    }

    public ResponseResult(Info responseInfo,T data) {
        this.code = responseInfo.getCode();
        this.msg = responseInfo.getMsg();
        this.data = data;
    }

}
