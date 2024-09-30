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
                archiveArtifacts artifacts: 'C:/Users/admin/Desktop/JenkinsTests/JenkinsFile.java', fingerprint: true
                //echo "Unit and integration is up to standard, JUnit finished"
            }
            // Post running of steps, do this for a successful or failure of a build
            post {
                success{
                    // Uses email extention to send advanced emails
                    emailext to: 'mcubed132@gmail.com', // Who's the email to?
                        subject: "Unit and Integration Tests Complete", // What's the subject?
                        body: "Confirming Unit and Integration Tests were successfully completed.", // What's in the body?
                        attachLog: true, compressLog: true // Attach the log and compress it to be easier to send and receive
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
