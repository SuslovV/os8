# Реализация аутентификации и авторизации пользователей с использованием Spring Security и JWT токенов

- Создание нового пользователя и получение токена - /v1/auth/register
- Получение токена - /v1/auth/authenticate
- Обновление токена - /v1/auth/refresh-token
- Защищенные ресурсы - /v1/hello/user, /v1/hello/admin

## Собрать приложение
```
$ mvn clean package
```

## Запустить приложение
```
$ java -jar ./target/user-service.jar
```

## OpenAPI
```
http://localhost:8080/swagger-ui/index.html
```