package com.baiyi.opscloud.datasource.ldap.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.LdapConfig;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetRelationProvider;
import com.baiyi.opscloud.core.util.AssetUtil;
import com.baiyi.opscloud.datasource.ldap.entity.LdapGroup;
import com.baiyi.opscloud.datasource.ldap.entity.LdapPerson;
import com.baiyi.opscloud.datasource.ldap.repo.PersonRepo;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_LDAP_USER;

/**
 * @Author baiyi
 * @Date 2021/6/19 3:58 下午
 * @Version 1.0
 */
@Component
public class LdapUserProvider extends AbstractAssetRelationProvider<LdapPerson.Person, LdapGroup.Group>  {

    @Resource
    private PersonRepo personRepo;

    @Resource
    private LdapUserProvider ldapUserProvider;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.LDAP.name();
    }

    private LdapConfig.Ldap buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, LdapConfig.class).getLdap();
    }

    @Override
    protected List<LdapPerson.Person> listEntities(DsInstanceContext dsInstanceContext, LdapGroup.Group target) {
        LdapConfig.Ldap ldap = buildConfig(dsInstanceContext.getDsConfig());
        return personRepo.queryGroupMember(ldap, target.getGroupName());
    }

    @Override
    protected List<LdapPerson.Person> listEntities(DsInstanceContext dsInstanceContext) {
        return personRepo.getPersonList(buildConfig(dsInstanceContext.getDsConfig()));
    }

    @Override
    @SingleTask(name = PULL_LDAP_USER, lockTime = "5m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.USER.name();
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeConstants.GROUP.name();
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getAssetKey2(), asset.getAssetKey2())) {
            return false;
        }
        if (!AssetUtil.equals(preAsset.getName(), asset.getName())) {
            return false;
        }
        return true;
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(ldapUserProvider);
    }

}
