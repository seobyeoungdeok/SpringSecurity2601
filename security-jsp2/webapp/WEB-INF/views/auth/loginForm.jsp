<%@ page language="java"	contentType="text/html;charset=UTF-8"	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>로그인 페이지</title>
    <script>
        const login = (event) => {
            alert("로그인 호출");
            document.getElementById("f_login").submit();
        }
        const joinForm = (event) => {
            location.href = "joinForm";
        }
    </script>
</head>
<body>
<h1>로그인 페이지{WEB-INF}</h1>
<form id="f_login" action="/loginProcess" method="post">
    <input type="text" name="username" placeholder="Username" /><br>
    <input type="password" name="password" placeholder="Password" /><br>
    <div style="padding-bottom: 4px;"></div>
    <button type="button" onclick="login()">로그인</button>
    <button type="button" onclick="joinForm()">회원가입</button>
</form>
</body>
</html>