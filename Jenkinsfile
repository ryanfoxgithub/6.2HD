 pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build') { // Stage 1
            steps {
                //Maven ---> https://maven.apache.org/
                //test update for Jenkins
                echo "Building Code..."
                sleep 15
                bat 'mvn clean package'
                //echo "Building Automation Tool Maven completed build"
            }
        }
        stage('Test') { // Stage 2
            steps {
                //JUnit ---> https://junit.org/junit5/
                echo "Test Stage.."
                //echo "Launching JUnit"
                //echo "JUnit analysis..."
                sleep 5 // Sleeps for 5 seconds to 'process'
                bat 'mvn test'
                
                //echo "Unit and integration is up to standard, JUnit finished"
            }
            // Post running of steps, do this for a successful or failure of a build
            post {
                success{
                    junit "${env.BASE_PATH}target/surefire-reports/*.xml"
                }
                failure{
                    emailext to: 'mcubed132@gmail.com',
                        subject: "Unit and Integration Tests Complete",
                        body: "Unit and Integration Tests Failure!",
                        attachLog: true, compressLog: true 
                }
            }
        }
        stage('Deploy to Staging') { // Stage 5
            steps {
                //AWD CLI ---> https://aws.amazon.com/cli/
                echo "Deploy Stage..."
                sleep 5
                //echo "AWS CLI: Push complete!"
            }
        }
        stage('Release') { // Stage 7
            steps {
                //Kubernetes ---> https://kubernetes.io/
                echo "Release Stage..."
                sleep 20
                //echo "Production deployed!"
            }
        }
    }
}
