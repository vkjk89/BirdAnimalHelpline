package org.birdhelpline.app.controller;

import org.birdhelpline.app.model.CaseInfo;
import org.birdhelpline.app.model.PinCodeLandmarkInfo;
import org.birdhelpline.app.model.User;
import org.birdhelpline.app.service.CaseService;
import org.birdhelpline.app.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


@Controller
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private CaseService caseService;

    @RequestMapping(path = "/enableUser", method = RequestMethod.POST)
    public @ResponseBody
    String enableUser(@RequestParam("userName") String name) {
        userService.enableUser(name);
        return "SUCCESS";
    }


    @RequestMapping("/getPinCodeLandMark")
    public @ResponseBody
    List<PinCodeLandmarkInfo> getPinCodeVsLandMarks(@RequestParam("term") String term) {
        logger.info("vkj term : " + term);
        return userService.getPinCodeLandMarks(term.toLowerCase());
    }

    @RequestMapping("/getVolListForSearch")
    public @ResponseBody
    List<User> getUserList(@RequestParam("term") String term) {
        logger.info("vkj term : " + term);
        return userService.getUserList(term.toLowerCase());
    }

    @RequestMapping("/getCaseVolListForSearch")
    public @ResponseBody
    List<Object> getCaseUserList(@RequestParam("term") String term) {
        List<Object> list = new ArrayList<>();
        logger.info("vkj term : " + term);
        List<User> users = userService.getUserList(term.toLowerCase());
        List<CaseInfo> caseInfos = caseService.getAllCaseInfo(term);
        if(!CollectionUtils.isEmpty(users)) {
            list.addAll(users);
        }
        if(!CollectionUtils.isEmpty(caseInfos)) {
            list.addAll(caseInfos);
        }
        return list;
    }

}







