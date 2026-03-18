<%--
  Created by IntelliJ IDEA.
  User: etulyon1
  Date: 03/03/2026
  Time: 14:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title> Subscribe </title>
    <link rel="stylesheet" href="resources/css/styles.css">

</head>
<body class="connect">

        <label for="name"> Nom </label>
        <input type="text" id="name">
        <label for="email"> Adresse mail </label>
        <input type="email" id="email">
        <label for="password"> Mot de passe </label>
        <input type="password" id="password">
        <label for="confirm"> Confirmation du mot de passe </label>
        <input type="password" id="confirm">
        <button type="button" onclick="subscribe()"> Enregistrer </button>
        <input type="button" value="Se connecter" onclick="window.location.href='login.jsp';" />

    <script src="resources/JS/scriptLogin.js"></script>
</body>
</html>
