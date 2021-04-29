package LWJ.hellospring;

import LWJ.hellospring.repository.MemberRepository;
import LWJ.hellospring.repository.MemoryMemberRepository;
import LWJ.hellospring.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }
}
