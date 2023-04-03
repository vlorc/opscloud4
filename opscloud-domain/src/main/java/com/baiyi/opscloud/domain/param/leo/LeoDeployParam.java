package com.baiyi.opscloud.domain.param.leo;

import com.baiyi.opscloud.domain.param.IExtend;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2022/12/5 17:31
 * @Version 1.0
 */
public class LeoDeployParam {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class DoDeploy {

        @Min(value = 0, message = "关联任务ID不能为空")
        @Schema(name = "关联任务ID")
        private Integer jobId;

        @Schema(name = "构建ID")
        private Integer buildId;

        @Min(value = 0, message = "Deployment资产ID不能为空")
        @Schema(name = "Deployment资产ID")
        private Integer assetId;

        @Schema(name = "部署类型")
        private String deployType;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class QueryDeployDeployment {

        @Min(value = 0, message = "任务ID不能为空")
        @Schema(name = "任务ID")
        private Integer jobId;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QueryDeployVersion implements IExtend {

        @Min(value = 0, message = "任务ID不能为空")
        @Schema(name = "任务ID")
        private Integer jobId;

        @Schema(name = "名称")
        private String queryName;

        private Boolean extend;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class QueryDeployDeploymentVersion {

        @Min(value = 0, message = "任务ID不能为空")
        @Schema(name = "任务ID")
        private Integer jobId;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class CloneDeployDeployment {

        @NotNull(message = "资产ID不能为空")
        @Min(value = 0, message = "资产ID不能为空")
        @Schema(name = "资产ID")
        private Integer assetId;

        @NotNull(message = "构建ID不能为空")
        @Schema(name = "构建ID")
        private Integer buildId;

        private Integer jobId;

        private String name;

        @Schema(name = "无状态名称")
        private String deploymentName;

        @NotNull(message = "副本数量不能为空")
        @Schema(name = "副本数量")
        private Integer replicas;
    }

}
