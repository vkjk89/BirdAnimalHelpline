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

    Logger logger = LoggerFactory.getLogger(CaseController.class);

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

    @RequestMapping(value = "/activeCases", method = RequestMethod.GET)
    public @ResponseBody
    List<CaseInfo> getActiveCases( HttpSession session) {
        User user = getUser(session);
        if (user == null) {
            return null;
        }
        return caseService.getActiveCaseInfoByUserId(user.getUserId());
    }

    @RequestMapping(value = "/recentCases", method = RequestMethod.GET)
    public @ResponseBody
    List<CaseInfo> getRecentCases( HttpSession session) {
        User user = getUser(session);
        if (user == null) {
            return null;
        }
        return caseService.getRecentCaseInfoByUserId(user.getUserId());
    }

    @RequestMapping(value = "/closedCases", method = RequestMethod.GET)
    public @ResponseBody
    List<CaseInfo> getClosedCases( HttpSession session) {
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
