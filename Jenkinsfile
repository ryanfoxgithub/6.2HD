pipeline {
    agent any

    environment {
        EB_APP_NAME = 'vulnelastic'
        EB_ENV_NAME = 'Vulnelastic-env'
        AWS_ACCESS_KEY_ID = credentials('aws-access-key')
        AWS_SECRET_ACCESS_KEY = credentials('aws-secret-key')
        REGION = 'us-east-1'
        APPLICATION_NAME = 'YourApplicationName'
        ENVIRONMENT_NAME = 'YourEnvironmentName'
        S3_BUCKET = 'your-s3-bucket-for-deployment'
        VERSION_LABEL = "v${BUILD_NUMBER}"
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build') {
            steps {
                echo "Building Code..."
                bat 'mvn clean package'
            }
        }
        stage('Test') {
            steps {
                echo "Running Tests..."
                bat 'mvn -X test'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: '**/test-results/*.xml'
                }
                success {
                    echo "Tests Passed."
                }
                failure {
                    emailext to: 'mcubed132@gmail.com',
                        subject: "Unit and Integration Tests Failed",
                        body: "One or more tests have failed.",
                        attachLog: true, 
                        compressLog: true 
                }
            }
        }
        stage('Code Quality Analysis') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    bat 'mvn sonar:sonar'
                }
            }
        }
        stage('packaging'){
            steps {
                echo 'this is packaging'
                bat 'powershell.exe -Command "Compress-Archive -Path * -DestinationPath output.zip"'
            }
        }
        stage('Deploy to Staging') {
            steps {
                withCredentials([[
                    $class: 'AmazonWebServicesCredentialsBinding', 
                    credentialsId: 'AKIAVPEYWAFQSAQV5SXM'
                ]]) {
                    bat "echo Deploying using AWS Access Key ID %AWS_ACCESS_KEY_ID%"
                    // Use AWS CLI or other AWS tools here
                }
            }
        }
        stage('Release') {
            steps {
                echo "Releasing the application..."
                // Assuming release commands are added here
            }
        }
    }
}
