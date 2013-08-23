/*
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
*/
//这几种方式都可以向context中传入值,类似一个map容器,context是:screen widget处理器放置一个MapStack的实例
/*def blah = "blih";
context.put("blah",blah);*/
//context.blah = "blih";
context.put("blah","blih");
passMsg = "Condition passed. Showing widgets element." + 

           "  Blah is: " + context.get("blah") + 

           "  show is: " + parameters.get("show");

context.put("passMsg",passMsg);

