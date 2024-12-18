pipeline {
    agent any
    environment {
        MAVEN_HOME = tool 'maven'
    }
    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/OU-chaima/LibraryManager.git'
            }
        }
        stage('Build') {
            steps {
                sh '${MAVEN_HOME}/bin/mvn clean compile'
            }
        }
        stage('Test') {
            steps {
                sh '${MAVEN_HOME}/bin/mvn test'
            }
        }
        stage('Quality Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh '${MAVEN_HOME}/bin/mvn sonar:sonar'
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
