timeout(15) {
    node("maven") {
        wrap([$class: 'BuildUser']) {
            currentBuild.description = """
build user: ${BUILD_USER}
branch: ${REFSPEC}
"""

            config = readYaml text: env.YAML_CONFIG

            if (config != null) {
                for (param in config.entrySet()) {
                    env.setProperty(param.getKey(), param.getValue())
                }
            }
        }

        stage("Checkout") {
            checkout scm;
        }

        stage("Create configuration") {
            sh "echo BASE_URL=${env.getProperty('BASE_URL')} > ./.env"
        }

        stage("API tests in docker image") {
            sh "docker run --rm \
            --network=host --env-file ./.env \
            -v /root/.m2/repository:/root/.m2/repository \
            -v ./surefire-reports:/home/ubuntu/api_tests/target/surefire-reports \
            -v ./allure-results:/home/ubuntu/api_tests/target/allure-results \
            -t localhost:5005/api_tests:${env.getProperty('TEST_VERSION')}"
        }

        stage("Publish Allure Reports") {
            allure([
                    includeProperties: false,
                    jdk: '',
                    properties: [],
                    reportBuildPolicy: 'ALWAYS',
                    results: [[path: './allure-results']]
            ])
        }
    }
}