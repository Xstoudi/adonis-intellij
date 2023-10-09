package io.stouder.adonis.notifier;

import com.intellij.util.messages.Topic;
import io.stouder.adonis.cli.json.routes.RouteDomain;

public interface AdonisRouteUpdateNotifier {
    Topic<AdonisRouteUpdateNotifier> ADONIS_ROUTES_UPDATE_TOPIC = Topic.create("Adonis Routes Update", AdonisRouteUpdateNotifier.class);

    void routes(RouteDomain[] routes);
}
