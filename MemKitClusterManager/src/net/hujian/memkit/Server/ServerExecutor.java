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
package net.hujian.memkit.Server;

import net.hujian.memkit.client.ServerApiListener;
import net.hujian.memkit.core.HandlerNewSlave;
import net.hujian.memkit.core.SlaveCollection;

/**
 * Created by hujian on 16-9-8.
 * this class will set up the listener,and the client api.
 */
public class ServerExecutor {
    /**
     * the server api listener
     */
    private ServerApiListener serverApiListener=null;
    /**
     * accept new slave
     */
    private HandlerNewSlave handlerNewSlave=null;
    /**
     * the slave list
     */
    private SlaveCollection slaveList=null;
    /**
     * the constructor to set up the server
     * @param connectionPort
     * @param ApiPort
     */
    public ServerExecutor(int connectionPort, int ApiPort){
        this.serverApiListener=new ServerApiListener(ApiPort);
        this.handlerNewSlave=new HandlerNewSlave(connectionPort);
        this.slaveList=SlaveCollection.get_instance();
    }

    /**
     * you can use the api after this function return true
     * @return
     */
    public boolean isReady(){
        System.out.println("size=>" + slaveList.clusterSize());
        return this.slaveList.clusterSize()!=0;
    }
}
