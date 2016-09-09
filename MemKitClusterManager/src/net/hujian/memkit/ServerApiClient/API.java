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
package net.hujian.memkit.ServerApiClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by hujian on 16-9-9.
 */
public class API {
    /**
     * i want to connection to api server to get the server
     */
    private Socket  socket=null;
    /**
     * os/is
     */
    private BufferedReader is=null;
    private PrintWriter    os=null;
    /**
     * api port,please use 8888
     */
    private int apiPort=8888;
    /**
     * the api server ip
     */
    private String serverIP="127.0.0.1";

    /**
     * please offer the api server ip+port
     * @param ip
     * @param port
     */
    public API(String ip,int port){
        this.apiPort=port;
        this.serverIP=ip;
        try {
            socket=new Socket(this.serverIP,this.apiPort);
            is=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            os=new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ready...
     * @return
     */
    public boolean ready(){
        try {
            is=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            return this.is.ready();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * send a msg
     * @param msg
     */
    private void send(String msg){
        os.println(msg);
        os.flush();
    }

    /**
     * when you need to receive from mem kit server
     * @return
     */
    private String recv(){
        try {
            return is.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * set the ttl for the store_id@key
     * @param store_id
     * @param key
     * @param ttl
     * @return
     */
    public String set(String store_id,String key,String ttl){
        this.send("set "+store_id+" "+key+" "+ttl);
        return this.recv();
    }

    /**
     * get the store_id@key's ttl
     * @param store_id
     * @param key
     * @return
     */
    public String ttl(String store_id,String key){
        this.send("ttl "+store_id+" "+key);
        return this.recv();
    }

    /**
     * get an value from server
     * @param store_id
     * @param key
     * @return
     */
    public String get(String store_id,String key){
        this.send("get "+store_id+" "+key);
        return this.recv();
    }


    /**
     * append an value to the key
     * @param store_id
     * @param key
     * @param append_value
     * @return
     */
    public String append(String store_id,String key,String append_value){
        this.send("append "+store_id+" "+key+" "+append_value);
        return this.recv();
    }

    /**
     * put a key-value to mem
     * @param store_id
     * @param key
     * @param value
     * @return
     */
    public String put(String store_id,String key,String value){
        this.send("put "+store_id+" "+key+" "+value);
        return this.recv();
    }

    /**
     * flush the store
     * @param store_id
     * @return
     */
    public String flush(String store_id){
        this.send("flush "+store_id);
        return this.recv();
    }

    /**
     * just clear the cache
     * @return
     */
    public String flushAll(){
        this.send("flushA");
        return this.recv();
    }

    /**
     * get simple info from server
     * @return capacity,size,ip,port
     */
    public String info(){
        this.send("info");
        return this.recv();
    }

    /**
     * set new capacity
     * NOTICE:YOU WILL LOST DATA IF THE NEW CAPACITY<OLD CAPACITY
     * @param capacity
     * @return
     */
    public String setC(long capacity){
        this.send("setC "+capacity);
        return this.recv();
    }

    /**
     * delete a key-value
     * @param store_id
     * @param key
     * @return
     */
    public  String delete(String store_id,String key){
        this.send("delete "+store_id+" "+key);
        return this.recv();
    }

    /**
     * check the id@key
     * @param store_id
     * @param key
     * @return
     */
    public String exist(String store_id,String key){
        this.send("exist "+store_id+" "+key);
        return this.recv();
    }

    /**
     * check the store id
     * @param store_id
     * @return
     */
    public String exist(String store_id){
        this.send("existID "+store_id);
        return this.recv();
    }

    /**
     * just close the connect from server
     * @return
     */
    public String exit(){
        this.send("exit");
        return this.recv();
    }

}
