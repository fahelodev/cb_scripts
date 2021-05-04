// Customizable values ------------------
// Application Name
def ProjectName = "kchaug"
def AppName = "App kchaug"
def Envs = ["Sandbox", "Dev", "QA", "Beta", "Prod", "BF", "DR"]
// Components Name
def AppCompTiers = ["bff-mi-kchaug","ms-ban-kchaug"]
// data Components
def AppEnvTiers = ["Deployment":"Tier 1"]
def processName = "Deploy"
// Artifact group id
def ArtifactRoot = "cl.kchaug.mikchaug"
def EnvTiers = AppEnvTiers.values()
def AppTiers = AppEnvTiers.keySet()
// Create new -------------------------------
def ArtifactVersions = []
project ProjectName, {
  // Create Environments, Tiers and Resources
  Envs.each { Env ->
    environment environmentName: Env, {
      EnvTiers.each() { Tier ->
        def res = "${Env}"
        environmentTier Tier, {
          // create and add resource to the Tier
          resource resourceName: res, hostName : "localhost"
        }
      }
    }
  } 
 // Environments
  application AppName, {
    AppTiers.each() { Tier ->
      applicationTier Tier, {
        AppCompTiers.each() { CompTier ->
          CompName = "${CompTier}"
          def ArtifactVersion = "1.35"
          def ArtifactName = ArtifactRoot + ':' + CompName
          ArtifactVersions << [artifactName: ArtifactName, artifactVersion: ArtifactVersion]
          // Create artifact
          artifact groupId: ArtifactRoot, artifactKey: CompName
          // process
          applicationName = AppName
          component CompTier, pluginName: null, {
            applicationName = AppName
            reference = '1'
            sourceComponentName = CompTier
            sourceProjectName = ProjectName
          }
          CompName = CompTier
        }
      }
    }
    // Proceso General de ejecución
    process processName, {
      processStep  "Start", {
        actualParameter: [
          commandToRun: 'echo'
        ]
        applicationTierName = 'Deployment'
        processStepType:'command'
        subprocedure: 'RunCommand'
        subproject: '/plugins/EC-Core/project'

      }
      AppCompTiers.each() { CompTier ->
        formalParameter CompTier, defaultValue: 'false', {
          checkedValue = 'true'
          label = CompTier
          orderIndex = '1'
          required = '1'
          type = 'checkbox'
          uncheckedValue = 'false'
        }
        processStep CompTier, {
          applicationTierName = 'Deployment'
          processStepType = "process"
          subcomponent = CompTier
          subcomponentApplicationName = AppName
          subcomponentProcess = processName
        }
        processDependency 'Start', targetProcessStepName: CompTier, {
          branchCondition = '$[/javascript (\'$[/myJob/'+CompTier+']\' == \'true\')]'
          branchConditionName = "If " + CompTier
          branchConditionType = "CUSTOM"
          branchType = 'ALWAYS'
        }
      }	
    }
    // Create Application-Environment mappings
    Envs.each { Env ->
      tierMap "$AppName-$Env",
        environmentProjectName: projectName,
        environmentName: Env,
        tierMapping: AppEnvTiers
    }
  }
}