package org.birdhelpline.app.controller;

import org.birdhelpline.app.model.CaseImage;
import org.birdhelpline.app.model.CaseInfo;
import org.birdhelpline.app.model.User;
import org.birdhelpline.app.service.CaseService;
import org.birdhelpline.app.utils.ResponseStatus;
import org.birdhelpline.app.utils.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
public class CaseController {

    private static final Logger logger = LoggerFactory.getLogger(CaseController.class);

    @Autowired
    private CaseService caseService;

    @RequestMapping(path = "/casePicUpload", method = RequestMethod.POST)
    public @ResponseBody
    String caseImageUpload(MultipartHttpServletRequest request) throws IOException {
        Long caseId = Long.parseLong(request.getParameter("case_id"));
        logger.info("Received case images for : " + caseId);
        List<MultipartFile> multipartFileList = request.getFiles("case-photos");
        if (multipartFileList != null) {
            CaseImage caseImage = new CaseImage();
            caseImage.setCaseId(caseId);
            List<byte[]> list = new ArrayList<>();
            for (MultipartFile file : multipartFileList) {
                list.add(file.getBytes());
            }
            caseImage.setImages(list);
            caseService.saveCaseImages(caseImage);
        }
        return ResponseStatus.SUCCESS.name();
    }

    @RequestMapping(value = "/addNewCase", method = RequestMethod.POST)
    public @ResponseBody
    String addANewCase(@ModelAttribute CaseInfo caseInfo, HttpSession session) {
        logger.info("vkj : got " + caseInfo);
        User user = getUser(session);
        if (user == null) {
            return "Error";
        }
        caseInfo.setUserIdOpened(user.getUserId());
        caseInfo.setCurrentUserId(user.getUserId());
        return String.valueOf(caseService.save(caseInfo));
    }

    @RequestMapping(value = "/assignCase", method = RequestMethod.POST)
    public @ResponseBody
    String assignCase(@RequestParam("caseId") Long caseId, @RequestParam("userId") Long toUserId, HttpSession session) {
        User user = getUser(session);
        if (user == null) {
            return "Error";
        }
        return caseService.assignCase(user.getUserId(), toUserId, caseId);
    }

    //, consumes = {"application/x-www-form-urlencoded"}
    @RequestMapping(value = "/closeCase", method = RequestMethod.POST)
    public @ResponseBody
    String closeCase(@RequestParam("caseId") Long caseId, @RequestParam("closeRemark") String remark, @RequestParam("closeCaseReason") String closeReason, HttpSession session) {
        User user = getUser(session);
        if (user == null) {
            return "Error";
        }
        return caseService.closeCase(user.getUserId(), caseId, remark, closeReason);
    }

    @RequestMapping(value = "/getCaseInfoForSearch", method = RequestMethod.GET)
    public @ResponseBody
    List<CaseInfo> getCaseInfo(@RequestParam("term") String searchTerm, HttpSession session) {
        User user = getUser(session);
        if (user == null) {
            return null;
        }
        return caseService.getCaseInfo(user.getUserId(), searchTerm);
    }

    @RequestMapping(value = "/getVolInfo", method = RequestMethod.GET)
    public @ResponseBody
    String getVolInfo(@RequestParam("caseId") Long caseId) {
        return caseService.getVolInfoForCase(caseId);
    }

    @RequestMapping(value = "/activeCases", method = RequestMethod.GET)
    public @ResponseBody
    List<CaseInfo> getActiveCases(@RequestParam(name = "userId", required = false) Long forUserId, HttpSession session) {
        User user = getUser(session);
        if (user == null) {
            return null;
        }
        if (forUserId != null && forUserId != -1) {
            if (user.getRole().equals(Role.ADMIN.name()) || user.getRole().equals(Role.Receptionist.name())) {
                return caseService.getActiveCaseInfoByUserId(forUserId, user.getRole());
            } else {
                logger.warn("Unauthorized access by : " + user.getUserName() + " for : " + forUserId);
                return null;
            }
        } else {
            return caseService.getActiveCaseInfoByUserId(user.getUserId(), user.getRole());
        }
    }

    @RequestMapping(value = "/recentCases", method = RequestMethod.GET)
    public @ResponseBody
    List<CaseInfo> getRecentCases(@RequestParam(name = "userId", required = false) Long forUserId, HttpSession session) {
        User user = getUser(session);
        if (user == null) {
            return null;
        }
        if (forUserId != null && forUserId != -1) {
            if (user.getRole().equals(Role.ADMIN.name()) || user.getRole().equals(Role.Receptionist.name())) {
                return caseService.getRecentCaseInfoByUserId(forUserId, user.getRole());
            } else {
                logger.warn("Unauthorized access by : " + user.getUserName() + " for : " + forUserId);
                return null;
            }
        } else {
            return caseService.getRecentCaseInfoByUserId(user.getUserId(), user.getRole());
        }
    }

    @RequestMapping(value = "/closedCases", method = RequestMethod.GET)
    public @ResponseBody
    List<CaseInfo> getClosedCases(@RequestParam(name = "userId", required = false) Long forUserId, HttpSession session) {
        User user = getUser(session);
        if (user == null) {
            return null;
        }
        if (forUserId != null && forUserId != -1) {
            if (user.getRole().equals(Role.ADMIN.name()) || user.getRole().equals(Role.Receptionist.name())) {
                return caseService.getClosedCaseInfoByUserId(forUserId, user.getRole());
            } else {
                logger.warn("Unauthorized access by : " + user.getUserName() + " for : " + forUserId);
                return null;
            }
        } else {
            return caseService.getClosedCaseInfoByUserId(user.getUserId(), user.getRole());
        }
    }

    private User getUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }

    @RequestMapping(path = "/getCaseImages", method = RequestMethod.GET)
    public @ResponseBody
    List<String> getCaseImages(@RequestParam("caseId") Long caseId) throws IOException {
        List<String> list = new ArrayList<>();
        CaseImage caseImage = caseService.getCaseImages(caseId);
        for (byte[] bArr : caseImage.getImages()) {
            list.add(Base64.getEncoder().encodeToString(bArr));
        }
        return list;
    }

    @RequestMapping(value = "/getPendingCases", method = RequestMethod.GET)
    public @ResponseBody
    List<CaseInfo> getPendingCases(@RequestParam(name = "userId", required = false) Long forUserId, HttpSession session) {
        User user = getUser(session);
        if (user == null) {
            return null;
        }
        if (forUserId != null && forUserId != -1) {
            if (user.getRole().equals(Role.ADMIN.name()) || user.getRole().equals(Role.Receptionist.name())) {
                return caseService.getPendingCaseInfo(forUserId);
            } else {
                logger.warn("Unauthorized access by : " + user.getUserName() + " for : " + forUserId);
                return null;
            }
        } else {
            return caseService.getPendingCaseInfo(user.getUserId());
        }
    }

    @RequestMapping(value = "/acceptRejectCase", method = RequestMethod.POST)
    public @ResponseBody
    String acceptRejectCase(@RequestParam("caseId") Long caseId, @RequestParam("acceptReject") boolean acceptReject, HttpSession session) {
        User user = getUser(session);
        if (user == null) {
            return "Error";
        }
        return caseService.acceptRejectCase(user.getUserId(), acceptReject, caseId);
    }
}
