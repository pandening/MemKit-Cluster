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

import net.hujian.memkit.client.JMemKitClient;

/**
 * Created by hujian on 16-9-8.
 * this class will add the new coming slave into the slaves list
 * just do this~
 */
public class HandlerNewSlave {
    /**
     * the slaves list
     */
    private SlaveCollection SlaveList=null;
    /**
     * the new slave
     */
    private SlaveInformation slave=null;
    /**
     * get an instance of slave connection manage
     */
    private ConnectionManage manage=null;
    /**
     * the ip,port
     */
    private String IP=null,Port=null;
    /**
     * the client connection
     */
    private JMemKitClient jMemKitClient=null;

    /**
     * the constructor
     * @param port you should offer an port~
     */
    public HandlerNewSlave(int port){
        this.SlaveList=SlaveCollection.get_instance();
        manage=new ConnectionManage(port);
        slave=new SlaveInformation();
        this.puppet();
    }

    /**
     * the puppet
     */
    private void puppet(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                manage.setListener(new ConnectionManage.OnConnectionListener() {
                    @Override
                    public void newConnection(String information) {
                        /**
                         * i am recv the new slaves
                         * create an instance,then add it~
                         * capacity,size,ip,port,name
                         */
                        information=information.substring(4).trim();
                        slave=new SlaveInformation();
                        slave.setCapacity(Long.parseLong(information.split(",")[0]));
                        slave.setSize(Long.parseLong(information.split(",")[1]));
                        IP=information.split(",")[2];
                        Port=information.split(",")[3];
                        slave.setSlaveName(information.split(",")[4]);
                        /**
                         * get a connection to client
                         */
                        jMemKitClient=JMemKitClient.getInstance(IP,Integer.parseInt(Port));
                        slave.setClient(jMemKitClient);
                        /**
                         * add it
                         */
                        SlaveList.putSlave(slave);
                    }
                });
            }
        }).start();
    }
}
