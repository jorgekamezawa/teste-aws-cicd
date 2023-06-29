package com.example.aws.cicd.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ecs.EcsClient;
import software.amazon.awssdk.services.ecs.model.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/containers")
public class ContainerController {

    private final EcsClient ecsClient;

    public ContainerController() {
        this.ecsClient = EcsClient.builder()
                .region(Region.AWS_GLOBAL)
                .build();
    }

    @GetMapping
    public List<String> getContainers() {
        return ecsClient.listTasks(ListTasksRequest.builder().build())
                .taskArns()
                .stream()
                .map(this::describeTask)
                .collect(Collectors.toList());
    }

    private String describeTask(String taskArn) {
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
