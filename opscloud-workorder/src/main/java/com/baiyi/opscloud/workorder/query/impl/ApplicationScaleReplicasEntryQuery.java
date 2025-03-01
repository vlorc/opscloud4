package com.baiyi.opscloud.workorder.query.impl;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.ApplicationResource;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAssetProperty;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;
import com.baiyi.opscloud.service.application.ApplicationResourceService;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetPropertyService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.entry.ApplicationScaleReplicasEntry;
import com.baiyi.opscloud.workorder.query.impl.base.BaseTicketEntryQuery;
import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import jakarta.annotation.Resource;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/7/18 10:10
 * @Version 1.0
 */
@Component
public class ApplicationScaleReplicasEntryQuery extends BaseTicketEntryQuery<ApplicationScaleReplicasEntry.KubernetesDeployment> {

    @Resource
    private ApplicationResourceService applicationResourceService;

    @Resource
    private ApplicationService applicationService;

    @Resource
    private DsInstanceAssetService dsInstanceAssetService;

    @Resource
    private DsInstanceAssetPropertyService dsInstanceAssetPropertyService;

    @Override
    protected List<ApplicationScaleReplicasEntry.KubernetesDeployment> queryEntries(WorkOrderTicketEntryParam.EntryQuery entryQuery) {
        DsAssetParam.AssetPageQuery pageQuery = getAssetQueryParam(entryQuery);
        DataTable<DatasourceInstanceAsset> dataTable = dsInstanceAssetService.queryPageByParam(pageQuery);
        return dataTable.getData().stream().map(e -> {
            Optional<DatasourceInstanceAssetProperty> optionalProperty = dsInstanceAssetPropertyService.queryByAssetId(e.getId())
                    .stream()
                    .filter(p -> p.getName().equals("replicas"))
                    .findFirst();
            int replicas = 0;
            if (optionalProperty.isPresent()) {
                replicas = Integer.parseInt(optionalProperty.get().getValue());
            }
            return ApplicationScaleReplicasEntry.KubernetesDeployment.builder()
                    .id(e.getId())
                    .instanceUuid(entryQuery.getInstanceUuid())
                    .name(e.getAssetId())
                    .deploymentName(e.getAssetKey())
                    .namespace(e.getAssetKey2())
                    .replicas(replicas)
                    .scaleReplicas(replicas + 1)
                    .comment(getApplicationComment(e.getId()))
                    .build();
        }).collect(Collectors.toList());
    }

    private String getApplicationComment(Integer assetId) {
        List<ApplicationResource> resources = applicationResourceService.queryByBusiness(BusinessTypeEnum.ASSET.getType(), assetId);
        return CollectionUtils.isEmpty(resources) ? "" : applicationService.getById(resources.get(0).getApplicationId()).getComment();
    }

    public static String getComment(ApplicationScaleReplicasEntry.KubernetesDeployment entry) {
        String c = "已创建{}个,总共需要{}个";
        String desc = StringFormatter.arrayFormat(c, entry.getReplicas(), entry.getScaleReplicas());
        if (StringUtils.isNotBlank(entry.getComment())) {
            return Joiner.on("").skipNulls().join(entry.getComment(), "(", desc, ")");
        } else {
            return desc;
        }
    }

    @Override
    protected WorkOrderTicketVO.Entry<ApplicationScaleReplicasEntry.KubernetesDeployment> toEntry(WorkOrderTicketEntryParam.EntryQuery entryQuery, ApplicationScaleReplicasEntry.KubernetesDeployment entry) {
        return WorkOrderTicketVO.Entry.<ApplicationScaleReplicasEntry.KubernetesDeployment>builder()
                .workOrderTicketId(entryQuery.getWorkOrderTicketId())
                .name(entry.getName())
                .entryKey(entry.getDeploymentName())
                .businessType(BusinessTypeEnum.ASSET.getType())
                .businessId(entry.getId())
                .content(JSONUtil.writeValueAsString(entry))
                .comment(getComment(entry))
                .entry(entry)
                .build();
    }

    private DsAssetParam.AssetPageQuery getAssetQueryParam(WorkOrderTicketEntryParam.EntryQuery entryQuery) {
        return DsAssetParam.AssetPageQuery.builder()
                .instanceUuid(entryQuery.getInstanceUuid())
                .assetType(DsAssetTypeConstants.KUBERNETES_DEPLOYMENT.name())
                .queryName(entryQuery.getQueryName())
                .isActive(true)
                .page(1)
                .length(entryQuery.getLength())
                .build();
    }

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.APPLICATION_SCALE_REPLICAS.name();
    }

}