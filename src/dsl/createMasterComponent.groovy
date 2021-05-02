def projName = "kchaug"
def componentNames = ["bff-mi-kchaug","ms-ban-kchaug"]
//Variables del proceso
def processName = "Deploy"
def processTypeName = "DEPLOY"
//Variables del subproceso
def subTaskNames = ["Start-cmd-1", "Run Plan SB-runPlanBamboo-2", 
"Create Release SB-CreateRelBamboo-2.1", "Run Plan SB-runPlanBamboo-3", 
"Create Release Dev-CreateRelBamboo-3.1", "Deploy-proc-4"]
def paramSubTaskNames = ["command", "planKeySb", "planKeyDev", "projectKeySb","projectKeyDev"]

componentNames.each { compName->
    component compName, {
        pluginKey = 'EC-Artifact'
        projectName = projName
        //Se genera el proceso del componente
        process processName, {
            processType = processTypeName
            //Se generan las subTasksProcess
            subTaskNames.each { subTaskName->
                def subTaskType = subTaskName.split("-")
                if(subTaskType[1] == 'cmd'){
                    processStep subTaskType[0], {
                        actualParameter = [
                            commandToRun: 'echo'
                        ]
                        processStepType = 'command'
                        subprocedure = 'RunCommand'
                        subproject = '/plugins/EC-Core/project'
                    }
                }else if(subTaskType[1] == 'runPlanBamboo'){
                    processStep subTaskType[0], {

                    }
                    
                }else if(subTaskType[1] == 'CreateRelBamboo'){
                    processStep subTaskType[0], {
                    }
                }else if(subTaskType[1] == 'proc'){
                    processStep subTaskType[0], {
                    processStepType = 'procedure'
                    subprocedure = 'Deployment'
                    subproject = projName
                    }
                    /*subTaskNames.each {
                        def validation = it.split("-")
                        if(validation[2] == "1" || validation[2] == "2.1" || validation[2] == "3.1" || )
                        processDependency 'Start', targetProcessStepName: 'Deploy', {
                            branchCondition = '\'$[/myResource/name]\' == \'qa\''
                            branchConditionName = 'if'
                            branchConditionType = 'CUSTOM'
                            branchType = 'ALWAYS'
                        }
                    }*/
                }
            }
        }
        //proceso que rellena los valores de artifactName del Componente
        property 'ec_content_details', {
            property 'artifactName', value: 'a'
            artifactVersionLocationProperty = '/myJob/retrievedArtifactVersions/$[assignedResourceName]'
            property 'pluginProjectName', value: 'EC-Artifact'
        }
    }
}