netsh wlan set hostednetwork mode=allow ssid=G1 key=Abc123...

netsh

运行ncpa.cpl打开网络设置，找到刚刚创建的G1，右键--共享---允许xxx  勾选虚拟

然后开启无线网络
netsh wlan start hostednetwork