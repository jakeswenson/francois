Francois
=====

Francois is a dropwizard.io api and web application for templatizing jenkins jobs. Francois is available as a docker image: https://hub.docker.com/r/onoffswitch/francois/
Francois deploys to the port `9090` with its dropwizard admin port on `9099`.

Configuration
===

Francois gets its configuration from either its configuration yaml file (configuration.yml) or the environment. 

The following environment variables are avaiable

- `JENKINS_URL`: this should point to your jenkins machine. example: `http://jenkins.jakeswenson.github.com/`
- `JENKINS_USER`: this is the user created to manage jenkins and create jobs. example: `francois`
- `JENKINS_TOKEN`: this should be the user token for that jenkins user above. You can get this from the users jenkins configuration page.


Using the docker image
=====



```
docker run -it \
    -e HOST_IPADDR=`docker-machine ip $DOCKER_MACHINE_NAME` \
    -e JENKINS_URL='http://jenkins.jakeswenson.github.com/' \
    -e JENKINS_USER=francois \
    -e JENKINS_TOKEN=USER_JENKINS_TOKEN \
    -p 9090:9090 \
    -p 9099:9099 \
    -p 1044:1044 \
    -p 1898:1898 \
    -v `pwd`/logs/core:/data/logs \
    onoffswitch/francois
```


Building your own container
=====

Use: 

```
mvn clean package

./scripts/run-core.sh
```

