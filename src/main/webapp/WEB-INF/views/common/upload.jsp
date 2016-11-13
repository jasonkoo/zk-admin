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
		function uploadFile(){
		var file = $("#fileUpload").val();  
        if(file==""){  
            alert("请选择上传的文件");  
            return;  
        }  
        else{  
        	var path=$("#path").val();
        	var overwrite=false;
        	if($("#overwrite").attr("checked")=="checked"){
        		overwrite=true;
        	}
            var url = "importNode?nowtime="+new Date().getTime()+"&path="+path+"&overwrite="+overwrite;  
            $.ajaxFileUpload({  
                url:url,  
                secureuri:false,  
                fileElementId:"fileUpload",        //file的id  
                    dataType:"text",                  //返回数据类型为文本  
                success:function(data,status){  
                    alert("上传成功");
                    window.close();
                }  
            })  
        }  
	}
	</script>
</head>
<body>
<form enctype='multipart/form-data' method='post' action='./configAjax/importNode'>
	请选择文件(txt)：<input type='file' id='fileUpload' name='fileUpload'/><br/>
	<input type="hidden" id="path" name="path" value="${param.path }"/><br/>
	是否覆蓋：<input id="overwrite" type="checkbox" name="overwrite" /><br/>
	<input type='button' value='提交' onclick='uploadFile()'/>
</form>		  				
</body>
</html>