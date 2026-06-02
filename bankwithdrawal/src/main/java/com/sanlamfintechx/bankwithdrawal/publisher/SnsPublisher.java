package com.sanlamfintechx.bankwithdrawal.publisher;

import com.sanlamfintechx.bankwithdrawal.config.AwsConfig;
import com.sanlamfintechx.bankwithdrawal.event.WithdrawalEvent;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@Component
public class SnsPublisher {

    public final SnsClient snsClient;
    public final AwsConfig awsConfig;

    public SnsPublisher(SnsClient snsClient, AwsConfig awsConfig) {
        this.snsClient = snsClient;
        this.awsConfig = awsConfig;
    }

    public boolean publishMessage(WithdrawalEvent event) {
        String snsTopicArn = awsConfig.getSnsTopicArn();
        String eventJson = event.toJson(); // Convert event to JSON

        PublishRequest publishRequest = PublishRequest.builder()
                                                      .message(eventJson)
                                                      .topicArn(snsTopicArn)
                                                        .build();
        PublishResponse publishResponse = snsClient.publish(publishRequest);

        return publishResponse.sdkHttpResponse().isSuccessful()
            && publishResponse.messageId() != null;
    }



}
