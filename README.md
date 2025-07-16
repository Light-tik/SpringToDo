<h1>Практическое задание №1: Spring Boot TODO приложение</h1>

<p>Создайте простое <strong>RESTful API</strong> веб-сервиса с использованием <strong>Spring Boot</strong>. Нужно реализовать приложение для управления списком дел (TODO).</p>

<hr>

<h2>Требования к приложению</h2>

<ul>
  <li><strong>База данных:</strong> Использовать <code>PostgreSQL</code>.</li>
  <li><strong>Миграции:</strong> Настроить миграции с помощью <code>Liquibase</code> или <code>Flyway</code>. Миграции должны автоматически применяться при старте приложения и в тестах.</li>
  <li><strong>Работа с БД:</strong> Не использовать <code>Hibernate/JPA</code>, вместо этого использовать <code>JdbcTemplate</code>.</li>
  <li><strong>Тестирование:</strong>
    <ul>
      <li>Покрыть приложение юнит и интеграционными тестами.</li>
      <li>Интеграционные тесты для всех контроллеров через <code>JSON Assert</code>.</li>
      <li>Настроить тестовую БД через <code>Testcontainers</code>.</li>
      <li>Использовать <code>@DataJpaTest</code> для грязного контекста БД.</li>
      <li>Применять <code>@Sql</code> для подготовки данных.</li>
      <li>Все тесты снабдить <code>@DisplayName</code> для читаемости логов.</li>
    </ul>
  </li>
  <li><strong>Кеширование:</strong> Использовать <code>Spring Cache</code> и <code>Redis</code>.</li>
  <li><strong>Документация:</strong> Описать API с помощью <code>OpenAPI/Swagger</code> и вынести описание эндпоинтов в отдельный интерфейс.</li>
  <li><strong>Валидация:</strong> Обеспечить валидацию всех входящих <code>DTO</code>.</li>
  <li><strong>Пагинация:</strong> Реализовать <strong>limit-offset</strong> пагинацию для списка дел.</li>
  <li><strong>Обработка ошибок:</strong> 
    <ul>
      <li>Добавить глобальный <code>ExceptionHandler</code> для обработки ошибок.</li>
      <li>Написать тесты на обработку ошибок.</li>
    </ul>
  </li>
  <li><strong>Ответы API:</strong> Не использовать <code>ResponseEntity</code> напрямую.</li>
  <li><strong>Маппинг:</strong> Использовать <code>MapStruct</code> для преобразования Entity в DTO.</li>
  <li><strong>Мониторинг:</strong> 
    <ul>
      <li>Подключить <code>Spring Boot Actuator</code>.</li>
      <li>Открыть доступ к эндпоинтам `/actuator/health`, `/actuator/metrics`, `/actuator/info`.</li>
      <li>Создать минимум одну <strong>кастомную метрику</strong> с использованием <code>Micrometer</code> (например, количество выполненных задач).</li>
    </ul>
  </li>
</ul>

<hr>

<h2>Как выполнить задание</h2>

<ol>
  <li>Сделать <strong>fork</strong> данного проекта.</li>
  <li>Создать ветку <code>dev</code> в своём репозитории и работать в ней.</li>
  <li>После завершения работы открыть <strong>Pull Request</strong> в <code>master</code> своего репозитория.</li>
  <li>Назначить ментора ревьювером на PR.</li>
  <li>Отправить ссылку на PR в Telegram-чат ментору.</li>
</ol>

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

<hr>

<h2>Требования к структуре</h2>

<ul>
  <li>Код должен быть чистым и структурированным.</li>
  <li>Пакеты следует логически организовать: <code>controller</code>, <code>service</code>, <code>repository</code>, <code>dto</code>, <code>config</code>, <code>exception</code> и т.д.</li>
  <li>Бизнес-ошибки должны обрабатываться централизованно через единый ExceptionHandler.</li>
</ul>
