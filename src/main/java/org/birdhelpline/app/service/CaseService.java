package org.birdhelpline.app.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.birdhelpline.app.dataaccess.CaseDao;
import org.birdhelpline.app.dataaccess.UserDao;
import org.birdhelpline.app.model.CaseImage;
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


    @Transactional
    public Long save(CaseInfo caseInfo) {
        if (StringUtils.isNotBlank(caseInfo.getNewBirdAnimal())) {
            boolean exists = userDao.birdOrAnimalExists(caseInfo.getNewBirdAnimal());
            if (!exists) {
                if ("Bird".equalsIgnoreCase(caseInfo.getBirdOrAnimal())) {
                    userDao.addBird(caseInfo.getNewBirdAnimal());
                } else {
                    userDao.addAnimal(caseInfo.getNewBirdAnimal());
                }
                caseDao.saveNewBirdAnimal(caseInfo.getBirdOrAnimal(), caseInfo.getNewBirdAnimal());
                caseInfo.setTypeAnimal(caseInfo.getNewBirdAnimal());
            }
        }
        Long caseId = caseDao.save(caseInfo);
        caseInfo.setCaseId(caseId);
        caseDao.saveCaseTxn(caseInfo);
        return caseId;
    }


    public List getActiveCaseInfoByUserId(Long userId) {
        List<CaseInfo> list = getAllCaseInfo(userId);
        if (list != null && !list.isEmpty()) {
            list = list.stream().filter(c -> c.isActive()).collect(Collectors.toList());
            Collections.sort(list,
                    ((c1, c2) -> c2.getLastModificationDate().compareTo(c1.getLastModificationDate())));
            return list;
        }
        return Collections.EMPTY_LIST;
    }

    public List getRecentCaseInfoByUserId(Long userId) {
        List<CaseInfo> list = getAllCaseInfo(userId);
        if (list != null && !list.isEmpty()) {
            Collections.sort(list, (c1, c2) -> c2.getLastModificationDate().compareTo(c1.getLastModificationDate()));
            return list;
        }
        return Collections.EMPTY_LIST;

    }

    public List getClosedCaseInfoByUserId(Long userId) {
        List<CaseInfo> list = getAllCaseInfo(userId);
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
            top5Vol = top5Vol.stream().filter(user -> !user.getRole().equals(Role.ADMIN.name()) && !user.getRole().equals(Role.Receptionist.name())).limit(5).collect(Collectors.toList());
            List<User> nearestVol = userDao.getNearestVol(caseInfo.getLocationPincode());
            nearestVol = nearestVol.stream().filter(user -> !user.getRole().equals(Role.ADMIN.name()) && !user.getRole().equals(Role.Receptionist.name())).collect(Collectors.toList());
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
        caseDao.closeCase(userId, caseId, closeRemark, closeReason);
        return "success";
    }

    public List getCaseInfo(Long userId, String searchTerm) {
        List<CaseInfo> list = getAllCaseInfo(userId);
        if (list != null && !list.isEmpty()) {
            list = list.stream().filter(c -> String.valueOf(c.getCaseId()).contains(searchTerm)).collect(Collectors.toList());
            Collections.sort(list, (c1, c2) -> c2.getLastModificationDate().compareTo(c1.getLastModificationDate()));
            return list;
        }
        return Collections.EMPTY_LIST;
    }

    public List<CaseInfo> getAllCaseInfo(String searchTerm) {
        List<CaseInfo> list = caseDao.getAllCaseInfoBySearchTerm(searchTerm);
        getUserDetailsForCase(list);
        return list;
    }


    private List<CaseInfo> getAllCaseInfo(Long userId) {
        List<CaseInfo> list = caseDao.getAllCaseInfoByUserId(userId);
        getUserDetailsForCase(list);
        return list;
    }

    private void getUserDetailsForCase(List<CaseInfo> list) {
        list.stream().forEach(c -> {
            if (c.getCurrentUserId() != null) {
                User u = userDao.getUser(c.getCurrentUserId());
                if (u != null) {
                    c.setUserNameCurrent(u.getUserName());
                    c.setUserRoleCurrent(u.getRole());
                }
            }
        });
    }

    public void saveCaseImages(CaseImage caseImage) {
        caseDao.saveCaseImages(caseImage);
    }

    public CaseImage getCaseImages(Long caseId) {
        CaseImage caseImage = new CaseImage();
        caseImage.setCaseId(caseId);
        caseDao.loadCaseImages(caseImage);
        return caseImage;
    }

    public List<CaseInfo> getPendingCaseInfo(Long forUserId) {
        List<CaseInfo> list = caseDao.getAllPendingCaseInfoByUserId(forUserId);
        if (list != null && !list.isEmpty()) {
            return list;
        }
        return Collections.EMPTY_LIST;

    }

    @Transactional
    public String acceptRejectCase(Long userId, boolean acceptReject, Long caseId) {
        caseDao.updateCaseTxn(caseId,userId,acceptReject);
        caseDao.updateUserInfo(userId,acceptReject);
        return "success";
    }
}
