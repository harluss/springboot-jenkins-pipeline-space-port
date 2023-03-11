pipeline {
    agent any

    tools {
       maven 'MAVEN'
       jdk 'JDK'
    }
    environment {
       FOO = 'bar'
    }
    options {
        buildDiscarder(logRotator(numToKeepStr: '3'))
        timeout(time: 5, unit: 'MINUTES')
        disableConcurrentBuilds()
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
            environment {
                SONAR_VER = '3.9.0.2155'
            }
            steps {
                withSonarQubeEnv(installationName: 'SONARQUBE_SERVER') {
                    sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:${SONAR_VER}:sonar'
                }
            }
        }
        stage('Build Docker Image') {
            when {
                anyOf {
                    changeRequest()
                    branch 'main'
                }
            }
            steps {
                echo 'Docker Image KABOOM!'
            }
        }
        stage('Deploy') {
            when {
                branch 'main'
            }
            steps {
                echo 'Deploy SKADOOSH!'
            }
        }
    }
    post {
        success {
            echo 'Build passed'
        }
        failure {
            error 'Build failed'
        }
    }
}
