package org.opstree

class EmployeeNotify implements Serializable {

    def steps

    EmployeeNotify(steps) {
        this.steps = steps
    }

    def success(config) {
        steps.slackSend(
            channel: config.SLACK,
            color: 'good',
            message: "SUCCESS: DAST\n${steps.env.BUILD_URL}"
        )

        steps.emailext(
            to: config.EMAIL,
            subject: "SUCCESS: ${steps.env.JOB_NAME} #${steps.env.BUILD_NUMBER}",
            body: "DAST Passed\n${steps.env.BUILD_URL}",
            attachmentsPattern: '**/zap_report.html'
        )
    }

    def failure(config, err) {
        steps.slackSend(
            channel: config.SLACK,
            color: 'danger',
            message: "FAILED: ${steps.env.BUILD_URL}\n${err.message}"
        )

        steps.emailext(
            to: config.EMAIL,
            subject: "FAILURE: ${steps.env.JOB_NAME} #${steps.env.BUILD_NUMBER}",
            body: "Error: ${err.message}",
            attachmentsPattern: '**/zap_report.html'
        )
    }
}