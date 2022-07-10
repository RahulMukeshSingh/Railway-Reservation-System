<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="../JSTLTagLib/jstlTaglib.jsp" %>
<jsp:include page="../COMMON/header.jsp" />
<h2>CANCELLATION</h2>
<table align="center" class="cancellation">
<tr>
 <th>PNR no.</th>
 <th>Source</th>
 <th>Destination</th>
 <th>Train name</th>
 <th>Departure-date time</th>
 <th>Arrival-date time</th>
 <th></th>
</tr>
<tr>
<td>3737730345</td>
<td>MUMBAI CSMT</td>
<td>BANGALORE CITY</td>
<td>UDYAN EXPRESS</td>
<td>12/10/18</td>
<td>12/10/18</td>
<td>
<center>
<button title="Show Details"
class="imagebutton nobuttonhover nobuttondefaultstyle">
<img src="../images/show-details.png"  class="imagebuttonImage" />
</button>

<button title="Cancel Booking"
class="imagebutton nobuttonhover nobuttondefaultstyle">
<img src="../images/cancel.png"  class="imagebuttonImage" />
</button>

<button title="Generate Pdf"
class="imagebutton nobuttonhover nobuttondefaultstyle">
<img src="../images/pdf.png"  class="imagebuttonImage" />
</button>
</center>
</td>
</tr>
</table>




<BR><BR><BR>
<jsp:include page="../COMMON/footer.jsp" />