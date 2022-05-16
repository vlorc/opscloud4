package com.baiyi.opscloud.packer.business;

import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.business.BusinessDocumentVO;
import com.baiyi.opscloud.facade.server.SimpleServerNameFacade;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.service.server.ServerGroupService;
import com.baiyi.opscloud.service.server.ServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/5/15 19:02
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class BusinessDocumentPacker implements IWrapper<BusinessDocumentVO.Document> {

    private final ServerGroupService serverGroupService;

    private final ServerService serverService;

    @Override
    public void wrap(BusinessDocumentVO.Document document, IExtend iExtend) {
        if (document == null) return;
        if (BusinessTypeEnum.SERVER.getType() == document.getBusinessType()) {
            Server server = serverService.getById(document.getBusinessId());
            document.setDisplayName(SimpleServerNameFacade.toServerName(server));
            return;
        }
        if (BusinessTypeEnum.SERVERGROUP.getType() == document.getBusinessType()) {
            ServerGroup serverGroup = serverGroupService.getById(document.getBusinessId());
            document.setDisplayName(serverGroup.getName());
        }
    }

}
