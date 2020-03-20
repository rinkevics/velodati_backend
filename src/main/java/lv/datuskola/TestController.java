package lv.datuskola;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.transaction.Transactional;
import java.util.Date;

@Controller
public class TestController {

    @Autowired
    private TaskExecutor taskExecutor;

    @GetMapping(value="/test")
    @Transactional
    public String abc() throws Exception {

        for (int i = 0; i < 10; i++) {
            fun();
        }

        return "test";
    }

    private void fun() {
        taskExecutor.execute(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("test");
        });
    }
}
