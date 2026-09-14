# 🗝️ El Último Archivo de Clara — Backend

> Si estás buscando a Clara, estás empezando por el final.

**API REST** construida con **Spring Boot** para _El último archivo de Clara_, un escape room narrativo de investigación. Desarrollada en **Java 21** con **Spring Data JPA** y **PostgreSQL** en Docker, verificada siguiendo **TDD** con **JUnit 5, Mockito, Hamcrest**, con cobertura de tests medida con **JaCoCo**.

---

## 📑 Índice

- [Descripción](#-descripción)
- [Tecnologías](#-tecnologías)
- [Autora](#-autora)

---

## 📋 Descripción

API REST que da soporte al escape room narrativo _El último archivo de Clara_: autenticación con JWT, progreso de los jugadores y contenido narrativo del sistema del portátil de Clara. Sigue una arquitectura por capas y por funcionalidad (Controller → Service → Mapper → Repository → Entity), con DTOs, manejo centralizado de excepciones, principios SOLID y los patrones Builder y Strategy donde corresponde.

[Volver al índice](#-índice)

---

## 🛠️ Tecnologías

- **[Java 21](https://www.oracle.com/java/technologies/downloads/)** — Lenguaje de programación del proyecto
- **[Spring Boot](https://spring.io/projects/spring-boot)** — Framework para construir la API REST
- **[Spring Web](https://docs.spring.io/spring-framework/reference/web.html)** — Exposición de los endpoints HTTP
- **[Spring Data JPA](https://spring.io/projects/spring-data-jpa)** — Acceso a datos y mapeo objeto-relacional
- **[PostgreSQL](https://www.postgresql.org/)** — Base de datos relacional, en contenedor Docker
- **[Docker Compose](https://docs.docker.com/compose/)** — Levanta la base de datos local
- **[H2 Database](https://www.h2database.com/)** — Base de datos en memoria para tests
- **[Bean Validation](https://beanvalidation.org/)** — Validación de los datos de entrada
- **[Apache Maven](https://maven.apache.org/)** — Gestor de dependencias y construcción del proyecto
- **[JUnit 5](https://junit.org/junit5/)** — Framework de tests unitarios
- **[Mockito](https://site.mockito.org/)** — Mocks para los tests de servicio
- **[Hamcrest](https://hamcrest.org/JavaHamcrest/)** — Librería de matchers para aserciones legibles
- **[JaCoCo](https://www.jacoco.org/jacoco/)** — Medición de la cobertura de tests
- **[Git](https://git-scm.com/)** / **[GitHub](https://github.com/)** — Control de versiones y alojamiento del proyecto

---

## 👩‍💻 Autora

**[Jenny Sánchez Requejo](https://github.com/Jennydev-25)**

[Volver al índice](#-índice)
