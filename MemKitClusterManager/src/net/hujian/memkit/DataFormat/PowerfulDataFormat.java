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
package net.hujian.memkit.DataFormat;

/**
 * Created by hujian on 16-9-8.
 */
public class PowerfulDataFormat {
    private String storeId=null;
    private String key=null;
    private String value=null;
    private String ttl=null;
    private String filename=null;
    private String newValue=null;
    private String flushFlag=null;
    private String newCapacity=null;
    private String exit=null;
    /**
     * the only constructor.
     */
    public PowerfulDataFormat(){
        this.exit="exit";
    }
    public void setStoreId(String id){
        this.storeId=id;
    }
    public String getStoreId(){
        return this.storeId;
    }
    public void setKey(String k){
        this.key=k;
    }
    public String getKey(){
        return this.key;
    }
    public void setValue(String v){
        this.value=v;
    }
    public String getValue(){
        return this.value;
    }
    public void setTtl(String t){
        this.ttl=t;
    }
    public String getTtl(){
        return this.ttl;
    }
    public void setFilename(String fm){
        this.filename=fm;
    }
    public String getFilename(){
        return this.filename;
    }
    public void setNewValue(String append){
        this.newValue=append;
    }
    public String getNewValue(){
        return this.newValue;
    }
    public void setFlushFlag(String flag){
        this.flushFlag=flag;
    }
    public String getFlushFlag(){
        return this.flushFlag;
    }
    public void setNewCapacity(String cap){
        this.newCapacity=cap;
    }
    public String getNewCapacity(){
        return this.newCapacity;
    }
    public String getExit(){
        return this.exit;
    }

    /**
     * clear the data
     */
    public void clear(){
          storeId=null;
          key=null;
          value=null;
          ttl=null;
          filename=null;
          newValue=null;
          flushFlag=null;
          newCapacity=null;
    }
}
