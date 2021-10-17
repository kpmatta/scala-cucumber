package com.krishnamatta.cucumber.aws

import com.amazonaws.Protocol.HTTPS
import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.regions.Regions
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest
import com.krishnamatta.cucumber.app.AppConfig
import com.krishnamatta.cucumber.common.Constants

object AwsCred {
  def generateAwsCredentials(): (String, String, String) = {
    val targetAccountId = System.getProperty(Constants.SysPropTargetAwsAccountId)
    val vaultDeveloper = System.getProperty(Constants.SysPropVaultAcctDeveloper)

    val roleArn = s"arn:aws:iam::$targetAccountId:role/$vaultDeveloper"
    val tempRoleSessionName = s"$vaultDeveloper-temp"

    val assumeRoleRequest = new AssumeRoleRequest()
      .withRoleArn(roleArn)
      .withRoleSessionName(tempRoleSessionName)

    assumeRoleRequest.setRequestCredentialsProvider(new DefaultAWSCredentialsProviderChain)

    val proxyConfig = AppConfig.getProxy
    val clientConfig = new ClientConfiguration()
      .withProtocol(HTTPS)
      .withProxyHost(proxyConfig.getString(Constants.ConfProxyHost))
      .withProxyPort(proxyConfig.getInt(Constants.ConfProxyPort))

    val sts = AWSSecurityTokenServiceClientBuilder.standard()
      .withClientConfiguration(clientConfig)
      .withRegion(Regions.US_EAST_1)
      .build()

    val awsSessionCredentials = sts.assumeRole(assumeRoleRequest).getCredentials

    (awsSessionCredentials.getAccessKeyId,
      awsSessionCredentials.getSecretAccessKey,
      awsSessionCredentials.getSessionToken)
  }

}
