<%@ include file="header.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<h1>
    <c:choose>
        <c:when test="${Province == null}">
            <fmt:message key="msg.province-form.add" />
        </c:when>
        <c:otherwise>
            <fmt:message key="msg.province-form.edit" />
        </c:otherwise>
    </c:choose>
</h1>

<c:if test="${not empty errorMessage}">
    <div class="error-message">${errorMessage}</div>
</c:if>

<form action="provinces" method="post" style="display:inline;">
    <input type="hidden" name="id" value="${Province != null ? Province.id : ''}" />
    <input type="hidden" name="action" value="${Province == null ? 'insert' : 'update'}" />

    <label for="code"><fmt:message key="msg.province-form.code" />:</label>
    <input type="text" name="code" id="code" value="${Province != null ? province.code : ''}" required />

    <label for="name"><fmt:message key="msg.province-form.name" />:</label>
    <input type="text" name="name" id="name" value="${Province != null ? province.name : ''}" required />

    <label for="id_region"><fmt:message key="msg.province-form.id_region" />:</label>
    <input type="text" name="id_region" id="id_region" value="${Province != null ? province.region.id : ''}" required />

    <input type="submit" value="<c:choose><c:when test='${Province == null}'><fmt:message key='msg.province-form.create' /></c:when><c:otherwise><fmt:message key='msg.province-form.update' /></c:otherwise></c:choose>" />
</form>

<a href="provinces"><fmt:message key="msg.province-form.returnback" /></a>

<%@ include file="footer.jsp" %>
