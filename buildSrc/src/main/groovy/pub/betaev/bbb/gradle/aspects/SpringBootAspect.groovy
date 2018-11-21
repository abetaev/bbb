package pub.betaev.bbb.gradle.aspects

import org.gradle.api.Project
import org.gradle.api.Task
import org.springframework.boot.gradle.plugin.SpringBootPlugin
import pub.betaev.bbb.gradle.BBBContext

class SpringBootAspect extends ProjectAspect {

    SpringBootAspect() {
        after(ContainerAspect)
    }

    @Override
    protected boolean applicable(Project target) {
        return target.file('src/main/resources/bootstrap.yml').isFile()
    }

    @Override
    protected void doApply(Project target, BBBContext context) {
        target.plugins.apply(SpringBootPlugin)

        context.ifApplied(ContainerAspect) {
            Task customizeSpringBootContainerTask = target.tasks.create('customizeSpringBootContainer') {
                dependsOn target.configurations.archives
                doLast {
                    String from = target.relativePath(target.configurations.bootArchives.artifacts.files.first())
                    String to = '/app.jar'
                    target.containerArtifacts.put(from, to)
                }
            }
            target.tasks.configureDockerfile.dependsOn(customizeSpringBootContainerTask)
        }

    }
}
