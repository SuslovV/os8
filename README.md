# Многомодульный java spring boot проект

## Список проектов:
- perfomance-statistics - Система учета времени выполнения методов в приложении с использованием Spring AOP
- http-logging-spring-boot-starter - Spring boot starter логирующий в базу данных postgres входящие и исходящие http запросы и ответы, время выполнения запросов
- user-service - Реализация аутентификации и авторизации с использованием Spring Security и JWT
- metrics-producer - Реализация kafka продюсера отправляющего сообщения о метриках работы приложения
- metrics-consumer - Реализация kafka консьюмера принимающего сообщения о метриках работы приложения и сохранящего их в базу данных postgres

## Собрать приложение

```
$ mvn clean package
```
