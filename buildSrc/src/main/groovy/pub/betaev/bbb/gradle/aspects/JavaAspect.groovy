package pub.betaev.bbb.gradle.aspects

import io.franzbecker.gradle.lombok.LombokPlugin;
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import pub.betaev.bbb.gradle.BBBContext;

class JavaAspect extends ProjectAspect {

    JavaAspect() {
    }

    @Override
    protected boolean applicable(Project target) {
        return target.file('src/main/java').isDirectory();
    }

    @Override
    protected void doApply(Project target, BBBContext context) {
        target.plugins.apply(JavaPlugin)
        target.plugins.apply(LombokPlugin)
    }
}
