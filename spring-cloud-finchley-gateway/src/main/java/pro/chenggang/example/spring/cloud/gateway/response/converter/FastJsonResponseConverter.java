package pro.chenggang.example.spring.cloud.gateway.response.converter;


import com.alibaba.fastjson.JSON;
import pro.chenggang.example.spring.cloud.gateway.response.ResponseResult;
import pro.chenggang.example.spring.cloud.gateway.response.info.Info;

/**
 * @classDesc:
 * @author: chenggang
 * @createTime: 2018/10/22
 * @version: v1.0.0
 */
public class FastJsonResponseConverter implements ResponseConverter {

    @Override
    public String convertJsonResponse(Info responseInfo) {
        ResponseResult responseResult = new ResponseResult(responseInfo);
        return JSON.toJSONString(responseResult);
    }

    @Override
    public String covertJsonResponse(Info responseInfo, Object data) {
        ResponseResult responseResult = new ResponseResult(responseInfo, data);
        return JSON.toJSONString(responseResult);
    }
}
