#MemKit-Cluster(another LTS project)

##What?

```
MemKit is a key-value store tool,and the Memkit is local,but this project will work on    
cluster,the topology is star-type.the center is the manager.the edge is the memkit node.  
if you want to let an node join into the memkit cluster,just tell the node by writing the  
configure file.there was a template configure file with the project,BUT THIS CONFIGURE  
FILE JUST FOR TESTING THE PROJECT,YOU SHOULD ALWAYS MODIFY THE CONFIGURE FILE ACCORDING   
TO YOUR CASE.   
You should know these issues before using memkit-cluster.   
(1). you have used the MemKit in your host by local mode.if not,just find the memkit to test  
(2). you should permit the memkit cluster fail.and the data will flush when you shut down the cluster  
(3). you can add as many as you can nodes into memkit cluster.but you should trade off the cost   
(4). the project is a LTS project of mine.so,you can use it and report the bugs to me with the e-mail   
(5). again,if you want to know about the principle and process of memkit.please learn the memkit project    
firstly,then you will find this is so easy to use.
```


##how?

####There is a small demo for testing the memkit cluster. 
+++><https://github.com/pandening/MemKit-Cluster/blob/master/MemKitClusterManager/src/net/hujian/memkit/clusterTestDemo/ClusterConsoleControlRunner.java>
####hot to set up the demo?
#####1.edit the config file,the follow codes is a template configure.
```
#This is the config file of this memcached,and the code start with '#' means
#this is a comment,the program will skip these code,and you can just use the
#default config file,or you can change the config to your style,and contact
#with me (1425124481@qq.com) when the mem kit is not work by this config.
#1.remote ip,do not use 127.0.0.1,you should let other host connect to you by this ip
$ip:192.168.1.1
#2. the port
$port:6423
#3. the default capacity
$capacity:1000
#4.the default time to refresh memcached(minute)
$refresh_mem_time:20
#5. the default ttl(the default is 1 hour)
$ttl:3600
#6. the backlog
$backlog:10
#7. an name for this slave
$name:slave_a
#8. manager ip,you want to join a cluster
$manager_ip:127.0.0.1
#9. manage port,you want to join a cluster
$Manage_Port:8888
#10. if you want to join a cluster,just set true
$Flag:true
#11. if you want to set up the re-build engine
$RBF:true
#12. the timer to dump file for rebuilding
$DT:60
#13.dump file name
$fn:./conf/dump.log
```
#####2.start the memkit.
#####3.start the cluster manager
#####4.check the run result.
#####if you look the result like this,you are succeed  
#########memkit manager
![iamge](https://github.com/pandening/images/blob/master/memkitDemoRun.png)
########memkit 
![image](https://github.com/pandening/images/blob/master/memkitserver.png)

##Developer?

hujian:<1425124481@qq.com>

##update Information

```
1.2016/9/9      set up the project with total code 4026 lines.    
2.2016/9/10     re-organize the files,and set up the auto-dump engine,you can rebuild the memkit now~ 
3.2016/9/11     (1). create the .sh file,you can use .sh file to run the memkit
                (2). more apis,such as 'ps','ss','ks',etc,you can check the consoleRunner.h file to get details 
                (3). new timer and thread factory,more fixable and stable. 
                (4). you get replace a key's value to another now,but you should offer the new value,not another store@value
                (5). you can rename a storage now,return true/false to show the result
                (6). you can rename a key's name,return true/false
                (7). add some new files,but now they are stupid.
4.2016/9/13
                (1). fix the load bugs,now the load function works well.
                (2). remove some useless codes.
                (3). move the .sh file to bin/
                (4). create a thread pool,but not work now.
                (5). the memkit can get the system's memory information now,then the manage can set capacity according to this info.
                (6). codes:5329 lines
5. 2016/9/14    
                (1). new memory pool,and set up the in-memory cache with single list.
                (2). update the manager client.
                (3). codes:6265 lines
```

##License?

```
Copyright 2016 HuJian

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

```
