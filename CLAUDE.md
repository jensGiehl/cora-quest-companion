# Claude Code Guidelines

## Code Style

- Use **object-oriented programming** principles (encapsulation, single responsibility, etc.)
- Follow **Clean Code** practices
- Use **English** for all names, comments, and documentation
- Use **descriptive method and variable names** — the name should explain the intent
- Write **minimal comments** — only when the *why* is non-obvious; never explain what the code does

## Java 25

- Prefer modern Java 25 features over boilerplate where they fit the use case
- Use **Records** for immutable data carriers instead of POJOs with getters
- Use pattern matching, sealed classes, and other Java 25 language features where appropriate

## Lombok

- Use Lombok annotations instead of writing boilerplate manually
- Prefer `@RequiredArgsConstructor` for constructors; use `@AllArgsConstructor` / `@NoArgsConstructor` only when required
- Use `@Slf4j` for logging — never instantiate a logger manually

## Database

- Use **Spring Data JDBC** for database access — not JPA/Hibernate
- Use **Flyway** for all schema changes — never modify the database schema manually
- The database is **H2** (in-memory for development and tests)

## Frontend

- Use **Thymeleaf** for server-side HTML templating
- Include static assets (Bootstrap, etc.) via **WebJars** — never download or commit static files manually
- Use **Bootstrap** for layout and components
- All pages must be **mobile-optimized**: use Bootstrap's responsive grid, include the viewport meta tag, and avoid fixed pixel widths

## Internationalization (i18n)

- All visible frontend text must be **externalized** into Thymeleaf message files — no hardcoded strings in templates
- Always maintain **both** `messages.properties` (English, the default) and `messages_de.properties` (German)
- Use Spring's `MessageSource` with browser language detection (`AcceptHeaderLocaleResolver`) — no manual language switching required
- Reference messages in templates with `#{key}`

## Testing

- Write a **test for every piece of code** added or changed
- Tests live in `src/test/java` mirroring the production package structure

## Documentation

- All `.md` files are written in **English**
- After every change, check whether `README.md` or any other `.md` file needs to be updated to reflect the change
- `README.md` links to all other documentation files — keep those links valid

## After Every Change

Run the following commands and ensure both pass before considering a task done:

```bash
./mvnw compile
./mvnw test
```
