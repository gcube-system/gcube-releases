<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%-- Uncomment below lines to add portlet taglibs to jsp
<%@ page import="javax.portlet.*"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>

<portlet:defineObjects />
--%>
<script
	src='<%=request.getContextPath()%>/workspace/js/jquery-1.10.1.min.js'></script>
<script
	src='<%=request.getContextPath()%>/workspace/js/bootstrap.min.js'></script>

<link rel="stylesheet"
	href="<%=request.getContextPath()%>/resources/css/gxt-all.css"
	type="text/css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/workspaceportlet.css"
	type="text/css">


<script type="text/javascript" language="javascript"
	src="<%=request.getContextPath()%>/workspace/workspace.nocache.js"></script>
<div id="workspaceDiv"></div>