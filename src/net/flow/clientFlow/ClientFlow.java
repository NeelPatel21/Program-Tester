/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.flow.clientFlow;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.UrlTools;
import net.flow.dataSerFlow.DataSerFlow;
import net.logSer.IntRemoteLog;
import net.mainSer.IntMainSer;
import net.mainSer.userStatus.IntUserStatus;
import static programtester.config.Configuration.getDefaultMainSer;

/**
 *
 * @author Neel Patel
 */
public class ClientFlow implements IntNetClient{
     //local
     private Thread t=null;
     private Consumer<String> errRun=x->{},messageRun=x->{};
     private Scanner sc=new Scanner(System.in);
     private boolean flag=false;
     private String mainSer;
     private String uName="",passwd="";
     private Registry r;
     private int port;
     //remote reference
     private IntMainSer mainObj;
     private IntRemoteLog logOb;
     public ClientFlow(){}
     
     private void run(){
          flag=true;
          for(;flag;){
               try{
                    try {
                         Thread.sleep(1000);
                    } catch (InterruptedException ex) {}
                    if(!mainObj.aya()){
                         assert true:"main server says not alive";
                         return;
                    }
                    if(!logOb.aya()){
                         assert true:"log server says not alive";
                         return;
                    }
               }catch(RemoteException ex){
                    return;
               }
          }
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

     @Override
     public boolean init(String uName,String passwd){
          this.mainSer=getDefaultMainSer();
          try{
               System.out.println("Main ser..."+mainSer);
               mainObj=(IntMainSer)Naming.lookup(mainSer);
               
               IntUserStatus u=mainObj.getStatus(uName, passwd);
               if(u==null){
                    errRun.accept("wrong username or Password");
                    return false;
               }
          }catch(Exception ex){
               System.err.println("Error in getting reference of Remote."+ex);
               ex.printStackTrace();
               return false;
          }
          try {
               logOb=(IntRemoteLog)Naming.lookup(mainObj.getLogSer());
               if(logOb==null)
                    return false;
               if(!logOb.aya()){
                    assert true:"logger says not alive";
                    return false;
               }
               DataSerFlow d=new DataSerFlow(mainSer);
               d.start();
               d.join();
               //code for register User State as a backup logger.
               //IntRemoteLog rg=(IntRemoteLog)Naming.lookup(mainLogSer);
               //rg.setBackupLogger(UserFactory.init(mainLogSer));
          } catch (Exception ex) {
               System.err.println("Server error");
               return false;
          }
          start();
          return true;
     }
     
     @Override
     public boolean regErrRunner(Consumer r) {
          errRun=r::accept;
          return true;
     }

     @Override
     public boolean regMessageRunner(Consumer r) {
          messageRun=r::accept;
          return true;
     }

     @Override
     public boolean log(String s) {
          try{
               if(logOb==null)
                    return false;
               if(!logOb.aya())
                    assert true:"log server says not alive";
               return logOb.log(s);
          }catch(Exception ex){
               return false;
          }
     }

     @Override
     public int credit(long pid){
          try {
               if(mainObj==null)
                    return -1;
               if(!mainObj.aya())
                    assert true:"log server says not alive";
               IntUserStatus u=mainObj.getStatus(uName, passwd);
               return u.getAllProStatus().get(pid);
          } catch (Exception ex) {
               return -1;
          }
     }

     @Override
     public int userCredit() {
          try {
               if(mainObj==null)
                    return -1;
               if(!mainObj.aya())
                    assert true:"log server says not alive";
               IntUserStatus u=mainObj.getStatus(uName, passwd);
               return u.getUserCredit();
          } catch (Exception ex) {
               return -1;
          }
     }
}
