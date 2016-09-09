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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hujian on 16-9-8.
 */
public class SlaveCollection {
    /**
     * the only instance
     */
    private static SlaveCollection _instance=null;
    /**
     * the slaves list
     */
    private List<SlaveInformation> Slaves=null;

    /**
     * get the slaves
     * @return
     */
    public List<SlaveInformation> getSlaves(){
        return this.Slaves;
    }

    /**
     * check
     * @param info
     * @return
     */
    public boolean exist(SlaveInformation info){
        for(SlaveInformation slave:Slaves){
            if(slave.getSlaveName().equals(info.getSlaveName())){
                return true;
            }
        }
        return false;
    }

    /**
     * put a slave into list
     * @param slave
     */
    public void putSlave(SlaveInformation slave){
        if(this.Slaves.contains(slave)){
            System.out.println("the slave already existed=>"+slave.getSlaveName());
            System.out.println(getClusterInfo());
        }else{
            this.Slaves.add(slave);
        }
    }

    /**
     * get the size
     * @return
     */
    public int clusterSize(){
        return this.Slaves.size();
    }

    /**
     * get the info of cluster to response the client's query
     * @return slaveTotalCapacity slaveTotalSize
     */
    public String getClusterInfo(){
        long capacity=0;
        long size=0;
        for(SlaveInformation slave:Slaves){
            capacity+=slave.getCapacity();
            size+=slave.getSize();
        }
        return String.valueOf(capacity)+" "+String.valueOf(size);
    }

    /**
     * just give me the capacity,i will choose to set
     * @param capacity
     */
    public void setCapacity(String capacity){
        long maxCapacity=-1;
        for(SlaveInformation slave:Slaves){
            if(Long.parseLong(slave.getClient().info().substring(4).split(",")[0])>=maxCapacity){
                maxCapacity=Long.parseLong(slave.getClient().info().substring(4).split(",")[0]);
            }
        }
        /**
         * add..
         */
        long totalCapacity=Long.parseLong(capacity);
        long setCp=0;
        for(SlaveInformation slave:Slaves){
            setCp=maxCapacity-Long.parseLong(slave.getClient().info().substring(4).split(",")[0]);
            if(setCp!=0){
                if(totalCapacity>=setCp) {
                    slave.getClient().setC(setCp);
                    totalCapacity-=setCp;
                }else{
                    slave.getClient().setC(totalCapacity);
                    return;
                }
            }
        }
        if(totalCapacity!=0){
            setCp=totalCapacity/Slaves.size();
            for(SlaveInformation slave:Slaves){
                slave.getClient().setC(setCp);
            }
        }
    }

    /**
     * flush all
     */
    public void flushALL(){
        for(SlaveInformation slave:this.Slaves){
            slave.getClient().flushAll();
        }
    }

    /**
     * get a slave info by the name
     * @param name
     * @return
     */
    public SlaveInformation getByName(String name){
        for(SlaveInformation slave:this.Slaves){
            if(slave.getSlaveName().equals(name)){
                return slave;
            }
        }
        /**
         * so,if you get null from this function,just means not exist
         */
        return null;
    }

    /**
     * get a client according the store id and the key
     * @param store_id
     * @param key
     * @return
     */
    public SlaveInformation getByStoreIdAndKey(String store_id,String key){
        for(SlaveInformation slave:this.Slaves){
            if(slave.getClient()!=null) {
                if (slave.getClient().exist(store_id, key).endsWith("true")) {
                    return slave;
                }
            }
        }
        return null;
    }

    /**
     * get the slave by the store id
     * @param store_id
     * @return
     */
    public SlaveInformation getByStoreID(String store_id){
        for(SlaveInformation slave:this.Slaves){
            if(slave.getClient().exist(store_id).startsWith("ack")){
                return slave;
            }
        }
        return null;
    }

    /**
     * the constructor
     */
    private SlaveCollection(){
        this.Slaves=new ArrayList<SlaveInformation>();
        Timer updateTimer=new Timer();
        updateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                for(SlaveInformation slaveInformation:Slaves){
                    if(slaveInformation.getClient()==null){
                        Slaves.remove(slaveInformation);
                        System.out.print("Remove an null connection");
                        continue;
                    }
                    System.out.println("capacity+size=>" + slaveInformation.getClient().info());
                    long capacity=Long.parseLong(slaveInformation.getClient().info().
                            substring(4).split(",")[0]);
                    long size=Long.parseLong(slaveInformation.getClient().info().
                            substring(4).split(",")[1]);
                    System.out.println("new capacity,new size=>"+capacity+","+size);
                    slaveInformation.setCapacity(capacity);
                    slaveInformation.setSize(size);
                }
            }
        },60000,10000);//10s to update
    }

    /**
     * get the only instance
     * @return
     */
    public static SlaveCollection get_instance(){
        if(_instance==null){
            _instance=new SlaveCollection();
        }
        return _instance;
    }
}
