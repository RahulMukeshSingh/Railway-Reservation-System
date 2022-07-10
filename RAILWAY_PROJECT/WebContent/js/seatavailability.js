$(document).ready(function(){
var counter=0;	
var periodicCheck=function(){	
$.ajax({
		complete: function() 
        {	
    	setTimeout(periodicCheck, 1000);
    	},
			type : 'post',
			url : '/RAILWAY_PROJECT/railway/availableSeats',
			cache : false,
			dataType : "json",
			success : function(datas) {
				if (datas != "" && datas != null) {
					$.each(datas, function(i,data) {
						$('.available-seats-display tr:eq('+(i+1)+') td:nth-child(2)').text(data.cnf);
						$('.available-seats-display tr:eq('+(i+1)+') td:nth-child(3)').text(data.rac);
						$('.available-seats-display tr:eq('+(i+1)+') td:nth-child(4)').text(data.wl);
			        });
				} else {
					alertBox('Something Went Wrong! Reload the Page Again!');
				}		
			},
			error: function(jqXHR,exception)
			{
				errorAjax(jqXHR,exception);
			}
		});
}
periodicCheck();
//--------------------------------------------------------------------------------------------------------
$('#backToQuotaDetails').on("click",function(){
	window.location.replace("../Phase3/quotadetails.jsp");
});
$('#proceedToBooking').on("click",function(){
	window.location.href="../Phase4/booking.jsp";
});
//--------------------------------------------------------------------------------------------------------

});