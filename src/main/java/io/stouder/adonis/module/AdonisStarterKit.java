package io.stouder.adonis.module;

import io.stouder.adonis.AdonisBundle;
import lombok.Getter;

@Getter
public enum AdonisStarterKit {
    SLIM("Slim", AdonisBundle.message("adonis.project.generator.template.slim.description"), "github:adonisjs/slim-starter-kit"),
    WEB("Web", AdonisBundle.message("adonis.project.generator.template.web.description"), "github:adonisjs/web-starter-kit"),

    API("API", AdonisBundle.message("adonis.project.generator.template.api.description"), "github:adonisjs/api-starter-kit"),
    ;

    private final String name;
    private final String hint;
    private final String repositoryUrl;

    AdonisStarterKit(String name, String hint, String repositoryUrl) {
        this.name = name;
        this.hint = hint;
        this.repositoryUrl = repositoryUrl;
    }

}
