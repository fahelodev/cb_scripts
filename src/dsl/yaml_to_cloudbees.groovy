@Grab(group='org.codehaus.groovy', module='groovy-yaml', version='3.0.8')
import groovy.yaml.YamlSlurper


def configYaml = '''\
projects:
    - projectName: "Mi Inversion"
      apps:
        - name: "bff-mi-banco"
          environments:
            - name: "Sandbox"
              values:
                - projectKey: "ASD"
                - planKey: "FG01"
                - deploymentName: "planes_bamboo_sb"
                - branchName: "ci_build"
                - branchKey: "FG02"
                - artifactId: "asd123"
                - artifactName: "sb-devsecops.cl"
                  parameters:
                    - name: "namespace"
                    - value: "sb-devsecops"
            - name: "Dev"
              values:
                - projectKey: "ASD"
                - planKey: "FG02"
                - deploymentName: "planes_bamboo_dev"
                - branchName: "ci_build"
                - branchKey: "FG04"
                - artifactId: "asd1234"
                - artifactName: "sb-devsecops.cl4"
                  parameters:
                    - name: "namespace4"
                    - value: "sb-devsecops4"
        - name: "bff-mi-fondos-mutuos"
          environments:
            - name: "Sandbox"
              values:
                - projectKey: "ASD"
                - planKey: "FG01"
                - deploymentName: "planes_bamboo_sb"
                - branchName: "ci_build"
                - branchKey: "FG02"
                - artifactId: "asd123"
                - artifactName: "sb-devsecops.cl"
                  parameters:
                    - name: "namespace"
                    - value: "sb-devsecops"
            - name: "Dev"
              values:
                - projectKey: "ASD"
                - planKey: "FG02"
                - deploymentName: "planes_bamboo_dev"
                - branchName: "ci_build"
                - branchKey: "FG04"
                - artifactId: "asd1234"
                - artifactName: "sb-devsecops.cl4"
                  parameters:
                    - name: "namespace4"
                    - value: "sb-devsecops4"
'''
// Parse the YAML.
  
def config = new YamlSlurper().parseText(configYaml)
def mss = []
config.projects.each{ objProjects ->
    //println objProjects.projectName
    objProjects.apps.each{ objApps ->
        mss.add(objApps.name)
    }
}
println mss