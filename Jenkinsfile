pipeline {
    agent any

    stages {
        stage('Build') { // Stage 1
            steps {
                //Maven ---> https://maven.apache.org/
                //test update for Jenkins
                echo "Building Code..."
                sleep 15
                echo "Building Automation Tool Maven completed build"
            }
        }
        stage('Unit and Integration Tests') { // Stage 2
            steps {
                //JUnit ---> https://junit.org/junit5/
                echo "Running unit and integrations tests"
                echo "Launching JUnit"
                echo "JUnit analysis..."
                sleep 5 // Sleeps for 5 seconds to 'process'
                echo "Unit and integration is up to standard, JUnit finished"
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
        stage('Code Analysis') { // Stage 3
            steps {
                //SonarQube ---> https://www.sonarsource.com/products/sonarqube/
                echo "SonarQube initialisation..."
                echo "Code analysis has begun..."
                sleep 10
                echo "SonarQube: Code bug free confirmation"
            }
        }
        stage('Security Scan') { // Stage 4
            steps {
                //OWASP Dependency-Check ---> https://owasp.org/www-project-dependency-check/
                echo "Booting up OWASP Dependency-Check"
                sleep 25
                echo "OWASP: Codebase and dependencies have no active vulnerabilities"
            }
            post {
                success{
                    emailext to: 'mcubed132@gmail.com',
                        subject: "Security Scan Complete",
                        body: "Confirming Security Scan were successfully completed.",
                        attachLog: true, compressLog: true
                }
                failure{
                    emailext to: 'mcubed132@gmail.com',
                        subject: "Security Scan Complete",
                        body: "Security Scan Has Failed.",
                        attachLog: true, compressLog: true
                }
            }
        }
        stage('Deploy to Staging') { // Stage 5
            steps {
                //AWD CLI ---> https://aws.amazon.com/cli/
                echo "AWS CLI Pushing..."
                sleep 5
                echo "AWS CLI: Push complete!"
            }
        }
        stage('Integration Tests on Staging') { // Stage 6
            steps {
                //Postman ---> https://www.postman.com/
                echo "Begin Postman API testing..."
                sleep 7
                echo "Testing complete"
                echo "Successful test"
            }
            post {
                success{
                    emailext to: 'mcubed132@gmail.com',
                        subject: "Integration Tests on Staging Complete",
                        body: "Integration Tests on Staging were successfully completed.",
                        attachLog: true, compressLog: true
                }
                failure{
                    emailext to: 'mcubed132@gmail.com',
                        subject: "Integration Tests on Staging Failure",
                        body: "Integration Tests on Staging Had An Error!",
                        attachLog: true, compressLog: true
                }
            }
        }
        stage('Deploy to Production') { // Stage 7
            steps {
                //Kubernetes ---> https://kubernetes.io/
                echo "Production deployment using Kubernetes..."
                sleep 20
                echo "Production deployed!"
            }
        }
    }
}
