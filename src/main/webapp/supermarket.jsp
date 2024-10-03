<%@ include file="header.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<h1><fmt:message key="msg.supermarket.title" /></h1>

<!-- Muestra el mensaje de error si existe -->
<c:if test="${not empty errorMessage}">
    <div style="color: red;">
        <strong><fmt:message key="msg.region.error" />: <fmt:message key="msg.region.error.foreignkey" /> </strong>
    </div>
</c:if>

<!-- Muestra la lista de supermarket solo si no hay error -->
<c:if test="${empty errorMessage}">
    <a href="supermarkets?action=new"><fmt:message key="msg.supermarket.add" /></a>
    <table border="1">
        <thead>
            <tr>
                <th><fmt:message key="msg.supermarket.id" /></th>
                <th><fmt:message key="msg.supermarket.name" /></th>
                <th><fmt:message key="msg.supermarket.actions" /></th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="supermarket" items="${listSupermarket}">
                <tr>
                    <td>${supermarket.id}</td>
                    <td>${supermarket.name}</td>
                    <td>
                        <a href="supermarkets?action=edit&id=${supermarket.id}"><fmt:message key="msg.supermarket.edit" /></a>
                        <form action="supermarkets" method="post" style="display:inline;">
                            <input type="hidden" name="action" value="delete" />
                            <input type="hidden" name="id" value="${supermarket.id}" />
                            <input type="submit" value="<fmt:message key='msg.supermarket.delete' />"
                                   onclick="return confirm('<fmt:message key='msg.supermarket.confirm' />')" />
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</c:if>
<%@ include file="footer.jsp" %>
