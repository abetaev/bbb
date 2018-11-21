package pub.betaev.bbb.gradle

import com.bmuschko.gradle.docker.DockerRemoteApiPlugin
import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.DockerRemoveImage
import com.bmuschko.gradle.docker.tasks.image.Dockerfile
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.Dependency
import org.gradle.api.tasks.bundling.Zip

class ContainerPlugin implements Plugin<Project> {
    @Override
    void apply(Project target) {
        target.repositories.maven { url 'http://repo.spring.io/libs-milestone/' }

        target.plugins.apply(DockerRemoteApiPlugin)

        Dockerfile dockerfileTask = target.tasks.create('dockerfile', Dockerfile)
        dockerfileTask.dependsOn(target.configurations.default)
        dockerfileTask.outputs.files("${target.buildDir}/Dockerfile")

        dockerfileTask.metaClass.artifact = { configurationName ->
            String artifactName = target.configurations[configurationName].artifacts.files.first().name
            return "build/libs/${artifactName}"
        }

        DockerBuildImage dockerBuildImageTask = target.tasks.create('dockerBuildImage', DockerBuildImage) {

            dependsOn dockerfileTask, target.configurations.archives
            inputDir = project.projectDir
            dockerFile = dockerfileTask.destFile
            tags = [
                    "${project.group}/${project.name}" as String,
                    "${project.group}/${project.name}:${project.version}" as String
            ]

        }

        Task containerBuildTask = target.tasks.create('containerBuild', Zip) {
            dependsOn dockerBuildImageTask
            from dockerfileTask.destFile
        }

        Configuration containerConfiguration = target.configurations.create('container')
        target.configurations.default.extendsFrom(containerConfiguration)

        Configuration containerFileConfiguration = target.configurations.create('containerFile').setTransitive(false)
        target.configurations.default.extendsFrom(containerFileConfiguration)

        dockerfileTask.metaClass.dependency = { alias ->
            Dependency dependency = containerFileConfiguration.dependencies.find { alias.equals(it.alias) }
            if (dependency == null) {
                throw new IllegalStateException("dependency with alias ${alias} not found")
            }
            Set<File> files = containerFileConfiguration.files(dependency)
            if (files.size() > 1) {
                throw new IllegalStateException("more than one file resolved for dependency '${alias}'")
            }
            return "${target.relativePath(target.buildDir)}/libs/${files.first().name}"
        }

        target.artifacts.add('container', containerBuildTask)

        Map<String, String> artifacts = [:]
        target.extensions.add('containerArtifacts', artifacts)

        Task configureDockerfileTask = target.task('configureDockerfile') {

            dependsOn containerFileConfiguration

            doLast {

                containerConfiguration.dependencies.each {
                    dockerfileTask.from(dependencyToImageName(it))
                }
                target.copy {
                    from containerFileConfiguration
                    into "${target.buildDir}/libs/"
                }
                target.file('src/main/container')
                target.apply(from: target.file('dockerfile.gradle'), to: dockerfileTask)
                target.fileTree('src/main/container').each {
                    if (it.file) {
                        String relativePath = target.relativePath(it)
                        dockerfileTask.copyFile(relativePath, relativePath.substring('src/main/container'.length()))
                    }
                }
                target.file('src/main/container').listFiles()
                        .each { dockerfileTask.copyFile('src/main/container/' + it.name, it.name) }
                artifacts.each { from, to -> dockerfileTask.copyFile(from, to) }

            }

        }

        // finally make a loop
        dockerfileTask.dependsOn(configureDockerfileTask)

        DockerRemoveImage dockerRemoveImageTask = target.tasks.create('dockerRemoveImage', DockerRemoveImage) {
            imageId = "${project.group}/${project.name}:${project.version}" as String
            onError({println "image not found"})
            onComplete({println "image deleted"})
        }

        target.tasks.clean.dependsOn(dockerRemoveImageTask)

    }

    private static String dependencyToImageName(Dependency dependency) {
        String version = dependency.version == null ? 'latest' : dependency.version
        String prefix = dependency.group == null ? '' : "${dependency.group}/"
        String name = dependency.name
        if (dependency.version == null) {
            return "${dependency.group}:${dependency.name}"
        }

        return "${prefix}${name}:${version}"
    }

}
