pipeline {
    agent any

    environment {
        EB_APP_NAME = 'vulnelastic'
        EB_ENV_NAME = 'Vulnelastic-env'
        REGION = 'us-east-1'
        AWS_ACCESS_KEY_ID = credentials('AKIAVPEYWAFQSAQV5SXM')  // The ID you gave your AWS credentials
        AWS_SECRET_ACCESS_KEY = credentials('lD8OryxRt7qZAoefMgRu7bG1dj1N03udffXcFjyr')
        VERSION_LABEL = "${BUILD_NUMBER}"  // Using Jenkins BUILD_NUMBER as the version label
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
                bat """
                    echo Deploying to AWS Elastic Beanstalk...
                    aws elasticbeanstalk update-environment --environment-name Vulnelastic-env --version-label '${env.VERSION_LABEL}' --region us-east-1
                """
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
