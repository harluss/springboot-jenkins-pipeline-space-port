pipeline {
    agent any

    tools {
       maven 'MAVEN'
       jdk 'JDK'
    }
    environment {
       IMAGE_NAME = readMavenPom().getArtifactId()
//        IMAGE_VERSION = readMavenPom().getVersion()
       SONAR_VER = '3.9.0.2155'
    }
    options {
        buildDiscarder(logRotator(numToKeepStr: '3'))
        disableConcurrentBuilds()
        timeout(time: 5, unit: 'MINUTES')
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
                    sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:${SONAR_VER}:sonar'
                }
            }
        }
        stage('Build Docker Image') {
//             when {
//                 anyOf {
//                     changeRequest()
//                     branch 'main'
//                 }
//             }
            steps {
                echo '1 $BUILD_NUMBER'
                echo '2 ${BUILD_NUMBER}'
//                 sh """
//                   docker build -t ${IMAGE_NAME} .
//                   docker tag ${IMAGE_NAME} ${IMAGE_NAME}:${IMAGE_VERSION}
//                   docker push ${IMAGE_VERSION}
//                 """

                script {
                    dockerImage = docker.build('${IMAGE_NAME}')
                    dockerImage.push('latest')
                }
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
//     post {
//         success {
//             echo 'Build passed'
//         }
//         failure {
//             echo 'Sending notifications...'
//             error 'Build failed'
//         }
//     }
}
