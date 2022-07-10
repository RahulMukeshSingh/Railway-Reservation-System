/**
 * 
 */
$(document).ready(function(){
	/*---------------------------------------------------------*/
	$(".train_class_container").on("mouseenter",".train_class",function(){
		$(".train_class_know_more",this).slideDown();
		});
	$(".train_class_container").on("mouseleave",".train_class",function(){
		$(".train_class_know_more",this).slideUp();
		});
	/*---------------------------------------------------------*/
	$(".train_class_container").on("click",".seatmap",function(){
		var classname=$(this).closest(".train_class")[0].childNodes[0].nodeValue.trim();
		var classId=$(this).closest(".train_class").find("input[type='hidden']").val();
		var classIdImage="../images/"+classId+".gif";	
		$(".modal-header").empty();
		$(".modal-header").append("<div style='background-color:#c0c0c0;width:100%;height:15%;" +
				"top:100%;position:relative;'></div>");
		$(".modal-header").append("<div style='height:10px;'</div>");
		$("#modal-content").empty();
		$(".modal-header").append(classname);
		$("#modal-content").append("<div style=\"background-image: url('"+classIdImage+"');" +
				"background-repeat: no-repeat;background-size: 70% 100%;height:700px;background-position: center;\">" +
				"</div>");
		$(".modal").show();
		
	});
	/*---------------------------------------------------------*/
	
	$(".train_class_container").on("click",".proceedtoquotadetails",function(){
		var classId=$(this).closest(".train_class").find("input[type='hidden']").val();
		$("[name='classid']").val(classId);
		$("[name='proceedToQuotaDetails']").submit();
	});
	/*---------------------------------------------------------*/
	$("#goToViewDetails").on("click",function(){
		window.location.replace("../Phase2/viewdetails.jsp");
	});
	/*---------------------------------------------------------*/
});