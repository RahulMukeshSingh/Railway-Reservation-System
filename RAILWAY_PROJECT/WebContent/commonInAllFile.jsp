<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="../JSTLTagLib/jstlTaglib.jsp" %>
<jsp:include page="../COMMON/header.jsp" />

<jsp:include page="../COMMON/footer.jsp" />
${sessionScope.userid}<br>
${sessionScope.fname}<br>
${sessionScope.role}<br>
${sessionScope.date}<br>
${sessionScope.source}<br>
${sessionScope.destination}<br>
${sessionScope.trainno}<br>
${sessionScope.trainreturn}<br>