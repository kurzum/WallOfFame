<%@page contentType="text/html;charset=utf-8"%>
<%@include file="../taglibs.jspf"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<tags:head title="Validation Form" />

<body id="validate">

    <h1>WebId Validator</h1>

    <form:form commandName="validation">
        <textarea id="webid" name="webid" rows="30" cols="70">Paste your WebId in here.</textarea>
        <form:errors path="webid" cssStyle="color:red"/>
        <br/>
        <input type="submit" value="Validate" />
    </form:form>



    <br>

    <input type="button" onclick="location.href='/static/exhibit/walloffame.html'" value="Wall of Fame">
    <input type="button" onclick="location.href='/'" value="Back">
</body>
</html>