<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <title>Spring Boot JSP</title>
</head>
<body>
    Welcome to Learning journey

    <form action="add">
        Enter 1st Number : <input type="text" name="num1"></input><br>
        Enter 2nd Number : <input type="text" name="num2"></input><br>
        <input type="submit">
    </form>

     <form action="addUser">
            Enter Age : <input type="text" name="age"></input><br>
            Enter Name : <input type="text" name="name"></input><br>
            <input type="submit">
        </form>

        User Back As ${name}
</body>
</html>