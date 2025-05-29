package com.esdllm.catFriend.once;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.esdllm.catFriend.model.User;
import com.esdllm.catFriend.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * 导入数据类
 */

@Component
public class InsertUsers {
    @Resource
    private UserService userService;

    /**
     * 批量插入用户
     */
//    @Scheduled(initialDelay = 5000, fixedDelay = Long.MAX_VALUE)
    public void doInsert() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int batchSize = 10000;
        final int INSERT_NUM = 3700000;
        int j = 0;
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int i = 0; i < (INSERT_NUM-j) / batchSize; i++) {
            List<User> users = new ArrayList<>();
            do {
                j++;
                User user = new User();
                user.setUsername("假流浪猫" + j);
                user.setUserAccount("fakeEsdllm" + j);
                user.setAvatarUrl("https://tse3-mm.cn.bing.net/th/id/OIP-C.S1JdG74RGGOHcilL6Og6dAAAAA?w=206&h=206&c=7&r=0&o=5&dpr=2&pid=1.7");
                user.setGender(0);
                user.setUserPassword("12345678");
                user.setPhone("123" + j);
                user.setEmail("123" + j + "@esdllm.com");
                user.setUserStatus(0);
                user.setUserRole(0);
                user.setInspectionCode("" + (14 + j));
                user.setProfile("我是测试用户" + j);
                user.setTags("[]");
                users.add(user);
            } while (j % batchSize != 0);
            //异步执行
            CompletableFuture<Void> future = CompletableFuture.runAsync(()->{
                System.out.println("ThreadName:" + Thread.currentThread().getName());
                userService.saveBatch(users, batchSize);
            });
            futures.add(future);
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[]{})).join();

        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        System.out.println("<UNK>"+totalTimeMillis+"<UNK>");
    }
}
