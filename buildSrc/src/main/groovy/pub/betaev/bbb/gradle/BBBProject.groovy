package pub.betaev.bbb.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.reflections.Reflections
import pub.betaev.bbb.gradle.aspects.ProjectAspect

class BBBProject implements Plugin<Project> {

    @Override
    void apply(Project target) {

        Set<ProjectAspect> aspects = []

        new Reflections("pub.betaev.bbb.gradle.aspects").getSubTypesOf(ProjectAspect).
                stream().
                map({ aspectClass -> aspectClass.newInstance() as ProjectAspect }).sorted().
                each { aspect -> aspects.add(aspect as ProjectAspect) }

        BBBContext context = new BBBContext()

        for (ProjectAspect aspect : aspects) {
            aspect.apply(target, context)
        }

        target.subprojects { subproject ->
            context = new BBBContext()
            aspects.each { it.apply(subproject, context) }
        }

    }

}
