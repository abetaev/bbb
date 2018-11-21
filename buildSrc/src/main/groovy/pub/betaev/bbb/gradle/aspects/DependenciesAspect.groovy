package pub.betaev.bbb.gradle.aspects

import io.spring.gradle.dependencymanagement.DependencyManagementPlugin
import org.gradle.api.Project
import pub.betaev.bbb.gradle.BBBContext

class DependenciesAspect extends ProjectAspect {

    @Override
    protected boolean applicable(Project target) {
        return true
    }

    @Override
    protected void doApply(Project target, BBBContext context) {
        target.plugins.apply(DependencyManagementPlugin)
    }

}
