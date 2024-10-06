# Реализация kafka продюсера принимающего по REST и отправляющего в очередь сообщения о метриках работы приложения


## Собрать приложение
```
mvn clean package
```

## Запустить приложение
```
java -jar ./target/metrics-producer.jar
```

## OpenAPI
```
http://localhost:8080/swagger-ui/index.html
```