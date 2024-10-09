# Система учета времени выполнения методов в приложении с использованием Spring AOP

Система асинхронно логирует время выполнения методов в базу данных с использованием Spring AOP.
Для этого нужно над методом указать аннотацию @TrackTime, для асинхронных методов - @TrackAsyncTime.
Данные о времени выполнении метода асинхронно записываются в таблицу perfomance_statistics.

## Собрать приложение
```
$ mvn clean package
```

## Запустить приложение
```
$ java -jar ./target/perfomance-statistics.jar
```

## OpenAPI
```
http://localhost:8080/swagger-ui/index.html
```