# Реализация kafka продюсера принимающего по REST сообщения о метриках работы приложения и отправляющего их в kafka

## Собрать приложение
```
$ mvn clean package
```

## Запустить приложение
```
$ java -jar ./target/metrics-producer.jar
```

## Интеграционные java-тесты
### Перед запуском java-тестов нужно запустить сервер kafka. Для этого в каталоге ./metrics-producer есть docker-compose.yaml файл.
```
$ cd ./metrics-producer
$ docker-compose up
```

## OpenAPI
```
http://localhost:8080/swagger-ui/index.html
```