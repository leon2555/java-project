pipeline {
	agent none
	
	stages {
		stage('Unit test') {
			agent {
				label 'apache'
			}
			steps {
				sh 'ant -f test.xml -v'
				junit 'reports/result.xml'
			}
		}
		stage('build') {
			agent {
				label 'apache'
			}
			steps {
				sh 'ant -f build.xml -v'
			}
		}
		stage('deploy') {
			agent {
				label 'apache'
			}
			steps {
				sh "cp dist/rectangle_${env.BUILD_NUMBER}.jar /var/www/html/rectangles/all/"
			}
		}
		stage("Running on CentOS") {
			agent {
				label 'CentOS'
			}
			steps {
				sh "wget https://leon25551.mylabserver.com/rectangle/all/rectablge_${env.BUILD_NUMBER}.jar"
				sh "java -jar rectangle_${env.BUILD_NUMBER}.jar 3 4"
			}
		}
	}
	post {
		always {
			archiveArtifacts artifacts: 'dist/*.jar', fingerprint: true
		}
	}
}