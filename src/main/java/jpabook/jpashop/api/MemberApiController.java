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
import java.util.List;
import java.util.stream.Collectors;

@RestController /* @Controller @ResponseBody 혼합 */
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public List<Member> membersV1(){
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public Result memberV2(){
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());// Java 8(List Member --> List Dto 로 변경)

        return new Result(collect.size(), collect); //Result값(Array --> Json) 형태로 변경 [반환 데이터 추가시 유용]
    }

    /**
     * List 호출시 JsonArray로 나가게 되므로 Custom해서 한번 감싸준다(유지 보수 향상)
     * Ex) Count 값 추가시 배열이 감싸고 있으면 수정이 어려움
     */
    @Data
    @AllArgsConstructor
    static class Result<T>{
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String name;
    }


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
