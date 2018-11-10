package org.birdhelpline.app.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.birdhelpline.app.model.PinCodeLandmarkInfo;
import org.birdhelpline.app.model.User;
import org.birdhelpline.app.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;


@Controller
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	Gson gson = new Gson();




    @RequestMapping("/getPinCodeLandMark")
    public @ResponseBody List<PinCodeLandmarkInfo>  getPinCodeVsLandMarks(@RequestParam("term") String term) {
        logger.info("vkj term : "+term);
	    return userService.getPinCodeLandMarks(term);
//        Map<Long, List<String>> pincodeVsLand = userService.getPinCodeVsLandMarks(
//        logger.info( gson.toJson(pincodeVsLand));
//        //return gson.toJson(userService.getPinCodeVsLandMarks());
//        JsonObject jsonObject = new JsonObject();
////        for(Map.Entry<Long,List<String>> entry : pincodeVsLand.entrySet()) {
////            jsonObject.addProperty("pincode",entry.getKey());
////            jsonObject.add("landmarks",gson.toJsonTree(entry.getValue()));
////        }
////        logger.info("vkj : "+jsonObject.toString());
        //return jsonObject.toString();
    }
}







