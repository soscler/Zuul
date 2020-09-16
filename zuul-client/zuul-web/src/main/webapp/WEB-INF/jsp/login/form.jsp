<%@ taglib prefix='c' uri='http://java.sun.com/jstl/core_rt' %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="z" tagdir="/WEB-INF/tags/zuul" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Login</title>
</head>

<body>
<jsp:include page="_errorCheck.jsp"/>
<div class="row">
    <div class="span12">
        <div class="page-header">
            <h1>Login</h1>
        </div>
        <form class="form-horizontal" method='POST' action='${pageContext.request.contextPath}/login'>
            <div class="control-group">
                <label class="control-label" for="username">Username</label>
                <div class="controls">
                    <input type="text" name="username" id="username" value="${fn:escapeXml(SPRING_SECURITY_LAST_USERNAME)}" />
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="password">Password</label>
                <div class="controls">
                    <input type="password" name="password" id="password"/>
                </div>
            </div>
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">Submit</button>
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
    </div>

</div>

</body>
</html>
