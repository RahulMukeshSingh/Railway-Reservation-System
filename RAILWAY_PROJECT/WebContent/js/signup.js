/**
 * 
 */
$(document).ready(function() {
	$.ajax({
	    beforeSend: function() 
        { 
        $("#state_loading").css("visibility","visible"); 
        }, 
        complete: function() 
        { 
    	$("#state_loading").css("visibility","hidden");
        },
			type : 'post',
			url : '/RAILWAY_PROJECT/railway/stateDistrict',
			cache : false,
			dataType : "json",
			data : {
				"q" : "",
				"countrycode" : "IN",
				"featurecode" : "ADM1",
			},
			success : function(datas) {
				if (datas != "" && datas != null) {
					$.each(datas, function(i,data) {
			            $("[name='state']").append($('<option>').text(data).attr('value', data));
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
	
//------------------------------------------------------------------------------------------------------------------		
		$("[name='state']").on("change",function(){
			$("[name='district']").prop('disabled', false);
			$("[name='district']").empty().append($('<option>').text("---Select District---").val("").attr('selected','selected').attr('hidden','hidden'));
			$('#district_warn').text("");	
			$('#district_validity').attr("src","");
			$('#district_validity').css("visibility","hidden");
			var q=$('[name=state]').find(":selected").val();
			
			$.ajax({
				beforeSend: function() 
		        { 
		        $("#district_loading").css("visibility","visible"); 
		        }, 
		        complete: function() 
		        { 
		    	$("#district_loading").css("visibility","hidden");
		        },
				type:'post',
				url:'/RAILWAY_PROJECT/railway/stateDistrict',
				cache:false,
				dataType:'json',
				data:{
					"q":q,
					"countrycode":"IN",
					"featurecode":"ADM2",
				},
				success: function(datas){
				if(datas!="" && datas!=null)
					{
					$.each(datas,function(i,data){
					$("[name='district']").append($('<option>').text(data).attr('value',data));
				});	
					}
				else
					{
					alertBox('Something Went Wrong! Choose the state Again!');
					}
				},
				error: function(jqXHR,exception)
				{
					errorAjax(jqXHR,exception);
				}
			});
			
		});
//------------------------------------------------------------------------------------------------------------------
		$("input[name='txt_userid']").on("input",function(){
			var value=$(this).val();
			if(value!="" && value!=null && value.length>=4)
				{
				
			$.ajax({
				beforeSend: function() 
		        { 
		         $("#userid_loading").css("visibility","visible"); 
		        }, 
		        complete: function() 
		        { 
		    	$("#userid_loading").css("visibility","hidden");
		        },
				type:'post',
				url:'/RAILWAY_PROJECT/railway/validationCheck',
				cache:false,
				dataType:'text',
				data:{
				"userid":value,	
				},
				success:function(datas)
				{
					if(datas=="true")
						{
					$('#userid_warn').text("");	
					$('#userid_validity').attr("src","../images/right.png");
					$('#userid_validity').css("visibility","visible");
						}
					else {
						$('#userid_warn').text("User Id already Exists!");
						$('#userid_validity').attr("src","../images/wrong.png");
						$('#userid_validity').css("visibility","visible");
					}
				},
				error: function(jqXHR,exception)
				{
					errorAjax(jqXHR,exception);
				}
			
			});
			
			}
			else
				{
				$('#userid_warn').text("Please enter the User ID of minimum 4 letters!");
				$('#userid_validity').attr("src","../images/wrong.png");
				$('#userid_validity').css("visibility","visible");
				}
		
	});
//--------------------------------------------------------------------------------------------------------------------
		$("input[name='txt_password']").on("input",function(){
			var value=$(this).val();
			$("input[name='txt_retypepassword']").val("");
			$('#retypepassword_warn').text("");	
			$('#retypepassword_validity').attr("src","");
			$('#retypepassword_validity').css("visibility","hidden");
			if(value!="" && value!=null && value.length>=6)
				{
					if(/[a-z]/.test(value) && /[0-9]/.test(value) && /[!@#\$%\^&\*]/.test(value) && /[A-Z]/.test(value))
						{
					$('#password_warn').text("");	
					$('#password_validity').attr("src","../images/right.png");
					$('#password_validity').css("visibility","visible");
					$("input[name='txt_retypepassword']").prop('disabled', false);	
						}
					else{
						$('#password_warn').text("Must contain 1 Lowercase, 1 Uppercase,1 digit and 1 Special Character!");
						$('#password_validity').attr("src","../images/wrong.png");
						$('#password_validity').css("visibility","visible");
						$("input[name='txt_retypepassword']").prop('disabled', true);
					}
			
			}
			else
				{
				$('#password_warn').text("Please enter the Password of Minimum 6 letters!");
				$('#password_validity').attr("src","../images/wrong.png");
				$('#password_validity').css("visibility","visible");
				$("input[name='txt_retypepassword']").prop('disabled', true);
				}
		
	});
//--------------------------------------------------------------------------------------------------------------------
		$(".imagebutton").on("click",function(){
			if($(".imagebutton .eyepassword").attr("src").includes("eyeclose.png")){
				$(".imagebutton .eyepassword").attr("src","../images/eyeopen.png");
				$("input[name='txt_password']").attr("type","text");
				$(this).attr("title","Hide Password");
				}
			else{
				$(".imagebutton .eyepassword").attr("src","../images/eyeclose.png");
				$("input[name='txt_password']").attr("type","password");
				$(this).attr("title","Show Password");
			}
				});
//--------------------------------------------------------------------------------------------------------------------
		$("input[name='txt_retypepassword']").on("input",function(){
			if($("input[name='txt_password']").val()==$("input[name='txt_retypepassword']").val())
				{
				$('#retypepassword_warn').text("");	
				$('#retypepassword_validity').attr("src","../images/right.png");
				$('#retypepassword_validity').css("visibility","visible");
				
				}
			else
				{
				$('#retypepassword_warn').text("Password Didn't Matched!");	
				$('#retypepassword_validity').attr("src","../images/wrong.png");
				$('#retypepassword_validity').css("visibility","visible");
				
				}
		});
//--------------------------------------------------------------------------------------------------------------------
		$("input[name='txt_fname']").on("input",function(){
			var value=$("input[name='txt_fname']").val();
			if(value.length > 0 && value !=null && value.trim()!="")
				{
				if(/^[a-zA-z ]+$/.test(value)){
				$('#fname_warn').text("");	
				$('#fname_validity').attr("src","../images/right.png");
				$('#fname_validity').css("visibility","visible");
				}else{
					$('#fname_warn').text("Must only contain Alphabets!");	
					$('#fname_validity').attr("src","../images/wrong.png");
					$('#fname_validity').css("visibility","visible");
					
				}
				}
			else
				{
				$('#fname_warn').text("Please enter the name!");	
				$('#fname_validity').attr("src","../images/wrong.png");
				$('#fname_validity').css("visibility","visible");
				
				}
		});
//--------------------------------------------------------------------------------------------------------------------
		$("[name='gender']").on("change",function(){
			if($("[name='gender']").val()!="" && $("[name='gender']").val()!=null )
				{
				$('#gender_warn').text("");	
				$('#gender_validity').attr("src","../images/right.png");
				$('#gender_validity').css("visibility","visible");
				}

		});
//--------------------------------------------------------------------------------------------------------------------
		$("[name='marital']").on("change",function(){
			if($("[name='marital']").val()!="" && $("[name='marital']").val()!=null )
				{
				$('#marital_warn').text("");	
				$('#marital_validity').attr("src","../images/right.png");
				$('#marital_validity').css("visibility","visible");
				}

		});
//--------------------------------------------------------------------------------------------------------------------
		$("[name='DOB']").on("change",function(){
			
		if($(this).val().trim()!="" && $(this).val()!=null)
			{
			var fromArray=$(this).attr("min").split("-");
			var toArray=$(this).attr("max").split("-");
			var chooseArray=$(this).val().split("-");
			var from=fromArray[1]+"-"+fromArray[2]+"-"+fromArray[0];
			var to=toArray[1]+"-"+toArray[2]+"-"+toArray[0];
			var choose=chooseArray[1]+"-"+chooseArray[2]+"-"+chooseArray[0];
			var fromParse=Date.parse(from);
			var toParse=Date.parse(to);
			var chooseParse=Date.parse(choose);
				if(chooseParse>=fromParse && chooseParse<=toParse)
					{
					$('#dob_warn').text("");		
					$('#dob_validity').attr("src","../images/right.png");
					$('#dob_validity').css("visibility","visible");
					}else
						{
						$('#dob_warn').text("Age should be between 18-60 years!");		
						$('#dob_validity').attr("src","../images/wrong.png");
						$('#dob_validity').css("visibility","visible");

						}
				
			}else{
				$('#dob_warn').text("Please enter the complete and correct date (Or use DatePicker)!");		
				$('#dob_validity').attr("src","../images/wrong.png");
				$('#dob_validity').css("visibility","visible");
			}
			
		});		
//--------------------------------------------------------------------------------------------------------------------
		$("[name='state']").on("change",function(){
			if($("[name='state']").val()!="" && $("[name='state']").val()!=null )
				{
				$('#state_warn').text("");	
				$('#state_validity').attr("src","../images/right.png");
				$('#state_validity').css("visibility","visible");
				}

		});
//--------------------------------------------------------------------------------------------------------------------
		$("[name='district']").on("change",function(){
			if($("[name='district']").val()!="" && $("[name='district']").val()!=null )
				{
				$('#district_warn').text("");	
				$('#district_validity').attr("src","../images/right.png");
				$('#district_validity').css("visibility","visible");
				}

		});
//--------------------------------------------------------------------------------------------------------------------
		$("[name='address']").on("input",function(){
			if($(this).val().trim()!="" && $(this).val()!=null && $(this).val().trim().length>=10 && $(this).val().trim().length<255 )
				{
				$('#address_warn').text("");	
				$('#address_validity').attr("src","../images/right.png");
				$('#address_validity').css("visibility","visible");
				}
			else{
				$('#address_warn').text("Address should be between 10-255 letters");	
				$('#address_validity').attr("src","../images/wrong.png");
				$('#address_validity').css("visibility","visible");
			}

		});
//--------------------------------------------------------------------------------------------------------------------
		$("input[name='email']").on("input",function(){
			var emailObject=document.getElementById("email");
			if($(this).val().trim()!="" && $(this).val()!=null)
				{
				if(emailObject.checkValidity()){
				$('#email_warn').text("");	
				$('#email_validity').attr("src","../images/right.png");
				$('#email_validity').css("visibility","visible");
				$('#emailotptd').show();
				}else{
					$('#email_warn').text(emailObject.validationMessage);	
					$('#email_validity').attr("src","../images/wrong.png");
					$('#email_validity').css("visibility","visible");
					$('#emailotptd').hide();
				}
				}
			else{
				$('#email_warn').text("OOPS! Email cannot be empty");	
				$('#email_validity').attr("src","../images/wrong.png");
				$('#email_validity').css("visibility","visible");
				$('#emailotptd').hide();
			}

		});
//--------------------------------------------------------------------------------------------------------------------
		$("#forotp").on("click",function(){
			$("input[name='email']").prop("disabled",true);
			$('#otp_warn').text("");	
			$('#otp_validity').attr("src","");
			$('#otp_validity').css("visibility","hidden");
			$.ajax({
				beforeSend:function(){
					$("#otp_loading").css("visibility","visible");
				},
				complete:function(){
					$("#otp_loading").css("visibility","hidden");
				},
				type:"post",
				url:"/RAILWAY_PROJECT/railway/getOTP",
				cache:false,
				dataType:"text",
				data:{
					"emailid":$("input[name='email']").val(),
				},
				success:function(datas){
					if(datas=="true")
						{
						$("#forotp").hide();
						$("#foremail").show();
						$("#otptd").slideDown();
						$("#otptd1").slideDown();
						}
					else{
						$("input[name='email']").prop("disabled",false);
						alertBox("Problem sending OTP on your E-mail. Try Again!");
					}
				},
				error:function(jqXHR,exception){
					errorAjax(jqXHR,exception);
				}
			});

		});
//--------------------------------------------------------------------------------------------------------------------
		$("#foremail").on("click",function(){
			$("input[name='email']").prop("disabled",false);
			$(this).hide();
			$("#forotp").show();
			$("#otptd").hide();
			$("#otptd1").hide();
			$("input[name='OTP']").val("");
			$('#otp_warn').text("");	
			$('#otp_validity').attr("src","");
			$('#otp_validity').css("visibility","hidden");
		});
//--------------------------------------------------------------------------------------------------------------------
		$("input[name='OTP']").on("input",function(){
			if($(this).val().trim()!="" && $(this).val().trim()!=null)
				{
			$.ajax({
				beforeSend:function(){
					$("#otpcheck_loading").css("visibility","visible");
				},
				complete:function(){
					$("#otpcheck_loading").css("visibility","hidden");
				},
				type:"post",
				url:"/RAILWAY_PROJECT/railway/checkOTP",
				cache:false,
				dataType:"text",
				data:{
					"emailid":$("input[name='email']").val(),
					"otp":$("input[name='OTP']").val(),
				},
				success:function(datas){
					if(datas=="true")
						{
						$('#otp_warn').text("");	
						$('#otp_validity').attr("src","../images/right.png");
						$('#otp_validity').css("visibility","visible");

						}else if(datas=="false"){
							$('#otp_warn').text("OTP doesn't matched!");	
							$('#otp_validity').attr("src","../images/wrong.png");
							$('#otp_validity').css("visibility","visible");
						}else if(datas!=null && datas!=""){
							$("input[name='email']").prop("disabled",false);
							$("#foremail").hide();
							$("#forotp").show();
							$("#otptd").hide();
							$("#otptd1").hide();
							$("input[name='OTP']").val("");
							$('#otp_warn').text("");	
							$('#otp_validity').attr("src","");
							$('#otp_validity').css("visibility","hidden");
							alertBox(datas);
						}
					},
				error:function(jqXHR,exception){
					errorAjax(jqXHR,exception);
				}
			});
				}else{
					$('#otp_warn').text("OTP cannot be empty!");	
					$('#otp_validity').attr("src","../images/wrong.png");
					$('#otp_validity').css("visibility","visible");

				}
		});
//--------------------------------------------------------------------------------------------------------------------
		$("input[name='mobile']").on("input",function(){
			if($(this).val().trim()!=null && $(this).val().trim()!="")
				{
				if(/^(\+91|0|91)?[7-9][0-9]{9}$/.test($(this).val()))
					{
					$('#mobile_warn').text("");	
					$('#mobile_validity').attr("src","../images/right.png");
					$('#mobile_validity').css("visibility","visible");
					}else{
						$('#mobile_warn').text("Mobile No is incorrect!");	
						$('#mobile_validity').attr("src","../images/wrong.png");
						$('#mobile_validity').css("visibility","visible");
					}
				}else
					{
					$('#mobile_warn').text("Mobile No cannot be empty!");	
					$('#mobile_validity').attr("src","../images/wrong.png");
					$('#mobile_validity').css("visibility","visible");
					}
			
		});
//--------------------------------------------------------------------------------------------------------------------
		blankError=function(nameOfFields,imageId,warnId){
			var i;
			for(i=0;i<nameOfFields.length;i++){

				if($("[name="+nameOfFields[i]+"]").val()=="" || $("[name="+nameOfFields[i]+"]").val()==null)
					{
					$("#"+warnId[i]).text("This Field cannot be empty!");	
					$("#"+imageId[i]).attr("src","../images/wrong.png");
					$("#"+imageId[i]).css("visibility","visible");
					}
			}
		}
//--------------------------------------------------------------------------------------------------------------------
                   allCorrect=function(imageId)
                   {
                	var i;
                	var counter=0;
           			for(i=0;i<imageId.length;i++){
           			if($("#"+imageId[i]).attr("src").includes("right.png")){
           				counter=counter+1;
           			}
           			}
           			var correctness;
           			if(counter==imageId.length)
           				{
           				correctness=true;
           				}else{
           					correctness=false;
           				}
           			return correctness;
                   }
//--------------------------------------------------------------------------------------------------------------------
		
						$("#button1").on("click",function() {
						var nameOfFields = [ "txt_userid",
										"txt_password", "txt_retypepassword",
										"txt_fname", "mobile", "DOB",
										"address", "email", "OTP", "state","gender","marital","district" ];
								var imageId = [ "userid_validity",
										"password_validity",
										"retypepassword_validity",
										"fname_validity", "mobile_validity",
										"dob_validity", "address_validity",
										"email_validity", "otp_validity",
										"state_validity","gender_validity","marital_validity","district_validity" ];
								var warnId = [ "userid_warn", "password_warn",
										"retypepassword_warn", "fname_warn",
										"mobile_warn", "dob_warn",
										"address_warn", "email_warn",
										"otp_warn", "state_warn","gender_warn","marital_warn","district_warn" ];
								blankError(nameOfFields, imageId, warnId);
								if(allCorrect(imageId)){
									$("input[name='email']").prop("disabled",false);
									$("[name='form1']").submit();
								}else{
									alertBox("Seems like something is missing or incorrect!");
								}
		});
//--------------------------------------------------------------------------------------------------------------------
});
