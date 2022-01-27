/*
	forma de invocación de método call:
	def ejecucion = load 'script.groovy'
	ejecucion.call()
*/

def call(){
  
  stage('Build & Unit Test'){
		STAGE = env.STAGE_NAME
		bat "gradle clean build"
	}
	
	stage('Sonar'){
		STAGE = env.STAGE_NAME
		def scannerHome = tool 'sonar-scanner';
        withSonarQubeEnv('sonar-server'){
			bat "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.sources=src -Dsonar.java.binaries=build" }
	}
	
	stage('Run'){
		STAGE = env.STAGE_NAME
		bat "start /b gradle bootRun"
		sleep 10
	}
	
	stage('Test'){
		STAGE = env.STAGE_NAME
		bat "start chrome http://localhost:8081/rest/mscovid/test?msg=testing"
	}
	
	stage('Nexus'){
		STAGE = env.STAGE_NAME
		nexusPublisher nexusInstanceId: 'nexus-server', nexusRepositoryId: 'test-gradle', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: 'C:/Users/psantacruz/Documents/diplomado-devops/ejemplo-gradle2/ejemplo-gradle/build/DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '0.0.1']]]
	}

}

return this;