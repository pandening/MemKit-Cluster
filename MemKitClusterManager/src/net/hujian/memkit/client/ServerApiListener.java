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

import net.hujian.memkit.core.ChooseAPutClient;
import net.hujian.memkit.core.SlaveCollection;
import net.hujian.memkit.core.SlaveInformation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by hujian on 16-9-8.
 * user need to connect to this class to control
 * the mem kit slaves
 */
public class ServerApiListener{
    /**
     * the query client
     */
    private JMemKitClient QueryClient=null;
    /**
     * the slave connection set manage
     */
    private SlaveCollection SlaveList=null;
    /**
     * choose client for put
     */
    private ChooseAPutClient putClientHandler=null;
    /**
     * i am server,please connect to me
     */
    ServerSocket serverSocket=null;
    Socket       clientSocket=null;

    /**
     * api server's port
     * @param port please 8888
     */
    public ServerApiListener(int port){
        try {
            this.serverSocket=new ServerSocket(port);
            this.puppet();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SlaveList=SlaveCollection.get_instance();
        putClientHandler=new ChooseAPutClient();
    }

    /**
     * the operator type
     */
   public enum OPERATE_TYPE{
       EXIT,/*just quit directly*/
       EXIST,/*query...*/
       EXIST_ID,
       DELETE,
       SET_C,
       SET,
       TTL,
       PUT,
       GET,
       DUMP,/*not implement*/
       APPEND,
       FLUSH,
       FLUSH_ALL,
       INFO,/*you should do not use this to get the client*/
       LOAD/*now it's not work.*/
   }

    /**
     * according to the type to choose the client
     * @param type
     * @param store_id can be null
     * @param key can be null
     */
    public void ChooseClientForThisSession(OPERATE_TYPE type,String store_id,String key,String capacity){
        SlaveInformation slaveInformationM=new SlaveInformation();
        switch (type)
      {
          case EXIST:{//just tell me the store@key where..
              slaveInformationM=SlaveList.getByStoreIdAndKey(store_id,key);
              if(slaveInformationM!=null){
                  this.QueryClient=slaveInformationM.getClient();
              }else{
                  this.QueryClient=null;
              }
              break;
          }
          case EXIST_ID:{//tell me the store
              slaveInformationM=SlaveList.getByStoreID(store_id);
              if(slaveInformationM!=null){
                  this.QueryClient=slaveInformationM.getClient();
              }else{
                  this.QueryClient=null;
              }
              break;
          }
          case DELETE:{//delete
              slaveInformationM=SlaveList.getByStoreIdAndKey(store_id,key);
              if(slaveInformationM!=null){
                  this.QueryClient=slaveInformationM.getClient();
              }else{
                  this.QueryClient=null;
              }
              break;
          }
          case SET_C:{//this funny
              SlaveList.setCapacity(capacity);
              break;
          }
          case SET:{//set ttl
              slaveInformationM=SlaveList.getByStoreIdAndKey(store_id,key);
              if(slaveInformationM!=null){
                  this.QueryClient=slaveInformationM.getClient();
              }else{
                  this.QueryClient=null;
              }
              break;
          }
          case TTL:{//get the ttl
              slaveInformationM=SlaveList.getByStoreIdAndKey(store_id,key);
              if(slaveInformationM!=null){
                  this.QueryClient=slaveInformationM.getClient();
              }else{
                  this.QueryClient=null;
              }
              break;
          }
          case PUT:{
              this.QueryClient=this.putClientHandler.getByServer();
              break;
          }
          case GET:{
              slaveInformationM=SlaveList.getByStoreIdAndKey(store_id,key);
              if(slaveInformationM!=null){
                  this.QueryClient=slaveInformationM.getClient();
              }else{
                  this.QueryClient=null;
              }
              break;
          }
          case APPEND:{
              slaveInformationM=SlaveList.getByStoreIdAndKey(store_id,key);
              if(slaveInformationM!=null){
                  this.QueryClient=slaveInformationM.getClient();
              }else{
                  this.QueryClient=null;
              }
              break;
          }
          case FLUSH:{
              slaveInformationM=SlaveList.getByStoreID(store_id);
              if(slaveInformationM!=null){
                  this.QueryClient=slaveInformationM.getClient();
              }else{
                  this.QueryClient=null;
              }
              break;
          }
          case FLUSH_ALL:{
              break;
          }
          case INFO:{
              break;
          }
          case LOAD:{
              break;
          }
          case DUMP:{
              break;
          }
          case EXIT:{
              break;
          }
          default:{
              break;
          }
      }
    }


    /**
     * i will do the real job~
     */
    private class DoJob implements Runnable{
        /**
         * the client socket
         */
        private Socket socket;
        /**
         * the constructor
         * @param clientSock
         */
        public DoJob(Socket clientSock){
           this.socket=clientSock;
        }
        @Override
        public void run() {
            BufferedReader is=null;
            PrintWriter    os=null;
            try {
                is=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                os=new PrintWriter(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(is==null||os==null){
                System.out.println("api client socket error");
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
            /**
             * handler the query from client
             */
            while(true){
                String response=null;
                try {
                    String line=is.readLine();
                    line=line.trim();
                    switch(line.split(" ")[0].charAt(0))
                    {
                        case 'e':{
                            //exit/exist
                            if(line.split(" ")[0].equals("exist")){//exit
                                ChooseClientForThisSession(OPERATE_TYPE.EXIST,line.split(" ")[1],
                                        line.split(" ")[2],null);
                                response=exist(line.split(" ")[1],line.split(" ")[2]);
                                break;
                            }else if(line.split(" ")[0].equals("exit")){//exist
                                os.println("exit");
                                os.flush();
                                is.close();
                                os.close();
                                socket.close();
                                return;
                            }else{//exist id
                                ChooseClientForThisSession(OPERATE_TYPE.EXIST_ID,line.split(" ")[0],
                                        null,null);
                                response=exist(line.split(" ")[0]);
                                break;
                            }
                        }
                        case 'd':{//delete
                            ChooseClientForThisSession(OPERATE_TYPE.DELETE,line.split(" ")[1],
                                    line.split(" ")[2],null);
                            response=delete(line.split(" ")[1],line.split(" ")[2]);
                            break;
                        }
                        case 's':{//set/setC
                            if(line.split(" ")[0].equals("set")){//set
                                ChooseClientForThisSession(OPERATE_TYPE.SET,line.split(" ")[1],
                                        line.split(" ")[2],null);
                                response=set(line.split(" ")[1],line.split(" ")[2],
                                        line.split(" ")[3]);
                            }else{
                                ChooseClientForThisSession(OPERATE_TYPE.SET_C,null,null,line.split(" ")[1]);
                                response="ack";
                            }
                            break;
                        }
                        case 'l':{//load,not work~~
                            break;
                        }
                        case 't':{//ttl
                            ChooseClientForThisSession(OPERATE_TYPE.TTL,line.split(" ")[1],
                                    line.split(" ")[2],null);
                            response=ttl(line.split(" ")[1],line.split(" ")[2]);
                            break;
                        }
                        case 'g':{//get
                            ChooseClientForThisSession(OPERATE_TYPE.GET,line.split(" ")[1],
                                    line.split(" ")[2],null);
                            response=get(line.split(" ")[1],line.split(" ")[2]);
                            break;
                        }
                        case 'a':{//append
                            ChooseClientForThisSession(OPERATE_TYPE.GET,line.split(" ")[1],
                                    line.split(" ")[2],null);
                            response=append(line.split(" ")[1],line.split(" ")[2]
                            ,line.split(" ")[3]);
                            break;
                        }
                        case 'p':{//put
                            ChooseClientForThisSession(OPERATE_TYPE.PUT,null,null,null);
                            response=put(line.split(" ")[1],line.split(" ")[2],
                                    line.split(" ")[3]);
                            break;
                        }
                        case 'f':{//flush/flushAll
                            if(line.split(" ")[0].equals("flush")){//flush
                                ChooseClientForThisSession(OPERATE_TYPE.FLUSH,line.split(" ")[1],
                                        null,null);
                                response=flush(line.split(" ")[1]);
                            }else{
                                SlaveList.flushALL();
                                response="ack";
                            }
                            break;
                        }
                        case 'i':{//info
                            response=info();
                            break;
                        }
                        case 'm':{
                            response=mem();
                            break;
                        }
                        default:{//do nothing
                            System.out.println("unknown command from client.");
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //send..
                if(!response.isEmpty()){
                    if(!response.startsWith("ack_")){
                        response="ack_"+response;
                    }
                    os.println(response);
                    os.flush();
                }
            }
        }
    }

    /**
     * this is the puppet
     */
    private void puppet(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        clientSocket = serverSocket.accept();
                        new DoJob(clientSocket).run();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * set the ttl for the store_id@key
     * @param store_id
     * @param key
     * @param ttl
     * @return
     */
    public String set(String store_id,String key,String ttl){
        if(QueryClient!=null) {
            return QueryClient.set(store_id, key, ttl);
        }else{
            return "fail";
        }
    }

    /**
     * get the store_id@key's ttl
     * @param store_id
     * @param key
     * @return
     */
    public String ttl(String store_id,String key){
        if(QueryClient!=null) {
            return QueryClient.ttl(store_id, key);
        }else{
            return "fail";
        }
    }

    /**
     * get an value from server
     * @param store_id
     * @param key
     * @return
     */
    public String get(String store_id,String key){
        if(QueryClient!=null) {
            return QueryClient.get(store_id, key);
        }else{
            return "fail";
        }
    }

    /**
     * append an value to the key
     * @param store_id
     * @param key
     * @param append_value
     * @return
     */
    public String append(String store_id,String key,String append_value){
        if(QueryClient!=null) {
            return QueryClient.append(store_id, key, append_value);
        }else{
            return "fail";
        }
    }

    /**
     * put a key-value to mem
     * @param store_id
     * @param key
     * @param value
     * @return
     */
    public String put(String store_id,String key,String value){
        if(QueryClient!=null) {
            return QueryClient.put(store_id, key, value);
        }else{
            return "fail";
        }
    }

    /**
     * flush the store
     * @param store_id
     * @return
     */
    public String flush(String store_id){
        if(QueryClient!=null) {
            return QueryClient.flush(store_id);
        }else{
          return "fail";
        }
    }
    /**
     * get simple info from server
     * @return capacity,size
     */
    public String info(){
        if(QueryClient!=null) {
            return SlaveList.getClusterInfo();
        }else{
            return "fail";
        }
    }

    /**
     * delete a key-value
     * @param store_id
     * @param key
     * @return
     */
    public  String delete(String store_id,String key){
        if(QueryClient!=null) {
            return QueryClient.delete(store_id, key);
        }else{
            return "fail";
        }
    }

    /**
     * check the id@key
     * @param store_id
     * @param key
     * @return
     */
    public String exist(String store_id,String key){
        if(QueryClient!=null) {
            return QueryClient.exist(store_id, key);
        }else{
            return "fail";
        }
    }
    /**
     * check the store id
     * @param store_id
     * @return
     */
    public String exist(String store_id){
        if(QueryClient!=null) {
            return QueryClient.exist(store_id);
        }else{
            return "fail";
        }
    }

    /**
     * alive check
     * @return
     */
    public String alive(){
        if(QueryClient!=null){
            return QueryClient.alive();
        }else{
            return "fail";
        }
    }

    public String mem(){
        if(QueryClient!=null){
            return QueryClient.mem();
        }else{
            return "fail";
        }
    }
}
