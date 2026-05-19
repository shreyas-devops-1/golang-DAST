package org.opstree

class EmployeeScan implements Serializable {

    def steps

    EmployeeScan(steps) {
        this.steps = steps
    }

    def execute(config) {
        def ZAP = config.ZAP
        def TARGET = config.TARGET
        def KEY = config.KEY

        steps.sh "curl -s '${ZAP}/JSON/spider/action/scan/?url=${TARGET}&apikey=${KEY}'"

        steps.timeout(time: 5, unit: 'MINUTES') {
            steps.waitUntil {
                def res = steps.sh(
                    script: "curl -s '${ZAP}/JSON/spider/view/status/?scanId=0&apikey=${KEY}'",
                    returnStdout: true
                ).trim()

                def status = res.replaceAll('[^0-9]', '')
                steps.echo "Spider: ${status}%"
                return status == "100"
            }
        }

        steps.sh "curl -s '${ZAP}/JSON/ascan/action/scan/?url=${TARGET}&recurse=true&apikey=${KEY}'"

        steps.sleep(10)

        def scanId = steps.sh(
            script: "curl -s '${ZAP}/JSON/ascan/view/scans/?apikey=${KEY}'",
            returnStdout: true
        ).trim().tokenize('"').findAll { it.isNumber() }[-1]

        steps.echo "Scan ID: ${scanId}"

        steps.timeout(time: 10, unit: 'MINUTES') {
            steps.waitUntil {
                def res = steps.sh(
                    script: "curl -s '${ZAP}/JSON/ascan/view/status/?scanId=${scanId}&apikey=${KEY}'",
                    returnStdout: true
                ).trim()

                def status = res.replaceAll('[^0-9]', '')
                steps.echo "Active Scan: ${status}%"
                return status == "100"
            }
        }
    }
}