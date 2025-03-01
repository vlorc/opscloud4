package com.baiyi.opscloud.datasource.nacos.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.SingleTaskConstants;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.NacosConfig;
import com.baiyi.opscloud.core.exception.DatasourceProviderException;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.core.util.AssetUtil;
import com.baiyi.opscloud.datasource.nacos.driver.NacosClusterDriver;
import com.baiyi.opscloud.datasource.nacos.entity.NacosCluster;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/12 1:59 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class NacosClusterNodeProvider extends BaseAssetProvider<NacosCluster.Node> {

    @Resource
    private NacosClusterNodeProvider nacosClusterNodeProvider;

    @Resource
    private NacosClusterDriver nacosClusterDriver;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.NACOS.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.NACOS_CLUSTER_NODE.name();
    }

    private NacosConfig.Nacos buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, NacosConfig.class).getNacos();
    }

    @Override
    protected List<NacosCluster.Node> listEntities(DsInstanceContext dsInstanceContext) {
        try {
            NacosCluster.NodesResponse nodesResponse = nacosClusterDriver.listNodes(buildConfig(dsInstanceContext.getDsConfig()));
            return nodesResponse.getCode() == 200 ? nodesResponse.getData() : Collections.emptyList();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new DatasourceProviderException(e.getMessage());
        }
    }

    @Override
    @SingleTask(name = SingleTaskConstants.PULL_NACOS_CLUSTER_NODE, lockTime = "1m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getName(), asset.getName())) {
            return false;
        }
        if (!preAsset.getIsActive().equals(asset.getIsActive())) {
            return false;
        }
        return true;
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(nacosClusterNodeProvider);
    }
}
