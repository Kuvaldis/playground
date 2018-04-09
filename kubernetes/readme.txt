Start on Windows
minikube start --vm-driver=hyperv

Run docker image (deployment name would be kubernetes-bootcamp)
kubectl run kubernetes-bootcamp --image=gcr.io/google-samples/kubernetes-bootcamp:v1 --port=8080

Expose service
kubectl expose deployment/kubernetes-bootcamp --type="NodePort" --port 8080

Create smth (pod, deployment etc.) from file.
kubectl create -f ./<file name>


* Pods - your instances
* Deployments - your deployment of pods
* Services - your gateway to pods

Print exposed service url (accessible from outside the cluster)
minikube service <service name> --url