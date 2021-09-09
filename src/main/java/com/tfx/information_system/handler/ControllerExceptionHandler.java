package com.tfx.information_system.handler;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 *拦截所有带有controller标注的控制器
 */
@ControllerAdvice
public class ControllerExceptionHandler {
    private final Logger logger= LoggerFactory.getLogger(this.getClass());
    /**
     *
     * @ExceptionHandler该注解标明标示的方法可以用做异常处理
     * Exception.class表示级别是Exception级别的
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(HttpServletRequest request, Exception e) throws Exception {
        //记录
        logger.error("Request URL :{}, Exception :{}",request.getRequestURL(),e);
        if(AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class)!=null){
            throw  e;
        }
        ModelAndView mv=new ModelAndView();
        mv.addObject("url",request.getRequestURL());
        mv.addObject("exception",e);
        mv.setViewName("error/error");
        return mv;
    }
}
