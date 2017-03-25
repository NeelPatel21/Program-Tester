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
package lib.userModule.result;

import lib.userModule.TimeNotAvailableException;

/**
 *
 * @author Neel Patel
 */
public class ResultAdapter implements IntResult{
     final long runTime;
     final int code;
     final String message;
     ResultAdapter(long time,int code,String message){
          this.message=message;
          this.runTime=time;
          this.code=code;
     }
     @Override
     public long getRunTime() throws TimeNotAvailableException {
          return runTime;
     }

     @Override
     public String getMessage() {
          return message;
     }

     @Override
     public int getMessageCode() {
          return code;
     }
     
}
