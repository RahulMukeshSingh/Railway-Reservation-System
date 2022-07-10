<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="../JSTLTagLib/jstlTaglib.jsp" %>
<jsp:include page="../COMMON/header.jsp" />
<script src="../js/index.js"></script>
<!--  -->
<c:if test="${param.success != null}">
<script>
$(document).ready(function(){
	alertBox("Sign Up Successful!");
});
</script>
</c:if>
<!--  -->


<center>
	<form method="post" id="authenticate" action="/RAILWAY_PROJECT/railway/loginAuth" >
		<fieldset style="width: 30%">
			<legend>
				<H2>Login</H2>
			</legend>
			<table style="width: 400px; height: 120px">
				<tr>
					<td colspan="2"><label id="userhideeffect"><b>User ID</b></label></td>
				</tr>
				<tr>
					<td colspan="2"><input type="text" placeholder="User ID"
					name="userid"	id="userhideeffectinput" class="user-icon" autocomplete="off"/></td>
				</tr>
				<tr>
					<td colspan="2"><label id="passhideeffect"><b>Password</b></label></td>
				</tr>

				<tr>
					<td colspan="2"><input type="password"
					name="password"	placeholder="Password" id="passhideeffectinput" class="password-icon" /></td>
				</tr>
			</table>
			<br>
			<br>
		</fieldset>
		<table width="400" height="80">
			<tr>
				<td>
					<button type="button" id="goToValidate">Submit</button>
				</td>
				<td>
					<button type="button" id="signup">Sign Up</button>
				</td>
			</tr>
		</table>
	</form>

</center>

<jsp:include page="../COMMON/footer.jsp" />

