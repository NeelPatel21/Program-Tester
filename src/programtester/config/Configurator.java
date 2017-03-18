/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programtester.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import lib.dT.problemManipulate.ProgramDetails;
import lib.logger.LocalLogger;
import lib.userModule.test.Test;

/**
 *
 * @author Neel Patel
 */
public class Configurator {
     private Configurator(){}
     public static void init(){
          Path p=Paths.get("config.txt").toAbsolutePath();
          try {
               Files.lines(p).map(i->i.trim())
                       .filter(i->!i.startsWith("#"))
                       //.peek(i->System.out.println("peek :- "+i))
                       .forEach(i->proc(i));
          } catch (IOException ex) {
               ex.printStackTrace();
          }
                  
     }
     
     private static void proc(String s){
          String ar[]=Arrays.stream(s.split(":-",2)).map(i->i.trim())
                  .toArray(String[]::new);
          switch(ar[0]){
               /*case "working_dir":
                    Path p=Paths.get(ar[1]).toAbsolutePath();
                    System.out.println("wd :- "+p);
                    if(Files.exists(p)&&Files.isDirectory(p)){
                         System.out.println("test");
                         System.setProperty("user.dir",p.toString());
                    }
                    break;
               */
               
               case "source_data_dir":
                    Path p2=Paths.get(ar[1]).toAbsolutePath();
                    //System.out.println("sdd :- "+p2);
                    if(Files.exists(p2)&&Files.isDirectory(p2))
                         Configuration.setDefaultDir(p2);
                    break;
               case "problem_dir":
                    Path p3=Paths.get(ar[1]).toAbsolutePath();
                    if(Files.exists(p3)&&Files.isDirectory(p3))
                         Configuration.setDefaultProDir(p3);
                    break;
               case "local_logger_dir":
                    Path p4=Paths.get(ar[1]).toAbsolutePath();
                    try {
                         Files.createDirectories(p4);
                    } catch (IOException ex) {}
                    if(Files.exists(p4)&&Files.isDirectory(p4))
                         Configuration.setDefaultLogDir(p4);
                    break;
               case "user_details":
                    Path p5=Paths.get(ar[1]).toAbsolutePath();
                    if(Files.exists(p5)&&!Files.isDirectory(p5))
                         Configuration.setDefaultUserDetailPath(p5);
                    break;
               case "network_logger_ip":
                    break;
               case "parallel_execution":
                    boolean b=Boolean.parseBoolean(ar[1]);
                    Configuration.setIsParallel(b);
                    break;
               case "parallel_thread":
                    try{
                         int i=Integer.parseInt(ar[1]);
                         System.setProperty("java.util.concurrent.ForkJoinPool"
                                 + ".common.parallelism",""+i);
                         
                    }catch(NumberFormatException e){}
                    break;
               case "rmi_port":
                    try{
                         int p=Integer.parseInt(ar[1]);
                         Configuration.setDefaultRMIPort(p);
                    }catch(NumberFormatException e){}
                    break;
               case "main_server":
                    Configuration.setDefaultMainSer(ar[1]);
                    break;
               case "main_data_server":
                    Configuration.setDefaultMainDataSer(ar[1]);
                    break;
               case "main_remote_logger":
                    Configuration.setDefaultMainLogSer(ar[1]);
                    break;
               case "data_server":
                    Configuration.setDefaultDataSer(ar[1]);
                    break;
                    
          }
     }
}

/*
working_dir :- .\resources\
problem_dir :- .\resources\data
source_data_dir :- .\resources\data
local_logger_dir :- .\resources\
network_logger_ip :- 127.0.0.1
network_logger_port :- 8686



*/