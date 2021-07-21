```yaml
log_debug: true
network_groups:
  - name: "inner"
    networks: ["127.0.0.0/24"]
  - name: "apps"
    networks: ["192.168.1.1/24",192.168.2.1/24","192.168.3.1/24"
,"192.168.4.1/24"
]
server:
  http:
    listen_addr: ":9090"
	  allowed_networks: ["inner","apps"]
users:
  - name: "default"
    to_cluster: "default"
    to_user: "default"
    allowed_networks: ["inner"]
  - name: "web"
    password: "123456"
    to_cluster: "default"
    to_user: "web"
    cache: "longterm"
    max_concurrent_queries: 20
    max_execution_time: 30s
    max_queue_size: 40
    max_queue_time: 25s
  - name: "collect"
    password: "123456"
    to_cluster: "default"
    to_user: "collect"
clusters:
   - name: "default"
     nodes: ["192.168.1.20:8123","192.168.1.21:8123","192.168.1.22:8123","192.168.1.23:8123"]
     users:
      - name: "default"
      - name: "web"
        password: "123456"
      - name: "collect"
        password: "123456"
caches:
   - name: "longterm"
     dir: "/usr/local/data/data1/chproxy_cache_longterm"
     max_size: 20Gb
     expire: 1h
     grace_time: 20s
   - name: "shortterm"
     dir: "/usr/local/data/data1/chproxy_cache_shortterm"
     max_size: 1Gb
     expire: 1m
```