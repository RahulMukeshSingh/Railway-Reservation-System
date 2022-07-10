/**
 * 
 */
$(window).load(function() {
				$(".pageloading").fadeOut("slow");
				
			});

//------------------------------------------------------------------------------------------------------------
//------------------------------------------------------------------------------------------------------------

$(document).ready(function() {
// ------------------------------------------------------------------------------------------------------------
			$(window).scroll(
					function() {

						if ($(window).scrollTop() >= 173) {
							$('#nav').css('margin-top', '0%').css('position',
									'fixed').css('width', '100%').css('top',
									'0').css("z-index","10");
							$('#title').css('visibility', "hidden");
							$('#header').css('margin-bottom', '9%');
						} else {
							$('#nav').css('margin-top', '3%').css('position',
									'relative').css("z-index","0");
							$('#title').css('visibility', "visible");
							$('#header').css('margin-bottom', '0%');
						}
					});

//-------------------------------------------------------------------------------------------------------------
			alertBox=function(text)
			{
				$(".alert").show();
				$("#alertContent").text(text);
			}
//-------------------------------------------------------------------------------------------------------------
			$("#alertButton").on("click",function(){
				$(".alert").hide();
			});
// ------------------------------------------------------------------------------------------------------------
			modalBox=function(text)
			{
				$(".modal").show();
				$("#modal-content").html(text);
			}
//-------------------------------------------------------------------------------------------------------------
			$("#modalButton").on("click",function(){
				$(".modal").hide();
			});
// ------------------------------------------------------------------------------------------------------------

			
			errorAjax=function(jqXHR,exception)
			{
				if (jqXHR.status === 0) {
		            msg = 'Not connect.\n Verify Network.';
		        } else if (jqXHR.status == 404) {
		            msg = 'Requested page not found. [404]';
		        } else if (jqXHR.status == 500) {
		            msg = 'Internal Server Error [500].';
		        } else if (exception === 'parsererror') {
		            msg = 'Requested JSON parse failed.';
		        } else if (exception === 'timeout') {
		            msg = 'Time out error.';
		        } else if (exception === 'abort') {
		            msg = 'Ajax request aborted.';
		        } else {
		            msg = 'Uncaught Error.\n' + jqXHR.responseText;
		        }
				alertBox(msg);
			}
//-------------------------------------------------------------------------------------------------------------

			displayRangeRow=function(presentbutton,totalrow,noofrows)
			{
				var mintr=(parseInt(presentbutton,10)-1)*noofrows;
				var maxtr=mintr+noofrows;
				if(maxtr>parseInt(totalrow,10)){
					maxtr=totalrow;
				}
				var i;
				$(".displayTableDesign tbody tr").hide();
				for(i=mintr;i<maxtr;i++)
					{	
				
				$(".displayTableDesign tbody tr:eq("+i+")").show();	
					}
				
			}
//-------------------------------------------------------------------------------------------------------------
			displayPaginationButton=function(totalrow,noofrows,displayedbuttons,activepaginationbuttonn)
			{
				$(".divpaginationbutton").empty();				
				if(totalrow>0){
					
					var noofbuttons=parseInt((totalrow/noofrows),10);
					
					if(totalrow%noofrows>0){
						noofbuttons=noofbuttons+1;
						}
					
					if(noofbuttons>displayedbuttons){
						noofbuttons=displayedbuttons;
					}
					$(".divpaginationbutton").text("No Of Rows : ").append($('<input>')
							.attr('type','range').attr("placeholder","No Of Rows").addClass("noofrowpagination")
							.val(noofrows).attr("min",2).attr("max",totalrow));
					$(".divpaginationbutton").append($('<span>').attr("id","noofrowsspan").text("("+noofrows+")"));
					$(".divpaginationbutton").append($('<br>')).append($('<br>'));
					$(".divpaginationbutton").append($('<button>').text("<<Prev")
							.attr('type','button').addClass("nobuttonhover nobuttondefaultstyle paginationbutton")
							.attr('id','prevpaginationbutton'));
					for(var i=1;i<=noofbuttons;i++){
						if(i==activepaginationbuttonn){
						$(".divpaginationbutton").append($('<button>').text(i)
								.attr('type','button').addClass("nobuttonhover nobuttondefaultstyle paginationbutton")
								.attr('id','activepaginationbutton'));	
						}else{
							$(".divpaginationbutton").append($('<button>').text(i)
									.attr('type','button').addClass("nobuttonhover nobuttondefaultstyle paginationbutton")
								);	
	
						}
					}
					$(".divpaginationbutton").append($('<button>').text("Next>>")
							.attr('type','button').addClass("nobuttonhover nobuttondefaultstyle paginationbutton")
							.attr('id','nextpaginationbutton'));
					
					$("#prevpaginationbutton").prop("disabled",true);
					if(noofbuttons==1){
						$("#nextpaginationbutton").prop("disabled",true);
					}
				}
			}
//-------------------------------------------------------------------------------------------------------------
			clickNextPaginationButton=function(totalrow,noofrows,presentbutton,clickedbutton){
				var intpresentbutton=parseInt(presentbutton,10);
				var lastdisplayedbutton=$(".divpaginationbutton button:not(#nextpaginationbutton)").last().text();
				var noofbuttons=parseInt((totalrow/noofrows),10);
				if(totalrow%noofrows>0){
					noofbuttons=noofbuttons+1;
					}
				if(clickedbutton>lastdisplayedbutton)
					{
					$('.divpaginationbutton button').not("#nextpaginationbutton,#prevpaginationbutton").each(function(){
					var buttontext=parseInt($(this).text(),10);
					$(this).text(buttontext+1);
					});
					}else{
						$('.divpaginationbutton button').not("#nextpaginationbutton,#prevpaginationbutton").each(function(){
							var buttontext=parseInt($(this).text(),10);
							if(buttontext==intpresentbutton){
								$(this).removeAttr('id');
							}else if(buttontext==clickedbutton){
								$(this).attr('id','activepaginationbutton');
							}
							});
					}
				intpresentbutton=clickedbutton;
				if(intpresentbutton==noofbuttons){
					$('#nextpaginationbutton').prop('disabled',true);
				}
				if(intpresentbutton!=1){
					$('#prevpaginationbutton').prop('disabled',false);
				}
				displayRangeRow(intpresentbutton,totalrow,noofrows);
				
				return intpresentbutton;
			}
//-------------------------------------------------------------------------------------------------------------
			clickPrevPaginationButton=function(totalrow,noofrows,presentbutton,clickedbutton){
				var intpresentbutton=parseInt(presentbutton,10);
				var firstdisplayedbutton=$(".divpaginationbutton button:not(#prevpaginationbutton)").first().text();
				var noofbuttons=parseInt((totalrow/noofrows),10);
				if(totalrow%noofrows>0){
					noofbuttons=noofbuttons+1;
					}
				if(clickedbutton<firstdisplayedbutton)
					{
					$('.divpaginationbutton button').not("#nextpaginationbutton,#prevpaginationbutton").each(function(){
					var buttontext=parseInt($(this).text(),10);
					$(this).text(buttontext-1);
					});
					}else{
						$('.divpaginationbutton button').not("#nextpaginationbutton,#prevpaginationbutton").each(function(){
							var buttontext=parseInt($(this).text(),10);
							if(buttontext==intpresentbutton){
								$(this).removeAttr('id');
							}else if(buttontext==clickedbutton){
								$(this).attr('id','activepaginationbutton');
							}
							});
					}
				intpresentbutton=clickedbutton;
				if(intpresentbutton!=noofbuttons){
					$('#nextpaginationbutton').prop('disabled',false);
				}
				if(intpresentbutton==1){
					$('#prevpaginationbutton').prop('disabled',true);
				}
				displayRangeRow(intpresentbutton,totalrow,noofrows);
				
				return intpresentbutton;
			}

			
//-------------------------------------------------------------------------------------------------------------
			formatCurrency=function(price){
				var priceArray=price.toString().split(".");
				var priceString=priceArray[0];
				var formatPrice="";
				var counter=0;
				for (var i = priceString.length-1; i >= 0; i--) {
					if((counter-3)>=0 && (counter-3)%2==0){
						formatPrice=","+formatPrice;
					}
					var priceAtLocation=priceString.charAt(i);
					formatPrice=priceAtLocation+formatPrice;
					counter++;
				}
				var decimall="";
				if(priceArray.length>1){
					decimall="."+priceArray[1];
				}
				return "&#8377;"+formatPrice+decimall;
			}
//-------------------------------------------------------------------------------------------------------------

		});
