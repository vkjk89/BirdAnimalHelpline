package org.birdhelpline.app.controller;

import org.birdhelpline.app.model.CaseInfo;
import org.birdhelpline.app.model.PinCodeLandmarkInfo;
import org.birdhelpline.app.model.User;
import org.birdhelpline.app.service.CaseService;
import org.birdhelpline.app.service.UserService;
import org.birdhelpline.app.utils.ResponseStatus;
import org.birdhelpline.app.utils.Role;
import org.birdhelpline.app.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private CaseService caseService;

    @RequestMapping(path = "/enableUser", method = RequestMethod.POST)
    public @ResponseBody
    String enableUser(@RequestParam("userId") Long userId, @RequestParam("acceptReject") boolean enable, HttpSession session ) {
        User user = WebUtils.getUser(session);
        if (user == null) {
            return ResponseStatus.ERROR.toString();
        }
        if(Role.ADMIN.name().equals(user.getRole())) {
            if(enable) {
                return userService.enableUser(userId);
            }
            else {
                return userService.disableUser(userId);
            }
        }
        return ResponseStatus.ERROR.name();
    }


    @RequestMapping("/getPinCodeLandMark")
    public @ResponseBody
    List<PinCodeLandmarkInfo> getPinCodeVsLandMarks(@RequestParam("term") String term,
                                                    @RequestParam(name = "selectedPinCodes", required = false) String selectedPinCodes) {
        logger.info("vkj term : " + term);
        logger.info("vkj selected : " + selectedPinCodes);
        return userService.getPinCodeLandMarks(term.toLowerCase(), selectedPinCodes);
    }

    @RequestMapping("/getVolListForSearch")
    public @ResponseBody
    List<User> getUserList(@RequestParam("term") String term, HttpSession session) {
        logger.info("vkj term : " + term);
        User user = WebUtils.getUser(session);
        if (user == null) {
            return Collections.EMPTY_LIST;
        }
        return userService.getUserList(user.getUserId(),term.toLowerCase());
    }

    @RequestMapping("/getCaseVolListForSearch")
    public @ResponseBody
    List<Object> getCaseUserList(@RequestParam("term") String term, HttpSession session) {
        User user = WebUtils.getUser(session);
        if (user == null) {
            return Collections.EMPTY_LIST;
        }
        List<Object> list = new ArrayList<>();
        logger.info("vkj term : " + term);
        List<User> users = userService.getUserList(user.getUserId(), term.toLowerCase());
        List<CaseInfo> caseInfos = caseService.getAllCaseInfo(term);
        if (!CollectionUtils.isEmpty(users)) {
            list.addAll(users);
        }
        if (!CollectionUtils.isEmpty(caseInfos)) {
            list.addAll(caseInfos);
        }
        return list;
    }

    @RequestMapping("/getUsersForActivation")
    public @ResponseBody
    List<User> getListUsersPendingForActivation(HttpSession session) {
        User user = WebUtils.getUser(session);
        if (user == null) {
            return Collections.EMPTY_LIST;
        }
        return userService.getUsersPendingForActivation();
    }
}