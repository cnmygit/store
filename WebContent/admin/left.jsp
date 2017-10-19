<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>菜单</title>
<script src="${ pageContext.request.contextPath }/js/jquery-1.11.3.min.js" type="text/javascript"></script>
<link href="${pageContext.request.contextPath}/css/left.css" rel="stylesheet" type="text/css"/>
<link rel="StyleSheet" href="${pageContext.request.contextPath}/css/dtree.css" type="text/css" />

</head>
<body>
<table width="100" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td height="12"></td>
  </tr>
</table>
<table width="100%" border="0">
  <tr>
    <td>
		<div class="dtree">

		<a href="javascript: d.openAll();">展开所有</a> | <a href="javascript: d.closeAll();">关闭所有</a>
		
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/dtree.js"></script>
		<script type="text/javascript">
		
			d = new dTree('d');
			$(function(){
				$.post("${ pageContext.request.contextPath }/MenuServlet",{"method":"findMenu"},function(data){
					$(data).each(function(i,n){
						if(n.menu_url == "#") {
							d.add(n.menu_id,n.parent_id,n.menu_bar) ;
						}else {
							d.add(n.menu_id,n.parent_id,n.menu_bar,"${ pageContext.request.contextPath }"+n.menu_url,'','mainFrame') ;
						}
					});
					document.write(d);
				},'json') ;
			}) ;
			
		</script>
	</div>	
	</td>
  </tr>
</table>
</body>
</html>
