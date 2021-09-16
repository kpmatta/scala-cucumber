package com.krishnamatta.cucumber.stepdefs

//import io.cucumber.core.logging.LoggerFactory
import io.cucumber.scala.{EN, ScalaDsl, Scenario}
import org.slf4j.LoggerFactory

class SampleSteps extends ScalaDsl with EN {
  private val logger = LoggerFactory.getLogger(this.getClass)

  Before{ scenario: Scenario =>
    logger.info("Before ...")
  }

  After{ scenario: Scenario =>
    logger.info("After ...")
  }

  Given("init resources") {
    logger.info("Initializing the resources")
  }

  And("""upload input files {string}""") { (fileName : String) =>
    logger.info(s"uploading file : $fileName")
  }

  And("""execute application with {string}""") { (fileName: String) =>
    logger.info(s"Executing the application with file: $fileName")
  }
}
