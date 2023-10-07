package io.stouder.adonis.cli.json;

import lombok.Getter;

import java.util.List;

@Getter
public class Route {
    private String name;
    private String pattern;
    private List<String> methods;
    private RouteHandler handler;
}
