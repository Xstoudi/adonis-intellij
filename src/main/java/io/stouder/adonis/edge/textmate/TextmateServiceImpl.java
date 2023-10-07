package io.stouder.adonis.edge.textmate;

import com.intellij.openapi.application.PluginPathManager;
import com.intellij.openapi.diagnostic.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.stream.Stream;

public class TextmateServiceImpl implements TextmateService {
    private static final Logger LOG = Logger.getInstance(TextmateService.class);

    @Override
    public void ensureEdgeIsInstalled() {
        Path bundlesPath = this.getBundlesPath();

        if(this.isEdgeInstalled()) {
            LOG.info("Edge.js is already installed.");
            return;
        }

        this.installEdge();
    }

    private Path getBundlesPath() {
        return PluginPathManager.getPluginHome("textmate")
                .toPath()
                .resolve("lib/bundles")
                .normalize();
    }

    private boolean isEdgeInstalled() {
        Path bundlesPath = this.getBundlesPath();
        try (Stream<Path> files = Files.list(bundlesPath)) {
            return files.anyMatch(path -> path.getFileName().toString().equals("edge"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void installEdge() {
        LOG.info("Installing Edge.js...");
        this.resources("edge")
                .forEach(path -> {
                    String rootedPath = Paths.get("/").resolve(path).toString().replaceAll("\\\\", "/");
                    try(InputStream inputStream = Objects.requireNonNull(TextmateServiceImpl.class.getResource(rootedPath)).openStream()) {
                        Path filePath = this.getBundlesPath().resolve(path);
                        try {
                            Files.createDirectories(filePath.getParent());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        try (OutputStream outputStream = Files.newOutputStream(filePath, StandardOpenOption.CREATE)) {
                            inputStream.transferTo(outputStream);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } catch (IOException e) {
                        LOG.error("Could not open stream for " + rootedPath, e);
                    }
                    LOG.info("Installed " + path);
                });
    }

    private Stream<Path> resources(String folder) {
        return Stream.of(
                "package.json",
                "package.nls.json",
                "language-configuration.json",
                "syntaxes/edge.tmLanguage.json",
                "snippets/edge/globals.json",
                "snippets/edge/tags.json",
                "snippets/adonis/routes.json"
        )
                .map(path -> Paths.get(folder, path));
    }
}
