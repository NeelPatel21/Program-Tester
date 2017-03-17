/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.flow.logSerFlow;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import net.UrlTools;
import net.logSer.RemoteLog;
import net.mainSer.MainSer;
import net.mainSer.SerDetails;
import static programtester.config.Configuration.getDefaultMainDataSer;
import static programtester.config.Configuration.getDefaultMainLogSer;
import static programtester.config.Configuration.getDefaultMainSer;
import static programtester.config.Configuration.getDefaultRMIPort;

/**
 *
 * @author Neel Patel
 */
public class RemoteLogFlow {
     private Thread t=null;
     private Scanner sc=new Scanner(System.in);
     private boolean flag=false;
     private String logSer;
     private RemoteLog remoteObj;
     private Registry r;
     private int port;
     public RemoteLogFlow(){}
     
     private void run(){
          if(!init())
               return;
          flag=true;
          System.out.println("enter 0 to exit");
          for(;flag;){
               try{
                    if(sc.nextInt()==0)
                    break;
               }catch(Exception ex){ 
               }
          }
     }
     
     private boolean init(){
          this.logSer=getDefaultMainLogSer();
          this.port=getDefaultRMIPort();
          try {
               remoteObj=new RemoteLog();
               System.out.println("Enter Backup Logger..");
               String s;
               for(s=sc.nextLine();s.trim().isEmpty();s=sc.nextLine());
               if(remoteObj.setBackupLogger(s))
                    System.err.println("Backup Logger Registration fail");
          } catch (Exception ex) {
               System.err.println("Object creation error");
               return false;
          }
          try{
               r=LocateRegistry.createRegistry(port);
          }catch(Exception ex){
               System.err.println("Registry fail");
          }
          try{
               if(!UrlTools.registerObj(remoteObj,logSer))
                    throw new RemoteException();
          }catch(Exception ex){
               System.out.println("Object Binding fail");
               return false;
          }
          return true;
     }
     
     public void start(){
          if(t!=null&&t.isAlive())
               return;
          t=new Thread(this::run);
          t.start();
     }
     
     public void stop(){
          flag=false;
     }
}