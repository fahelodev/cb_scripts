def projectName = "TSOFT"

project projectName, {
    pipeline "Pipeline", {
        formalParameter "comando", type: "entry"
        stage "Stage 1", {
            task "Tarea 1", {
                taskType = 'PROCEDURE'
                subprocedure = 'Procedimiento'
                stageSummaryParameters = '[{"name":"Pais","label":"Pais"}]'
                actualParameter = [
                    'comando': '$[comando]',
                ]               
            }
            task "Tarea 2", {
                taskType = 'COMMAND'
                subprocedure = 'RunCommand'
                subpluginKey = 'EC-Core'               
                actualParameter = [
                    'commandToRun': 'echo $[/myStageRuntime/ec_summary/Pais]',
                ]       
            }

            task "Tarea 3A", {
                taskType = 'MANUAL'
                formalParameter 'Deployment', defaultValue: null, {
                    propertyReference = '$[/myPipeline/Deployment]'
                    required = '1'
                    type = 'select'
                }    
            }
        }
    }

    procedure "Procedimiento", {
        formalParameter "comando", type: "entry"
        formalOutputParameter 'Pais'

        step "Paso 1", command : 'echo $[comando]'
        step "Paso 2", command : 'ectool setProperty /myPipeline/Deployment/Pais --value "Chile"'
        step "Paso 3", command : 'ectool setOutputParameter Pais "CHILE"'
    }

    property 'Deployment', {
    }
}