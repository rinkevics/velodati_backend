<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
    <title>Admin</title>
</head>
<body onload="init()">

    <form action="/app/adminlogin" id="myform" method="POST">

    <button onclick="myFunction()">Submit</button>

    <input type="text" id="fname" name="password">
    </form>

    <script>
        function myFunction() {
            //document.getElementById("action").value = "approve";
            document.getElementById("myForm").submit();
        }

    </script>

</body>
</html>