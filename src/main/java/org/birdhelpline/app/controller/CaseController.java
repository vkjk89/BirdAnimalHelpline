package org.birdhelpline.app.controller;

import org.birdhelpline.app.model.*;
import org.birdhelpline.app.service.CaseService;
import org.birdhelpline.app.service.UserService;
import org.birdhelpline.app.utils.ResponseStatus;
import org.birdhelpline.app.utils.Role;
import org.birdhelpline.app.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

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

    @Autowired
    private UserService userService;

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
        User user = WebUtils.getUser(session);
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
        User user = WebUtils.getUser(session);
        if (user == null) {
            return "Error";
        }
        return caseService.assignCase(user.getUserId(), toUserId, caseId, null, null, null);
    }

    //, consumes = {"application/x-www-form-urlencoded"}
    @RequestMapping(value = "/closeCase", method = RequestMethod.POST)
    public @ResponseBody
    String closeCase(@RequestParam("caseId") Long caseId, @RequestParam("closeRemark") String remark, @RequestParam("closeCaseReason") String closeReason, HttpSession session) {
        User user = WebUtils.getUser(session);
        if (user == null) {
            return "Error";
        }
        return caseService.closeCase(user.getUserId(), caseId, remark, closeReason, null, null);
    }

    @RequestMapping(value = "/getCaseTxn", method = RequestMethod.GET)
    public @ResponseBody
    List<CaseTxn> getCaseTxn(@RequestParam("caseId") Long caseId, HttpSession session) {
        User user = WebUtils.getUser(session);
        if (user == null) {
            return null;
        }
        return caseService.getCaseTxn(caseId);
    }

    @RequestMapping(value = "/getCaseInfoForSearch", method = RequestMethod.GET)
    public @ResponseBody
    List<CaseInfo> getCaseInfo(@RequestParam("term") String searchTerm, HttpSession session) {
        User user = WebUtils.getUser(session);
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
        User user = WebUtils.getUser(session);
        if (user == null) {
            return null;
        }
        if (forUserId != null && forUserId != -1) {
            User forUser = userService.findUserByUserId(forUserId);
            if (forUser == null) {
                return null;
            }
            if (user.getRole().equals(Role.ADMIN.name()) || user.getRole().equals(Role.Receptionist.name())) {
                return caseService.getActiveCaseInfoByUserId(forUserId, forUser.getRole());
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
        User user = WebUtils.getUser(session);
        if (user == null) {
            return null;
        }
        if (forUserId != null && forUserId != -1) {
            User forUser = userService.findUserByUserId(forUserId);
            if (forUser == null) {
                return null;
            }
            if (user.getRole().equals(Role.ADMIN.name()) || user.getRole().equals(Role.Receptionist.name())) {
                return caseService.getRecentCaseInfoByUserId(forUserId, forUser.getRole());
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
        User user = WebUtils.getUser(session);
        if (user == null) {
            return null;
        }
        if (forUserId != null && forUserId != -1) {
            User forUser = userService.findUserByUserId(forUserId);
            if (forUser == null) {
                return null;
            }
            if (user.getRole().equals(Role.ADMIN.name()) || user.getRole().equals(Role.Receptionist.name())) {
                return caseService.getClosedCaseInfoByUserId(forUserId, forUser.getRole());
            } else {
                logger.warn("Unauthorized access by : " + user.getUserName() + " for : " + forUserId);
                return null;
            }
        } else {
            return caseService.getClosedCaseInfoByUserId(user.getUserId(), user.getRole());
        }
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
        User user = WebUtils.getUser(session);
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
        User user = WebUtils.getUser(session);
        if (user == null) {
            return "Error";
        }
        return caseService.acceptRejectCase(user.getUserId(), acceptReject, caseId);
    }

    @RequestMapping(value = "/caseDetails", method = RequestMethod.GET)
    public ModelAndView caseDetails(@RequestParam("caseId") Long caseId, HttpSession session) {
        UserAndModelView userAndModelView = new UserAndModelView(caseId, session).invoke();
        if (userAndModelView.is()) return new ModelAndView("Error");
        ModelAndView modelAndView = userAndModelView.getModelAndView();
        modelAndView.setViewName("Vol_Dashboard/vol-dashboard-my-cases");
        return modelAndView;
    }

    @RequestMapping(value = "/updateCase", method = RequestMethod.GET)
    public ModelAndView updateCase(@RequestParam("caseId") Long caseId, HttpSession session) {
        UserAndModelView userAndModelView = new UserAndModelView(caseId, session).invoke();
        if (userAndModelView.is()) return new ModelAndView("Error");
        ModelAndView modelAndView = userAndModelView.getModelAndView();
        modelAndView.setViewName("Vol_Dashboard/vol-dashboard-update-center");
        return modelAndView;
    }

    @RequestMapping(value = "/updateCase", method = RequestMethod.POST)
    public ModelAndView updateCasePost(@ModelAttribute CaseUpdateVO caseUpdateVO, HttpSession session) {
        logger.info("vkj update case : " + caseUpdateVO);
        UserAndModelView userAndModelView = new UserAndModelView(caseUpdateVO.getCaseId(), session).invoke();
        if (userAndModelView.is()) return new ModelAndView("Error");
        User fromUser = userAndModelView.getUser();

        if (caseUpdateVO.getAction().equalsIgnoreCase("assign")) {
            caseService.assignCase(fromUser.getUserId(), caseUpdateVO.getAssignedUserId(), caseUpdateVO.getCaseId(), caseUpdateVO.getCaseSummary(), caseUpdateVO.getChargesIncurred(), caseUpdateVO.getCaseUpdateDate());
        } else if (caseUpdateVO.getAction().equalsIgnoreCase("close")) {
            String closeReason = caseUpdateVO.getCaseCloseReason();
            if (closeReason.equalsIgnoreCase("other")) {
                closeReason = caseUpdateVO.getCaseCloseReasonOther();
            }
            caseService.closeCase(fromUser.getUserId(), caseUpdateVO.getCaseId(), caseUpdateVO.getCaseSummary(), closeReason, caseUpdateVO.getChargesIncurred(), caseUpdateVO.getCaseUpdateDate());
        } else {
            return new ModelAndView("Error");
        }

        ModelAndView modelAndView = userAndModelView.getModelAndView();
        modelAndView.setViewName("redirect:/default");
        return modelAndView;
    }


    private class UserAndModelView {
        private boolean myResult;
        private Long caseId;
        private HttpSession session;
        private ModelAndView modelAndView;
        private User user;
        private CaseInfo caseInfo;

        public UserAndModelView(Long caseId, HttpSession session) {
            this.caseId = caseId;
            this.session = session;
        }

        boolean is() {
            return myResult;
        }

        public ModelAndView getModelAndView() {
            return modelAndView;
        }

        public UserAndModelView invoke() {
            User user = WebUtils.getUser(session);
            if (user == null) {
                myResult = true;
                return this;
            }
            this.user = user;
            CaseInfo caseInfo = caseService.getCaseInfo(caseId);
            if (caseInfo == null) {
                myResult = true;
                return this;
            }
            this.caseInfo = caseInfo;
            modelAndView = new ModelAndView();
            modelAndView.addObject("caseInfo", caseInfo);
            modelAndView.addObject("user", user);
            myResult = false;
            return this;
        }

        public User getUser() {
            return user;
        }

        public CaseInfo getCaseInfo() {
            return caseInfo;
        }
    }
}
