package org.birdhelpline.app.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.birdhelpline.app.dataaccess.CaseDao;
import org.birdhelpline.app.model.CaseImage;
import org.birdhelpline.app.model.CaseInfo;
import org.birdhelpline.app.model.CaseTxn;
import org.birdhelpline.app.model.User;
import org.birdhelpline.app.utils.AnimalType;
import org.birdhelpline.app.utils.ResponseStatus;
import org.birdhelpline.app.utils.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private UserService userService;
    @Value("${maxRecentCases}")
    private long maxRecentCases;

    @Transactional
    public Long save(CaseInfo caseInfo) {
        if (StringUtils.isNotBlank(caseInfo.getNewBirdAnimal())) {
            boolean exists = userService.birdOrAnimalExists(caseInfo.getNewBirdAnimal());
            if (!exists) {
                if (AnimalType.Bird.name().equalsIgnoreCase(caseInfo.getBirdOrAnimal())) {
                    userService.addBird(caseInfo.getNewBirdAnimal());
                } else {
                    userService.addAnimal(caseInfo.getNewBirdAnimal());
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


    public List<CaseInfo> getActiveCaseInfoByUserId(Long userId, String role) {
        Boolean pendingForAck = getTypeCaseStatus(role);
        List<CaseInfo> list = getAllCaseInfo(userId, pendingForAck);
        if (list != null && !list.isEmpty()) {
            if (pendingForAck != null) { // only filter for nonAdmin and receptionist
                list = list.stream().filter(c -> c.isActive() && c.getCurrentUserId().equals(userId)).collect(Collectors.toList());
            } else {
                list = list.stream().filter(c -> c.isActive() && (c.getCurrentUserId() == 0 || c.getIsAck() < 0)).collect(Collectors.toList());
            }
            Collections.sort(list,
                    ((c1, c2) -> c2.getLastModificationDate().compareTo(c1.getLastModificationDate())));
            return list;
        }
        return Collections.EMPTY_LIST;
    }

    private Boolean getTypeCaseStatus(String role) {
        Boolean pendingForAck;
        if (Role.ADMIN.name().equalsIgnoreCase(role) || Role.Receptionist.name().equalsIgnoreCase(role)) {
            pendingForAck = null;
        } else {
            pendingForAck = false;
        }
        return pendingForAck;
    }

    public List<CaseInfo> getRecentCaseInfoByUserId(Long userId, String role) {
        Boolean pendingForAck = getTypeCaseStatus(role);
        List<CaseInfo> list = getAllCaseInfo(userId, pendingForAck);
        if (list != null && !list.isEmpty()) {
            Collections.sort(list, (c1, c2) -> c2.getLastModificationDate().compareTo(c1.getLastModificationDate()));
            list = list.stream().limit(maxRecentCases).collect(Collectors.toList());
            return list;
        }
        return Collections.EMPTY_LIST;

    }

    public List<CaseInfo> getClosedCaseInfoByUserId(Long userId, String role) {
        Boolean pendingForAck = getTypeCaseStatus(role);
        List<CaseInfo> list = getAllCaseInfo(userId, pendingForAck);
        if (list != null && !list.isEmpty()) {
            list = list.stream().filter(c -> !c.isActive()).collect(Collectors.toList());
            Collections.sort(list,
                    Comparator.comparing(caseInfo -> caseInfo.getLastModificationDate())
            );
            list = list.stream().limit(maxRecentCases).collect(Collectors.toList());
            return list;
        }
        return Collections.EMPTY_LIST;
    }

    public String getVolInfoForCase(Long caseId) {
        CaseInfo caseInfo = caseDao.getCaseInfoByCaseId(caseId);
        if (caseInfo != null) {
            List<User> top5Vol = userService.getTop5Vol();
            top5Vol = top5Vol.stream().filter(user -> !user.getRole().equals(Role.ADMIN.name()) && !user.getRole().equals(Role.Receptionist.name())).limit(5).collect(Collectors.toList());
            List<User> nearestVol = userService.getNearestVol(caseInfo.getLocationPincode());
            nearestVol = nearestVol.stream().filter(user -> !user.getRole().equals(Role.ADMIN.name()) && !user.getRole().equals(Role.Receptionist.name())).collect(Collectors.toList());
            String top5VolStr = gson.toJson(top5Vol);
            String nearestVolStr = gson.toJson(nearestVol);
            JsonObject obj = new JsonObject();
            obj.addProperty("top5", top5VolStr);
            obj.addProperty("nearest", nearestVolStr);
            return gson.toJson(obj);
        }
        return new JsonObject().toString();
    }

    public String assignCase(Long userId, Long toUserId, Long caseId, String description, Double amount, String transferDate) {
        caseDao.assignCase(userId, toUserId, caseId, description, amount, transferDate);
        /*NexmoClient client = new NexmoClient.Builder()
                .apiKey("fcca97fd")
                .apiSecret("CAIlnXiPCxrE0FhI")
                .build();

        String messageText = "Hello from Vimal + Case : "+caseId+" Assigned ";
        TextMessage message = new TextMessage("Nexmo", "918369440643", messageText);

        SmsSubmissionResponse response = null;
        try {
            response = client.getSmsClient().submitMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NexmoClientException e) {
            e.printStackTrace();
        }

        for (SmsSubmissionResponseMessage responseMessage : response.getMessages()) {
            System.out.println(responseMessage);
        }*/
        //SMSSender.sendSms();
        return ResponseStatus.SUCCESS.name();
    }

    public String closeCase(Long userId, Long caseId, String closeRemark, String closeReason, Double chargesIncurred, String transferDate) {
        caseDao.closeCase(userId, caseId, closeRemark, closeReason, chargesIncurred, transferDate);
        return ResponseStatus.SUCCESS.name();
    }

    public List<CaseInfo> getCaseInfo(Long userId, String searchTerm) {
        List<CaseInfo> list = getAllCaseInfo(userId, null);
        if (list != null && !list.isEmpty()) {
            list = list.stream().filter(c -> String.valueOf(c.getCaseId()).contains(searchTerm)).collect(Collectors.toList());
            Collections.sort(list, (c1, c2) -> c2.getLastModificationDate().compareTo(c1.getLastModificationDate()));
            return list;
        }
        return Collections.EMPTY_LIST;
    }

    public List<CaseInfo> getAllCaseInfo(String searchTerm) {
        List<CaseInfo> list = caseDao.getAllCaseInfoBySearchTerm(searchTerm);
        getUserDetailsForCaseInfo(list);
        return list;
    }


    private List<CaseInfo> getAllCaseInfo(Long userId, Boolean pendingForAck) {
        List<CaseInfo> list = caseDao.getCaseInfoByUserId(userId, pendingForAck);
        getUserDetailsForCaseInfo(list);
        return list;
    }

    private void getUserDetailsForCaseInfo(List<CaseInfo> list) {
        list.stream().forEach(c -> {
            if (c.getCurrentUserId() != null) {
                User u = userService.findUserByUserId(c.getCurrentUserId());
                if (u != null) {
                    c.setUserNameCurrent(u.getUserName());
                    c.setUserRoleCurrent(u.getRole());
                }
            }
        });
    }

    private void getUserDetailsForCaseTxn(List<CaseTxn> list) {
        list.stream().forEach(c -> {
            if (c.getFromUserId() != null) {
                User u = userService.findUserByUserId(c.getFromUserId());
                if (u != null) {
                    c.setFromUser(u.getUserName());
                    c.setFromUserRole(u.getRole());
                }
            }

            if (c.getToUserId() != null) {
                User u = userService.findUserByUserId(c.getToUserId());
                if (u != null) {
                    c.setToUser(u.getUserName());
                    c.setToUserRole(u.getRole());
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
        List<CaseInfo> list = caseDao.getCaseInfoByUserId(forUserId, true);
        if (list != null && !list.isEmpty()) {
            return list;
        }
        return Collections.EMPTY_LIST;

    }

    @Transactional
    public String acceptRejectCase(Long userId, boolean acceptReject, Long caseId) {
        caseDao.updateCaseTxn(caseId, userId, acceptReject);
        caseDao.updateUserInfo(userId, acceptReject);
        return ResponseStatus.SUCCESS.name();
    }

    public CaseInfo getCaseInfo(Long caseId) {
        return caseDao.getCaseInfoByCaseId(caseId);
    }

    public List<CaseTxn> getCaseTxn(Long caseId) {
        List<CaseTxn> list = caseDao.getCaseTxn(caseId);
        getUserDetailsForCaseTxn(list);
        return list;
    }
}
