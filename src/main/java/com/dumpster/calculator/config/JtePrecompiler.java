package com.dumpster.calculator.config;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.DirectoryCodeResolver;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public final class JtePrecompiler {

    private JtePrecompiler() {
    }

    public static void main(String[] args) {
        Path templateRoot = args.length >= 1 ? Path.of(args[0]) : Path.of("src/main/jte");
        Path outputRoot = args.length >= 2 ? Path.of(args[1]) : Path.of("build/jte-classes");

        if (!Files.exists(templateRoot)) {
            throw new IllegalStateException("JTE template directory does not exist: " + templateRoot.toAbsolutePath());
        }

        recreateDirectory(outputRoot);

        TemplateEngine engine = TemplateEngine.create(
                new DirectoryCodeResolver(templateRoot),
                outputRoot,
                ContentType.Html
        );
        engine.setClassPath(classPathEntries());
        List<String> precompiledTemplates = engine.precompileAll();
        System.out.println("Precompiled JTE templates: " + precompiledTemplates.size());
    }

    private static List<String> classPathEntries() {
        String classPath = System.getProperty("java.class.path", "");
        return Arrays.stream(classPath.split(File.pathSeparator))
                .filter(entry -> entry != null && !entry.isBlank())
                .toList();
    }

    private static void recreateDirectory(Path target) {
        try {
            if (Files.exists(target)) {
                try (var walk = Files.walk(target)) {
                    walk.sorted(Comparator.reverseOrder()).forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            throw new IllegalStateException("Failed deleting " + path, e);
                        }
                    });
                }
            }
            Files.createDirectories(target);
        } catch (IOException e) {
            throw new IllegalStateException("Failed preparing JTE output directory: " + target.toAbsolutePath(), e);
        }
    }
}
