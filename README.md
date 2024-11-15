itlameda

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
    * HAProxy
        * 9090 : Dashboard
* usage
```
 $ cd infra/emqx/docker-env
 $ docker-compose up --build
```
