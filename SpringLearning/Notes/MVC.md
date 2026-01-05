## MVC
1. Model
2. View 
3. Controller

```
            
Request --> |Dispatch Servlet --> User Controller|
```

* src/main/webapp
* HttpServletRequest
* HttpSession session = req.getSession();
* addNumber(@RequestParam("num1") int num1,@RequestParam("num2") int num2,HttpSession session )
* ``` 
  ModelAndView mv = new ModelAndView();
        mv.setViewName("result"); 
  ```



  