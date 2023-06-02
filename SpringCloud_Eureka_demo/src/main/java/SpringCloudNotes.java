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
     *      【搭建eureka-server】创建eureka-server服务；
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
     * 【服务注册】
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
     * */


}
