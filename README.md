# itlameda

### folder structure
```
.
├── README.md
└── infra
    └── emqx
        └── docker-env
            ├── docker-compose.yml
            └── haproxy.cfg
```

***

#### infra-emqx
1. docker env(local)
* architecture
```
                    [HAProxy]
                        |
            -------------------------
            |           |           |
         [EMQX1]    [EMQX2]    [EMQX3]
```
* port
    * EMQX1/2/3
        * 1883 : MQTT TCP
        * 8083 : MQTT WebSocket
        * 8883 : MQTT TCP(TLS)
        * 8084 : MQTT WebSocket(TLS)
        * 18083 : EMQ Dashboard
    * HAProxy
        * 9090 : Dashboard
* usage
```
### run docker
$ cd infra/emqx/docker-env
$ docker-compose up --build
```

* eqmx dashboard
    * url : http://localhost:18083
    * default admin account : admin / public
* haproxy stats
    * url : http://localhost:9090/stats

