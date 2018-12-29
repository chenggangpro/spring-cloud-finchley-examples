package pro.chenggang.example.spring.cloud.gateway.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @classDesc:过滤器校验切面,满足校验条件则不执行过滤器,否则执行过滤器
 * @author: chenggang
 * @createTime: 2018/10/29
 * @version: v1.0.0
 * @copyright: 北京辰森
 * @email: cg@choicesoft.com.cn
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Documented
@Inherited
public @interface FilterValidate {

    /**
     * 校验类型
     * @return
     */
    ValidateType[] value() default {ValidateType.GLOBAL_WHITE_LIST};

    /**
     * 指定校验规则
     * @return
     */
    ValidateRule rule() default ValidateRule.OR;

    /**
     * 校验Type的顺序,如果和类型数量不匹配,则默认按照添加顺序排在最后
     * @return
     */
    int[] order() default {0};
    /**
     * 校验类型
     */
    enum ValidateType{
        /**
         * 全局白名单
         */
        GLOBAL_WHITE_LIST,
        ;

    }

    /**
     * 校验规则
     */
    enum ValidateRule{
        /**
         * 与规则
         * 所有类型都全部满足后返回,否则执行原方法
         * TypeA -- if true then mark success;else process original method
         * TypeB -- if true then mark success;else process original method
         * TypeC -- if true then mark success and return;else process original method
         */
        AND,
        /**
         * 或规则
         * 所有类型只要有一个满足则返回,全部不满足则执行原方法
         * TypeA -- if true then return;else goto next
         * TypeB -- if true then return;else goto next
         * TypeC -- if true then return;else process original method
         */
        OR,
        ;
    }
}
