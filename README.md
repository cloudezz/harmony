harmony
=======

Harmony connects to the HAProxy load balancer server and modifies the proxy cfg file and restarts the proxy server .

The new service started by cloudezz is registered to the proxy server

Mappings are added to proxy cfg dynamically 

testapp.cloudezz.com  - >  192.168.1.2:67531
testapp.cloudezz.com  - >  192.168.5.7:67432

