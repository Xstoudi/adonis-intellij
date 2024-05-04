package io.stouder.adonis.tool_window

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import io.stouder.adonis.AdonisBundle
import io.stouder.adonis.notifier.AdonisRcUpdateNotifier
import io.stouder.adonis.notifier.AdonisRouteUpdateNotifier
import io.stouder.adonis.service.AdonisAppService
import io.stouder.adonis.tool_window.content.MakeToolWindowContent
import io.stouder.adonis.tool_window.content.RoutesToolWindowContent

class AdonisToolWindow : ToolWindowFactory {

    override suspend fun isApplicableAsync(project: Project): Boolean {
        return AdonisAppService.getInstance(project).isAdonisProject()
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val routesToolWindowContent = RoutesToolWindowContent(project)
        val makeToolWindowContent = MakeToolWindowContent(project)
        val routesContent = ContentFactory.getInstance().createContent(routesToolWindowContent.rootPanel, AdonisBundle.message("adonis.tool_window.routes"), false)
        val makeContent = ContentFactory.getInstance().createContent(makeToolWindowContent.getRootPanel(), AdonisBundle.message("adonis.tool_window.make"), false)
        toolWindow.contentManager.addContent(routesContent)
        toolWindow.contentManager.addContent(makeContent)

        project.messageBus.connect().subscribe(AdonisRouteUpdateNotifier.ADONIS_ROUTES_UPDATE_TOPIC, routesToolWindowContent)
        project.messageBus.connect().subscribe(AdonisRcUpdateNotifier.ADONIS_RC_UPDATE_TOPIC, makeToolWindowContent)
    }

    override fun init(toolWindow: ToolWindow) {
        super<ToolWindowFactory>.init(toolWindow)
    }

    override fun shouldBeAvailable(project: Project): Boolean {
        return super<ToolWindowFactory>.shouldBeAvailable(project)
    }
}