/*
	forma de invocación de método call:
	def ejecucion = load 'script.groovy'
	ejecucion.call()
*/

def call(String pipelineType){
	figlet 'Gradle'
	if (pipelineType == 'CI') {
		figlet 'Integracion Continua'

		if (params.Stage.contains('build')) {
		  stage('Build & Unit Test'){
				STAGE = env.STAGE_NAME
				figlet "${STAGE}"
				bat "gradle clean build"
			}
		} else { println 'No ha especificado ejecutar el Stage: BUILD' }
		
		if (params.Stage.contains('sonar')) {	
			stage('Sonar'){
				STAGE = env.STAGE_NAME
				figlet "${STAGE}"
				def scannerHome = tool 'sonar-scanner';
		        withSonarQubeEnv('sonar-server'){
					bat "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.sources=src -Dsonar.java.binaries=build" }
			}
		} else { println 'No ha especificado ejecutar el Stage: SONAR' }
			
		if (params.Stage.contains('run')) {	
			stage('Run'){
				STAGE = env.STAGE_NAME
				figlet "${STAGE}"
				bat "start /b gradle bootRun"
				sleep 10
			}
		} else { println 'No ha especificado ejecutar el Stage: RUN' }
		
		if (params.Stage.contains('test')) {	
			stage('Test'){
				STAGE = env.STAGE_NAME
				figlet "${STAGE}"
				bat "start chrome http://localhost:8081/rest/mscovid/test?msg=testing"
			}
		} else { println 'No ha especificado ejecutar el Stage: TEST' }

		if (params.Stage.contains('upload')) {	
			stage('UploadNexus') {
	            steps {
	                script {
	                    nexusPublisher nexusInstanceId: 'nexus-server', nexusRepositoryId: 'test-repo', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: 'C:/ProgramData/Jenkins/.jenkins/workspace/nexus-pipeline_feature-nexus/build/DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '0.0.1']]]
	                }
	            }
	        }
	  } else { println 'No ha especificado ejecutar el Stage: UPLOAD' }

	} else {
		figlet 'Delivery Continuo'

		if (params.Stage.contains('nexus')) {
			stage('DownloadNexus'){
				STAGE = env.STAGE_NAME
				figlet "${STAGE}"
				nexusPublisher nexusInstanceId: 'nexus-server', nexusRepositoryId: 'test-gradle', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: 'C:/Users/psantacruz/Documents/diplomado-devops/ejemplo-gradle2/ejemplo-gradle/build/DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '0.0.1']]]
			}
		} else { println 'No ha especificado ejecutar el Stage: NEXUS' }

		if (params.Stage.contains('run')) {	
			stage('RunSnapshotJar'){
				STAGE = env.STAGE_NAME
				figlet "${STAGE}"
				bat "start /b gradle bootRun"
				sleep 10
			}
		} else { println 'No ha especificado ejecutar el Stage: RUN' }

		if (params.Stage.contains('test')) {	
			stage('Test'){
				STAGE = env.STAGE_NAME
				figlet "${STAGE}"
				bat "start chrome http://localhost:8081/rest/mscovid/test?msg=testing"
			}
		} else { println 'No ha especificado ejecutar el Stage: TEST' }

		if (params.Stage.contains('upload')) {	
			stage('UploadNexus') {
	            steps {
	                script {
	                    nexusPublisher nexusInstanceId: 'nexus-server', nexusRepositoryId: 'test-repo', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: 'C:/ProgramData/Jenkins/.jenkins/workspace/nexus-pipeline_feature-nexus/build/DevOpsUsach2020-1.0.0.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '1.0.0']]]
	                }
	            }
	        }
	  } else { println 'No ha especificado ejecutar el Stage: UPLOAD' }
	}
}

return this;