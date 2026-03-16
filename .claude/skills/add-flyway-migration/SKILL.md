---
description: Create a new Flyway database migration
argument-hint: [description]
allowed-tools: Read, Write, Bash(mvn flyway:*)
---

Create a new Flyway migration for: $ARGUMENTS

1. Check existing migrations in src/main/resources/db/migration/ to determine the next version number
2. Create the migration file following the naming convention V{N}__{description}.sql
3. Write the SQL DDL/DML
4. Run `mvn flyway:validate -pl backend` if Flyway Maven plugin is configured
