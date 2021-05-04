def projectName = "TSOFT"
def procedureName = "Procedimiento"
def pipelineName = "Pipeline"

deletePipeline(projectName: projectName, pipelineName: pipelineName) 
deleteProcedure(projectName: projectName, procedureName: procedureName) 

project projectName, {
    procedure procedureName, {
        property 'Config', {
            property 'url', value: 'http://152.139.147.195:8085/rest/api/latest/'
            property 'headers', value: '["Authorization": "Basic c2VydmljZXVzZXI6UjRzQjNycnlwaTMj"]'
            property 'path', value:'deploy/project/forPlan'
            property 'json', value: '[{"id":1504837649,"name":"bff-mi-inversion-sandbox-dply","description":"bff-mi-inversion"},{"id":1504837645,"name":"ms-servicios-demogenerador-sandbox-dply","description":"ms-servicios-demogenerador"}]'
            property 'name', value: '/myPipeline/Deployment/'
        }

        formalParameter "bambooPlanKey", type: "entry", defaultValue: "MIINSB-BFFBCSB"

        step "Paso 1", shell: 'ec-groovy',command: '''
            @Grab(group="org.codehaus.groovy.modules.http-builder", module="http-builder", version="0.7.1")
            import groovyx.net.http.RESTClient
            import groovy.json.JsonSlurper
            import com.electriccloud.client.groovy.ElectricFlow

            def ef = new ElectricFlow()
            def bambooPlanKey = "$[bambooPlanKey]".split("-")
            def client = new RESTClient( "$[/myProcedure/Config/url]" )
            def Slurper = new JsonSlurper()

            //def response = client.get(path: "$[/myProcedure/Config/path]", query: ["planKey" : bambooPlanKey[0] + "-" + bambooPlanKey[1]],  headers: $[/myProcedure/Config/headers])           
            def json = '$[/myProcedure/Config/json]' //response.responseData
            def obj = Slurper.parseText(json)

            obj.each { v -> ef.setProperty(propertyName: "$[/myProcedure/Config/name]" + v.name, value: "" +v.id) } 
            '''.stripIndent()
        //step "Paso 2", command : 'ectool setProperty /myPipeline/Deployment/Pais --value "Chile"'
        //step "Paso 3", command : 'ectool setOutputParameter Pais "CHILE"'
    }

    pipeline pipelineName, {
        formalParameter "bambooPlanKey", type: "entry", defaultValue: "MIINSB-BFFBCSB"
        formalParameter "environment", type: "entry", defaultValue: "Sandbox"

        stage "Stage 1", {
            task "Tarea 1", {
                taskType = 'PROCEDURE'
                subprocedure = 'Procedimiento'
                //stageSummaryParameters = '[{"name":"Pais","label":"Pais"}]'
                actualParameter = [
                    'bambooPlanKey': '$[bambooPlanKey]',
                ]               
            }
            /*task "Tarea 2", {
                taskType = 'COMMAND'
                subprocedure = 'RunCommand'
                subpluginKey = 'EC-Core'               
                actualParameter = [
                    'commandToRun': 'echo $[/myStageRuntime/ec_summary/Pais]',
                ]       
            }*/

            task "Tarea 2", {
                taskType = 'MANUAL'
                formalParameter 'DeploymentName', defaultValue: null, {
                    propertyReference = '$[/myPipeline/Deployment]'
                    required = '1'
                    type = 'select'
                }    
            }

            task "Tarea 3", {
                taskType = 'COMMAND'
                subprocedure = 'RunCommand'
                subpluginKey = 'EC-Core'               
                actualParameter = [
                    'commandToRun': 'echo $[/myPipelineRuntime/stages/Stage 1/tasks/Tarea 2/DeploymentName]',
                ] 
            }
        }

        property 'Deployment', {
        }
    }


}