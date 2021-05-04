//@Grab(group='org.codehaus.groovy', module='groovy-yaml', version='3.0.8')
import groovy.yaml.YamlSlurper
//import com.electriccloud.client.groovy.ElectricFlow
//ef = new ElectricFlow()

def configYaml = '''\
projects:
    - projectName: "kchaug"
      apps:
        - name: "bff-mi-fondos-mutuos"
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
        - name: "bff-mi-kchaug"
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

void scan(String val) {
  def config = new YamlSlurper().parseText(val)
  config.projects.each{ objProjects ->
    def startingPath = '/myJob/' + objProjects.projectName
    objProjects.apps.each{ objApps ->
      def middlePath = objApps.name
      objApps.environments.each{ environment->
        def finishPath = environment.name
        environment.values.each { objValues->
          def path = '/'+startingPath+'/'+middlePath+'/'+finishPath+ '/' 
          objValues.data.each {
            if (it.value != null) {
              //ef.setProperty(propertyName: "${path}", value: "${it.value}")
              def line = 'propertyName:' + path +','+' value:'+ it.value
              println line
            } else {
              path = path + it.name
            }
          }
        }
      }
    }
  }
}

void scanFirst(String val) {
  def config = new YamlSlurper().parseText(val)
  config.projects.each{ objProjects ->
    def startingPath = 'server/' + objProjects.projectName
    def pathMss = '/'+startingPath +'/'+ 'mss'
    def mss
    objProjects.apps.each{ objApps ->
      if(mss != null){
        mss = objApps.name + ";" + mss
      }else{
        mss = objApps.name
      }
    }
    //ef.setProperty(propertyName: "${pathMss}", value: "${objApps.name}"
    def lines = 'propertyName:' + pathMss +','+' value:'+ mss
    println lines
  }
}

scanFirst(configYaml)
scan(configYaml)


