<%@ include file="header.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<h1><fmt:message key="msg.locations.title" /></h1>

<!-- Muestra el mensaje de error si existe -->
<c:if test="${not empty errorMessage}">
    <div style="color: red;">
        <strong><fmt:message key="msg.locations.error" />: <fmt:message key="msg.locations.error.foreignkey" /></strong>
    </div>
</c:if>

<!-- Muestra la lista de ubicaciones solo si no hay error -->
<c:if test="${empty errorMessage}">
    <a href="locations?action=new"><fmt:message key="msg.locations.add" /></a>
    <table border="1">
        <thead>
            <tr>
                <th><fmt:message key="msg.locations.id" /></th>
                <th><fmt:message key="msg.locations.address" /></th>
                <th><fmt:message key="msg.locations.city" /></th>
                <th><fmt:message key="msg.locations.supermarket" /></th>
                <th><fmt:message key="msg.locations.province" /></th>
                <th><fmt:message key="msg.locations.actions" /></th>
            </tr>
        </thead>
        <tbody>
            <!-- Itera sobre las ubicaciones y las muestra en la tabla -->
            <c:forEach var="location" items="${listLocations}">
                <tr>
                    <td>${location.id}</td>
                    <td>${location.address}</td>
                    <td>${location.city}</td>
                    <td>${location.supermarket.name}</td>
                    <td>${location.province.name}</td>
                    <td>
                        <!-- Enlace para editar la ubicación -->
                        <a href="locations?action=edit&id=${location.id}">
                            <fmt:message key="msg.locations.edit" />
                        </a>
                        <!-- Formulario para eliminar la ubicación -->
                        <form action="locations" method="post" style="display:inline;">
                            <input type="hidden" name="action" value="delete" />
                            <input type="hidden" name="id" value="${location.id}" />
                            <input type="submit" value="<fmt:message key='msg.locations.delete' />"
                                   onclick="return confirm('<fmt:message key='msg.locations.confirm' />')" />
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</c:if>

<%@ include file="footer.jsp" %>
