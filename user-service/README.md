# Реализация аутентификации и авторизации с использованием Spring Security и JWT

- Создание нового пользователя и получение токена - /v1/auth/register
- Аутентификация и получение токена - /v1/auth/authenticate
- Обновление токена - /v1/auth/refreshtoken
- Защищенный ресурс - /v1/admin/health

## Собрать приложение
```
mvn clean package
```

## Запустить приложение
```
java -jar ./target/user-service.jar
```

## OpenAPI
```
http://localhost:8080/swagger-ui/index.html
```