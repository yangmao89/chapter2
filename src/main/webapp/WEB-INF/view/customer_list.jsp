<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="BASE" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>客户管理--创建客户</title>
</head>

<body>
<h1>客户列表</h1>
<table>
    <thead>
    <tr>
        <td>客户名称</td>
        <td>联系人</td>
        <td>电话号码</td>
        <td>邮箱地址</td>
        <td>操作</td>
    </tr>
    </thead>

    <tbody>
    <c:forEach var="item" items="${customerList}">
        <tr>
            <td>${item.name}</td>
            <td>${item.contact}</td>
            <td>${item.telephone}</td>
            <td>${item.email}</td>
            <td>
                <a href="${BASE}/customer_edit?id=${item.id}">编辑</a>
                <a href="${BASE}/customer_delete?id=${item.id}">删除</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>

</html>