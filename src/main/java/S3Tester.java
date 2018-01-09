import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class S3Tester {
    public static Bucket getBucket(String bucketName, AmazonS3 client) {
        Bucket namedBucket = null;
        List<Bucket> buckets = client.listBuckets();
        for (Bucket b : buckets) {
            if (b.getName().equals(bucketName)) {
                namedBucket = b;
            }
        }
        return namedBucket;
    }

    public static Bucket createBucket(String bucket_name, AmazonS3 client) {
        Bucket b = null;
        if (client.doesBucketExist(bucket_name)) {
            System.out.format("Bucket %s already exists.\n", bucket_name);
            b = getBucket(bucket_name, client);
        } else {
            try {
                b = client.createBucket(bucket_name);
            } catch (AmazonS3Exception e) {
                System.err.println(e.getErrorMessage());
            }
        }
        return b;
    }

    public static void main(String[] args) throws IOException {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIAIOSFODNN7EXAMPLE", "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY");
        AmazonS3ClientBuilder amazonS3ClientBuilder = AmazonS3ClientBuilder.standard();
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
                "http://localhost:4569",
                "us-east-1"
        );
        amazonS3ClientBuilder
                .withEndpointConfiguration(endpointConfiguration)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .enablePathStyleAccess().disableChunkedEncoding();
        AmazonS3 s3Client = amazonS3ClientBuilder.build();
        createBucket("test", s3Client);

        s3Client.putObject("test", "shtate", "A BIG LONG STRING TO USE IN THE TEST");
        S3Object object = s3Client.getObject(new GetObjectRequest("test", "shtate"));
        InputStream objectData = object.getObjectContent();
        String theString = IOUtils.toString(objectData);
        System.out.println(theString);
        objectData.close();
    }
}
