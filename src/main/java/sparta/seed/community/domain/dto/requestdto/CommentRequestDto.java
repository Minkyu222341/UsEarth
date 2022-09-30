package sparta.seed.community.domain.dto.requestdto;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class CommentRequestDto {
	@NotEmpty(message = "내용을 입력해주세요.")
	private String content;
	private boolean delete;
	private String changeNickname;
}
