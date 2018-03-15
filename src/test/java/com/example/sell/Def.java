package com.example.sell;

import com.example.sell.exception.ExceptionCode;
import com.example.sell.exception.SellException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;


//@RunWith(SpringRunner.class)
//@SpringBootTest
@Slf4j
public class Def {

    @Test
    public  void main(){
        log.info("77777");
        SellException sellException = new SellException(ExceptionCode.PRODUCT_NOT_EXIST);
        log.error("except={}","test",sellException);
    }
}
