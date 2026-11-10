# AGENTS.md — demo-spring

Application Spring Boot (Maven, Java 21). API REST ; les données restent en mémoire tant qu'aucune base n'est demandée.

## Commandes
- Tests : `mvn -B test` (à lancer avant de conclure ; ils doivent passer).
- Compilation seule : `mvn -B -q compile`.
- Pas de lancement long de l'application pour vérifier : les tests MockMvc suffisent.

## Architecture
Quatre couches, une classe par responsabilité :
- `controller/` : routes, codes HTTP, validation des entrées. Aucune logique métier.
- `service/` : logique métier. Ne connaît pas HTTP.
- `repository/` : stockage. En mémoire : structures concurrentes (`ConcurrentHashMap`) et identifiants via `AtomicLong`.
- `dto/` : `record` pour les entrées et sorties JSON. Les entités internes ne sortent pas par l'API.
Le package de base est celui de la classe `@SpringBootApplication` ; garde-le.

## Conventions
- Injection par constructeur (pas de `@Autowired` sur les champs).
- Validation avec `jakarta.validation` (`@Valid`, `@NotBlank`, `@Size`…) sur les DTO d'entrée.
- Erreurs : un `@RestControllerAdvice` unique renvoie `{"error": "<raison courte>"}` avec le bon code (400, 404…). Jamais de stack trace ni de nom de classe dans une réponse.
- Réponses JSON ; codes explicites (201 à la création, 204 à la suppression, 404 si inconnu).
- Pas de `System.out` ni `printStackTrace` : utilise SLF4J, sans données personnelles dans les logs.
- Pas de `@SuppressWarnings` ni de code mort.

## Tests
- JUnit 5 + AssertJ. Contrôleurs : `@WebMvcTest` + MockMvc. Services : test unitaire sans Spring.
- Chaque endpoint a un test nominal et un test par cas d'erreur.
- Ajoute des tests NOUVEAUX ; ne modifie pas et ne désactive pas un test existant (`@Disabled` interdit).

## Dépendances
- N'ajoute une dépendance que si la tâche le demande, en version explicite et justifiée dans le résumé.
- Ne touche pas à la version de Spring Boot ni à `java.version` sans demande.

## Sécurité
- Aucun secret, jeton ni mot de passe dans le code, les tests ou `application.properties`.
- Requêtes SQL paramétrées uniquement, jamais de concaténation de chaînes.
- Des contrôles automatiques (Semgrep, Trivy) tournent avant le commit : corrige la cause d'un constat ; n'ajoute ni `// nosemgrep` ni fichier d'exclusion.

## Périmètre
Fais seulement ce que demande la tâche, avec les changements les plus petits possibles. Ne touche pas à `AGENTS.md`, à `.gitignore`, à `pom.xml` (sauf dépendance demandée) ni à la CI.
