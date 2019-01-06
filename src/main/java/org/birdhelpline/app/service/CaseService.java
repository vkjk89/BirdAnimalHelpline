package org.birdhelpline.app.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.birdhelpline.app.dataaccess.CaseDao;
import org.birdhelpline.app.dataaccess.UserDao;
import org.birdhelpline.app.model.CaseInfo;
import org.birdhelpline.app.model.User;
import org.birdhelpline.app.utils.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service

public class CaseService {

    Gson gson = new Gson();
    @Autowired
    private CaseDao caseDao;
    @Autowired
    private UserDao userDao;

    public CaseInfo getCaseInfoByCaseId(Long caseId) {
        return caseDao.getCaseInfoByCaseId(caseId);
    }


    @Transactional
    public Long save(CaseInfo caseInfo) {
        Long caseId = caseDao.save(caseInfo);
        caseInfo.setCaseId(caseId);
        caseDao.saveCaseTxn(caseInfo);
        return caseId;
    }


    public List<CaseInfo> getActiveCaseInfoByUserId(Long userId) {
        List<CaseInfo> list = caseDao.getAllCaseInfoByUserId(userId);
        if (list != null && !list.isEmpty()) {
            list = list.stream().filter(c -> c.isActive()).collect(Collectors.toList());
            Collections.sort(list,
                    ((c1, c2) -> c2.getLastModificationDate().compareTo(c1.getLastModificationDate())));
            return list;
        }
        return Collections.EMPTY_LIST;
    }

    public List<CaseInfo> getRecentCaseInfoByUserId(Long userId) {
        List<CaseInfo> list = caseDao.getAllCaseInfoByUserId(userId);
        if (list != null && !list.isEmpty()) {
            Collections.sort(list, (c1, c2) -> c2.getLastModificationDate().compareTo(c1.getLastModificationDate()));
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

    public String getVolInfoForCase(Long caseId) {
        CaseInfo caseInfo = caseDao.getCaseInfoByCaseId(caseId);
        if (caseInfo != null) {
            List<User> top5Vol = userDao.getTop5Vol();
            top5Vol = top5Vol.stream().filter(user -> {
                return !user.getRole().equals(Role.ADMIN) || !user.getRole().equals(Role.Receptionist);
            }).collect(Collectors.toList());
            List<User> nearestVol = userDao.getNearestVol(caseInfo.getLocationPincode());
            nearestVol = nearestVol.stream().filter(user -> {
                return !user.getRole().equals(Role.ADMIN) || !user.getRole().equals(Role.Receptionist);
            }).collect(Collectors.toList());
            JsonObject jsonObject = new JsonObject();
            String top5VolStr = gson.toJson(top5Vol);
            String nearestVolStr = gson.toJson(nearestVol);
            JsonObject obj = new JsonObject();
            obj.addProperty("top5", top5VolStr);
            obj.addProperty("nearest", nearestVolStr);
            return gson.toJson(obj);

        }
        return "";
    }

    public String assignCase(Long userId, Long toUserId, Long caseId) {
        caseDao.assignCase(userId, toUserId, caseId);
        return "success";
    }

    public String closeCase(Long userId, Long caseId, String closeRemark, String closeReason) {
        caseDao.closeCase(userId, caseId, closeRemark,closeReason);
        return "success";
    }

    public List<CaseInfo> getCaseInfo(Long userId, String searchTerm) {
        List<CaseInfo> list = caseDao.getAllCaseInfoByUserId(userId);
        if (list != null && !list.isEmpty()) {
            list = list.stream().filter(c -> String.valueOf(c.getCaseId()).contains(searchTerm)).collect(Collectors.toList());
            Collections.sort(list, (c1, c2) -> c2.getLastModificationDate().compareTo(c1.getLastModificationDate()));
            return list;
        }
        return Collections.EMPTY_LIST;
    }

    public List<CaseInfo> getAllCaseInfo(String searchTerm) {
        List<CaseInfo> list = caseDao.getAllCaseInfoBySearchTerm(searchTerm);
        return list;
    }
}
