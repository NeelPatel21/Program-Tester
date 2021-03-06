/*
* Copyright 2017 Program Tester Team
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at

*     http://www.apache.org/licenses/LICENSE-2.0

* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
* package lib.adminModule;
*/
package lib.userModule.net;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import lib.dT.problemManipulate.IntProgramDetail;
import lib.dT.problemManipulate.ProgramDetails;
import lib.logger.MyLogger;
import lib.ui.IntUI;
import lib.userModule.IntUserFlow;
import lib.userModule.result.IntLiveResultSet;
import lib.userModule.result.IntProgramState;
import lib.userModule.result.IntResultSet;
import lib.userModule.result.ProgramStateAdapter;
import lib.userModule.test.Test;
import net.flow.clientFlow.ClientFlow;
import net.flow.clientFlow.IntNetClient;
import static programtester.config.Configuration.getDefaultDir;
import static programtester.config.Configuration.getDefaultProDir;

/**
 *
 * @author Neel Patel
 */
public class NetUserFlow implements IntUserFlow{
     private IntUI ui; //Refrence to the UI
     private MyLogger logger; //Refrence to the Logger
     private List<ProblemState> ps; //Reference to the live Problem definations.
     private final IntNetClient net;
     private String uName,password;
     //private Thread t;
     //this is executer service used to execute the parallel task.
     private ExecutorService es=Executors.newCachedThreadPool();

     @Override
     public int getCredit() {
          System.out.println("getcredit meth");
          if(net==null)
               return -1;
          System.out.println("credit get");
          return net.userCredit();
     }

     @Override
     public synchronized void refresh(){
          System.out.println("refresh meth");
          try{
               if(net==null)
                    return;
               Map<Long,Integer> u=net.getAllStatus();
               if(u==null)
                    return;
               ps.stream().forEach(i->{
                    i.setState(u.getOrDefault(i.getProgramID(),0));
               });
               System.out.println("refresh meth com");
          }catch(Exception ex){
               System.err.println("refresh meth com err");
               ui.showMessage("error while refreshing");
          }
     }

     /**
      * this is simple extension of class {@code ProgramStateAdapter},
        which implements the {@code IntProblemState}.
      * this class Override the default implementation of {@code setState}
        method.
      */
     private class ProblemState extends ProgramStateAdapter{
          /**
           * constructor
           * @param pd object of type {@code IntProgramDetail} which will be
             used to initialize this object.
           */
          ProblemState(IntProgramDetail pd){
               super(pd);
          }
          
          /**
           * this is synchronized implementation of setState method.
           * this method will simple call the implementation of super class.
           * @param s 
           */
          @Override
          public synchronized void setState(int s){
               super.setState(s);
          }
     }
     
     /**
      * this method is used to update the {@code ProblemState}.
      * this method get the final results from {@code t} and determine if
        the state of the corresponding problem should be updated or not.
      * if the state of the corresponding problem is 1, then it remain as it is.
      * if the state of the corresponding problem is 0 and all the test cases passed
        by the program corresponding to the {@code t} then the state of the
        problem will be updated with 1.
      * this method can be executed in parallel to support live platforms.
      * @param t object of Test for with update operation perform.
      */
     private void update(Test t){
          t.join();
          IntResultSet rs=t.getIntResultSet();
          ProblemState x=ps.stream().filter(i->i.getProgramID()==t.getProgramID())
                  .findAny().get();
          int code=rs.getAllResult().stream().mapToInt(r->r.getMessageCode())
                  .min().orElse(0);
          if(code>x.getState()){
               x.setState(code);
          }
          if(logger!=null){
               String l=logger.getlogString("DateTime = "+LocalDateTime.now()
                         ,"UserName = "+uName
                         //,"Password = "+password
                         ,"PID = "+x.getProgramID()
                         ,"State = "+code);
               logger.log(l);
               if(!net.log(l))
                    ui.showMessage("Remote log error");
          }
          System.gc();
     }
     
     /**
      * this method read and return all the program from default
        program directory.
      * this method internally call {@code ProgramDetails.readProgramDetail()}
        to get problem definition.
      * this process create the new objects of type ProblemState using the
        objects of type {@code IntProgramState}.
      * @return unmodifiable list of IntProgramStates.
      */
     private List<? extends IntProgramState> getPrograms(){
          //System.out.println("suf getPro...");
          List<ProblemState> psl=ProgramDetails.readProgramDetail().stream()
                  //.peek(i->System.out.println("Pid :- "+i.getProgramID()))
                  .map(i->new ProblemState(i)).collect(Collectors.toList());
          psl=Collections.unmodifiableList(psl);
          return psl;
     }
     
     /**
      * constructor.
      * initialize the object with all problem definition read from the default
        program directory.
      */
     public NetUserFlow(){
          System.out.println("Pro :- "+getDefaultProDir());
          System.out.println("test :- "+getDefaultDir());
          net=new ClientFlow();
          //net.regErrRunner(System.out::println);
          //net.regMessageRunner(System.out::println);
     }
     
     /**
      * this method register the user interface.
      * calling this method will replace the previous registered user interface
        with {@code ui}.
      * @param ui object of IntUI.
      */
     @Override
     public synchronized void register(IntUI ui) {
          if(this.ui!=null)
               return;
          this.ui=ui;
          net.regMessageRunner(ui::showMessage);
          net.regErrRunner(ui::showMessage);
          uName=ui.Prompt("enter User Name");
          password=ui.Prompt("enter password");
          for(;!net.init(uName, password);){
               ui.showMessage("username or password wrong.");
               uName=ui.Prompt("enter User Name");
               password=ui.Prompt("enter password");
          }
     }

     /**
      * this method executes the command {@code cmd} with proper inputs
        corresponding to the Program ID specified by {@code pid}.
      * this method internally execute the command with all available inputs
        corresponds to the Program Id available in the default source directory.
      * this method returns live result set immediately and start the processing
        in parallel.
      * this will method update the resultSet in parallel, after returning.
      * @param pid program ID corresponds to the executable command
      * @param cmd executable command
      * @return object of type IntLiveResultSet.
      */
     @Override
     public IntLiveResultSet execute(long pid,String cmd) {
          try {
               if(ps==null)
               ps=(List<ProblemState>)getPrograms();
               Test t=new Test(pid,cmd);
               IntLiveResultSet rt=t.start();
               es.submit(()->update(t));
               return rt;
          } catch (IOException ex) {
               ui.showMessage("file not found !!!");
          }catch(Exception ex){
               ui.showMessage("something wrong !!");
          }
          return null;
     }
     
     /**
      * set logger.
      * @param logger object of logger. 
      */
     @Override
     public synchronized void setLogger(MyLogger logger){
          this.logger=logger;
     }

     /**
      * this method return list of {@code IntProgramState}
        corresponds to the all Program Details.
      * the list return by this method is unmodifiable list.
      * @return unmodifiable list of Program states
      */
     @Override
     public synchronized List<? extends IntProgramState> getAllProgramDetail() {
          if(ps==null)
               ps=Collections.synchronizedList((List<ProblemState>)getPrograms());
          return Collections.unmodifiableList(ps);
     }
     
     /**
      * {@inheritDoc }
      */
     @Override
     public void finalize(){
          es.shutdownNow();
          System.exit(0);
     }
     
     /**
      * {@inheritDoc }
      */
     @Override
     public void close(){
          es.shutdownNow();
          logger.close();
          ui.close();
          System.exit(0);
     }
}
