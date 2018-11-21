package pub.betaev.bbb.gradle

import org.gradle.api.Plugin
import org.gradle.api.initialization.ProjectDescriptor
import org.gradle.api.initialization.Settings

class BBBSettings implements Plugin<Settings> {

    @Override
    void apply(Settings target) {

        target.rootProject.buildFileName = 'project.gradle'

        target.rootProject.projectDir.listFiles().findAll({
            it.directory &&
                    !(
                            'buildSrc'.equals(it.name) ||
                                    'gradle'.equals(it.name) ||
                                    it.name.startsWith('.')
                    )
        }).each({ dir -> applyRecursively(target, dir) })

    }

    void applyRecursively(Settings settings, File directory) {

        File projectFile = new File(directory, 'module.gradle')

        if (projectFile.file) {
            String projectPath = directory.absolutePath.substring(settings.rootProject.projectDir.absolutePath.length() + 1)
            settings.include(projectPath)
            ProjectDescriptor project = settings.project(directory)
            project.name = projectPath.replace('/', '.')
            project.buildFileName = 'module.gradle'
            return
        }

        directory.listFiles()
                .findAll({ it.directory })
                .each { applyRecursively(settings, it) }

    }

}
