# AWS EKS setup

## Steps to create EKS cluster

### prerequisite

AWS CLI version 2 - https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2.html

eksctl - https://docs.aws.amazon.com/eks/latest/userguide/install-kubectl.html

kubectl - https://docs.aws.amazon.com/eks/latest/userguide/install-kubectl.html

### Setup environment variables

```
export DEV_AWS_REGION=eu-west-1 <-- Change this to match your region
export DEV_ACCOUNT_ID=$(aws sts get-caller-identity --query 'Account' --output text)
export DEV_EKS_CLUSTER=sample-app <-- cluster name as per your requirement
export AZS=($(aws ec2 describe-availability-zones --query 'AvailabilityZones[].ZoneName' --output text --region $DEV_AWS_REGION))
export DEV_PRIVATE_SUBNET=($(aws ec2 describe-subnets --filter Name=vpc-id,Values=vpc-2ca2a14a --query 'Subnets[?MapPublicIpOnLaunch==`false`].SubnetId' --output text))
export DEV_PUBLIC_SUBNET=($(aws ec2 describe-subnets --filter Name=vpc-id,Values=vpc-2ca2a14a --query 'Subnets[?MapPublicIpOnLaunch==`true`].SubnetId' --output text))
export DEV_EKS_NODE_GROUP_NAME=ng-sample-app <-- cluster name as per your requirement
```

### Create an EKS cluster

#### Create an eksctl deployment file (eks-cluster.yaml) use in creating your cluster using the following syntax:

```
cat << EOF > eks-cluster.yaml
---
apiVersion: eksctl.io/v1alpha5
kind: ClusterConfig

metadata:
  name: ${DEV_EKS_CLUSTER}
  region: ${DEV_AWS_REGION}
  version: "1.20"
vpc:
  subnets:
    # must provide 'private' and/or 'public' subnets by availibility zone as shown
    public:
      eu-west-1a:
        id: "${DEV_PUBLIC_SUBNET[0]}"
      eu-west-1b:
        id: "${DEV_PUBLIC_SUBNET[1]}"
      eu-west-1c:
        id: "${DEV_PUBLIC_SUBNET[2]}"
nodeGroups:
- name: ng-${DEV_EKS_CLUSTER}
  desiredCapacity: 2
  instanceType: t3.small

EOF

```

Note! make sure that the region and subnet matches as per the existing VPC setup

#### Next, use the file you created as the input for the eksctl cluster creation.

```
eksctl create cluster -f eks-cluster.yaml  <-- this file is placed at devops/k8s/aws/eks-cluster.yaml
```

#### status check for eks cluster

```
 aws eks --region $DEV_AWS_REGION describe-cluster --name  $DEV_EKS_CLUSTER --query cluster.status
```

### Access EKS cluster for local machine

#### Update local machine kubectl context

```
aws eks  update-kubeconfig --name  $DEV_EKS_CLUSTER --region $DEV_AWS_REGION
```

To check the active context -

```
kubectl config get-contexts

kubectl config view --minify
```

#### Deploy Kubernetes dashboard details using /dashboard/README.md

[Kubernetes Dashboard setup](../dashboard/README.md)

### Delete EKS cluster

```
eksctl delete cluster $DEV_EKS_CLUSTER --region $DEV_AWS_REGION
```

### Create RDS

#### Setup environment variables

```

export DEV_AWS_DB_INSTANCE_IDENTIFIER=sample-app-instance
```

#### Create RDS

```
aws rds create-db-instance \
--db-name sampleBD \
--db-instance-identifier $DEV_AWS_DB_INSTANCE_IDENTIFIER \
--db-instance-class db.t3.micro \
--engine postgres \
--master-username root \
--master-user-password secret99 \
--max-allocated-storage 50 \
--backup-retention-period 10 \
--allocated-storage 20
```

#### Open traffic between database and EKS cluster

Update database security group inbound rule to allow traffic from EKS cluster for port 5432

### Delete RDS

```
aws rds delete-db-instance \
	--db-instance-identifier $DEV_AWS_DB_INSTANCE_IDENTIFIER  \
	--skip-final-snapshot
	
```

#### link for more details https://aws.amazon.com/getting-started/hands-on/create-connect-postgresql-db/

### Deploy resources on EKS

Go to the yaml file location to run the below commands

1. Apply configuration
   `
   kubectl apply -f configmap.yaml
   `

   `
   kubectl apply -f graphql-gateway.yaml -f rest-gateway.yaml -f grpc-service.yaml
   `
2. Get k8s resources
   `
   kubectl get pods,service,deployment,configmap
   `

### Scale up

`kubectl scale --current-replicas=1 --replicas=3 deployment graphql-gateway`

### update/replace

`kubectl replace --force -f configmap.yaml`

`kubectl set image deployment/graphql-gateway graphql-gateway=navyasolutions/graphql-gateway:latest`

`kubectl set resources deployment graphql-gateway --limits=cpu=200m,memory=512Mi --requests=cpu=100m,memory=256Mi`

### History

`kubectl rollout history deployment/graphql-gateway`

`kubectl rollout history deployment/graphql-gateway -o yaml`

### Delete resources

delete configmap
`kubectl delete -n default configmap`

delete service
`kubectl delete service/graphql-gateway-service service/grpc-service service/rest-gateway-service`

delete deployment
` kubectl delete deployment.apps/graphql-gateway deployment.apps/grpc-service  deployment.apps/rest-gateway`

APP URL :
http://LOAD-BALANCER_ARM:8086/graphql
http://LOAD-BALANCER_ARM:8086/playground

#### Add SSL support using AWS certificate

configuration defined at
` devops/k8s/aws/aws-ssl-cert/graphql-gateway-enable-aws-https-config.yaml`