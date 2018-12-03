<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Let' Share</title>
  <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/bootstrap2.css">
  <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/navbar.css">
  <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/semantic.css">
  <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/style.css">

  <script src="<%=request.getContextPath() %>/js/jquery.js"></script>
  <script src="<%=request.getContextPath() %>/js/semantic.js"></script>
  <script src="<%=request.getContextPath() %>/js/basic.js"></script>
  <script src="<%=request.getContextPath() %>/js/code.js"></script>
</head>
<body>
  <%@ include file="topbar.jsp" %>
	<div class="container">
		<div class="row">
			<div class="span4">
				<div class="ui vertical menu">
				  <a class="item" href='<c:url value="/followings"/>' >
				    关注
				  </a>
				  <a class="active teal item" href='<c:url value="/followers"/>'>
				    粉丝
				  </a>
				</div>
			
			</div>
			<div class="span8">
				<div class="ui header">
					我的关注
				</div>
				<div class="ui divider">
				</div>

				<div class="ui cards">
					<c:forEach items="${followers }" var="follower">
			            <div class="ui card" style="width:33%">
			              <div class="ui small centered circular  image">
			                <a href="<c:url value="/user/${follower.id }" />"><img src="<c:url value="http://image.share.com/${follower.user_avatar }"/> "></a>
			              </div>
			              <div class="content">
			                <a class="header centered" href="<c:url value="/user/${following.id}" />">
			                	${follower.user_name }
			                </a>
			              </div>
			            </div> 
					
					
					</c:forEach>
				
				
				</div>
				<!-- end cards -->

			</div>
		</div>
	
	</div>
</body>
</html>