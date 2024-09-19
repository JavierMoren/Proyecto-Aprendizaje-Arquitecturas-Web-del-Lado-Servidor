<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ include file="header.jsp" %>
    <h2>Hello World!</h2>
    <h1>Number List:</h1>
    <ul>
        <c:forEach var="i" begin="1" end="5">
            <li>Number: ${i}</li>
        </c:forEach>
    </ul>
<%@ include file="footer.jsp" %>

