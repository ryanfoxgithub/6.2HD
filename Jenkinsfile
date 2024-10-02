pipeline {
    agent any

    environment {
        EB_APP_NAME = 'vulnelastic'
        EB_ENV_NAME = 'Vulnelastic-env'
        REGION = 'us-east-1'
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
        stage('Deploy to Staging') {
            steps {
                echo 'Deploying to AWS Elastic Beanstalk...'
                script {
                    // Example of a Windows compatible command
                    bat "echo Deploying to AWS Elastic Beanstalk..."
                    bat "aws elasticbeanstalk update-environment --environment-name ${EB_ENV_NAME} --version-label 'your-version-label'"
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
