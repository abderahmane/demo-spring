# demo-spring

API REST de démonstration en **Spring Boot** (Maven). Sert de projet d'essai pour le pipeline opencode.

## Prérequis
- JDK 21 (ou la version déclarée dans `pom.xml`, propriété `java.version`)
- Maven 3.9+ (ou le wrapper `./mvnw` s'il est présent)

## Commandes
| Action | Commande |
|---|---|
| Lancer les tests | `mvn -B test` |
| Lancer l'application | `mvn spring-boot:run` (port 8080) |
| Construire le jar | `mvn -B package` |

## Structure
```
src/main/java/<package>/
  Application.java        point d'entrée
  controller/             contrôleurs REST (entrée/sortie HTTP uniquement)
  service/                logique métier
  repository/             accès aux données (en mémoire pour l'instant)
  dto/                    objets d'échange (records)
src/test/java/<package>/  tests, même arborescence que main
```

## Endpoints
Ajoutés au fil des tâches ; la liste à jour se lit dans les contrôleurs.

## Configuration
`src/main/resources/application.properties`. Aucun secret dans le dépôt : les valeurs sensibles passent par des variables d'environnement.

## Contribuer
Les modifications passent par merge request. Le pipeline lance `mvn -B test` ; elle doit être verte. Conventions détaillées dans `AGENTS.md`.
