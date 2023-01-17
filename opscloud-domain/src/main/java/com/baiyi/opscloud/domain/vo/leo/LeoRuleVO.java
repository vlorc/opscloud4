package com.baiyi.opscloud.domain.vo.leo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2023/1/10 17:48
 * @Version 1.0
 */
public class LeoRuleVO {


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Rule {

        private Integer id;

        private String name;

        private Boolean isActive;

        private String ruleConfig;

        private String comment;

        @ApiModelProperty(value = "显示名称")
        private String displayName;

    }

}
