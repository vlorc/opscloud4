package com.baiyi.opscloud.datasource.aws.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.core.util.AssetUtil;
import com.baiyi.opscloud.datasource.aws.domain.driver.AmazonDomainDriver;
import com.baiyi.opscloud.datasource.aws.domain.entity.AmazonDomain;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_AWS_DOMAIN;

/**
 * @Author baiyi
 * @Date 2022/4/18 17:30
 * @Version 1.0
 */
@Slf4j
@Component
public class AwsDomainProvider extends BaseAssetProvider<AmazonDomain.Domain> {

    @Resource
    private AwsDomainProvider awsDomainProvider;

    @Override
    @SingleTask(name = PULL_AWS_DOMAIN, lockTime = "1m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private AwsConfig.Aws buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, AwsConfig.class).getAws();
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getName(), asset.getName())) {
            return false;
        }
        if (!AssetUtil.equals(preAsset.getExpiredTime(), asset.getExpiredTime())) {
            return false;
        }
        return true;
    }

    @Override
    protected List<AmazonDomain.Domain> listEntities(DsInstanceContext dsInstanceContext) {
        AwsConfig.Aws aws = buildConfig(dsInstanceContext.getDsConfig());
        return AmazonDomainDriver.listDomains(aws);
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.AWS.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.AMAZON_DOMAIN.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(awsDomainProvider);
    }

}

