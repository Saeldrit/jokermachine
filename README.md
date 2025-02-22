# Инструкция по запуску Spring Boot приложения JokerMachine

В этом руководстве описаны шаги для запуска Spring Boot приложения **JokerMachine**, включая получение токена OpenRouter, настройку локальных параметров в формате YAML, генерацию кода JOOQ и запуск приложения.

---

## 1. Получение токена OpenRouter

1. Перейдите на сайт [OpenRouter](https://openrouter.ai/).
2. Зарегистрируйтесь или войдите в аккаунт.
3. Перейдите в раздел **API Keys** в личном кабинете.
4. Сгенерируйте новый API-токен (если его нет) и скопируйте его.

---

## 2. Настройка локальных параметров приложения

Для локальной разработки создайте файл `application-local.yml` в директории `src/main/resources`. Этот файл будет содержать настройки, специфичные для вашего окружения, в формате YAML.

### Пример содержимого `application-local.yml`:
```yaml
# OpenRouter API
openrouter:
  api:
    key: ВАШ_ТОКЕН_OPENROUTER
    url: https://openrouter.ai/api/v1/chat/completions

# Настройки базы данных
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/jokermachine
    username: ВАШ_ПОЛЬЗОВАТЕЛЬ
    password: ВАШ_ПАРОЛЬ
  jooq:
    sql-dialect: PostgreSQL

## Настройка и использование JOOQ

JOOQ (Java Object Oriented Querying) — это библиотека для типобезопасной работы с SQL в Java.
В этом разделе описана настройка JOOQ для генерации кода на основе вашей базы данных.

### Настройка `pom.xml`

Для генерации кода JOOQ необходимо добавить конфигурацию плагина в ваш `pom.xml`. Убедитесь, что у вас есть следующий блок в разделе `<build>`:

<build>
    <plugins>
        <plugin>
            <groupId>org.jooq</groupId>
            <artifactId>jooq-codegen-maven</artifactId>
            <version>3.19.6</version> <!-- Используйте актуальную версию -->
            <executions>
                <execution>
                    <id>generate-jooq</id>
                    <phase>generate-sources</phase>
                    <goals>
                        <goal>generate</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <jdbc>
                    <driver>org.postgresql.Driver</driver>
                    <url>${spring.datasource.url}</url>
                    <user>${spring.datasource.username}</user>
                    <password>${spring.datasource.password}</password>
                </jdbc>
                <generator>
                    <database>
                        <name>org.jooq.meta.postgres.PostgresDatabase</name>
                        <includes>.*</includes>
                        <inputSchema>public</inputSchema>
                    </database>
                    <target>
                        <package>com.jokermachine.generated</package>
                        <directory>src/main/java/generated</directory>
                    </target>
                </generator>
            </configuration>
        </plugin>
    </plugins>
</build>
```
## Запуск генерации кода JOOQ

После настройки конфигурации JOOQ в `pom.xml` необходимо сгенерировать код на основе схемы вашей базы данных. Этот процесс автоматически создаст Java-классы, которые соответствуют таблицам и другим объектам базы данных.

### Команда для генерации кода

Чтобы запустить генерацию кода JOOQ, выполните следующую команду в терминале:

mvn clean compile
