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
package programtester;

import java.io.FileNotFoundException;
import lib.adminModule.AdminFlow;
import lib.logger.LocalLogger;
import lib.logger.MyLogger;
import lib.ui.IntUI;
import lib.ui.cli.CliUser;
import lib.ui.gui.UserGUI;
import lib.userModule.IntUserFlow;
import lib.userModule.local.SingleUserFlow;
import lib.userModule.net.NetUserFlow;
import net.flow.dataSerFlow.MainDataSerFlow;
import net.flow.logSerFlow.RemoteLogFlow;
import net.flow.mainSerFlows.MainSerFlow;
import programtester.config.Configurator;

/**
 *
 * @author Neel Patel
 */
public class
ProgramTester {

     /**
      * @param args the command line arguments
      */
     public static void main(String[] args) {
          //mainUserGui();
          //mainUserCli();
          if(args.length>0&&args[0].equalsIgnoreCase("admin"))
               mainAdminCli();
          else if(args.length>0&&args[0].equalsIgnoreCase("localuser"))
               mainUserGui();
          else if(args.length>0&&args[0].equalsIgnoreCase("localusercli"))
               mainUserCli();
          else if(args.length>0&&args[0].equalsIgnoreCase("netusercli"))
               mainNetUserCli();
          else mainNetUserGui();
     }
     
     public static void mainUserCli(){
          Configurator.init();
          
          //Test.setDefaultDir(Paths.get("Data").toAbsolutePath());
          //System.out.println("Working Directory = " +System.getProperty("user.dir"));
          //System.out.println("Data Directory = " +Test.getDefaultDir());
          //System.out.println("Program Directory = " +ProgramDetails.getDefaultDir());
          IntUserFlow uf=new SingleUserFlow();
          try {
               MyLogger l=new LocalLogger("log.txt");
               l.setSep(",");
               uf.setLogger(l);
          } catch (FileNotFoundException ex) {
               System.out.println("error in log file.");
          }
          IntUI cli=new CliUser(uf);
          cli.start();
          //System.exit(0);
     }
     public static void mainUserGui(){
          Configurator.init();
          
          //Test.setDefaultDir(Paths.get("Data").toAbsolutePath());
          //System.out.println("Working Directory = " +System.getProperty("user.dir"));
          //System.out.println("Data Directory = " +Test.getDefaultDir());
          //System.out.println("Program Directory = " +ProgramDetails.getDefaultDir());
          IntUserFlow uf=new SingleUserFlow();
          try {
               MyLogger l=new LocalLogger("log.txt");
               l.setSep(",");
               uf.setLogger(l);
          } catch (FileNotFoundException ex) {
               System.out.println("error in log file.");
          }
          IntUI cli=new UserGUI(uf);
          cli.start();
          //System.exit(0);
     }
     public static void mainNetUserGui(){
          Configurator.init();
          
          //Test.setDefaultDir(Paths.get("Data").toAbsolutePath());
          //System.out.println("Working Directory = " +System.getProperty("user.dir"));
          //System.out.println("Data Directory = " +Test.getDefaultDir());
          //System.out.println("Program Directory = " +ProgramDetails.getDefaultDir());
          IntUserFlow uf=new NetUserFlow();
          try {
               MyLogger l=new LocalLogger("log.txt");
               l.setSep(",");
               uf.setLogger(l);
          } catch (FileNotFoundException ex) {
               System.out.println("error in log file.");
          }
          IntUI cli=new UserGUI(uf);
          cli.start();
          //System.exit(0);
     }
     public static void mainNetUserCli(){
          Configurator.init();
          
          //Test.setDefaultDir(Paths.get("Data").toAbsolutePath());
          //System.out.println("Working Directory = " +System.getProperty("user.dir"));
          //System.out.println("Data Directory = " +Test.getDefaultDir());
          //System.out.println("Program Directory = " +ProgramDetails.getDefaultDir());
          IntUserFlow uf=new NetUserFlow();
          try {
               MyLogger l=new LocalLogger("log.txt");
               l.setSep(",");
               uf.setLogger(l);
          } catch (FileNotFoundException ex) {
               System.out.println("error in log file.");
          }
          IntUI cli=new CliUser(uf);
          cli.start();
          //System.exit(0);
     }
     
     public static void mainAdminCli(){
          Configurator.init();
          new AdminFlow().start();
          System.exit(0);
     }
     
     public static void mainSerCli(){
          Configurator.init();
          new MainSerFlow().start();
     }
     
     public static void mainDataSerCli(){
          Configurator.init();
          new MainDataSerFlow().start();
     }
     
     public static void mainLogSerCli(){
          Configurator.init();
          new RemoteLogFlow().start();
     }
}
