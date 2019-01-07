package com.spring.start;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.service.BbsService;
import com.spring.service.MemberService;
import com.spring.service.ReplyService;
import com.spring.vo.BbsVO;
import com.spring.vo.ReplyVO;
import com.spring.vo.FindCriteria;
import com.spring.vo.MemberVO;
import com.spring.vo.PagingMaker;
import com.spring.vo.RoleVO;
import com.spring.vo.SecurityMaker;
import com.spring.vo.etc.MakeExcel;

@Controller
@RequestMapping("/fbbs/*")
public class FindController {
	private static final Logger logger = LoggerFactory.getLogger(FindController.class);
	
	@Inject
	private BbsService bsvc;
	
	@Inject
	private MemberService msvc;
	
	@Inject
	private ReplyService rsvc;
	
	@RequestMapping("/index")
	public ModelAndView main(
			@ModelAttribute("mvo") MemberVO mvo){	
		ModelAndView mav = new ModelAndView("/fbbs/index");
		
		
	
		return mav;
	}
	
	
	@RequestMapping("/login")
	public ModelAndView login(
			@RequestParam(value="error", required=false)String error ,
			@ModelAttribute("fCria") FindCriteria fCria
		) throws Exception {
		
		ModelAndView mav =  new ModelAndView("/fbbs/index");
				
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(null != authentication && null != mav) { 
			
			Object principal = authentication.getPrincipal();
			System.out.println("prin");
			
			if(principal instanceof MemberVO) {
				
				MemberVO mem = (MemberVO)principal;
				System.out.println("login");			
				
				if(null != mem) {System.out.println(mem.getUsername());}
					
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		/*boolean authorized = authorities.equals(new SimpleGrantedAuthority("admin"));*/
		boolean authorized = authorities.contains(new SimpleGrantedAuthority("admin"));
		boolean authorized2 = authorities.contains(new SimpleGrantedAuthority("user"));
		
		if(authorized){
			ModelAndView mavAdmin = new ModelAndView("/fbbs/list");
			
			mavAdmin.addObject("username", mem.getUsername());
			mavAdmin.addObject("name", mem.getWriter());
			
			System.out.println("admin");
			mavAdmin.addObject("list", bsvc.listFind(fCria));
			
			PagingMaker pagingMaker = new PagingMaker();
			pagingMaker.setCri(fCria);
			
			//pagingMaker.setTotalData(bsvc.listCountData(fCria));
			pagingMaker.setTotalData(bsvc.findCountData(fCria));
			
			mavAdmin.addObject("pagingMaker", pagingMaker);
			
			return mavAdmin;
		}
		
		if(authorized2){
			ModelAndView mavUser = new ModelAndView("/fbbs/ulist");
			
			mavUser.addObject("username", mem.getUsername());
			mavUser.addObject("name", mem.getWriter());
			
			System.out.println("user");
			mavUser.addObject("ulist", bsvc.listFind(fCria));
			
			PagingMaker pagingMaker = new PagingMaker();
			pagingMaker.setCri(fCria);
			
			//pagingMaker.setTotalData(bsvc.listCountData(fCria));
			pagingMaker.setTotalData(bsvc.findCountData(fCria));
			
			mavUser.addObject("pagingMaker", pagingMaker);
			
			return mavUser;
		
		}
		
		}
		if(null != error) {
			mav.addObject("msg", "로그인 실패");
			ModelAndView mavFail = new ModelAndView("/fbbs/index");
			return mavFail;
		}
		
		}

		
	return mav;
	}

	//ulist도 마찬가지로
	//로그인 안해도 내용
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public void list(@ModelAttribute("fCria") FindCriteria fCria, Model model) throws Exception{

		SecurityMaker sesck = new SecurityMaker();
		
		model.addAttribute("sesck",sesck);
		
		MemberVO mem = new MemberVO();		
		
		mem = sesck.securityAuthen(mem);
		
		model.addAttribute("username", mem.getUsername()); //아이디
 		model.addAttribute("name", mem.getWriter()); 	//작성자(닉넴)
		
		
		model.addAttribute("list", bsvc.listFind(fCria));
		
		PagingMaker pagingMaker = new PagingMaker();
		pagingMaker.setCri(fCria);
		
		//pagingMaker.setTotalData(bsvc.listCountData(fCria));
		pagingMaker.setTotalData(bsvc.findCountData(fCria));
		
		model.addAttribute("pagingMaker", pagingMaker);
		

	}
	
	
	@RequestMapping(value="/readPage", method = RequestMethod.GET)
	public void readPage(@RequestParam("bid") int bid, @ModelAttribute("fCria") FindCriteria fCria, Model model)
	 throws Exception{
		model.addAttribute(bsvc.read(bid));
	}

	
	@RequestMapping(value="/delPage", method = RequestMethod.POST)
	public String delPage(@RequestParam("bid") int bid, FindCriteria fCria,
			RedirectAttributes reAttr) throws Exception{
		
		bsvc.remove(bid);
		
		reAttr.addAttribute("page", fCria.getPage());
		reAttr.addAttribute("numPerPage", fCria.getNumPerPage());
		reAttr.addAttribute("findType", fCria.getFindType());
		reAttr.addAttribute("keyword", fCria.getKeyword());
		
		reAttr.addFlashAttribute("result", "success");
		
		return "redirect:/fbbs/list";
	}
	
	
	
	
	
	@RequestMapping(value="/ulist", method = RequestMethod.GET)
	public void ulist(@ModelAttribute("fCria") FindCriteria fCria,
			Model model) throws Exception{

		SecurityMaker sesck = new SecurityMaker();
		
		model.addAttribute("sesck",sesck);
		
		MemberVO mem = new MemberVO();		
		
		mem = sesck.securityAuthen(mem);
		
		model.addAttribute("username", mem.getUsername());
		model.addAttribute("name", mem.getWriter());
		
		model.addAttribute("secCheck", msvc.secCheck(mem.getUsername()));
		
		model.addAttribute("ulist", bsvc.listFind(fCria));
		
		PagingMaker pagingMaker = new PagingMaker();
		pagingMaker.setCri(fCria);
		
		//pagingMaker.setTotalData(bsvc.listCountData(fCria));
		pagingMaker.setTotalData(bsvc.findCountData(fCria));
		
		model.addAttribute("pagingMaker", pagingMaker);
	}
	
	
	@RequestMapping(value="/ureadPage", method = RequestMethod.GET)
	public ModelAndView ureadPage(@RequestParam("bid") int bid,
			@ModelAttribute("fCria") FindCriteria fCria,
			@ModelAttribute("rvo") ReplyVO rvo)throws Exception{
		
		ModelAndView mav = new ModelAndView("/fbbs/ureadPage");
		
		SecurityMaker sesck = new SecurityMaker();
		
		mav.addObject("sesck",sesck);
		
		MemberVO mem = new MemberVO();
		
		mem = sesck.securityAuthen(mem);
	
		
		mav.addObject("secCheck", msvc.secCheck(mem.getUsername()));
		
		mav.addObject("relist", rsvc.replyList(bid));
	
		
		if(mav != null){
			mav.addObject("username", mem.getUsername());
			mav.addObject("name", mem.getWriter());
			
			System.out.println(mem.getWriter());
			mav.addObject(bsvc.read(bid));
		}
				
		return mav;
	}


	@RequestMapping(value="/umodifyPage", method=RequestMethod.GET)
	public void umodifyGET(int bid, @ModelAttribute("fCria") FindCriteria fCri, Model model) throws Exception{
		
		SecurityMaker sesck = new SecurityMaker();
		
		model.addAttribute("sesck",sesck);
		
		MemberVO mem = new MemberVO();
		
		mem = sesck.securityAuthen(mem);
	
		model.addAttribute("secCheck", msvc.secCheck(mem.getUsername()));
		
		model.addAttribute(bsvc.read(bid));
	}
	
		
	@RequestMapping(value="/umodifyPage", method=RequestMethod.POST)
	public String umodifyPOST(BbsVO bvo, FindCriteria fCria, RedirectAttributes reAttr) throws Exception{
		logger.info(fCria.toString());
		
		bsvc.modify(bvo);
		
		reAttr.addAttribute("page", fCria.getPage());
		reAttr.addAttribute("numPerPage", fCria.getNumPerPage());
		reAttr.addAttribute("findType", fCria.getFindType());
		reAttr.addAttribute("keyword", fCria.getKeyword());
		
		// url창에 주소값이 남아있지 않게 하기 위해, model대신으로도
		reAttr.addFlashAttribute("result", "success");
		
		logger.info(reAttr.toString());
		
		return "redirect:/fbbs/ulist";
		
	}
	
	@RequestMapping(value="/udelPage", method = RequestMethod.POST)
	public String udelPage(@RequestParam("bid") int bid, FindCriteria fCria,
			RedirectAttributes reAttr) throws Exception{
		
		bsvc.remove(bid);
		
		reAttr.addAttribute("page", fCria.getPage());
		reAttr.addAttribute("numPerPage", fCria.getNumPerPage());
		reAttr.addAttribute("findType", fCria.getFindType());
		reAttr.addAttribute("keyword", fCria.getKeyword());
		
		reAttr.addFlashAttribute("result", "success");
		
		return "redirect:/fbbs/ulist";
	}
	
	
	
	// 글쓰기 페이지 요청
	// 로그인 안營슬첼 보기전용 
	// 이렇게도 되지만 웬만하면 modelandview 추천
	@RequestMapping(value="/write", method = RequestMethod.GET)
	public void writeGET(Model model) throws Exception{
		
        SecurityMaker sesck = new SecurityMaker();
		
		model.addAttribute("sesck",sesck);
		
		MemberVO mem = new MemberVO();
		
		mem = sesck.securityAuthen(mem);
	
		model.addAttribute("secCheck", msvc.secCheck(mem.getUsername()));
			
		logger.info("writeGET() 호출..........");
	}
	
	
	// DB 글 입력처리
	@RequestMapping(value="/write", method = RequestMethod.POST)
	public String writePOST(BbsVO bvo, RedirectAttributes reAttr) throws Exception{
		
		SecurityMaker sesck = new SecurityMaker();
		
		MemberVO mem = new MemberVO();
		
		mem = sesck.securityAuthen(mem);
		
		if(msvc.secCheck(mem.getUsername()) == null){
		   return "/fbbs/index";	
		}
		
		logger.info("writePost() 호출...");
		
		bsvc.write(bvo);
		
		reAttr.addFlashAttribute("result", "success");
		
	
		return "redirect:/fbbs/ulist";
	}
		
	//엑셀로 게시글 보기
	@RequestMapping(value = "/downExcel")
    public void listExcel(HttpServletRequest request,
            HttpServletResponse response, ModelMap modelMap) throws Exception, Exception {

		
        // 그냥 평소에 마이바티스에서 데이터 뽑는 방법으로 데이터를 가져온다.
        List<BbsVO> dataList = bsvc.list();
      
        
        // 받은 데이터를 맵에 담는다.
        Map<String, Object> beans = new HashMap<String, Object>();
        beans.put("dataList", dataList);
        
        // 엑셀 다운로드 메소드가 담겨 있는 객체
        MakeExcel me = new MakeExcel();

        me.download(request, response, beans, "temp", "temp.xlsx", "무시해도됨");
    }


	//
		



}
