package com.plug.xiaojiang.dist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.plug.xiaojiang.dist.common.tip.DistResult;
import com.plug.xiaojiang.dist.model.DisMemberAmount;
import com.plug.xiaojiang.dist.model.DisMemberInfo;
import com.plug.xiaojiang.dist.utils.http.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class IndexController {

    @Value("${dist.server.prefix}")
    private String prefix;

    @RequestMapping({"/","/index",""})
    public String index(){
        return "/index";
    }

    @RequestMapping("/main")
    public String main(String userId, HttpServletRequest request){
        DistResult result= RestClient.create(prefix+"/getUserInfo")
                .contentType(MediaType.APPLICATION_JSON)
                .acceptableMediaType(MediaType.APPLICATION_JSON)
                .addParam("userId",userId)
                .get(new ParameterizedTypeReference<DistResult>() {
                });
        if(result.isSuccess()){
            Map<String ,Object> map= (Map<String, Object>) result.getData();
            ObjectMapper  mapper=new ObjectMapper();
            DisMemberInfo member = mapper.convertValue(map.get("member"), DisMemberInfo.class);
            DisMemberAmount amount= mapper.convertValue(map.get("amount"),DisMemberAmount.class) ;
            request.getSession().setAttribute("member",member);
            request.getSession().setAttribute("amount",amount);
        }else{
            System.out.println("登录失败");
            return "/index";
        }
        return "/self";
    }
}
