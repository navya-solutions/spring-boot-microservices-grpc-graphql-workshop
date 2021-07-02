# PostgreSQL docker image

## Steps:

### Build local image

```shell
   docker build -t navyasolutions/workshop-postgresql .
```

### Create tag from local image

```shell
   docker tag navyasolutions/workshop-postgresql:latest navyasolutions/workshop-postgresql:<IMAGE_VERSION>
```

### Push images to dockerhub

```shell
   docker push navyasolutions/workshop-postgresql:<IMAGE_VERSION>
   docker push navyasolutions/workshop-postgresql:latest
```
