<%@ include file="header.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<h1><c:out value="${Province == null ? fmt:message(key='msg.province-form.add') : fmt:message(key='msg.province-form.edit')}" /></h1>

<c:if test="${not empty errorMessage}">
    <div class="error-message">${errorMessage}</div>
</c:if>


<form action="provinces" method="post" style="display:inline;">
    <input type="hidden" name="id" value="${Province != null ? Province.id : ''}" />
    <input type="hidden" name="action" value="${Province == null ? 'insert' : 'update'}" />

    <label for="code"><fmt:message key="msg.province-form.code" />:</label>
    <input type="text" name="code" id="code" value="${Province != null ? Province.code : ''}" required />

    <label for="name"><fmt:message key="msg.province-form.name" />:</label>
    <input type="text" name="name" id="name" value="${Province != null ? Province.name : ''}" required />

    <input type="submit" value="${Province == null ? fmt:message(key='msg.province-form.create') : fmt:message(key='msg.province-form.update')}" />
</form>

<a href="provinces"><fmt:message key="msg.province-form.returnback" /></a>

<%@ include file="footer.jsp" %>
