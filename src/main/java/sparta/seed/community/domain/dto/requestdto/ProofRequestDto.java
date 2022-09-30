package sparta.seed.community.domain.dto.requestdto;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class ProofRequestDto {
	@NotEmpty(message = "제목을 입력해 주세요")
	private String title;
	@NotEmpty(message = "내용을 입력해 주세요.")
	private String content;
	private Long[] imgIdList;
	private String changeNickname;
}
