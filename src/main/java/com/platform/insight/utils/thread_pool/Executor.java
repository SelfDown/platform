package com.platform.insight.utils.thread_pool;

import com.platform.insight.utils.EnvUtils;
import com.platform.insight.utils.thread_pool.ssh.connect.ServerPortConnect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class Executor {
    public static final String SERVER_PORT_CONNECT = "server_port_connect";
//    private static ThreadPoolExecutor executor = null;
//    private  static Integer executor_pool_num;
    List<Callable> item_list = new ArrayList<>();
    private Integer pool_num=0;
    private Integer wait = 0;
    // todo 这个里通过方射，动态获取多线程执行的实现类，为了避免以后无限个if 判断，值从配置文件中获取
    public Executor(String type,List<Map<String,Object>> list,Integer pool_num,Integer wait){
        if(SERVER_PORT_CONNECT.equals(type)){
           for (Map<String,Object> item :list){
               ServerPortConnect serverPortConnect = new ServerPortConnect(item);
               item_list.add(serverPortConnect);

           }
        }
        this.pool_num = pool_num;
        this.wait = wait;
    }
    private  ThreadPoolExecutor initExecutors(Integer pool_num){
        ThreadPoolExecutor executor = null;


        if (executor != null){
            executor.shutdown();
        }

        if (pool_num ==0){
            executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        }else {
            executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(pool_num);
        }

        return executor;
    }
//    private static void  destroyExecutors(){
//        executor.shutdown();
//        executor = null;
//        executor_pool_num = 0;
//    }

    //todo 实现一个消息接口，将消息返回给，队列进行查看
    public List<Map<String,Object>> execute(){

        ThreadPoolExecutor executor = initExecutors(this.pool_num);
        List<Future<Map<String,Object>>> resultList = new ArrayList<>();
        for(Callable item:item_list){
            Future<Map<String,Object>> res = executor.submit(item);
            resultList.add(res);
        }
        int index=0;
        do {
//            System.out.printf("number of completed tasks: %d\n", executor.getCompletedTaskCount());
            for (int i = 0; i < resultList.size(); i++) {
                Future<Map<String, Object>> result = resultList.get(i);
            }
            try {
                TimeUnit.MILLISECONDS.sleep(this.wait);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Long total = Long.parseLong(resultList.size()+"");
            Long complete = executor.getCompletedTaskCount();
            Long per = (complete*100)/total;

            if(EnvUtils.isDebugger()){
                index++;
                String msg = String.format("第 %s 轮 ,总共 %s 个任务，完成 %s。%s ",index,total,complete, per.floatValue());
                System.out.println(msg+"%");
            }
        } while (executor.getCompletedTaskCount() < resultList.size());

        List<Map<String,Object>> list = new ArrayList<>();
        for (int i = 0; i < resultList.size(); i++) {
            Future<Map<String, Object>> result = resultList.get(i);
            try {
                list.add(result.get());// blocking method
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }
        executor.setKeepAliveTime(0L,TimeUnit.MILLISECONDS);

        executor.purge();
        executor.shutdownNow();

        executor = null;



        return list;

    }
}
