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
		<div id="errDiv" class="error" <c:if test="${empty errMsg }">style="display: none"</c:if>>${errMsg }</div>
		
		<form id="redisConfig" name="redisConfig" action="update?oldDbName=${redisConfig.name }" method="post" >
			<table cellspacing="0" cellpadding="0" border="0">
			  <caption><em>${path } redis config edit</em></caption>
			  <tbody>
			    <tr>
			      <td class="span-3"><label>name:</label></td>
			      <td class="span-3">
			      	<input type="text" id="name" name="name" value="${redisConfig.name }" />
			      </td>
			    </tr>
			    <tr>
			      <td class="span-3"><label>存活ip&port:</label></td>
			      <td class="span-3">
			      	<input type="text" id="ipAndPort" name="ipAndPort" value="${redisConfig.ipAndPort }" />
			      </td>
			    </tr>
			    <tr>
			      <td class="span-3"><label>Deadip&port:</label></td>
			      <td class="span-3">
			      	<input type="text" id="deadIpAndPort" name="deadIpAndPort" value="${redisConfig.deadIpAndPort }" />
			      </td>
			    </tr>
			    <tr>
			      <td class="span-3"><label>密码:</label></td>
			      <td class="span-3">
			      	<input type="text" id="password" name="password" value="${redisConfig.password }" />
			      </td>
			    </tr>
			    <tr>
			      <td class="span-3"><label>maxActive:</label></td>
			      <td class="span-3">
			      	<input type="text" id="maxActive" name="maxActive" value="${redisConfig.maxActive }" />
			      </td>
			    </tr>
			    <tr>
			      <td class="span-3"><label>maxIdle:</label></td>
			      <td class="span-3">
			      	<input type="text" id="maxIdle" name="maxIdle" value="${redisConfig.maxIdle }" />
			      </td>
			    </tr>
			    <tr>
			      <td class="span-3"><label>maxWait:</label></td>
			      <td class="span-3">
			      	<input type="text" id="maxWait" name="maxWait" value="${redisConfig.maxWait }" />
			      </td>
			    </tr>
			    <tr>
			      <td class="span-3"><label>testOnBorrow:</label></td>
			      <td class="span-3">
			      	<input type="text" id="testOnBorrow" name="testOnBorrow" value="${redisConfig.testOnBorrow }" />
			      </td>
			    </tr>
			    <tr>
			      <td class="span-3"><label>testOnReturn:</label></td>
			      <td class="span-3">
			      	<input type="text" id="testOnReturn" name="testOnReturn" value="${redisConfig.testOnReturn }" />
			      </td>
			    </tr>
			    
			  </tbody>
			 <%--  <tfoot id="dbConfigTfoot">
			    <tr>
			      <td><label>bizNames:</label></td>
			      <td><input type="text" id="inputTempBizName" size="30" /></td>
			      <td><input class="added" id="addTempBizName" type="button" value="add" /></td>
			    </tr>
			    <c:forEach var="bizName" items="${redisConfig.bizNameList}" >
				    <tr class="quiet">
				      <td></td>
				      <td>
				      	${bizName }
				      	<input class="hide" type="hidden" id="bizNameList" name="bizNameList" value="${bizName }" />
				      </td>
				      <td><input class="removed" type="button" value="delete" /></td>
				    </tr>
			    </c:forEach>
			  </tfoot> --%>
			</table>
			<%-- <input id="oldBizNameList" type="hidden" value="${redisConfig.bizNameList}" /> --%>
			
			<p><input id="saveBtn" type="button" value="Save" /></p>
		</form>
    </div>
</div>
</body>
</html>