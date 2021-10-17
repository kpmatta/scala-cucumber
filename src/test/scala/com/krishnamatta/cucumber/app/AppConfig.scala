package com.krishnamatta.cucumber.app

import java.io.File

import com.krishnamatta.cucumber.common.Constants
import com.typesafe.config.{Config, ConfigFactory}

object AppConfig {
  lazy val config: Config = loadConfig(Constants.ConfigFile)

  def main(args: Array[String]): Unit = {
    loadConfig(Constants.ConfigFile)
  }

  def loadConfig(configPath: String): Config = {
    val configurtion = ConfigFactory.parseFile(new File(configPath))
//    val configurtion1 = ConfigFactory.load(configurtion)
    configurtion
  }

  def getS3Config : Config = config.getConfig(Constants.ConfS3Config)

  def getProxy : Config = config.getConfig(Constants.ConfProxy)
}
