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
package net.hujian.memkit.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by hujian on 16-9-7.
 * this is the java-mem kit client
 */
public class JMemKitClient {
    /**
     * this is the instance of this class
     */
    private static JMemKitClient _instance=null;
    /**
     * the sock
     */
    Socket socket=null;
    /**
     * the writer
     */
    PrintWriter writer=null;
    /**
     * the reader
     */
    BufferedReader reader=null;

    /**
     * the constructor,it's private
     */
    private JMemKitClient(String ip,int port){
        /**
         * get the sock
         */
        try {
            socket=new Socket(ip,port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * get the is and os
         */
        try {
            writer=new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * get the only instance
     * @return
     */
    public static JMemKitClient getInstance(String ip,int port){
        _instance=new JMemKitClient(ip,port);
        return _instance;
    }

    /**
     * send a msg
     * @param msg
     */
    private void send(String msg){
        writer.println(msg);
        writer.flush();
    }

    /**
     * when you need to receive from mem kit server
     * @return
     */
    private String recv(){
        try {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * load,you should offer a right path.
     * @param filepath
     * @return
     */
    public String load(String filepath){
        this.send("load "+filepath);
        return this.recv();
    }

    /**
     * set the ttl for the store_id@key
     * @param store_id
     * @param key
     * @param ttl
     * @return
     */
    public String set(String store_id,String key,String ttl){
        this.send("set " + store_id + " " + key + " " + ttl);
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
     * dump to file
     * @param flag true means you want to clear the old mem
     * @return
     */
    public String dump(String flag){
        this.send("dump "+flag);
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
        this.send("flusha");
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
        this.send("setc "+capacity);
        return this.recv();
    }

    /**
     * delete a key-value
     * @param store_id
     * @param key
     * @return
     */
    public  String delete(String store_id,String key){
        this.send("rm "+store_id+" "+key);
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
     * puts
     * @param ikvs
     * @return
     */
    public String ps(String ikvs){
        this.send("ps "+ikvs);
        return this.recv();
    }

    /**
     * get the keys list
     * @return
     */
    public String ks(){
        this.send("ks");
        return this.recv();
    }

    /**
     * get the store id's keys list
     * @param store_id
     * @return
     */
    public String ks(String store_id){
        this.send("ks "+store_id);
        return this.recv();
    }

    /**
     * get the stores list
     * @return
     */
    public String ss(){
        this.send("ss");
        return this.recv();
    }

    /**
     * rename the store id
     * @param old_id
     * @param new_id
     * @return
     */
    public String rs(String old_id,String new_id){
        this.send("rs "+old_id+" "+new_id);
        return this.recv();
    }

    /**
     * rename the key
     * @param id
     * @param ok
     * @param nk
     * @return
     */
    public String rk(String id,String ok,String nk){
        this.send("rk "+id+" "+ok+" "+nk);
        return this.recv();
    }

    /**
     * replace the value
     * @param id
     * @param k
     * @param nv
     */
    public String re(String id,String k,String nv){
        this.send("re "+id+" "+k+" "+nv);
        return this.recv();
    }

    /**
     * get an random key
     * @return
     */
    public String rd(){
        this.send("rd");
        return this.recv();
    }

    /**
     * get the mem info
     * @return
     */
    public String mem(){
        this.send("mem");
        return this.recv();
    }

    /**
     * alive check
     * @return
     */
    public String alive(){
        this.send("hh");
        return this.recv();
    }

    /**
     * just close the connect from server
     * @return
     */
    public String exit(){
        this.send("exit");
        String rt=this.recv();
        this.writer.close();
        try {
            this.reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rt;
    }
}
