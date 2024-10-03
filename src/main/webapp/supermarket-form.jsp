<%@ include file="header.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:set var="formTitle" value="${supermarket == null ? 'msg.supermarket-form.add' : 'msg.supermarket-form.edit'}" />
<h1><fmt:message key="${formTitle}" /></h1>

<c:if test="${not empty errorMessage}">
    <div class="error-message">${errorMessage}</div>
</c:if>

<form action="supermarkets" method="post">
    <input type="hidden" name="id" value="${supermarket != null ? supermarket.id : ''}" />
    <input type="hidden" name="action" value="${supermarket == null ? 'insert' : 'update'}" />

    <label for="name"><fmt:message key="msg.supermarket-form.name" />:</label>
    <input type="text" name="name" id="name"
           value="${supermarket != null && supermarket.name != null ? supermarket.name : ''}" required />

    <c:set var="submitLabel" value="${supermarket == null ? 'msg.supermarket-form.create' : 'msg.supermarket-form.update'}" />
    <input type="submit" value="<fmt:message key='${submitLabel}' />" />
</form>

<%@ include file="footer.jsp" %>
