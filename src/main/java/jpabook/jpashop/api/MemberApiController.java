package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
     * 멤버 등록 API
     * 엔티티 스펙 변경되도 API 스펙 변경할 필요 없음 (CreateMemberRequest DTO 생성)
     */
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){
        Member member = new Member();
        member.setName(request.getName());
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /**
     * 멤버 수정 API
     */
    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id, @RequestBody @Valid UpdateMemberRequest request){
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id); //업데이트 이후 다시 찾는다.
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    // 멤버등록 DTO //
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

    // 멤버수정 DTO //
    @Data
    static class UpdateMemberRequest{
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse{
        private Long id;
        private String name;
    }


}
