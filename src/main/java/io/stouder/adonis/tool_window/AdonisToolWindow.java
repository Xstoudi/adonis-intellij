package io.stouder.adonis.tool_window;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import io.stouder.adonis.AdonisBundle;
import io.stouder.adonis.notifier.AdonisRcUpdateNotifier;
import io.stouder.adonis.notifier.AdonisRouteUpdateNotifier;
import io.stouder.adonis.service.AdonisAppService;
import io.stouder.adonis.tool_window.content.MakeToolWindowContent;
import io.stouder.adonis.tool_window.content.RoutesToolWindowContent;
import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AdonisToolWindow implements ToolWindowFactory {

    @Override
    public Object isApplicableAsync(@NotNull Project project, @NotNull Continuation<? super Boolean> $completion) {
        return AdonisAppService.getInstance(project).isAdonisProject();
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        RoutesToolWindowContent routesToolWindowContent = new RoutesToolWindowContent(project);
        MakeToolWindowContent makeToolWindowContent = new MakeToolWindowContent(project);
        Content routesContent = ContentFactory.getInstance().createContent(routesToolWindowContent.getRootPanel(), AdonisBundle.message("adonis.tool_window.routes"), false);
        Content makeContent = ContentFactory.getInstance().createContent(makeToolWindowContent.getRootPanel(), AdonisBundle.message("adonis.tool_window.make"), false);
        toolWindow.getContentManager().addContent(routesContent);
        toolWindow.getContentManager().addContent(makeContent);

        project.getMessageBus().connect().subscribe(AdonisRouteUpdateNotifier.ADONIS_ROUTES_UPDATE_TOPIC, routesToolWindowContent);
        project.getMessageBus().connect().subscribe(AdonisRcUpdateNotifier.ADONIS_RC_UPDATE_TOPIC, makeToolWindowContent);
    }

    @Override
    public void init(@NotNull ToolWindow toolWindow) {
        ToolWindowFactory.super.init(toolWindow);
    }

    @Override
    public boolean shouldBeAvailable(@NotNull Project project) {
        return ToolWindowFactory.super.shouldBeAvailable(project);
    }

}
