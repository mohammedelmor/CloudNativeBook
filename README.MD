# My Notes on "Cloud Native Spring in Action" book by Thomas Vitale
# Chapter 2 Cloud native patterns and technologies

## This chapter is just an overview of what the rest of the book is, it goes through building a simple Spring Boot application and deploying it to a Kubernetes cluster, doing it in the cloud native way

### Building a simple rest endpoint
```java
package com.polarbookshop.catalogservice;
 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
 
@RestController
public class HomeController {
 
  @GetMapping("/")
  public String getGreeting() {
    return "Welcome to the book catalog!";
  }
}
```

### Containerizing applications with Docker
By using the out-of-the-box integration of Spring Boot with Cloud Native Buildpacks (https://buildpacks.io). It provides a high-level abstraction for automatically transforming application source code into container images instead of using a low-level Dockerfile
by running:
```shell
$ ./gradlew bootBuildImage
```
this will create an image with the name `catalog-service:0.0.1-SNAPSHOT

#### Check the image
```shell
$ docker images catalog-service:0.0.1-SNAPSHOT
REPOSITORY        TAG              IMAGE ID       CREATED        SIZE
catalog-service   0.0.1-SNAPSHOT   f0247a113eff   42 years ago   275MB
```

#### Run the image
```shell
$ docker run --rm --name catalog-service -p 8080:8080 catalog-service:0.0.1-SNAPSHOT
```

### Managing containers with Kubernetes


#### Start a local Kubernetes cluster
```shell
$ minikube start
```

#### Loading the image into the local Kubernetes

By default, minikube uses the Docker Hub registry to pull images, and it doesn’t have access to your local ones. Therefore, it will not find the image you built for the Catalog Service application.

```shell
$ minikube image load catalog-service:0.0.1-SNAPSHOT
```

#### Creating a deployment
Create a Deployment from a container image. Kubernetes will take care of creating Pods for the application and managing them.
```shell
$ kubectl create deployment catalog-service --image=catalog-service:0.0.1-SNAPSHOT
```

#### Verify the creation of the Deployment object
```shell
$ kubectl get deployment
NAME              READY   UP-TO-DATE   AVAILABLE   AGE
catalog-service   1/1     1            1           7s
```

Behind the scenes, Kubernetes created a Pod for the application defined in the Deployment resource. You can verify the creation of the Pod object as follows:
```shell
$ kubectl get pod
NAME                               READY   STATUS    RESTARTS   AGE
catalog-service-5b9c996675-nzbhd   1/1     Running   0          21s
```

By default, applications running in Kubernetes are not accessible. Let’s fix that. First, you can expose Catalog Service to the cluster through a Service resource by running the following command:
```shell
$ kubectl expose deployment catalog-service --name=catalog-service --port=8080
```

```shell
$ kubectl get service catalog-service
NAME              TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)    AGE
catalog-service   ClusterIP   10.96.141.159   <none>        8080/TCP   7s
```

however we need port forwarding to access the service from the local machine
```shell
$ kubectl port-forward service/catalog-service 8000:8080
Forwarding from 127.0.0.1:8000 -> 8080
Forwarding from [::1]:8000 -> 8080
```

#### To delete the service and deployment
```shell
$ kubectl delete service catalog -service
```
```shell
$ kubectl delete deployment catalog -service
```

#### stop the minikube cluster
```shell
$ minikube stop
```

