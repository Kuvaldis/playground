Start on Windows
minikube start --vm-driver=hyperv

Run docker image (deployment name would be kubernetes-bootcamp)
kubectl run kubernetes-bootcamp --image=gcr.io/google-samples/kubernetes-bootcamp:v1 --port=8080

Expose service
kubectl expose deployment/kubernetes-bootcamp --type="NodePort" --port 8080