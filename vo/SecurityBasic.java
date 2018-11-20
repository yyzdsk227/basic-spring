package com.spring.vo;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityBasic {

/*	private Authentication authentication;
	private Principal principal;
	
	public SecurityBasic(){
		this.authentication = SecurityContextHolder.getContext().getAuthentication();
		//this.principal = (Principal) authentication.getPrincipal();
		
	}
	*/
	
	public MemberVO securityAuthen(MemberVO mem){
	
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(null != authentication ){ 
			
			Object principal = authentication.getPrincipal();
			System.out.println("prin");
			
			if(principal instanceof MemberVO) {
				
				mem = (MemberVO)principal;
				System.out.println("login");
				
				mem.getUsername();
				mem.getWriter();
				System.out.println(mem.getWriter());
				return mem;
			}
		}
		MemberVO memFail = new MemberVO();
		
		return memFail;
		}
	
	/*public RoleVO securityAuthen(RoleVO rol){

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(null != authentication ){ 
			
			Object principal = authentication.getPrincipal();
			System.out.println(1);
			
			if(principal instanceof MemberVO) {
				
				rol = (RoleVO)principal;
				System.out.println(4);
				
				rol.getId();
				System.out.println(rol.getId());
				return rol;
			}
		}
		RoleVO rolFail = new RoleVO();
		
		return rolFail;
		}*/
	
}
