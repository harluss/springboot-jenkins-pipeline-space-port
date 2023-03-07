pipeline {
    agent any

    tools {
       maven 'MAVEN'
       jdk 'JDK'
    }
    environment {
      FOO = 'bar'
      SONAR_VER = '3.9.0.2155'
    }
    options {
        timeout(time: 5, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }

    stages {
        stage('Build') {
            steps {
                sh 'mvn clean verify'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml, **/target/failsafe-reports/*.xml'
                }
            }
        }
        stage('SonarQube Scan') {
            steps {
                withSonarQubeEnv(installationName: 'SONARQUBE_SERVER') {
                    sh 'mvn clean org.sonarsource.scanner.maven:sonar-maven-plugin:${SONAR_VER}:sonar'
                }
            }
        }
        stage('KABOOM PR') {
            when {
                changeRequest()
            }
            steps {
                echo 'KABOOM!'
                echo '${env.FOO} bar'
                echo '${FOO} bar 2'
            }
        }
        stage('SKADOOSH MAIN') {
            when {
                branch 'main'
            }
            steps {
                echo 'SKADOOSH!'
            }
        }
    }
    post {
        failure {
            error 'test failed'
        }
    }
}
