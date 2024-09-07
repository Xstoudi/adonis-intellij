package io.stouder.adonis.notifier

import com.intellij.util.messages.Topic
import io.stouder.adonis.cli.json.routes.RouteDomain
import java.util.*

interface AdonisRouteUpdateNotifier {
    companion object {
        val ADONIS_ROUTES_UPDATE_TOPIC = Topic.create("Adonis Routes Update", AdonisRouteUpdateNotifier::class.java)
    }

    fun routes(routes: Map<String, Optional<Array<RouteDomain>>>?)
}
