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
        stage('Setup .env') {
            steps {
                withCredentials([
                    string(credentialsId: 'DB_URL_CRED', variable: 'DB_URL'),
                    string(credentialsId: 'DB_USER_CRED', variable: 'DB_USER'),
                    string(credentialsId: 'DB_PASSWORD_CRED', variable: 'DB_PASSWORD')
                ]) {
                    sh '''
                    echo "DB_URL=${DB_URL}" > .env
                    echo "DB_USER=${DB_USER}" >> .env
                    echo "DB_PASSWORD=${DB_PASSWORD}" >> .env
                    '''
                    echo ".env file generated successfully!"
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
            emailext to: 'ouazzanchaimae@gmail.com',
                subject: 'Build Success',
                body: 'Le build a été complété avec succès.'
        }
        failure {
            emailext to: 'ouazzanchaimae@gmail.com',
                subject: 'Build Failed',
                body: 'Le build a échoué.'
        }
    }
}
