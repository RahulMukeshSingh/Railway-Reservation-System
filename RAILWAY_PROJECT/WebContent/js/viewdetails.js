/**
 * 
 */
$(document).ready(function(){
	var totalrow=$(".displayTableDesign tbody tr").length;
	var noofrows=5;
	var displaybuttons=5;
	displayPaginationButton(totalrow,noofrows,displaybuttons,1);
	var presentbutton=parseInt($("#activepaginationbutton").text(),10);
	
	
	$(".displayTableDesign").tablesorter({
		headers: { 5: { sorter: false} 
	}}).bind("sortEnd",function(){
		displayRangeRow(presentbutton,totalrow,noofrows); 
    });

	displayRangeRow(presentbutton,totalrow,noofrows);
	
	$(".divpaginationbutton").on("change",'.noofrowpagination',function(){
				noofrows=parseInt($(this).val(),10);
				displayPaginationButton(totalrow,noofrows,displaybuttons,1);
				presentbutton=parseInt($("#activepaginationbutton").text(),10);
				displayRangeRow(presentbutton,totalrow,noofrows);
				$("#noofrowsspan").text("("+noofrows+")");
			
	});

	
	
	$(".divpaginationbutton").on("click","#nextpaginationbutton",function(){
		presentbutton= clickNextPaginationButton(totalrow,noofrows,presentbutton,(presentbutton+1));	
	});
	$(".divpaginationbutton").on("click","#prevpaginationbutton",function(){
		presentbutton= clickPrevPaginationButton(totalrow,noofrows,presentbutton,(presentbutton-1));	
	});
	$(".divpaginationbutton").on("click","button:not(#nextpaginationbutton,#prevpaginationbutton)",function(){
		var clickedbutton=parseInt($(this).text(),10);
		if(clickedbutton>presentbutton){
		presentbutton= clickNextPaginationButton(totalrow,noofrows,presentbutton,clickedbutton);
		}else if(clickedbutton<presentbutton){
		presentbutton= clickPrevPaginationButton(totalrow,noofrows,presentbutton,clickedbutton);
		}
		
	});

//------------------------------------------------------------------------------------------------------------------		
	var selectedDate=$("input[name='date']").val();
	var selectedSrcStation=$("#selectedSrcStation").val();
	var selectedDestStation=$("#selectedDestStation").val();

		$.ajax({
	    beforeSend: function() 
        { 
        $("#source_station_loading").css("visibility","visible"); 
        }, 
        complete: function() 
        {
        	if(selectedSrcStation!=null){
        		if(selectedSrcStation.trim().length > 0){
        			$("[name='source'] option[value='"+selectedSrcStation+"']").prop("selected",true);
        			$("[name='source']").trigger("change");
        		}
        	}
    	$("#source_station_loading").css("visibility","hidden");
    	},
			type : 'post',
			url : '/RAILWAY_PROJECT/railway/srcanddest',
			cache : false,
			dataType : "json",
			data : {
				"src" : "",
				},
			success : function(datas) {
				if (datas != "" && datas != null) {
					$.each(datas, function(i,data) {
			            $("[name='source']").append($('<option>').text(data).attr('value', data));
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
		$("[name='source']").on("change",function(){
			$("[name='destination']").prop("disabled",false);
			$("[name='destination']").empty().append($('<option>').text("---Destination---").val("").attr('selected','selected').attr('hidden','hidden'));
			var src=$(this).val();
			$.ajax({
			    beforeSend: function() 
		        { 
		        $("#dest_station_loading").css("visibility","visible"); 
		        }, 
		        complete: function() 
		        {
		        	if(selectedDestStation!=null){
		        		if(selectedDestStation.trim().length > 0){
		        			$("[name='destination'] option[value='"+selectedDestStation+"']").prop("selected",true);
		        			
		        		}
		        	}	
		    	$("#dest_station_loading").css("visibility","hidden");
		        },
					type : 'post',
					url : '/RAILWAY_PROJECT/railway/srcanddest',
					cache : false,
					dataType : "json",
					data : {
						"src" : src,
						},
					success : function(datas) {
						if (datas != "" && datas != null) {
							$.each(datas, function(i,data) {
					            $("[name='destination']").append($('<option>').text(data).attr('value', data));
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
		});
//------------------------------------------------------------------------------------------------------------------		
		$('#search-train-button').on("click",function(){
			if($('[name=date]').val()!="" && $('[name=source]').val()!=null && $('[name=destination]').val()!=null
					&& $('[name=source]').val()!="" && $('[name=destination]').val()!=""){
				$('[name=details]').submit();
			}else{
				alertBox("You didn't filled all the data");
			}
		});
//------------------------------------------------------------------------------------------------------------------		
		$('#searchtrain-box').on("mouseenter","div",function(){
			$(this).css('background-color','#00788b').css('color','white');
		});
		
		$('#searchtrain-box').on("mouseleave","div",function(){
			$(this).css('background-color','white').css('color','black');
		});
//------------------------------------------------------------------------------------------------------------------		
		$('#searchtrain-box').on("click","div",function(){
			$(this).find('input[type=radio]').prop('checked',true);
			displayPaginationButton(1,1,1,1);
			displayRangeRow(1,1,1);
		});
		

//------------------------------------------------------------------------------------------------------------------		
		$('.searchtrainnow').on({
			focusin : function() {
				$('#searchtrain-box').slideDown();
				
			},
			focusout: function(){
				$('#searchtrain-box').slideUp();
				
			}
		});
//------------------------------------------------------------------------------------------------------------------		
		$('.searchtrainnow').on("input",function(){
			$('#searchtrain-box div').hide();
			$('#searchtrain-box div:contains('+$(this).val().toLowerCase() +')').show();
			$(".displayTableDesign tbody tr").show();
			
			displayPaginationButton(totalrow,noofrows,displaybuttons,presentbutton);
			
			displayRangeRow(presentbutton,totalrow,noofrows);

		});
//------------------------------------------------------------------------------------------------------------------		
		$('#searchtrain-box').on("click","div",function(){
			$('.searchtrainnow').val($(this).text());
			$(".displayTableDesign tbody tr").hide();
			$(".displayTableDesign tbody td")
			.filter(function() {return $(this).text().toLowerCase() == $('.searchtrainnow').val().toLowerCase();})
			.parent("tr").show();
		});

//------------------------------------------------------------------------------------------------------------------		
		$('.displayTableDesign tbody').on("click",".routetime",function(){
			var trainNo=$(this).closest("tr").children('td:first').text();
			var returnRoute=$(this).closest("tr").children('td:first').find("input[type='hidden']").val();
			var returnRouteInBinary;
			if(returnRoute=="true"){
				returnRouteInBinary=1;
			}else{
				returnRouteInBinary=0;
			}
			var SrcDestClassChain=false;
			$(".modal-header").empty();
			$(".modal-header").append("<div style='background-color:#c0c0c0;width:100%;height:15%;top:100%;position:relative;'></div>");
			$(".modal-header").append("<div style='height:10px;'</div>");
			$("#modal-content").empty();
			$.ajax({
			    beforeSend: function() 
		        { 
		        $(".pageloading").show(); 
		        }, 
		        complete: function() 
		        { 
		    	$(".pageloading").fadeOut("slow");
		        },
					type : 'post',
					url : '/RAILWAY_PROJECT/railway/routedisp',
					cache : false,
					dataType : "json",
					data : {
						"trainNo" : trainNo,
						"returnRoute":returnRouteInBinary,
						"selectedDate":selectedDate,
						},
					success : function(datas) {
						if (datas != "" && datas != null) {
							var counter=0;
							$(".modal-header").append("Route ["+selectedSrcStation+"-"+selectedDestStation+"]");
							$.each(datas, function(i,data) {
								if(counter==0){
									$("#modal-content").append("<p style='text-align: center;color:#00788b'>"+data+"</p>");	
									$("#modal-content").append("<ol class='routestation'>");
									
								}else{
									if(SrcDestClassChain || data.indexOf(selectedSrcStation) >= 0){
									if(counter==1){
									$("#modal-content ol").append("<li value='1'><span></span><b style='border:thick double #00788b'>"+data+"</b></li>");
									}else{
									$("#modal-content ol").append("<li><span></span><b style='border:thick double #00788b'>"+data+"</b></li>");	
									}
									if(data.indexOf(selectedSrcStation) >= 0){
										SrcDestClassChain=true;
									}else if(data.indexOf(selectedDestStation) >= 0){
										SrcDestClassChain=false;
									}
									}else{
										if(counter==1){
											$("#modal-content ol").append("<li value='1'><span></span>"+data+"</li>");
											}else{
											$("#modal-content ol").append("<li><span></span>"+data+"</li>");	
											}
									
									}
								}
					            counter=counter+1;
					        });
							$("#modal-content").append("</ol>");
							$(".modal").show();
						} else {
							alertBox('Something Went Wrong! Reload the Page Again!');
						}		
					},
					error: function(jqXHR,exception)
					{
						errorAjax(jqXHR,exception);
					}
				});

		});
//------------------------------------------------------------------------------------------------------------------		
		$('.displayTableDesign tbody').on("click",".proceed",function(){
			var trainNo=$(this).closest("tr").children('td:first').text().trim();
			var returnRoute=$(this).closest("tr").children('td:first').find("input[type='hidden']").val();
			var returnRouteInBinary;
			if(returnRoute=="true"){
				returnRouteInBinary=1;
			}else{
				returnRouteInBinary=0;
			}
			$("[name='selectedTrainno']").val(trainNo);
			$("[name='selectedTrainreturn']").val(returnRouteInBinary);
			$("[name='proceedToNextPage']").submit();
		});
//------------------------------------------------------------------------------------------------------------------		

});