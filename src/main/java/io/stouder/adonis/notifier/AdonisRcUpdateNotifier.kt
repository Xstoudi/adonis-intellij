package io.stouder.adonis.notifier

import com.intellij.util.messages.Topic
import io.stouder.adonis.cli.json.ace.Command
import java.util.*

interface AdonisRcUpdateNotifier {
    companion object {
        val ADONIS_RC_UPDATE_TOPIC = Topic.create("Adonis RC Update", AdonisRcUpdateNotifier::class.java)
    }

    fun commands(commands: Map<String, Optional<Array<Command>>>?)
}
