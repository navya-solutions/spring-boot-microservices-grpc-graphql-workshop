# How to generate kubernetes configuration files

Most common way to create kubernetes configuration files are:

1. Manually
2. Using Dekorate -Java compile-time generators)
3. Using kompose - Conversion tool from Docker Compose files to container orchestrators (Kubernetes or OpenShift)

## Dekorate

Can find installation and how to use details at: `https://dekorate.io/dekorate/#kubernetes`
Example:

``` 
@KubernetesApplication(
name = "graphql-gateway",
version = "v1",
        ports = {
                @Port(name = "web", containerPort = 8086),
                },
        envVars = {
                @Env(name = "grpcServiceHost", configmap = "app-config", value = "grpc-service"),
                @Env(name = "grpcServicePort", configmap = "app-config", value = "6565"),
        },
        //pvcVolumes = @PersistentVolumeClaimVolume(volumeName = "postgres-volume", claimName = "mysql-dev"),
        //mounts = @Mount(name = "postgres-volume", path = "/var/lib/mysql")

)
@DockerBuild(image = "navyasolutions/graphql-gateway:latest")
public class GatewayApplication {}
```

## Kompose

Can find installation details at: `https://kompose.io/installation/`
Can find how to use details at: `https://kubernetes.io/docs/tasks/configure-pod-container/translate-compose-kubernetes/`

Basic step are:

1. Install Kompose
2. Open the terminal, go to the location of docker-compose.yaml file
3. run `kompose convert`
4. Environment and secret parameters are not converted automatically. We have to manually create/migrate parameters to
   K8s configMap and secret.Examples `configmap.yaml`. We can read more about it
   at: `https://kubernetes.io/docs/concepts/configuration/configmap/`
5. We need to change the K8s configuration files metadata, labels, names etc as per project requirement
6. We need to merge the configuration file as per requirement, by default Kompose create separate file for each K8s kind

# How to run and test K8s on location machine

Options are:

1. Microk8s - https://microk8s.io/
2. MiniKube

## MiniKube

Installation and configuration details are at: `https://minikube.sigs.k8s.io/docs/start/`
Import configuration point:
minikube dashboard -- provide the dashboard view to monitor k8s resources minikube tunnel -- start/resolve service
loadbalancer pending issues. More details are at :
`https://stackoverflow.com/questions/44110876/kubernetes-service-external-ip-pending`
minikube addons enable ingress -- activate to work on ingress

# How to deploy on AWS EKS

1. Using AWS Console
2. Using eksctl : `https://eksctl.io/`
3. Using AWS CDK
4. Using EC2 cluster

## AWS Console

https://docs.aws.amazon.com/eks/latest/userguide/getting-started-console.html
link: https://www.youtube.com/watch?v=SsUnPWp5ilc

## eksctl

https://www.eksworkshop.com/010_introduction/
https://aws.amazon.com/eks/getting-started/
https://www.youtube.com/watch?v=p6xDCz00TxU

## AWK EKS

https://www.youtube.com/watch?v=X2ljiOx6BMQ

# Required tools to connet and deploy resources on AWS EKS

1. install kubctl using https://docs.aws.amazon.com/eks/latest/userguide/install-kubectl.html
   `curl -o kubectl https://amazon-eks.s3.us-west-2.amazonaws.com/1.20.4/2021-04-12/bin/linux/amd64/kubectl
   chmod +x ./kubectl mkdir -p $HOME/bin && cp ./kubectl $HOME/bin/kubectl && export PATH=$PATH:$HOME/bin echo 'export PATH=$PATH:$HOME/bin' >> ~/.bashrc kubectl version --short --client`
2. install eksctl using https://docs.aws.amazon.com/eks/latest/userguide/eksctl.html
   `curl --silent --location "https://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp sudo mv /tmp/eksctl /usr/local/bin eksctl version`
3. Install aws-iam-authenticator
   using https://docs.aws.amazon.com/eks/latest/userguide/install-aws-iam-authenticator.html
   `curl -o aws-iam-authenticator https://amazon-eks.s3.us-west-2.amazonaws.com/1.19.6/2021-01-05/bin/linux/amd64/aws-iam-authenticator
   chmod +x ./aws-iam-authenticator mkdir -p $HOME/bin && cp ./aws-iam-authenticator $HOME/bin/aws-iam-authenticator && export PATH=$PATH:$HOME/bin echo 'export PATH=$PATH:$HOME/bin' >> ~/.bashrc aws-iam-authenticator help`
4. status check for eks cluster aws eks --region eu-west-1 describe-cluster --name cluster-name --query cluster.status

5. Connect EKS cluster from local machine/EC2 OR Update local kubectl file to connect to the AWS EKS cluster
    1. update the Cluster secutriy group to allow the traffic from your EC2 machine
    2. update kubctl config file using below command (activate the kubectl context)
       aws eks --region eu-west-1 update-kubeconfig --name cluster-name

   aws eks --region eu-west-1 update-kubeconfig --name eks-cluster-name

   To check the active context - kubectl config get-contexts

   NOTE! CDK generate a role *clusteridMastersRole* which is used to connect to the EKS master node from local system
   aws eks --region eu-west-1 update-kubeconfig --name eks-cluster-name --role-arn arn:aws:iam::XXXXXXXXXXXX:
   role/XXXXXXXXXXXclusteridCreationRole-XXXXXx

        3. update as per the clsuter role/user and apply - kubectl apply -f /dashboard/eks-admin-service-account.yaml
   	4. check the Master role has trusted relationship
   	{

"Version": "2012-10-17",
"Statement": [
{
"Effect": "Allow",
"Principal": {
"AWS": "arn:aws:iam::XXXXXXXXXXXXXXXXX:root"
},
"Action": "sts:AssumeRole"
}
]
}

6. Add AWS role to cluster *No need to use this for now *
   eksctl utils write-kubeconfig --cluster eks-cluster-name --region eu-west-1 --authenticator-role-arn arn:aws:iam::
   XXXXXXXXXXXXXXX:role/role-name
7. check the kubctl context kubectl config view --minify
8. Access related issues in case of eks nocredentialproviders load balancer error
    - https://tutorials.releaseworksacademy.com/learn/how-to-debug-the-alb-controller-for-fargate-on-eks.html
    - https://docs.aws.amazon.com/eks/latest/userguide/aws-load-balancer-controller.html
      https://docs.aws.amazon.com/eks/latest/userguide/managing-auth.html
      https://aws.amazon.com/premiumsupport/knowledge-center/eks-api-server-unauthorized-error/

9 Deploy kubernetes dashboard details are at /dashboard/README.md

Important link:

- https://zero-to-jupyterhub.readthedocs.io/en/latest/kubernetes/amazon/step-zero-aws-eks.html

2. https://zero-to-jupyterhub.readthedocs.io/en/latest/kubernetes/amazon/step-zero-aws.html

# K8s common commands

K8s cheatsheet link `https://kubernetes.io/docs/reference/kubectl/cheatsheet/`

### Deploy resources

Go to the yaml file location to run the below commands

1. Apply configuration
   `
   kubectl apply -f configmap.yaml
   `

   `
   kubectl apply -f graphql-gateway.yaml -f rest-gateway.yaml -f grpc-service.yaml
   `
2. Get k8s resources
   `kubectl get pods,service,deployment,configmap`

### Scale up

`kubectl scale --current-replicas=1 --replicas=3 deployment graphql-gateway`

### update/replace

`kubectl replace --force -f configmap.yaml`
`kubectl set image deployment/graphql-gateway graphql-gateway=navyasolutions/graphql-gateway:latest`
`kubectl set resources deployment graphql-gateway --limits=cpu=200m,memory=512Mi --requests=cpu=100m,memory=256Mi`

### history

`kubectl rollout history deployment/graphql-gateway`
`kubectl rollout history deployment/graphql-gateway -o yaml`

### Delete resources

delete configmap
`kubectl delete -n default configmap`

delete service
`kubectl delete service/graphql-gateway-service service/grpc-service service/rest-gateway-service`

delete deployment
` kubectl delete deployment.apps/graphql-gateway deployment.apps/grpc-service  deployment.apps/rest-gateway`

APp URL :
http://LOAD-BALANCER_ARM:8086/graphql
http://LOAD-BALANCER_ARM:8086/playground

### add support for https using AWS certificate

https://aws.amazon.com/premiumsupport/knowledge-center/terminate-https-traffic-eks-acm/