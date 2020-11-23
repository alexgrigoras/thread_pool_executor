# Thread Pool Executor

## Description
Thread pool executor implementation, similar to the one present in Java platform: java.util.concurrent.ThreadPoolExecutor.

## Implementation
The application is built using Java. The component will accept for execution tasks of type java.lang.Runnable.
The input parameters are:
- int corePoolSize
- int maximumPoolSize
- int keepAliveTime (resolution = 1 second)
- int queueSize

## License
The application is licensed under the MIT license.