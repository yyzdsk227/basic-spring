package com.spring.start;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.service.BbsService;
import com.spring.service.MemberService;
import com.spring.service.ReplyService;
import com.spring.vo.FindCriteria;
import com.spring.vo.PageCriteria;
import com.spring.vo.PagingMaker;
import com.spring.vo.ReplyVO;
import com.spring.vo.BbsVO;
import com.spring.vo.MemberVO;
import com.spring.vo.SecurityMaker;

@RestController
@RequestMapping("/fbbs/replies/*")

public class ReplyController {

	@Inject
	private ReplyService rsvc;
	
	@Inject
	private MemberService msvc;
	
	
	@RequestMapping(value="/{bid}", method=RequestMethod.GET)
    public ModelAndView reply(@PathVariable("bid") int bid, @ModelAttribute("rvo") ReplyVO rvo) throws Exception{
		
		ModelAndView mav = new ModelAndView("/fbbs/replies");
		
		SecurityMaker sesck = new SecurityMaker();
		
		mav.addObject("sesck",sesck);
	
		MemberVO mem = new MemberVO();
			
		mem = sesck.securityAuthen(mem);
		
		mav.addObject("secCheck", msvc.secCheck(mem.getUsername()));
		
		System.out.println(rvo.getBid());
		
		return mav;
    }
	
	@RequestMapping(value="/{bid}", method=RequestMethod.POST)
	
	public ResponseEntity<String> input(@PathVariable("bid") Integer bid, @RequestBody ReplyVO rvo){
	
			
		ResponseEntity<String> resEntity = null;
		
		try {
			rvo.setBid(bid);
			rsvc.inputReply(rvo);
			resEntity  = new ResponseEntity<String>("Success", HttpStatus.OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		return resEntity;
	}
	
	@RequestMapping(value="/selectAll/{bid}", method=RequestMethod.GET)
	public ResponseEntity<List<ReplyVO>> list(@PathVariable("bid") Integer bid){
		ResponseEntity<List<ReplyVO>> resEntity = null;
	
		try { //왼쪽파라미터 위쪽 선언 부분 자료형과 동일
			resEntity = new ResponseEntity<>(rsvc.replyList(bid), HttpStatus.OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return resEntity;
	
	}
	
	
	@RequestMapping(value="/{rebid}", method=RequestMethod.PATCH)
	public ResponseEntity<String> modify(@PathVariable("rebid") Integer rebid,
			 @RequestBody ReplyVO rvo){
	
		 ResponseEntity<String> resEntity = null;
	
		 try {

			 rvo.setRebid(rebid);
			 rsvc.modifyReply(rvo);
			 
			 resEntity = new ResponseEntity<String>("Success", HttpStatus.OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		 
		 return resEntity;
	
 }
	
	@RequestMapping(value="/{rebid}", method=RequestMethod.DELETE)
	public ResponseEntity<String> reDel(@PathVariable("rebid") Integer rebid) {
		
		ResponseEntity<String> resEntity = null;
		
		try{
			rsvc.delReply(rebid);
			resEntity = new ResponseEntity<String>("Success", HttpStatus.OK);			
		}catch (Exception e){
			e.printStackTrace();
			resEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
			
		}
		
		return resEntity;
	}	
	
}