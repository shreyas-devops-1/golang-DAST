package org.opstree

class EmployeeSetup implements Serializable {

    def steps

    EmployeeSetup(steps) {
        this.steps = steps
    }

    def execute(config) {
        def ZAP = config.ZAP
        def BASE = config.BASE
        def TARGET = config.TARGET
        def KEY = config.KEY

        steps.sh """
            curl -s "${ZAP}/JSON/core/action/newSession/?name=new&overwrite=true&apikey=${KEY}"
            curl -s "${ZAP}/JSON/core/action/accessUrl/?url=${BASE}&apikey=${KEY}"
            curl -s "${ZAP}/JSON/core/action/accessUrl/?url=${TARGET}&apikey=${KEY}"
        """
    }
}