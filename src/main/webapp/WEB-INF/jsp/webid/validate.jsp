<%@page contentType="text/html;charset=utf-8" %>
<%@include file="../taglibs.jspf" %>

<html>
<tags:head title="Validation Form"/>

<body id="validate">

<h1>WebId Validator</h1>

<form:form method="post">
    <textarea id="webid" name="webid" rows="30" cols="70">Paste your WebId in here.</textarea>
    <br/>
    <input type="submit" value="Validate"/>
</form:form>


<br>

<input type="button" onclick="location.href='/static/exhibit/walloffame.html'" value="Wall of Fame">
<input type="button" onclick="location.href='/'" value="Back">
</body>
</html>