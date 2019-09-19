package quoters;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class ProfilingHanglerBeanPostProcessor implements BeanPostProcessor {

  private Map<String, Class> map = new HashMap<>();
  private ProfilingController controller = new ProfilingController();

  public ProfilingHanglerBeanPostProcessor() throws Exception {
    MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
    platformMBeanServer.registerMBean(controller, new ObjectName("profiling", "name", "controller"));
  }

  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    Class<?> beanClass = bean.getClass();
    if (beanClass.isAnnotationPresent(Profiling.class)) {
      map.put(beanName, beanClass);
    }

    return bean;
  }

  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    Class<?> beanClass = map.get(beanName);
    if (beanClass != null) {
      return Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(), new InvocationHandler() {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
          if (controller.isEnabled()) {
            System.out.println("Profiling...");
            long before = System.nanoTime();
            Object relVal = method.invoke(bean, args);
            long after = System.nanoTime();
            System.out.println(after - before);
            System.out.println("Finish!");
            return relVal;
          }

          return method.invoke(bean, args);
        }
      });
    }

    return bean;
  }
}
