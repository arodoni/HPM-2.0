<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.zuora.hosted.lite.util.HPMHelper" %>
<%
	HPMHelper hpmHelper = HPMHelper.getInstance();

	String message = "";
	if("Response_From_Submit_Page".equals(request.getParameter("responseFrom"))) {
		if("true".equals(request.getParameter("success"))) {
			// Validate signature. Signature's expired time is 30 minutes.
			try {
				if(!hpmHelper.isValidSignature(request.getParameter("field_passthrough4"), request.getParameter("signature"), 1000 * 60 * 30)) {
					throw new Exception("Signature is invalid.");
				}
			} catch(Exception e) {
				// TODO: Error handling code should be added here.			
				
				throw e;
			}
			
			// Submitting hosted page succeeds.
			message = "Hosted Page submits successfully. The payment method id is " + request.getParameter("refId") + ".";
		} else {
			// Submitting hosted page fails.
			message = "Hosted Page fails to submit. The reason is: " + request.getParameter("errorMessage") + ".";
		}		
	} else {
		// Requesting hosted page fails.
		message = "Hosted Page fails to load. The reason is: " + request.getParameter("errorMessage") + ".";
	}
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<link href="css/hpm2samplecode.css" rel="stylesheet" type="text/css" />
<title>Result</title>
<script type="text/javascript">
function hideBackToHomepage() {
	if(window.parent != window) {
		// Inline, submit button outside hosted page. Hide the 'Back To Homepage' button.
		document.getElementById("backToHomepage").style.display = "none";
	}	
}
</script>
</head>
<body onload="hideBackToHomepage()">
	<div class="firstTitle"><font size="4"><%=message%></font></div>
	<div id="backToHomepage" class="item"><button onclick='window.location.replace("Homepage.jsp")' style="margin-left: 150px; width: 140px; height: 24px;">Back To Homepage</button></div>
</body>
</html>

<script type="text/javascript">
if(window.parent != window) {
	// Inline, submit button outside hosted page.
	<%
	if("Response_From_Submit_Page".equals(request.getParameter("responseFrom"))) {
		if("true".equals(request.getParameter("success"))) {
			// Submitting hosted page succeeds.
	%>
	window.parent.submitSucceed();
	<%
		} else {
			// Submitting hosted page fails.
	%>
	window.parent.submitFail("<%=request.getParameter("errorMessage").replace('\n', ' ')%>");
	<%
		}
	}
	%>	
}
</script>