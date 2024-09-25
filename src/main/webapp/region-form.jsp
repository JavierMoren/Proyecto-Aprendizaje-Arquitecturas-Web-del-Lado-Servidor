<%@ include file="header.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<h1><c:out value="${region == null ? '<fmt:message key='msg.region-form.add' />' : '<fmt:message key='msg.region-form.edit' />'}" /></h1>

<c:if test="${not empty errorMessage}">
    <div class="error-message">${errorMessage}</div>
</c:if>

<form action="regions" method="post">
    <input type="hidden" name="id" value="${region != null ? region.id : ''}" />
    <input type="hidden" name="action" value="${region == null ? 'insert' : 'update'}" />

    <label for="code"><fmt:message key="msg.region-form.code" />:</label>
    <input type="text" name="code" id="code" value="${region != null ? region.code : ''}" required />

    <label for="name"><fmt:message key="msg.region-form.name" />:</label>
    <input type="text" name="name" id="name" value="${region != null ? region.name : ''}" required />

    <input type="submit" value="${region == null ? '<fmt:message key='msg.region-form.create' />' : '<fmt:message key='msg.region-form.update' />'}" />
</form>

<a href="regions"><fmt:message key="msg.region-form.returnback" /></a>

<%@ include file="footer.jsp" %>
