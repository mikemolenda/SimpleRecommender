<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/templates/_header.jsp"></jsp:include>

<h2>User-based recommendations for User ${userId}</h2>

<p>This user rated ${numRated} items.</p>

<table class="table table-striped">
    <tr>
        <th>Item ID</th>
        <th>Similarity</th>
    </tr>

    <c:forEach items="results" var="result">
        <tr>
            <td>${result.getItemId()}</td>
            <td>${result.getValue()}</td>
        </tr>
    </c:forEach>

</table>

<jsp:include page="/templates/_footer.jsp"></jsp:include>
