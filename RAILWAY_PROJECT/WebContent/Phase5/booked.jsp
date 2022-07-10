<%@page import="railwayFrequentFunctions.DatabaseConnection"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="../JSTLTagLib/jstlTaglib.jsp"%>
<jsp:include page="../COMMON/header.jsp" />
<script src="../js/booked.js"></script>
<c:set var="userid" value="${sessionScope.userid}" />
<sql:setDataSource var="con"
	driver="<%=(new DatabaseConnection()).getDriver()%>"
	url="<%=(new DatabaseConnection()).getURL()%>"
	user="<%=(new DatabaseConnection()).getUsername()%>"
	password="<%=(new DatabaseConnection()).getPassword()%>" />
<sql:query var="rs" dataSource="${con}">
SELECT email FROM user_details WHERE userid=?;
<sql:param value="${userid}" />
</sql:query>
<c:forEach var="row" items="${rs.rows}">
	<c:set var="email" value="${row.email}" />
</c:forEach>
<br />
<br />
<center>
<div class="sendMailNowLoading"></div>
	<div id="pdfContainer">
		<div class="sendMailNowLoading"></div>
		<iframe src="../railway/ticket" id="iframePDF"> </iframe>
		<div id="buttonsContainer">
			<center>
				<br/><br/><br/><br/><br/>
				<div id="showButtons">
					<a href="../railway/ticket" download>
						<button type="button" title="Download"
							class="imagebutton nobuttonhover nobuttondefaultstyle">
							<img src="../images/download.png"
								style="width: 70px; height: 70px;" class="imagebuttonImage">
						</button>
					</a>
					<button type="button" title="Send Mail ( ${email} )"
						class="imagebutton nobuttonhover nobuttondefaultstyle" id="sendMailNow">
						<img src="../images/sendmail.png"
							style="width: 70px; height: 70px;" class="imagebuttonImage">
					</button>
				</div>
			</center>
		</div>
	</div>
</center>
<jsp:include page="../COMMON/footer.jsp" />

