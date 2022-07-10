$(document).ready(function(){
	$(".addcustomer-display tr td:eq(2)").text($("#classNameWithID").val());
		$.ajax({
				type : 'post',
				url : '/RAILWAY_PROJECT/railway/getquotalistwithid',
				cache : false,
				dataType : "json",
				success : function(datas) {
					if (datas != "" && datas != null) {
						$.each(datas, function(i,data) {
				            $("#addcustomer-modal-content select[name='quota']").append("<option value="+data.id+">"+data.name+"</option>");
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

/*-------------------------------------------------------------------------------------------------------------*/
		$.ajax({
				type : 'post',
				url : '/RAILWAY_PROJECT/railway/seatpreferences',
				cache : false,
				dataType : "json",
				success : function(datas) {
					if (datas != "" && datas != null) {
						$.each(datas, function(i,data) {
				            $("#addcustomer-modal-content select[name='seatpref']").append("<option value="+data.id+">"+data.name+"</option>");
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

/*-------------------------------------------------------------------------------------------------------------*/
	$("#closeAddCustomerModal").on("click",function(){
		$(".addcustomer-modal").hide();
	});


/*-------------------------------------------------------------------------------------------------------------*/	
	$(".addcustomer-display").on("click","#adddetails",function(){
		
		var rowIndex=$(this).closest("tr").index();
		var details=$(this).closest("tr").find("input[type='hidden']").val();
		if(details==""){
			$("#addcustomer-modal-content input[type='text']").val("");
			$("#addcustomer-modal-content input[name='gender']:checked").prop("checked",false);
			$("#addcustomer-modal-content select[name='quota'] option[value='GN']").prop("selected",true);
			$("#addcustomer-modal-content select[name='seatpref'] option[value='NP']").prop("selected",true);
		}else{
			detail=details.split(";");
			$("[name='nameModal']").val(detail[0].trim());
			$("[name='ageModal']").val(detail[1].trim());
			$("#addcustomer-modal-content input[value='"+detail[2].trim()+"']").prop("checked",true);
			$("#addcustomer-modal-content select[name='quota'] option[value='"+detail[3].trim()+"']").prop("selected",true);
			$("#addcustomer-modal-content select[name='seatpref'] option[value='"+detail[4].trim()+"']").prop("selected",true);
		}
		$("#tableRowNoInModal").val(rowIndex);
		$(".addcustomer-modal").show();
		
	});
/*-------------------------------------------------------------------------------------------------------------*/	
	$("#addCustomer").on("click",function(){
		var newRowNo=$(".addcustomer-display tr").length;
		$(".addcustomer-display tr:last").after('<tr> <td>'+newRowNo+'.</td>'+
				'<td>Passenger</td> <td>'+$("#classNameWithID").val().trim()+'</td>'+
				'<td><button title="Add Details" id="adddetails"'+
				'class="imagebutton nobuttonhover nobuttondefaultstyle"'+
				'style="width: 25%; border-radius: 160px; height: 40px; background: radial-gradient(#00788b, #005866);">'+
				'<img src="../images/add.png" class="imagebuttonImage" /></button></td>'+
				'<td>Not Specified</td><td>N/A</td><td class="calculate">N/A</td>'+
				'<td><button type="button" title="Delete Passenger" id="delPassenger" class="imagebutton nobuttonhover nobuttondefaultstyle">'+
				'<img src="../images/del.png" class="imagebuttonImage" /></button><input type="hidden" value=""></td></tr> ');
		
	});
/*-------------------------------------------------------------------------------------------------------------*/	
	$(".addcustomer-display").on("click","#delPassenger",function(){
	$(this).closest("tr").remove();	
	var totalRow=$(".addcustomer-display tr").length;
	var i=1;
		for(i=1;i<totalRow;i++){
			$(".addcustomer-display tr:eq("+i+") td:first").text(i+".");
		}
	$(".addcustomer-display").trigger("customUpdate");	
	});
/*-------------------------------------------------------------------------------------------------------------*/	
	$("#confirmDetails").on("click",function(){
		var namee=$("#addcustomer-modal-content input[name='nameModal']").val();
		var agee=$("#addcustomer-modal-content input[name='ageModal']").val();
		var genderr=$("#addcustomer-modal-content input[type='radio'][name='gender']:checked");
		var genderrValue=genderr.val();
		var quotaa=$("#addcustomer-modal-content select[name='quota']").val();
		var quotaaText=$("#addcustomer-modal-content select[name='quota']").find(":selected").text();
		var preff=$("#addcustomer-modal-content select[name='seatpref']").val();
		if(namee.length==0 || agee.length==0 || genderr.length==0){
			
			alertBox("Complete the Data!!!");
		}else if(!(/^[a-zA-Z ]+$/.test(namee))){
			alertBox("Name should be alphabet");
		}else if(!(/^[0-9]+$/.test(agee))){
			alertBox("Age Should be in Integer");
		}else{
			var tableRowNo=$("#tableRowNoInModal").val();
			$(".addcustomer-display tr:eq("+tableRowNo+")").find("input[type='hidden']")
			.val(namee+";"+agee+";"+genderrValue+";"+quotaa+";"+preff);
			$(".addcustomer-display tr:eq("+tableRowNo+") td:nth-child(2)").text(namee);
			$(".addcustomer-display tr:eq("+tableRowNo+") td:nth-child(5)").text(quotaaText+" ("+quotaa+")");
			var cnfQuot=0,cnfGn=0,rac=0,wl=0,tqwl=0;
			for (var i = tableRowNo - 1 ; i > 0; i--) {
					var quotaColumnValue=$(".addcustomer-display tr:eq("+i+") td:nth-child(5)").text().trim();
					var statusColumnValue=$(".addcustomer-display tr:eq("+i+") td:nth-child(6)").text().trim();
					if(statusColumnValue.indexOf("(CNF")>=0 && quotaColumnValue.indexOf(quotaaText+" ("+quotaa+")")>=0){
						cnfQuot=cnfQuot+1;
					}
					if(statusColumnValue.indexOf("(CNF")>=0 && quotaColumnValue.indexOf("(GN)")>=0){
						cnfGn=cnfGn+1;
					}
					if(statusColumnValue.indexOf("(RAC")>=0){
						rac=rac+1;
					}
					if(statusColumnValue.indexOf("(WL")>=0){
						wl=wl+1;
					}
					if(statusColumnValue.indexOf("(TQWL")>=0){
						tqwl=tqwl+1;
					}
				
			}
			
			$.ajax({
				 beforeSend: function() 
			        { 
			        $("#confirmDetailsLoading").css("visibility","visible"); 
			        }, 
			        complete: function() 
			        {
			        $(".addcustomer-display").trigger("customUpdate");	
			        $("#confirmDetailsLoading").css("visibility","hidden");
			    	$(".addcustomer-modal").hide();
			        },
					type : 'post',
					url : '/RAILWAY_PROJECT/railway/getseatsandprice',
					cache : false,
					dataType : "json",
					data : {
						"quotaid" : quotaa,
						"cnfGn":cnfGn,
						"rac":rac,
						"wl":wl,
						"tqwl":tqwl,
						"cnfQuot":cnfQuot,
					},
					success : function(datas) {
						if (datas != "" && datas != null) {
							
								$(".addcustomer-display tr:eq("+tableRowNo+") td:nth-child(6)").text(datas.seatavailable);
								$(".addcustomer-display tr:eq("+tableRowNo+") td:nth-child(7)").html("&#8377;"+datas.price);
					        
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
	});
/*-------------------------------------------------------------------------------------------------------------*/	
	$(".addcustomer-display").on("customUpdate",function(){
		var totalPrice=0;
		$(".calculate").each(function(){
				var digitsOfPrice=parseFloat($(this).text().replace(/[^\d\.]/g,''));
				if(!isNaN(digitsOfPrice)){
				totalPrice=totalPrice+digitsOfPrice;
				}
			});
		
		var totalFomattedPrice=formatCurrency(totalPrice.toFixed(2));
		
		var totalTaxPrice=0;
		$(".taxcalculate").each(function(){
				var digitsOfTaxPrice=parseFloat($(this).text().replace(/[^\d\.]/g,''));
				if(!isNaN(digitsOfTaxPrice)){
				totalTaxPrice=totalTaxPrice+digitsOfTaxPrice;
				}
			});
		var totalTaxPriceAfterAddition=totalPrice+(totalPrice*totalTaxPrice/100);
		var totalTaxFomattedPrice=formatCurrency(totalTaxPriceAfterAddition.toFixed(2));
		$(".addcustomerprice-display tr:first td:nth-child(2)").html(totalFomattedPrice);
		$(".addcustomerprice-display tr:last td:nth-child(2)").html(totalTaxFomattedPrice);
		});
	
/*-------------------------------------------------------------------------------------------------------------*/	
	$("#res_ticket").on("click",function(){
		var totalPassVal="";
		for (var i = 1; i < $(".addcustomer-display tr").length; i++) {
		var passVal=$(".addcustomer-display tr:eq("+i+") td:last").find("input[type='hidden']").val();
		if(passVal.length>0){
			if(i!=1){
				totalPassVal=totalPassVal+"///";
			}
			totalPassVal=totalPassVal+passVal;
		}
		}
		$("form[name='moveToPaymentGateway'] input[type='hidden']").val(totalPassVal);
		if(totalPassVal!=""){
			$("form[name='moveToPaymentGateway']").submit();
		}else{
			alertBox("No Passenger Details Entered!!!");
		}
		
	});	
/*-------------------------------------------------------------------------------------------------------------*/	

});
	
