<%@page import="railwayFrequentFunctions.DatabaseConnection"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="../JSTLTagLib/jstlTaglib.jsp" %>
<jsp:include page="../COMMON/header.jsp" />
<script type="text/javascript" src="../js/quotadetails.js"></script>
<sql:setDataSource var="con" driver="<%=(new DatabaseConnection()).getDriver() %>"
 url="<%=(new DatabaseConnection()).getURL() %>" user="<%=(new DatabaseConnection()).getUsername() %>"
 password="<%=(new DatabaseConnection()).getPassword() %>" />
<sql:query var="rs" dataSource="${con}">
SELECT quota_name,quota_id,quota_details FROM quota_details;
</sql:query>
<c:set var="counter" value="0"/>
<table class="quota_class_container">
<tr>
<c:forEach var="row" items="${rs.rows}">
<td>
<div class="quota_class" style="background-image:url(../images/${row.quota_id}-icon.png),radial-gradient(#d9d9d9, #00788b,#005866);background-repeat:no-repeat;background-position:center;background-size:contain">
<c:out value="${row.quota_name}(${row.quota_id})"/> 
<div class="quota_class_know_more">
<p><c:out value="${fnc:trim(row.quota_details)}"/></p>
</div>
</div></td>
<c:set var="counter" value="${counter + 1}"/>
<c:if test="${counter%3==0}">
</tr><tr>
</c:if>
</c:forEach>
</tr>
</table>
<button id="backToClassSelection" style="background-image:url(../images/back-button.png),linear-gradient(#b3b3b3,#a6a6a6,#737373);border-radius:10px;left:40%;padding-right:40px;color:#fefefe;top:60px;position:relative;background-repeat:no-repeat;width:10%;background-position:right;background-size:contain">Back</button>
<button id="proceedToSeatAvailability" style="background-image:url(../images/next-button.png),linear-gradient(#b3b3b3,#a6a6a6,#737373);border-radius:10px;left:40%;;padding-left:35px;color:#fefefe;top:60px;position:relative;background-repeat:no-repeat;width:12%;background-position:left;background-size:contain">Proceed</button>
<jsp:include page="../COMMON/footer.jsp" />