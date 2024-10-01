pipeline {
    agent any

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
                withSonarQubeEnv('Your SonarQube Server Name') {
                    script {
                        // Path to your sonar-project.properties file or inline configuration
                        def sonarScannerHome = tool 'SonarQube Scanner Name';
                        bat "${sonarScannerHome}\\bin\\sonar-scanner.bat"
                    }
                }
            }
        }
        stage('Deploy to Staging') {
            steps {
                echo "Deploying to Staging..."
                // Assuming deployment commands are added here
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
