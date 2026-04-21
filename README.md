<h1> Spring Boot TODO приложение</h1>

<p>Это простое <strong>RESTful API</strong> веб-сервиса с использованием <strong>Spring Boot</strong>. Приложение для управления списком дел (TODO).</p>

<hr>

<h2>Основные детали приложения</h2>

<ul>
  <li><strong>База данных:</strong><code>PostgreSQL</code>.</li>
  <li><strong>Миграции:</strong> Настроены миграции с помощью <code>Liquibase</code> или <code>Flyway</code>. Миграции автоматически применяются при старте приложения и в тестах.</li>
  <li><strong>Работа с БД:</strong> Не используется <code>Hibernate/JPA</code>, вместо этого - <code>JdbcTemplate</code>.</li>
  <li><strong>Тестирование:</strong>
    <ul>
      <li>Приложение покрыто юнит и интеграционными тестами.</li>
      <li>Интеграционные тесты для всех контроллеров через <code>JSON Assert</code>.</li>
      <li>Тестовая БД через <code>Testcontainers</code>.</li>
      <li>Используется <code>@DataJpaTest</code> для грязного контекста БД.</li>
      <li><code>@Sql</code> для подготовки данных.</li>
      <li>Все тесты снабжены <code>@DisplayName</code> для читаемости логов.</li>
    </ul>
  </li>
  <li><strong>Кеширование:</strong> Используется <code>Spring Cache</code> и <code>Redis</code>.</li>
  <li><strong>Документация:</strong> Описано API с помощью <code>OpenAPI/Swagger</code> и вынесено описание эндпоинтов в отдельный интерфейс.</li>
  <li><strong>Валидация:</strong> Обеспечена валидация всех входящих <code>DTO</code>.</li>
  <li><strong>Пагинация:</strong> Реализована <strong>limit-offset</strong> пагинация для списка дел.</li>
  <li><strong>Обработка ошибок:</strong> 
    <ul>
      <li>Добавлен глобальный <code>ExceptionHandler</code> для обработки ошибок.</li>
      <li>Написаны тесты на обработку ошибок.</li>
    </ul>
  </li>
  <li><strong>Ответы API:</strong> Не использовано <code>ResponseEntity</code> напрямую.</li>
  <li><strong>Маппинг:</strong> Использован <code>MapStruct</code> для преобразования Entity в DTO.</li>
  <li><strong>Мониторинг:</strong> 
    <ul>
      <li>Подключён <code>Spring Boot Actuator</code>.</li>
      <li>Открыт доступ к эндпоинтам `/actuator/health`, `/actuator/metrics`, `/actuator/info`.</li>
      <li>Создана <strong>кастомная метрика</strong> с использованием <code>Micrometer</code> (например, количество выполненных задач).</li>
    </ul>
  </li>
</ul>

<hr>

<hr>

<h2>Технические детали</h2>

<table>
  <thead>
    <tr>
      <th>Технология</th>
      <th>Описание</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Java</td>
      <td>17+</td>
    </tr>
    <tr>
      <td>Spring Boot</td>
      <td>3.x</td>
    </tr>
    <tr>
      <td>PostgreSQL</td>
      <td>Latest</td>
    </tr>
    <tr>
      <td>Liquibase/Flyway</td>
      <td>Latest</td>
    </tr>
    <tr>
      <td>Redis</td>
      <td>Latest</td>
    </tr>
    <tr>
      <td>Testcontainers</td>
      <td>Latest</td>
    </tr>
    <tr>
      <td>MapStruct</td>
      <td>Latest</td>
    </tr>
    <tr>
      <td>Swagger / OpenAPI</td>
      <td>Latest</td>
    </tr>
    <tr>
      <td>Spring Actuator</td>
      <td>Latest</td>
    </tr>
  </tbody>
</table>

