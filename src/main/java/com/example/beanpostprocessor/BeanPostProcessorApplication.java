package com.example.beanpostprocessor;

import com.example.beanpostprocessor.component.Bean1;
import com.example.beanpostprocessor.component.Bean2;
import com.example.beanpostprocessor.component.Bean3;
import com.example.beanpostprocessor.component.Bean4;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.context.support.GenericApplicationContext;


public class BeanPostProcessorApplication {
    public static void main(String[] args) {
        /**
         * 这是一个相对“干净"的容器
         * 即没有加Bean后处理器，BeanFactory后处理器
         */
        GenericApplicationContext context = new GenericApplicationContext();
        //注册三个Bean
        context.registerBean(Bean1.class);
        context.registerBean(Bean2.class);
        context.registerBean(Bean3.class);
        context.registerBean(Bean4.class);
        //---snip---
        //添加Bean后置处理器
        context.registerBean(AutowiredAnnotationBeanPostProcessor.class);
        //添加一个解析器，用于解析@Value 的值注入(与AutowiredAnnotationBeanPostProcessor配置使用)
        context.getDefaultListableBeanFactory()
                .setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());
        //---snip---
        //添加Bean后置处理器
        context.registerBean(CommonAnnotationBeanPostProcessor.class);
        //---snip---
        ConfigurationPropertiesBindingPostProcessor.register(context.getDefaultListableBeanFactory());
        //---snip---
        /**
         * 初始化容器
         * 1.执行BeanFactory的后置处理器
         * 2.添加Bean的后置处理器
         * 3.初始化所有单例
         */
        context.refresh();
        //---snip---
        System.out.println(context.getBean(Bean4.class));
        //--snip---
        //销毁容器
        context.close();
    }
}
