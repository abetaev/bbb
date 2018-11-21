package pub.betaev.bbb.gradle.aspects

import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import pub.betaev.bbb.gradle.BBBContext

class BaseAspect extends ProjectAspect {

    BaseAspect() {
        after(VersionAspect)
    }

    @Override
    protected boolean applicable(Project target) {
        return true
    }

    @Override
    protected void doApply(Project target, BBBContext context) {
        target.plugins.apply(BasePlugin)
        target.version = target.rootProject.version
    }

}
