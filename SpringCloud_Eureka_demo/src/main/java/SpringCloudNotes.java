public class SpringCloudNotes {

    /*
     * 微服务调用方式
     *        1.基于RestTemplate发起的http请求实现远程调用;
     *           1.1注册一个RestTemplate的实例到Spring容器,在需要注入的服务的配置类中使用@Bean注入;
     *               // SpringBoot启动类是一个配置类。在项目的配置类中声明一个Bean对象。
     *               @Bean
     *               public RestTemplate restTemplate(){
     *                   return new RestTemplate();
     *               }
     *           1.2 在需要使用的Service层中注入RestTemplate对象，后续进行调用。
     *
     *        2.http请求做远程调用是与语言无关的调用，只要知道对方的ip、端口、接口路径、请求参数即可。
     *
     * */

    /*
     *
     * Eureka注册中心
     *   SpringCloud中的注册中心来解决，其中最广为人知的注册中心就是Eureka。
     *   在Eureka架构中，微服务角色有两类：
     *
     *   1.EurekaServer：服务端，注册中心；
     *       1.1.记录服务信息；
     *       1.2.心跳监控；
     *   2.EurekaClient：客户端。这个就是每一个服务提供者中要；
     *       2.1.Provider：服务提供者，例如案例中的 user-service；
     *           2.1.1.注册自己的信息到EurekaServer；
     *           2.1.2.每隔30秒向EurekaServer发送心跳；
     *       2.2.consumer：服务消费者，例如案例中的 order-service；
     *           2.2.1.根据服务名称从EurekaServer拉取服务列表；
     *           2.2.2.基于服务列表做负载均衡，选中一个微服务后发起远程调用。
     *
     *   【搭建eureka-server】：
     *       注册中心服务端：eureka-server，这必须是一个独立的微服务；
     *      【搭建eureka-server】创建eureka-server服务【在父项目下面新建一个子服务】；
     *           1-1.在父工程下，创建一个子模块eureka-server；
     *           1-2.在子模块eureka-server的pom文件中引入SpringCloud为eureka提供的starter依赖<artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>；
     *           1-3.给eureka-server服务编写一个启动类，一定要添加一个@EnableEurekaServer注解，开启eureka的注册中心功能；
     *              import org.springframework.boot.SpringApplication;
     *              import org.springframework.boot.autoconfigure.SpringBootApplication;
     *              import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
     *              @SpringBootApplication
     *              @EnableEurekaServer
     *              public class EurekaApplication {
     *                  public static void main(String[] args) {
     *                      SpringApplication.run(EurekaApplication.class, args);
     *                  }
     *              }
     *
     *           1-4.编写配置文件application.yml文件；注意yml格式文件的缩进
     *              server:
     *                port: 10086 # 服务端口
     *              spring:
     *                  application:
     *                      name: eurekaserver # eureka的服务名称
     *              eureka:
     *                  client:
     *                      service-url:  # eureka的地址信息
     *                          defaultZone: http://127.0.0.1:10086/eureka
     *              #           defaultZone: http://localhost:10086/eureka
     *
     *
     * 【Eureka服务注册】
     *      1.在需要注册的服务中引入依赖：在需要注册的服务的pom文件中引入<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>；
     *      2.修改需要注册的服务的application.yml文件，添加服务名称、eureka地址
     *          spring:
     *              application:
     *                  name: userservice # orderservice的服务名称
     *          eureka:
     *              client:
     *                  service-url:  # eureka的地址信息
     *                      defaultZone: http://127.0.0.1:10086/eureka
     *
     *
     *
     * 【Eureka服务发现】
     *      1）引入依赖:eureka服务发现、服务注册统一都封装在eureka-client依赖,服务注册时已经做了；
     * :<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>;
     *      2）配置文件：服务发现也需要知道eureka地址，因此第二步与服务注册一致，都是配置eureka信息
     *      3）服务拉取和负载均衡：在服务消费者的配置类【服务消费者启动类】中给给RestTemplate这个Bean添加一个@LoadBalanced注解；
     *      4）在服务消费者的方法中，修改访问的url路径，使用服务提供者的服务名代替ip和端口。
     *
     *
     *
     * */



    /*
    *
    * Ribbon负载均衡
    *   负载均衡原理：SpringCloud底层其实是利用了一个名为Ribbon的组件，来实现负载均衡功能的。
    *   SpringCloudRibbon的底层采用了一个拦截器，拦截了RestTemplate发出的请求，对地址做了修改。
    *
    *   负载均衡策略
    *       负载均衡的规则都定义在IRule接口中。
    *       RoundRobinRule:简单轮询服务列表来选择服务器。它是Ribbon默认的负载均衡规则。
    *       ZoneAvoidanceRule:以区域可用的服务器为基础进行服务器的选择。使用Zone对服务器进行分类，这个Zone可以理解为一个机房、一个机架等。而后再对Zone内的多个服务做轮询。
    *       默认的实现就是ZoneAvoidanceRule，是一种轮询方案。
    *
    *   自定义负载均衡策略
    *       通过定义IRule实现可以修改负载均衡规则，有两种方式：
    *       【自定义负载均衡策略方式一】：在服务消费者的配置类【启动类】中定义一个新的IRule:
    *           @Bean
    *           public IRule randomRule(){
    *               return new RandomRule();
    *           }
    *
    *       【自定义负载均衡策略方式二】：在服务消费者的配置文件application.yml文件中，添加有关服务提供者的新的配置
    *       服务提供者: # 给某个微服务配置负载均衡规则，这里是userservice服务
    *           ribbon:
    *               NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule # 负载均衡规则
    *
    *
    *
    * */


}
