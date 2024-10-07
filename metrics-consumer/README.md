# Реализация kafka консьюмера принимающего из kafka сообщения о метриках приложения и сохранящего их в базу данных postgres


## Собрать приложение
```
$ mvn clean package
```

## Запустить приложение
```
$ java -jar ./target/metrics-consumer.jar
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