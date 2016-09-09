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
 * just choose a put client for server
 */
public class ChooseAPutClient {
    /**
     * the slaves list
     */
    private SlaveCollection slaveCollection=null;

    /**
     * the constructor,choose by server
     */
    public ChooseAPutClient(){
        this.slaveCollection=SlaveCollection.get_instance();
    }

    /**
     * choose by the name
     * @param slaveName
     * @return
     */
    public JMemKitClient getBySlaveName(String slaveName){
        return this.slaveCollection.getByName(slaveName).getClient();
    }

    /**
     * you will get the suitable client
     * @return
     */
    public JMemKitClient getByServer(){
        /**
         * the base algorithm:calc the capacity-size
         * choose the max
         */
        long dis=0;
        JMemKitClient client=slaveCollection.getSlaves().get(0).getClient();
        for(SlaveInformation slave:this.slaveCollection.getSlaves()){
            if(slave.getCapacity()-slave.getSize()>=dis){
                dis=slave.getCapacity()-slave.getSize();
                client=slave.getClient();
            }
        }
        return client;
    }
}
