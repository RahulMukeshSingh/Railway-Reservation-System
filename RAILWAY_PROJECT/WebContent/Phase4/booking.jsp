<%@page import="railwayFrequentFunctions.DatabaseConnection"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="../JSTLTagLib/jstlTaglib.jsp"%>
<jsp:include page="../COMMON/header.jsp" />
<script type="text/javascript" src="../js/booking.js"></script>
<sql:setDataSource var="con" driver="<%= (new DatabaseConnection()).getDriver() %>" url="<%= (new DatabaseConnection()).getURL() %>" 
user="<%= (new DatabaseConnection()).getUsername() %>" password="<%= (new DatabaseConnection()).getPassword() %>"/>
<sql:query dataSource="${con}" var="rs">
SELECT class_name FROM train_class_details where class_id=?;
<sql:param value="${sessionScope.classid}"/>
</sql:query>
<c:forEach var="row" items="${rs.rows}">
<input type="hidden" id="classNameWithID" value="${row.class_name} (${sessionScope.classid})"/>
</c:forEach>
<div class="addcustomer-modal">
	<div class="addcustomer-modal-box">
		<div class="addcustomer-modal-header">
			<button id="closeAddCustomerModal" style="left: 47%; position: relative; margin-top: 0px"
				class="imagebutton nobuttonhover nobuttondefaultstyle" title="Close">
				<span class="imagebuttonImage" style="font-size: 30px">&times;</span>
			</button>

		</div>

		<div id="addcustomer-modal-content">

			<form>
				<input type="hidden" id="tableRowNoInModal"/>
				<table style="width: 50%; left: 24%; position: relative; top: 70px;">
					<tr>
						<td>Name:</td>
						<td><input type="text" name="nameModal" style="height: 35px" placeholder="Name"></td>
					</tr>
					<tr>
						<td>Age:</td>
						<td><input type="text" name="ageModal" style="height: 35px" placeholder="Age"></td>
					</tr>
					<tr>
						<td>Gender:</td>
						<td><input type="radio" name="gender" value="Male">Male
							<input type="radio" name="gender" value="Female">Female
							<input type="radio" name="gender" value="Others">Others</td>
					</tr>
					<tr>
						<td>Quota:</td>
						<td><select name="quota" style="height: 35px">
							</select>
						</td>
					</tr>
					<tr>
						<td>Seat Preference:</td>
						<td><select name="seatpref" style="height: 35px">
							</select>
						</td>
					</tr>
					<tr>
						<td colspan="2"><button type="button" id="confirmDetails"
								style="width: 60%; height: 45px; font: 23px fantasy, serif, sans-serif; 
								background-color: white;border: 1px solid #00788b;color: #00788b;">
								Confirm<img src="../images/loading.gif"
								class="loading" id="confirmDetailsLoading"></button></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
</div>



<table class="addcustomer-display">
	<tr>
		<th style="width: 5%">Sr.No</th>
		<th style="width: 25%">Customer Name</th>
		<th style="width: 15%">Class</th>
		<th style="width: 14%">Add Details</th>
		<th>Quota</th>
		<th>Status</th>
		<th>Gross Price (After Discount)</th>
		<th style="width: 5%"></th>
	</tr>

	<tr>
		<td>1.</td>
		<td>Passenger</td>
		<td></td>
		<td>
			<button title="Add Details" id="adddetails"
				class="imagebutton nobuttonhover nobuttondefaultstyle"
				style="width: 25%; border-radius: 160px; height: 40px; background: radial-gradient(#00788b, #005866);">
				<img src="../images/add.png" class="imagebuttonImage" />
			</button>
			
		</td>
		<td>
		Not Specified
		</td>
		<td>N/A</td>
		<td class="calculate">N/A</td>
		<td>
			<button type="button" title="Delete Passenger" id="delPassenger"
				class="imagebutton nobuttonhover nobuttondefaultstyle">
				<img src="../images/del.png" class="imagebuttonImage" />
			</button>
		<input type="hidden" value="">
		</td>
	</tr>


</table>

<button title="Add Passenger" id="addCustomer"
class="imagebutton nobuttonhover nobuttondefaultstyle"
style="width:4%;top:80px;left:90%;position:relative;height:50px;">
<img src="../images/add-customer.png" class="imagebuttonImage" style="height:50px;width:100%" />
</button>

<sql:setDataSource var="con" driver="<%= (new DatabaseConnection()).getDriver() %>" url="<%= (new DatabaseConnection()).getURL() %>" 
user="<%= (new DatabaseConnection()).getUsername() %>" password="<%= (new DatabaseConnection()).getPassword() %>"/>
<sql:query dataSource="${con}" var="rs">
SELECT tax_id,tax_price from tax;
</sql:query>


<table class="addcustomerprice-display">
	<tr>
		<td>Total Gross Price :</td>
		<td>&#8377;0</td>
	</tr>
<c:forEach var="row" items="${rs.rows}">
	<tr>
		<td><c:out value="${row.tax_id}"/>  (+):</td>
		<td class="taxcalculate"><c:out value="${row.tax_price}"/> %</td>
	</tr>
</c:forEach>	
	<tr>
		<td>Net Gross Price :</td>
		<td>&#8377;0</td>
	</tr>
</table>





<button style="width:25%;top:50px;position:relative;left:30%;
background:url(../images/book.png),linear-gradient(#00788b, #005866);background-position:right;
background-repeat:no-repeat;background-size:contain" id="res_ticket">Reserve Ticket</button>

<form name="moveToPaymentGateway" method="post" action="../railway/bookTopaymentgateway">
<input type="hidden" name="ticketData" value=""/>
</form>
<br><br><br><br><br><br><br><br><br><br>
<jsp:include page="../COMMON/footer.jsp" />