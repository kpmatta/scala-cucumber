package com.krishnamatta.cucumber.common

object Constants {
  val ConfigFile : String = "src/test/resources/config/appConfig.conf"
  val ConfS3Config: String = "s3Config"
  val ConfProxy : String = "proxyConfig"
  val ConfProxyHost = "host"
  val ConfProxyPort = "port"

  //  System Properties
  val SysPropTargetAwsAccountId = "targetAwsAccountId"
  val SysPropVaultAcctDeveloper = "targetAwsIamRole"
}
