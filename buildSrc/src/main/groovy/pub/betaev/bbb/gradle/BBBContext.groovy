package pub.betaev.bbb.gradle;

import pub.betaev.bbb.gradle.aspects.ProjectAspect

class BBBContext {

    private final Map<Class<? extends ProjectAspect>, ProjectAspect> appliedAspects = [:];

    void add(ProjectAspect projectAspect) {
        this.appliedAspects.put(projectAspect.class, projectAspect)
    }

    void ifApplied(Class<? extends ProjectAspect> aspectType, Closure closure) {
        ProjectAspect aspect = appliedAspects.get(aspectType)
        if (aspect) {
            closure.setDelegate(aspect)
            closure()
        }
    }

}
