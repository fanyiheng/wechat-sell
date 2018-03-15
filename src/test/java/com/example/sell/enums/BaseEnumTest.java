package com.example.sell.enums;

import org.junit.Test;

import static org.junit.Assert.*;

public class BaseEnumTest {

    @Test
    public void fromCode() {
        PayStatus payStatus = BaseEnum.fromCode(0, PayStatus.class);
        System.out.println(payStatus);
    }
}
