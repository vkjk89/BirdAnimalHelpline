package org.birdhelpline.app.controller;

import org.birdhelpline.app.model.CaseInfo;
import org.birdhelpline.app.model.User;
import org.birdhelpline.app.service.CaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CaseController {

    private static final Logger logger = LoggerFactory.getLogger(CaseController.class);

    @Autowired
    private CaseService caseService;

    @RequestMapping(value = "/addNewCase", method = RequestMethod.POST, consumes = {"application/x-www-form-urlencoded"})
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

    @RequestMapping(value = "/assignCase", method = RequestMethod.POST, consumes = {"application/x-www-form-urlencoded"})
    public @ResponseBody
    String assignCase(@RequestParam("caseId") Long caseId, @RequestParam("userId") Long toUserId, HttpSession session) {
        User user = getUser(session);
        if (user == null) {
            return "Error";
        }
        return String.valueOf(caseService.assignCase(user.getUserId(), toUserId, caseId));

    }

    @RequestMapping(value = "/closeCase", method = RequestMethod.POST, consumes = {"application/x-www-form-urlencoded"})
    public @ResponseBody
    String closeCase(@RequestParam("caseId") Long caseId, @RequestParam("closeRemark") String remark, @RequestParam("closeCaseReason") String closeReason, HttpSession session) {
        User user = getUser(session);
        if (user == null) {
            return "Error";
        }
        return String.valueOf(caseService.closeCase(user.getUserId(), caseId, remark,closeReason));

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
    List<CaseInfo> getActiveCases(HttpSession session) {
        User user = getUser(session);
        if (user == null) {
            return null;
        }
        return caseService.getActiveCaseInfoByUserId(user.getUserId());
    }

    @RequestMapping(value = "/recentCases", method = RequestMethod.GET)
    public @ResponseBody
    List<CaseInfo> getRecentCases(HttpSession session) {
        User user = getUser(session);
        if (user == null) {
            return null;
        }
        return caseService.getRecentCaseInfoByUserId(user.getUserId());
    }

    @RequestMapping(value = "/closedCases", method = RequestMethod.GET)
    public @ResponseBody
    List<CaseInfo> getClosedCases(HttpSession session) {
        User user = getUser(session);
        if (user == null) {
            return null;
        }
        return caseService.getClosedCaseInfoByUserId(user.getUserId());
    }

    private User getUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }
}
