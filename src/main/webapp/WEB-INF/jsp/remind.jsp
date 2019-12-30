<%@ page language="java" contentType="text/html; charset=US-ASCII" pageEncoding="US-ASCII"%>

<%@ page isELIgnored="false"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <title>Bootstrap Example</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
</head>

<body>

    <c:if test = "${not empty error}">
        <div class="alert alert-danger" role="alert">
          Cannot Set Reminder. Error: <c:out value="${error}" />
        </div>
    </c:if>

    <c:if test = "${success}">
        <div class="alert alert-success" role="alert">
          Reminder Successfully set
        </div>
    </c:if>

    <div class="container">
        <h2>Its Time to Book Uber</h2>

        <form action="/remind" method="post">
            <div class="form-group">
                <label for="source">Source:</label>
                <input type="text" name="source" value="12.927880,77.627600" class="form-control" placeholder="Latitude,Longitude">
            </div>
            <div class="form-group">
                <label for="destination">Destination:</label>
                <input type="text" name="destination" value="13.035542,77.597100" class="form-control" placeholder="Latitude,Longitude">
            </div>
            <div class="form-group">
                <label for="time">Time:</label>
                <input type="datetime-local" name="time" value="2019-12-30T14:00:00" class="form-control">
            </div>
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" name="email" value="some.user@test.com" class="form-control" placeholder="Email Id">
            </div>
            <button type="submit" class="btn btn-primary">Remind Me</button>
        </form>
    </div>

    <hr />

    <div class="container">
        <h2>API Call Logs</h2>
        <ul class="list-group">
          <c:forEach items="${logs}" var="log">
             <li class="list-group-item"><c:out value="${log}" /></li>
          </c:forEach>
        </ul>
    </div>

</body>

</html>