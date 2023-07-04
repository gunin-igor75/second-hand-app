### Платформа по перепродаже вещей second_hand

Реализация backend части платформы
- Авторизация и аутентификация пользователей.
- Распределение ролей между пользователями: пользователь и администратор*.
- CRUD для объявлений на сайте: администратор может удалять или редактировать все объявления, а пользователи— только свои.
- Под каждым объявлением пользователи могут оставлять отзывы.
- В заголовке сайта можно осуществлять поиск объявлений по названию.
- Показывать и сохранять картинки объявлений*.

Для запуска проекта предварительно в корне проекта содать файл .env  для инициализации 
переменных, указанных в application.properties и docker-compose.yaml
Запуск проекта из среды docker:
``` 
docker compose up -d
```
Запуск jar файла 
```
java -jar ads-0.0.1-SNAPSHOT.jar
```

В проекте применены следующие технолонии
- java 17
- spring boot
- spring mvc
- spring data jpa
- spring security
- postgresql 15.3
- liquibase

Проект покрыт тестами на 88%

Разработчики проекта
- Гунин Игорь
