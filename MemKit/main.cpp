/**
 *      copyright C hujian 2016 version 1.0.0
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
#include "ConsoleRunner.h"
#include "RemoteRunner.h"
#include "RefreshMem.h"

typedef struct remote_ds{
    int file_ds;
    MemKit* memKit;
}remote_ds_t;

/**
 * handler
 * @param info
 */
void do_remote_job(remote_ds_t info) {
    MemKitConfig* config=MemKitConfig::getConfigure();
    bool loop=true;
    while (loop) {
        char buffer[BUFFER_SIZE];
        memset(buffer, 0, sizeof(buffer));
        int rcv_len = 0;
        while(rcv_len==0||rcv_len==-1){
            rcv_len=recv(info.file_ds, buffer, sizeof(buffer), 0);
        }
        /**
         * 1.get
         * 2.put
         * 3.set
         * 4.ttl
         * 5.setc
         * 6.dump
         * 7.append
         * 8.flush
         * 9.flusha
         * 10.info
         * 11.exit
         * 12.load
         * 13.rm
         * 14.exist
         */
        String line, store_id, key, value, cmd, file, ttl, cap, clear, newV, command, response;
        std::vector<String> splitVec;
        line = buffer;
        if(buffer[strlen(buffer)-1]=='\n') {
            line=line.substr(0,line.length()-1);
        }
        if(DEBUG) {
            os << "Receive Length:" << rcv_len << "@command[" << line << "]" << el;
        }
        splitVec.clear();
        splitVec = MemKitUtils::split(line, " ");
        if (splitVec.empty()) {
            continue;
        }
        if (DEBUG) {
            std::vector<std::string>::iterator vit = splitVec.begin();
            for (; vit != splitVec.end(); vit++) {
                std::cout << "-------->[" << *vit << "]" << std::endl;
            }
        }
        command = splitVec[0];
        switch (command[0]) {
            case 'h':{//i am alive
                response="alive";
                break;
            }
            case 'r':{
                store_id=splitVec[1];
                key=splitVec[2];
                if(DEBUG) {
                    os << "delete store[" << store_id << "] key[" << key << "]" << el;
                }
                response=info.memKit->popMem(store_id,key);
                break;
            }
            case 'e': {
                cmd=splitVec[0];
                if(cmd=="exit") {
                    os << "\tclose the file des connection,Bye~" << el;
                    response = "exit\n";
                    send(info.file_ds, response.c_str(), strlen(response.c_str()), 0);
                    response = "exit";
                    close(info.file_ds);
                    loop = false;
                    break;
                }else if(cmd=="exist"){
                    store_id=splitVec[1];
                    key=splitVec[2];
                    bool check=info.memKit->exist(store_id,key);
                    if(check){
                        response="true";
                    }else{
                        response="false";
                    }
                    break;
                }else{
                    store_id=splitVec[1];
                    bool check=info.memKit->exist(store_id);
                    if(check){
                        response="true";
                    }else{
                        response="false";
                    }
                    break;
                }
            }
            case 'l': {//load from file
                cmd = splitVec[0];
                if (cmd != "load" && cmd != "LOAD") {
                    response = "error";
                    if (DEBUG) {
                        os << "cmd:[" << cmd << "]" << el;
                    }
                    break;
                } else {
                    /**
                     * get the file
                     */
                    file = splitVec[1];
                    /**
                     * action
                     */
                    info.memKit->loadFromFile(file);
                    response = "ok";
                    if (DEBUG) {
                        os << "file:[" << file << "]" << el;
                    }
                    break;
                }
            }
                /**
                  * 1. set the ttl
                  * 2. set the capacity
                  */
            case 's': {
                cmd = splitVec[0];
                if (DEBUG) {
                    os << "cmd:[" << cmd << "]" << el;
                }
                if (cmd == "set") {
                    /**
                     * get the store id and the key and the ttl
                     */
                    store_id = splitVec[1];
                    key = splitVec[2];
                    ttl = splitVec[3];
                    if (DEBUG) {
                        os << "store_id[" << store_id << "] key[" << key << "] ttl[" << ttl << "]" << el;
                    }
                    /**
                     * action
                     */
                    info.memKit->set(store_id, key, atol(ttl.c_str()));
                    if(DEBUG){
                        os<<"SET TTL=>"<<atol(ttl.c_str())<<el;
                    }
                    response = "ok";
                    break;
                } else if (cmd == "setc" || cmd == "SETC") {
                    /**
                     * get the new capacity
                     */
                    cap = splitVec[1];
                    /**
                     * action
                     */
                    info.memKit->setCapacity(atol(cap.c_str()));
                    response = MemKitUtils::parseString(info.memKit->capacity());
                    break;
                } else {
                    response = "0";
                    break;
                }
            }
            case 't': {//get the ttl
                store_id = splitVec[1];
                key = splitVec[2];
                /**
                 * action
                 */
                response = MemKitUtils::parseString(info.memKit->getTTL(store_id, key));
                if (DEBUG) {
                    os<<"src ttl:"<<info.memKit->getTTL(store_id,key)<<el;
                    os << "store_id[" << store_id << "] key[" << key << "]" << el;
                }
                break;
            }
            case 'g': {//get a value
                store_id = splitVec[1];
                key = splitVec[2];
                /**
                 * action
                 */
                response = info.memKit->get(store_id, key);
                if (DEBUG) {
                    os << "store_id[" << store_id << "] key[" << key << "]" << el;
                }
                break;
            }
            case 'd': {//dump
                clear = splitVec[1];
                if (clear == "true") {
                    info.memKit->dump(true);
                } else {
                    info.memKit->dump(false);
                }
                response = "ok";
                if (DEBUG) {
                    os << "flag[" << clear << "]" << el;
                }
                break;
            }
            case 'a': {
                store_id = splitVec[1];
                key = splitVec[2];
                newV = splitVec[3];
                /**
                 * action
                 */
                info.memKit->append(store_id, key, newV);
                response = "ok";
                if (DEBUG) {
                    os << "store_id[" << store_id << "] key[" << key << "]" << "new value[" << newV << "]" << el;
                }
                break;
            }
            case 'p': {//put
                store_id = splitVec[1];
                key = splitVec[2];
                value = splitVec[3];
                /**
                 * action
                 */
                info.memKit->put(store_id, key, value);
                response = "ok";
                if (DEBUG) {
                    os << "store_id[" << store_id << "] key[" << key << "] value[" << value << "]" << el;
                }
                break;
            }
            case 'f': {
                cmd = splitVec[0];
                if (DEBUG) {
                    os << "cmd[" << cmd << "]" << el;
                }
                if (cmd == "flush") {//flush
                    /**
                     * get the flush store id
                     */
                    store_id = splitVec[1];
                    /**
                     * action
                     */
                    info.memKit->flushStore(store_id);
                    response = "ok";
                    if (DEBUG) {
                        os << "store_id[" << store_id << el;
                    }
                    break;
                } else {//flush all
                    info.memKit->flushAll();
                    response = "ok";
                    break;
                }
            }
            case 'i': {//capacity,size,ip,port,name
                response = MemKitUtils::parseString(info.memKit->capacity()) +
                           "," + MemKitUtils::parseString(info.memKit->size())+
                           ","+ config->getIP()+","+config->getPort()+
                           ","+config->getName();
                break;
            }
            default: {
                break;
            }
        }
        /**
         * send the response
         */
        response+="\n";
        response="ack_"+response;
        int send_len=send(info.file_ds, response.c_str(), strlen(response.c_str()), 0);
        if(DEBUG){
            os << "The response is:[" << response << "]" <<"send length:"<<send_len<<el;
        }
        response="";
    }
}
/**
 * remote runner
 */
void run_RemoteRunner()
{
    RemoteRunner* runner=new RemoteRunner();
    int server_sock_fd;
    socklen_t  length;
    struct sockaddr_in client_sockaddr;
    server_sock_fd=runner->get_server_sock_fd();
    while(1) {
        os<<"i am listen....."<<el;
        length = sizeof(client_sockaddr);
        int connection_fd = accept(server_sock_fd,
                                   (struct sockaddr *) &client_sockaddr, &length);
        if (connection_fd < 0) {
            perror("accept error");
            exit(1);
        } else {
            remote_ds_t rdt;
            rdt.file_ds=connection_fd;
            rdt.memKit=runner->getMemKit();
            std::thread Thread(do_remote_job,rdt);
            Thread.detach();
        }
    }
}
/**
 * the fresh~
 * @param seconds
 */
void run_refreshMem(long seconds){
    /**
     * it's easy to do this.
     */
    RefreshMem* refreshMem=new RefreshMem(seconds);
}

/**
 * console runner
 */
void run_Console()
{
    ConsoleRunner* runner=new ConsoleRunner(1024);
    runner->run();
}
/**
 * this function will send the mem kit's information to server
 * let the server know about it.
 */
void report_to_server_manage(){
    /**
     * for get the information
     */
    MemKitConfig* config=MemKitConfig::getConfigure();
    /**
     * for get size
     */
    MemKit* memKit=MemKit::getInstance(1024);
    MemKitUtils* memKitUtils=new MemKitUtils();
    /**
     * capacity,size,ip,port,name
     */
    String report="ack_"+config->getCapacity()+","+memKitUtils->parseString(memKit->size())+
                  ","+config->getIP()+","+config->getPort()+","+config->getName()+"\n";
    os<<"-->Report information:"<<report;
    String toIP=config->getManagerIP();
    int toPort=atoi(config->getManagerPort().c_str());
    /**
     * connect to server
     */
    int sock=socket(AF_INET,SOCK_STREAM,0);
    struct sockaddr_in server_addr;
    memset(&server_addr,0,sizeof(server_addr));
    /**
     * set up the server address
     */
    server_addr.sin_family=AF_INET;
    server_addr.sin_port=htons(toPort);
    server_addr.sin_addr.s_addr=inet_addr(toIP.c_str());
    while(true) {
        /**
         * start to connect
         */
        if (connect(sock, (struct sockaddr *) &server_addr, sizeof(server_addr))>=0) {
            break;
        }
        perror("connect to server manager error");
        sleep(1);
    }
    char buf[1024];
    /**
     * send to server my information
     */
    int send_len=send(sock, report.c_str(), strlen(report.c_str()), 0);
    os<<"Report to server manager:"<<send_len<<" bytes"<<el;
    memset(buf,0, sizeof(buf));
    while(true) {
        /**
         * recv the ack
         */
        if(recv(sock, buf, sizeof((buf)), 0)>0){
            break;
        }
        os<<"---wait to receive...."<<el;
        sleep(1);
    }
    report=buf;
    report=report.substr(0,report.length()-1);
    os<<"receive from server manager:["<<report<<"]"<<el;
    /**
     * close the connection
     */
    close(sock);
}

/**
 * the main
 * @param argc
 * @param argv
 * @return
 */
int main(int argc,char**argv) {
    /**
     * show the base config
     */
    MemKitConfig* config=MemKitConfig::getConfigure();
    os<<"\tip:"<<config->getIP()<<el;
    os<<"\tport:"<<config->getPort()<<el;
    os<<"\tcapacity:"<<config->getCapacity()<<el;
    os<<"\trefresh:"<<config->getRTime()<<el;
    os<<"\tttl:"<<config->getTTL()<<el;
    os<<"\tbacklog:"<<config->getBackLog()<<el;
    os<<"\tname:"<<config->getName()<<el;
    os<<"\tmanager ip:"<<config->getManagerIP()<<el;
    os<<"\tmanager port:"<<config->getManagerPort()<<el;
    os<<"\tremote flag:"<<config->getFlag()<<el;
    String cmd;
    while(true){
        os<<el<<"\tlocal/remote/exit:";
        is>>cmd;
        if(cmd!="local"&&cmd!="remote"&&cmd!="exit"){
            continue;
        }
        if(cmd=="local"){
            std::thread console(run_Console);
            console.join();
        }else if(cmd=="remote"){
            /**
             * report my information to server
             */
            if(config->getFlag()=="true"){
                os<<"i am sending the information to server.....";
                std::thread Report(report_to_server_manage);
                Report.join();
                os<<"report done";
            }
            long fresh=atol(config->getRTime().c_str());
            std::thread freshThread(run_refreshMem,fresh);
            freshThread.detach();
            run_RemoteRunner();
        }else{
            os<<"Bye"<<el;
            break;
        }
    }
    return 0;
}