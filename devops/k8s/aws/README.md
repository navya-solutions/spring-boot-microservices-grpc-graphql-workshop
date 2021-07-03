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

## Create an eksctl deployment file (eks-cluster.yaml) use in creating your cluster using the following syntax:

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

## Next, use the file you created as the input for the eksctl cluster creation.

```
eksctl create cluster -f eks-cluster.yaml
```

Note! make sure that the region and subnet match as per defined in VPC

### Access the cluster for local machine

#### status check for eks cluster

```
 aws eks --region $DEV_AWS_REGION describe-cluster --name  $DEV_EKS_CLUSTER --query cluster.status
```

#### update local kubectl context

```
aws eks  update-kubeconfig --name  $DEV_EKS_CLUSTER --region $DEV_AWS_REGION
```

To check the active context -

```
kubectl config get-contexts

kubectl config view --minify
```

#### Deploy Kubernetes dashboard details are at /dashboard/README.md

[Kubernetes Dashboard setup](../dashboard/README.md)

### Delete EKS cluster

```
eksctl delete cluster $DEV_EKS_CLUSTER --region $DEV_AWS_REGION
```

## Create RDS

### Setup environment variables

```

export DEV_AWS_DB_INSTANCE_IDENTIFIER=sample-app-instance
```

### create rds

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

### connect database to the EKS cluster

Update database security group inbound rule to allow traffic from EKS cluster for port 5432

### delete rds

```
aws rds delete-db-instance \
	--db-instance-identifier $DEV_AWS_DB_INSTANCE_IDENTIFIER  \
	--skip-final-snapshot
	
```

### https://aws.amazon.com/getting-started/hands-on/create-connect-postgresql-db/