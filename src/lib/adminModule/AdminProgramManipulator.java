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
package lib.adminModule;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;
import lib.dT.problemManipulate.IntProgramDetail;
import lib.dT.problemManipulate.ProgramDetails;
import static programtester.config.Configuration.getDefaultProDir;

/**
 *
 * @author admin
 */
public class AdminProgramManipulator {
     private static Scanner sc=new Scanner(System.in);
     public static synchronized void start(){
          while(true){
               System.out.println("\nMenu > Program Defination manipulation..\n"
                         + "1.add Program Defination\n"
                         + "2.show Program Detail\n"
                         + "3.show default Path\n"
                         + "0.exit from Program Defination manipulation\n");
               switch(sc.nextInt()){
                    case 1:addProgramDefination();
                              break;
                    case 2:showProgramDetail();
                              break;
                    case 3:showDefaultPath();
                              break;
                    case 0:return;
                    default: System.out.println("Invalid input");
               }
          }
     }
     
     public static void addProgramDefination(){
          try {
               Path dir=getDefaultProDir();
               System.out.println("enter file name = ");
               String s;
               for(s=sc.nextLine();s.trim().isEmpty();s=sc.nextLine());
               IntProgramDetail x=ProgramDetails.parseProgramDetail(dir.resolve(s));
               System.out.println("\nProgram Id :\t"+x.getProgramID());
               System.out.println("\nTitle :\t\t"+x.getTitle());
               System.out.println("\nDescription :-");
               x.getDescription().forEach(j->System.out.println(j));
               System.out.println("\nInput :\t\t"+x.getInput());
               System.out.println("\nOutput :\t"+x.getOutput());
               System.out.println("\nSample Input :-");
               x.getSampleInput().forEach(j->System.out.println(j));
               System.out.println("\nSample Output :-");
               x.getSampleOutput().forEach(j->System.out.println(j));
               System.out.println("\nCredit : "+x.getCredit());
               System.out.println("Do you want to save ?\n1.Yes\n0.No");
               int i;
               for(i=sc.nextInt();i<0||i>1;i=sc.nextInt())
                    System.out.println("invalid!!");
               if(i==1)
                    ProgramDetails.writeIntProgramDetail(x);
          } catch (IOException ex) {
               System.out.println("Error :- "+ex.getMessage());
          }
     }
     
     public static void showProgramDetail(){
          try {
               Path dir=getDefaultProDir();
               System.out.println("Program Directory = " + dir);
               System.out.println("enter file name = ");
               String s="";
               for(s=sc.nextLine();s.trim().isEmpty();s=sc.nextLine());
               IntProgramDetail x=ProgramDetails.readProgramDetail(dir.resolve(s));
               System.out.println("\nProgram Id :\t"+x.getProgramID());
               System.out.println("\nTitle :\t\t"+x.getTitle());
               System.out.println("\nDescription :-");
               x.getDescription().forEach(j->System.out.println(j));
               System.out.println("\nInput :\t\t"+x.getInput());
               System.out.println("\nOutput :\t"+x.getOutput());
               System.out.println("\nSample Input :-");
               x.getSampleInput().forEach(j->System.out.println(j));
               System.out.println("\nSample Output :-");
               x.getSampleOutput().forEach(j->System.out.println(j));
               System.out.println("\nCredit : "+x.getCredit());
               System.out.println("Do you want to save ?\n1.Yes\n0.No");
               int i;
               for(i=sc.nextInt();i<0||i>1;i=sc.nextInt())
                    System.out.println("invalid!!");
               if(i==1)
                    System.out.println(ProgramDetails.writeToTxt(x));
          } catch (IOException ex) {
               System.out.println("Error :- "+ex.getMessage());
          }
     }
     
     public static void showDefaultPath(){
          System.out.println("Program Directory = " +getDefaultProDir());
     }
}
