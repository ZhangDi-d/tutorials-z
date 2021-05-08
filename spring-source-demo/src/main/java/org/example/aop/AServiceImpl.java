package org.example.aop;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author dizhang
 * @date 2021-05-06
 */
@Service
public class AServiceImpl implements AService{

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void a() {
        //需要配合 @EnableAspectJAutoProxy(exposeProxy = true) 使用
        ((AService)AopContext.currentProxy()).b();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void b() {

    }
}
