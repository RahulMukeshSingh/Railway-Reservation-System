/**
 * 
 */
$(document).ready(function(){
//-------------------------------------------------------------------------------------------------------------	
	$(".quota_class_container").on("mouseenter",".quota_class",function(){
		$(".quota_class_know_more",this).slideDown("slow");
		});
	$(".quota_class_container").on("mouseleave",".quota_class",function(){
		$(".quota_class_know_more",this).slideUp("slow");
		});

//-------------------------------------------------------------------------------------------------------------	
	$('#backToClassSelection').on("click",function(){
		window.location.replace("../Phase2/classselection.jsp");
	});
	$('#proceedToSeatAvailability').on("click",function(){
		window.location.href="../Phase3/seatavailability.jsp";
	});

//-------------------------------------------------------------------------------------------------------------	

});