# Реализация kafka консьюмера принимающего из очереди сообщения о метриках приложения и сохранящего их в базу данных postgres


## Собрать приложение
```
mvn clean package
```

## Запустить приложение
```
java -jar ./target/metrics-consumer.jar
```

## OpenAPI
```
http://localhost:8080/swagger-ui/index.html
```