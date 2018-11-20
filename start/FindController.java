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
			mav.addObject("msg", "�α��� ����");
			ModelAndView mavFail = new ModelAndView("/fbbs/index");
			return mavFail;
		}
		
		}

		
	return mav;
	}

	//ulist�� ����������
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public void list(@ModelAttribute("fCria") FindCriteria fCria, Model model) throws Exception{

		SecurityMaker sesck = new SecurityMaker();
		
		model.addAttribute("sesck",sesck);
		
		MemberVO mem = new MemberVO();		
		
		mem = sesck.securityAuthen(mem);
		
		model.addAttribute("username", mem.getUsername()); //���̵�
 		model.addAttribute("name", mem.getWriter()); 	//�ۼ���(�г�)
		
		
		model.addAttribute("list", bsvc.listFind(fCria));
		
		PagingMaker pagingMaker = new PagingMaker();
		pagingMaker.setCri(fCria);
		
		//pagingMaker.setTotalData(bsvc.listCountData(fCria));
		pagingMaker.setTotalData(bsvc.findCountData(fCria));
		
		model.addAttribute("pagingMaker", pagingMaker);
		

	}
	
	// 126~ 176
	// �����ڿ� url
	
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
	
	
	
	
	// 178~ 
	// ����ڿ� url pathVariable ��� �� ��Ÿ ���
	
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
		
		// urlâ�� �ּҰ��� �������� �ʰ� �ϱ� ����, model������ε�
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
	
	
	
	// �۾��� ������ ��û
	// �α��� �ȉ罽�ÿ� �������� 
	// �̷��Ե� ������ �����ϸ� modelandview ��õ
	@RequestMapping(value="/write", method = RequestMethod.GET)
	public void writeGET(Model model) throws Exception{
		
        SecurityMaker sesck = new SecurityMaker();
		
		model.addAttribute("sesck",sesck);
		
		MemberVO mem = new MemberVO();
		
		mem = sesck.securityAuthen(mem);
	
		model.addAttribute("secCheck", msvc.secCheck(mem.getUsername()));
			
		logger.info("writeGET() ȣ��..........");
	}
	
	
	// DB �� �Է�ó��
	@RequestMapping(value="/write", method = RequestMethod.POST)
	public String writePOST(BbsVO bvo, RedirectAttributes reAttr) throws Exception{
		
		SecurityMaker sesck = new SecurityMaker();
		
		MemberVO mem = new MemberVO();
		
		mem = sesck.securityAuthen(mem);
		
		if(msvc.secCheck(mem.getUsername()) == null){
		   return "/fbbs/index";	
		}
		
		logger.info("writePost() ȣ��...");
		
		bsvc.write(bvo);
		
		reAttr.addFlashAttribute("result", "success");
		
	
		return "redirect:/fbbs/ulist";
	}
		
	//������ �Խñ� ����
	@RequestMapping(value = "/downExcel")
    public void listExcel(HttpServletRequest request,
            HttpServletResponse response, ModelMap modelMap) throws Exception, Exception {

		
        // �׳� ��ҿ� ���̹�Ƽ������ ������ �̴� ������� �����͸� �����´�.
        List<BbsVO> dataList = bsvc.list();
      
        
        // ���� �����͸� �ʿ� ��´�.
        Map<String, Object> beans = new HashMap<String, Object>();
        beans.put("dataList", dataList);
        
        // ���� �ٿ�ε� �޼ҵ尡 ��� �ִ� ��ü
        MakeExcel me = new MakeExcel();

        me.download(request, response, beans, "temp", "temp.xlsx", "�����ص���");
    }


	//
		



}