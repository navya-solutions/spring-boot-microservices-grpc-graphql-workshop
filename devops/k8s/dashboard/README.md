# AWS EKS dashboard

https://docs.aws.amazon.com/eks/latest/userguide/dashboard-tutorial.html

1. Deploy Kubernetes dashboard

``` 
kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.0.5/aio/deploy/recommended.yaml
```

2. Create an eks-admin service account and cluster role binding

```   
kubectl apply -f eks-admin-service-account.yaml
```

3. Connect to the dashboard

 ```
kubectl -n kube-system describe secret $(kubectl -n kube-system get secret | grep eks-admin | awk '{print $1}')

kubectl proxy

http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/#!/login

 ``` 