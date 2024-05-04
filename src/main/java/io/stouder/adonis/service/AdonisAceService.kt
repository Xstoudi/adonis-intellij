package io.stouder.adonis.service

import com.intellij.openapi.project.Project
import io.stouder.adonis.cli.json.ace.Command

import java.util.Optional
import java.util.function.Consumer

interface AdonisAceService {

    fun <T> runAceGetCommandOnEveryRoots(progressTitle: String, parameters: List<String>, responseType: Class<T>): Map<String, Optional<T>>
    fun <T> runAceGetCommandAsyncOnEveryRoots(callback: Consumer<Map<String, Optional<T>>>, parameters: List<String>, responseType: Class<T>)
    fun execAceCommand(progressTitle: String, parameters: List<String>, basePath: String): Boolean
    fun fetchCommands(callback: Consumer<Map<String, Optional<Array<Command>>>?>)

    companion object {
        fun getInstance(project: Project): AdonisAceService {
            return project.getService(AdonisAceService::class.java)
        }
    }
}