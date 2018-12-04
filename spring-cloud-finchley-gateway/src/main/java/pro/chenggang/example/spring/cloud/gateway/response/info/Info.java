package pro.chenggang.example.spring.cloud.gateway.response.info;

/**
 * @classDesc:
 * @author: chenggang
 * @createTime: 2018/10/22
 * @version: v1.0.0
 */
public interface Info {

    /**
     * 获取响应码
     * @return
     */
    String getCode();

    /**
     * 获取响应信息
     * @return
     */
    String getMsg();
}
