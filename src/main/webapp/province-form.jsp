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

<form action="provinces" method="post">
    <input type="hidden" name="id" value="${province != null ? province.id : ''}">

    <!-- Campo para el código de la provincia -->
    <label for="code"><fmt:message key="msg.province-form.code" /></label>
    <input type="text" id="code" name="code" value="${province != null ? province.code : ''}" required>

    <!-- Campo para el nombre de la provincia -->
    <label for="name"><fmt:message key="msg.province-form.name" /></label>
    <input type="text" id="name" name="name" value="${province != null ? province.name : ''}" required>

    <!-- Desplegable para elegir la Comunidad Autónoma (Región) -->
    <label for="id_region"><fmt:message key="msg.province-form.region" /></label>
    <select id="id_region" name="id_region" required>
        <c:forEach var="region" items="${listRegions}">
            <option value="${region.id}" ${province != null && province.region.id == region.id ? 'selected' : ''}>
            ${region.id} - ${region.name}
            </option>
        </c:forEach>
    </select>

    <!-- Botón para enviar el formulario -->
    <input type="submit" value="${province != null ? 'Actualizar' : 'Crear'}">
    <input type="hidden" name="action" value="${province != null ? 'update' : 'insert'}">
</form>



<%@ include file="footer.jsp" %>
