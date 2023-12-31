package io.stouder.adonis.notifier;

import com.intellij.util.messages.Topic;
import io.stouder.adonis.cli.json.ace.Command;

import java.util.Map;
import java.util.Optional;

public interface AdonisRcUpdateNotifier {
    Topic<AdonisRcUpdateNotifier> ADONIS_RC_UPDATE_TOPIC = Topic.create("Adonis RC Update", AdonisRcUpdateNotifier.class);

    void commands(Map<String, Optional<Command[]>> commands);
}
