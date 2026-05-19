package org.opstree

class EmployeeReport implements Serializable {

    def steps

    EmployeeReport(steps) {
        this.steps = steps
    }

    def execute(config) {
        def ZAP = config.ZAP
        def KEY = config.KEY

        steps.sh "curl -s '${ZAP}/OTHER/core/other/htmlreport/?apikey=${KEY}' -o zap_report.html"

        steps.archiveArtifacts(
            artifacts: 'zap_report.html',
            fingerprint: true
        )
    }
}