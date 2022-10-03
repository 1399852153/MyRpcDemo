package service;

public class HelloServiceImpl implements HelloService{

    @Override
    public void sayHello() {
        System.out.println("HelloService sayHello!");
    }

    @Override
    public String echo(String message) {
        return "server echo: " + message;
    }
}
