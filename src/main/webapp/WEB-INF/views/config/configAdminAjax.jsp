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
	<div class="span-10 colborder">
		<button id="addZNodeRoot">新增根节点</button>
		<button id="addZNodeSub">新增子节点</button>
		<button id="importNode">导入</button>
		<button id="exportNode">导出</button>
		<button id="nodexNode">Nodex配置</button>
		
	  	<ul id="browser" class="filetree">
	  		<c:forEach var="znode" items="${znodeList}" varStatus="count">
	  			<c:if test="${znode.stat.numChildren == 0}">
	  				<li>
	  					<span id="top_${count.count }" value="${znode.id}" class="file" >${znode.name}(${znode.data })</span>
	  				</li>
	  			</c:if>
	  			<c:if test="${znode.stat.numChildren != 0}">
	  				<li>
	  					<span id="top_${count.count }" value="${znode.id}" class="folder" >${znode.name}(${znode.data })</span>
	  					<ul></ul>
	  				</li>
	  			</c:if>
	  		</c:forEach>
		</ul>
    </div>
    <div class="span-12 prepend-1 last">
		<div id="errDiv" class="error" style="display: none"></div>
		<div id="succDiv" class="success" style="display: none"></div>
    
		<p>
		  <input id="addZNode" type="button" value="Add" disabled="disabled" />
		  <input id="deleteZNode" type="button" value="Delete" disabled="disabled" />
          <input id="updateZNode" type="button" value="Update" disabled="disabled" />
        </p>
    	
        <p>
          <label for="znode_name">Name: </label>
          <input type="text" id="znode_name" name="znode_name" class="title">
        </p>
        <p>
          <label for="znode_path">Path: </label>
          <input type="text" id="znode_path" name="znode_path" class="title">
        </p>
        <p>
          <label for="znode_data">Data: </label>
          <textarea style="height:100px;" rows="1" cols="5" name="znode_data" id="znode_data"></textarea>
        </p>
        <!-- <p>
          <label for="znode_stat_czxid">czxid: </label><br>
          <span id="znode_stat_czxid"></span>
        </p>
        <p>
          <label for="znode_stat_mzxid">mzxid: </label><br>
          <span id="znode_stat_mzxid"></span>
        </p>
        <p>
          <label for="znode_ctime">ctime: </label><br>
          <span id="znode_ctime"></span>
        </p>
        <p>
          <label for="znode_mtime">mtime: </label><br>
          <span id="znode_mtime"></span>
        </p>
        <p>
          <label for="znode_stat_version">version: </label><br>
          <span id="znode_stat_version"></span>
        </p>
        <p>
          <label for="znode_stat_cversion">cversion: </label><br>
          <span id="znode_stat_cversion"></span>
        </p>
        <p>
          <label for="znode_stat_aversion">aversion: </label><br>
          <span id="znode_stat_aversion"></span>
        </p>
        <p>
          <label for="znode_stat_ephemeralOwner">ephemeralOwner: </label><br>
          <span id="znode_stat_ephemeralOwner"></span>
        </p>
        <p>
          <label for="znode_stat_dataLength">dataLength: </label><br>
          <span id="znode_stat_dataLength"></span>
        </p>
        <p>
          <label for="znode_stat_numChildren">numChildren: </label><br>
          <span id="znode_stat_numChildren"></span>
        </p>
        <p>
          <label for="znode_stat_pzxid">pzxid: </label><br>
          <span id="znode_stat_pzxid"></span>
        </p> -->
    </div>
</div>
</body>
</html>