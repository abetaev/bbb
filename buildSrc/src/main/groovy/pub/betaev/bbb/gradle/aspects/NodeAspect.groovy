package pub.betaev.bbb.gradle.aspects

import org.gradle.api.Project
import org.gradle.api.Task
import org.ysb33r.gradle.nodejs.plugins.NpmPlugin
import org.ysb33r.gradle.nodejs.tasks.NpmTask
import pub.betaev.bbb.gradle.BBBContext

class NodeAspect extends ProjectAspect {

    NodeAspect() {
        after(BaseAspect)
    }

    @Override
    protected boolean applicable(Project target) {
        return target.file('package.json').isFile();
    }

    @Override
    protected void doApply(Project target, BBBContext context) {
        target.plugins.apply(NpmPlugin)

        Task npmBuildTask = target.tasks.create('npmBuild', NpmTask) {
            command 'run'
            cmdArgs 'build'
        }
        target.tasks.build.finalizedBy(npmBuildTask)

    }

}
