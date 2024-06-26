### Consul

1. Pull docker image:<br>
```docker pull hashicorp/consul```

2. Start docker container:<br>
```docker run -d --name=dev-consul -e CONSUL_BIND_INTERFACE=eth0 -p 8500:8500 consul```

### API Node Cluster with Loadbalancer

1. Ensure that you have ```release.env```, ```docker.env``` and ```nginx.conf``` files ready.
2. Build Mini Zanzibar Docker Image:<br>
```docker build -t mini-zanzibar-image .```

3. Start cluster containers:<br>
```docker compose --env-file docker.env up -d```

4. To start a single node, use this command:<br>
```docker run -d -p 8080:8080 -v "{absolute_path_to_db}:/data" --name mini-zanzibar mini-zanzibar-image```

