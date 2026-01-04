package com.springLearn.MVC.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @RequestMapping("/home")
    public String home()
    {
        System.out.println("home page requested");
        return "index";

    }

    @RequestMapping("addnum")
    public String add(HttpServletRequest req)
    {
        int num1 = Integer.parseInt( req.getParameter("num1"));
        int num2 = Integer.parseInt( req.getParameter("num2"));
        int sum = num2+num1;

        HttpSession session = req.getSession();

        session.setAttribute("sum" ,sum);

        return "result";
    }

    @RequestMapping("add2")
    public String addNumber(@RequestParam("num1") int num1,@RequestParam("num2") int num2,HttpSession session )
    {

        int sum = num2+num1;

        session.setAttribute("sum" ,sum);

        return "result";
    }

    @RequestMapping("add")
    public ModelAndView addNumberMv(@RequestParam("num1") int num1,@RequestParam("num2") int num2 )
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("result");
        int sum = num2+num1;
        mv.addObject("sum",sum);

        return mv;
    }
}
