<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
 
<%@ include file="../include/header.jsp" %>		

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">

 <!--main content start-->

		   
	<style>
	#modifyDiv {
		width:500px;
		height:100px;
		background-color:gray;
		position:absolute;
		top:40%;
		left:30%;
		padding:20px;
		z-index:100;	
	}


	</style>
	
	</head>

 	
      
		<input type="hidden" name="bid" id="rvoBid" size="35" value="${rvo.bid}"> <!-- ajax bid처리위해 name반드시 기술 -->
		
    		<br>
    		<br>
    		<br>
    		<br>
    		<br>
              
              <div>
               <div> 작성자:
                 <input type="text" class="form-control" name="writer" id="reWriter" value="${secCheck}"
                 readonly="readonly"/>	
                  </div>
              
   				 <div> 내용:
             	 <input type="text"	class="form-control" size="35" name="replyContent" id="addReContent"/>	
                  </div>       


		<br/>
		<button id="submitBtn">댓글 작성</button>
		</div>
		
		
		<br>
		<button id="checkBtn">내가 쓴 댓글</button>

	
	<div id = "modifyDiv" style="display:none;">
	 <div class='title-dialog'></div>
		 <div>
			수정 내용: <br/>
			<input type="text" id="reContent" size="35">
		 </div>
		
	 <div>
		<button type="button" id="reModifyBtn">수정</button>
		<button type="button" id="reDelBtn">삭제</button>
		<button type="button" id="closeBtn">닫기</button>
	 </div>
	</div>
	

	

	
	<br>
	<br>
	<!-- <a href><button>내가 쓴 리스트</button></a> -->
		댓글 리스트
	


	<ul id = "reply">
	</ul>
	

  

  <script type ="text/javascript">

	var bid = Number($("#rvoBid").val());
	var name = $("#reWriter").val();
	
	
	/* alert(bid);
	alert(name); */
//--------------------------------------------------------------------	
	function reListAll(){
	
	
	
	$.getJSON("/fbbs/replies/selectAll/"+bid, function(data){
		//console.log(data.length);
		var str ="";
		
		$(data).each(function(){
		 	if(name == this.writer){ 
			str += "<li data-rebid='"+this.rebid+"' class='reList'>"
				+ this.rebid + " | "+ this.replyContent + "("+this.writer+")"			
				+"<button>수정</button>"
				+"</li>";
			 } 
		});
		$("#reply").html(str);
	});
	
	}

	//댓글 쓰기 처리
	$("#submitBtn").on("click", function(){
	

		var reWriter = $("#reWriter").val();
		var reContent = $("#addReContent").val();
		
		alert(reContent);
		
		$.ajax({
		type : 'post',
		url : '/fbbs/replies/'+bid,
	             
	    headers :{
				"Content-Type" : "application/json",
				"X-HTTP-Method-Override" : "POST"			
			},
		dataType : 'text',	
		data : JSON.stringify({
			bid : bid,
			writer : reWriter,
			replyContent : reContent
		}),
		
		success : function(result){
			if(result == 'Success'){
				alert("댓글 등록 성공!!!")
				reListAll();	
			}
		}
	  });	
	});

	
	//미래에 생길 리스트
	$("#reply").on("click", ".reList Button", function(){
		
		var li = $(this).parent();
		
		var rebid = li.attr("data-rebid");
		var reContent = li.text();
		
		alert("댓글번호: "+rebid+" 수정할 내용: "+reContent);
		$(".title-dialog").html(rebid);
		$("#reContent").val(reContent);
		$("#modifyDiv").show("slow"); //수정
	})
	
	
	
	
	//댓글 수정 처리
	$("#reModifyBtn").on("click", function(){
		
		var rebid = $(".title-dialog").html(); //.title-dialog 범위 내 HTML 메소드 특성
		var reContent =$("#reContent").val();
	

		alert(reContent);
		$.ajax({
			type : 'patch',
			url : '/fbbs/replies/'+rebid,
			headers :{
				"Content-Type" : "application/json",
				"X-HTTP-Method-Override" : "PATCH"
				
			},
		  	dataType : 'text',
		  	data : JSON.stringify({replyContent : reContent}),
		  
		  	success : function(result){
		  		if(result == 'Success'){
		  			alert("댓글 수정 성공!");
		  			$("#modifyDiv").hide("slow");
		  		}
		  	}
		
		});
		
	});
	
	
	//댓글 삭제 처리
	$("#reDelBtn").on("click", function(){
		
		var rebid = $(".title-dialog").html();
		var reContent =$("#reContent").val();
	
		$.ajax({
			type : 'delete',
			url : '/fbbs/replies/'+rebid,
			
			headers :{
				"Content-Type" : "application/json",
				"X-HTTP-Method-Override" : "DELETE"
				
			},
		  	dataType : 'text',
		  	success : function(result){
		  		if(result == 'Success'){
		  			alert("댓글 삭제 성공!");
		  			$("#modifyDiv").hide("slow");
		  		}
		  	}
		
		});
		
	});
		
	
	$("#closeBtn").on("click", function(){
		$("#modifyDiv").hide("slow");
	});
	

	$("#checkBtn").on("click", function(){
		reListAll();	
	});

</script>
    
  <%@ include file="../include/footer.jsp" %>  			  
