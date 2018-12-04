package pro.chenggang.example.spring.cloud.gateway.response.converter;

import pro.chenggang.example.spring.cloud.gateway.response.info.Info;

/**
 * @classDesc:
 * @author: chenggang
 * @createTime: 2018/10/22
 * @version: v1.0.0
 */
public interface ResponseConverter {

    /**
     * 转换响应Json
     * @param responseInfo
     * @return
     */
    String convertJsonResponse(Info responseInfo);

    /**
     * 转换含有Data的JsonResponse
     * @param responseInfo
     * @param data
     * @return
     */
    String covertJsonResponse(Info responseInfo, Object data);
}
