import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class S3Tester {
    public static void main(String[] args) {
        AmazonS3ClientBuilder amazonS3ClientBuilder = AmazonS3ClientBuilder.standard();
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
                "http://localhost:9090/s3",
                "us-east-1"
        );
        amazonS3ClientBuilder.withEndpointConfiguration(endpointConfiguration);
        AmazonS3 amazonS3Client = amazonS3ClientBuilder.build();
        amazonS3Client.createBucket("test-bucket");
    }
}
