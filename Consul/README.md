# How to enable ACL for consul

1. Run the following command to start consul container with enabled acl:
``` docker run -d --name <consul-container-name> -p 8500:8500 -e CONSUL_LOCAL_CONFIG="{\"acl\": {\"enabled\": true,\"default_policy\": \"deny\",\"enable_token_persistence\": true}}" <consul-image-name>```

2. Generate secret key:
```docker exec -it <consul-container-name> consul acl bootstrap```

3. Copy `SecretID` from previous command

4. Copy `kv-store-polciy.hcl` to container:
```docker cp "./kv-store-policy.hcl" <consul-container-name>:/tmp/kv-store-policy.hcl```

5. Apply policy: 
```docker exec -it <consul-container-name> consul acl policy create -name "kv-store-policy" -rules @tmp/kv-store-policy.hcl -token <SecretID>```