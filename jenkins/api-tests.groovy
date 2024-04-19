timeout(60) {
    node("maven") {
        wrap([$class: 'BuildUser']) {
            currentBuild.description = """
build user: ${BUILD_USER}
branch: ${REFSPEC}
"""

            config = readYaml text: env.YAML_CONFIG ?: null;

            if(config !=null) {
                for(param in config.entrySet()) {
                    env.setProperty(param.getValue())
                }
            }
        }

        stage("Checkout") {
            checkout scm;
        }
        stage("Create configuration") {
            sh "echo BASE_URL=${env.getProperty('BASE_URL')} > ./.env"
        }
        stage("Run API tests") {
            sh "mkdir ./reports"
            sh "docker run --rm --env-file -v ./reports:root/api_tests_allure-results ./.env -t api_tests:${env.getProperty('TEST_VERSION')}"
        }
        stage("Publish allure results") {
            REPORT_DISABLE = Boolean.parseBoolean(env.getProperty('REPORT_DISABLE')) ?: false
            allure([
                    reportBuildPolicy: 'ALWAYS',
                    results: ["./reports", "./allure-results"],
                    disabled: REPORT_DISABLE
            ])
        }
    }
}