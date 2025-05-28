pipeline {
    agent any

     tools {
            maven 'Maven_Tool' // Tên đúng với cấu hình ở bước trên
     }

    environment {
        IMAGE_NAME = "todoapp"
        CONTAINER_NAME_1 = "todoapp_instance1_container"
        CONTAINER_NAME_2 = "todoapp_instance2_container"
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
                sh 'mv target/todo-app-0.0.1-SNAPSHOT.jar target/todoapp.jar'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $IMAGE_NAME .'
            }
        }

        stage('Push and Deploy to VPS') {
            steps {
                sshagent(['vps-ssh-key']) {
                    sh """
                        ssh -o StrictHostKeyChecking=no $VPS_USER@$VPS_IP "mkdir -p $REMOTE_APP_PATH"
                        docker save $IMAGE_NAME > todoapp.tar
                        scp -o StrictHostKeyChecking=no todoapp.tar $VPS_USER@$VPS_IP:$REMOTE_APP_PATH/
                        ssh -o StrictHostKeyChecking=no $VPS_USER@$VPS_IP '
                            docker load < $REMOTE_APP_PATH/todoapp.tar &&

                                docker stop todoapp_instance1 || true &&
                                docker rm todoapp_instance1 || true &&
                                docker rm -f todoapp_instance1_container || true &&
                                docker run -d --name $CONTAINER_NAME_1 --restart always -p 1212:1212 $IMAGE_NAME &&

                                docker stop todoapp_instance2 || true &&
                                docker rm todoapp_instance2 || true &&
                                docker rm -f todoapp_instance2_container || true &&
                                docker run -d --name $CONTAINER_NAME_2 --restart always -p 1213:1212 $IMAGE_NAME
                        '
                    """
                }
            }
        }
    }
}
