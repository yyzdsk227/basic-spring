package com.spring.start;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice //@ControllerAdvice 어노테이션을 현재 이클래스가 컨트롤러에서 발생하는 Exception를 전문적으로 처리하는 클래스임을 알려준다.
public class CommonExceptionAdivce {

	private static final Logger logger =
			LoggerFactory.getLogger(CommonExceptionAdivce.class);
	
/*	@ExceptionHandler(Exception.class)
	public String common(Exception e){
		logger.info(e.toString());
		
		return "err_exception"; //뷰로 리턴, 커먼메소드의 주역할
	}*/
	
	@ExceptionHandler(Exception.class)
	private ModelAndView errMAV(Exception e){
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/err_exception");
		mav.addObject("exception", e);
		
		return mav;
		
	}
	
}
