package io.stouder.adonis.cli.json.routes

data class Route(
    val name: String,
    val pattern: String,
    val methods: List<String>,
    val handler: RouteHandler,
    val middleware: List<String>
)
