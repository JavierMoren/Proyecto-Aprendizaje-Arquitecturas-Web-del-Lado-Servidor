<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ include file="header.jsp" %>

<h2><fmt:message key="msg.saludo"/></h2>

<div>
    <form action="provinces" method="get">
        <button type="submit">
            <fmt:message key="msg.province.title" />
        </button>
    </form>

    <form action="regions" method="get">
        <button type="submit">
            <fmt:message key="msg.region.title" />
        </button>
    </form>
    <form action="supermarkets" method="get">
        <button type="submit">
             <fmt:message key="msg.supermarket.title" />
        </button>
    </form>
    <form action="locations" method="get">
        <button type="submit">
             <fmt:message key="msg.locations.title" />
        </button>
    </form>
</div>

<%@ include file="footer.jsp" %>
