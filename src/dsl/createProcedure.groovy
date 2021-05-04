def projectName = "TSOFT"
def procedureName = "Procedimiento"
def pipelineName = "Pipeline"

deletePipeline(projectName: projectName, pipelineName: pipelineName) 
deleteProcedure(projectName: projectName, procedureName: procedureName) 

project projectName, {
    procedure "Procedimiento", {
        property 'Config', {
            property 'url', value: 'http://152.139.147.195:8085/rest/api/latest/'
            property 'headers', value: '["Authorization": "Basic c2VydmljZXVzZXI6UjRzQjNycnlwaTMj"]'
            property 'path', value:'deploy/project/forPlan'
            property 'json', value: '[{"id":1504837649,"name":"bff-mi-inversion-sandbox-dply","description":"bff-mi-inversion"},{"id":1504837645,"name":"ms-servicios-demogenerador-sandbox-dply","description":"ms-servicios-demogenerador"}]'
            property 'name', value: '/myPipeline/Deployment'
        }

        formalParameter "bambooPlanKey", type: "entry", defaultValue: "MIINSB-BFFBCSB"

        step "Paso 1 - Reset Properties", shell: 'ec-groovy',command: '''
            import com.electriccloud.client.groovy.ElectricFlow
            def ef = new ElectricFlow()
            
            ef.deleteProperty(propertyName: "$[/myProcedure/Config/name]")
        '''.stripIndent()

        step "Paso 2 - Setear Properties", shell: 'ec-groovy',command: '''
            @Grab(group="org.codehaus.groovy.modules.http-builder", module="http-builder", version="0.7.1")
            import groovyx.net.http.RESTClient
            import groovy.json.JsonSlurper
            import com.electriccloud.client.groovy.ElectricFlow

            def ef = new ElectricFlow()
            def bambooPlanKey = "$[bambooPlanKey]".split("-")
            def client = new RESTClient( "$[/myProcedure/Config/url]" )
            def Slurper = new JsonSlurper()

            def response = client.get(path: "$[/myProcedure/Config/path]", query: ["planKey" : bambooPlanKey[0] + "-" + bambooPlanKey[1]],  headers: $[/myProcedure/Config/headers])           
            def obj = response.responseData
            //def json = '$[/myProcedure/Config/json]'
            //def obj = Slurper.parseText(json)

            obj.each { v -> ef.setProperty(propertyName: "$[/myProcedure/Config/name]" + "/" + v.name, value: "" +v.id) } 
            '''.stripIndent()
    }

    procedure "Procedimiento2", {
        property 'Config', {
            property 'url', value: 'http://152.139.147.195:8085/rest/api/latest/'
            property 'headers', value: '["Authorization": "Basic c2VydmljZXVzZXI6UjRzQjNycnlwaTMj"]'
        }

        formalParameter "bambooPlanKey", type: "entry", defaultValue: "MIINSB-BFFBCSB"
        formalParameter "environment", type: "entry", defaultValue: "Sandbox"
        formalParameter "deploymentProjectId", type: "entry"

        step "Paso 1 - Despliegue", shell: 'ec-groovy',command: '''
            @Grab(group="org.codehaus.groovy.modules.http-builder", module="http-builder", version="0.7.1")
            import groovyx.net.http.RESTClient

            def client = new RESTClient( "$[/myProcedure/Config/url]" )
            def response = client.get(path :'deploy/project/' + $[deploymentProjectId],  headers: $[/myProcedure/Config/headers])
            println "deploymentProjectId: " + $[deploymentProjectId]

            def environmentId = response.responseData.environments.findAll{ env -> env.name == "$[environment]"}.id[0]
            println "environmentId: " + environmentId

            response = client.get(path : 'deploy/project/' + $[deploymentProjectId] + '/versions',  headers: $[/myProcedure/Config/headers])
            def versionId = response.responseData.versions[0].id
            println "versionId: " + versionId

            response = client.post(path : 'queue/deployment', query: ['environmentId': environmentId, 'versionId': versionId], headers: $[/myProcedure/Config/headers])
            def deploymentResultId = response.responseData.deploymentResultId
            println "deploymentResultId: " + deploymentResultId 

            def validation = true
            while (validation) {
                sleep(15000) //wait 15 secs
                response = client.get(path : 'deploy/environment/' + environmentId + '/results',  headers: $[/myProcedure/Config/headers])
                validation = response.responseData.results[0].deploymentState == 'SUCCESS'
            }
        '''.stripIndent()
    }

    pipeline pipelineName, {
        formalParameter "bambooPlanKey", type: "entry", defaultValue: "MIINSB-BFFBCSB"
        formalParameter "environment", type: "entry", defaultValue: "Sandbox"

        stage "Stage 1", {
            task "Tarea 1 - getDeploymentList", {
                taskType = 'PROCEDURE'
                subprocedure = 'Procedimiento'
                actualParameter = [
                    'bambooPlanKey': '$[bambooPlanKey]',
                ]               
            }

            task "Tarea 2 - Seleccionar Deployment", {
                taskType = 'MANUAL'
                formalParameter 'DeploymentName', defaultValue: null, {
                    propertyReference = '$[/myPipeline/Deployment]'
                    required = '1'
                    type = 'select'
                }    
            }

            task "Tarea 3 - Imprimir deploymentProjectId", {
                taskType = 'COMMAND'
                subprocedure = 'RunCommand'
                subpluginKey = 'EC-Core'
                enabled = 0               
                actualParameter = [
                    'commandToRun': 'echo $[/myPipelineRuntime/stages/Stage 1/tasks/Tarea 2 - Seleccionar Deployment/DeploymentName]',
                ] 
            }

            task "Tarea 4 - Desplegar" , {
                taskType = 'PROCEDURE'
                subprocedure = 'Procedimiento2'
                actualParameter = [
                    'deploymentProjectId': '$[/myPipelineRuntime/stages/Stage 1/tasks/Tarea 2 - Seleccionar Deployment/DeploymentName]',
                    'environment': '$[environment]',
                    'bambooPlanKey': '$[bambooPlanKey]'
                ]               
            }
        }

        property 'Deployment', {
        }
    }


}