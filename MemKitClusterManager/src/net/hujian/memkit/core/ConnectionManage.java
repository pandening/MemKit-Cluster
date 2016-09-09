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
package net.hujian.memkit.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by hujian on 16-9-8.
 */
public class ConnectionManage{
    /**
     * the server/client socket
     */
    ServerSocket serverSocket=null;
    Socket       clientSocket=null;
    /**
     * reader/writer
     */
    BufferedReader is=null;
    PrintWriter    os=null;
    /**
     * response to slave
     */
    String response=null;
    /**
     * get message from client
     */
    String receive=null;
    /**
     * the listener
     */
    private OnConnectionListener Listener=null;
    /**
     * you should set a listener for this.
     * @param listener
     */
    public void setListener(OnConnectionListener listener){
        this.Listener=listener;
        this.puppet();
    }
    /**
     * just wait the connection
     */
    private void puppet(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        clientSocket=serverSocket.accept();
                        /**
                         * get the is/os
                         */
                        is=new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        os=new PrintWriter(clientSocket.getOutputStream());
                        receive=is.readLine();
                        /**
                         * tell slave i know who are you
                         */
                        os.println(response);
                        os.flush();
                        /**
                         * remember close this connection
                         */
                        is.close();
                        os.close();
                        clientSocket.close();
                        try {
                            /**
                             * 5s to check..
                             */
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    /**
                     * if recv the message,just tell the listener
                     */
                    Listener.newConnection(receive);
                }
            }
        }).start();
    }
    /**
     * the constructor
     * @param port port to listen new slave comes
     */
    public ConnectionManage(int port){
        try {
            this.serverSocket=new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.response="ack_";
    }


    /**
     * the listener,if new slave coming,you will get the information
     * from the msg
     */
    public interface OnConnectionListener{
        /**
         * you can get the ip,port,capacity,size,name from slave
         * @param information
         */
        public void newConnection(String information);
    }
}
