### Consul

1. Pull docker image:
```docker pull hashicorp/consul```

2. Start docker container:
```docker run -d --name=dev-consul -e CONSUL_BIND_INTERFACE=eth0 -p 8500:8500 consul```



