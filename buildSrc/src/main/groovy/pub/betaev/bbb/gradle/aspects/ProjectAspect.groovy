package pub.betaev.bbb.gradle.aspects

import org.gradle.api.Project
import pub.betaev.bbb.gradle.BBBContext
import pub.betaev.bbb.gradle.Ordered

abstract class ProjectAspect implements Ordered<ProjectAspect>, Comparable<ProjectAspect> {

    final void apply(Project target, BBBContext context) {
        if (applicable(target)) {
            doApply(target, context)
            context.add(this)
        }
    }

    protected abstract boolean applicable(Project target)

    protected abstract void doApply(Project target, BBBContext context)

    @Override
    final int compareTo(ProjectAspect that) {
        int r = compare(that)
        if (r == 0) {
            return this.class.name.compareTo(that.class.name)
        }
        return r
    }
}