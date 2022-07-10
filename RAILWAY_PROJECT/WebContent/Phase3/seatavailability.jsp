
<%@page import="railwayFrequentFunctions.DatabaseConnection"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="../JSTLTagLib/jstlTaglib.jsp" %>
<jsp:include page="../COMMON/header.jsp" />
<script type="text/javascript" src="../js/seatavailability.js"></script>
<jsp:useBean id="price" class="railwayFrequentFunctions.DatabasePrice" />
<c:set var="pricewithoutquota" value="${price.getTrainRoutePrice(sessionScope.source,sessionScope.destination,sessionScope.routeId,
		sessionScope.trainno,sessionScope.trainreturn,sessionScope.classid)}"/>
<sql:setDataSource var="con" driver="<%= (new DatabaseConnection()).getDriver() %>" url="<%= (new DatabaseConnection()).getURL() %>" 
user="<%= (new DatabaseConnection()).getUsername() %>" password="<%= (new DatabaseConnection()).getPassword() %>"/>
<sql:query dataSource="${con}" var="rs">
SELECT quota_id,quota_name,price_discount FROM quota_details order by seats_quota desc;
</sql:query>
<table class="available-seats-display" align="center">		

<tr>
<th>Quota</th>
<th class="seat-icon">Available<br>Confirm Tickets</th>
<th class="seat-icon">Available<br>RAC Tickets</th>
<th class="seat-icon">Available<br>Waiting Tickets</th>
<th>Gross Price<br>(After Discount)</th>
</tr>
<c:forEach var="row" items="${rs.rows}">
<tr>
<td><c:out value="${row.quota_name}( ${row.quota_id} )"/></td>
<td></td>
<td></td>
<td></td>
<td>
&#8377;
<fmt:formatNumber var="pricewithquota" value='${pricewithoutquota - (pricewithoutquota * row.price_discount / 100)}'
 type="NUMBER" maxFractionDigits="1"/>
 <c:out value="${pricewithquota} ( ${row.price_discount}% )"/></td>
</tr>
</c:forEach>
</table>
<br><br>
<button id="backToQuotaDetails" style="background-image:url(../images/back-button.png),linear-gradient(#b3b3b3,#a6a6a6,#737373);border-radius:10px;left:40%;padding-right:40px;color:#fefefe;top:60px;position:relative;background-repeat:no-repeat;width:10%;background-position:right;background-size:contain">Back</button>
<button id="proceedToBooking" style="background-image:url(../images/next-button.png),linear-gradient(#b3b3b3,#a6a6a6,#737373);border-radius:10px;left:40%;;padding-left:35px;color:#fefefe;top:60px;position:relative;background-repeat:no-repeat;width:12%;background-position:left;background-size:contain">Proceed</button>


<br>
<jsp:include page="../COMMON/footer.jsp" />