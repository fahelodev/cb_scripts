@Grab(group='org.codehaus.groovy', module='groovy-yaml', version='3.0.8')
import groovy.yaml.YamlSlurper
import com.electriccloud.client.groovy.ElectricFlow
ef = new ElectricFlow()

def configYaml = '''\
projects:
    - projectName: "kchaug"
      apps:
        - name: "bff-mi-fondos-mutuos-kchaug"
          environments:
            - name: "Sandbox"
              values:
                - data:
                  - name: "projectKey"
                  - value: "ASD"
                - data:
                  - name: "planKey"
                  - value: "ASD-1"
                - data:
                  - name: "deploymentName"
                  - value: "planes_bamboo_sb"
                - data:
                  - name: "branchName"
                  - value: "ci_build"
                - data:
                  - name: "branchKey"
                  - value: "FG02"
                - data:
                  - name: "artifactId"
                  - value: "asd123"
                - data:
                  - name: "artifactName"
                  - value: "sb-devsecops.cl"
                - data:
                  - name: "nameBambooExtras"
                  - value: "namespace"
                - data:
                  - name: "valueBambooExtras"
                  - value: "sb-devsecops.cl"
            - name: "Dev"
              values:
                - data:
                  - name: "projectKey"
                  - value: "ASD"
                - data:
                  - name: "planKey"
                  - value: "ASD-1"
                - data:
                  - name: "deploymentName"
                  - value: "planes_bamboo_sb"
                - data:
                  - name: "branchName"
                  - value: "ci_build"
                - data:
                  - name: "branchKey"
                  - value: "FG02"
                - data:
                  - name: "artifactId"
                  - value: "asd123"
                - data:
                  - name: "artifactName"
                  - value: "sb-devsecops.cl"
                - data:
                  - name: "nameBambooExtras"
                  - value: "namespace"
                - data:
                  - name: "valueBambooExtras"
                  - value: "sb-devsecops.cl"
        - name: "bff-mi-kchaug2"
          environments:
            - name: "Sandbox"
              values:
                - data:
                  - name: "projectKey"
                  - value: "BBBB"
                - data:
                  - name: "planKey"
                  - value: "BBB-1"
                - data:
                  - name: "deploymentName"
                  - value: "planes_bamboo_sb"
                - data:
                  - name: "branchName"
                  - value: "ci_build"
                - data:
                  - name: "branchKey"
                  - value: "FBB02"
                - data:
                  - name: "artifactId"
                  - value: "asd123"
                - data:
                  - name: "artifactName"
                  - value: "sb-devsecops.cl"
                - data:
                  - name: "nameBambooExtras"
                  - value: "namespace"
                - data:
                  - name: "valueBambooExtras"
                  - value: "sb-devsecops.cl"
            - name: "Dev"
              values:
                - data:
                  - name: "projectKey"
                  - value: "CCCC"
                - data:
                  - name: "planKey"
                  - value: "CCCC-1"
                - data:
                  - name: "deploymentName"
                  - value: "planes_bamboo_sb"
                - data:
                  - name: "branchName"
                  - value: "ci_build"
                - data:
                  - name: "branchKey"
                  - value: "FCC02"
                - data:
                  - name: "artifactId"
                  - value: "asd123"
                - data:
                  - name: "artifactName"
                  - value: "sb-devsecops.cl"
                - data:
                  - name: "nameBambooExtras"
                  - value: "namespace"
                - data:
                  - name: "valueBambooExtras"
                  - value: "sb-devsecops.cl"
'''
// Parse the YAML.


void scanFirst(String val) {
  def config = new YamlSlurper().parseText(val)
  config.projects.each{ objProjects ->
    def startingPath = 'server/' + objProjects.projectName
    def pathMss = '/'+startingPath +'/'+ 'mss'
    //def mss
    objProjects.apps.each{ objApps ->
      ef.createComponent(projectName: objProjects.projectName, 
      	componentName: objApps.name, 
        pluginKey: "EC-Artifact"
        
        process: processName: 'test'
      )
    }
    //def lines = 'propertyName:' + pathMss +','+' value:'+ mss
    //println lines
  }
}

scanFirst(configYaml)