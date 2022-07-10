<%@page import="railwayFrequentFunctions.DatabaseConnection"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="../JSTLTagLib/jstlTaglib.jsp" %>
<jsp:include page="../COMMON/header.jsp" />
<script src="../js/classselection.js"></script>
<c:set var="trainno" value="${sessionScope.trainno}" />
<sql:setDataSource var="con" driver="<%=(new DatabaseConnection()).getDriver() %>"
 url="<%=(new DatabaseConnection()).getURL() %>" user="<%=(new DatabaseConnection()).getUsername() %>"
 password="<%=(new DatabaseConnection()).getPassword() %>" />
<sql:query var="rs" dataSource="${con}">
SELECT class_id,class_name from train_class_details where class_id in 
(SELECT class_id FROM train_coach_details where train_no=?);
<sql:param value="${trainno}" />
</sql:query>
<c:set var="counter" value="0"/>
<table class="train_class_container">
<tr>
<c:forEach var="row" items="${rs.rows}">
<td><div class="train_class" style="background:url(../images/seat.png),linear-gradient(#00788b, #008599,#009bb3);
background-repeat:no-repeat;background-size:contain;background-position:center">
<c:out value="${row.class_name}" />
<input type="hidden" value="${row.class_id}">
<div class="train_class_know_more">
<button type="button" style="width:25%;height:60px;background-color:#dfdfdf;color:#00788b;top:60px;
position:relative;border-radius:5px;" class="seatmap">Seat Map</button>
<button type="button" style="width:25%;height:60px;background-color:#dfdfdf;color:#00788b;top:60px;
position:relative;border-radius:5px;" class="proceedtoquotadetails">Proceed</button>
</div>
</div></td>
<c:set var="counter" value="${counter + 1}"/>
<c:if test="${counter%3==0}">
</tr><tr>
</c:if>
</c:forEach>
</tr>
</table>

<form name="proceedToQuotaDetails" method="post" action="/RAILWAY_PROJECT/railway/classSelToQuotaDetails">
<input type="hidden" name="classid" value="">
</form>

<button style="background:url(../images/back.png),radial-gradient(#00788b, #005866);background-repeat:no-repeat;
background-size:contain;background-position:left;padding-left:60px;width:12%;top:70px;left:85%;position:relative"
id="goToViewDetails">Back</button>

<jsp:include page="../COMMON/footer.jsp" />