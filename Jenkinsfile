pipeline {
    agent any

    tools {
       maven 'MAVEN'
       jdk 'JDK'
    }
    environment {
       APP_NAME = readMavenPom().getArtifactId()
       DOCKERHUB_USER = 'harluss'
       IMAGE_NAME = '${DOCKERHUB_USER}/${APP_NAME}'
       SONAR_VER = '3.9.0.2155'
    }
    options {
        buildDiscarder(logRotator(numToKeepStr: '3'))
        disableConcurrentBuilds()
        timeout(time: 5, unit: 'MINUTES')
    }

    stages {
        stage('Build Project') {
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
                    sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:${SONAR_VER}:sonar'
                }
            }
        }
        stage('Build and Publish Docker Image') {
            when {
                anyOf {
                    changeRequest()
                    branch 'main'
                }
            }
            steps {
            // using latest tag only for demo purposes
                sh """
                  docker build -t ${IMAGE_NAME} .
                  docker push ${IMAGE_NAME}
                """
            }
        }
    }
}
