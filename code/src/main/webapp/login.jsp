<%--
  Created by IntelliJ IDEA.
  User: etulyon1
  Date: 03/03/2026
  Time: 14:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Connexion</title>
    <link rel="stylesheet" href="resources/css/styles.css">
</head>
<body class="connect">
        <label for="email"> Adresse mail </label>
        <input type="email" id="email">
        <label for="password"> Mot de passe </label>
        <input type="password" id="password">
        <button type="button" onclick="login()"> Valider </button>
        <input type="button" value="S'inscrire" onclick="window.location.href='subscribe.jsp';" />
    <script src="resources/JS/scriptLogin.js"></script>
</body>
</html>
