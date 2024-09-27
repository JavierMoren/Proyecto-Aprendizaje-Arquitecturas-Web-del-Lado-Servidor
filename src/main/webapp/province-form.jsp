<%@ include file="header.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<h1>
    <c:choose>
        <c:when test="${province == null}">
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
    <input type="hidden" name="id" value="${province != null ? province.id : ''}" />
    <input type="hidden" name="action" value="${province == null ? 'insert' : 'update'}" />

    <label for="code"><fmt:message key="msg.province-form.code" />:</label>
    <input type="text" name="code" id="code" value="${province != null && province.code != null ? province.code : ''}" required />

    <label for="name"><fmt:message key="msg.province-form.name" />:</label>
    <input type="text" name="name" id="name" value="${province != null && province.name != null ? province.name : ''}" required />

    <label for="id_region"><fmt:message key="msg.province-form.id_region" />:</label>
    <input type="text" name="id_region" id="id_region" value="${province != null && province.region != null && province.region.id != null ? province.region.id : ''}" required />

    <input type="submit" value="<c:choose> <c:when test='${province == null}'><fmt:message key='msg.province-form.create' /></c:when><c:otherwise><fmt:message key='msg.province-form.update' /></c:otherwise></c:choose>" />
</form>

<a href="provinces"><fmt:message key="msg.province-form.returnback" /></a>

<%@ include file="footer.jsp" %>
