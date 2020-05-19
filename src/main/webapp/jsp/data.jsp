<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<!DOCTYPE HTML>
<html>
<head>
    <title>Getting Started: Serving Web Content</title>
</head>
<body>
    <table id="dataTable" style="border:1px solid black;border-collapse:collapse;">
        <tr>
            <th style="border:1px solid black;"> Attēls </th>
            <th style="border:1px solid black;"> Apraksts </th>
            <th style="border:1px solid black;"> Vietas tips </th>
            <th style="border:1px solid black;"> Balsu skaits </th>
            <th style="border:1px solid black;"> Atrašanās vieta </th>
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
                <td style="border:1px solid black;">
                    <img src="/app/files/2${place.img}" style="width: 300px; height: 300px;" />
                </td>
                <td style="border:1px solid black;">
                    <div style="width: 500px; overflow: hidden; text-overflow: ellipsis;">
                        ${place.description}
                    </div>
                </td>
                <td style="border:1px solid black;">
                    <c:choose>
                        <c:when test="${place.placeType==1}">
                            <c:set var="type" value="Šaurība, nepārredzamība"/>
                        </c:when>
                        <c:when test="${place.placeType==2}">
                            <c:set var="type" value="Strauji pagriezieni"/>
                        </c:when>
                        <c:when test="${place.placeType==3}">
                            <c:set var="type" value="Segums (bedres, augstas apmales)"/>
                        </c:when>
                        <c:when test="${place.placeType==4}">
                            <c:set var="type" value="Cits"/>
                        </c:when>
                    </c:choose>
                </td>
                <td style="border:1px solid black;"> ${place.voteCount} </td>
                <td style="border:1px solid black;"> ${place.lat}, ${place.lon} </td>
              </tr>
        </c:forEach>
    </table>

</body>
</html>