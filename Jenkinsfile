pipeline {
    agent any
    environment {
        MAVEN_HOME = tool 'maven'
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/OU-chaima/LibraryManager.git'
            }
        }
        stage('Inject .env') {
            steps {
                withCredentials([file(credentialsId: 'ENV_FILE_CRED', variable: 'ENV_FILE')]) {
                    sh '''
                    cp $ENV_FILE .env
                    echo ".env file successfully copied!"
                    '''
                }
            }
        }
        stage('Build') {
            steps {
                sh '"${MAVEN_HOME}/bin/mvn" clean compile'
            }
        }
        stage('Test') {
            steps {
                sh '"${MAVEN_HOME}/bin/mvn" test'
            }
        }
        stage('Quality Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh '"${MAVEN_HOME}/bin/mvn" sonar:sonar'
                }
            }
        }
        stage('Deploy') {
            steps {
                echo 'Déploiement simulé réussi'
            }
        }
    }
    post {
            success {
                script {
                    emailext(
                        subject: "Build SUCCESS: ${env.JOB_NAME} ${env.BUILD_NUMBER}",
                        body: "Good news! The build for ${env.JOB_NAME} completed successfully.",
                        to: 'ouazzanadam24@gmail.com',
                        replyTo: 'no-reply@yourdomain.com',
                        mimeType: 'text/html'
                    )
                }
            }
            failure {
                script {
                    emailext(
                        subject: "Build FAILED: ${env.JOB_NAME} ${env.BUILD_NUMBER}",
                        body: "Unfortunately, the build for ${env.JOB_NAME} has failed. Please check Jenkins logs for more details.",
                        to: 'ouazzanadam24@gmail.com',
                        replyTo: 'no-reply@yourdomain.com',
                        mimeType: 'text/html'
                    )
                }
            }
    }
}
