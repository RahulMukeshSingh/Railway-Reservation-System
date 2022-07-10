/**
 * 
 */
$(document).ready(function(){
	var totalrow=$(".history tbody tr").length;
	var noofrows=5;
	var displaybuttons=5;
	displayPaginationButton(totalrow,noofrows,displaybuttons,1);
	var presentbutton=parseInt($("#activepaginationbutton").text(),10);
	
	
	$(".history").tablesorter({
		headers: { 6: { sorter: false} 
	}}).bind("sortEnd",function(){
		displayRangeRow(presentbutton,totalrow,noofrows); 
    });

	displayRangeRow(presentbutton,totalrow,noofrows);
	
	$(".history").on("change",'.noofrowpagination',function(){
				noofrows=parseInt($(this).val(),10);
				displayPaginationButton(totalrow,noofrows,displaybuttons,1);
				presentbutton=parseInt($("#activepaginationbutton").text(),10);
				displayRangeRow(presentbutton,totalrow,noofrows);
				$("#noofrowsspan").text("("+noofrows+")");
			
	});

	
	
	$(".history").on("click","#nextpaginationbutton",function(){
		presentbutton= clickNextPaginationButton(totalrow,noofrows,presentbutton,(presentbutton+1));	
	});
	$(".history").on("click","#prevpaginationbutton",function(){
		presentbutton= clickPrevPaginationButton(totalrow,noofrows,presentbutton,(presentbutton-1));	
	});
	$(".history").on("click","button:not(#nextpaginationbutton,#prevpaginationbutton)",function(){
		var clickedbutton=parseInt($(this).text(),10);
		if(clickedbutton>presentbutton){
		presentbutton= clickNextPaginationButton(totalrow,noofrows,presentbutton,clickedbutton);
		}else if(clickedbutton<presentbutton){
		presentbutton= clickPrevPaginationButton(totalrow,noofrows,presentbutton,clickedbutton);
		}
		
	});

//------------------------------------------------------------------------------------------------------------------		
});