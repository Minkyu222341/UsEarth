package sparta.seed.community.domain.dto.requestdto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class CommentRequestDto {
	private String content;
	private boolean delete;
	private String changeNickname;
}
