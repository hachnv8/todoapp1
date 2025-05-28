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

        stage('Create nginx.conf') {
            steps {
                script {
                    writeFile file: 'nginx.conf', text: '''
                    events {}

                    http {
                        upstream todoapp {
                            server 127.0.0.1:1212;
                            server 127.0.0.1:1213;
                        }

                        server {
                            listen 80;

                            location / {
                                proxy_pass http://todoapp;
                                proxy_set_header Host $host;
                                proxy_set_header X-Real-IP $remote_addr;
                                proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                            }
                        }
                    }
                    '''
                }
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

        stage('Deploy NGINX') {
            steps {
                sshagent(['vps-ssh-key']) {
                    sh """
                        # Upload nginx.conf to VPS
                        scp -o StrictHostKeyChecking=no nginx.conf $VPS_USER@$VPS_IP:$REMOTE_APP_PATH/

                        ssh -o StrictHostKeyChecking=no $VPS_USER@$VPS_IP '
                            # Stop and remove old nginx container if exists
                            docker stop nginx || true &&
                            docker rm nginx || true &&

                            # Run new NGINX container with the uploaded config
                            docker run -d --name nginx \
                                -v $REMOTE_APP_PATH/nginx.conf:/etc/nginx/nginx.conf:ro \
                                -p 80:80 \
                                --restart always \
                                nginx
                        '
                    """
                }
            }
        }

    }
}
