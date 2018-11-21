package pub.betaev.bbb.gradle.aspects

import org.gradle.api.Project
import pub.betaev.bbb.gradle.BBBContext
import pub.betaev.bbb.gradle.ContainerPlugin

class ContainerAspect extends ProjectAspect {

    ContainerAspect() {
        after(BaseAspect)
    }

    @Override
    protected boolean applicable(Project target) {
        return target.file('dockerfile.gradle').isFile()
    }

    @Override
    protected void doApply(Project target, BBBContext context) {
        target.plugins.apply(ContainerPlugin)
    }

}
