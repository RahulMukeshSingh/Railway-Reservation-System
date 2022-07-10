<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="../JSTLTagLib/jstlTaglib.jsp" %>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>RAILWAY</title>
<link href="../CSS/stylee.css" rel="stylesheet" type="text/css">
<link href="../CSS/imageForm.css" rel="stylesheet" type="text/css">
<script src="../js/jquery.js"></script>
<script src="../js/HtmlandCommon.js"></script>
</head>
<body>

<div class="pageloading"></div>
	<div class="alert">
		<div class="alertBox">
			<div class="alertHeader">RAILWAY ALERT</div>
			<div id="alertContent"></div>
			<div class="alertFooter">
				<button type="button" style="width:30%; height:90%;" id="alertButton">OK</button>
			</div>
		</div>
	</div>
	
<div class="modal">
		<div class="modal-box" >
			<div class="modal-header">
			  <div style="background-color:#c0c0c0;width:100%;height:15%;
			  top:100%;position:relative;">
			  </div>
			 <div style="height:10px;">
			  </div>
			</div>
			<br>
	        <div id="modal-content">
				
			</div>
			<div class="modal-footer">
				<button type="button" style="width:30%; height:90%;" id="modalButton">OK</button>
			</div>
	     </div>
</div>	
	
	<div id="header">
		<h1 id="title">RAILWAY RESERVATION</h1>
		<img src="../images/logo.png" alt="logo" id="logo" width=18%>
		<div id="nav">
			<ul>
				<li><a class="active" href="#home">Home</a></li>
				<li><a href="#news">News</a></li>
				<li><a href="#contact">Contact Us</a></li>
				<li><a href="#about">About Us</a></li>
			</ul>
		</div>
</div>