package io.stouder.adonis.module;

enum class AdonisDatabaseChoice(
    val label: String,
    val value: String
) {
    SQLITE("SQLite", "sqlite"),
    POSTGRES("PostgreSQL", "postgres"),
    MYSQL("MySQL", "mysql"),
    MSSQL("Microsoft SQL Server", "mssql");
}
