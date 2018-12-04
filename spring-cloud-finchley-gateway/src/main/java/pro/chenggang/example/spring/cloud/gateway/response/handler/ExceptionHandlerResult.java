package pro.chenggang.example.spring.cloud.gateway.response.handler;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

/**
 * @classDesc:异常处理结果统一封装
 * @author: chenggang
 * @createTime: 2018/10/30
 * @version: v1.0.0
 */
@Getter
@Setter
@ToString
public class ExceptionHandlerResult {

    private HttpStatus httpStatus;

    private String responseResult;

    public ExceptionHandlerResult() {
    }

    public ExceptionHandlerResult(HttpStatus httpStatus, String responseResult) {
        this.httpStatus = httpStatus;
        this.responseResult = responseResult;
    }

}
