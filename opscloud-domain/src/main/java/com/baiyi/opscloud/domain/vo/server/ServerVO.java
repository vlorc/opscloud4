package com.baiyi.opscloud.domain.vo.server;

import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.business.BusinessAssetRelationVO;
import com.baiyi.opscloud.domain.vo.business.BusinessDocumentVO;
import com.baiyi.opscloud.domain.vo.business.BusinessPropertyVO;
import com.baiyi.opscloud.domain.vo.env.EnvVO;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/21 4:56 下午
 * @Version 1.0
 */
public class ServerVO {

    @EqualsAndHashCode(callSuper = true)
    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Server extends BaseVO implements
            EnvVO.IEnv,
            TagVO.ITags,
            ServerGroupVO.IServerGroup,
            ServerAccountVO.IAccount,
            // 资产与业务对象绑定关系
            BusinessAssetRelationVO.IBusinessAssetRelation,
            BusinessPropertyVO.IBusinessProperty,
            BusinessDocumentVO.IBusinessDocument,
            Serializable {

        @Override
        public String getBusinessUniqueKey() {
            return privateIp;
        }

        @Serial
        private static final long serialVersionUID = -1011261913967456450L;

        private BusinessDocumentVO.Document document;

        private List<TagVO.Tag> tags;

        private EnvVO.Env env;

        private ServerGroupVO.ServerGroup serverGroup;

        private final Integer businessType = BusinessTypeEnum.SERVER.getType();

        private List<ServerAccountVO.Account> accounts;

        private BusinessPropertyVO.Property businessProperty;

        @Override
        public Integer getServerId() {
            return id;
        }

        @Override
        public Integer getBusinessId() {
            return id;
        }

        @Schema(description = "主键", example = "1")
        @Builder.Default
        private Integer id = 0;

        @Schema(description = "服务器名称")
        @NotBlank(message = "服务器名称不能为空")
        private String name;

        @Schema(description = "显示名称")
        private String displayName;

        @Schema(description = "服务器组id", example = "1")
        @NotNull(message = "服务器组不能为空")
        private Integer serverGroupId;

        @Schema(description = "环境类型", example = "1")
        @NotNull(message = "环境类型不能为空")
        private Integer envType;

        @Schema(description = "公网IP")
        private String publicIp;

        @Schema(description = "私网IP")
        @NotBlank(message = "私网ip不能为空")
        private String privateIp;

        @Schema(description = "服务器类型", example = "1")
        @NotNull(message = "服务器类型不能为空")
        @Builder.Default
        private Integer serverType = 0;

        @Schema(description = "地区")
        private String area;

        @Schema(description = "系统类型")
        private String osType;

        @Schema(description = "序号", example = "1")
        @Builder.Default
        private Integer serialNumber = 0;

        @Schema(description = "监控状态", example = "1")
        private Integer monitorStatus;

        @Schema(description = "资源描述")
        private String comment;

        @Schema(description = "服务器状态", example = "1")
        private Integer serverStatus;

        @Schema(description = "有效")
        @Builder.Default
        private Boolean isActive = true;

        @Schema(description = "资产ID")
        private Integer assetId;

    }

}
