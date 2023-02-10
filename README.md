#start minikube

> minikube start

#check the minikube startus

> minikube status

#Before create image run bellow one(before create images case we need to crate our image inside minikube)

> eval $(minikube docker-env)

backend/demo
#Create springboot image

> docker build -t sun_travel_springboot_k8s_image .

backend/demo/k8s
#create secret

> kubectl apply -f mysql-db-root-credentials.yml
> kubectl apply -f mysql-db-credentials.yml

#create configmap

> kubectl apply -f mysql-configmap.yml

#create mysql service, deployment,pv and pcv

> kubectl apply -f mysql-deployment.yml

#create springboot service and deployment

> kubectl apply -f springboot-deployment.yml

frontend/sun-Travel

#Create frontend image

> docker build -t sun_travel_angular_k8s_image .

frontend/demo/k8s
#create angular service and deployment

> kubectl apply -f angular-deployment.yml
