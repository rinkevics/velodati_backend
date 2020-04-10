<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Getting Started: Serving Web Content</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body onload="init()">

<div id="`table" style="overflow-y: scroll; height:600px;">
    <table id="dataTable" >
        <thead>
        <tr>
            <th> Title </th>
            <th> Author </th>
        </tr>
        </thead>
        <tbody>
        <tr th:if="${places.empty}">
            <td colspan="2"> No Books Available </td>
        </tr>

        <tr th:each="place : ${places}" >
            <td><input type="checkbox" name="places" value="1" ></td>
            <td><img th:src="${'/app/files/' + place.img}" height="300px"/></td>
            <td><span th:text="${place.description}"> </span></td>
        </tr>
        </tbody>
    </table>
</div>

<script>

    function init(){
        var table = document.getElementById('dataTable');
        for (var i=0;i < table.rows.length;i++){
            eddEvent(able.rows[i]);
        }
    }

    function addEvent(var parent) {
        parent.addEventListener('click',function(event){

            event.stopPropagation();
            event.preventDefault();

            parent.style.backgroundColor='#BCD4EC';
        },true);
    }

</script>

</body>
</html>