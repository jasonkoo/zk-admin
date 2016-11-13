<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
	<%@include file="../common/head.jsp" %>
</head>
<body>
<div class="container">
	<h1> 
		ZK Configration Admin 
	</h1>
   	<%@include file="../common/menu.jsp" %>
	<hr>	
	<div>
		<table cellspacing="0" cellpadding="0" border="0">
		  <caption><em>${path }</em></caption>
		  <thead>
		    <tr>
		      <th class="span-2" rowspan="1">name</th>
		      <th class="span-2" rowspan="1">IP-PORT</th>
		      <th class="span-2 last" rowspan="1">op</th>
		    </tr>
		  </thead>
		  <tfoot>
		    <tr>
		      <td colspan="2"><a href="./redisConfig/edit?dbname=">create</a></td>
		    </tr>
		  </tfoot>
		  <tbody>
		    <c:forEach var="redisConfig" items="${redisConfigList}">
			    <tr>
			      <td>${redisConfig.name}</td>
			      <td>${redisConfig.ipAndPort}</td>
			      <%-- <td>
			      	<c:forEach var="bizName" items="${redisConfig.bizNameList}">
			      		${bizName }<br/>
			      	</c:forEach>
			      </td> --%>
			      <td><a href="./redisConfig/edit?dbname=${redisConfig.name }">edit</a></td>
			    </tr>
		    </c:forEach>
		  </tbody>
		</table>
    </div>
</div>
</body>
</html>