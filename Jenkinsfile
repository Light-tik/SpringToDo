pipeline {
    agent any

    tools {
            jdk 'JDK21'
            maven 'maven'
        }

    environment {
        DOCKER_IMAGE = "kuzminova/todo-service"
        DOCKER_HUB_CREDENTIALS = credentials('dockerhub-credentials')
    }

    options {
        skipDefaultCheckout(false)
        timestamps()
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            when {
                anyOf {
                    branch 'dev'
                    changeRequest()
                }
            }
            agent any

            steps {
                bat 'mvn clean package'
            }
        }

        stage('Build & Push Docker Image') {
            when {
                branch 'master'
            }

            steps {
                script {
                    bat 'docker --version'
                    bat 'echo %DOCKER_HUB_CREDENTIALS_PSW% | docker login -u %DOCKER_HUB_CREDENTIALS_USR% --password-stdin'
                    bat "docker build -t %DOCKER_IMAGE%:latest ."
                    bat "docker push %DOCKER_IMAGE%:latest"
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline finished.'
        }
        failure {
            echo 'Build failed!'
        }
    }
}
