pipeline {
	agent none
	
	environment {
		MAJOR_VERSION = 1
	}

	stages {
		stage('Say Hello') {
			agent any
			steps {
				sayhello 'Awsome Leon'
			}
		}
		stage('Git Information') {
			agent any
			steps {
				echo "my branch ${env.BRANCH_NAME}"
				script {
					def myLib = new leonorg.git.gitstuff();
					echo "My commit: ${myLib.gitCommit("${env.WORKSPACE}/.git")}"
				}
			}
		}
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
			post {
				success {
					archiveArtifacts artifacts: 'dist/*.jar', fingerprint: true
				}
			}
		}
		stage('deploy') {
			agent {
				label 'apache'
			}
			steps {
				sh "if ![ -d '/var/www/html/rectanlges/all/${env.BRANCH_NAME}' ]; then mkdir /var/www/html/rectangles/all/${env.BRANCH_NAME}; fi"
				sh "cp dist/rectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar /var/www/html/rectangles/all/${env.BRANCH_NAME}"
			}
		}
		stage("Running on CentOS") {
			agent {
				label 'CentOS'
			}
			steps {
				sh "wget http://leon25551.mylabserver.com/rectangles/all/${env.BRANCH_NAME}/rectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar"
				sh "java -jar rectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar 3 4"
			}
		}
		stage("Test on Debian") {
			agent {
				docker 'openjdk:8u121'
 			}
 			steps {
 				sh "wget http://leon25551.mylabserver.com/rectangles/all/${env.BRANCH_NAME}/rectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar"
 				sh "java -jar rectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar 3 4"
 			}
		}
		stage("Promote to green") {
			agent {
				label 'apache'
			}
			when {
				branch 'master'
			}
			steps {
				sh "cp /var/www/html/rectangles/all/${env.BRANCH_NAME}/rectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar /var/www/html/rectangles/green/rectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar"
			}
		}
		stage ("promote development to master") {
			agent {
				label 'apache'
			}
			when {
				branch 'development'
			}
			steps {
				echo "Stashing Any Local Changes"
        		sh 'git stash'
        		echo "Checking Out Development Branch"
        		sh 'git checkout development'
        		sh 'git pull origin'
        		echo 'Checking Out Master Branch'
        		sh 'git pull origin'
        		sh 'git checkout master'
        		echo 'Merging Development into Master Branch'
        		sh 'git merge development'
        		echo 'Pushing to Origin Master'
        		sh 'git push origin master'
        		echo 'Tagging the release'
        		sh "git tag rectangle-${env.MAJOR_VERSION}.${env.BUILD_NUMBER}"
        		sh "git push origin rectangle-${env.MAJOR_VERSION}.${env.BUILD_NUMBER}"
			}
			post {
				success {
					emailext(
						subject: "${env.JOB_NAME} [${env.BUILD_NUMBER}] Development Promoted to master!",
						body: """<p>'${env.JOB_NAME} [${env.BUILD_NUMBER}]' Development Promoted to master!":</p>
	        			<p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
	        			to: "leon@tr8nhub.com"
					)
				}
			}
		}
	}

	post {
		failure {
			emailext(
				subject: "${env.JOB_NAME} [${env.BUILD_NUMBER}] Failed!",
				body: """<p>'${env.JOB_NAME} [${env.BUILD_NUMBER}]' Failed!":</p>
        		<p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
        		to: "leon@tr8nhub.com"
			)
		}
	}
}