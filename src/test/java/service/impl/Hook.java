package service.impl;

import service.IHook;

public class Hook implements IHook {

    @Override
    public void test() {
        System.out.println("1");
    }
}
