package com.example.beanpostprocessor;

import com.example.beanpostprocessor.component.Bean1;
import com.example.beanpostprocessor.component.Bean2;
import com.example.beanpostprocessor.component.Bean3;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.StandardEnvironment;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author : KaelvihN
 * @date : 2023/8/13 0:28
 */
public class TestAutoAnnotationBeanPostProcessor {
    public static void main(String[] args) {
//        testTotalProcess();
        try {
            testPostProcessProperties();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void testTotalProcess() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        //通过这种方式注册bean会使bean跳过 instantiate ，dependencies injected ，init 阶段
        beanFactory.registerSingleton("bean2", new Bean2());
        beanFactory.registerSingleton("bean3", new Bean3());
        //解析@Value
        beanFactory.setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());
        //解析${}
        beanFactory.addEmbeddedValueResolver(new StandardEnvironment()::resolvePlaceholders);
        //查看哪一个方法或属性 添加了@Autowired，这被称为InjectionMetadata
        AutowiredAnnotationBeanPostProcessor beanPostProcessor =
                new AutowiredAnnotationBeanPostProcessor();
        //依赖注入时，需要去去获取Bean的性息
        beanPostProcessor.setBeanFactory(beanFactory);
        //依赖注入前
        Bean1 bean1 = new Bean1();
        System.out.println(">>>依赖注入前" + bean1);
        //依赖注入
        beanPostProcessor.postProcessProperties(null, bean1, "bean1");
        System.out.println(">>>依赖注入后" + bean1);
    }

    public static void testPostProcessProperties() throws Throwable {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        //通过这种方式注册bean会使bean跳过 instantiate ，dependencies injected ，init 阶段
        beanFactory.registerSingleton("bean2", new Bean2());
        beanFactory.registerSingleton("bean3", new Bean3());
        //解析@Value
        beanFactory.setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());
        //解析${}
        beanFactory.addEmbeddedValueResolver(new StandardEnvironment()::resolvePlaceholders);
        //查看哪一个方法或属性 添加了@Autowired，这被称为InjectionMetadata
        AutowiredAnnotationBeanPostProcessor beanPostProcessor =
                new AutowiredAnnotationBeanPostProcessor();
        //依赖注入时，需要去去获取Bean的性息
        beanPostProcessor.setBeanFactory(beanFactory);
        //依赖注入前
        Bean1 bean1 = new Bean1();
        System.out.println(">>>依赖注入前" + bean1);
        //模拟依赖注入
        //反射获取AutowiredAnnotationBeanPostProcessor的findAutowiringMetadata方法
        Method method =
                AutowiredAnnotationBeanPostProcessor.class
                        .getDeclaredMethod("findAutowiringMetadata", String.class, Class.class, PropertyValues.class);
        method.setAccessible(true);
        //反射调用findAutowiringMetadata方法，获取Bean1上加了@Autowired的属性和方法
        InjectionMetadata metadata =
                (InjectionMetadata) method.invoke(beanPostProcessor, "bean1", Bean1.class, null);
        //依赖注入
        metadata.inject(bean1, "bean1", null);
        System.out.println("bean1 = " + bean1);
        //获取bean1中的bean3
        Field bean3 = Bean1.class.getDeclaredField("bean3");
        DependencyDescriptor dd3 = new DependencyDescriptor(bean3, false);
        Object b3 = beanFactory.doResolveDependency(dd3, null, null, null);
        System.out.println("b3 = " + b3);

        //获取bean1中的bean2
        Method setBean2 = Bean1.class.getDeclaredMethod("setBean2", Bean2.class);
        DependencyDescriptor dd2 =
                new DependencyDescriptor(new MethodParameter(setBean2,0), false);
        Object b2 = beanFactory.doResolveDependency(dd2, null, null, null);
        System.out.println("b2 = " + b2);
    }
}
