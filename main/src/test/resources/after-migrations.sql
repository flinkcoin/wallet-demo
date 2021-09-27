/* Fixing schema_version PK name... (flyway names it schema_version_pk, JPA/Hibernate names it schema_version_pkey) */
ALTER TABLE flyway_schema_history RENAME CONSTRAINT flyway_schema_history_pkey to flyway_schema_history_pk;
