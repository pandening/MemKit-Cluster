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
 */
public class SlaveInformation {
    /**
     * the slave's connection
     */
    private JMemKitClient Client=null;
    /**
     * the capacity
     */
    private long Capacity=0;
    /**
     * the size
     */
    private long Size=0;
    /**
     * the name
     */
    String SlaveName=null;

    /**
     * set client for it
     * @param client
     */
    public void setClient(JMemKitClient client){
        this.Client=client;
    }

    /**
     * get the client
     * @return
     */
    public JMemKitClient getClient(){
        return this.Client;
    }

    /**
     * set the capacity,get from mem kit
     * @param capacity
     */
    public void setCapacity(long capacity){
        this.Capacity=capacity;
    }

    /**
     * get
     * @return
     */
    public long getCapacity(){
        return this.Capacity;
    }

    /**
     *
     * @param size
     */
    public void setSize(long size){
        this.Size=size;
    }

    /**
     *
     * @return
     */
    public long getSize(){
        return this.Size;
    }

    /**
     *
     * @param name
     */
    public void setSlaveName(String name){
        this.SlaveName=name;
    }

    /**
     *
     * @return
     */
    public String getSlaveName(){
        return this.SlaveName;
    }
}
