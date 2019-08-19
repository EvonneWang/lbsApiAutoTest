package service.impl;

import service.IHook;

public class Hook2 implements IHook {
    @Override
    public void test() {
        System.out.println("2");
    }
}
