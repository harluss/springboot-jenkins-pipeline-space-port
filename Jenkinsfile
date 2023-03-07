pipeline {
    agent any

    tools {
       maven 'MVN_3_8_5'
       jdk 'JDK_17'
    }
    environment {
      FOO = 'bar'
    }
    options {
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
        success {
            echo 'test passed'
        }
        failure {
            error 'test failed'
        }
    }
}
