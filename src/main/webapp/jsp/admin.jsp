<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
    <title>Getting Started: Serving Web Content</title>
</head>
<body>

    <form action="/app/admin/${action}" id="myform" method="POST">

    <a href="/app/admin/all">All data</a>
    <a href="/app/admin/new">New items</a>

    <button onclick="myFunction()">Submit</button>
    <button onclick="approveAll()">Approve all</button>

    <div id="table" style="overflow-y: scroll; width: 100%; height: 700px;">
        <table id="dataTable" style="border:1px solid black;border-collapse:collapse;">
            <tr>
                <th style="border:1px solid black;"> Approve </th>
                <th style="border:1px solid black;"> Block </th>
                <th style="border:1px solid black;"> Img </th>
                <th style="border:1px solid black;"> Description </th>
                <th style="border:1px solid black;"> Place Type </th>
                <th style="border:1px solid black;"> Lat Lon </th>
                <th style="border:1px solid black;"> Reply From Town Hall </th>
                <th style="border:1px solid black;"> Reply From Town Hall State </th>
                <th style="border:1px solid black;"> Created at </th>
                <th style="border:1px solid black;"> Ip Address </th>
                <th style="border:1px solid black;"> Email </th>
                <th style="border:1px solid black;"> Recieve email </th>
                <th style="border:1px solid black;"> Admin reviewed </th>
                <th style="border:1px solid black;"> Blocked </th>
            </tr>

            <c:set var = "dark" value = "0"/>

            <c:forEach items="${places}" var="place">
                <c:set var = "dark" value = "${ (dark + 1) % 2 }"/>
                <c:choose>
                    <c:when test="${dark==1}">
                        <c:set var = "style" value = "background-color: lightgray; "/>

                    </c:when>
                    <c:otherwise>
                        <c:set var = "style" value = ""/>

                    </c:otherwise>
                </c:choose>

                <tr style="${style}">
                    <td style="border:1px solid black;"> ${place.id} <input type ="checkbox" name="approve" value="${place.id}"/> </td>
                    <td style="border:1px solid black;"> <input type ="checkbox" name="block" value="${place.id}"/> </td>
                    <td style="border:1px solid black;"> <img src="/app/files/2${place.img}" /></td>
                    <td style="border:1px solid black;">
                        <div style="width: 300px; overflow: hidden; text-overflow: ellipsis;">
                            ${place.description}
                        </div>
                    </td>
                    <td style="border:1px solid black;"> ${place.placeType.label} </td>
                    <td style="border:1px solid black;"> ${place.lat}, ${place.lon} </td>
                    <td style="border:1px solid black;"> ${place.replyFromTownHall} </td>
                    <td style="border:1px solid black;"> ${place.townHallReplyState.label} </td>
                    <td style="border:1px solid black;"> ${place.createdDateTime} </td>
                    <td style="border:1px solid black;"> ${place.ipAddress} </td>
                    <td style="border:1px solid black;"> ${place.email} </td>
                    <td style="border:1px solid black;"> ${place.receiveEmails} </td>
                    <td style="border:1px solid black;"> ${place.adminReviewed} </td>
                    <td style="border:1px solid black;"> ${place.blocked} </td>
                </tr>
            </c:forEach>
        </table>


    </div>

    </form>

    <script>
        function myFunction() {
            document.getElementById("myForm").submit();
        }

        function approveAll() {
            document.getElementsByName("approve").forEach(checkbox => checkbox.checked = true);
            document.getElementById("myForm").submit();
        }

    </script>

</body>
</html>