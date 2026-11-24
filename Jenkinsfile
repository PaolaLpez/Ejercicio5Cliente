pipeline {
    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven 3.9'
    }

    environment {
        SONAR_SCANNER = tool 'SonarScanner 5.5'
        JAVA_HOME = tool 'JDK21'
    }

    stages {
        stage('Checkout') {
            steps {
                echo '=== Descargando codigo de Frontend ==='
                checkout scm
            }
        }

        stage('Compilacion y Pruebas con Mock') {
            steps {
                dir('admin-proyectos/Ejercicio5Cliente') {
                    echo '=== Compilando Frontend Spring Boot 3.2.5 con Java 21 ==='
                    sh 'mvn clean test'
                }
            }
            post {
                always {
                    junit 'admin-proyectos/Ejercicio5Cliente/target/surefire-reports/*.xml'
                    jacoco execFile: 'admin-proyectos/Ejercicio5Cliente/target/jacoco.exec',
                           classPattern: 'admin-proyectos/Ejercicio5Cliente/target/classes',
                           sourcePattern: 'admin-proyectos/Ejercicio5Cliente/src/main/java',
                           inclusionPattern: '**/*.class',
                           exclusionPattern: '**/target/**'
                }
            }
        }

        stage('Analisis de Codigo SonarQube (SAST)') {
            steps {
                dir('admin-proyectos/Ejercicio5Cliente') {
                    withSonarQubeEnv('SonarQubeServer') {
                        sh '''
                        mvn sonar:sonar                           -Dsonar.projectKey=admin-proyectos-frontend                           -Dsonar.projectName="AdminProyectos - Frontend Web"                           -Dsonar.java.binaries=target/classes                           -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml                           -Dsonar.exclusions="**/target/**,**/static/**"
                        '''
                    }
                }
            }
        }

        stage('Empaquetado JAR') {
            steps {
                dir('admin-proyectos/Ejercicio5Cliente') {
                    sh 'mvn package -DskipTests'
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }
    }
}
