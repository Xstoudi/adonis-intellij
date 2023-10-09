package io.stouder.adonis.cli.json.routes;

import lombok.Getter;

@Getter
public class ControllerRouteHandler extends RouteHandler {
    private String moduleNameOrPath;
    private String method;
}
