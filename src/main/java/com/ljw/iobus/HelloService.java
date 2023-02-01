package com.ljw.iobus;

import com.ljw.iobus.SayHelloIn;
import com.ljw.iobus.SayHelloOut;

public interface HelloService {

    public SayHelloOut sayHello(SayHelloIn Input,String name);

}
