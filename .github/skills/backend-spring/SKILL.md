---
name: backend-spring
summary: 'Expert backend developer workflow for Spring Boot applications.'
description: |
  Use this skill when you need a step-by-step Spring Boot backend engineering workflow in this project.
  Covers architecture, security, persistence, REST API design, validation, testing, and production readiness.
  Includes decision points, acceptance checks, and quality gate criteria.
---

# Skill: Spring Boot Backend Expert

## Goal
Guide the agent to deliver maintainable, secure, testable Spring Boot backend changes that align with best practices.

## Workflow steps
1. Understand context
   - inspect existing code layout, packages, and feature files
   - locate relevant controllers/services/repository/config classes
   - confirm requirements from user story/issue text

2. Design API and domain model
   - define DTOs and entities, relationships, validation constraints
   - choose HTTP methods and status codes
   - define security roles and access rules in `WebSecurityConfig`

3. Implement
   - add/modify `@RestController`, `@Service`, and `@Repository`
   - use Spring Data JPA for persistence
   - use `@Transactional` only where needed
   - keep business logic in service layer

4. Add validation and error handling
   - use `@Valid` and JSR-380 annotations
   - define global exception handler (`@ControllerAdvice`) if missing
   - return clear error payloads

5. Add tests
   - unit tests for service and utility logic (JUnit 5 + Mockito)
   - integration tests using `@SpringBootTest` and `@AutoConfigureMockMvc`
   - security tests to cover authorized/unauthorized flows

6. Verify and refine
   - run `./mvnw test` and ensure all pass
   - run static checks (checkstyle, findbugs if configured)
   - sanity-check behavior with curl or Swagger if available

## Decision points
- `PUT` vs `PATCH` vs `POST`: use semantics and idempotency
- `@ManyToOne`/`@OneToMany` fetch strategy: prefer `LAZY` unless exceptional
- JWT vs session auth: follow existing `JwtUtil` and `JwtAuthFilter`

## Quality criteria
- no hard-coded secrets in source
- queries not vulnerable to N+1 or injection issues
- API endpoint has clear OpenAPI/Swagger docs if generated
- transaction boundaries are explicit and minimal
- responses use standard HTTP codes
- warnings/failures in tests are addressed

## Usage examples
- "Implement endpoint to create referral program with validation and persistence."
- "Refactor AuthService and add integration tests for JWT login flow."
- "Add role-based authorization to existing consumer endpoints."

## Notes
- For small single-file changes, still prefer minimal, focused diff.
- For schema/design changes, document migration strategy before altering DB schema.
