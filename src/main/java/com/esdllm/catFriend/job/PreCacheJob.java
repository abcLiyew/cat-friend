package com.esdllm.catFriend.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.esdllm.catFriend.constant.UserConstant;
import com.esdllm.catFriend.model.User;
import com.esdllm.catFriend.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 预缓存任务
 */
@Slf4j
@Component
public class PreCacheJob {
    @Resource
    private UserService  userService;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    //  重点用户
    private final List<Long> mainUserIdList = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L);
    @Scheduled(cron = "0 00 03 * * ? ")
    public void doCacheRecommendUser() {
        RLock lock = redissonClient.getLock("catFriend:preCacheJob:doCache:lock");
        try {
            //只有一个线程获取到锁，才执行
            if (lock.tryLock(0,-1, TimeUnit.MILLISECONDS)) {
                System.out.println("lock.getName() = " + lock.getName());
                System.out.println("Thread.currentThread().getName(),ID = " + Thread.currentThread().getName()+","+Thread.currentThread().getId());
                ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();

                for (Long userId : mainUserIdList) {
                    String redisKey = UserConstant.redisKeyUser(userId);
                    //查数据库
                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                    Page<User> userPage = userService.page(new Page<>(1,20),queryWrapper);
                    //缓存数据
                    try {
                        opsForValue.set(redisKey,userPage,1000*60*60*24, TimeUnit.MILLISECONDS);
                    }catch (Exception e){
                        log.error("redis set key error",e);
                    }
                }
            }
        } catch (InterruptedException e) {
            log.error("doCacheRecommendUser error",e);
        }finally {
            //只能释放当前线程的锁
            if (lock.isHeldByCurrentThread()) {
                System.out.println("unlock.getName() = " + lock.getName());
                lock.unlock();
            }
        }
    }
}
