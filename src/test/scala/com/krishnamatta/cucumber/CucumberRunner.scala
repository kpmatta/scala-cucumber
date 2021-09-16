package com.krishnamatta.cucumber

import io.cucumber.junit.{Cucumber, CucumberOptions}
import org.junit.runner.RunWith

@RunWith(classOf[Cucumber])
@CucumberOptions(
  features = Array("src/test/resources/features"),
  glue = Array("com.krishnamatta.cucumber.stepdefs"),
  useFileNameCompatibleName = true,
  tags = "@Regression"
)
class CucumberRunner {

}
