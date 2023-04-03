package com.baiyi.opscloud.domain.vo.auth;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.base.ReadableTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2022/9/19 09:36
 * @Version 1.0
 */
public class AuthPlatformVO {

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Platform {

        @Schema(name = "平台ID")
        private Integer platformId;

        @Schema(name = "平台名称")
        private String name;

        @Schema(name = "平台说明")
        private String comment;

    }

    @EqualsAndHashCode(callSuper = true)
    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AuthPlatformLog extends BaseVO implements ReadableTime.IAgo {

        private Integer id;

        @Schema(name = "平台ID")
        private Integer platformId;

        @Schema(name = "平台名称")
        private String platformName;

        @Schema(name = "认证用户名")
        private String username;

        private Boolean otp;

        @Schema(name = "认证结果")
        private Boolean result;

        @Schema(name = "认证消息")
        private String resultMsg;

        private String ago;

        @Override
        public Date getAgoTime() {
            return getCreateTime();
        }

    }

}
