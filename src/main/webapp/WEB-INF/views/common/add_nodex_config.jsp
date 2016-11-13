<%@page contentType="text/html;charset=UTF-8"%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
	<%@include file="../common/head.jsp" %>
	<script type="text/javascript">
		function submitData(){
			var data=$("#data").val();
			var overwrite=false;
        	if($("#overwrite").attr("checked")=="checked"){
        		overwrite=true;
        	}
			$.ajax({  
                async:false,//使用同步的Ajax请求  
                type: "POST",  
                url: "addNodexConfig",  
                data: {"data":data, "overwrite":overwrite},  
                success: function(msg){  
                	alert("导入成功");
                	window.close();
                },
                error:function(){
                	alert("error");
                }
            });  
		}
	</script>
</head>
<body>
<form method='post' action='addNodexConfig'>
	请输入配置内容：<textarea id="data" name="data" rows="60" cols="100" style="width:600px;height:300px">${data }</textarea><br/>
	是否覆蓋：<input id="overwrite" type="checkbox" name="overwrite" /><br/>
	<input type='button' value='提交' onclick="submitData()"/>
</form>		  				
</body>
</html>