package sparta.seed.campaign.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import sparta.seed.community.service.SlangService;

@RestController
@RequiredArgsConstructor
public class slangController {
	private final SlangService slangService;

	@PostMapping("/api/admin/slang")
	public String addSlang(){
		slangService.addSlang();
		return "비속어 레디스 주입 완료.";
	}
}
