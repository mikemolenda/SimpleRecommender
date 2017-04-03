<jsp:include page="/templates/_header.jsp"></jsp:include>

<h2>Recommender Demo</h2>

<p>Enter a User ID to get that user's recommendations.</p>

<form action="/recommend">

    <div class="form-group">
        <label for="userId">User ID:</label>
        <input type="text" class="form-control" id="userId" name="userId">
        <input type="submit" class="btn btn-default" value="Submit">
    </div>

</form>

<jsp:include page="/templates/_footer.jsp"></jsp:include>
