<%@page import="railwayFrequentFunctions.DatabaseConnection"%>
<%@page import="java.time.LocalDate"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="../JSTLTagLib/jstlTaglib.jsp" %>
<jsp:include page="../COMMON/header.jsp" />
<script src="../js/jquery.tablesorter.js"></script>
<script src="../js/viewdetails.js"></script>

<br>
<c:set var="now" value="<%=new Date(System.currentTimeMillis()+(1000*60*60*24)) %>"/>
<c:set var="maxMonth" value="<%=LocalDate.now().plusMonths(4)%>" />
<c:set var="exists" value="false" />
<div id="search-train">
<form action="viewdetails.jsp" method="post" name="details">

<table style="width:50%;float:left">
     <tr>
       <td>Journey Date:</td>
       <td><input type="date" name="date" min='<fmt:formatDate pattern="yyyy-MM-dd" value="${now}"/>' max="${maxMonth}" 
       style="padding:1%;border-radius:8px;font-size:63%;width:97%;" class="calendar-icon" value="${param.date}"></td>	
	 
	  
     </tr>
     
    <tr>
       <td>Source:<img src="../images/loading.gif" class="loading"
					id="source_station_loading"></td>
       <td>
         <select name="source" style="width:100%;padding:1%;border-radius:8px;padding-left:30%" class="source-icon">
         <option value="" disabled selected hidden="true">---Source---</option>
         </select>
       </td>  
			
    </tr>
 
    <tr>
      <td>Destination:<img src="../images/loading.gif"
					class="loading" id="dest_station_loading"></td>
      <td> 
      <select name="destination" style="width:100%;padding:1%;border-radius:8px;padding-left:30%;" class="source-icon" disabled>
      <option value="" disabled selected hidden="true">---Destination---</option>
      </select></td>
      
    </tr>
    <tr></tr>
    <tr>
      <td colspan="2" style="padding-left:15%"><button type="button" id="search-train-button" style="width:80%;">Search</button></td> 
    </tr>
  </table>
       
 <table style="width:50%;height:190px">
       <tr>
         <td colspan="3" style="vertical-align:top"> 
          
            <input type="text" style="width:75%;padding-top:0;" class="train-icon searchtrainnow" placeholder="Search Train..." autocomplete="off">
            
                 <div id="searchtrain-box">
                 </div>
         </td>
         
         <td style="vertical-align:top">
	     </td>
       </tr>
 </table>


</form>
</div>


<c:if test="${param.date !=null}">
<form name="proceedToNextPage" action="/RAILWAY_PROJECT/railway/trainSeltoClassSel" method="post">
<input type="hidden" name="selectedDatee" value="${param.date}"/>
<input type="hidden" name="selectedSrcStation1" id="selectedSrcStation" value="${param.source}"/>
<input type="hidden" name="selectedDestStation1" id="selectedDestStation" value="${param.destination}"/>
<input type="hidden" name="selectedTrainno" value=""/>
<input type="hidden" name="selectedTrainreturn" value=""/>
</form>
<fmt:parseDate var="journeydate" pattern="yyyy-MM-dd" value="${param.date}"/>
<fmt:formatDate var="weekjourneydate" pattern="EEEE" value="${journeydate}"/>

<sql:setDataSource var="con" driver="<%= (new DatabaseConnection()).getDriver() %>" url="<%= (new DatabaseConnection()).getURL() %>" 
user="<%= (new DatabaseConnection()).getUsername() %>" password="<%= (new DatabaseConnection()).getPassword() %>"/>
<sql:query dataSource="${con}" var="rs">

SELECT train_no,train_name,(SELECT station_name FROM station_master WHERE station_code = source_station_code) AS source_station,
(SELECT station_name FROM station_master WHERE station_code = dest_station_code) AS  dest_station,special,train_return 
FROM train_master t where t.route_id IN 
(SELECT DISTINCT route_id FROM route_master r WHERE r.station_code=t.dest_station_code AND counter 
>= (SELECT counter from route_master r1 where r1.route_id=r.route_id AND r1.station_code=
(SELECT station_code FROM station_master WHERE station_name = ?) AND counter 
> (SELECT counter from route_master r2 where r2.route_id=r.route_id AND r2.station_code=
(SELECT station_code FROM station_master WHERE station_name = ?) AND counter 
>= (SELECT counter from route_master r3 where r3.route_id=r.route_id AND r3.station_code=t.source_station_code)))) 
AND (t.train_no,t.train_return) IN (SELECT train_no,train_return FROM train_normal_schedule where ${weekjourneydate}=1);

<sql:param value="${param.destination}"/>
<sql:param value="${param.source}"/>

</sql:query>

 
<div class="pagination">
<table class="displayTableDesign">
<thead>
  <tr>
    <th>Train No</th>
    <th>Train Name</th>
    <th>Source</th>
	<th>Destination</th>
    <th>Special Train</th>
    <th></th>
  </tr> 
</thead>
<tbody>	
   <script>
	$(document).ready(function(){
		$('#searchtrain-box').empty();
	});
	</script>
	<c:forEach var="row" items="${rs.rows}">
	<c:set var="exists" value="true"/>
	<tr>
	<td><c:out value="${row.train_no}" />
		<input type="hidden" value="${row.train_return}" />
	</td>
	<td><c:out value="${row.train_name}" /></td>
	<td><c:out value="${row.source_station}" /></td>
	<td><c:out value="${row.dest_station}" /></td>
	<td>
	<c:choose>
	<c:when test="${row.special}" >
	Yes
	</c:when>
	<c:otherwise>
	No
	</c:otherwise>
	</c:choose>
	</td>
	<td><button type="button" title="Time" class="imagebutton nobuttonhover nobuttondefaultstyle routetime"> 
					<img src="../images/time.png" class="imagebuttonImage"></button>
		<button type="button" title="Proceed" class="imagebutton nobuttonhover nobuttondefaultstyle proceed" > 
					<img src="../images/rightarrow.png" class="imagebuttonImage"></button></td>
	</tr>
	
	<script>
	$(document).ready(function(){
		
		$('#searchtrain-box').append('<div><input type="radio" name="Train-search" style="float:left;">${fnc:toLowerCase(row.train_name)}</div>');
	
	});
	</script>
	</c:forEach>
</tbody>
</table>
 
<div class="divpaginationbutton">

</div>

</div>
<c:choose>
<c:when test="${exists eq true}">
<script>
$(document).ready(function(){
	$(".pagination").show();
});
</script>

</c:when>
<c:otherwise>
<script>
$(document).ready(function(){
	$(".pagination").hide();
	alertBox("No Trains Found!!!");
});
</script>
</c:otherwise>
</c:choose>
</c:if>
 <br><br><br>
<jsp:include page="../COMMON/footer.jsp" />
