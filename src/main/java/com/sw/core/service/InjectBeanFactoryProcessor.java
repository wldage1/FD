package com.sw.core.service;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * Inject Sevice 
 * Just used for injecting diffrent database type sevice class attributes
 * @author JING
 */

public class InjectBeanFactoryProcessor implements BeanFactoryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory configurablelistablebeanfactory) throws BeansException {
		for(String name : configurablelistablebeanfactory.getBeanDefinitionNames()){
		System.out.println("---------"+name);
		}
		
	}

//	@Override
//	public Object postProcessAfterInitialization(Object arg0, String arg1) throws BeansException {
//		System.out.println("--------bean:"+arg0.getClass().getSimpleName());
//		System.out.println("--------name:"+arg1);
//		return arg0;
//	}
//
//	@Override
//	public Object postProcessBeforeInitialization(Object arg0, String arg1) throws BeansException {
//		// TODO Auto-generated method stub
//		return arg0;
//	}

//	@Override
//	public Object postProcessAfterInitialization(Object bean, String name) throws BeansException {
//		System.out.println("--------bean:"+bean.getClass().getSimpleName());
////		System.out.println("--------name:"+name);
//		if (bean.getClass().getSuperclass().getSimpleName().contains("UserService")) { 
//			Field[] fields = bean.getClass().getSuperclass().getFields();
//			System.out.println("--------UserService-------------:"+fields.toString());
//		}
////		Field[] fields = bean.getClass().getDeclaredFields();
////		try{
////			for(int i=0;i<fields.length;i++){
////				if (fields[i].isAnnotationPresent(Iinject.class)) {
////					Iinject ij = fields[i].getAnnotation(Iinject.class);
////					String dataType = ij.name();
////					if ("".equals(dataType)) {
////						dataType = Constant.DEFAULT_DATABASE_TYPE;
////					}
////					// 查找同类的对象实例
////					String[] beanNames = super.getApplicationContext().getBeanNamesForType(fields[i].getType());
////					String targetNeanName = null;
////					for (String bn : beanNames) {
////						if (bn.contains(dataType)) {
////							targetNeanName = bn;
////							break;
////						}
////					}
////					if (targetNeanName == null) {
////						continue;
////					}
////					Object targetBean = super.getApplicationContext().getBean(targetNeanName);
////					// 保存当前field的修饰类型
////					boolean flag = fields[i].isAccessible();
////					// 强制field 可以访问
////					fields[i].setAccessible(true);
////					// 注入实例
////					fields[i].set(bean, targetBean);
////					// 还原当前field 的修饰类型，避免被不非法使用
////					fields[i].setAccessible(flag);
////				}
////			}
////		}catch(IllegalAccessException e ){
////			e.printStackTrace();
////		}
//		return bean;
//	}
//
//	@Override
//	public Object postProcessBeforeInitialization(Object arg0, String arg1) throws BeansException {
//		// TODO Auto-generated method stub
//		return arg0;
//	}

//	/**自定义属性注入
//	 * @param obj
//	 * @throws Exception
//	 */
//	public void injectAttribute(Object obj) throws Exception {
//		System.out.println(obj.getClass().getSimpleName()+"SU:"+obj.getClass().getSuperclass().getSimpleName());
//		Field[] fields = obj.getClass().getFields();
//		for (Field f : fields) {
//			if (f.isAnnotationPresent(Iinject.class)) {
//				Iinject ij = f.getAnnotation(Iinject.class);
//				String name = ij.name();
//				if ("".equals(name)) {
//					name = Constant.DEFAULT_DATABASE_TYPE;
//				}
//				// 查找同类的对象实例
//				String[] beanNames = super.getApplicationContext().getBeanNamesForType(f.getType());
//				String targetNeanName = null;
//				for (String bn : beanNames) {
//					if (bn.contains(name)) {
//						targetNeanName = bn;
//						break;
//					}
//				}
//				if (targetNeanName == null) {
//					continue;
//				}
//				Object targetBean = super.getApplicationContext().getBean(targetNeanName);
//				// 保存当前field的修饰类型
//				boolean flag = f.isAccessible();
//				// 强制field 可以访问
//				f.setAccessible(true);
//				// 注入实例
//				f.set(obj, targetBean);
//				// 还原当前field 的修饰类型，避免被不非法使用
//				f.setAccessible(flag);
//			}
//		}
//	}
	
}
