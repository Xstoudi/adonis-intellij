package io.stouder.adonis.service.impl

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.ScriptRunnerUtil
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import io.stouder.adonis.cli.json.ace.Command
import io.stouder.adonis.cli.json.routes.RouteHandler
import io.stouder.adonis.service.AdonisAceService
import io.stouder.adonis.service.AdonisAppService
import java.io.File
import java.util.*
import java.util.function.Consumer

class AdonisAceServiceImpl(private val project: Project) : AdonisAceService {

    private val log = Logger.getInstance(AdonisAceService::class.java)

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(RouteHandler::class.java, RouteHandler.Deserializer())
        .registerTypeAdapter(Command::class.java, Command.Deserializer())
        .create()

    private fun <T> runCommandOnRoots(params: List<String>, responseType: Class<T>): Map<String, Optional<T & Any>> {
        return AdonisAppService
            .getInstance(this.project)
            .getAdonisRoots()
            .associateWith { basePath: String ->
                try {
                    val commandLine = GeneralCommandLine()
                        .withExePath("node")
                        .withWorkDirectory(File(basePath))
                        .withParameters(params)
                    val jsonOutput = ScriptRunnerUtil.getProcessOutput(commandLine)
                    Optional.ofNullable(gson.fromJson(jsonOutput, responseType))
                } catch (e: ExecutionException) {
                    Optional.empty()
                }
            }
    }

    override fun <T> runAceGetCommandOnEveryRoots(
        progressTitle: String,
        parameters: List<String>,
        responseType: Class<T>
    ): Map<String, Optional<T & Any>> {
        val params = ArrayList(parameters)
        params.add(0, "ace")

        ProgressManager.getInstance().runProcessWithProgressSynchronously(
            {
                runCommandOnRoots(params, responseType)
            },
            progressTitle,
            true,
            this.project
        )
        return runCommandOnRoots(params, responseType)
    }

    override fun <T> runAceGetCommandAsyncOnEveryRoots(
        callback: Consumer<Map<String, Optional<T & Any>>>,
        parameters: List<String>,
        responseType: Class<T>
    ) {
        val params = ArrayList(parameters)
        params.add(0, "ace")

        ApplicationManager.getApplication().executeOnPooledThread {
            val result = runCommandOnRoots(params, responseType)
            callback.accept(result)
        }
    }

    override fun execAceCommand(
        progressTitle: String,
        parameters: List<String>,
        basePath: String
    ): Boolean {
        val params = ArrayList(parameters)
        params.add(0, "ace")
        val commandLine = GeneralCommandLine()
            .withExePath("node")
            .withWorkDirectory(File(basePath))
            .withParameters(params)
        log.info("Running command with progress bar: ${commandLine.commandLineString}")
        return ProgressManager.getInstance().runProcessWithProgressSynchronously(
            {
                try {
                    ScriptRunnerUtil.getProcessOutput(commandLine)
                    true
                } catch (e: ExecutionException) {
                    false
                }
            },
            progressTitle,
            true,
            this.project
        )
    }

    override fun fetchCommands(callback: Consumer<Map<String, Optional<Array<Command>>>>) {
        val adonisAceService = AdonisAceService.getInstance(this.project)
        adonisAceService.runAceGetCommandAsyncOnEveryRoots(callback, listOf("list", "--json"), Array<Command>::class.java)
    }
}