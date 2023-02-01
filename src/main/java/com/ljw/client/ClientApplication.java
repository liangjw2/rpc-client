package com.ljw.client;

import com.ljw.client.util.SysUtil;
import com.ljw.iobus.HelloService;
import com.ljw.iobus.SayHelloIn;
import com.ljw.iobus.SayHelloOut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//@SpringBootApplication
public class ClientApplication {

    private static Logger logger = LoggerFactory.getLogger(ClientApplication.class);

    public static void main(String[] args) {

        //SpringApplication.run(ClientApplication.class, args);
        Initialize.initClient();
        HelloService Service = SysUtil.getRemoteInstance(HelloService.class);
        //System.out.println(Service.sayHello("liangjw"));
        SayHelloIn Input = new SayHelloIn();
        Input.setName("liangjw");
        SayHelloOut Output = Service.sayHello(Input,"liangjw2");
        System.out.println(Output.getReply());

    }
}
