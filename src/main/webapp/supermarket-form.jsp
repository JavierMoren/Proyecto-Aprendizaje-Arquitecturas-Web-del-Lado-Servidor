<%@ include file="header.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:set var="formTitle" value="${region == null ? 'msg.region-form.add' : 'msg.region-form.edit'}" />
<h1><fmt:message key="${formTitle}" /></h1>

<c:if test="${not empty errorMessage}">
    <div class="error-message">${errorMessage}</div>
</c:if>

<form action="regions" method="post">
    <input type="hidden" name="id" value="${region != null ? region.id : ''}" />
    <input type="hidden" name="action" value="${region == null ? 'insert' : 'update'}" />

    <label for="code"><fmt:message key="msg.region-form.code" />:</label>
    <input type="text" name="code" id="code"
           value="${region != null && region.code != null ? region.code : ''}" required />

    <label for="name"><fmt:message key="msg.region-form.name" />:</label>
    <input type="text" name="name" id="name"
           value="${region != null && region.name != null ? region.name : ''}" required />

    <c:set var="submitLabel" value="${region == null ? 'msg.region-form.create' : 'msg.region-form.update'}" />
    <input type="submit" value="<fmt:message key='${submitLabel}' />" />
</form>

<a href="regions"><fmt:message key="msg.region-form.returnback" /></a>

<%@ include file="footer.jsp" %>
