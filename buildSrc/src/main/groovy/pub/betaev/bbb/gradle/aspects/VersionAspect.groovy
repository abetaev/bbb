package pub.betaev.bbb.gradle.aspects

import org.gradle.api.Project
import pub.betaev.bbb.gradle.BBBContext

class VersionAspect extends ProjectAspect {

    @Override
    protected boolean applicable(Project target) {
        return target == target.rootProject
    }

    @Override
    protected void doApply(Project target, BBBContext context) {
        target.version = '0.0.1-SNAPSHOT'
    }

}
