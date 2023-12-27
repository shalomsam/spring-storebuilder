# spring-storebuilder
online store builder application

[![CodeFactor](https://www.codefactor.io/repository/github/shalomsam/spring-storebuilder/badge)](https://www.codefactor.io/repository/github/shalomsam/spring-storebuilder)
[![codecov](https://codecov.io/gh/shalomsam/spring-storebuilder/graph/badge.svg?token=DO0JC6CSPX)](https://codecov.io/gh/shalomsam/spring-storebuilder)

# Setup
To run this application you need to supply a valid mongo uri. This application is configured to use spring cloud with Hashicorp Vault as its source for secrets.


## Vault Installation and Configuration
Before running or building the application you need to first download and install [vault](https://developer.hashicorp.com/vault/install)

```
brew tap hashicorp/tap
brew install hashicorp/tap/vault
```

After installing vault via brew you have an option to run vault as a background service. When running as a service the output can be found in the following log location - `/home/linuxbrew/.linuxbrew/var/log/vault.log` (may vary with OS).

*Note: Additional config info can be found in the brew service file*

Add the `VAULT_ADDR`, and `VAULT_TOKEN`(Root token) to bashrc or bash_profile. Save the `Unseal Key` safely in a file on device root.

Check connection with `vault status`. If everything is setup correctly you can add the mongodb uri with the following command to vault -

```
vault kv put secret/storebuilder spring.data.mongodb.uri=......
```

Ensure to update the token value in `application.yaml` for vault configuration as well. On running the application `spring.data.mongodb.uri` value should be picked up by spring boot automatically.