/*******************************************************************************
 * Copyright (c) 2019 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package com.redhat.devtools.intellij.lsp4mp4ij.psi.core;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.progress.EmptyProgressIndicator;
import com.redhat.devtools.intellij.MavenModuleImportingTestCase;
import com.redhat.devtools.intellij.lsp4mp4ij.psi.internal.core.ls.PsiUtilsLSImpl;
import org.eclipse.lsp4mp.commons.ClasspathKind;
import org.eclipse.lsp4mp.commons.DocumentFormat;
import org.eclipse.lsp4mp.commons.MicroProfileProjectInfo;
import org.eclipse.lsp4mp.commons.MicroProfilePropertiesScope;
import org.jetbrains.idea.maven.model.MavenId;
import org.jetbrains.idea.maven.project.MavenProjectsManager;
import org.jetbrains.idea.maven.utils.MavenArtifactUtil;

import java.io.File;

import static com.redhat.devtools.intellij.lsp4mp4ij.psi.core.MicroProfileAssert.assertProperties;
import static com.redhat.devtools.intellij.lsp4mp4ij.psi.core.MicroProfileAssert.p;
import static org.eclipse.lsp4mp.commons.metadata.ItemMetadata.CONFIG_PHASE_BUILD_TIME;

/**
 * @see <a href="https://github.com/redhat-developer/quarkus-ls/blob/master/microprofile.jdt/com.redhat.microprofile.jdt.test/src/main/java/com/redhat/microprofile/jdt/core/PropertiesManagerTest.java">https://github.com/redhat-developer/quarkus-ls/blob/master/microprofile.jdt/com.redhat.microprofile.jdt.test/src/main/java/com/redhat/microprofile/jdt/core/PropertiesManagerTest.java</a>
 */
public class PropertiesManagerTest extends MavenModuleImportingTestCase {
    private static final String XML = "<groupId>com.redhat.devtools.intellij.quarkus</groupId>" +
            "<artifactId>sample-core-deployment</artifactId>" +
            "<version>0.0.1-SNAPSHOT</version>" +
            "<dependencies>" +
            "<dependency>" +
            "<groupId>io.quarkus</groupId>" +
            "<artifactId>quarkus-core-deployment</artifactId>" +
            "<version>0.24.0</version>" +
            "</dependency>" +
            "</dependencies>";

    private Module module;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        module = super.createMavenModule("core-deployment", XML);
    }

    public void testQuarkusCoreDeploymentProperties() {
        MicroProfileProjectInfo info = PropertiesManager.getInstance().getMicroProfileProjectInfo(module, MicroProfilePropertiesScope.SOURCES_AND_DEPENDENCIES, ClasspathKind.SRC, PsiUtilsLSImpl.getInstance(myProject), DocumentFormat.PlainText, new EmptyProgressIndicator());
        MavenProjectsManager myProjectsManager = MavenProjectsManager.getInstance(module.getProject());
        File quarkusCoreJARFile = MavenArtifactUtil.getArtifactFile(myProjectsManager.findProject(module).getLocalRepository(), new MavenId("io.quarkus:quarkus-core-deployment:0.24.0"), "jar").toFile();
        assertNotNull("Test existing of quarkus-core*.jar", quarkusCoreJARFile);

        assertProperties(info,

                // io.quarkus.deployment.ApplicationConfig
                p("quarkus-core", "quarkus.application.name", "java.lang.String",
                        "The name of the application.\nIf not set, defaults to the name of the project.", true,
                        "io.quarkus.deployment.ApplicationConfig", "name", null, CONFIG_PHASE_BUILD_TIME, null),

                p("quarkus-core", "quarkus.application.version", "java.lang.String",
                        "The version of the application.\nIf not set, defaults to the version of the project", true,
                        "io.quarkus.deployment.ApplicationConfig", "version", null, CONFIG_PHASE_BUILD_TIME, null),

                // io.quarkus.deployment.JniProcessor$JniConfig
                p("quarkus-core", "quarkus.jni.enable", "boolean", "Enable JNI support.", true,
                        "io.quarkus.deployment.JniProcessor$JniConfig", "enable", null, CONFIG_PHASE_BUILD_TIME,
                        "false"),

                p("quarkus-core", "quarkus.jni.library-paths", "java.util.List<java.lang.String>",
                        "Paths of library to load.", true, "io.quarkus.deployment.JniProcessor$JniConfig",
                        "libraryPaths", null, CONFIG_PHASE_BUILD_TIME, null),

                // io.quarkus.deployment.SslProcessor$SslConfig
                p("quarkus-core", "quarkus.ssl.native", "java.util.Optional<java.lang.Boolean>",
                        "Enable native SSL support.", true, "io.quarkus.deployment.SslProcessor$SslConfig", "native_",
                        null, CONFIG_PHASE_BUILD_TIME, null),

                // io.quarkus.deployment.index.ApplicationArchiveBuildStep$IndexDependencyConfiguration
                // -> Map<String, IndexDependencyConfig>
                p("quarkus-core", "quarkus.index-dependency.{*}.classifier", "java.lang.String",
                        "The maven classifier of the artifact to index", true,
                        "io.quarkus.deployment.index.IndexDependencyConfig", "classifier", null,
                        CONFIG_PHASE_BUILD_TIME, null),
                p("quarkus-core", "quarkus.index-dependency.{*}.artifact-id", "java.lang.String",
                        "The maven artifactId of the artifact to index", true,
                        "io.quarkus.deployment.index.IndexDependencyConfig", "artifactId", null,
                        CONFIG_PHASE_BUILD_TIME, null),
                p("quarkus-core", "quarkus.index-dependency.{*}.group-id", "java.lang.String",
                        "The maven groupId of the artifact to index", true,
                        "io.quarkus.deployment.index.IndexDependencyConfig", "groupId", null, CONFIG_PHASE_BUILD_TIME,
                        null));
    }
}
