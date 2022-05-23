package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController /* @Controller @ResponseBody 혼합 */
@RequiredArgsConstructor
public class MemberApiController {

        private final MemberService memberService;

        @PostMapping("/api/v1/members")
        public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){ //@RequestBody --> Json으로 온 Body를 Member객체로 다 바꿔줌
            Long id = memberService.join(member);
            return new CreateMemberResponse(id);

        }

    /**
     * 엔티티 스펙 변경되도 API 스펙 변경할 필요 없음(CreateMemberRequest DTO 생성)
     */
    @PostMapping("/api/v2/members")
        public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){
            Member member = new Member();
            member.setName(request.getName());
            Long id = memberService.join(member);
            return new CreateMemberResponse(id);
        }

        @Data
        static class CreateMemberRequest{
            @NotEmpty //CreateMemberRequest DTO에서 처리 하는게 정석
            private String name;
        }

        @Data
        static class CreateMemberResponse{
            private Long id;

            //id 생성자
            public CreateMemberResponse(Long id) {
                this.id = id;
            }
        }



}
