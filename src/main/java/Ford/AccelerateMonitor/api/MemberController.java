package Ford.AccelerateMonitor.api;

import Ford.AccelerateMonitor.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import Ford.AccelerateMonitor.model.Member;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:8081", "http://35.9.22.64:8888/", "http://35.9.22.89:8888/", "http://35.9.22.63:8888/", "http://accmonitor.com/"})
@RequestMapping("members")
@RestController
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/addMember")
    public void addMember(@RequestBody Member member) { memberService.addMember(member); }

    @GetMapping("/getAllMembers")
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping(path = "/getMember/{id}")
    public Member getMember(@PathVariable("id") String id){ return memberService.getMember(id); }

    /// get member by email
    @GetMapping(path = "/getByEmail/{email}")
    public Member getByEmail(@PathVariable("email") String email){ return memberService.getByEmail(email); }

    @DeleteMapping(path = "/deleteMember/{id}")
    public void deleteMember(@PathVariable("id") String id){ memberService.deleteMember(id); }

    @PutMapping(path = "/updateMember/{id}")
    public void updateMember(@PathVariable("id") String id, @RequestBody Member member) { memberService.updateMember(id, member);}

    @PutMapping(path = "/addTeam/{id}")
    public void addTeam(@PathVariable("id") String id, @RequestBody Map<String,String> team){ memberService.addTeam(id, team.get("team")); }

    @PutMapping(path="/removeTeam/{id}")
    public void removeTeam(@PathVariable("id") String id, @RequestBody Map<String,String> team){ memberService.removeTeam(id, team.get("team")); }


}