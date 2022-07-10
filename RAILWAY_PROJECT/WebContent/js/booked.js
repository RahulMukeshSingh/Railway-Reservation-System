/**
 * 
 */


//------------------------------------------------------------------------------------------------------------------		

$(document).ready(function(){
//------------------------------------------------------------------------------------------------------------------		

	$("#pdfContainer").on("mouseenter",function(){
		$("#buttonsContainer").show();
	});
	$("#pdfContainer").on("mouseleave",function(){
		$("#buttonsContainer").hide();
	});
//------------------------------------------------------------------------------------------------------------------		
	
	$("#sendMailNow").on("click",function(){
		$.ajax({
		    beforeSend: function() 
	        { 
		    	$(".sendMailNowLoading").show(); 
	        }, 
	        complete: function() 
	        {
	        	$(".sendMailNowLoading").fadeOut(5000);
	    	},
				type : 'post',
				url : '/RAILWAY_PROJECT/railway/sendPDFMail',
				cache : false,
				success : function(data) {
					if (data == "true") {
						$(".sendMailNowLoading").css("background-image","url('../images/maildone.gif')");
					} else {
						$(".sendMailNowLoading").hide();
						alertBox('Please Try Again or Download the Ticket!!!');
					}		
				},
				error: function(jqXHR,exception)
				{
					errorAjax(jqXHR,exception);
				}
			});
	});
//------------------------------------------------------------------------------------------------------------------		
		
		$(window).on("beforeunload",function() {
			$.ajax({
			    	type : 'post',
					url : '/RAILWAY_PROJECT/railway/deleteNonLoginSession',
					cache : false,
					async: false,
					success : function(data) {
							
					},
					error: function(jqXHR,exception)
					{
						errorAjax(jqXHR,exception);
					}
				});
		});
//------------------------------------------------------------------------------------------------------------------		
	
});