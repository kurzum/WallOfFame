<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Validation Result</title>
</head>
<body id="result">
<input type="button" onclick="location.href='/validate'" value="Validate WebId">

<input type="button" onclick="location.href='/static/exhibit/walloffame.html'" value="Wall of Fame">
</br>
</br>
Your WebId has the following error: </br>
${result}
</body>
</html>
