pipeline {
    agent any

    environment {
        IMAGE_NAME = "todoapp"
        CONTAINER_NAME = "todoapp_container"
        VPS_USER = "root"
        VPS_IP = "36.50.135.128"
        REMOTE_APP_PATH = "/home/root/todoapp"
    }

    stages {
        stage('Check Docker') {
            steps {
                sh 'docker --version && docker ps'
            }
        }
        stage('Clone Repository') {
            steps {
                git branch: 'master', url: 'https://github.com/hachnv8/todoapp1.git'
            }
        }

        stage('Build JAR') {
            steps {
                sh 'chmod +x mvnw'
                sh './mvnw clean package -DskipTests'

            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $IMAGE_NAME .'
            }
        }

        stage('Push and Deploy to VPS') {
            steps {
                // Optional: Save image to tar, copy, and run on VPS
                sh """
                  ssh -i ~/.ssh/id_rsa $VPS_USER@$VPS_IP "mkdir -p $REMOTE_APP_PATH"
                  docker save $IMAGE_NAME > todoapp.tar
                  scp todoapp.tar $VPS_USER@$VPS_IP:$REMOTE_APP_PATH/
                  ssh $VPS_USER@$VPS_IP 'docker load < $REMOTE_APP_PATH/todoapp.tar && docker stop $CONTAINER_NAME || true && docker rm $CONTAINER_NAME || true && docker run -d --name $CONTAINER_NAME -p 1212:1212 $IMAGE_NAME'
                """
            }
        }
    }
}
