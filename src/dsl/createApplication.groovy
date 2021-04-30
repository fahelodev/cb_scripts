
// Customizable values ------------------
// Application Name
def ProjectName = "Mi Banco"
def AppName = "Mi Inversion"
def Envs = ["Sandbox","Dev","QA","Beta","PROD",'BF',"DR"]
// Components Name
def AppCompTiers = ["bff-mi-inversion","ms-ban-productos-fondos-mutuos","ms-ban-servicios-inversiones-patrimonio","ms-clientes",
"ms-clientes-persona-perfil","ms-movil-controlador-version","ms-productos-divisas-banchile","ms-productos-tarjeta-credito",
"ms-seguridad-autorizador","ms-seguridad-movil","ms-seguridad-movil-autorizador","ms-seguridad-movil-enrolamiento",
"ms-seguridad-movil-sesion","ms-servicios-inversiones-dap","ms-utilidades-calendario","ms-utilidades-indicadores",
"ms-utilidades-journal-canales-remotos","ms-utilidades-notificacion-sms","ms-utilidades-notificaciones","ms-utilidades-sucursales"]
// data Components

def dataPlanIdCompTiers = ["bff-mi-banco;projectKey;projectPlanKey;"]
// Application-Environment tier mapping ["apptier1":"envtier1", "apptier2":"envtier2" ...]
// The values will be used to create application and environment tier names and their mappings
def AppEnvTiers = ["Deployment":"Tier 1"]
// Artifact group id
def ArtifactRoot = "cl.bancochile.mibanco"
// Clean up from prior runs ------------------
def EnvTiers = AppEnvTiers.values()
def AppTiers = AppEnvTiers.keySet()
// Remove old application model
deleteApplication (projectName: ProjectName, applicationName: AppName)
// Remove old Environment models
/*
Envs.each { Env ->
  AppTiers.each() { Tier ->
    def res = "${Env}_${Tier}"
    deleteResource resourceName: res
  }
  deleteEnvironment(projectName: ProjectName, environmentName: Env)
}
*/
// Create new -------------------------------
def ArtifactVersions = []
project ProjectName, {
  // Create Environments, Tiers and Resources
  Envs.each { Env ->
    environment environmentName: Env, {
      EnvTiers.each() { Tier ->
        def res = "${Env}"//_${Tier}"
        environmentTier Tier, {
          // create and add resource to the Tier
          resource resourceName: res, hostName : "localhost"
        }
      }
    }
  } // Environments
  application AppName, {
    AppTiers.each() { Tier ->
      applicationTier Tier, {
        def CompName = 'Dummy'
        AppCompTiers.each() { CompTier ->
          CompName = "${CompTier}"
          def ArtifactVersion = "1.35"
          def ArtifactName = ArtifactRoot + ':' + CompName
          ArtifactVersions << [artifactName: ArtifactName, artifactVersion: ArtifactVersion]
          // Create artifact
          artifact groupId: ArtifactRoot, artifactKey: CompName
          component CompName, pluginKey: "EC-Artifact", {
            ec_content_details.with {
              pluginProjectName = "EC-Artifact"
              pluginProcedure = "Retrieve"
              artifactName = ArtifactName
              filterList = ""
              overwrite = "update"
              versionRange = ArtifactVersion
              artifactVersionLocationProperty = "/myJob/retrievedArtifactVersions/\$" + "[assignedResourceName]"
            }
            process "Deploy", processType: "DEPLOY", componentApplicationName: AppName,{
              processStep "Start",
                processStepType: "command",
                subprocedure: "RunCommand",
                errorHandling: "abortJob",
                subproject: "/plugins/EC-Core/project",
                applicationName: null,
                applicationTierName: null,
                dependencyJoinType: "and",
                actualParameter: [
                  commandToRun: 'echo'
                ]
              processStep "Run Plan SB",
                processStepType: "command",
                subprocedure: "RunPlan",
                errorHandling: "abortJob",
                subproject: "/plugins/EC-Bamboo/project",
                applicationName: null,
                applicationTierName: null,
                dependencyJoinType: "and",
                actualParameter: [
                  additionalBuildVariables: '',
                  config: 'Bamboo OnPremise',
                  customRevision: '',
                  planKey: '.',
                  projectKey: '.',
                  resultFormat: 'json',
                  resultPropertySheet: '/myJob/runResult',
                  waitForBuild: '1',
                  waitTimeout: '500',
                ]
              processStep "Run Plan Dev",
                processStepType: "command",
                subprocedure: "RunPlan",
                errorHandling: "abortJob",
                subproject: "/plugins/EC-Bamboo/project",
                applicationName: null,
                applicationTierName: null,
                dependencyJoinType: "and",
                actualParameter: [
                  additionalBuildVariables: '',
                  config: 'Bamboo OnPremise',
                  customRevision: '',
                  planKey: 'MSSEAU',
                  projectKey: 'MSOCI',
                  resultFormat: 'json',
                  resultPropertySheet: '/myJob/runResult',
                  waitForBuild: '1',
                  waitTimeout: '500',
                ]
              processStep "Deploy SB",
                processStepType: "procedure",
                subprocedure: "Deployment",
                errorHandling: "abortJob",
                subproject: "Mi Inversión",
                applicationName: null,
                applicationTierName: null,
                dependencyJoinType: "and"
              processStep "Deploy Dev",
                processStepType: "procedure",
                subprocedure: "Deployment",
                errorHandling: "abortJob",
                subproject: "Mi Inversión",
                applicationName: null,
                applicationTierName: null,
                dependencyJoinType: "and"
              processStep "Deploy",
                processStepType: "procedure",
                subprocedure: "Deployment",
                errorHandling: "abortJob",
                subproject: "Mi Inversión",
                applicationName: null,
                applicationTierName: null,
                dependencyJoinType: "and"
              processStep "Create Release SB",
                processStepType: "command",
                subprocedure: "CreateRelease",
                errorHandling: "abortJob",
                subproject: "/plugins/EC-Bamboo/project",
                applicationName: null,
                applicationTierName: null,
                dependencyJoinType: "and",
                actualParameter: [
                  config: 'Bamboo OnPremise',
                  deploymentProjectName: '.',
                  planBuildKey: '$[/javascript JSON.parse(myJob.runResult).key]',
                  releaseName: '',
                  requestReleaseName: '1',
                  resultFormat: 'json',
                  resultPropertySheet: '/myJob/release',
                ]
              processStep "Create Release Dev",
                processStepType: "command",
                subprocedure: "CreateRelease",
                errorHandling: "abortJob",
                subproject: "/plugins/EC-Bamboo/project",
                applicationName: null,
                applicationTierName: null,
                dependencyJoinType: "and",
                actualParameter: [
                  config: 'Bamboo OnPremise',
                  deploymentProjectName: 'ms-seguridad-autorizador-dply',
                  planBuildKey: '$[/javascript JSON.parse(myJob.runResult).key]',
                  releaseName: '',
                  requestReleaseName: '1',
                  resultFormat: 'json',
                  resultPropertySheet: '/myJob/release',
                ]
              processDependency 'Start', targetProcessStepName: 'Run Plan SB', {
                branchCondition = ' (\'$[/myResource/name]\' ==\'Sandbox\')'
                branchConditionName = 'if Sandbox'
                branchConditionType = 'CUSTOM'
                branchType = 'ALWAYS'
              }
              processDependency 'Start', targetProcessStepName: 'Deploy', {
                branchCondition = '\'$[/myResource/name]\' != \'Dev\''
                branchConditionName = 'if'
                branchConditionType = 'CUSTOM'
                branchType = 'ALWAYS'
              }
              processDependency 'Start', targetProcessStepName: 'Run Plan Dev', {
                branchCondition = '\'$[/myResource/name]\' == \'Dev\''
                branchConditionName = 'If Dev'
                branchConditionType = 'CUSTOM'
                branchType = 'ALWAYS'
              }
              processDependency 'Run Plan SB', targetProcessStepName: 'Create Release SB', {
                branchCondition = null
                branchConditionName = null
                branchConditionType = null
                branchType = 'ALWAYS'
              }
              processDependency 'Run Plan Dev', targetProcessStepName: 'Create Release Dev', {
                branchCondition = null
                branchConditionName = null
                branchConditionType = null
                branchType = 'ALWAYS'
              }
              processDependency 'Create Release SB', targetProcessStepName: 'Deploy SB', {
                branchCondition = null
                branchConditionName = null
                branchConditionType = null
                branchType = 'ALWAYS'
              }
              processDependency 'Create Release Dev', targetProcessStepName: 'Deploy Dev', {
                branchCondition = null
                branchConditionName = null
                branchConditionType = null
                branchType = 'ALWAYS'
              }
            }
          } // process
        } // Components
        process "Deploy",{
          AppCompTiers.each() { CompTier ->
            formalParameter "${CompTier}", type: "checkbox", label: "${CompTier}", required: "1"
          }
          processStep  "Start load Configmaps & Secrets",
            actionLabelText: null
          actualParameter: [
            commandToRun: 'echo'
          ]
          afterLastRetry: null
          allowSkip: null
          alwaysRun: '0'
          applicationTierName: 'Deployment'
          componentRollback: null
          dependencyJoinType: 'and'
          disableFailure: null
          emailConfigName: null
          errorHandling: 'abortJob'
          instruction: null
          notificationEnabled: null
          notificationTemplate: null
          processStepType:'command'
          retryCount: null
          retryInterval: null
          retryType: null
          rollbackSnapshot: null
          rollbackType: null
          rollbackUndeployProcess: null
          skipRollbackIfUndeployFails: null
          smartRollback: null
          subcomponent: null
          subcomponentApplicationName: null
          subcomponentProcess: null
          subprocedure: 'RunCommand'
          subproject: '/plugins/EC-Core/project'
          subservice: null
          subserviceProcess: null
          timeLimitUnits: null
          useUtilityResource: '0'
          utilityResourceName: null
          workingDirectory: null
          workspaceName: null
          property 'ec_deploy',{
            // Custom properties
            ec_notifierStatus: '0'
          }
          AppCompTiers.each() { CompTier ->
            CompName = "${CompTier}"
            processStep "${CompTier}", 
              processStepType: 'process',
              componentName: null,
              componentApplicationName: AppName,
              errorHandling: 'failProcedure',
              subcomponent: CompName,
              subcomponentApplicationName: AppName,
              subcomponentProcess: 'Deploy'
            processDependency 'Start load Configmaps & Secrets', targetProcessStepName: "${CompTier}", {
              branchCondition = null//'$[/javascript (\'$[/myJob/ms-ban-servicios-inversiones-patrimonio]\' == \'true\') ]'
              branchConditionName = null//'If need'
              branchConditionType = null//'CUSTOM'
              branchType = 'ALWAYS'
            }

			//Sunil Clarificar???
            property 'ec_customEditorData', {
              // Custom properties
              property 'parameters', {
                // Custom properties
                property "${CompTier}", {
                  // Custom properties
                  property 'checkedValue', value: 'true', {
                    expandable = '1'
                    suppressValueTracking = '0'
                  }
                  formType = 'standard'
                  property 'uncheckedValue', value: 'false', {
                    expandable = '1'
                    suppressValueTracking = '0'
                  }
                }
              }
            }
          }	
        } // process
      } // applicationTier
    } // each Tier
    // Create Application-Environment mappings
    Envs.each { Env ->
      tierMap "$AppName-$Env",
        environmentProjectName: projectName,
        environmentName: Env,
        tierMapping: AppEnvTiers
    } // each Env
  } // Applications
} // project
//Sunil please help ---
// Create publishArtifact procedure 
project ProjectName, {
  procedure "Publish Artifact Versions", {
    formalParameter "artifactName", type: "entry", required: "1"
    formalParameter "artifactVersion", type: "entry", required: "1"
    formalParameter "fileName", type: "entry", required: "1"
    formalParameter "fileContent", type: "textarea", required: "1"
    step "Create File",
      subproject: "/plugins/EC-FileOps/project",
      subprocedure: "AddTextToFile",
      actualParameter: [
        Path: '$' + "[fileName]",
        Content: '$' + "[fileContent]",
        AddNewLine: "0",
        Append: "0"
      ]
    step "Publish Artifact",
      subproject: "/plugins/EC-Artifact/project",
      subprocedure: "Publish",
      actualParameter: [
        artifactName: '$' + "[artifactName]",
        artifactVersionVersion: '$' + "[artifactVersion]",
        includePatterns: '$' + "[fileName]",
        repositoryName: "Default"
        //fromLocation:
      ]
  }
}
ArtifactVersions.each { ar ->
  // Create artifact version
  transaction {
    runProcedure procedureName: "Publish Artifact Versions", projectName: ProjectName,
      actualParameter: [
        artifactName: ar.artifactName,
        fileContent: "echo Installing " + ar.artifactName,
        fileName: "installer.sh",
        artifactVersion: ar.artifactVersion
      ]
  }
}
