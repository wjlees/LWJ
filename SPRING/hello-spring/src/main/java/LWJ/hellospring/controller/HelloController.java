package LWJ.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {
    @GetMapping("hello") // localhost:8080/hello 에서의 hello 를 의미함.
    public String hello(Model model) {
        model.addAttribute("data", "hello!!");
        return "hello"; // templates/hello.html 를 찾아감.
    }

    @GetMapping("hello-mvc")
    public String helloMVC(@RequestParam("name") String name, Model model) {
        // RequestParam"name" 으로 받는건 주소에서 들어옴.
        // localhost:8080/hello-mvc?name=LWJ => 여기에서 ?name=LWJ 부분.
        // 이 name(LWJ) 을 동일하게 value에 담아서 model에 담아서 보냄
        model.addAttribute("name", name);
        return "hello-template";
    }

    @GetMapping("hello-string")
    @ResponseBody // html에 body 부분에 이걸 직접 넣어주겠다는 의미
    public String helloString(@RequestParam("name") String name) {
        return "hello " + name;
        // 이건 view 이런게 없음. 그냥 문자가 그대로 내려감.
        // 그래서 소스보기 하면 html 같은거 없이 진짜 그냥 그대로 내려감. (소스보기 하면 그냥 hello LWJ 로 나올 것)
    }

    @GetMapping("hello-api")
    @ResponseBody // 객체를 반환하면 기본으로 json으로 반환함.
    public Hello helloApi(@RequestParam("name") String name) {
        Hello hello = new Hello();
        hello.setName(name);
        return hello;
    }

    static class Hello {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
