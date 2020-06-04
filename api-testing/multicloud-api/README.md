# Multi cloud API Automation

**Following tool you can use for this test automation**

 Intellij idea community edition [link](https://www.jetbrains.com/idea/download/#section=windows)

**How to import code in intellij idea community edition?**
 
 Clicked on File > Open
 
 **How to set environment variable in intellij?**
   
   Before test execute setup this environment variables select [Run | Edit Configuration](https://www.jetbrains.com/help/idea/creating-run-debug-configuration-for-tests.html) from the main menu and set variables.
   
   For CreateBucketBackendTest and LifecycleTests set following variable <br/>
  `HOST_IP=***.***.*.***;INPUT_PATH=D:/SODA-Test (Your Project Folder Path);API_SERVER_PORT=:****;S3_API_PORT=:****`
   
   For MigrationTests set following variable <br/>
  `HOST_IP=***.***.*.***;INPUT_PATH=D:/SODA-Test (Your Project Folder Path);API_SERVER_PORT=:****;S3_API_PORT=:****;SCHEDULE_TIME=00 00 07 29 5 5`
