@Grab(group='org.codehaus.groovy', module='groovy-yaml', version='3.0.8')
import groovy.yaml.YamlSlurper
import com.electriccloud.client.groovy.ElectricFlow
import com.electriccloud.client.groovy.models.ActualParameter

ef = new ElectricFlow()
processNameStep = "Deploy"
processNameStepSB = ["Run Plan SB", "Create Release SB", "Deploy SB"]
processNameStepDev = ["Run Plan Dev", "Create Release Dev", "Deploy Dev"]

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
void typeOfProcessStep(String _processStepName, String _projectName , String _componentName,
    String _processSetType, String _subProcedure, String _subProject){
        def oldProcessStepName
        processNameStepSB.eachWithIndex{ it, index ->
            ef.createProcessStep(processStepName: it,
                processName: 'Deploy',
                projectName: _projectName,
                componentName: _componentName,
                processStepType: _processSetType,
                subprocedure: _subProcedure,
                subproject: _subProject
            )
            println index
            if(index == 0){
                typeOfDependency(_componentName, _projectName, 'Start', it)
            }else if(index >= 1){
                println "index: "+index +" oldprocess:"+oldProcessStepName+" it:"+it
                typeOfDependency(_componentName, _projectName, oldProcessStepName, it)
            }
            oldProcessStepName = it
        }
        
}

void typeOfDependency(String _processName, String _projectName , String _processStepName,
    String _targetProcessStepName){
    ef.createProcessDependency(
                projectName: _projectName,
                processName: 'Deploy',
                processStepName: _processStepName,
                targetProcessStepName: _targetProcessStepName,
                componentName: _processName
    )
}

void scanFirst(String val) {
  def config = new YamlSlurper().parseText(val)
  config.projects.each{ objProjects ->
    def startingPath = 'server/' + objProjects.projectName
    def pathMss = '/'+startingPath +'/'+ 'mss'
    //def mss
    objProjects.apps.each{ objApps ->
        ef.createComponent(projectName: objProjects.projectName,
            componentName: objApps.name,
            pluginKey: "EC-Artifact",
        )
        ef.createProcess(processName: 'Deploy',
        projectName: objProjects.projectName,
        componentName: objApps.name
        )
        //Tarea Start necesaria para la generación de condiciones
        ef.createProcessStep(processStepName: 'Start',
            processName: 'Deploy',
            projectName: objProjects.projectName,
            componentName: objApps.name,
            processStepType: 'command',
            subprocedure: 'RunCommand',
            subproject: '/plugins/EC-Core/project'
        )
        typeOfProcessStep('Start', objProjects.projectName, objApps.name,
                        'command', 'RunCommand', '/plugins/EC-Core/project')
        //def result = ef.getActualParameter(
                //actualParameterName: 'test-actualParameterName')
        objApps.environments.each{ env->
            
            env.values.each{ objValues->
                objValues.data.each{
                    
                }
            }
        }
        /*
        ef.createProcessStep(processStepName: 'Test',
            processName: 'Deploy',
            projectName: objProjects.projectName,
            componentName: objApps.name,
            processStepType: 'command',
            subprocedure: 'RunCommand',
            subproject: '/plugins/EC-Core/project'
        )
        ef.createActualParameter(
            processName: 'Deploy',
            processStepName: 'Test',
            projectName: objProjects.projectName,
            componentName: objApps.name,
            actualParameterName: 'commandToRun',
            value: 'echo'
        )
        */
    }
    //def lines = 'propertyName:' + pathMss +','+' value:'+ mss
    //println lines
  }
}

scanFirst(configYaml)