<%@ page import="railwayFrequentFunctions.DateFunctions"%>
<%@ include file="../JSTLTagLib/jstlTaglib.jsp" %>
<jsp:include page="../COMMON/header.jsp" />
<script src='https://www.google.com/recaptcha/api.js'></script>
<script src="../js/signup.js"></script>
<center>

	<H2>Registration Page</H2>
	<form name="form1" method="post" action="/RAILWAY_PROJECT/railway/signUpSuccessful">

		<fieldset>
			<legend>Login Details</legend>
			<table style="width: 570px; height: 120px">

				<tr>
					<td>User ID:
					<img src="../images/loading.gif" class="loading" id="userid_loading">
					</td>
					<td><input type="text" name="txt_userid"
						placeholder="Anything You Think Relevant">
					<img src="" class="validity" id="userid_validity">
					</td>
				</tr>
				<tr>
					<td></td>
					<td class="warning" id="userid_warn"></td>
				</tr>
				<tr>
					<td>Password:<br>
					<button type="button" title="Show Password" class="imagebutton nobuttonhover nobuttondefaultstyle"> 
					<img src="../images/eyeclose.png" class="eyepassword"></button></td>
					<td><input type="password" name="txt_password"
						placeholder="Type Password Be Secured">
						<img src="" class="validity" id="password_validity"></td>
				</tr>
				<tr>
					<td></td>
					<td class="warning" id="password_warn"></td>
				</tr>
				<tr>
					<td>Retype Password:</td>
					<td><input type="password" name="txt_retypepassword"
						placeholder="Retype Password" disabled>
						<img src="" class="validity" id="retypepassword_validity"></td>
				</tr>
				<tr>
					<td></td>
					<td class="warning" id="retypepassword_warn"></td>
				</tr>
			</table>
		</fieldset>

		<br>
		<fieldset>
			<legend>Personal Details</legend>
			<table style="width: 570px; height: 500px;">

				<tr>
					<td>Full Name:</td>
					<td><input type="text" name="txt_fname" 
						placeholder="Sweet Name"><img src="" class="validity" id="fname_validity"></td>
				</tr>
				<tr>
					<td></td>
					<td class="warning" id="fname_warn"></td>
				</tr>
				<tr>
					<td>Mobile No:</td>
					<td><input type="text" name="mobile" 
						placeholder="9999999999"><img src="" class="validity" id="mobile_validity"></td>
				</tr>
				<tr>
					<td></td>
					<td class="warning" id="mobile_warn"></td>
				</tr>	
				<tr>
					<td>Gender:</td>
					<td><select name="gender">
					<option value="" disabled selected hidden="true">---Gender---</option>
					<option value="male">Male</option>
					<option value="female">Female</option>
					<option value="others">Others</option>
					</select>
					<img src="" class="validity" id="gender_validity"></td>
				</tr>
				<tr>
				    <td></td>
					<td class="warning" id="gender_warn"></td>
				</tr>
				<tr>
					<td>Marital Status:</td>
					<td><select name="marital">
					<option value="" disabled selected hidden="true">---Marital Status---</option>
					<option value="married">Married</option>
					<option value="unmarried">Unmarried</option>
					</select>
					<img src="" class="validity" id="marital_validity"></td>
				</tr>
				<tr>
					<td></td>
					<td class="warning" id="marital_warn"></td>
				</tr>
				<tr>
					<td>Date Of Birth:</td>
					<td><input type="date" name="DOB" id="DOB"
					max="<%= (new DateFunctions()).dateforMinAge(18) %>"
					min="<%= (new DateFunctions()).dateforMinAge(60) %>">
					<img src="" class="validity" id="dob_validity"></td>
				</tr>
				<tr>
					<td></td>
					<td class="warning" id="dob_warn"></td>
				</tr>
				<tr>
					<td>State:
					<img src="../images/loading.gif" class="loading" id="state_loading">
					</td>
					<td><select name="state">

							<option value="" disabled selected hidden="true">---Select
								State---</option>
					</select>
					<img src="" class="validity" id="state_validity">
					</td>
				</tr>
				<tr>
					<td></td>
					<td class="warning" id="state_warn"></td>
				</tr>
				<tr>
					<td>District:
					<img src="../images/loading.gif" class="loading" id="district_loading">
					</td>
					<td><select name="district" disabled>
							<option value="" disabled selected hidden="true">---Select
								District---</option>

					</select>
					<img src="" class="validity" id="district_validity">
					</td>
				</tr>
				<tr>
					<td></td>
					<td class="warning" id="district_warn"></td>
				</tr>
				<tr>
					<td>Address:</td>
					<td><textarea rows="4" cols="30"
						name="address"	placeholder="Enter your Residential Address"></textarea>
					<img src="" class="validity" id="address_validity"></td>
				</tr>
				<tr>
					<td></td>
					<td class="warning" id="address_warn"></td>
				</tr>
				<tr>
					<td>Email:</td>
					<td><input type="email" name="email"
						id="email" placeholder="xyz@abc.com" class="email-icon">
					<img src="" class="validity" id="email_validity"></td>
				</tr>
				<tr>
					<td></td>
					<td class="warning" id="email_warn"></td>
				</tr>
				<tr id="emailotptd">
				<td><img src="../images/loading.gif" class="loading" id="otp_loading"></td>
				<td>
				<button type="button" id="forotp" class="nobuttonhover"
				style="width:80%;height:100%;">GET OTP</button>
				<button type="button" id="foremail" class="nobuttonhover"
				style="width:80%;height:100%;">CHANGE EMAIL/RESEND</button>
				<img src="" class="validity"></td>
				</tr>
				<tr id="otptd">
					<td>OTP:<img src="../images/loading.gif" class="loading" id="otpcheck_loading"></td>
					<td><input type="text" name="OTP" placeholder="AB3456">
					
					<img src="" class="validity" id="otp_validity"></td>
				</tr>
				<tr id="otptd1">
					<td></td>
					<td class="warning" id="otp_warn"></td>
				</tr>
				<tr>
					<td></td>
					<td>
					<div style="text-align: center">
						<div class="g-recaptcha"
							data-sitekey="6Lc48jwUAAAAAP3LTfSG8y7blgt1EgBhu-jCzora" style=" display: inline-block;">
						</div>
						<img src="" class="validity">
					</div>	
					</td>

				</tr>
				<tr>
					<td colspan="2" class="warning" id="recaptcha_warn"></td>
				</tr>
			</table>

		</fieldset>
		<br> <br>

		<button type="button" name="signup" id="button1">Sign Up</button>


	</form>


</center>

<jsp:include page="../COMMON/footer.jsp" />
