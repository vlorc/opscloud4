package com.baiyi.opscloud.facade.business;

import com.baiyi.opscloud.domain.vo.business.BusinessDocumentVO;

/**
 * @Author baiyi
 * @Date 2022/5/16 11:34
 * @Version 1.0
 */
public interface BusinessDocumentFacade {

    BusinessDocumentVO.Document getById(int id);

    void add(BusinessDocumentVO.Document document);

    void update(BusinessDocumentVO.Document document);

}
