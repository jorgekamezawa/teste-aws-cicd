package com.example.aws.cicd.controller;

import com.example.aws.cicd.model.Container;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProviderChain;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ecs.EcsClient;
import software.amazon.awssdk.services.ecs.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/containers")
public class ContainerController {

    private List<String> erros = new ArrayList<>();

    @GetMapping
    public Container getContainers() {
        try {
            EcsClient ecsClient = EcsClient.builder()
                    .region(Region.AWS_GLOBAL)
                    .build();

            return new Container(ecsClient.listTasks(ListTasksRequest.builder().build())
                    .taskArns()
                    .stream()
                    .map(taskArn -> describeTask(taskArn, ecsClient))
                    .collect(Collectors.toList()));

        } catch (Exception ex1) {
            erros.add(ex1.getMessage());
            try {
                EcsClient ecsClient = EcsClient.builder()
                        .credentialsProvider(ProfileCredentialsProvider.create())
                        .region(Region.AWS_GLOBAL)
                        .build();

                return new Container(ecsClient.listTasks(ListTasksRequest.builder().build())
                        .taskArns()
                        .stream()
                        .map(taskArn -> describeTask(taskArn, ecsClient))
                        .collect(Collectors.toList()));

            } catch (Exception ex2) {
                erros.add(ex2.getMessage());
                try {
                    EcsClient ecsClient = EcsClient.builder()
                            .credentialsProvider(AwsCredentialsProviderChain.builder().build())
                            .region(Region.AWS_GLOBAL)
                            .build();

                    return new Container(ecsClient.listTasks(ListTasksRequest.builder().build())
                            .taskArns()
                            .stream()
                            .map(taskArn -> describeTask(taskArn, ecsClient))
                            .collect(Collectors.toList()));

                } catch (Exception ex3) {
                    erros.add(ex3.getMessage());

                    try {
                        EcsClient ecsClient = EcsClient.builder()
                                .credentialsProvider(AwsCredentialsProviderChain.builder().credentialsProviders().build())
                                .region(Region.AWS_GLOBAL)
                                .build();

                        return new Container(ecsClient.listTasks(ListTasksRequest.builder().build())
                                .taskArns()
                                .stream()
                                .map(taskArn -> describeTask(taskArn, ecsClient))
                                .collect(Collectors.toList()));

                    } catch (Exception ex4) {
                        erros.add(ex4.getMessage());

                        try {
                            EcsClient ecsClient = EcsClient.builder()
                                    .credentialsProvider(AwsCredentialsProviderChain.builder().credentialsProviders(DefaultCredentialsProvider.create()).build())
                                    .region(Region.AWS_GLOBAL)
                                    .build();

                            return new Container(ecsClient.listTasks(ListTasksRequest.builder().build())
                                    .taskArns()
                                    .stream()
                                    .map(taskArn -> describeTask(taskArn, ecsClient))
                                    .collect(Collectors.toList()));

                        } catch (Exception ex5) {
                            erros.add(ex5.getMessage());
                            return new Container(erros);
                        }
                    }
                }
            }
        }
    }

    private String describeTask(String taskArn, EcsClient ecsClient) {
        DescribeTasksResponse describeTasksResponse = ecsClient.describeTasks(
                DescribeTasksRequest.builder()
                        .tasks(taskArn)
                        .build()
        );

        Task task = describeTasksResponse.tasks().get(0);
        String containerArn = task.containerInstanceArn();
        String containerName = ecsClient.describeContainerInstances(
                DescribeContainerInstancesRequest.builder()
                        .containerInstances(containerArn)
                        .build()
        ).containerInstances().get(0).ec2InstanceId();

        return "Task ARN: " + task.taskArn() + ", Container Name: " + containerName;
    }
}
