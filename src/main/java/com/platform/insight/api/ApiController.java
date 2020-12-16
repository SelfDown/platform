package com.platform.insight.api;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("insight/api")
public class ApiController {

    @RequestMapping(value = "query")
    @ResponseBody
    public String query(){
        return "hello world 1 ";
    }
}
