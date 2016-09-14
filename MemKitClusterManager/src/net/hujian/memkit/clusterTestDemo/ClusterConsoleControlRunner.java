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
package net.hujian.memkit.clusterTestDemo;

import net.hujian.memkit.Server.ServerExecutor;
import net.hujian.memkit.ServerApiClient.API;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

/**
 * Created by hujian on 16-9-9.
 */
public class ClusterConsoleControlRunner {
    /**
     * print out
     * @param out
     */
    private static void print(String out){
        System.out.println(out);
    }
    /**
     * the control runner
     * @param args
     */
    public static void main(String[] args){
        /**
         * drt up the server
         */
        ServerExecutor executor=new ServerExecutor(8888,8889);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /**
         * put the data
         */
        API api=new API("127.0.0.1",8889);
        for(int i=0;i<100;i++){
            api.put(UUID.randomUUID().toString(),"hujian","MemKit 1.0.1");
        }
        BufferedReader input=new BufferedReader(new InputStreamReader(System.in));
        String line="",response="";
        try {
            while(true){
                line=input.readLine();
                switch (line.charAt(0)){
                    case 'm':{
                        response=api.mem();
                        break;
                    }
                    case 'p':{
                        response=api.put(line.split(" ")[1],line.split(" ")[2],line.split(" ")[3]);
                        break;
                    }
                    case 'g':{
                        response=api.get(line.split(" ")[1],line.split(" ")[2]);
                        break;
                    }
                    case 'i':{
                        response=api.info();
                        break;
                    }
                    case 's':{
                        response=api.setC(Long.parseLong(line.split(" ")[1]));
                        break;
                    }
                    case 'e':{
                        response=api.exit();
                        print("server_Response:"+response);
                        System.exit(0);
                    }
                }
                print("server_Response:"+response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
