package org.birdhelpline.app.service;

import org.birdhelpline.app.dataaccess.CaseDao;
import org.birdhelpline.app.model.CaseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service

public class CaseService {

    @Autowired
    private CaseDao caseDao;

    public CaseInfo getCaseInfoByCaseId(Long caseId) {
        return caseDao.getCaseInfoByCaseId(caseId);
    }

    public List<CaseInfo> getCaseInfoByUserId(Long userId) {
        return caseDao.getCaseInfoByUserId(userId);
    }

    @Transactional
    public Long save(CaseInfo caseInfo) {
        Long caseId = caseDao.save(caseInfo);

        return caseId;
    }
}
