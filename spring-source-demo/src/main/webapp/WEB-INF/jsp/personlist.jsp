<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<h2>This is SpringMVC demo page</h2>
<c:forEach items="${persons}" var="person">
    <c:out value="${person.username}"/><br/>
    <c:out value="${person.age}"/><br/>
</c:forEach>