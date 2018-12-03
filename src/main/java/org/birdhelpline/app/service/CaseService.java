package org.birdhelpline.app.service;

import org.birdhelpline.app.dataaccess.CaseDao;
import org.birdhelpline.app.model.CaseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service

public class CaseService {

    @Autowired
    private CaseDao caseDao;

    public CaseInfo getCaseInfoByCaseId(Long caseId) {
        return caseDao.getCaseInfoByCaseId(caseId);
    }

    public List<CaseInfo> getActiveCaseInfoByUserId(Long userId) {
        List<CaseInfo> list = caseDao.getAllCaseInfoByUserId(userId);
        if (list != null && !list.isEmpty()) {
            Collections.sort(list.stream().filter(c -> c.isActive()).collect(Collectors.toList()),
                    ((c1, c2) -> c1.getLastModificationDate().compareTo(c2.getLastModificationDate())));
            return list;
        }
        return Collections.EMPTY_LIST;
    }

    @Transactional
    public Long save(CaseInfo caseInfo) {
        Long caseId = caseDao.save(caseInfo);
        return caseId;
    }

    public List<CaseInfo> getRecentCaseInfoByUserId(Long userId) {
        List<CaseInfo> list = caseDao.getAllCaseInfoByUserId(userId);
        if (list != null && !list.isEmpty()) {
            Collections.sort(list, (c1, c2) -> c1.getLastModificationDate().compareTo(c2.getLastModificationDate()));
            return list;
        }
        return Collections.EMPTY_LIST;

    }

    public List<CaseInfo> getClosedCaseInfoByUserId(Long userId) {
        List<CaseInfo> list = caseDao.getAllCaseInfoByUserId(userId);
        if (list != null && !list.isEmpty()) {
            list = list.stream().filter(c -> !c.isActive()).collect(Collectors.toList());
            Collections.sort(list,
                    Comparator.comparing(caseInfo -> caseInfo.getLastModificationDate())
            );
            return list;
        }
        return Collections.EMPTY_LIST;
    }
}
