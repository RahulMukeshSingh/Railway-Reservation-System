/**
 * 
 */

$(document).ready(function(){
$('#userhideeffectinput').on({
				focusin : function() {
					$("#userhideeffect").slideDown();
					$(this).attr("placeholder","");
				},
				focusout: function(){
					$("#userhideeffect").slideUp();
					$(this).attr("placeholder","User ID");
				}
			});
			$('#passhideeffectinput').on({
				focusin : function() {
					$("#passhideeffect").slideDown();
					$(this).attr("placeholder","");
					
				},
				focusout: function(){
					$("#passhideeffect").slideUp();
					$(this).attr("placeholder","Password");
				}
			});
// -------------------------------------------------------------------------------------------------------------
$("#signup").on("click",function(){
	window.location.href="../Phase1/signup.jsp";	
});
//-------------------------------------------------------------------------------------------------------------
$("#goToValidate").on("click",function(){
	if($("[name='userid']").val()!="" && $("[name='password']").val()!=""){
		$("#authenticate").submit();
		
	}else if($("[name='userid']").val()=="" && $("[name='password']").val()==""){
		alertBox("Please enter the Userid and Password!!!");
	}else if($("[name='userid']").val()==""){
		alertBox("Please enter the Userid!!!");
	}else if($("[name='password']").val()==""){
		alertBox("Please enter the Password!!!");
	}	
});
//-------------------------------------------------------------------------------------------------------------

});