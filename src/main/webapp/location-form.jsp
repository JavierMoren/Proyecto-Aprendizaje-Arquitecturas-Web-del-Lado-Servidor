<%@ include file="header.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<h1>
    <c:choose>
        <c:when test="${location == null}">
            <fmt:message key="msg.locations-form.add" />
        </c:when>
        <c:otherwise>
            <fmt:message key="msg.locations-form.edit" />
        </c:otherwise>
    </c:choose>
</h1>

<c:if test="${not empty errorMessage}">
    <div class="error-message">${errorMessage}</div>
</c:if>

<form action="locations" method="post">
    <input type="hidden" name="id" value="${location != null ? location.id : ''}">

    <!-- Campo para la dirección -->
    <label for="address"><fmt:message key="msg.locations-form.address" /></label>
    <input type="text" id="address" name="address" value="${location != null ? location.address : ''}" required>

    <!-- Campo para la ciudad -->
    <label for="city"><fmt:message key="msg.locations-form.city" /></label>
    <input type="text" id="city" name="city" value="${location != null ? location.city : ''}" required>

    <!-- Desplegable para el supermercado -->
    <label for="supermarket_id"><fmt:message key="msg.locations-form.supermarket" /></label>
    <select id="supermarket_id" name="supermarket_id" required>
        <c:forEach var="supermarket" items="${listSupermarkets}">
            <option value="${supermarket.id}"
                ${location != null && location.supermarket.id == supermarket.id ? 'selected' : ''}>
                ${supermarket.name}
            </option>
        </c:forEach>
    </select>

    <!-- Desplegable para la provincia -->
    <label for="province_id"><fmt:message key="msg.locations-form.province" /></label>
    <select id="province_id" name="province_id" required>
        <c:forEach var="province" items="${listProvinces}">
            <option value="${province.id}"
                ${location != null && location.province.id == province.id ? 'selected' : ''}>
                ${province.name}
            </option>
        </c:forEach>
    </select>

    <!-- Botón para enviar el formulario -->
    <input type="submit" value="${location != null ? 'Actualizar' : 'Crear'}">
    <input type="hidden" name="action" value="${location != null ? 'update' : 'insert'}">
</form>

<%@ include file="footer.jsp" %>
