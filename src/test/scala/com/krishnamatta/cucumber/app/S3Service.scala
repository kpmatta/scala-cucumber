package com.krishnamatta.cucumber.app

import java.io.{BufferedReader, File, InputStreamReader}

import com.amazonaws.{AmazonClientException, ClientConfiguration}
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}
import com.amazonaws.Protocol.HTTPS
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.model.{GetObjectRequest, ObjectMetadata, PutObjectRequest, SSEAlgorithm}
import com.amazonaws.services.s3.transfer.{MultipleFileUpload, ObjectMetadataProvider, TransferManagerBuilder}
import com.krishnamatta.cucumber.common.Constants

object S3Service {
  lazy val clientConfig: ClientConfiguration = new ClientConfiguration()
    .withProtocol(HTTPS)
    .withProxyHost(AppConfig.getProxy.getString(Constants.ConfProxyHost))
    .withProxyPort(AppConfig.getProxy.getInt(Constants.ConfProxyPort))

  lazy val s3Client: AmazonS3 = AmazonS3ClientBuilder.standard()
    .withCredentials(new DefaultAWSCredentialsProviderChain)
    .withClientConfiguration(clientConfig)
    .withRegion(Regions.US_EAST_1)
    .build()

  def uploadDirectoryToS3(bucketName: String, fileInput: File, s3FolderKey: String) : Unit = {
    val threshold = 5 * 1024 * 1024L
    val transferManager = TransferManagerBuilder.standard()
      .withS3Client(s3Client)
      .withMultipartUploadThreshold(threshold)
      .build()

    val metadataProvider = new ObjectMetadataProvider {
      override def provideObjectMetadata(file: File, metadata: ObjectMetadata): Unit = {
        metadata.setSSEAlgorithm(SSEAlgorithm.KMS.getAlgorithm)
      }
    }

    try {
      val upload: MultipleFileUpload =
        transferManager.uploadDirectory(bucketName, s3FolderKey, fileInput, true, metadataProvider)

      upload.waitForCompletion()
    } catch {
      case ex @ ( _: AmazonClientException | _: InterruptedException) =>
        throw new RuntimeException(ex)
    }
  }

  def uploadFileToS3(bucketName : String,
                     s3FolderKey: String,
                     inputFolder : String,
                     inputFile: String,
                     outputFile: String) : Unit = {

    val objectMetadata = new ObjectMetadata()
    objectMetadata.setSSEAlgorithm(SSEAlgorithm.KMS.getAlgorithm)

    val fileInput = new File(inputFolder + inputFile)
    val s3FileKey = s3FolderKey + outputFile
    if (fileInput.isFile) {
      val request = new PutObjectRequest(bucketName, s3FileKey, fileInput)
        .withMetadata(objectMetadata)
    } else {
      uploadDirectoryToS3(bucketName, fileInput, s3FileKey)
    }
  }

  def readFileFromS3(bucketName : String, outputFile: String): List[String] = {
    val s3Object = s3Client.getObject(new GetObjectRequest(bucketName, outputFile))
    val br = new BufferedReader(new InputStreamReader(s3Object.getObjectContent))
    Stream.continually(br.readLine())
      .takeWhile(x => Option(x).nonEmpty)
      .toList
  }

}
